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


# Cria e retorna uma versão sem saturação da imagem
def removeSat(img_hls):
    hls_no_sat = np.copy(img_hls)
    for y in range(len(img_hls[0])):
        for x in range(len(img_hls)):
            hls_no_sat[x][y][2] = 0
    return hls_no_sat


# Procura por áreas verdes restantes e tenta resolver com uma média da vizinhança ou
# removendo sua saturação
def spillSuppression(img, mask):
    hls = cv2.cvtColor(img, cv2.COLOR_BGR2HLS)
    img_no_sat = cv2.cvtColor(removeSat(hls), cv2.COLOR_HLS2BGR)
    img_out = np.copy(img)
    for y in range(len(img[0])):
        for x in range(len(img)):
            if mask[x][y][0] != 0:
                hls_pixel = hls[x][y]
                # Com um range maior, verifica se o pixel é "verde" e está na mascara
                if hls_pixel[0] > 35 and hls_pixel[0] < 85:
                    soma = [0, 0, 0]
                    peso_soma = 0
                    # Com uma janela 3x3, faz uma média dos valores na vizinhança que não são "verdes"
                    for j in range(-1, 2):
                        for i in range(-1, 2):
                            if i != 0 and j != 0:
                                try:
                                    if hls[x+i][y+j][0] < 35 or hls[x+i][j+y] > 85:
                                        soma += img_out[x+i][y+j]
                                        peso_soma += 1
                                except:
                                    pass
                    # Substitui o valor do pixel pela média da vizinhança, ou
                    # se nenhum pixel da vizinhança serviu, remove a saturação
                    if peso_soma != 0:
                        img_out[x][y] = soma / peso_soma
                    else:
                        img_out[x][y] = img_no_sat[x][y]
    return img_out


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
            mask = cv2.inRange(hls, (50, 51, 61), (85, 204, 255))
            cv2.normalize(mask, mask, 0, 1, cv2.NORM_MINMAX)
            mask = 1 - mask
            mask = np.stack((mask,)*3,axis=-1)

            # Preparando Background para ser colocado
            bg_crop = cv2.resize(bg, (len(img[0]), len(img)))

            # Juntando Background e Foreground
            img_new = (mask * img) + ((1 - mask) * bg_crop)

            # Fazendo Spill Suppression
            img_out = spillSuppression(img_new, mask)

            cv2.imwrite(f"img/res/{out_filename} - final.bmp", img_out.astype('uint8'))


if __name__ == "__main__":
    main()
