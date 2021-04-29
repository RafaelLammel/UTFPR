import socket
import matplotlib.pyplot as plt
  
PORT = 12345
FORMAT = "ascii"
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
DISCONNECT = "!DISCONNECT"
CHAVE_CRIPTO = 3

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)

# Digitar 
print("Escreva sua mensagem: ", end="")
msg = chr(19)

# Criptografia
cripto = ""
for c in msg:
    criptoChar = (ord(c) + CHAVE_CRIPTO) % 128
    cripto += chr(criptoChar)
print("Mensagem Criptografada: " + cripto)

# Transforma em binário
binary = ''.join(format(ord(i), '08b') for i in cripto)
print("Binario:" + binary)

# Algorítmo de codificação de Linha (Manchester Diferencial)
primeiraVez = True
msg_codificada = ""
anterior = ""
for b in binary:
    if primeiraVez:
        if b == "0":
            msg_codificada += "01"
            anterior = "01"
        else:
            msg_codificada += "10"
            anterior = "10"
        primeiraVez = False
    else:
        if b == "1":
            if anterior == "01":
                msg_codificada += "10"
                anterior = "10"
            else:
                msg_codificada += "01"
                anterior = "01"
        else:
            msg_codificada += anterior
print("Mensagem codificada em Manchester Diferencial: " + msg_codificada)

# TODO: Apresentação do gráfico
array = list(map(int, [c for c in msg_codificada]))
plt.plot(array, drawstyle="steps-pre")
plt.ylabel('Manchester Diferencial')
plt.show()

# Enviar para o servidor
client.send(msg_codificada.encode(FORMAT))

client.send(DISCONNECT.encode(FORMAT))