import cv2
import sys
import timeit
import numpy as np

INPUT_IMAGEM = 'Imagens/a01 - Original.bmp'
TAMANHO_JANELA_X = 3
TAMANHO_JANELA_Y = 3
TEMPORARIO = 5

def ingenuo(imagem, x, y):
    aux = np.empty([len(imagem[0]), len(imagem)])
    nova_img = np.empty([len(imagem[0]), len(imagem)])
    
    for i in range(TEMPORARIO, len(aux)-TEMPORARIO):
        for j in range(TEMPORARIO , len(aux[0])-TEMPORARIO ):
            soma = 0

          
            for k in range(int(i-x/2), int(i+x/2)):
                for l in range(int(j-y/2), int(j+y/2)):
                    soma += aux[i][j]
            nova_img[i][j] = soma/(x*y)

    return nova_img

def main():
    img = cv2.imread(INPUT_IMAGEM, cv2.IMREAD_GRAYSCALE)
    if img is None:
        print("erro ao ler imagem")
        sys.exit()

    img = img.reshape ((img.shape [0], img.shape [1], 1))
    img = img.astype (np.float32) / 255

    img = ingenuo(img, TAMANHO_JANELA_X, TAMANHO_JANELA_Y)

    cv2.imwrite(f'Imagens/a01 - Borrada {TAMANHO_JANELA_X}X{TAMANHO_JANELA_Y}.bmp', img)


if __name__ == '__main__':
    main()
