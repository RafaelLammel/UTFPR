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
	```
	numpy.where(Img > limiar, objeto, fundo)
	```
# flood fill
1. percorre a imagem, procurando um pixel não rotulado
1. quando encontrar, usa o pixel como "semente"
1. "inunda" a imagem, marcando com o mesmo rótulo a semente, seus vizinhos, os vizinhos dos vizinhos, etc(recursividade, pilha)
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
## convolução
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


* Distribuição Gaussiana
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
* nitidez a esuqerda(borrar)	
	* efeito mais sutil que o filtro da media
	* filtro gaussiano
		* a distruibuição gaussiana é descrita por
			* um valor medio( mi)
			* um desvio padrão (sigma)
		* suavização gaussiana(borra)
		* filtro linear e espacial
		* kernel com mesmo sigmas podem dar resultados diferentes
		* separavel
        * o kernel se aproxima de uma distribuição gaussiana 2D alinhada aos eixos x e y
		* o kernel é simétrico em cada eixo
			* valores a esquerda do centro são iguais aos valores a direita do centro(mas espelhados)
		* o kernel é discreto, os valores formam uma aproximação de uma curva gaussicana
		 * é nromal usar valores, como +- 2sigma ou +- 1.5 sigma
		 	* kernel maior: mais proximo de uma curva gaussiana real
			* kernel menor: menos calculos
		* box blur X gaussian blur
			* o filtro gaussiano permite variações menores na quantidade de suavização
			* o filtro gaussiano não cria "fantasmas"
			* a interpretação do box bluer em uma linha, ele faz umas rampa
			* a interpretação do gaussiano em uma linha, ele faz subida suave
* nitidez a direita( realçar detalhes)
	* realçar os detalhes também realça ruidos
	* unsharp masking
		* cria uma versão borrada da imagem original
		* subtria a versão borrada da imagem original
			* o que resta é uma imagem contendo os detalhes removidos
		* verifica quais são os detalhes mais significativos
			* onde a diferença é maior que um limiar
		* soma os detalhes significativos a imagem original
			* as difrenças normalmente tem valores pequenos, então multiplicamos elas por um (alfa) antes da soma
	* unsharp masking: paremetros
		* sigma do filtro gaussiano
			* quanto maior, maiores são os "detalhes" realçados
		* limiar que indica o que é um detalhe significativo
			* quanto maior, mais "afiados" os detalhes precisam ser
		* multiplicador \alfa aplicado a difrençã antes da soma
			* quanto maior, mais realçados os detalhes são

# Distribuição Gaussiana
* visão computacional
* ruido de sensor de camera(desvio padrão)
* medir tamanho medio de um grão de arroz
* a area entre \mi -\delta e \mi+\delta é 0.6827(68.27%)
* utfpr CR normalizado (Z-score)

# bloom
* é um artefato(anomalia/erro) produzido por cameras reais
* o brilho de regiões claras da imagem "vaza"
* este efeito é criticado as vezes, porque:
	* é um artefato
	* não funciona como a visão humana
	* o uso as vezes é considerado exagerado
* geralmente é usado no pre-processamento
* ideia geral
	1. criamos uma mascara contendo as fontes de luz
		* usando pos-processamento, se utiliza o "bright-pass"
			* uma versão da imagem contendo apenas os pixels com intensidade ou luminancia mais alta
	1. borramos a mascara, fazendo a luz "vazar" para os arredores
		* a imagem "ideal" normanete é obtida borrando a imagem várias vezes, e somando as imagem borradas
			* suavização gaussiana, dobrando o valor de \sigma a cada imagem
			* otimização
				* a filtragem gaussina pode ser um pouco cara, especialmente para valores grandes de \sigma
				* seja uma imagem I com NxMpixels, borrada usando suavização gaussiana, com \sigma = x
				* seja uma imagem J com N/2 x M/2 pixels, borrada usando suavização gaussiana, com \sigma = x/2
				* I e J tem exatemente os mesmo dados(matematicamente)
				* em vez de dobrar o valor de \sigma a cada imagem, usamos o mesmo valor de \sigma, mas para uma imagem com metade da altura e largura
				* a imagem depois é reescalada para o tamanho original
	1. "colamos" a máscara sobre a imagem original
		* basta somar as duas imagens, precisa encontrar formas de evitar que o resultado fique muito "esotourado"
		* a soma pode ser ponderada por fatores de correção
			* g(x,y) = a*f(x,y)+B*mascara(x,y)
