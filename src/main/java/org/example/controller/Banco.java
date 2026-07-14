package org.example.controller;

import org.example.model.*;

import java.net.ConnectException;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Banco {
    private String usuario;
    private String senha;
    private String url;

    public Banco(){

    }

    public Connection conectar(){
        usuario="root";
        senha="root";
        url="jdbc:mysql://localhost:3306/vidanova";

        Connection conectado = null;

        try{
            conectado = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Banco de dados conectado com sucesso!");
        }catch(SQLException e){
            System.out.println("Não foi possível conectar ao banco de dados com essas informações.");
        }
        return conectado;
    }

    public void desconectar(Connection conexao) throws SQLException {
        conexao.close();
    }

    public void adicionar(Medico medico, Connection conexao){
        String sql = "insert into medico(nome, crm) values(?,?)";

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getCrm());

            int linhasAfetadas = stmt.executeUpdate();
            if(linhasAfetadas > 0){
                System.out.println("O médico foi cadastrado com sucesso!");
            }

            stmt.close();
        }catch(SQLException e){
            System.out.println("Aconteceu um erro ao tentar cadastrar um médico no banco de dados.");
        }
    }

    public void adicionar(Paciente paciente, Connection conexao){

    }

    public void adicionar(Endereco endereco, Connection conexao){

    }

    public void adicionar(Telefone telefone, Connection conexao){

    }

    public void adicionar(Atendimento atendimento, Connection conexao){

    }
}
