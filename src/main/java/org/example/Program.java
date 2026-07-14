package org.example;

import org.example.controller.Banco;
import org.example.model.Atendimento;
import org.example.model.Medico;
import org.example.model.Paciente;
import org.example.model.Telefone;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Program {
    public static void main(String[] args) throws SQLException {
        Banco b = new Banco();
        Connection conexao = b.conectar();

        Medico medico = new Medico("Gabryel Duarte", "1014");
        medico.setId(5);

        b.atualizar(medico, conexao);
//        b.adicionar(medico, conexao);
        b.desconectar(conexao);
    }
}