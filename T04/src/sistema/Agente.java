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
import java.util.Scanner;
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
            System.out.println("Qual busca deseja fazer?"
                    + "\n1: Busca Custo Uniforme"
                    + "\n2: Busca A*");
            Scanner scan = new Scanner (System.in);
            String escolha = "1";
            do{
                escolha = scan.nextLine();
                if(!(escolha.equals("1") || escolha.equals("2")))
                    System.out.println("Escolha invalida! Digite novamente.");
            }while(!(escolha.equals("1") || escolha.equals("2")));
            if(escolha.equals("1"))
                plan = custoUniforme();
            else
                plan = buscaAEstrela();
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
                    TreeNode f = null;
                    TreeNode e = null;
                    for(TreeNode n : fronteira)
                        if(n.getState().igualAo(child.getState())){
                            f = n;
                            break;
                        }
                    for(TreeNode m : explorados)
                        if(m.getState().igualAo(child.getState())){
                            e = m;
                            break;
                        }
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
    
    // Busca A*
    public int[] buscaAEstrela(){
        //Inicializando a lista aberta e fechada
        List<TreeNode> aberta = new ArrayList<>();
        List<TreeNode> fechada = new ArrayList<>();
        TreeNode atual = new TreeNode(null);
        atual.setGn(0);
        atual.setHn(0);
        atual.setState(estAtu);
        //Adicionando o nó inicial na lista aberta
        aberta.add(atual);
        while(!aberta.isEmpty()){
            //Pegando o nó com menor F(n) na lista aberta e o colocando na lista fechada;
            atual = aberta.get(0);
            for(int i = 0; i < aberta.size(); i++)
                if(atual.getFn() > aberta.get(i).getFn())
                    atual = aberta.get(i);
            aberta.remove(atual);
            fechada.add(atual);
            //Verifica se o nó atual é o objetivo
            if(prob.testeObjetivo(atual.getState())){
                List<Integer> listaSolucao = new ArrayList<>();
                while(atual.getParent() != null){
                    listaSolucao.add(atual.getAction());
                    atual = atual.getParent();
                }
                int[] solucao = new int[listaSolucao.size()];
                for (int i = 0; i < listaSolucao.size(); i++)
                    solucao[i] = listaSolucao.get(listaSolucao.size()-i-1);
                return solucao;
            }
            //Gerando os filhos
            int[] acoes = prob.acoesPossiveis(atual.getState());
            List<TreeNode> filhos = new ArrayList<>();
            for(int i = 0; i < 8; i++){
                if(acoes[i] != -1){
                    TreeNode child = atual.addChild();
                    child.setAction(i);
                    child.setState(prob.suc(atual.getState(), i));
                    filhos.add(child);
                }
            }
            //Passando pelos filhos criados e verificando se eles vão para a lista aberta
            for(TreeNode f : filhos){
                for (TreeNode c : fechada)
                    if (c.getState().igualAo(f.getState()))
                        continue;
                f.setGn(prob.obterCustoAcao(atual.getState(), f.getAction(), f.getState())+atual.getGn());
                f.setHn(h1(f.getState(),prob.estObj));
                for(TreeNode a : aberta)
                    if(f.getState().igualAo(a.getState()) && f.getGn() > a.getGn())
                        continue;
                aberta.add(f);
            }
        }
        return null;
    }
    
    //Manhattan Distance
    public float h1(Estado no, Estado objetivo){
        float dx = Math.abs(no.getCol()-objetivo.getCol());
        float dy = Math.abs(no.getLin()-objetivo.getLin());
        float D = 1;
        return D*(dx+dy);
    }
    
    //Diagnoal
    public float h2(Estado no, Estado objetivo){
        float dx = Math.abs(no.getCol()-objetivo.getCol());
        float dy = Math.abs(no.getLin()-objetivo.getLin());
        float D1 = 1, D2 = 1.5f;
        float menor;
        if(dx>dy)
            menor = dy;
        else
            menor = dx;
        return D1 *(dx+dy) + (D2-2*D1) * menor;
    }
    
}