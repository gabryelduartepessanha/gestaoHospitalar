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

    public Paciente pesquisarPaciente(String cpf, Connection conexao){
        String sql = "select p.id, p.nome, e.rua, e.bairro, e.numero, t.numero as telefone, t.id as codigo from paciente p join endereco e on p.id = e.paciente_id join telefone t on p.id = t.paciente_id where cpf = ?";

        Paciente paciente = new Paciente();

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();

            Endereco endereco = new Endereco();
            Telefone telefone;
            ArrayList<Telefone> telefones = new ArrayList<>();

            while(rs.next()){
                paciente.setId(rs.getInt("id"));
                paciente.setNome(rs.getString("nome"));
                paciente.setCpf(rs.getString("cpf"));

                endereco.setRua(rs.getString("rua"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setNumero(rs.getInt("numero"));

                telefone = new Telefone(rs.getString("telefone"));
                telefone.setId(rs.getInt("codigo"));
                telefones.add(telefone);
            }

            rs.close();
            stmt.close();

            endereco.setId(paciente.getId());
            paciente.setEndereco(endereco);
            paciente.setTelefones(telefones);

        }catch (SQLException e){
            System.out.println("Não foi possível achar o paciente pelo CPF.");
        }
        return paciente;
    }

    public void atualizarPaciente(Paciente paciente_novo, Connection conexao){
        String sqlPaciente = "update paciente set nome = ?, cpf = ? where cpf = ?";
        String sqlEndereco = "upadate endereco set rua = ?, bairro = ?, numero = ? where paciente_id = ?";
        String sqlTelefone = "upadate telefone set numero = ? where paciente_id = ? and id = ?";

        try {
            //atualização dos dados do paciente
            PreparedStatement stmtPaciente = conexao.prepareStatement(sqlPaciente);
            stmtPaciente.setString(1, paciente_novo.getNome());
            stmtPaciente.setString(2, paciente_novo.getCpf());
            stmtPaciente.setString(3, paciente_novo.getCpf());

            stmtPaciente.executeUpdate();

            //atualizacao dos dados do endereco
            PreparedStatement stmtEndereco = conexao.prepareStatement(sqlEndereco);
            stmtEndereco.setString(1, paciente_novo.getEndereco().getRua());
            stmtEndereco.setString(2, paciente_novo.getEndereco().getBairro());
            stmtEndereco.setInt(3, paciente_novo.getEndereco().getNumero());
            stmtEndereco.setInt(4, paciente_novo.getId());

            stmtEndereco.executeUpdate();

            //atualizacao dos dados do telefone

            ArrayList<Telefone> telefones = paciente_novo.getTelefones();

            for(int i = 0; i<telefones.size();i++){
                Telefone t = telefones.get(i);
                PreparedStatement stmtTelefone = conexao.prepareStatement(sqlTelefone);
                stmtTelefone.setString(1, t.getNumero());
                stmtTelefone.setInt(2, paciente_novo.getId());
                stmtTelefone.setInt(3, t.getId());
                stmtTelefone.executeUpdate();
                stmtTelefone.close();
            }

            stmtEndereco.close();
            stmtPaciente.close();

            System.out.println("Os dados do paciente foram atualizados com sucesso!");

        }catch (SQLException e){
            System.out.println("Não foi possível atualizar os dados do paciente!");
        }
    }

    public void deletarPaciente(String cpf, Connection conexao){
        Paciente paciente = pesquisarPaciente(cpf, conexao);
        int chaveEstrangeiraPaciente = paciente.getId();

        String sqlTelefone = "delete from telefone where paciente_id = ?";
        String sqlEndereco = "delete from enderedo where paciente_id = ?";
        String sqlPaciente = "delete from paciente where cpf = ?";

        try{
            //delecao dos telefones
            PreparedStatement stmtTelefone = conexao.prepareStatement(sqlTelefone);
            stmtTelefone.setInt(1, chaveEstrangeiraPaciente);
            stmtTelefone.executeUpdate();
            stmtTelefone.close();
            stmtTelefone.close();
            System.out.println("Todos os contatos do paciente "+ paciente.getNome() + " foram deletados.");

            //delecao dos enderecos
            PreparedStatement stmtEndereco = conexao.prepareStatement(sqlEndereco);
            stmtEndereco.setInt(1, chaveEstrangeiraPaciente);
            stmtEndereco.executeUpdate();
            stmtEndereco.close();
            System.out.println("Tdoos os endereços do paciente "+ paciente.getNome() + " foram deletados.");

            //delecao paciente
            PreparedStatement stmtPaciente = conexao.prepareStatement(sqlPaciente);
            stmtPaciente.setString(1, paciente.getCpf());
            stmtPaciente.executeUpdate();
            stmtPaciente.close();
            System.out.println("O paciente foi deletado do sistema.");

        }catch (SQLException e){
            System.out.println("Não foi possível deletar os telefones do paciente!");
        }
    }

    public void adicionarAtendimento(String crm, String cpf, String dataDeAtendimento, Connection conexao){
        Medico medico = pesquisarMedico(crm, conexao);
        Paciente paciente = pesquisarPaciente(cpf, conexao);

        int idMedico = medico.getId();
        int idPaciente = paciente.getId();

        String sql = "insert into atendimento(paciente_id, medico_id, data_atendimento) values(?, ?, ?)";

        try{
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idMedico);
            stmt.setInt(2, idPaciente);
            stmt.setString(3, dataDeAtendimento);

            stmt.executeUpdate();
            stmt.close();

            System.out.println("O atendimento do médico " + medico.getNome() + ", foi adicionado com sucesso!");

        }catch (SQLException e){
            System.out.println("Erro ao registrar um atendimento médico.");
        }
    }

    public void adicionar(Atendimento atendimento, Connection conexao){

    }

}
