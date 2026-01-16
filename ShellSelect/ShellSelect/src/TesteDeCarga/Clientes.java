package TesteDeCarga;
import java.sql.*;
import pdv.Conexao;
public class Clientes {
public void salvarCompleto(String nome,String cpf,String email,
		String telefone,String cep,String endereco,String bairro,String cidade,
		String estado, int pontos)throws SQLException {
	String sql = "insert into clientes(nome,cpf,email,telefone,cep,endereco,bairro,cidade,estado,pontos) values (?,?,?,?,?,?,?,?,?,?)";
	

    try (Connection conn = Conexao.conectar(); 
    	PreparedStatement ps = conn.prepareStatement(sql)){
    		ps.setString(1, nome);
    		ps.setString(2, cpf);
    		ps.setString(3, email);
    		ps.setString(4, telefone);
    		ps.setString(5, cep);
    		ps.setString(6, endereco);
    		ps.setString(7, bairro);
    		ps.setString(8, cidade);
    		ps.setString(9, estado);
    		ps.setInt(10, pontos);
    		ps.executeUpdate();
    	}
    }
}