# ===============================================================================
# Exemplo: segmentação de uma imagem em escala de cinza.
# -------------------------------------------------------------------------------
# Autor: Bogdan T. Nassu
# Universidade Tecnológica Federal do Paraná
# ===============================================================================

import sys
import timeit

import cv2
import numpy as np

# ===============================================================================


INPUT_IMAGE = "arroz.bmp"

# TODO: ajuste estes parâmetros!
NEGATIVO = False
THRESHOLD = 0.8
ALTURA_MIN = 15
LARGURA_MIN = 15
N_PIXELS_MIN = 255  # o menor é 412

# ===============================================================================


def binariza(img, threshold):
    """ Binarização simples por limiarização.

Parâmetros: img: imagem de entrada. Se tiver mais que 1 canal, binariza cada
            canal independentemente.
            threshold: limiar.
            
Valor de retorno: versão binarizada da img_in."""

    # TODO: escreva o código desta função.
    # Dica/desafio: usando a função np.where, dá para fazer a binarização muito
    # rapidamente, e com apenas uma linha de código!
    """
    for i in range(len(img)):
        for j in range(len(img[i])):
            if img[i][j] < threshold:
                img[i][j] = 0
            else:
                img[i][j] = 1
    """
    return np.float32(np.where(img < threshold, 0, 1))


# -------------------------------------------------------------------------------


def rotula(img, largura_min, altura_min, n_pixels_min):
    """Rotulagem usando flood fill. Marca os objetos da imagem com os valores
[0.1,0.2,etc].

Parâmetros: img: imagem de entrada E saída.
            largura_min: descarta componentes com largura menor que esta.
            altura_min: descarta componentes com altura menor que esta.
            n_pixels_min: descarta componentes com menos pixels que isso.

Valor de retorno: uma lista, onde cada item é um vetor associativo (dictionary)
com os seguintes campos:

'label': rótulo do componente.
'n_pixels': número de pixels do componente.
'T', 'L', 'B', 'R': coordenadas do retângulo envolvente de um componente conexo,
respectivamente: topo, esquerda, baixo e direita."""

    # TODO: escreva esta função.
    # Use a abordagem com flood fill recursivo.

    # Instanciando auxiliar e lista de retorno
    result = []
    aux = np.empty([len(img[0]), len(img)])

    # Colocando 0 para background e -1 para foreground na matriz auxiliar
    for i in range(len(img)):
        for j in range(len(img[0])):
            if img[i][j][0] == 1:
                aux[j][i] = -1
            else:
                aux[j][i] = 0
    label = 1
    """
    Percorrendo a matriz auxiliar e procurando pixels que pertencem ao
    Foreground (pixel == -1)
    """
    for x in range(len(aux)):
        for y in range(len(aux[0])):
            if aux[x][y] == -1:

                # Inicializando as propriedades do blob
                T = y
                L = x
                B = y
                R = x
                """
                Chamando flood_fill e montando dict de resposta quando 
                termina de inunda
                """
                res = flood_fill(label, aux, x, y, T, L, B, R)
                if (
                    res["n_pixels"] >= N_PIXELS_MIN
                    or abs(res["T"] - res["B"]) >= ALTURA_MIN
                    or abs(res["L"] - res["R"]) >= LARGURA_MIN
                ):
                    result.append(
                        {
                            "label": label,
                            "n_pixels": res["n_pixels"],
                            "T": res["T"],
                            "L": res["L"],
                            "B": res["B"],
                            "R": res["R"],
                        }
                    )
                # Modificando label e matriz
                aux = res["aux"]
                label += 1
    return result


# Verifica o vizinho determinado para saber se já possui rótulo ou não
def verifica_vizinho(aux, x, y):
    if y < 0 or x < 0 or x > len(aux) or y > len(aux[y]):
        return False
    if aux[x][y] == -1:
        return True
    else:
        return False


# Inunda recursivamente o pixel passado e posteriormente todos os seus
# vizinhos não rotulados
def flood_fill(label, aux, x, y, T, L, B, R):

    aux[x][y] = label

    # Vizinho 8
    verifica_y = [-1, -1, -1, 0, 0, 1, 1, 1]
    verifica_x = [-1, 0, 1, -1, 1, -1, 0, 1]

    n_pixels = 1

    # Verifica se o pixel atual está fora do retangulo do blob;
    # Se estiver, ele atualiza os valores necessários para aumentar o retangulo
    if y > T:
        T = y
    if x > R:
        R = x
    if x < L:
        L = x
    if y < B:
        B = y

    # Percorre todos os vizinhos e chama o flood_fill para os não rotulados
    for i in range(len(verifica_y)):
        if verifica_vizinho(aux, x + verifica_x[i], y + verifica_y[i]):
            res = flood_fill(
                label, aux, x + verifica_x[i], y + verifica_y[i], T, L, B, R
            )
            # Após visitar um vizinho, atualiza os valores do BLOB que podem
            # ter sido alterados pelo vizinho
            n_pixels += res["n_pixels"]
            T = res["T"]
            R = res["R"]
            L = res["L"]
            B = res["B"]
    return {"n_pixels": n_pixels, "T": T, "R": R, "L": L, "B": B, "aux": aux}


# ===============================================================================


def main():

    # Abre a imagem em escala de cinza.
    img = cv2.imread(INPUT_IMAGE, cv2.IMREAD_GRAYSCALE)
    if img is None:
        print("Erro abrindo a imagem.\n")
        sys.exit()

    # É uma boa prática manter o shape com 3 valores, independente da imagem ser
    # colorida ou não. Também já convertemos para float32.
    img = img.reshape((img.shape[0], img.shape[1], 1))
    img = img.astype(np.float32) / 255

    # Mantém uma cópia colorida para desenhar a saída.
    img_out = cv2.cvtColor(img, cv2.COLOR_GRAY2BGR)
    # Segmenta a imagem.

    if NEGATIVO:
        img = 1 - img
    img = binariza(img, THRESHOLD)
    cv2.imshow("01 - binarizada", img)
    cv2.imwrite("01 - binarizada.png", img * 255)

    start_time = timeit.default_timer()
    componentes = rotula(img, LARGURA_MIN, ALTURA_MIN, N_PIXELS_MIN)
    n_componentes = len(componentes)
    print("Tempo: %f" % (timeit.default_timer() - start_time))
    print("%d componentes detectados." % n_componentes)

    # Mostra os objetos encontrados.
    for c in componentes:
        cv2.rectangle(img_out, (c["L"], c["T"]), (c["R"], c["B"]), (0, 0, 1))

    cv2.imshow("02 - out", img_out)
    cv2.imwrite("02 - out.png", img_out * 255)
    cv2.waitKey()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    main()

# ===============================================================================
