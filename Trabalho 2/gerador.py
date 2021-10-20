import json, hashlib
from datetime import datetime


USER_DATA = "user_data.json"
SEED_DATA = "seed_data.json"


def criar_usuario():
    try:
        with open(USER_DATA, "r") as file:
            user = json.load(file)
        with open(SEED_DATA, "r") as file:
            seed = json.load(file)
    except FileNotFoundError:
        user = []
        seed = []
    usuario = input("\nFavor entre com um nome de usuário: ")
    if next((item for item in user if item["usuario"] == usuario), False):
        print("\nNome de usuario ja cadastrado!")
        return
    senha_local = input("Favor entre com uma senha local: ")
    senha_semente = input("Favor entre com uma senha semente: ")
    user.append({ "usuario": usuario, "senha_local": hashlib.sha512(senha_local.encode()).hexdigest() })
    seed.append({ "usuario": usuario, "senha_semente": hashlib.sha512(senha_semente.encode()).hexdigest() })
    with open(USER_DATA, "w") as file:
        json.dump(user, file)
    with open(SEED_DATA, "w") as file:
        json.dump(seed, file)


def login():
    usuario = input("\nFavor entre com o usuario: ")
    senha_local = input("Favor entre com a senha local: ")
    senha_hash = hashlib.sha512(senha_local.encode()).hexdigest()
    try:
        with open(USER_DATA, "r") as file:
            data = json.load(file)
    except:
        return False
    user = next((item for item in data if item["usuario"] == usuario), False)
    if user == False:
        return False
    elif user["usuario"] == usuario and user["senha_local"] == senha_hash:
        return usuario
    return False


def criar_tokens(usuario, hora_date):
    with open(SEED_DATA, "r") as file:
        data = json.load(file)
    user = next((item for item in data if item["usuario"] == usuario), False)
    if user:
        senha_hash = user["senha_semente"]
        hora = hora_date.strftime("%d/%m/%Y %H:%M")
        hora_hash = hashlib.sha256(hora.encode()).hexdigest()
        senha_atual = hashlib.sha256(str(senha_hash + hora_hash).encode()).hexdigest()
        tokens = []
        for x in range(5):
            tokens.append(senha_atual[0:6])
            senha_atual = hashlib.sha256(senha_atual.encode()).hexdigest()
        return tokens


def main():
    print("\nSelecione a opcao desejada: ")
    print("1 - Criar Usuario")
    print("2 - Logar")
    try:
        opcao = int(input())
        if opcao == 1:
            criar_usuario()
        elif opcao == 2:
            usuario = login()
            if usuario:
                print("\nBem vindo " + usuario)
                opcao_logado = 0
                while opcao_logado != 2:
                    print("\nO que deseja fazer?")
                    print("1 - Gerar Token")
                    print("2 - Sair")
                    try:
                        opcao_logado = int(input())
                        if opcao_logado == 1:
                            hora_date = datetime.now()
                            tokens = criar_tokens(usuario, hora_date)
                            x = 1
                            print("\nSenhas Geradas: ")
                            print("----------------------------------------------------")
                            for token in tokens:
                                print(str(x) + ": " + token)
                                x += 1
                            print("----------------------------------------------------")
                            print("As senhas acima serão expiradas em: " + datetime.fromtimestamp(float(hora_date.timestamp() + 60)).strftime("%d/%m/%Y %H:%M"))
                        elif opcao_logado != 2:
                            print("Opcao invalida!")
                    except ValueError:
                        print("Opcao invalida!")
            else:
                print("\nFalha na autenticação! Reveja seu Login ou Senha Local")
        else:
            print("\nOpcao invalida!")
    except ValueError:
        print("\nOpcao invalida!")
    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    main()