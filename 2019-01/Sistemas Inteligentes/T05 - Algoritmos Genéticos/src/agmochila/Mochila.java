package agmochila;

import java.util.BitSet;
import java.util.Random;

/**
 * Uma instância desta classe representa uma mochila com seus itens, peso e
 * valor. 
 * VERSÃO ENUNCIADO: Os métodos de cálculo de fitness foram retirados!!!!
 *
 * @author tacla
 */
public class Mochila {
    /** capacidade máxima suportada pela mochila em Kg */
    public static final int CAPACIDADE_KG_MOCHILA = 113;
    /** quantidade de itens disponíveis para serem colocados na mochila */
    public static final int QTD_ITENS_DISPON = 42;
    /** peso de cada um dos itens de acordo com o fornecido no enunciado */
    int[] pesoItem = {3,  8, 12,  2,  8,  4,  4,  5,  1,  1,  8,  6,  4,  3, 
                      3,  5,  7,  3,  5,  7,  4,  3,  7,  2,  3,  5,  4,  3,
                      7, 19, 20, 21, 11, 24, 13, 17, 18,  6, 15, 25, 12, 19};
    /** valor de cada um dos itens de acordo com o fornecido no enunciado */
    int[] vlrItem = { 1,  3,  1,  8,  9,  3,  2,  8,  5,  1,  1,  6,  3,  2, 
                      5,  2,  3,  8,  9,  3,  2,  4,  5,  4,  3,  1,  3,  2,
                     14, 32, 20, 19, 15, 37, 18, 13, 19, 10, 15, 40, 17, 39};
    
    /** Gerador de número aleatórios para esta mochila */
    Random s = new Random();
    /** peso atual da mochila em Kg */
    int peso = 0;
    /** quantidade de itens atualmente na mochila */
    int qtdItens = 0; 
    /** valor atual da mochila = FITNESS = quanto maior, melhor */
    int valor = 0; 
    /** controle de itens que estão (ou não) na mochila */
    BitSet colocado = new BitSet(QTD_ITENS_DISPON);
    
    // TIPOS DE EXECUÇÃO: PENALIZAÇÃO DO FITNESS OU REPARAÇÃO
    public static final int PENALIZACAO = 0;
    public static final int REPARACAO = 1;
    
    /**
     * Cria uma mochila vazia.
     */
    Mochila() {
        colocado.clear();
    }

    /**
     * Cria um clone da mochila.
     * @return Mochila: mochila criada
     */
    Mochila clonar() {
        Mochila clone = new Mochila();
        for (int i = 0; i < QTD_ITENS_DISPON; i++) {
            clone.colocado.set(i, this.colocado.get(i));
        }
        clone.peso = this.peso;
        clone.qtdItens = this.qtdItens;
        clone.valor = this.valor;
        return (clone);
    }
    
    /**
     * Imprime valores da mochila separados por virgula na ordem
     * qtdItens, peso, valor, true or false 
     * O último valor indica se o iten está ou não na mochila
     * @return String: com os valores separados por vírgulas.
     */
    public String imprimirMochilaCSV() {
        String strCSV = String.valueOf(qtdItens) + "," + String.valueOf(peso) + "," + String.valueOf(valor);
        for (int i = 0; i < QTD_ITENS_DISPON; i++) {
            if (colocado.get(i)) {
                strCSV = strCSV.concat(",1");
            } else {
                strCSV = strCSV.concat(",0");
            }
        }
        return strCSV;
    }

    /**
     * Imprime a mochila com printfs.
     */
    public void imprimirMochila() {
        System.out.printf("Mochila  peso valor\n");
        System.out.printf("-------------------\n");
        for (int i = 0; i < QTD_ITENS_DISPON; i++) {
            if (colocado.get(i) == true) {
                System.out.printf("item[%2d] %4d %5d\n", i+1, pesoItem[i], vlrItem[i]);
            }
        }
        System.out.printf("---------------------\n");
        System.out.printf("Mochila com %3d ITENS\n", qtdItens);
        System.out.printf("Mochila com %3d KG\n", peso);
        System.out.printf("Mochila com %3d VALOR\n", valor);
        System.out.printf("---------------------\n");

    }

