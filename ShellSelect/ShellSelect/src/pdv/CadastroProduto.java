package pdv;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import pdv.Conexao;

public class CadastroProduto extends JFrame {
    private JTextField txtId = new JTextField();
    private JTextField txtNome = new JTextField();
    private JTextField txtPreco = new JTextField(); 
    private JTextField txtCaminhoImagem = new JTextField();
    private JLabel lblPreview = new JLabel("Sem Foto", SwingConstants.CENTER);

    public CadastroProduto() {
    	getContentPane().setBackground(new Color(250, 193, 50));
        setTitle("Cadastro de Produtos - Conveniência");
        setSize(400, 520);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        
        JLabel l1 = new JLabel("Código (ID):"); l1.setBounds(20, 20, 100, 25); getContentPane().add(l1);
        txtId.setBounds(130, 20, 220, 25); getContentPane().add(txtId);

        JLabel l2 = new JLabel("Nome:"); l2.setBounds(20, 60, 100, 25); getContentPane().add(l2);
        txtNome.setBounds(130, 60, 220, 25); getContentPane().add(txtNome);

        JLabel l3 = new JLabel("Preço (0,00):"); l3.setBounds(20, 100, 100, 25); getContentPane().add(l3);
        txtPreco.setBounds(130, 100, 220, 25); 
        txtPreco.setToolTipText("Exemplo: 10,50 ou 10.50");
        getContentPane().add(txtPreco);

        
        JButton btnSelecionar = new JButton("Selecionar Imagem");
        btnSelecionar.setBounds(20, 140, 150, 25); getContentPane().add(btnSelecionar);
        
        lblPreview.setBounds(180, 140, 170, 170);
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        getContentPane().add(lblPreview);

       
        JButton btnSalvar = new JButton("SALVAR PRODUTO");
        btnSalvar.setBounds(20, 380, 340, 45);
        btnSalvar.setBackground(new Color(255, 255, 0));
        btnSalvar.setForeground(new Color(255, 0, 0)); 
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        getContentPane().add(btnSalvar);

     
        btnSelecionar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                File arquivo = chooser.getSelectedFile();
                txtCaminhoImagem.setText(arquivo.getAbsolutePath());
                ImageIcon icon = new ImageIcon(arquivo.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(img));
                lblPreview.setText("");
            }
        });

        btnSalvar.addActionListener(e -> validarESalvar());
    }

    private void validarESalvar() {
        try {
           
            int id = Integer.parseInt(txtId.getText().trim());

            
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) throw new Exception("O nome do produto é obrigatório.");

           
            String precoTexto = txtPreco.getText().trim().replace(",", ".");
            if (!precoTexto.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
                throw new Exception("Preço inválido! Use o formato: 10.50");
            }
            double preco = Double.parseDouble(precoTexto);

            
            String path = txtCaminhoImagem.getText();
            if (path.isEmpty()) throw new Exception("Selecione uma imagem para o produto.");

            salvarNoBanco(id, nome, preco, path);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O Código e o Preço devem ser numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void salvarNoBanco(int id, String nome, double preco, String path) {
        String sql = "INSERT INTO produtos (id, nome, preco, imagem_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.setDouble(3, preco);
            stmt.setString(4, path);
            
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Produto '" + nome + "' cadastrado com sucesso!");
            limparCampos();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Erro: Este código (ID) já existe!");
            } else {
                JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage());
            }
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtCaminhoImagem.setText("");
        lblPreview.setIcon(null);
        lblPreview.setText("Sem Foto");
    }
}