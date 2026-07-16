package org.example.model;

public class Endereco {
    private String rua;
    private String bairro;
    private int numero;
    private Integer id;

    public Endereco() {
        this.id = 0;
        this.rua = "";
        this.bairro = "";
        this.numero = 0;
    }

    public Endereco(String rua, String bairro, int numero) {
        this.id = 0;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", numeor=" + numero +
                ", id=" + id +
                '}';
    }
}