    /**
     * Este método é chamado após um descendente ser gerado por crossover e 
     * sofrer mutação no método {@link agmochila.AGMochila#executarAG() executarAG}
     * da classe AGMochila. <br>
     * Portanto, o peso e valor da mochila estão desatualizados.
     * A capacidade da mochila pode estar violada (não satisfeita).<br>
     * @param tipo PENALIZACAO chama o método que penaliza indivíduos infactíveis;
     *             REPARACAO chama o método que repara indivíduos infactíveis.
     */
    public void calcularFitnessMochila(int tipo) {
        // conta itens e soma seus pesos atualizando-os
        this.peso = this.valor = this.qtdItens = 0;
        for (int i = 0; i < QTD_ITENS_DISPON; i++) {

            if (colocado.get(i) == true) {
                this.peso += pesoItem[i];
                this.valor += vlrItem[i];
                this.qtdItens++;
            }
        }
        
        // penalizar ou reparar
        switch (tipo) {
            case PENALIZACAO:
                calcularFitnessPenalizacao();
                break;
            case REPARACAO:
                calcularFitnessReparacao();
                break;
        }    
    }
    /**
     * Este método recalcula o fitness de uma mochila e penaliza se a mochila
     * ultrapassar a capacidade máxima de peso permitida.
     */
    private void calcularFitnessPenalizacao() {
        // Penalizar indivíduos que violam a capacidade da MOCHILA
        // @to do 
        if(CAPACIDADE_KG_MOCHILA <= this.peso)
            this.valor = 0;
    }
    /**
     * Este método repara a mochila fazendo-a respeitar a capacidade máxima de peso.
     */
    private void calcularFitnessReparacao() {
        //@to do
        int i;
        while(CAPACIDADE_KG_MOCHILA <= this.peso){
            i = s.nextInt(QTD_ITENS_DISPON);
            if (colocado.get(i) == true) {
                this.peso -= pesoItem[i];
                this.valor -= vlrItem[i];
                this.qtdItens--;
                
            }
        }
    }

    /**
     * Preenche mochila com itens aleatórios sem violar a capacidade em KG
     */
    protected void encherMochilaAleatoriamente() {
        int item;
        boolean[] sorteado = new boolean[QTD_ITENS_DISPON];
        int qtdSorteados = 0;

        // controle dos numeros ja sorteados para poder sair quando todos
        // os itens tiverem sido sorteados
        for (int i = 0; i < sorteado.length; i++) {
            sorteado[i] = false;
        }

        do {
            item = s.nextInt(QTD_ITENS_DISPON);

            if (sorteado[item] == false) {
                qtdSorteados++;
                sorteado[item] = true;
                colocarItem(item); // sem repeticao e observando a capacidade
            }
        } while (peso < CAPACIDADE_KG_MOCHILA && qtdSorteados < QTD_ITENS_DISPON);
    }

    /**
     * Coloca item somente se já não estiver na mochila e houver capacidade
     * disponível.
     * @param item um inteiro que identifica o item a ser colocado
     * @return boolean: true quando consegue colocar o item, false caso não.
     */
    private boolean colocarItem(int item) {
        if (colocado.get(item) == false && (peso + pesoItem[item]) <= CAPACIDADE_KG_MOCHILA) {
            qtdItens++;
            peso += pesoItem[item];
            valor += vlrItem[item];
            colocado.set(item);
            return true;
        }
        return false;
    }

    /**
     * Retira um item da mochila.
     * @param  item a ser retirado
     * @return boolean: true se o item existia na mochila e foi satisfatoriamente
     * retirado; false caso não existisse.
     */
    private boolean retirarItem(int item) {
        if (colocado.get(item) == true) {
            qtdItens--;
            peso -= pesoItem[item];
            valor -= vlrItem[item];
            colocado.clear(item);
            return true;
        }
        return false;
    }
}
