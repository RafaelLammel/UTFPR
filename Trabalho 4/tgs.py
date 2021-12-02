import socket, threading, random, sqlite3
from base64 import b64encode, b64decode
from Crypto.Cipher import DES


# Configurações do SOCKET
HEADER = 64
PORT = 3002
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = "utf-8"
SEPARADOR = "."


# Chave do TGS
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


def prepara_resposta(validade: str, numero_aleatorio: str, id_client: str, servico: str, chave_sessao_tgs: str) -> bytes:
    con = sqlite3.connect("tgs.db")
    chave_servico: str = con.cursor().execute("SELECT chave FROM servico WHERE id = ?", [servico]).fetchone()[0]
    con.close()
    
    chave_sessao_servico = random.randbytes(8).hex()[0:8]

    parte1 = f"{chave_sessao_servico}{SEPARADOR}{validade}{SEPARADOR}{numero_aleatorio}"
    parte1_cifra = cifra(chave_sessao_tgs, parte1)
    parte2 = f"{id_client}{SEPARADOR}{validade}{SEPARADOR}{chave_sessao_servico}"
    parte2_cifra = cifra(chave_servico, parte2)
    return f"{parte1_cifra}{SEPARADOR}{parte2_cifra}".encode(FORMAT)


def recebe_mensagem(msg: str) -> tuple[str, str, str, str, str]:
    # Separa dados da mensagem
    dados_msg = msg.split(SEPARADOR)

    # Decifra dados do AS
    dados_as = decifra(CHAVE_TGS, dados_msg[1])
    dados_as_split = dados_as.split(SEPARADOR)
    chave_sessao = dados_as_split[2]

    # Decifra dados do cliente
    dados_cliente = decifra(chave_sessao, dados_msg[0])
    dados_cliente_split = dados_cliente.split(SEPARADOR)

    return dados_cliente_split[2], dados_cliente_split[3], dados_cliente_split[0], dados_cliente_split[1], chave_sessao


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
            validade, numero_aleatorio, cliente, servico, chave_sessao  = recebe_mensagem(msg)
            resposta = prepara_resposta(validade, numero_aleatorio, cliente, servico, chave_sessao)
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