import cv2
import numpy as np


PATH = "img"
BG = "bg.bmp"
INPUT_IMAGES = [
    "0.bmp",
    "1.bmp",
    "2.bmp",
    "3.bmp",
    "4.bmp",
    "5.bmp",
    "6.bmp",
    "7.bmp",
    "8.bmp",
]


def main():
    bg = cv2.imread(f"{PATH}/{BG}", cv2.IMREAD_COLOR)
    for filename in INPUT_IMAGES:
        img = cv2.imread(f"{PATH}/{filename}", cv2.IMREAD_COLOR)
        if img is None:
            print(f"Erro ao abrir imagem: {PATH}/{filename}")
        else:
            out_filename = filename.split(".", 1)[0]

            # Convertendo imagem para HLS e criando uma máscara
            hls = cv2.cvtColor(img, cv2.COLOR_BGR2HLS)
            mask = cv2.inRange(hls, (50, 50, 61), (70, 179, 255))

            # Usando a máscara, remove o fundo verde da imagem
            img_masked = np.copy(img)
            img_masked[mask != 0] = [0, 0, 0]

            # Preparando Background para ser colocado
            bg_crop = cv2.resize(bg, (len(img[0]), len(img)))
            bg_crop[mask == 0] = [0, 0, 0]

            # Juntando imagem sem
            img_out = img_masked + bg_crop
            cv2.imwrite(f"img/res/{out_filename} - final.bmp", img_out)


if __name__ == "__main__":
    main()
