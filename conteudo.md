* processamento de imagens
   * imagem->imagem, pega uma imagem e processa ela em varios sentidos dependendo do contexto, junta uma na outra, tira fundo
   * usado em aparelhos eletronicos(telas),imagens medicas, geoprocessamento, visualização de imagensde experimento cientifico, inspeção de estradas de ferros
* visão computaciona
   * imagem->dados,informações, pega uma imagem ja processada e tira informações e dados dela,catalogando objetos, analise meteorológicas, reconhecimento facial
* computação grafico
   * imagem->imagem->pega uma imagem e coloca nela algo gerado por um algoritmo(gerado apenacomputacionalmente), pega uma imagem/video sem fogo, gerar o fogo por algoritmos e colocar ele naimagem
* representação de imagens f(x,y) f[y][x] f(x,y)=y é o mesmo que f[y][x]=y gerando uma imagem degradê
* 1pixel = 1 bytes(8bpp)
* R(vermelho),G(verde), B(azul)
	* Cada pixel f(x,y) é associado a uma tupla de 3 valores(f:Z2→Z3).f(x,y)=(r,g,b)
	* Img[c][y][x]
	* O modo mais usado tem 1 byte para cada canal (24bpp)
	* podem ser representadas2563cores pelo rgb
* escala de cinza
	* mais simples de trabalhar
	* conversão simples = (r+g+b)/3
	* metodo mais preciso  i = (0.299r + 0.587g + 0.114b) o
		* os pesos vem da sensibilidade dos olhos, comosomos mais sensiveis ao verde ele tem mais peso
* contar objetos
	* binarização+rotulagem
		* n muito boa
	* binarização+segmentação+componente conexo
		0. segmentaçã0
			* dividir os pixel em regiões
		0. binarização
			* dividir os pixel em duas partes: fundo e frente, objetos de interesse e o resto(é um tipo de segmentação)
		0. componente conexo
			* colocar um blob(componente/parte de interesse) separados por uma parte q n é de interesse
			* vizinho 4 ou 8
		0. rotulagem
			* marcar cada blob com um identificador
* binarização com limiar grobal


# processamento de imagens
* imagem->imagem, pega uma imagem e processa ela em varios sentidos dependendo do contexto, junta uma na outra, tira fundo
* usado em aparelhos eletronicos(telas),imagens medicas, geoprocessamento, visualização de imagensde experimento cientifico, inspeção de estradas de ferros
 visão computaciona
* imagem->dados,informações, pega uma imagem ja processada e tira informações e dados dela,catalogando objetos, analise meteorológicas, reconhecimento facial
# computação grafico
* imagem->imagem->pega uma imagem e coloca nela algo gerado por um algoritmo(gerado apenacomputacionalmente), pega uma imagem/video sem fogo, gerar o fogo por algoritmos e colocar ele naimagem
# representação de imagens f(x,y) f[y][x] f(x,y)=y é o mesmo que f[y][x]=y gerando uma imagem degradê
* 1pixel = 1 bytes(8bpp)
* R(vermelho),G(verde), B(azul)
* Cada pixel f(x,y) é associado a uma tupla de 3 valores(f:Z2→Z3).f(x,y)=(r,g,b)
* Img[c][y][x]
* O modo mais usado tem 1 byte para cada canal (24bpp)
* podem ser representadas2563cores pelo rgb

# escala de cinza
* mais simples de trabalhar
* conversão simples = (r+g+b)/3
* metodo mais preciso  i = (0.299r + 0.587g + 0.114b) o
	* os pesos vem da sensibilidade dos olhos, comosomos mais sensiveis ao verde ele tem mais peso
# contar objetos
* binarização+rotulagem
	* n muito boa
* binarização+segmentação+componente conexo
	0. segmentaçã0
		* dividir os pixel em regiões
	0. binarização
		* dividir os pixel em duas partes: fundo e frente, objetos de interesse e o resto(é um tipo de segmentação)
	0. componente conexo
		* colocar um blob(componente/parte de interesse) separados por uma parte q n é de interesse
		* vizinho 4 ou 8
	0. rotulagem
		* marcar cada blob com um identificador
