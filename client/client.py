import socket

PORT = 12345
FORMAT = "ascii"
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
DISCONNECT = "!DISCONNECT"

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)

msg = input()
# TODO: Criptografia
# TODO: Transformar em binário
# TODO: Algorítmo de codificação de Linha (Manchester Diferencial)
# TODO: Apresentação do gráfico
# TODO: Enviar para o servidor
client.send(msg.encode(FORMAT))

client.send(DISCONNECT.encode(FORMAT))