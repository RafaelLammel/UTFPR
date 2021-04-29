import socket

PORT = 12345
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = 'ascii'
DISCONNECT = "!DISCONNECT"
CHAVE_CRIPTO = 3

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)

def handle_client(conn, addr):
    print(f"[NOVA CONEXÃO] {addr} conectado.")
    str_data = ''
    while True:
        msg = conn.recv(1024)
        if msg.decode(FORMAT) == DISCONNECT:
            break
        msg_codificada = msg.decode(FORMAT)
        # TODO: Apresentar o gráfico
        # Aplicar o algoritmo de codificação de linha (Manchester Diferencial) modo inverso
        primeiraVez = True
        binary = ""
        anterior = ""
        for i in range(0, len(msg_codificada)-1, 2):
            b = msg_codificada[i] + msg_codificada[i+1]
            if primeiraVez:
                if b == "01":
                    binary += "0"
                    anterior = "01"
                else:
                    binary += "1"
                    anterior = "10"
                primeiraVez = False
            else:
                if anterior == b:
                    binary += "0"
                    anterior = b
                else:
                    binary += "1"
                    anterior = b

        #Convertendo o binário
        print("Binario: " + binary)
        for i in range(0, len(binary), 8):
            temp_data = binary[i:i + 8]     
            decimal_data = int(temp_data, 2)
            str_data = str_data + chr(decimal_data) 
        
        # Algoritmo de criptografia inverso
        print("Mensagem Criptografada: " + str_data)
        descripto = ""
        for c in str_data:
            criptoChar = (ord(c) - CHAVE_CRIPTO) % 128
            descripto += chr(criptoChar)
        
        # Mostrar a mensagem
        print("Mensagem final: " + descripto)
    conn.close()
    print(f"[CONEXÃO DESCONECTADO] {addr} desconectado.")

def start():
    server.listen()
    print(f"[ESCUTANDO] Servidor ouvindo em {SERVER}:{PORT}")
    conn, addr = server.accept()
    handle_client(conn, addr)

print("[INICIANDO] Servidor iniciando...")
start()