import sys, unicodedata, string


ACCEPTED = list(string.ascii_uppercase+string.ascii_lowercase+string.digits)


def main(argv):
    if len(argv) > 1:
        print("Apenas um arquivo pode ser analisado por vez!")
    elif len(argv) < 1:
        print("Forneça um arquivo para ser analisado!")
    else:
        try:
            f = open(argv[0], encoding="utf-8")
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


if __name__ == "__main__":
    main(sys.argv[1:])