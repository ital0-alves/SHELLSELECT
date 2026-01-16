package pdv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.*;

public class PDV extends JFrame {
    private JTextField txtCod = new JTextField(10);
    private JTable tabela = new JTable(new DefaultTableModel(new Object[]{"Prod", "Preço Original", "Com Desconto"}, 0));
    private JLabel lblTotal = new JLabel("Total: R$ 0.00");
    private JLabel lblFoto = new JLabel();
    private JLabel lblStatusCliente = new JLabel("Cliente: Não identificado");
    
   
    private double total = 0;
    private String cpfIdentificado = null;
    private double percentualDesconto = 0.0;

    public PDV() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(PDV.class.getResource("/Imagens/shell-select-logo-png_seeklogo-268712.png")));
        setTitle("Shell Select");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setLocationRelativeTo(null);

       
        JPanel pLeft = new JPanel(new BorderLayout());
        
       
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(new Color(250, 193, 50));
        
        pTop.add(new JLabel("Código:")); 
        pTop.add(txtCod);
        
        JButton btnAdd = new JButton("Adicionar Item");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(new Color(255, 0, 0));
        pTop.add(btnAdd);

        JButton btnIdentificar = new JButton("Identificar Cliente (CPF)");
        btnIdentificar.setBackground(new Color(52, 152, 219));
        btnIdentificar.setForeground(new Color(255, 0, 0));
        pTop.add(btnIdentificar);
        
        pTop.add(lblStatusCliente);
        
        pLeft.add(pTop, BorderLayout.NORTH);
        pLeft.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
      
        JPanel pRight = new JPanel();
        pRight.setBackground(new Color(250, 193, 50));
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setPreferredSize(new Dimension(300, 600));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblFoto.setBorder(BorderFactory.createTitledBorder("Foto do Produto"));
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFoto.setPreferredSize(new Dimension(250, 250));
        
        
        JButton btnCadProduto = new JButton("Cadastrar Novo Produto");
        JButton btnCadAdmin = new JButton("Cadastrar Novo Admin");
        JButton btnCli = new JButton("Cadastrar Novo Cliente");
        JButton btnPag = new JButton("FINALIZAR PAGAMENTO");
        JButton btnLimpar = new JButton("Limpar Carrinho");

        // Estilização do Botão de Pagar
        btnPag.setBackground(new Color(231, 76, 60));
        btnPag.setForeground(new Color(128, 64, 64));
        btnPag.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Ajuste de tamanho dos botões
        Dimension btnDim = new Dimension(250, 40);
        JButton[] botoes = {btnCadProduto, btnCadAdmin, btnCli, btnPag, btnLimpar};
        for (JButton b : botoes) {
            b.setMaximumSize(btnDim);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        pRight.add(lblFoto);
        pRight.add(Box.createRigidArea(new Dimension(0, 10)));
        pRight.add(btnCadProduto);
        pRight.add(Box.createRigidArea(new Dimension(0, 5)));
        pRight.add(btnCadAdmin);
        pRight.add(Box.createRigidArea(new Dimension(0, 5)));
        pRight.add(btnCli);
        pRight.add(Box.createRigidArea(new Dimension(0, 20)));
        pRight.add(btnLimpar);
        pRight.add(Box.createRigidArea(new Dimension(0, 5)));
        pRight.add(btnPag);
        pRight.add(Box.createRigidArea(new Dimension(0, 20)));
        lblTotal.setVerticalAlignment(SwingConstants.TOP);
        
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotal.setForeground(new Color(192, 57, 43));
        pRight.add(lblTotal);

        getContentPane().add(pLeft, BorderLayout.CENTER);
        getContentPane().add(pRight, BorderLayout.EAST);

        // --- AÇÕES DOS BOTÕES ---
        btnAdd.addActionListener(e -> buscarProduto());
        
        btnIdentificar.addActionListener(e -> identificarCliente());

        btnCadProduto.addActionListener(e -> new CadastroProduto().setVisible(true));
        
        btnCadAdmin.addActionListener(e -> new CadastroAdmin().setVisible(true));
        
        btnCli.addActionListener(e -> new CadastroCliente().setVisible(true));
        
        btnLimpar.addActionListener(e -> limparCarrinho());

        btnPag.addActionListener(e -> {
            if (total > 0) {
                new Pagamento(total, cpfIdentificado).setVisible(true);
                // Opcional: limparCarrinho(); chamar após fechar o pagamento
            } else {
                JOptionPane.showMessageDialog(this, "Adicione itens antes de pagar!");
            }
        });
        
        // Permitir dar ENTER no campo de código
        txtCod.addActionListener(e -> buscarProduto());
    }

    private void identificarCliente() {
        String input = JOptionPane.showInputDialog(this, "Digite o CPF do Cliente:");
        if (input == null || input.trim().isEmpty()) return;
        
        String cpfBusca = input.replaceAll("[^0-9]", "");

        try (Connection conn = Conexao.conectar()) {
            String sql = "SELECT nome, pontos FROM clientes WHERE cpf = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cpfBusca);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.cpfIdentificado = cpfBusca;
                String nome = rs.getString("nome");
                int pontos = rs.getInt("pontos");

                
                if (pontos >= 5000) {
                    this.percentualDesconto = 0.10; 
                    lblStatusCliente.setText("Cliente VIP: " + nome + " (10% Desc.)");
                } else {
                    this.percentualDesconto = 0.05; 
                    lblStatusCliente.setText("Cliente: " + nome + " (5% Desc.)");
                }
                lblStatusCliente.setForeground(new Color(39, 174, 96));
            } else {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado!");
                this.cpfIdentificado = null;
                this.percentualDesconto = 0.0;
                lblStatusCliente.setText("Cliente: Não encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarProduto() {
        String codigo = txtCod.getText().trim();
        if (codigo.isEmpty()) return;

        try (Connection conn = Conexao.conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM produtos WHERE id = ?");
            ps.setInt(1, Integer.parseInt(codigo));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String nome = rs.getString("nome");
                double precoOriginal = rs.getDouble("preco");
                String path = rs.getString("imagem_path");
                
                // Cálculo do Preço com Desconto
                double precoFinal = precoOriginal - (precoOriginal * percentualDesconto);
                
                ((DefaultTableModel)tabela.getModel()).addRow(new Object[]{
                    nome, 
                    String.format("%.2f", precoOriginal), 
                    String.format("%.2f", precoFinal)
                });

                total += precoFinal;
                lblTotal.setText(String.format("Total: R$ %.2f", total));
                
                
                File file = new File(path);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(img));
                } else {
                    lblFoto.setIcon(null);
                    lblFoto.setText("Imagem não encontrada");
                }
                
                txtCod.setText(""); 
                txtCod.requestFocus();
                
            } else {
                JOptionPane.showMessageDialog(this, "Produto não cadastrado!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O código deve ser um número!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void limparCarrinho() {
        total = 0;
        percentualDesconto = 0;
        cpfIdentificado = null;
        ((DefaultTableModel)tabela.getModel()).setRowCount(0);
        lblTotal.setText("Total: R$ 0.00");
        lblFoto.setIcon(null);
        lblStatusCliente.setText("Cliente: Não identificado");
        lblStatusCliente.setForeground(Color.BLACK);
        txtCod.setText("");
    }
}