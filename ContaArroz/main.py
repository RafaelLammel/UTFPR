import cv2
import numpy as np


PATH = "Imagens"
INPUT_IMAGES = ["60.bmp",
                "82.bmp",
                "114.bmp",
                "150.bmp",
                "205.bmp"]


def main():
    for filename in INPUT_IMAGES:
        # Abre a imagem em escala de cinza
        img_out = cv2.imread(f"{PATH}/{filename}", cv2.IMREAD_COLOR)
        if img_out is None:
            print("Erro ao abrir imagem: " + img_path)
        else:
            # Nome do arquivo de saída
            out_filename = filename.split(".", 1)[0]

            # Mantém uma cópia colorida para desenhar a saída.
            img = cv2.cvtColor(img_out, cv2.COLOR_BGR2GRAY)

            # Realiza a limiarização adaptativa, devido as diferenças de iluminação que atrapalham
            # a limiarização global em algumas imagens
            img_bin = cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 49, -20.0)
            cv2.imwrite(f"{PATH}/Processadas/{out_filename} - binarizada.bmp", img_bin)

            # Realiza a abertura, para remover ruídos da imagem binarizada
            kernel = np.ones((5,5), np.uint8)
            img_open = cv2.morphologyEx(img_bin, cv2.MORPH_OPEN, kernel)
            cv2.imwrite(f"{PATH}/Processadas/{out_filename} - aberta.bmp", img_open)
            
            # Identifica e desenha contornos no arroz
            contours, _ = cv2.findContours(img_open, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
            cv2.drawContours(img_out, contours, -1, (0, 0, 255), 2)

            # Salva a imagem com os arrozes circulados (WIP)
            cv2.imwrite(f"{PATH}/Processadas/{out_filename} - out.bmp", img_out)


if __name__ == "__main__":
    main()