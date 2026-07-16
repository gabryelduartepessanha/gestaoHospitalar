package org.example.controller;

import org.example.model.*;

import java.net.ConnectException;
import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public void adicionarMedico(Medico medico, Connection conexao){
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

    public void atualizarMedico(Medico medico, Connection conexao){
        String sql = "update medico set nome = ?, crm = ? where id = ?";

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getCrm());
            stmt.setInt(3, medico.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if(linhasAfetadas > 0){
                System.out.println("O médico foi atualizado com sucesso!");
            }else{
                System.out.println("O médico não foi encontrado com o ID informado.");
            }
        }catch(SQLException e){
            System.out.println("Aconteceu um erro ao tentar atualizar o médico no banco de dados.");
            e.printStackTrace();
        }
    }

    public List<Medico> pesquisarTodosOsMedicos(Connection conexao){
        List<Medico> medicos = new ArrayList<>();
        String sql = "select id, nome, crm from medico";

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Medico medico = new Medico();

                medico.setId(rs.getInt("id"));
                medico.setNome(rs.getString("nome"));
                medico.setCrm(rs.getString("crm"));

                medicos.add(medico);
            }

            rs.close();
            stmt.close();
        }catch (SQLException e){
            System.out.println("Erro ao buscar os cadastros dos médicos.");
            e.printStackTrace();
        }
        return medicos;
    }

    public void deletarMedico(int id, Connection conexao){
        String sql = "delete from medico where id = ?";

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            if(linhasAfetadas > 0){
                System.out.println("O médico foi deletado com sucesso!");
            }else{
                System.out.println("Nenhum médico foi encontrado com o ID informado.");
            }

            stmt.close();
        }catch (SQLException e){
            System.out.println("Não foi possível deletar médico pelo ID.");
            e.printStackTrace();
        }
    }

    public Medico pesquisarMedico(String crm, Connection conexao){
        String sql = "select id, nome, crm from medico where crm = ?";
        Medico medico = new Medico();

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, crm);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String crm2 = rs.getString("crm");
                medico.setId(id);
                medico.setNome(nome);
                medico.setCrm(crm2);
            }

            rs.close();
            stmt.close();
        }catch (SQLException e){
            System.out.println("O médico não foi identificado pelo CRM.");
        }
        return medico;
    }

    public void adicionarPaciente(Paciente paciente, Endereco endereco, ArrayList<Telefone> telefones, Connection conexao){
        String sql = "insert into paciente(nome, cpf) values(?, ?)";

        try{
            //cadastrando paciente
            PreparedStatement stmtPaciente = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtPaciente.setString(1, paciente.getNome());
            stmtPaciente.setString(2, paciente.getCpf());

            stmtPaciente.executeUpdate();

            ResultSet rsPaciente = stmtPaciente.getGeneratedKeys();
            int pacienteId = 0;

            if(rsPaciente.next()){
                pacienteId = rsPaciente.getInt(1);
            }
            rsPaciente.close();
            stmtPaciente.close();

            //cadastrando o endereço usando PK do paciente
            String sqlEndereco = "insert into endereco(paciente_id, numero, bairro, rua) values(?, ?, ?, ?)";

            PreparedStatement stmtEndereco = conexao.prepareStatement(sqlEndereco);
            stmtEndereco.setInt(1, pacienteId);
            stmtEndereco.setInt(2, endereco.getNumero());
            stmtEndereco.setString(3, endereco.getBairro());
            stmtEndereco.setString(4, endereco.getRua());

            stmtEndereco.executeUpdate();
            stmtEndereco.close();

            //cadastando os telefones de contato usando FK do paciente
            String sqlTelefone = "insert into telefone(paciente_id, numero) values(?, ?)";

            for(int i = 0; i < telefones.size();i++){
                Telefone telefone = telefones.get(i);

                PreparedStatement stmtTelefone = conexao.prepareStatement(sqlTelefone);
                stmtTelefone.setInt(1, pacienteId);
                stmtTelefone.setString(2, telefone.getNumero());

                stmtTelefone.executeUpdate();
                stmtTelefone.close();
            }

            System.out.println("Paciente registrado com sucesso!");
        }catch (SQLException e){
            System.out.println("Não foi possível inserir o paciente no Banco de dados.");
        }
    }

    public void adicionar(Endereco endereco, Connection conexao){

    }

    public void adicionar(Telefone telefone, Connection conexao){

    }

    public void adicionar(Atendimento atendimento, Connection conexao){

    }

    @Override
    public String toString() {
        return "Banco{" +
                "usuario='" + usuario + '\'' +
                ", senha='" + senha + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
