package pdv;
import java.sql.*;
import javax.swing.JOptionPane;

public class Conexao {
    public static Connection conectar() {
        try {
            // Ajuste o usuário e senha do seu MySQL aqui
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/conveniencia", "root", "Aluno");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar: " + e.getMessage());
            return null;
        }
    }
}