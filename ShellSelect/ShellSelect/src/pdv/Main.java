package pdv;



import pdv.LoginAdmin;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Define o visual do sistema para o padrão do Windows/Sistema Operacional
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicia o sistema pela tela de Login
        java.awt.EventQueue.invokeLater(() -> {
            new LoginAdmin().setVisible(true);
        });
    }
}
