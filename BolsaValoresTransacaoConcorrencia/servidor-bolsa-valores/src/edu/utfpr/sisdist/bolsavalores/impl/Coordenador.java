package edu.utfpr.sisdist.bolsavalores.impl;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import edu.utfpr.sisdist.bolsavalores.model.Cliente;
import edu.utfpr.sisdist.bolsavalores.model.Transacao;
import edu.utfpr.sisdist.bolsavalores.remote.InterfaceCo;

public class Coordenador implements InterfaceCo {

    private String status = "NÃO INICIADA";
    private String Id = UUID.randomUUID().toString();

    @Override
    public boolean abrirTransacao(Transacao compra, Transacao venda, Cliente comprador, Cliente vendedor) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        // Gravar log Transação iniciada
        this.status = "INICIADA";
        boolean retorno = false;
        try {
            File logCoordenador = new File("Log_TransacoesCoordenador.txt");
            logCoordenador.createNewFile();
            FileWriter fileWriter = new FileWriter("Log_TransacoesCoordenador.txt", true);
            fileWriter.write(dtf.format(LocalDateTime.now()) + " - " + this.Id + " - " + this.status + "\n");
            if(comprador.preparar(venda, 0, this) && vendedor.preparar(compra, 1, this)) {
                // Gravar log Transação preparada
                this.status = "PREPARADA";
                fileWriter.write(dtf.format(LocalDateTime.now()) + " - " + this.Id + " - " + this.status + "\n");
                if(comprador.efetuar(this) && vendedor.efetuar(this)) {
                    this.status = "EFETUADA";
                    fileWriter.write(dtf.format(LocalDateTime.now()) + " - " + this.Id + " - " + this.status + "\n");
                    retorno = true;
                    comprador.registrarLogEfetuado(this);
                    vendedor.registrarLogEfetuado(this);
                }
                else{
                    this.status = "ABORTADA";
                    comprador.abortar(this);
                    vendedor.abortar(this);
                    fileWriter.write(dtf.format(LocalDateTime.now()) + " - " + this.Id + " - " + this.status + "\n");
                }
            }
            else{
                this.status = "ABORTADA";
                comprador.abortar(this);
                vendedor.abortar(this);
                fileWriter.write(dtf.format(LocalDateTime.now()) + " - " + this.Id + " - " + this.status + "\n");
                fileWriter.close();
            }
            fileWriter.close();
            return retorno;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public String obterEstadoTransacao() {
        return this.status;
    }

    public String getId() {
        return this.Id;
    }
    
}
