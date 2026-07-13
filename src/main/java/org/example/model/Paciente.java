package org.example.model;

import java.util.ArrayList;

public class Paciente {
    private String nome;
    private String cpf;
    private Integer id;
    private ArrayList<Telefone> telefones;
    private Endereco endereco;
    private ArrayList<Atendimento> atendimentos;

    public Paciente(String nome, String cpf) {
        this.id = 0;
        this.nome = nome;
        this.cpf = cpf;
        this.telefones = new ArrayList<>();
        this.endereco = new Endereco();
        this.atendimentos = new ArrayList<>();
    }

    public Paciente(String nome, String cpf, String rua, String bairro, int numero) {
        this.id = 0;
        this.nome = nome;
        this.cpf = cpf;
        this.telefones = new ArrayList<>();
        this.endereco = new Endereco(rua, bairro, numero);
        this.atendimentos = new ArrayList<>();
    }

    public Paciente(String nome, String cpf, String rua, String bairro, int numero, ArrayList<Telefone> telefone) {
        this.id = 0;
        this.nome = nome;
        this.cpf = cpf;
        this.telefones = telefone;
        this.endereco = new Endereco(rua, bairro, numero);
        this.atendimentos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(ArrayList<Telefone> telefones) {
        this.telefones = telefones;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public ArrayList<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(ArrayList<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
    }

    public void adicionarTelefone(String telefone){
        Telefone t = new Telefone(telefone);
        telefones.add(t);
    }

    public void adicionarTelefone(String rua, String bairro, int numero){
        this.endereco = new Endereco(rua, bairro, numero);
    }

    public void adicionarAtendimento(Integer medico_id, Integer paciente_id, String data){
        Atendimento atendimento = new Atendimento(medico_id, paciente_id, data);
        atendimentos.add(atendimento);
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", id=" + id +
                ", telefones=" + telefones +
                ", endereco=" + endereco +
                ", atendimentos=" + atendimentos +
                '}';
    }
}
