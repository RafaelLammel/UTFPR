/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agmochila;

import static agmochila.AGCOperador.selecionarPorRoleta;

/**
 * Esta classe é a principal. Contém as configurações para execução do
 * algoritmo.
 *
 * @author Tacla (UTFPR, Curitiba)
 */
public class AGMochila {

    // PARÂMETROS DE CONFIGURAÇÃO DO AG 
    /**
     * número máximo de execuções
     */
    private static final int MAX_EXECUCOES = 10;
    /**
     * tamanho da população = quantidade de indivíduos ou cromossomos
     * OBRIGATORIAMENTE UM NÚMERO PAR >= 2
     */
    private static final int TAM_POP = 700;
    /**
     * critério de parada: máximo de gerações a serem criadas
     */
    private static final int MAX_GERACOES = 500;
    /**
     * probabilidade de crossover entre 2 individuos [0, 1]
     */
    private static final float PROB_CROSSOVER = (float) 0.7;
    /**
     * probabilidade de mutação de um alelo: [0 ,1]
     */
    private static final float PROB_MUTACAO = (float) 0.1;

    // ESTRUTURAS PARA EXECUÇÃO DO AG
    /**
     * pais e filhos de uma iteração no vetor m de Mochilas; metade inicial do
     * vetor eh para os pais, metade final para os descendentes
     */
    protected Mochila m[] = new Mochila[2 * TAM_POP];
    /**
     * armazena o fitness dos melhores
     */
    private int[] fitness = new int[TAM_POP];
    /**
     * estrutura auxiliar para classificar individuos com quick sort
     */
    private Mochila mAux[] = new Mochila[2 * TAM_POP];

    /**
     * penalização: escolhe se é fitness com penalização (caso true). Caso
     * false, escolhe fitness+reparação
     */
    private static final boolean penalizacao = false;
    
    private static int[] melhorFitness = new int[MAX_GERACOES];
    private static int ct;

    /**
     * Cria uma população inicial contendo TAM_POP mochilas preenchidas com
     * itens aleatórios.
     */
    public AGMochila() {
        // cria uma populacao inicial de pais 
        for (int i = 0; i < TAM_POP; i++) {
            m[i] = new Mochila();
            m[i].encherMochilaAleatoriamente(); // solucao factivel
        }
    }

    /**
     * Realiza classificação dos indivíduos em ordem decrescente de fitness
     *
     * @param lowerIndex índice inferior
     * @param higherIndex índice superior
     */
    private void quickSort(int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;

        // calcula o pivot = meio do array
        Mochila pivot = mAux[lowerIndex + (higherIndex - lowerIndex) / 2];
        while (i <= j) {

            while (mAux[i].valor > pivot.valor) {
                i++;
            }
            while (mAux[j].valor < pivot.valor) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j) {
            quickSort(lowerIndex, j);
        }
        if (i < higherIndex) {
            quickSort(i, higherIndex);
        }
    }

    /**
     * Método auxiliar ao quickSort
     *
     * @param i mochila a ser trocada
     * @param j mochila a ser trocada
     */
    private void exchangeNumbers(int i, int j) {
        Mochila temp = mAux[i];
        mAux[i] = mAux[j];
        mAux[j] = temp;
    }

    /**
     * Classifica os individuos (vetor m[]) em ordem decrescente de valor
     * (fitness)
     */
    public void sort() {
        if (m == null || m.length == 0) {
            return;
        }
        this.mAux = m;
        quickSort(0, m.length - 1);
    }

    /**
     * Executa uma vez o Algoritmo Genético até que MAX_GERACOES sejam criadas.
     *
     * @return retorna a 1a. melhor mochila encontrada
     */
    public Mochila executarAG() {
        int geracao = 0;
        int[] sel;
        sel = new int[TAM_POP];
        int melhorFit = 0;
        Mochila melhorMochila = null;

        do {
            // seleciona por roleta individuos para reproducao entre a populacao
            for (int i = 0; i < TAM_POP; i++) {
                fitness[i] = m[i].valor;
            }
            sel = selecionarPorRoleta(fitness, TAM_POP);

            // crossover na populacao - faz crossover par a par na ordem da
            // selecao da roleta e gera os filhos na segunda metade do vetor m[]
            int a, b; // indices para guardar os dois filhos dos pais que cruzam
            int j = 0;
            while (j < TAM_POP) {
                // Faz crossover com os selecionados da populacao.
                // Cada crossover gera dois novos individuos

                // primeiro, clonamos os pais:
                a = j + TAM_POP;
                b = j + TAM_POP + 1;
                m[a] = m[sel[j]].clonar();   // clona individuo 1
                m[b] = m[sel[j + 1]].clonar(); // clona individuo 2

                // cruzamos os pais (clonados), modificando-os (e, assim gerando os filhos a e b)
                AGCOperador.crossoverUmPonto(m[a].colocado, m[b].colocado,
                        Mochila.QTD_ITENS_DISPON, PROB_CROSSOVER);
                j += 2;
            }
            // mutamos os filhos e recalculamos fitness
            for (int i = TAM_POP; i < 2 * TAM_POP; i++) {
                AGCOperador.mutar(m[i].colocado, Mochila.QTD_ITENS_DISPON, PROB_MUTACAO);

                // UM DOS DOIS: reparacao XOR penalizacao
                // fitness com reparação de indivíduos infactíveis
                if (penalizacao) {
                    m[i].calcularFitnessMochila(Mochila.PENALIZACAO);
                } else {
                    m[i].calcularFitnessMochila(Mochila.REPARACAO);
                }
            }

            // Selecao da nova populacao: seleciona os TAM_POP melhores de m[]
            // e coloca-os nas posicoes iniciais de m[] = nova populacao
            sort();
            if (m[0].valor > melhorFit) {
                melhorFit = m[0].valor;
                melhorMochila = m[0];
            }
            geracao++;
            //System.out.printf("ger. %03d: melhor fit = %02d\n", geracao, melhorFit);
            System.out.println(geracao + "," + melhorFit);
            melhorFitness[ct] = melhorFit;
        } while (geracao < MAX_GERACOES);
        return melhorMochila;
    }

    public static void main(String[] args) {
        Mochila best;
        ct = 0;
        do {
            AGMochila ag = new AGMochila();
            best = ag.executarAG();
            if (best != null) {
                System.out.println(best.imprimirMochilaCSV());
                best.imprimirMochila();
            }
            ct++;
            System.out.println("Total de execucoes = " + ct);
        } while (ct < MAX_EXECUCOES);
        float m = 0;
        for (int i = 0; i < 10; i++){
            System.out.println("Execução: " + (i+1) + " Best fitness: " + melhorFitness[i]);
            m += melhorFitness[i];
        }
        System.out.println("Média: " + (m/10));
    }
}
