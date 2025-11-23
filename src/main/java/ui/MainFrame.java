package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema Da Roça - Menu Principal");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Manutenção");

        // Item: Categorias
        JMenuItem categoriasItem = new JMenuItem(new AbstractAction("Manutenção de Categorias") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Henry apenas ABRE o frame da Beth
                JFrame categoriasFrame = new CategoriesFrame(); 
                categoriasFrame.setVisible(true);
            }
        });

        // Item: Produtos
        JMenuItem produtosItem = new JMenuItem(new AbstractAction("Manutenção de Produtos") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Henry apenas ABRE o frame do Pietro
                JFrame produtosFrame = new ProductsFrame(); 
                produtosFrame.setVisible(true);
            }
        });

        menu.add(categoriasItem);
        menu.add(produtosItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        // Tela inicial simples
        JLabel label = new JLabel(
                "<html><h2>Bem-vindo ao Sistema Da Roça</h2>" +
                "<p>Use o menu acima para acessar os cadastros.</p></html>",
                SwingConstants.CENTER
        );

        add(label, BorderLayout.CENTER);
    }
}
