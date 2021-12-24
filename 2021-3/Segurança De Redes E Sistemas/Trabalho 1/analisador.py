import sys, unicodedata, string


# Lista de caracteres aceitaveis na criptografia
ACCEPTED = list(string.ascii_uppercase+string.ascii_lowercase+string.digits)


def analisador(fileName):
    """Função que fará a analise de frequência de um arquivo txt fornecido.

    Args:
        fileName (string): Caminho do arquivo
    """
    try:
        f = open(fileName, encoding="utf-8")
        # Pegando texto sem acentos do arquivo
        text = "".join(unicodedata.normalize('NFD', f.read()).encode('ascii', 'ignore').decode("utf-8").split())
        f.close()
        analisys = {}
        total = 0
        for c in text:
            if c in ACCEPTED:
                if c in analisys:
                    analisys[c] += 1
                else:
                    analisys[c] = 1
                total += 1
        for key in analisys:
            analisys[key] = round((analisys[key] / total) * 100, 2)
        analisys = sorted(analisys.items(), key=lambda x:x[1], reverse=True)
        print("{:<10} {:<5}".format('Símbolo','Freq. (%)'))
        for k, v in analisys:
            print("{:<10} {:<5}".format(k, v))
    except FileNotFoundError:
        print("Erro: arquivo não encontrado!")
    except:
        print("Erro desconhecido na hora de decifrar!")

def main(argv):
    if len(argv) > 1:
        print("Apenas um arquivo pode ser analisado por vez!")
    elif len(argv) < 1:
        print("Forneça um arquivo para ser analisado!")
    else:
        analisador(argv[0])


if __name__ == "__main__":
    main(sys.argv[1:])