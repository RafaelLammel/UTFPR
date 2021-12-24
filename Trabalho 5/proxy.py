import socket, threading, sys, logging, logging.handlers, hashlib


HOST = "127.0.0.1"
PORT = 12345
ENDERECO = (HOST, PORT)


def requisicao_host(requisicao: bytes) -> str:
    linhas = requisicao.split(b"\r\n")
    
    for linha in linhas:
        if linha.startswith(b"Host:"):
            return linha.split(b" ")[1].decode()


def requisicao_metodo(requisicao: bytes) -> str:
    primeira_linha = requisicao.split(b"\r\n")[0]
    return primeira_linha.split(b" ")[0].decode()


def requisicao_port(host: str) -> int:
    if(host.find(":") != -1):
        return int(host.split(":")[1])
    return 80


def requisicao_caminho(requisicao: bytes) -> str:
    primeira_linha = requisicao.split(b"\r\n")[0]
    return primeira_linha.split(b" ")[1].decode()


def resposta_status(resposta: bytes) -> str:
    primeira_linha = resposta.split(b"\r\n")[0]
    try:
        code = primeira_linha.split(b' ')[1].decode()
        status = primeira_linha.split(b' ')[2].decode()
        return f"{code} {status}"
    except:
        return "[STATUS NÃO RECONHECIDO]"


def servidor_tcp(client: socket.socket, address: socket.AddressFamily):
    logger.warning(f"[INFO] Cliente de IP e porta {address} conectado a proxy")

    requisicao = client.recv(2048)
    
    try:
        if b"monitorando" in requisicao:
            html = "<html><head><title>Exemplo de resposta HTTP </title></head><body>Acesso não autorizado!</body></html>"
            resposta = f"HTTP/1.1 200 OK\r\nServer: Microsoft-IIS/4.0\r\nDate: Mon, 3 Jan 2016 17:13:34 GMT\r\nContent-Type: text/html\r\nLast-Modified: Mon, 11 Jan 2016 17:24:42 GMT\r\nContent-Length: 112\r\n\r\n{html}"
            resposta = resposta.encode()
            client.send(resposta)
            logger.error("[ERROR] Acesso negado na pagina: " + requisicao_caminho(requisicao))
        else:
            try:
                proxy_cliente = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                host = requisicao_host(requisicao)
                if host:
                    port = requisicao_port(host)
                    host = host if host.find(":") == -1 else host.split(":")[0]
                    proxy_cliente.connect((host, port))
                    proxy_cliente.send(requisicao)
                    proxy_cliente.settimeout(0.2)
                    while True:
                        resposta = proxy_cliente.recv(2048)
                        if(len(resposta) > 0):
                            status = resposta_status(resposta)
                            logger.warning(f"[INFO] Requisicao ao host {host} retornou status {status}")
                            client.send(resposta)
                        else:
                            break
            except:
                pass
    except ConnectionAbortedError:
        pass
    client.close()


logger = logging.getLogger()
logger.addHandler(logging.handlers.SysLogHandler(("192.168.56.101", 514), socktype=socket.SOCK_DGRAM))
logger.addHandler(logging.StreamHandler())

hashfile = ""
integridade = ""
with open("proxy.py") as f:
    hashfile = hashlib.sha1(f.read().encode()).hexdigest()
with open("integridade.txt") as f:
    integridade = f.read()

if (hashfile != "" and integridade != "") and hashfile == integridade:
    logger.warning("[INFO] Arquivo íntegro!")
else:
    logger.warning("[WARN] Arquivo não íntegro!")

proxy = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
proxy.bind(ENDERECO)
proxy.listen()

try:
    while True:
            client, address = proxy.accept()
            thread = threading.Thread(target=servidor_tcp, args=(client, address))
            thread.daemon = True
            thread.start()
except KeyboardInterrupt:
    sys.exit()