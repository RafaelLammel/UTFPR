import socket, pickle
import matplotlib.pyplot as plt

PORT = 12345
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
DISCONNECT = "!DISCONNECT"
CHAVE_CRIPTO = 5

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)

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
        plt.show()
        
        # Aplicar o algoritmo de codificação de linha (Manchester Diferencial) modo inverso
        print()
        print("Mensagem codificada em Manchester Diferencial: " + msg_codificada)

        # Convertendo o binário
        print()
        print("Binario: " + binario)
        msg_cripto = ""
        for i in range(0, len(binario), 8):
            temp_msg = binario[i:i + 8]     
            decimal_msg = int(temp_msg, 2)
            msg_cripto =  msg_cripto + chr(decimal_msg) 
        
        # Algoritmo de criptografia inverso
        print()
        print("Mensagem Criptografada: " +  msg_cripto)
        descripto = ceaserCipherDecoding(msg_cripto)
        
        # Mostrar a mensagem
        print()
        print("Mensagem final: " + descripto)
        print()
    conn.close()
    print(f"[CONEXÃO DESCONECTADO] {addr} desconectado.")

if __name__ == "__main__":
    print("[INICIANDO] Servidor iniciando...")
    server.listen()
    print(f"[ESCUTANDO] Servidor ouvindo em {SERVER}:{PORT}")
    conn, addr = server.accept()
    handleClient(conn, addr)