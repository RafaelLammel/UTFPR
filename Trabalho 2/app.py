import json, hashlib
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler


SEED_DATA = "seed_data.json"
TOKENS_DATA = "tokens_data.json"


def criar_tokens():
    hora_date = datetime.now()
    with open(SEED_DATA, "r") as file:
        data = json.load(file)
    try:
        with open(TOKENS_DATA, "r") as file:
            tokens_data = json.load(file)
    except FileNotFoundError:
        tokens_data = []
    for user in data:
        senha_hash = user["senha_semente"]
        hora = hora_date.strftime("%d/%m/%Y %H:%M")
        hora_hash = hashlib.sha256(hora.encode()).hexdigest()
        senha_atual = hashlib.sha256(str(senha_hash + hora_hash).encode()).hexdigest()
        tokens = []
        for x in range(5):
            tokens.append(senha_atual[0:6])
            senha_atual = hashlib.sha256(senha_atual.encode()).hexdigest()
        token_user = next((item for item in tokens_data if item["usuario"] == user["usuario"]), False)
        if token_user:
            tokens_data[tokens_data.index(token_user)]["tokens"] = tokens
        else:
            tokens_data.append({
                "usuario": user["usuario"],
                "tokens": tokens
            })
    with open(TOKENS_DATA, "w") as file:
            json.dump(tokens_data, file)


def login(usuario, token):
    with open(TOKENS_DATA, "r") as file:
        tokens_data = json.load(file)
    token_user = next((item for item in tokens_data if item["usuario"] == usuario), False)
    if token_user:
        if token in token_user["tokens"]:
            tokens_data[tokens_data.index(token_user)]["tokens"] = tokens_data[tokens_data.index(token_user)]["tokens"][0:tokens_data[tokens_data.index(token_user)]["tokens"].index(token)]
            with open(TOKENS_DATA, "w") as file:
                json.dump(tokens_data, file)
            return True
    return False


def main():
    try:
        sched = BackgroundScheduler()
        sched.add_job(criar_tokens, "cron", minute="*")
        sched.start()
        criar_tokens()
        print("Por favor, insira login e senha descartavel: ")
        usuario = input("Usuario: ")
        token = input("Senha descartável: ")
        if login(usuario, token):
            print("\nSenha válida!")
        else:
            print("\nSenha invalida!")
    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    main()