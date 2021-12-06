import socket, hashlib, random
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
SEPARADOR = "\n"
TEMPO_TOKEN = 3600
IV = "bomuoN9Q4P4="
SALT = "adb7289508113adc02e0fa0cf8279c6ef932f1b06830fcc19d0653d73f4a4347"


def cifra(chave: str, msg: str) -> str:
    des = DES.new(chave.encode(FORMAT), DES.MODE_OFB, iv=b64decode(IV))
    msg_cifrada = b64encode(des.encrypt(msg.encode(FORMAT))).decode(FORMAT)
    return msg_cifrada


def decifra(chave: str, msg_cifrada: str) -> str:
    des = DES.new(chave.encode(FORMAT), DES.MODE_OFB, iv=b64decode(IV))
    msg_decifrada = des.decrypt(b64decode(msg_cifrada)).decode(FORMAT)
    return msg_decifrada


def envia_msg(client: socket.socket, msg: str):
    mensagem = msg.encode(FORMAT)
    tamanho_msg = len(mensagem)
    tamanho_enviar = str(tamanho_msg).encode(FORMAT)
    tamanho_enviar += b" " * (HEADER - len(tamanho_enviar))
    client.send(tamanho_enviar)
    client.send(mensagem)


def autenticar_as(user_id: str, service_id: str, password: str) -> tuple[str, str]:
    numero_random = random.randint(1000, 10000)
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((SERVER, PORTS["AS"]))

    chave_cliente = hashlib.sha256(f"{password}{SALT}".encode(FORMAT)).hexdigest()[0:8]
    parte2 = f"{service_id}{SEPARADOR}{TEMPO_TOKEN}{SEPARADOR}{numero_random}"

    parte2_cifra = cifra(chave_cliente, parte2)

    # 2. Obtém um ticket de acesso ao serviço de tickets TGS.
    envia_msg(client, f"{user_id}{SEPARADOR}{parte2_cifra}")
    resposta_as = client.recv(1024).decode(FORMAT)
    client.close()

    if(resposta_as == "Senha inválida."):
        raise Exception(resposta_as)

    resposta_as_split = resposta_as.split(SEPARADOR)

    resposta_cliente = decifra(chave_cliente, resposta_as_split[0])
    resposta_cliente_split = resposta_cliente.split(SEPARADOR)
    
    if(resposta_cliente_split[1] != str(numero_random)):
        raise ValueError("Falha no retorno do AS: Número randomico não confere.")
    return resposta_cliente_split[0], resposta_as_split[1]


def autenticar_tgs(user_id: str, service_id: str, chave_sessao_tgs: str, token_tgs: str) -> tuple[str, str]:
    numero_random = random.randint(1000, 10000)
    parte1 = f"{user_id}{SEPARADOR}{service_id}{SEPARADOR}{TEMPO_TOKEN}{SEPARADOR}{numero_random}"
    parte1_cifra = cifra(chave_sessao_tgs, parte1)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((SERVER, PORTS["TGS"]))

    # 4. Obtém um ticket de acesso ao serviço.
    envia_msg(client, f"{parte1_cifra}{SEPARADOR}{token_tgs}")
    resposta_tgs = client.recv(1024).decode(FORMAT)
    client.close()
    
    resposta_tgs_split = resposta_tgs.split(SEPARADOR)

    resposta_cliente = decifra(chave_sessao_tgs, resposta_tgs_split[0])
    resposta_cliente_split = resposta_cliente.split(SEPARADOR)

    if(resposta_cliente_split[2] != str(numero_random)):
        raise ValueError("Falha no retorno do TGS: Número randomico não confere.")
    return resposta_cliente_split[0], resposta_tgs_split[1]


def autenticar_servico(user_id: str, service_id: str, token_servico: str, chave_sessao_servico: str):
    numero_random = random.randint(1000, 10000)
    parte1 = f"{user_id}{SEPARADOR}{TEMPO_TOKEN}{SEPARADOR}{service_id}{SEPARADOR}{numero_random}"
    parte1_cifra = cifra(chave_sessao_servico, parte1)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect((SERVER, PORTS["SERVICOS"] + int(service_id)))

    # 6. Recebe retorno do servidor desejado.
    envia_msg(client, f"{parte1_cifra}{SEPARADOR}{token_servico}")
    resposta_servico = client.recv(1024).decode(FORMAT)
    client.close()

    resposta_servico_split = resposta_servico.split(SEPARADOR)

    resposta = decifra(chave_sessao_servico, resposta_servico_split[0])

    resposta_split = resposta.split(SEPARADOR)

    if(resposta_split[1] != str(numero_random)):
        raise ValueError("Falha no retorno do Serviço: Número randomico não confere.")
    print(resposta_split[0])


def main():
    print("Qual serviço gostaria de acessar?")
    print("1 - Serviço 1")
    print("2 - Serviço 2")
    try:
        service_id = input()
        if service_id != "1" and service_id != "2":
            raise ValueError("Valor inválido!")

        service_id = int(service_id)
        
        #1. O cliente se autentica junto ao AS
        user_id = input("Entre com o seu usuário: ")
        password = input("Entre com sua senha: ")
        chave_sessao_tgs, token_tgs = autenticar_as(user_id, service_id, password)

        #3. Solicita ao TGS um ticket de acesso ao serviço (servidor) desejado.
        chave_sessao_servico, token_servico = autenticar_tgs(user_id, service_id, chave_sessao_tgs, token_tgs)
        
        #5. Com esse novo ticket, ele pode se autenticar junto ao servidor desejado e solicitar serviços.
        autenticar_servico(user_id, service_id, token_servico, chave_sessao_servico)
    except Exception as e:
        print(e)


if __name__ == "__main__":
    main()