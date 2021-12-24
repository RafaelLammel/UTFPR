import sys, getopt, unicodedata, string


# Lista de caracteres aceitaveis na criptografia
ACCEPTED = list(string.ascii_uppercase+string.ascii_lowercase+string.digits)


def cesar(k, fileName, cipher):
    """Função utilizada para criptografar ou descriptografar um arquivo txt com a cifra de cesar.

    Args:
        k (int): Chave para a cifra de cesar
        fileName (string): Caminho do arquivo a ser criptografado/descriptografado
        cipher (int): Recebe 1 para quando a função é criptografar e -1 quando descriptografar
    """

    try:
        f = open(fileName, encoding="utf-8")
        # Pegando texto sem acentos do arquivo
        text = unicodedata.normalize('NFD', f.read()).encode('ascii', 'ignore').decode("utf-8")
        f.close()
        cipherdText = ""
        k = cipher * k
        for c in text:
            letter = c
            if c in ACCEPTED:
                letter = ACCEPTED[(ACCEPTED.index(letter) + k) % 62]
            cipherdText += letter
        if cipher == 1:
            f = open("texto-cifrado.txt", "w")
        else:
            f = open("texto-aberto.txt", "w")
        f.write(cipherdText)
        f.close()
    except FileNotFoundError:
        print("Erro: arquivo não encontrado!")
    except:
        print("Erro desconhecido na hora de aplicar o algoritmo!")


def main(argv):
    try:
        opts, args = getopt.getopt(argv, "cdk:")
    except:
        print("Erro ao ler os argumentos")

    dicto = dict(opts)

    if "-c" in dicto and "-d" in dicto:
        print("Erro: passe apenas uma opção! Cifrar ou Decifrar")
    elif "-c" in dicto or "-d" in dicto:
        if "-k" in dicto:
            if len(args) > 1:
                print("Erro: apenas um arquivo é permitido!")
            elif len(args) < 1:
                print("Erro: nenhum arquivo fornecido!")
            else:
                try:
                    k = int(dicto["-k"])
                    cesar(k, args[0], 1 if ("-c" in dicto) else -1)
                except ValueError:
                    print("Erro: chave inválida!")
        else:
            print("Erro: nenhuma chave fornecida!")
    else:
        print("Erro! Nenhuma opção fornecida! Passe uma opção:\n-c: cifrar\n-d: decifrar")


if __name__ == "__main__":
    main(sys.argv[1:])