# redimensionamiento
* o borrão é por causa de escalar para uma resolução maior e n por causa do redimensionamiento

# morfologia
* tecnicas com base em teoria dos conjuntos
	* conjuntos de pixels
* operadores morfológicos básicos:
	*filetros espaciais não-lineares
* I (+) K
	* dilatação
	* engorda as regiões de pixels brancos 
	* se tem algum vizinho setado, saida 1, senão 0
		* em escala de cinza, recebe o maior valor sob o kernel
* I (-) K
	* erosão
	* afina as regiões de pixels brancos
	* se todos os vizinhos estão setados, saida 1, senão 0
		* em escala de cinza, recebe o menor valor sob o kernel
* todos os outros operadores morfológicos se baseiam na dilatação e na erosão
* "engorda"/"afina" so é valido se o pixel central esta setado no kernel
* retirar contornos
	* contorno interno: I-(I(-)K)
	* contorno externo: (I(+)K)-I
* abertura
	* erosão seguida de dilatação
	* (I(-)K)(+)K
	* remove ruidos(fora do blob)
	* suaviza o formato do blob
* fechamento
	* dilatação seguida de erosão
	* (I(+)K)(-)K
	* fecha buracos(ruido dentro do blob)
	* suaviza o formato do blob
* filtro da mediana binario
	* remove ruidos
	* em imagens binárias, a mediana de uma região é igual ao valor que mais aparece na região
	* somar os pixels em cada vizinhaça,e verificar se a soma é maior que o tamnho da região dividido por 2
	* imagens integrais
* kernel quadrado com pixel de interesse no centro
	* fica igual aos filtros para minimos e maximos locais
	* são separávereis
# anti-aliasing
* aliasing
	* ocorre por as imagens são discretas
	* efeito visuvel: a froteira entre certos objetos parece "serrilhada"
* anti-aliasing
	* tem como objetivo atenuar a aparencia serrilhada
	* a cor dos pixels na fronteira entre dois objetos pode ser uma mistura das cores dos objetos
* superamostragem(supersampling)
	* SSAA
	* renderizar a cena em uma resolução maior do que a final, reduzindo a imagem usando interpolação
	* produz os melhores resultados visiveis
	* alto custo computacional
	* muito usada quando o resultado não é apresentado em tempo real
* maior parte das técnicas envolve amostragem de pontos extras, no tempo ou no espaço
* post-processing anti-aliasing
	* tecnicas usada apos a renderização
	* geram imagem ja com a resolução final
	* são mais rapidas que outros tipos de tecnica
1. ideia
	* misturar os valor dos pixels
	* borrar a imagem
	* borra a imagem toda, suavizando todos os detalhes
2. ideia
	* borrar a iamgem apenas em algumas regiões
	* bordas e gradientes
* borda
	* descontinuidades
		* medir descontibuidades usando derivadas
* gradiente
	* conceito de derivada em amsi de uma dimensão
	* um ponto de descontinuidade(borda) é aquele cujo gradiente tem um valor muito alto ou baixo
	* medindo gradientes
	*kernel
		* horizontal {[-1,0,1]}
			* como esta passando pela horizontal, reage melhor a bordas verticais
		* vertical {[-1],[0],[1]}
			* como esta passando pela vertical, reage melhor a bprdas horizontaos
* filtros de sobel
	* pode ser interessante dividir as saidas por 4,58, 287, para manter tudo no intervalo [-1,1]
	* kernel 3x3 {[-1,0,1],[-2,0,2],[-1,0,1]}
		* separaravel
			* {[-1,0,1]} e {[1],[2],[1]}
* magnetude e orientação
	* os gradientes podem ser vistos como vetores(matematicos)
	* magnitude = sqrt(Dx^2+Dy^2)
		* valor maximo , sqrt(2)
		* limiar
			* oq tiver pra cima, considera borda
	* orientação arctan(Dy/Dx)
		* arctan2 para passar o quadrante
* alternativas
	* timar os maiores valores encontrados em cada canal
# HDR
* alcance dinamico
	* em imagens com subesposição, algumas regióes podem aparecer totalmente sem contraste(ficam totalmente pretas)
		* regiões muito escuras tentedem a ser muito contaminadas por ruído
	* em imagens com superexposição, o sensor pode saturar em algumas regiões(fica totalmente brancas)
		* a luz pode tb "vazar" e deixar outras regiões muito claras
