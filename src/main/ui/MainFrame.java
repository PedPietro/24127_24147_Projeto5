package ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MainFrame extends JFrame {

    private CategoriasFrame categoriasFrame;
    private ProdutosFrame produtosFrame;

    public MainFrame() {
        setTitle("Sistema Da Roça - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        setLayout(new BorderLayout());
        criarMenu();
        criarTelaInicial();
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();


        JMenu menuCadastros = new JMenu("Cadastros");

        JMenuItem itemCategorias = new JMenuItem("Manutenção de Categorias");
        itemCategorias.addActionListener(e -> abrirCategorias());

        JMenuItem itemProdutos = new JMenuItem("Manutenção de Produtos");
        itemProdutos.addActionListener(e -> abrirProdutos());

        menuCadastros.add(itemCategorias);
        menuCadastros.add(itemProdutos);

        // Menu "Sistema" com opção "Sair"
        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> encerrarAplicacao());
        menuSistema.add(itemSair);

        menuBar.add(menuSistema);
        menuBar.add(menuCadastros);

        setJMenuBar(menuBar);
    }


    private void criarTelaInicial() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("Sistema Da Roça", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 26f));

        JLabel subtitulo = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "Bem-vindo ao sistema de manutenção de Categorias e Produtos.<br>"
                        + "Use o menu acima para acessar as telas de cadastro."
                        + "</div></html>",
                SwingConstants.CENTER
        );
        subtitulo.setFont(subtitulo.getFont().deriveFont(15f));

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(subtitulo, BorderLayout.CENTER);

        add(painel, BorderLayout.CENTER);
    }

    private void abrirCategorias() {
        // Se a janela ainda não existe ou foi fechada, cria uma nova
        if (categoriasFrame == null || !categoriasFrame.isDisplayable()) {
            categoriasFrame = new CategoriasFrame();
            categoriasFrame.setLocationRelativeTo(this);
        }
        categoriasFrame.setVisible(true);
        categoriasFrame.toFront();
    }

    private void abrirProdutos() {
        if (produtosFrame == null || !produtosFrame.isDisplayable()) {
            produtosFrame = new ProdutosFrame();
            produtosFrame.setLocationRelativeTo(this);
        }
        produtosFrame.setVisible(true);
        produtosFrame.toFront();
    }


    private void encerrarAplicacao() {
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do Sistema Da Roça?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcao == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}