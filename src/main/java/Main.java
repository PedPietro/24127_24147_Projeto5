

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import view.MainFrame;

public class Main {

    public static void main(String[] args) {

        // Captura exceções não tratadas da interface
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("Erro não tratado: " + e.getMessage());
            e.printStackTrace();
        });

        javax.swing.SwingUtilities.invokeLater(() -> {
            configurarLookAndFeel();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setLocationRelativeTo(null); // Centraliza a janela
            mainFrame.setVisible(true);
        });
    }

    private static void configurarLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Não foi possível aplicar o look and feel Nimbus. Usando padrão.");
        }
    }
}
