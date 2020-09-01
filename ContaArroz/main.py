import cv2
import numpy as np


PATH = "Imagens"
INPUT_IMAGES = ["60.bmp", "82.bmp", "114.bmp", "150.bmp", "205.bmp"]


def contagem_estatistica(estatisticas):
    # pega o indice do maior componente(no caso o fundo) e deleta ele
    indice = np.unravel_index(np.argmax(estatisticas, axis=None), estatisticas.shape)
    estatisticas = np.delete(estatisticas, indice[0], 0)

    contador = 0
    mediana = np.median(estatisticas, axis=0)
    media = np.mean(estatisticas, axis=0)
    for i in range(len(estatisticas)):
        contador += 1
        if estatisticas[i][4] > mediana[4]:
            total = estatisticas[i][4] / (mediana[4])
            if total >= 2:
                contador += int(total)
    return contador


def main():
    for filename in INPUT_IMAGES:
        # Abre a imagem em escala de cinza
        img_out = cv2.imread(f"{PATH}/{filename}", cv2.IMREAD_COLOR)
        if img_out is None:
            print(f"Erro ao abrir imagem:{PATH}/{filename} ")
        else:
            # Nome do arquivo de saída
            out_filename = filename.split(".", 1)[0]

            # Mantém uma cópia colorida para desenhar a saída.
            img = cv2.cvtColor(img_out, cv2.COLOR_BGR2GRAY)

            # Realiza a limiarização adaptativa, devido as diferenças de iluminação que
            # atrapalham a limiarização global em algumas imagens
            img_bin = cv2.adaptiveThreshold(
                img, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 49, -20.0
            )
            cv2.imwrite(f"{PATH}/Processadas/{out_filename} - binarizada.bmp", img_bin)

            # Realiza a abertura, para remover ruídos da imagem binarizada
            kernel = np.ones((5, 5), np.uint8)
            img_open = cv2.morphologyEx(img_bin, cv2.MORPH_OPEN, kernel)
            cv2.imwrite(f"{PATH}/Processadas/{out_filename} - aberta.bmp", img_open)
            # marca os componentes conexos, retorna:
            # * quantiade de rotulos
            # * imagens rotulada
            # * array com estatisticas de cada rotulo
            # array[i][4] area
            # * coordenadas do centroide do objeto
            _, _, estatisticas, _ = cv2.connectedComponentsWithStats(img_open)

            quantidade = contagem_estatistica(estatisticas)
            porcetagem = round((100 * quantidade) / int(filename[:-4]), 1)
            print(f"{filename} -> {quantidade} componentes -> {porcetagem}% de acerto")

            # Identifica e desenha contornos no arroz
            contours, _ = cv2.findContours(
                img_open, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE
            )
            cv2.drawContours(img_out, contours, -1, (0, 0, 255), 2)

            # Salva a imagem com os arrozes circulados (WIP)
            cv2.imwrite(f"{PATH}/Processadas/{out_filename} - out.bmp", img_out)


if __name__ == "__main__":
    main()
