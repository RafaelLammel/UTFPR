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
# Transforma em binário
binary = ''.join(format(ord(i), '08b') for i in msg)
print("binário:" + binary)
# TODO: Algorítmo de codificação de Linha (Manchester Diferencial)
# TODO: Apresentação do gráfico
# TODO: Enviar para o servidor


client.send(binary.encode(FORMAT))

client.send(DISCONNECT.encode(FORMAT))