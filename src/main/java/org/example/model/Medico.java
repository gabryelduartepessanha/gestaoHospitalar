package org.example.model;

import java.util.ArrayList;

public class Medico {
    private Integer id;
    private String nome;
    private String crm;
    private ArrayList<Atendimento> atendimentos;

    public Medico(String nome, String crm) {
        this.id = 0;
        this.nome = nome;
        this.crm = crm;
        this.atendimentos = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public ArrayList<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(ArrayList<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
    }

    public void adicionarAtendimento(int medico_id, int paciente_id, String data){
        Atendimento atendimento = new Atendimento(medico_id, paciente_id, data);
        atendimentos.add(atendimento);
    }
}
