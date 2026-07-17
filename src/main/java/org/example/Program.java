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
import java.util.List;

public class Program {
    public static void main(String[] args) throws SQLException {
        Banco b = new Banco();
        Connection conexao = b.conectar();

        b.desconectar(conexao);
    }
}