* informamente, significa ter mais do que 8bpp por canal

* hardware
	* sensores com alcance dinâmico maior
	* saidas com 12 ou mais bpp por canal
	* resultados "diretos"
	* alto custo
* software
	* capturar imagens com diferentes graus de exposição
	* combinar as imagens em uma saida com 12 ou mais bpp por canal
# detecção de bordas
* borda:
	* descontinuidade na intesensidade
	* medir descontinuidade com gradientes
* limiarização com base na magnitede dos gradientes
	* os gradientes podem ficar forte em varios pixels vizinhos, fazendo as bordas aparecerem muito "grossas"
	* afinar borda
		* remover iterativamente pixels brancos, evitando:
			* remover "terminações" de linhas
			* separar um componente conexo
	* zhang-suen
		* remover a cada iteração pixels setados que tem varios outros pixels setados como vizinhos
	* pode ocorrer de bordas perto de outras bordas virarem uma borda só
* as bordas no gradiente
	* as bordas estão nas posições que possuem maginitude alta e em maximos locais
	* usa orientação para encontrar a direção da borda
	* borda verticais: orientação proxima de 0° ou 180°
	* borda horizontais: orientação proxima de 90° ou 270°
	
* detector de bordas de canny
	1. computa a magnitude e orientação dos gradientes
	1. quantiza a orientação em 4 direções
		* (quase) horizontal
		* (quase) vertical
		* (quase) diagonal com a direita acima
		* (quase) diagonal com a esquerda acima
	1. elimina ponstos que não são maximos locais na direção do gradiente
	1. limiarização com histerese
* limiarização com histerese
	* 2 limiares
	* magnitude acidma do limiar superior -> é borda
	* magnitude acima do limuar inferior -> não é borda
	* magnitude entre os dois limiares
		* borda, se houver um caminho conectando o pixel a outro pixel de borda
			* vizinho 8, inundação
	* evita bordas quebradas
* imagens coloridas
	* converti pra escala de cinza
	* computar o gradiente para os 3 canais, selecionar aquele que tiver a maior magnitude
	* detectar bordas nos 3 canais independentemente, considerar a união das 3 imanges
* redução de ruidas
	* filtro da media
		* pode duplicar bordas,e podem modificar os gradientes
	* filtro gaussiano
		* bom
# hough
* ideia geral
	1. localizar retas em uma imagem, representando-as como entidades geometricas
* reta -> y=ax+b
* força bruta
	* para cada pixel pega todos pixel, traça uma reta e calcula quantos pixels estão abaixo da reta
	* alta complexidade computacional
	* pouca robustez a imprecisões
	* no final podem existir muitas retas duplicadas
* transformada
	* é uma função de conversão de dominios
* reta y=ax+b
	* b=y-xa
	* (1,1) -> b=1-a
	* (3,3) -> b=3-3a
	
* para cada pixel de borda
	* gera os valores de a e b de todas as retas q passam por (x,y)
* procura por cruzamentos no dominio parametrico
	* varias retas no domonio parametrico se cruzam em um ponto (a,b)
		* a reta definida por y=ax+b passa por varios pixels de borda
	* então, pontos do dominio parametrico onde varias retas se cruzam descrevem provaveies retas no dominio espacial
* dominio (a,b) é problematico
	* a equeção y=ax+b não permite representar retas verticais
	* os valores de b podem variar muito(são potencialmente infinitos)o que imprede a geração de todas as retas que passam por um ponto
* a reta é descrita por um angulo THETA e uma distancia r
	* (0,10) -> r=10*sen(theta)
	* (10,0) -> r=10*cos(theta)
	* valor theta em radianos
		* valor theta entre 0 e 2pi, descartando r negativo
	* montamos um histograma 2d, com cada compartimento correspondendo a uma combinação (r,theta)
		* a largura dos compartimentos diz quantos pixeis sob uma reta precisam estar alinhados
		
* algoritmo simples
	1. separamos as retas detectadas em aproximadamente horizontais e aproximadamente verticais
	1. encontramos para as retas aproximadamente horizontais o ponto medio de cruzamento com o eixo y
	1. separamos as retas entre aquelas que estão acimas e abaixo do ponto medio computado
	1. repetimos o processo para as retas aproximadamente verticais
