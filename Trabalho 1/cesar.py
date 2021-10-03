import sys, getopt, unicodedata, string


ACCEPTED = list(string.ascii_uppercase+string.ascii_lowercase+string.digits)


def cripto(k, fileName, cipher):
    try:
        f = open(fileName, encoding="utf-8")
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
                    cripto(k, args[0], 1 if ("-c" in dicto) else -1)
                except ValueError:
                    print("Erro: chave inválida!")
        else:
            print("Erro: nenhuma chave fornecida!")
    else:
        print("Erro! Nenhuma opção fornecida! Passe uma opção:\n-c: cifrar\n-d: decifrar")


if __name__ == "__main__":
    main(sys.argv[1:])