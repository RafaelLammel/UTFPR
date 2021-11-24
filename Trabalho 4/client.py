import socket, hashlib
from base64 import b64decode, b64encode
from Crypto.Cipher import DES


HEADER = 64
PORTS = {
    "AS": 3001,
    "TGS": 3002,
    "SERVICOS": 4000
}
SERVER = socket.gethostbyname(socket.gethostname())
FORMAT = "utf-8"
SEPARADOR = "."
TEMPO_TOKEN = 60


def cifra(chave: str, msg: str) -> tuple[str, str]:
    des = DES.new(chave.encode(FORMAT), DES.MODE_OFB)
    msg_cifrada = b64encode(des.encrypt(msg.encode(FORMAT))).decode(FORMAT)
    iv = b64encode(des.iv).decode(FORMAT)
    return msg_cifrada, iv


def decifra(chave: str, msg_cifrada: str, iv: str) -> str:
    des = DES.new(chave.encode(FORMAT), DES.MODE_OFB, iv=b64decode(iv))
    msg_decifrada = des.decrypt(b64decode(msg_cifrada)).decode(FORMAT)
    return msg_decifrada


def envia_msg(client: socket.socket, msg: str):
    mensagem = msg.encode(FORMAT)
    tamanho_msg = len(mensagem)
    tamanho_enviar = str(tamanho_msg).encode(FORMAT)
    tamanho_enviar += b" " * (HEADER - len(tamanho_enviar))
    client.send(tamanho_enviar)
    client.send(mensagem)


def autenticar_as(user_id: str, service_id: str, password: str) -> tuple[str, str, str]:
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((SERVER, PORTS["AS"]))

    chave_cliente = hashlib.sha256(password.encode(FORMAT)).hexdigest()[0:8]
    parte2 = f"{service_id}{SEPARADOR}{TEMPO_TOKEN}{SEPARADOR}{651231564}"

    parte2_cifra, iv = cifra(chave_cliente, parte2)

    # 2. Obtém um ticket de acesso ao serviço de tickets TGS.
    envia_msg(client, f"{user_id}{SEPARADOR}{parte2_cifra}{SEPARADOR}{iv}")
    resposta_as = client.recv(1024).decode(FORMAT)
    client.close()

    resposta_as_split = resposta_as.split(SEPARADOR)

    resposta_cliente = decifra(chave_cliente, resposta_as_split[0], resposta_as_split[2])
    resposta_cliente_split = resposta_cliente.split(SEPARADOR)

    return resposta_cliente_split[0], resposta_as_split[1], resposta_as_split[3]


def autenticar_tgs(user_id: str, service_id: str, chave_sessao_tgs: str, token_tgs: str, iv_tgs) -> tuple[str, str, str]:
    parte1 = f"{user_id}{SEPARADOR}{service_id}{SEPARADOR}{TEMPO_TOKEN}{SEPARADOR}{321654987}"
    parte1_cifra, iv_parte1 = cifra(chave_sessao_tgs, parte1)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((SERVER, PORTS["TGS"]))

    # 4. Obtém um ticket de acesso ao serviço.
    envia_msg(client, f"{parte1_cifra}{SEPARADOR}{token_tgs}{SEPARADOR}{iv_parte1}{SEPARADOR}{iv_tgs}")
    resposta_tgs = client.recv(1024).decode(FORMAT)
    client.close()
    
    resposta_tgs_split = resposta_tgs.split(SEPARADOR)

    resposta_cliente = decifra(chave_sessao_tgs, resposta_tgs_split[0], resposta_tgs_split[2])
    resposta_cliente_split = resposta_cliente.split(SEPARADOR)

    return resposta_cliente_split[0], resposta_tgs_split[1], resposta_tgs_split[3]


def autenticar_servico(user_id: str, service_id: str, token_servico: str, chave_sessao_servico: str, iv_servico: str):
    parte1 = f"{user_id}{SEPARADOR}{TEMPO_TOKEN}{SEPARADOR}{service_id}{SEPARADOR}{87946461}"
    parte1_cifra, iv_parte1 = cifra(chave_sessao_servico, parte1)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((SERVER, PORTS["SERVICOS"] + int(service_id)))

    # 6. Recebe retorno do servidor desejado.
    envia_msg(client, f"{parte1_cifra}{SEPARADOR}{token_servico}{SEPARADOR}{iv_parte1}{SEPARADOR}{iv_servico}")
    resposta_servico = client.recv(1024).decode(FORMAT)
    client.close()

    resposta_servico_split = resposta_servico.split(SEPARADOR)

    resposta = decifra(chave_sessao_servico, resposta_servico_split[0], resposta_servico_split[1])

    print(resposta)


def main():
    print("Qual serviço gostaria de acessar?")
    print("1 - Serviço 1")
    print("2 - Serviço 2")
    #try:
    service_id = int(input())
    if service_id != 1 and service_id != 2:
        raise ValueError
    
    #1. O cliente se autentica junto ao AS
    user_id = input("Entre com o seu usuário: ")
    password = input("Entre com sua senha: ")
    chave_sessao_tgs, token_tgs, iv_tgs = autenticar_as(user_id, service_id, password)

    #3. Solicita ao TGS um ticket de acesso ao serviço (servidor) desejado.
    chave_sessao_servico, token_servico, iv_servico = autenticar_tgs(user_id, service_id, chave_sessao_tgs, token_tgs, iv_tgs)
    
    #5. Com esse novo ticket, ele pode se autenticar junto ao servidor desejado e solicitar serviços.
    autenticar_servico(user_id, service_id, token_servico, chave_sessao_servico, iv_servico)
    # except ValueError as e:
    #     print("Opção invalida!")
    #     print(e)


if __name__ == "__main__":
    main()