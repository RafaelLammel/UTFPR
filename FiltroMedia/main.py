import cv2
import sys
import math
import numpy as np

# Caminho da imagem a ser processada
CAMINHO_IMAGEM = 'Imagens/'
INPUT_IMAGEM = 'b01 - Original.bmp'

# Largura e Altura da janela (Escolher sempre tamanhos ímpares para ambos)
LARGURA_JANELA = 5
ALTURA_JANELA = 5


ALGORITMO = 0
'''
Algoritmo a ser utilizado:
0 - "Ingenuo" (DONE - Ignorando margens)
1 - Filtros Separáveis (BUG - Começaram a surgir alguns pixels com cores estranhas sem explicação 
    em alguns pontos da imagem colorida, dentro da área filtrada)
2 - Imagens Integrais (DONE)
'''


def ingenuo(img, w, h):
    nova_img = np.copy(img)

    for y in range(math.floor(h/2), len(img[0])-math.ceil(h/2)+1):
        for x in range(math.floor(w/2), len(img)-math.ceil(w/2)+1):
            soma = [0, 0, 0]
            for j in range(y-math.floor(h/2), y+math.ceil(h/2)):
                for i in range(x-math.floor(w/2), x+math.ceil(w/2)):
                    soma += img[i][j]
            nova_img[x][y] = soma / (w*h)
    return nova_img


def integral(img, w, h):
    # transformar em float
    img = img.astype(np.float32)
    img_integral = np.copy(img)
    nova_img = np.copy(img)

    # Criando a imagem integral, um eixo por vez
    for y in range(len(img[0])):
        for x in range(1, len(img)):
            img_integral[x][y] = img[x][y] + img_integral[x-1][y]
            
    for y in range(1, len(img[0])):
        for x in range(len(img)):
            img_integral[x][y] = img_integral[x][y] + img_integral[x][y-1]
    
    # Fazendo a soma dos 4 pixels e criando a nova imagem de saída com o filtro da média
    for y in range(len(img[0])):
        for x in range(len(img)):
            topo = int(x-w/2)
            inferior = int(x+w/2)
            direita = int(y+h/2)
            esquerda = int(y-h/2)
            if topo < 0:
                topo = 0
            if inferior >= len(img):
                inferior = len(img)-1
            if direita >= len(img[0]):
                direita = len(img[0])-1
            if esquerda < 0:
                esquerda = 0
            # Perto das bordas a janela é menor, então é necessário calcular o tamanho da janela atual
            altura = inferior - topo
            largura = direita - esquerda
            nova_img[x][y] = (img_integral[topo][esquerda] - img_integral[topo][direita] + img_integral[inferior][direita] - img_integral[inferior][esquerda]) / ((altura if altura > 0 else 1) * (largura if largura > 0 else 1))

    return nova_img


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
                    soma += img[i][y]
            else:
                soma = (soma - img[primeiro_pixel][y] + img[ultimo_pixel][y])
            buffer[x][y] = soma / w

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
                    soma += buffer[x][i]
            else:
                soma = (soma - buffer[x][primeiro_pixel] + buffer[x][ultimo_pixel])
            nova_img[x][y] = soma / h

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