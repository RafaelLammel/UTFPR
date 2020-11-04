import cv2
import numpy as np
import sys

IMAGEM_CAMINHO = "imagens/"
IMAGEM_ENTRADA = "imagem_02.bmp"

LIMIAR_LUM = 127

ALFA = 1
BETA = 0.2

LARGURA_JANELA = 5
ALTURA_JANELA = 5

ALGORITMO = 2
'''
Algoritmo 1 = Filtro Gaussiana
Algoritmo 2 = Filtro Box Blur
'''

def filtro_bright_pass(img):
    hls = cv2.cvtColor(img, cv2.COLOR_BGR2HLS)
    for y in range(len(hls[0])):
        for x in range(len(hls)):
            if hls[x][y][1] <= LIMIAR_LUM:
                hls[x, y] = [0, 0, 0]
    cv2.imwrite(f"imagens/processadas/mascara.bmp", cv2.cvtColor(hls, cv2.COLOR_HLS2BGR))
    return cv2.cvtColor(hls, cv2.COLOR_HLS2BGR)


def gaussian_blom(img):
    blur = [None] * 5
    desvio_padrao = 10
    for i in range(5):
        blur[i] = cv2.GaussianBlur(img, (0, 0), desvio_padrao)
        cv2.imwrite(f"imagens/processadas/borrado - {i}.bmp", blur[i])
        desvio_padrao = desvio_padrao * 2
    return mascara(img, blur)


def boxblur_bloom(img):
    blur = [None] * 5
    soma = 0
    for i in range(5):
        blur[i] = cv2.blur(img, (LARGURA_JANELA + soma * i, ALTURA_JANELA + soma * i))
        cv2.imwrite(f"imagens/processadas/borrado - {i}.bmp", blur[i])
        soma += 7
    return mascara(img, blur)


def mascara(img, blur):
    # somatoria de todas as imagens na lista usando numpy
    # truncando valor em 255
    aux = np.array(blur).sum(axis=0)
    return np.where(aux > 255, 255, aux)


def bloom(img):
    if ALGORITMO == 1:
        img = gaussian_blom(img)
    elif ALGORITMO == 2:
        img = boxblur_bloom(img)
    return img


def soma(img, img_bloom):
    return ALFA * img + BETA * img_bloom


def main():
    img_original = cv2.imread(f"{IMAGEM_CAMINHO}{IMAGEM_ENTRADA}", cv2.IMREAD_COLOR)
    if img_original is None:
        print("erro ao ler a imagem")
        sys.exit()
    img_bright_pass = filtro_bright_pass(img_original)
    img_bloom = bloom(img_bright_pass)
    img = soma(img_original, img_bloom)
    cv2.imwrite(f"{IMAGEM_CAMINHO}/processadas/filtro_bright_pass.bmp", img)


if __name__ == "__main__":
    main()