# binarização com limiar grobal
	* é uma operação por pixel
	```
	para cade linha y
		para cada linha x
			se Img[y][x] > T
				Img[y][x] = objeto
			senão
				Img[y][x] = fundo
	```
	* em python
		```python
		numpy.where(Img > limiar, objeto, fundo)
		```
# flood fill
	1. percorre a imagem, procurando um pixel não rotulado
	1. quando encontrar, usa o pixel como "semente"
	1 "inunda" a imagem, marcando com o mesmo rótulo a semente, seus vizinhos, os vizinhos dos vizinhos, etc(recursividade, pilha)
	1. para quando não encontrar mais pixels não rotulados conectados a semente, volta ao passao *1*

# normalização
	* remapeia as intensidades dos pixels para outra faixa de valores(tipo uma trasnformação linear)
	* as vezes é chamada de alargamento de contraste(contrast stretching) ou de histograma(na verdade usa um histograma)
	* acentua o contraste, mas não cria novos dados
	* se a imagem original tinha n valores diferentes, a imagem normalizada tambem tem n valores diferentes
	* para evitar problemas causados por extremos raros, podemos descartar uma porcentagem dos pixels com valores extremos
	
![formula](https://render.githubusercontent.com/render/math?math=\LARGE%20g(x,y)=%20\frac{f(x,y)-min_i}{max_i%20-%20min_i}(max_o%20-%20min_o%20)+%20min_o)

<img src="https://render.githubusercontent.com/render/math?math=g(x,y)=\LARGE \frac{f(x,y)-min_i}{max_i - min_i}(max_o - min_o )+ min_o">

* legenda
	* min_i = pixel minimo da imagem
	* max_i = pixel maximo da imagem
	* min_o = minimo da saida(faixa)
	* max_o = maximo da saida(faixa)
	* o histograma de uma imagem é uma contagem de quantos pixels têm intensidade dentro de cada faixa
		* para imagens com 8bpp é comun usar 256 faixars
		* para imagens com 24bpp RGB, podemos usar 3 histogramas com 256 faixas, ou um histograma 3D com 256^3 faixas
		* para imagens com valores não-inteiros, precisamos definir faixas

# cores e ajustes
brilho, constraste e gamma
* operação pixel a pixel
* imagens RGB: aplicar nos 3 canais
* curva de resposta
## brilho 
* diminui: imagem mais escura
* aumenta: imagem mais clara
* g(x,y) = f(x,y)+B
    * soma uma constante
    * no histograma empurra o histograma pra dereita
## constraste
* g(x,y) = f(x,y)*C+B
    * multiplicador
    * diminuir o constraste 
        * deixa a imagem mais escura
        * diminui os espaço entre os pixel,
        * empurra pra esquerdar no histograma
        * escuros modificam um pouco
    * aumentar o contraste
        * deixa imagem mais clara
        * aumenta espaço entre os pixels(histograma)
        * empurra pra direira um pouco, mas achata o histograma
    * contraste menor que 1
        * claros caem
        * escuros mudam pouco
        * imagem mais escura
    * contraste maior que 1
        * imagem fica mais clara
        * pixeis claros estoram(apenas eles)

deslocando o contraste pra meio g(x,y) = (f(x,y)-0.5)*C +0.5+B
    * na real n entendi muito bem essa parte do que acontece na imagem
* diferente de alargamento de contraste
    * alargamento: olha o conteudo da imagem e aumenta ou diminui com base no conteudo
    * aq ele é global, aumenta ou baixa tudo
## gama, correção gama
* g(x,y) = f(x,y)^(gama)
* realçar contraste 
* aumentar gama(maior que 1)
    * diminuir o espaçamento entre valores que são baixo, e aumentar os que altos
    * aumenta constraste nas regiões claras
    * diminui contraste nas regiões escuras
* diminuir gama(menor que 1)
    * aumenta espaçamento entre valores baixo, e diminui os altos
* gama baixo ve bem na parte escura
* gama alto ve bem na parte clara
# saturação
* aumentar 
    * cores mais vivas
* diminuir 
    * cores fracas, acinzentados
* rgb é a maneira que vemos cor mas n é a que descrevemos
* exemplo bola vermelha, um pouco laranja, cor forte/viva, n é particulamente clara ou escura
* HSL
    * H
        * eixo horizontal do quadrado
        * matiz
        * tonalidade de cor
        * cor predominante
        * ciclico
    * S
        * saturação
        * eixo vertical do quadrado
        * intensidade
    * L
        * luminancia
        * claro/escuro
        * aumenta: tudo branco
        * diminui: tudo preto 
        * meio: cor pura
        * separados em canais é uma versão da escala de cinxa da imagem
* HSL para RGB
    * conversão sem perda(teoricamente)
* luminancia é parecido com o brilho, mas preserva a saturação e a matiz
* saturação baixo -> matiz não confiavel
    * parecido com cinza
* luminancia alta ou baixa -> saturação e matiz não confiavel
    * luminancia baixa é preto
    * luminancia alto é branco
## espaços de cores
* RGB+a 
    * usa 32bpp ao invez de 24
    * a é umas transparencia
* HSV
    * V baixo é preto porem alto é a cor pura
*YCbCr
    * versão digital do cabo componente
    * Y' versão escala de cinza
    * ele sepera informações de cor

# matiz

# filtro gausiano
## [convolução](https://youtu.be/4G8F_fB71Rg)
* filtragem no dominio espacial
	* filtros com janelas deslizantes
	* dominio espacial
		 * no espaço da imagem
	* divido em 2 grupos
		* *lineares* considerando a,b e c iamgens
		a+b=c
		F(a)+F(b)=F(a)+F(b)=F(a+b)=F(c)
		"o filtro é linear se a saida no centro da janela for uma soma ponderada dos valores dentro da janela na imagem de entrada"
		matriz de coeficientes(kernel)
		normalmente filtra os 3 canais da imagem independente
		*media* linear
		* *não lineares* seletor
		*mediana/minimo/maximo* não linear
* aplicar um filtro linear espacial
* "convolução é um operador linear que, a partir de duas funções 
	dadas,resulta nume terceira que mede a área subentendida pela 
	superposição damesmas em função do deslocamento existente entre elas"
	* operador linear = filtro linear
	* duas funções dadas = imagem e kernel
	* resulta numa terceira = imagem filtrada
*	 deslocamenteo = janela deslizante
	* superposição = aplicação do kernel na imagem
* utilidades
	* reberveração(audio)
	* simulação de caixa acustica
	* deep learning com redes convolucionais
		* aprende filtros


* [Distribuição Gaussiana](https://youtu.be/_RwOy023br0)
	* visão computacional
	* ruido de sensor de camera(desvio padrão)
	* medir tamanho medio de um grão de arroz
	* a area entre \mi -\delta e \mi+\delta é 0.6827(68.27%)
	* utfpr CR normalizado (Z-score)

* nitidez
    * controle
        * meio
             * imagem original
        * nitidez a esquerda
            * imagem ligeramente borrada
        * nitidez a direita 
            * imagem com detalhes realçados
	* efeito mais sutil que o filtro da media
	* filtro gaussiano
		* suavização gaussiana
		* filtro linear e espacial
		* kernel com mesmo sigmas podem dar resultados diferentes
		* separave
        * o kernel se aproxima de uma distribuição gaussiana 2D alinhada aos eixos x e y

# [Distribuição Gaussiana](https://youtu.be/_RwOy023br0)
* visão computacional
* ruido de sensor de camera(desvio padrão)
* medir tamanho medio de um grão de arroz
* a area entre \mi -\delta e \mi+\delta é 0.6827(68.27%)
* utfpr CR normalizado (Z-score)
