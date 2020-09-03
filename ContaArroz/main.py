import cv2
import numpy as np


PATH = "Imagens"
INPUT_IMAGES = ["50.jpeg", "60.bmp", "82.bmp", "114.bmp", "150.bmp", "205.bmp"]


def contagem_estatistica(estatisticas):
    # pega o indice do maior componente(no caso o fundo) e deleta ele
    indice = np.unravel_index(np.argmax(estatisticas, axis=None), estatisticas.shape)
    estatisticas = np.delete(estatisticas, indice[0], 0)

    # Faz a contagem, descartando arrozes que estão com área muito grande;
    # Nessa primeira contagem, usamos a mediana de todos e temos uma mediana
    # mais ou menos, pois ela é afetada pelos blobs maiores.
    # Consideramos só aqueles que estão próximos ou menores da mediana,
    # ou seja, há uma grande chance desses arrozes na primeira contagem serem apenas 1 e
    # não 2 ou mais grudados
    # (Menores pois há grãos bem pequenos nas imagens)
    contador = 0
    mediana = np.median(estatisticas, axis=0)
    grudados = []
    grudados_mediana = []
    for i in range(len(estatisticas)):
        # Se for próximo ou menor do que o valor da mediana, adicionamos na contagem e guardamos em
        # um grupo. Se não for, é muito possível que seja um blob com vários grãos grudados.
        # Guardamos em outro grupo.
        total = round(estatisticas[i][4] / mediana[4])
        if total <= 1:
            contador += 1
            grudados_mediana.append(estatisticas[i])
        else:
            grudados.append(estatisticas[i])

    # foi utilizado a termo reta e distancia aq, porem ela se refere a funções e numeros na
    # estatisticas, e não tem muito haver com a distancia euclidiana de geometria. é apenas uma
    # abstração
    # nessa parte é calculado o coeficiente de pearson, que esta relacionado a regressão linear
    # quanto mais proximo do 1, mais o valor esta perto da reta da regrassão
    pearson = np.corrcoef(estatisticas)
    # aqui é calculado a media de todos os elementos da regressão
    medias_pearson = np.mean(pearson, axis=0)
    # aqui pegamos a media das medias da regrassão, para ter um valor global, em vez de 1 valor
    # pra cada rotulo
    media = np.mean(medias_pearson)
    # o coeficiente de pearson indica a porcetagem que esta perto da reta, diminuimos 1-ele(coeficiente)
    # para saber a diferença/distancia entre ele e a reta em vez do aproximação total
    coeficiente = 1 - media
    # Nessa segunda contagem, temos dois grupos: os grãos que já foram contados
    # (muito possivelmente são apenas 1 grão) e os grãos que ainda não foram
    # (muito possivelmente são 2 ou mais grudados). Dessa forma, calculamos uma nova mediana,
    # porém utilizando apenas os blobs que tem maior possibilidade de ser apenas um grão, os
    # já contados e assim temos uma mediana um pouco mais precisa, nos dando a área de um arroz
    # um pouco mais precisa.
    if len(grudados) > 0:
        nova_mediana = np.median(grudados_mediana, axis=0)
        for i in range(len(grudados)):
            # Aqui simplesmente pegamos o quantas vezes maior aproximdamente é aquele blob
            # da mediana e somamos esse valor no contador de arroz.
            # foi notado que o valor de "total" para alguns blobs foi arredondado para baixo
            # por muito pouco (Ex.: X.49, e arredondava para baixo). Como esse método da mediana
            # não da certeza sobre a area de um grão, ele não é tão preciso, portanto adicionamos
            # um numero pequeno, só pra jogar esses que ficam muito próximos do meio pra cima.
            # para saber que numero utilizar, usamos a distancia que falta do numero pra reta
            # da regrassão linear
            total = round((grudados[i][4] / nova_mediana[4]) + coeficiente)
            contador += total

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
            print(f"{filename} -> {quantidade} componentes")


if __name__ == "__main__":
    main()
