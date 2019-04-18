package sistema;

import ambiente.*;
import arvore.TreeNode;
import arvore.fnComparator;
import problema.*;
import comuns.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

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
    int plan[];
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
        if(ct == 0){
            plan = custoUniforme();
            System.out.print("Solucao: " + plan[0]);
            for(int i = 1; i < plan.length; i++){
                System.out.print(" - " + plan[i]);
            }
            System.out.println("\n--------------------------------------------------------------------");
        }
        if(prob.testeObjetivo(estAtu)){
            System.out.println("estado atual: " + estAtu.getString());
            System.out.println("Chegou no objetivo!");
            return -1;
        }
        System.out.println("estado atual: " + estAtu.getString());
        System.out.println("proxima acao do plano: " + plan[ct]);
        executarIr(plan[ct]);
        Estado novoEstado = prob.suc(estAtu, plan[ct]);
        estAtu = novoEstado;
        System.out.println();
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
    
    public int[] custoUniforme(){
        // Iniciando o nó
        TreeNode node = new TreeNode(null);
        node.setGn(0);
        node.setState(estAtu);
        // Inciando a fronteira (fila de prioridades)
        Queue<TreeNode> fronteira = new PriorityQueue<>(50, new fnComparator());
        fronteira.add(node);
        // Iniciando o Set de explorados
        Set<TreeNode> explorados = new HashSet<>();
        do{
            node = fronteira.poll();
            if(prob.testeObjetivo(node.getState())){
                List<Integer> listaSolucao = new ArrayList<>();
                while(node.getParent() != null){
                    listaSolucao.add(node.getAction());
                    node = node.getParent();
                }
                int[] solucao = new int[listaSolucao.size()];
                for (int i = 0; i < listaSolucao.size(); i++)
                    solucao[i] = listaSolucao.get(listaSolucao.size()-i-1);
                return solucao;
            }
            explorados.add(node);
            int[] acoes = prob.acoesPossiveis(node.getState());
            for(int i = 0; i < 8; i++){
                if(acoes[i] != -1){
                    TreeNode child = node.addChild();
                    child.setAction(i);
                    child.setState(prob.suc(node.getState(), i));
                    child.setGn(prob.obterCustoAcao(node.getState(), i, child.getState())+node.getGn());
                    TreeNode f = existeNaFronteira(fronteira,child);
                    TreeNode e = jaExplorado(explorados,child);
                    if(f == null && e == null){
                        fronteira.add(child);
                    }
                    else if (f != null && f.getGn() > child.getGn()){
                        fronteira.remove(f);
                        fronteira.add(child);
                    }
                }
            }
        }while(!fronteira.isEmpty());
        return null;
    }
    
    public TreeNode existeNaFronteira (Queue<TreeNode> f, TreeNode node){
        for(TreeNode n : f)
            if(n.getState().igualAo(node.getState()))
                return n;
        return null;
    }
    
    public TreeNode jaExplorado(Set<TreeNode> e, TreeNode node){
        for(TreeNode n : e)
            if(n.getState().igualAo(node.getState())){
                return n;
            }
        return null;
    }
    
}