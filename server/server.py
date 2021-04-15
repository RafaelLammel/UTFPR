import socket

PORT = 12345
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = 'ascii'
DISCONNECT = "!DISCONNECT"

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)

def handle_client(conn, addr):
    print(f"[NOVA CONEXÃO] {addr} conectado.")
    str_data = ''
    while True:
        msg = conn.recv(1024)
        if msg.decode(FORMAT) == DISCONNECT:
            break
        # TODO: Apresentar o gráfico
        # TODO: Aplicar o algoritmo de codificação de linha (Manchester Diferencial) modo inverso

        #Convertendo o binário  
        binary = msg.decode(FORMAT)
        for i in range(0, len(binary), 8):
            temp_data = binary[i:i + 8]     
            decimal_data = int(temp_data, 2)
            str_data = str_data + chr(decimal_data) 
        print("O valor binário após a conversão é:" + str_data)
        
        # TODO: Algoritmo de criptografia inverso
        # TODO: Mostrar a mensagem
    conn.close()
    print(f"[CONEXÃO DESCONECTADO] {addr} desconectado.")

def start():
    server.listen()
    print(f"[ESCUTANDO] Servidor ouvindo em {SERVER}:{PORT}")
    conn, addr = server.accept()
    handle_client(conn, addr)

print("[INICIANDO] Servidor iniciando...")
start()