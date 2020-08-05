- Deixe o arquivo main.py e a pasta "Imagens" (criar se não houver) no mesmo diretório
- Deixe a pasta "Processadas" dentro da pasta "Imagens" (criar se não houver)
- Coloque a imagem que deseja na pasta "Imagens"
- No arquivo main.py, altere as seguintes informações de acordo com o que deseja:
	- INPUT_IMAGEM: Nome do arquivo de imagem a ser processada, dentro da pasta "Imagens"
	- LARGURA_JANELA: Largura da janela do filtro
	- ALTURA_JANELA: Altura da janela do filtro
	- ALGORITMO: Algoritmo a ser realizado (0 - Ingenuo, 1 - Separaveis, 2 - Imagens Integrais)
- Execute o main.py, e a imagem processada será criada dentro da pasta "Processadas" dentro da pasta "Imagens"

Obs.: Desenvolvido em Python 3, depende das bibliotecas **[OpenCV](https://pypi.org/project/opencv-python/)** e **[NumPy](https://numpy.org/)**