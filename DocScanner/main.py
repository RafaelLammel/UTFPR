import cv2
import numpy as np
import math


# Caminho da imagem e de onde devem ser salvas as imagens processadas
PATH = "."
PROCESS_PATH = "./Processadas"


# Nome da imagem para ser processada
IMG = "img.jpeg"


# Tamanho da janela do filtro da mediana
MEDIAN_FILTER_WINDOW = 25


# Parâmetros para o Canny
CANNY_THRESHOLD1 = 50
CANNY_THRESHOLD2 = 200


# Parâmetros para a transformada de Hough
HOUGH_RHO = 1
HOUGH_THETA = np.pi / 360
HOUGH_THRESHOLD = 100


# Parâmetros para a imagem final (TYPE se aplica em todas as imagens processadas)
FINAL_FILE_TYPE = "jpg"


def pega_tupla(vertical, horizontal):
    tupla = tuple(
        [
            int(i)
            for i in np.linalg.solve(
                np.array(
                    [
                        [np.cos(vertical["theta"]), np.sin(vertical["theta"])],
                        [np.cos(horizontal["theta"]), np.sin(horizontal["theta"])],
                    ]
                ),
                np.array([vertical["rho"], horizontal["rho"]]),
            )
        ]
    )
    return tupla


def drawLines(img, lines):
    img_out = np.copy(img)

    for line in lines:
        for rho, theta in line:
            pega_linha(theta, rho, img_out)

    return img_out


def separateHorizontalAndVerticalLines(lines):
    horizontals = []
    verticals = []

    for line in lines:
        for rho, theta in line:
            if (
                theta < np.pi / 4
                or (theta > 3 * np.pi / 4 and theta < 5 * np.pi / 4)
                or theta > 7 * np.pi / 4
            ):
                horizontals.append(rho)
            else:
                verticals.append(rho)

    return horizontals, verticals


def separateUpDownLeftRightLines(lines, horizontal_mean, vertical_mean):
    left = []
    right = []
    up = []
    down = []

    for line in lines:
        for rho, theta in line:
            if (
                theta < np.pi / 4
                or (theta > 3 * np.pi / 4 and theta < 5 * np.pi / 4)
                or theta > 7 * np.pi / 4
            ):
                if rho > horizontal_mean:
                    right.append(rho)
                else:
                    left.append(rho)
            else:
                if rho > vertical_mean:
                    down.append(rho)
                else:
                    up.append(rho)

    return up, down, left, right


def getBordersFromLines(
    lines, up_median, down_median, left_median, right_median, img=[]
):
    if img.any():
        img_border_lines = np.copy(img)

    upper = {"rho": 0, "theta": 0}
    right = {"rho": 0, "theta": 0}
    lower = {"rho": 0, "theta": 0}
    left = {"rho": 0, "theta": 0}

    for line in lines:
        for rho, theta in line:
            if rho == down_median or rho == up_median or rho == left_median or rho == right_median:
                if rho == down_median:
                    lower["rho"] = rho
                    lower["theta"] = theta
                elif rho == up_median:
                    upper["rho"] = rho
                    upper["theta"] = theta
                elif rho == left_median:
                    left["rho"] = rho
                    left["theta"] = theta
                elif rho == right_median:
                    right["rho"] = rho
                    right["theta"] = theta
                if img.any():
                    pega_linha(theta, rho, img_border_lines)
    if img.any():
        return img_border_lines, upper, lower, right, left
    return upper, lower, right, left


def pega_linha(theta, rho, img_border_lines):
    # conversão para coordenadas cartesianas
    a = np.cos(theta)
    b = np.sin(theta)
    x0 = a * rho
    y0 = b * rho
    # Achando 2 pontos quaisquer para traçar as linhas
    # tamanho grande apenas para que seja grande o suficiente
    # para linha ir do inicio ao fim da imagem
    tamanho_maximo = int(
        np.sqrt(len(img_border_lines) ** 2 + len(img_border_lines[0]) ** 2)
    )

    x1 = int(x0 + tamanho_maximo * (-b))
    y1 = int(y0 + tamanho_maximo * (a))
    x2 = int(x0 - tamanho_maximo * (-b))
    y2 = int(y0 - tamanho_maximo * (a))

    cv2.line(img_border_lines, (x1, y1), (x2, y2), (0, 0, 255))


