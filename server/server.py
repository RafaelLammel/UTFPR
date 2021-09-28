import socket, pickle, threading
import sys
import tkinter as tk
from tkinter.constants import END
import matplotlib
matplotlib.use("agg")
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import (FigureCanvasTkAgg, NavigationToolbar2Tk)

PORT = 12345
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
DISCONNECT = "!DISCONNECT"
CHAVE_CRIPTO = 5

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)

def close():
    root.destroy()
    sys.exit()

def gui():

    tk.Label(frame, text="Mensagem aplicado código manchester: ").grid(row=0)
    tk.Label(frame, text="Mensagem em Binário: ").grid(row=1)
    tk.Label(frame, text="Mensagem Criptografada: ").grid(row=2)
    tk.Label(frame, text="Mensagem final: ").grid(row=3)

    manchesterEntry.grid(row=0, column=1)
    binarioEntry.grid(row=1, column=1)
    criptoEntry.grid(row=2, column=1)
    msgEntry.grid(row=3, column=1)

    root.mainloop()

def drawGraph(bits, binario):
    fig = plt.figure(figsize=(10, 3), dpi=100)
    for i in range(0, len(bits)+1, 2):
        plt.axvline(i, color=".5", linewidth=2)
    plt.axhline(2.5, color='.5', linewidth=2)
    plt.plot([b+2 for b in bits], color="r", linewidth=2, drawstyle="steps-pre")
    plt.ylim([-1,6])
    step = 0.9
    for tbit, bit in enumerate(list(map(int, [c for c in binario]))):
        plt.text(tbit + step, 3.5, str(bit))
        step += 1
    plt.gca().axis('off')
    canvas = FigureCanvasTkAgg(fig, master=graphFrame)
    canvas.draw()
    canvas.get_tk_widget().grid(row=4, column=0, ipadx=40, ipady=20)
    toolbarFrame = tk.Frame(master=graphFrame)
    toolbarFrame.grid(row=5,column=0)
    NavigationToolbar2Tk(canvas, toolbarFrame)

def ceaserCipherDecoding(msg):
    descripto = ""
    for c in msg:
        criptoChar = (ord(c) - CHAVE_CRIPTO) % 256
        descripto += chr(criptoChar)
    return descripto

def difManchesterDecoding(binario_codificado):
    primeiraVez = True
    binario = ""
    anterior = ""
    binario_codificado = binario_codificado[1:]
    for i in range(0, len(binario_codificado)-1, 2):
        b = binario_codificado[i] + binario_codificado[i+1]
        if primeiraVez:
            if b == "01":
                binario += "0"
                anterior = "01"
            else:
                binario += "1"
                anterior = "10"
            primeiraVez = False
        else:
            if anterior == b:
                binario += "0"
                anterior = b
            else:
                binario += "1"
                anterior = b
    return binario

def handleClient(conn, addr):
    print(f"[NOVA CONEXÃO] {addr} conectado.")
    while True:
        # Recebendo dados
        data = conn.recv(2048)
        msg = ''.join([str(i) for i in pickle.loads(data)])
        if msg == DISCONNECT:
            break
        msg_codificada = msg
        
        # Apresentar o gráfico
        bits = list(map(int, [c for c in msg_codificada]))
        binario = difManchesterDecoding(msg_codificada)
        drawGraph(bits, binario)
        
        # Aplicar o algoritmo de codificação de linha (Manchester Diferencial) modo inverso
        manchesterEntry.delete(0,END)
        manchesterEntry.insert(0,msg_codificada)

        # Convertendo o binário
        binarioEntry.delete(0,END)
        binarioEntry.insert(0,binario)
        msg_cripto = ""
        for i in range(0, len(binario), 8):
            temp_msg = binario[i:i + 8]     
            decimal_msg = int(temp_msg, 2)
            msg_cripto =  msg_cripto + chr(decimal_msg) 
        
        # Algoritmo de criptografia inverso
        criptoEntry.delete(0,END)
        criptoEntry.insert(0,msg_cripto)
        descripto = ceaserCipherDecoding(msg_cripto)
        
        # Mostrar a mensagem
        msgEntry.delete(0,END)
        msgEntry.insert(0,descripto)
    conn.close()
    print(f"[CONEXÃO DESCONECTADO] {addr} desconectado.")
    close()

def serv():
    print("[INICIANDO] Servidor iniciando...")
    server.listen()
    print(f"[ESCUTANDO] Servidor ouvindo em {SERVER}:{PORT}")
    conn, addr = server.accept()
    handleClient(conn, addr)

root = tk.Tk()
root.protocol("WM_DELETE_WINDOW", close)

frame = tk.Frame(root)
frame.pack()
graphFrame = tk.Frame(root)
graphFrame.pack()

threadServer = threading.Thread(target=serv)

msgEntry = tk.Entry(frame, width=80)
criptoEntry = tk.Entry(frame, width=80)
binarioEntry = tk.Entry(frame, width=80)
manchesterEntry = tk.Entry(frame, width=80)

threadServer.daemon = True
threadServer.start()
gui()