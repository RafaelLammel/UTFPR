import socket, pickle
import tkinter as tk
from tkinter.constants import END
import matplotlib.pyplot as plt
  
PORT = 12345
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
DISCONNECT = "!DISCONNECT"
CHAVE_CRIPTO = 5

root = tk.Tk()

msgEntry = tk.Entry(root, width=80)
criptoEntry = tk.Entry(root, width=80)
binarioEntry = tk.Entry(root, width=80)
manchesterEntry = tk.Entry(root, width=80)

def gui():

    tk.Label(root, text="Digite a mensagem: ").grid(row=0)
    tk.Label(root, text="Mensagem Criptografada: ").grid(row=1)
    tk.Label(root, text="Mensagem em Binário: ").grid(row=2)
    tk.Label(root, text="Mensagem aplicado código manchester: ").grid(row=3)

    msgEntry.grid(row=0, column=1)
    criptoEntry.grid(row=1, column=1)
    binarioEntry.grid(row=2, column=1)
    manchesterEntry.grid(row=3, column=1)

    button = tk.Button(root, text="Enviar", command=start)
    button.grid(row=4)

    root.mainloop()

def ceaserCipherEncoding(msg):
    cripto = ""
    for c in msg:
        criptoChar = (ord(c) + CHAVE_CRIPTO) % 256
        cripto += chr(criptoChar)
    return cripto

def difManchesterEncoding(bits):
    primeiraVez = True
    msg_codificada = "1"
    anterior = ""
    for b in bits:
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
    return msg_codificada

def start():
    msg = msgEntry.get()
    # Iniciando conexão
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect(ADDR)

    # Criptografia
    cripto = ceaserCipherEncoding(msg)
    criptoEntry.delete(0,END)
    criptoEntry.insert(0,cripto)

    # Transforma em binário
    binary = ''.join(format(ord(i), '08b') for i in cripto)
    binarioEntry.delete(0,END)
    binarioEntry.insert(0,binary)

    # Algorítmo de codificação de Linha (Manchester Diferencial)
    msg_codificada = difManchesterEncoding(binary)
    manchesterEntry.delete(0,END)
    manchesterEntry.insert(0,msg_codificada)

    # Apresentação do gráfico
    bits = list(map(int, [c for c in msg_codificada]))
    for i in range(0, len(bits)+1, 2):
        plt.axvline(i, color=".5", linewidth=2)
    plt.axhline(2.5, color='.5', linewidth=2)
    plt.plot([b+2 for b in bits], color="r", linewidth=2, drawstyle="steps-pre")
    plt.ylim([-1,6])
    step = 0.9
    for tbit, bit in enumerate(list(map(int, [c for c in binary]))):
        plt.text(tbit + step, 3.5, str(bit))
        step += 1
    plt.gca().axis('off')
    plt.show()

    # Enviar para o servidor
    client.send(pickle.dumps(bits))

    # Enviando mensagem para desconectar
    client.send(pickle.dumps(DISCONNECT))

gui()