import socket, threading, random, sqlite3
from base64 import b64encode, b64decode
from Crypto.Cipher import DES


# Configurações do SOCKET
HEADER = 64
PORT = 3001
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = "utf-8"
SEPARADOR = "\n"

CHAVE_TGS = "d486dfbd"
IV = "bomuoN9Q4P4="

#Inicialização do Socket
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)


def cifra(chave: str, msg: str) -> str:
    des = DES.new(chave.encode(FORMAT), DES.MODE_OFB, iv=b64decode(IV))
    msg_cifrada = b64encode(des.encrypt(msg.encode(FORMAT))).decode(FORMAT)
    return msg_cifrada


def decifra(chave: str, msg_cifrada: str) -> str:
    des = DES.new(chave.encode(FORMAT), DES.MODE_OFB, iv=b64decode(IV))
    msg_decifrada = des.decrypt(b64decode(msg_cifrada)).decode(FORMAT)
    return msg_decifrada


def prepara_resposta(numero_randomico: str, chave_cliente: str, id_cliente: str, validade: str) -> bytes:
    chave_sessao_tgs = random.randbytes(8).hex()[0:8]
    parte1 = f"{chave_sessao_tgs}{SEPARADOR}{numero_randomico}"
    parte1_cifra = cifra(chave_cliente, parte1)
    parte2 = f"{id_cliente}{SEPARADOR}{validade}{SEPARADOR}{chave_sessao_tgs}"
    parte2_cifra = cifra(CHAVE_TGS, parte2)
    return f"{parte1_cifra}{SEPARADOR}{parte2_cifra}".encode(FORMAT)


def recebe_mensagem(msg: str) -> tuple[str, str, str, str]:
    dados_msg = msg.split(SEPARADOR)

    con = sqlite3.connect("as.db")
    chave_cliente = con.cursor().execute("SELECT chave FROM usuario WHERE nome = ?", [dados_msg[0]]).fetchone()[0]
    con.close()

    try:
        msg_cliente = decifra(chave_cliente, dados_msg[1])
        msg_cliente_split = msg_cliente.split(SEPARADOR)
        
        return msg_cliente_split[2], chave_cliente, dados_msg[0], msg_cliente_split[1], True
    except:
        return "", "", "", "", False


# Trata cada conexão nova em Thread separada
def trata_cliente(conn: socket.socket, addr: socket.AddressFamily):
    print(f"[NOVA CONEXÃO] {addr} conectou")
    connected = True
    while connected:
        # Pegando o HEADER com o tamanho da mensagem
        tamanho_msg = conn.recv(HEADER).decode(FORMAT)
        if tamanho_msg:
            tamanho_msg = int(tamanho_msg)
            msg = conn.recv(tamanho_msg).decode(FORMAT)
            numero_randomico, chave_cliente, id_cliente, validade, sucesso = recebe_mensagem(msg)
            if(sucesso):
                resposta = prepara_resposta(numero_randomico, chave_cliente, id_cliente, validade)
            else:
                resposta = "Senha inválida.".encode(FORMAT)
            conn.send(resposta)
            connected = False
    conn.close()


def main():
    server.listen()
    print(f"[RODANDO] TGS está ouvindo em {SERVER}:{PORT}")
    while True:
        conn, addr = server.accept()
        thread = threading.Thread(target=trata_cliente, args=(conn, addr))
        thread.start()
        print(f"[CONEXÕES ATIVAS] {threading.active_count() - 1}")


if __name__ == "__main__":
    print("[INICIANDO] TGS está iniciando...")
    main()