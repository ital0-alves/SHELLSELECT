package pdv;
import java.sql.*;
import javax.swing.ImageIcon;

public class VendaController {
    
   
    public ResultSet buscarProduto(int id) throws Exception {
        Connection conn = Conexao.conectar();
        String sql = "SELECT * FROM produtos WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) throw new Exception("Produto não encontrado!");
        return rs;
    }

    
    public double buscarDescontoCliente(String cpf) {
        try (Connection conn = Conexao.conectar()) {
            String sql = "SELECT desconto_especial FROM clientes WHERE cpf = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("desconto_especial");
        } catch (Exception e) { return 0; }
        return 0;
    }
}