package edu.utfpr.servidor.ultrassom.request;

public class ProcessRequest {
    
    private double[] data;
    private int usuario_id;
    private String algoritmo;
    private int largura;
    private int altura;

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
    private int amostras;
    private int sensores;

    public int getAmostras() {
        return amostras;
    }

    public void setAmostras(int amostras) {
        this.amostras = amostras;
    }

    public int getSensores() {
        return sensores;
    }

    public void setSensores(int sensores) {
        this.sensores = sensores;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }
    

    
    
}
