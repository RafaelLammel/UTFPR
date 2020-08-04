# aula 1

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
# aula 2
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
* flood fill
	1. percorre a imagem, procurando um pixel não rotulado
	1. quando encontrar, usa o pixel como "semente"
	1 "inunda" a imagem, marcando com o mesmo rótulo a semente, seus vizinhos, os vizinhos dos vizinhos, etc(recursividade, pilha)
	1. para quando não encontrar mais pixels não rotulados conectados a semente, volta ao passao *1*

# aula 3
* normalização
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