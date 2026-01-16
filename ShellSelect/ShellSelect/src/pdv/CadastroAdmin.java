package pdv;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import pdv.Conexao;

public class CadastroAdmin extends JFrame {
    
    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();

    public CadastroAdmin() {
    	getContentPane().setBackground(new Color(250, 193, 50));
        setTitle("Novo Administrador");
        setSize(350, 250);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null); 
        setResizable(false);

        
        JLabel lblTitulo = new JLabel("CADASTRO DE ADMIN", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(255, 0, 0));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(20, 10, 300, 30);
        getContentPane().add(lblTitulo);

        JLabel lblUser = new JLabel("Login / Usuário:");
        lblUser.setBounds(40, 50, 100, 25);
        getContentPane().add(lblUser);

        txtUser.setBounds(40, 75, 260, 25);
        getContentPane().add(txtUser);

        JLabel lblPass = new JLabel("Senha:");
        lblPass.setBounds(40, 105, 100, 25);
        getContentPane().add(lblPass);

        txtPass.setBounds(40, 130, 260, 25);
        getContentPane().add(txtPass);

        JButton btn = new JButton("CADASTRAR");
        btn.setBounds(40, 170, 260, 35);
        btn.setBackground(new Color(255, 255, 0)); 
        btn.setForeground(new Color(255, 0, 0));
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        getContentPane().add(btn);

      
        btn.addActionListener(e -> cadastrar());
    }

    private void cadastrar() {
        String usuario = txtUser.getText().trim();
        String senha = new String(txtPass.getPassword()).trim();

       
        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try (Connection conn = Conexao.conectar()) {
            String sql = "INSERT INTO admins(login, senha) VALUES(?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, usuario);
            st.setString(2, senha);
            
            st.executeUpdate();
            JOptionPane.showMessageDialog(this, "Administrador '" + usuario + "' cadastrado!");
            
          
            txtUser.setText("");
            txtPass.setText("");
            txtUser.requestFocus();
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Erro: Este login já está em uso!");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco: " + ex.getMessage());
            }
        }
    }
}