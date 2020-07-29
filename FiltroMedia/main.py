import cv2
import sys
import numpy as np

# Caminho da imagem a ser processada
CAMINHO_IMAGEM = 'Imagens/'
INPUT_IMAGEM = 'b01 - Original.bmp'

# Largura e Altura da janela (Escolher sempre tamanhos ímpares para ambos)
LARGURA_JANELA = 7
ALTURA_JANELA = 7


ALGORITMO = 1
'''
Algoritmo a ser utilizado:
0 - "Ingenuo" (DONE - Ignorando margens)
1 - Filtros Separáveis (DONE - Ignorando margens)
2 - Imagens Integrais (WIP)
'''


def ingenuo(img, w, h):
    nova_img = np.copy(img)

    for y in range(int(h/2), len(img[0])-int(h/2)):
        for x in range(int(w/2), len(img)-int(w/2)):
            soma = [0, 0, 0]
            for j in range(int(y-h/2), int(y+h/2)):
                for i in range(int(x-w/2), int(x+w/2)):
                    soma[0] += img[i][j][0]
                    soma[1] += img[i][j][1]
                    soma[2] += img[i][j][2]
            nova_img[x][y] = [s / (w*h) for s in soma]
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


def separaveis(img, w, h):
    buffer = np.copy(img)
    nova_img = np.copy(img)
    
    #Borra buffer
    for y in range(len(img[0])):
        soma = [0, 0, 0]
        primeiro = True
        for x in range(int(w/2), len(img)-int(w/2)):
            primeiro_pixel = int(x-w/2)
            ultimo_pixel = int(x+w/2)
            if primeiro:
                primeiro = False
                for i in range(primeiro_pixel, ultimo_pixel):
                    soma[0] += img[i][y][0]
                    soma[1] += img[i][y][1]
                    soma[2] += img[i][y][2]
            else:
                soma[0] = soma[0] - img[primeiro_pixel][y][0] + img[ultimo_pixel][y][0]
                soma[1] = soma[1] - img[primeiro_pixel][y][1] + img[ultimo_pixel][y][1]
                soma[2] = soma[2] - img[primeiro_pixel][y][2] + img[ultimo_pixel][y][2]
            buffer[x][y] = [s/w for s in soma]

    #Borra imagem final
    for x in range(int(w/2), len(img)-int(w/2)):
        soma = [0, 0, 0]
        primeiro = True
        for y in range(int(h/2), len(img[0])-int(h/2)):
            primeiro_pixel = int(y-h/2)
            ultimo_pixel = int(y+h/2)
            if primeiro:
                primeiro = False
                for i in range(primeiro_pixel, ultimo_pixel):
                    soma[0] += buffer[x][i][0]
                    soma[1] += buffer[x][i][1]
                    soma[2] += buffer[x][i][2]
            else:
                soma[0] = soma[0] - buffer[x][primeiro_pixel][0] + buffer[x][ultimo_pixel][0]
                soma[1] = soma[1] - buffer[x][primeiro_pixel][1] + buffer[x][ultimo_pixel][1]
                soma[2] = soma[2] - buffer[x][primeiro_pixel][2] + buffer[x][ultimo_pixel][2]
            nova_img[x][y] = [s/h for s in soma]
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