def main():
    # Abrindo a imagem e mantendo uma cópia cinza
    img = cv2.imread(f"{PATH}/{IMG}")
    img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    cv2.imwrite(f"{PROCESS_PATH}/1 - gray.{FINAL_FILE_TYPE}", img_gray)

    # Borrando a imagem e aplicando Canny para um versão com bordas da imagem
    img_blur = cv2.medianBlur(img_gray, MEDIAN_FILTER_WINDOW)
    cv2.imwrite(f"{PROCESS_PATH}/2 - blur.{FINAL_FILE_TYPE}", img_blur)
    img_edges = cv2.Canny(img_blur, CANNY_THRESHOLD1, CANNY_THRESHOLD2)
    cv2.imwrite(f"{PROCESS_PATH}/3 - edges.{FINAL_FILE_TYPE}", img_edges)

    # Pegando as linhas da transformada de Hough
    lines = cv2.HoughLines(img_edges, HOUGH_RHO, HOUGH_THETA, HOUGH_THRESHOLD)
    img_lines = drawLines(img, lines)
    cv2.imwrite(f"{PROCESS_PATH}/4 - lines.{FINAL_FILE_TYPE}", img_lines)

    # Deixando apenas 4 linhas da borda do papel

    # Primeiro, separamos em horizontais e verticais
    horizontals, verticals = separateHorizontalAndVerticalLines(lines)
    horizontals.sort()
    verticals.sort()

    # Achamos o meio das duas orientações
    horizontal_middle = np.mean(horizontals)
    vertical_middle = np.mean(verticals)

    # Separamos as linhas em cima, baixo, esquerda e direita
    up, down, left, right = separateUpDownLeftRightLines(
        lines, horizontal_middle, vertical_middle
    )

    # Pegamos a mediana de cada grupo
    if len(up) % 2 == 0:
        up.append(up[len(up) - 1])
    up_median = np.median(up)
    if len(down) % 2 == 0:
        down.append(down[len(down) - 1])
    down_median = np.median(down)
    if len(left) % 2 == 0:
        left.append(left[len(left) - 1])
    left_median = np.median(left)
    if len(right) % 2 == 0:
        right.append(right[len(right) - 1])
    right_median = np.median(right)

    img_border_lines, upper, lower, right, left = getBordersFromLines(
        lines, up_median, down_median, left_median, right_median, img
    )
    cv2.imwrite(f"{PROCESS_PATH}/5 - border-lines.{FINAL_FILE_TYPE}", img_border_lines)

    pontos = [
        pega_tupla(upper, right),
        pega_tupla(upper, left),
        pega_tupla(lower, right),
        pega_tupla(lower, left),
    ]

    p1 = np.float32([list(pontos[i]) for i in range(4)])
    # calcula distancia entre todos os pontos
    distancias = []
    for i in range(len(pontos)):
        for j in range(i, len(pontos)):
            tmp = int(
                math.sqrt(sum([(a - b) ** 2 for a, b in zip(pontos[i], pontos[j])]))
            )
            if tmp != 0:
                distancias.append(tmp)
    distancias.sort()
    cateto_menor = int(distancias[0])
    cateto_maior = int(
        np.sqrt(distancias[len(distancias) - 1] ** 2 - cateto_menor ** 2)
    )
    if len(img) < len(img[0]):
        cateto_maior, cateto_menor = cateto_menor, cateto_maior

    points = np.copy(img)
    for i in range(4):
        cv2.circle(points, pontos[i], 30, (0, 0, 255), 10)
    cv2.imwrite(f"{PROCESS_PATH}/6 - points.{FINAL_FILE_TYPE}", points)

    p2 = np.float32(
        [[0, 0], [cateto_menor, 0], [0, cateto_maior], [cateto_menor, cateto_maior]]
    )
    mat = cv2.getPerspectiveTransform(p1, p2)
    out_img = cv2.warpPerspective(img, mat, ((cateto_menor), (cateto_maior)))
    cv2.imwrite(f"{PROCESS_PATH}/7 - final.{FINAL_FILE_TYPE}", out_img)


if __name__ == "__main__":
    main()
