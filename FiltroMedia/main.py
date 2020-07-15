import cv2
import sys
import numpy as np

# Caminho da imagem a ser processada
CAMINHO_IMAGEM = 'Imagens/'
INPUT_IMAGEM = 'b01 - Original.bmp'

# Largura e Altura da janela (Escolher sempre tamanhos ímpares para ambos)
LARGURA_JANELA = 21
ALTURA_JANELA = 24


ALGORITMO = 2
'''
Algoritmo a ser utilizado:
0 - "Ingenuo" (WIP)
1 - Filtros Separáveis (TODO)
2 - Imagens Integrais (TODO)
'''


def ingenuo(img, h, w):
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


def integral(img, h, w):
    # transformar em float
    img = img.astype(np.float32) / 255
    img_integral = np.copy(img)
    # cria a imagem integral
    for i in range(len(img[0])):
        for j in range(len(img)):
            if i == 0 and j == 0:
                img_integral[i][j] = img[i][j]
            elif i != 0 and j != 0:
                img_integral[i][j] = (img[i][j]+img_integral[i][j-1] +
                                      img_integral[i-1][j]-img_integral[i-1][j-1])
            elif i == 0:
                img_integral[i][j] = img[i][j]+img_integral[i][j-1]
            elif j == 0:
                img_integral[i][j] = img[i][j]+img_integral[i-1][j]
    #cria imagem com a media
    for i in range(len(img[0])):
        for j in range(len(img)):
            for k in range(h, 0, -1):
                if i-k >= 0:
                    break
            for m in range(w, 0, -1):
                if j-m >= 0:
                    break
            img[i][j] = pixel_integral(img_integral, i, j, k, m)

    img = img * 255
    img = img.astype(np.uint8)
    return img


def pixel_integral(img_integral, i, j, h, w):
    pixel = ((img_integral[i][j]-img_integral[i-h][j] -
                  img_integral[i][j-w]+img_integral[i-h][j-w])/(w*h))
    return pixel


def separaveis(img, h, w):
    nova_img = np.copy(img)
    img_horizontal = np.copy(img)
    for i in range(len(img[0])):
        for j in range(w, len(img)-w):
            soma = [0, 0, 0]
            for l in range(int(j-w/2), int(j+w/2)):
                soma[0] += img[i][l][0]
                soma[1] += img[i][l][1]
                soma[2] += img[i][l][2]
            img_horizontal[i][j] = [s/w for s in soma]
    for j in range(len(img)):
        for i in range(h, len(img[0])-h):
            soma = [0, 0, 0]
            tamanho = [int(i-h/2), int(i+h/2)]
            for k in range(tamanho[0], tamanho[1]):
                soma[0] += img_horizontal[k][j][0]
                soma[1] += img_horizontal[k][j][1]
                soma[2] += img_horizontal[k][j][2]
            nova_img[i][j] = [s/h for s in soma]
    return nova_img


def filtro_media(img, w, h):
    switch = {
        0: ingenuo,
        1: separaveis,
        2: integral
    }
    func = switch.get(ALGORITMO, None)
    if func is None:
        print(f"função invalida")
        sys.exit()
    return func(img, w, h)


def main():
    img = cv2.imread(f'{CAMINHO_IMAGEM}{INPUT_IMAGEM}', cv2.IMREAD_COLOR)
    if img is None:
        print("Erro ao ler imagem")
        sys.exit()

    img = filtro_media(img, LARGURA_JANELA, ALTURA_JANELA)

    OUTPUT_IMAGEM = INPUT_IMAGEM.split('.', 1)[0]
    cv2.imwrite(f'Imagens/Processadas/{OUTPUT_IMAGEM} Borrada {LARGURA_JANELA}X{ALTURA_JANELA}.bmp', img)


if __name__ == '__main__':
    main()
