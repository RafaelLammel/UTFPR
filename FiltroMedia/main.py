import cv2
import sys
import numpy as np

# Caminho da imagem a ser processada
CAMINHO_IMAGEM = 'Imagens/'
INPUT_IMAGEM = 'a01 - Original.bmp'

# Largura e Altura da janela (Escolher sempre tamanhos ímpares para ambos)
LARGURA_JANELA = 3
ALTURA_JANELA = 3

'''
Algoritmo a ser utilizado:
0 - "Ingenuo" (WIP)
1 - Filtros Separáveis (TODO)
2 - Imagens Integrais (TODO)
'''
ALGORITMO = 0


def ingenuo(img, w, h):
    nova_img = np.copy(img)

    for i in range(h, len(img[0])-h):
        for j in range(w, len(img)-w):
            soma = [0, 0, 0]
            for k in range(int(i-h/2), int(i+h/2)):
                for l in range(int(j-w/2), int(j+w/2)):
                    soma[0] += img[l][k][0]
                    soma[1] += img[l][k][1]
                    soma[2] += img[l][k][2]
            nova_img[j][i] = [s / (w*h) for s in soma]

    return nova_img


def main():
    img = cv2.imread(f'{CAMINHO_IMAGEM}{INPUT_IMAGEM}', cv2.IMREAD_COLOR)
    if img is None:
        print("Erro ao ler imagem")
        sys.exit()

    if ALGORITMO == 0:
        img = ingenuo(img, LARGURA_JANELA, ALTURA_JANELA)
    else:
        print("Algoritmo inexistente ou não implementado!")
        sys.exit()

    OUTPUT_IMAGEM = INPUT_IMAGEM.split('.', 1)[0]
    cv2.imwrite(f'Imagens/Processadas/{OUTPUT_IMAGEM} Borrada {LARGURA_JANELA}X{ALTURA_JANELA}.bmp', img)


if __name__ == '__main__':
    main()
