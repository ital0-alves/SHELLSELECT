package pdv;

import javax.swing.*;
import java.sql.*;
import pdv.Conexao;

public class Pagamento extends JFrame {
    private double valorFinal;
    private String cpfCliente;

    public Pagamento(double valor, String cpf) {
        this.valorFinal = valor;
        this.cpfCliente = cpf;

        setTitle("Finalizar Venda");
        setSize(300, 300);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(new JLabel(" VALOR TOTAL: R$ " + String.format("%.2f", valorFinal)));
        
        JButton btnConfirmar = new JButton("CONFIRMAR PAGAMENTO");
        add(btnConfirmar);

        btnConfirmar.addActionListener(e -> finalizarVenda());
    }

    private void finalizarVenda() {
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false); // Inicia transação

            // 1. Registrar a Venda (Tabela histórico_vendas)
            String sqlVenda = "INSERT INTO vendas (valor, cpf_cliente, data) VALUES (?, ?, NOW())";
            PreparedStatement psVenda = conn.prepareStatement(sqlVenda);
            psVenda.setDouble(1, valorFinal);
            psVenda.setString(2, cpfCliente);
            psVenda.executeUpdate();

            // 2. Atualizar Pontos do Cliente (se houver cliente)
            if (cpfCliente != null && !cpfCliente.isEmpty()) {
                // Regra: 10 pontos para cada 1 real
                int pontosGanhos = (int) (valorFinal * 10);
                String sqlPontos = "UPDATE clientes SET pontos = pontos + ? WHERE cpf = ?";
                PreparedStatement psPontos = conn.prepareStatement(sqlPontos);
                psPontos.setInt(1, pontosGanhos);
                psPontos.setString(2, cpfCliente);
                psPontos.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Venda finalizada! O cliente ganhou " + pontosGanhos + " pontos.");
            }

            conn.commit(); // Salva tudo no banco
            JOptionPane.showMessageDialog(this, "Pagamento aprovado!");
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao processar: " + ex.getMessage());
        }
    }
}