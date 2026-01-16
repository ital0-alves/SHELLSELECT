package pdv;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.*;
import pdv.Conexao;
import java.util.Arrays;
import java.util.List;

public class CadastroCliente extends JFrame {
    
    private JTextField txtNome = new JTextField();
    private JFormattedTextField txtCPF;
    private JTextField txtEmail = new JTextField();
    private JFormattedTextField txtTelefone;
    private JFormattedTextField txtCEP;
    private JTextField txtEndereco = new JTextField();
    private JTextField txtBairro = new JTextField();
    private JTextField txtCidade = new JTextField();
    private JTextField txtEstado = new JTextField();

    private final List<String> UFS_VALIDAS = Arrays.asList(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", 
        "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    public CadastroCliente() {
    	getContentPane().setBackground(new Color(250, 193, 50));
        setTitle("Cliente ShellBox");
        setSize(450, 600);
        getContentPane().setLayout(new GridLayout(11, 2, 10, 10));
        setLocationRelativeTo(null);

        try {
          
            MaskFormatter maskCPF = new MaskFormatter("###.###.###-##");
            maskCPF.setPlaceholderCharacter('_');
            
            MaskFormatter maskTel = new MaskFormatter("(##) #####-####");
            maskTel.setPlaceholderCharacter('_');
            
            MaskFormatter maskCEP = new MaskFormatter("#####-###");
            maskCEP.setPlaceholderCharacter('_');

          
            txtCPF = new JFormattedTextField(maskCPF);
            txtTelefone = new JFormattedTextField(maskTel);
            txtCEP = new JFormattedTextField(maskCEP);

        } catch (Exception e) {
            e.printStackTrace();
        }

      
        getContentPane().add(new JLabel(" Nome Completo:")); getContentPane().add(txtNome);
        getContentPane().add(new JLabel(" CPF:")); getContentPane().add(txtCPF);
        getContentPane().add(new JLabel(" E-mail:")); getContentPane().add(txtEmail);
        getContentPane().add(new JLabel(" Telefone:")); getContentPane().add(txtTelefone);
        getContentPane().add(new JLabel(" CEP:")); getContentPane().add(txtCEP);
        getContentPane().add(new JLabel(" Endereço:")); getContentPane().add(txtEndereco);
        getContentPane().add(new JLabel(" Bairro:")); getContentPane().add(txtBairro);
        getContentPane().add(new JLabel(" Cidade:")); getContentPane().add(txtCidade);
        getContentPane().add(new JLabel(" Estado (Sigla):")); getContentPane().add(txtEstado);

        JButton btnSalvar = new JButton("Validar e Cadastrar");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(255, 255, 0));
        btnSalvar.setForeground(new Color(255, 0, 0));
        getContentPane().add(new JLabel("")); 
        getContentPane().add(btnSalvar);

        btnSalvar.addActionListener(e -> validarEntradas());
    }

    private void validarEntradas() {
        try {
           
            String nome = txtNome.getText().trim();
            String cpfRaw = txtCPF.getText().replaceAll("[^0-9]", "");
            String estado = txtEstado.getText().trim().toUpperCase();

            
            if (!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$") || nome.length() < 3) {
                throw new Exception("O nome deve conter apenas letras e ter no mínimo 3 caracteres!");
            }

          
            if (!isCPFValido(cpfRaw)) {
                throw new Exception("CPF Inválido!");
            }

           
            if (!UFS_VALIDAS.contains(estado)) {
                throw new Exception("Sigla de Estado (UF) inexistente!");
            }

            
            if (!txtEmail.getText().contains("@") || !txtEmail.getText().contains(".")) {
                throw new Exception("Formato de e-mail inválido!");
            }

            salvarNoBanco(cpfRaw);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isCPFValido(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        try {
            int soma = 0, peso = 10;
            for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * peso--;
            int r = 11 - (soma % 11);
            char d1 = (r == 10 || r == 11) ? '0' : (char) (r + '0');
            soma = 0; peso = 11;
            for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * peso--;
            r = 11 - (soma % 11);
            char d2 = (r == 10 || r == 11) ? '0' : (char) (r + '0');
            return (d1 == cpf.charAt(9)) && (d2 == cpf.charAt(10));
        } catch (Exception e) { return false; }
    }

    private void salvarNoBanco(String cpfLimpo) {
        String sql = "INSERT INTO clientes (nome, cpf, email, telefone, cep, endereco, bairro, cidade, estado) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Conexao.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtNome.getText().toUpperCase());
            ps.setString(2, cpfLimpo);
            ps.setString(3, txtEmail.getText().toLowerCase());
            ps.setString(4, txtTelefone.getText().replaceAll("[^0-9]", ""));
            ps.setString(5, txtCEP.getText().replaceAll("[^0-9]", ""));
            ps.setString(6, txtEndereco.getText());
            ps.setString(7, txtBairro.getText());
            ps.setString(8, txtCidade.getText());
            ps.setString(9, txtEstado.getText().toUpperCase());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Este CPF já existe ou erro no banco.");
        }
    }
}