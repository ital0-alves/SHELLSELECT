package pdv;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import pdv.Conexao;
import java.awt.Color;

public class LoginAdmin extends JFrame {
    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private JButton btnEntrar = new JButton("Entrar");

    public LoginAdmin() {
    	getContentPane().setBackground(new Color(250, 193, 50));
        setTitle("Painel Admin - Select Shell");
        setSize(300, 200);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblU = new JLabel("Usuário:"); lblU.setBounds(31, 40, 80, 25); getContentPane().add(lblU);
        txtUser.setBounds(111, 40, 140, 25); getContentPane().add(txtUser);
        
        JLabel lblS = new JLabel("Senha:"); lblS.setBounds(31, 80, 80, 25); getContentPane().add(lblS);
        txtPass.setBounds(111, 80, 140, 25); getContentPane().add(txtPass);

        btnEntrar.setBounds(111, 120, 100, 30); getContentPane().add(btnEntrar);
 
 JLabel lblNewLabel = new JLabel("");
 lblNewLabel.setIcon(new ImageIcon(LoginAdmin.class.getResource("/Imagens/shell-select-logo-png_seeklogo-268712 (3).png")));
 lblNewLabel.setBounds(121, 0, 90, 36);
 getContentPane().add(lblNewLabel);

        btnEntrar.addActionListener(e -> logar());
    }

    private void logar() {
        try (Connection conn = Conexao.conectar()) {
            String sql = "SELECT * FROM admins WHERE login = ? AND senha = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, txtUser.getText());
            st.setString(2, new String(txtPass.getPassword()));
            if (st.executeQuery().next()) {
                new PDV().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Dados inválidos!");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}