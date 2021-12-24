import sys, getopt, unicodedata, string, random


# Lista de caracteres aceitaveis na criptografia
ACCEPTED = list(string.ascii_uppercase+string.ascii_lowercase+string.digits)


def generateKey(keyFileName, text):
    """Função para gerar a chave aleatória usada na criptografia de Vernam

    Args:
        keyFileName (string): Caminho onde deve ser criado o arquivo que irá conter a chave, em formato '.dat'. (Se não existir, será criado)
        text (string): Texto a ser criptografado

    Returns:
        string: a chave gerada aleatóriamente
    """

    textLen = 0
    for c in text:
        if c in ACCEPTED:
            textLen += 1
    key = ""
    for x in range(textLen):
        key += random.choice(ACCEPTED)
    keyFile = open(keyFileName, "w")
    keyFile.write(key)
    keyFile.close()
    return key


def vernam(keyFileName, fileName, cipher):
    """Função utilizada para criptografar ou descriptografar um arquivo txt com a cifra de cesar.

    Args:
        k (int): Chave para a cifra de cesar
        fileName (string): Caminho do arquivo a ser criptografado/descriptografado
        cipher (int): Recebe 1 para quando a função é criptografar e -1 quando descriptografar
    """

    file = open(fileName, "r")
    text = unicodedata.normalize('NFD', file.read()).encode('ascii', 'ignore').decode("utf-8")
    file.close()
    if cipher == 1:
        key = generateKey(keyFileName, text)
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
            vernam(args[0], args[1], 1 if ("-c" in dicto) else -1)
    else:
        print("Erro! Nenhuma opção fornecida! Passe uma opção:\n-c: cifrar\n-d: decifrar")


if __name__ == "__main__":
    main(sys.argv[1:])