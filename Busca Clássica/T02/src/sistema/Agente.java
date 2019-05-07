package sistema;

import ambiente.*;
import problema.*;
import comuns.*;
import static comuns.PontosCardeais.*;

/**
 *
 * @author tacla
 */
public class Agente implements PontosCardeais {
    /* referência ao ambiente para poder atuar no mesmo*/
    Model model;
    Problema prob; // formulacao do problema
    Estado estAtu; // guarda o estado atual (posição atual do agente)
    /* @todo T2: fazer uma sequencia de acoes a ser executada em deliberar
       e armazena-la no atributo plan[]
    */
    int plan[] = {N,N,N,N,L,L,L,L,L,L,L,NE,N};
    double custo;
    static int ct = -1;
           
    public Agente(Model m) {
        this.model = m;
        prob = new Problema();
        
        //@todo T2: Aqui vc deve preencher a formulacao do problema 
        
        //@todo T2: crencas do agente a respeito do labirinto
        prob.criarLabirinto(9, 9);
        prob.crencaLabir.porParedeVertical(0, 1, 0);
        prob.crencaLabir.porParedeVertical(0, 0, 1);
        prob.crencaLabir.porParedeVertical(5, 8, 1);
        prob.crencaLabir.porParedeVertical(5, 5, 2);
        prob.crencaLabir.porParedeVertical(8, 8, 2);
        prob.crencaLabir.porParedeHorizontal(4, 7, 0);
        prob.crencaLabir.porParedeHorizontal(7, 7, 1);
        prob.crencaLabir.porParedeHorizontal(3, 5, 2);
        prob.crencaLabir.porParedeHorizontal(3, 5, 3);
        prob.crencaLabir.porParedeHorizontal(7, 7, 3);
        prob.crencaLabir.porParedeVertical(6, 7, 4);
        prob.crencaLabir.porParedeVertical(5, 6, 5);
        prob.crencaLabir.porParedeVertical(5, 7, 7);
               
        //@todo T2: crencas do agente: Estado inicial, objetivo e atual
        // utilizar atributos da classe Problema
        prob.defEstIni(8, 0);
        prob.defEstObj(2, 8);
        estAtu = new Estado(8,0);
        
    }
    
    /**Escolhe qual ação (UMA E SOMENTE UMA) será executada em um ciclo de raciocínio
     * @return 1 enquanto o plano não acabar; -1 quando acabar
     */
    public int deliberar() {
        ct++;
        //@todo T2: executar o plano de acoes: SOMENTE UMA ACAO POR CHAMADA DESTE METODO
        // Ao final do plano, verifique se o agente atingiu o estado objetivo verificando
        // com o teste de objetivo
        System.out.println("estado atual: " + estAtu.getString());
        System.out.print("açoes possiveis: {");
        int[] acoes = prob.acoesPossiveis(estAtu);
        for (int i = 0; i < 8; i++)
            if(acoes[i] != -1)
                System.out.print(acao[i]+" ");
        System.out.println("}");
        System.out.print("ct: " + (ct+1) + " de " + plan.length);
        System.out.println(" ação escolhida: " + acao[plan[ct]]);
        executarIr(plan[ct]);
        Estado novoEstado = prob.suc(estAtu, plan[ct]);
        custo+=prob.obterCustoAcao(estAtu, plan[ct], novoEstado);
        System.out.println("custo ate o momento: " + custo);
        estAtu = novoEstado;
        if(prob.testeObjetivo(estAtu)){
            return -1;
        }
        //@todo T2: imprimir o que foi pedido
        
        
        return 1;
    }
    
    /**Funciona como um driver ou um atuador: envia o comando para
     * agente físico ou simulado (no nosso caso, simulado)
     * @param direcao N NE S SE ...
     * @return 1 se ok ou -1 se falha
     */
    public int executarIr(int direcao) {
        model.ir(direcao);
        return 1; 
    }   
    
    // Sensor
    public Estado sensorPosicao() {
        int pos[];
        pos = model.lerPos();
        return new Estado(pos[0], pos[1]);
    }
}    

