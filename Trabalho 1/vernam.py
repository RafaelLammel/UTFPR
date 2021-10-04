import sys, getopt, unicodedata, string, random


ACCEPTED = list(string.ascii_uppercase+string.ascii_lowercase+string.digits)


def generateKey(keyFileName, textLen):
    key = ""
    for x in range(textLen):
        key += random.choice(ACCEPTED)
    keyFile = open(keyFileName, "w")
    keyFile.write(key)
    keyFile.close()
    return key


def cripto(keyFileName, fileName, cipher):
    file = open(fileName, "r")
    text = unicodedata.normalize('NFD', file.read()).encode('ascii', 'ignore').decode("utf-8")
    file.close()
    textLen = 0
    for c in text:
        if c in ACCEPTED:
            textLen += 1
    if cipher == 1:
        key = generateKey(keyFileName, textLen)
    else:
        keyFile = open(keyFileName, "r")
        key = keyFile.read()
        keyFile.close()
    cipherdText = ""
    keyPos = 0
    textPos = 0
    while textPos < len(text):
        if text[textPos] in ACCEPTED:
            cipherdText += ACCEPTED[(ACCEPTED.index(text[textPos]) + (ACCEPTED.index(key[keyPos]) * cipher)) % 62]
            keyPos += 1
        else:
            cipherdText += text[textPos]
        textPos += 1
    if cipher == 1:
        f = open("texto-cifrado.txt", "w")
    else:
        f = open("texto-aberto.txt", "w")
    f.write(cipherdText)
    f.close()


def main(argv):
    try:
        opts, args = getopt.getopt(argv, "cd")
    except:
        print("Erro ao ler os argumentos")

    dicto = dict(opts)

    if "-c" in dicto and "-d" in dicto:
        print("Erro: passe apenas uma opção! Cifrar ou Decifrar")
    elif "-c" in dicto or "-d" in dicto:
        if len(args) > 2:
            print("Erro: Apenas 2 arquivos podem ser referenciados! A chave e o texto")
        if len(args) < 2:
            print("Erro: Apenas 1 arquivo foi referenciado! Precisa de um arquivo chave e outro texto")
        else:
            cripto(args[0], args[1], 1 if ("-c" in dicto) else -1)
    else:
        print("Erro! Nenhuma opção fornecida! Passe uma opção:\n-c: cifrar\n-d: decifrar")


if __name__ == "__main__":
    main(sys.argv[1:])