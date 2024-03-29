import socket, threading, datetime
from base64 import b64encode, b64decode
from Crypto.Cipher import DES


# Configurações do SOCKET
HEADER = 64
PORT = 4001
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = "utf-8"
SEPARADOR = "\n"


# Chave do serviço
CHAVE = "1cbec737"
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


# Prepara resposta de retorno para o cliente
def prepara_resposta(servico: int, nmr_randomico: str, chave_sessao: str) -> bytes:
    msg = ""
    if servico == -1:
        msg = "Seu token expirou!"
    else:
        msg = "Você requisitou pelo serviço 1!"
    resposta = f"{msg}{SEPARADOR}{nmr_randomico}"
    resposta_cifrado = cifra(chave_sessao, resposta)
    return f"{resposta_cifrado}".encode(FORMAT)


def recebe_mensagem(msg: str) -> tuple[str, str, str]:
    # Separa dados da mensagem
    dados_msg = msg.split(SEPARADOR)

    # Decifra dados do TGS
    dados_tgs = decifra(CHAVE, dados_msg[1])
    dados_tgs_split = dados_tgs.split(SEPARADOR)
    chave_sessao = dados_tgs_split[2]

    # Decifra dados do cliente
    dados_cliente = decifra(chave_sessao, dados_msg[0])
    dados_cliente_split = dados_cliente.split(SEPARADOR)
    servico = "-1"
    if(float(dados_tgs_split[1]) > datetime.datetime.now().timestamp()):
        servico = dados_cliente_split[2]
    nmr_randomico = dados_cliente_split[3]

    return servico, nmr_randomico, chave_sessao


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
            servico, nmr_randomico, chave_cliente = recebe_mensagem(msg)
            resposta = prepara_resposta(int(servico), nmr_randomico, chave_cliente)
            conn.send(resposta)
            connected = False
    conn.close()


def main():
    server.listen()
    print(f"[RODANDO] Servidor está ouvindo em {SERVER}:{PORT}")
    while True:
        conn, addr = server.accept()
        thread = threading.Thread(target=trata_cliente, args=(conn, addr))
        thread.start()
        print(f"[CONEXÕES ATIVAS] {threading.active_count() - 1}")


if __name__ == "__main__":
    print("[INICIANDO] Servidor está iniciando...")
    main()