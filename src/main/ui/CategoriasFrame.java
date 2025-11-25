package ui;

import model.Produto;
import model.CategoriaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoriasFrame extends JFrame {

    private CategoriaService categoriaService = new CategoriaService();

    private List<Produto> categorias;
    private int indiceAtual = -1;

    private JTextField txtId;
    private JTextField txtNomeCategoria;
    private JTable tabelaCategorias;
    private DefaultTableModel tableModel;

    public CategoriasFrame() {
        setTitle("Manutenção de Categorias");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        carregarListaCategorias();
        exibirRegistroAtual();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelDados = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelDados.add(new JLabel("ID:"), gbc);
       
        txtId = new JTextField(5);
        txtId.setEditable(false); 
        gbc.gridx = 1; gbc.gridy = 0; panelDados.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDados.add(new JLabel("Nome:"), gbc);
        txtNomeCategoria = new JTextField(30);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; panelDados.add(txtNomeCategoria, gbc);
        gbc.gridwidth = 1; 

        add(panelDados, BorderLayout.NORTH);
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnPrimeiro = new JButton("<< Primeiro");
        JButton btnAnterior = new JButton("< Anterior");
        JButton btnProximo = new JButton("Próximo >");
        JButton btnUltimo = new JButton("Último >>");
        
        JButton btnNovo = new JButton("Novo");
        JButton btnIncluir = new JButton("Incluir");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnBuscar = new JButton("Buscar");

        panelBotoes.add(btnPrimeiro);
        panelBotoes.add(btnAnterior);
        panelBotoes.add(btnProximo);
        panelBotoes.add(btnUltimo);
        panelBotoes.add(new JSeparator(SwingConstants.VERTICAL));
        panelBotoes.add(btnNovo);
        panelBotoes.add(btnIncluir);
        panelBotoes.add(btnAlterar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnBuscar);

        add(panelBotoes, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCategorias = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);
        add(scrollPane, BorderLayout.SOUTH);

        btnPrimeiro.addActionListener(e -> navegarPrimeiro());
        btnAnterior.addActionListener(e -> navegarAnterior());
        btnProximo.addActionListener(e -> navegarProximo());
        btnUltimo.addActionListener(e -> navegarUltimo());
        
        btnNovo.addActionListener(e -> limparCampos());
        btnIncluir.addActionListener(this::btnIncluirActionPerformed);
        btnAlterar.addActionListener(this::btnAlterarActionPerformed);
        btnExcluir.addActionListener(this::btnExcluirActionPerformed);
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        tabelaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaCategorias.getSelectedRow() != -1) {
                indiceAtual = tabelaCategorias.getSelectedRow();
                exibirRegistroAtual();
            }
        });
    }
    

    private void carregarListaCategorias() {
        try {
            categorias = categoriaService.listarTodas();
            
            if (indiceAtual >= categorias.size()) {
                indiceAtual = categorias.size() > 0 ? categorias.size() - 1 : -1;
            } else if (indiceAtual == -1 && !categorias.isEmpty()) {
                indiceAtual = 0;
            }
            
            carregarCategoriasNaTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do MongoDB: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            categorias = new java.util.ArrayList<>();
            indiceAtual = -1;
        }
    }
    
    private void carregarCategoriasNaTabela() {
        tableModel.setRowCount(0); 
        for (Produto c : categorias) {
            tableModel.addRow(new Object[]{c.getIdProduto(), c.getNomeProduto()});
        }
    }

    private void exibirRegistroAtual() {
        if (indiceAtual >= 0 && indiceAtual < categorias.size()) {
            Produto categoria = categorias.get(indiceAtual);
            txtId.setText(String.valueOf(categoria.getIdProduto()));
            txtNomeCategoria.setText(categoria.getNomeProduto());
        } else {
            limparCampos();
        }
    }
    

    private void limparCampos() {
        txtId.setText("");
        txtNomeCategoria.setText("");
        txtNomeCategoria.requestFocus(); 
        indiceAtual = -1; 
        tabelaCategorias.clearSelection();
    }

    private void navegarPrimeiro() {
        if (!categorias.isEmpty()) {
            indiceAtual = 0;
            exibirRegistroAtual();
        }
    }

    private void navegarAnterior() {
        if (indiceAtual > 0) {
            indiceAtual--;
            exibirRegistroAtual();
        }
    }

    private void navegarProximo() {
        if (indiceAtual < categorias.size() - 1 && indiceAtual != -1) {
            indiceAtual++;
            exibirRegistroAtual();
        }
    }

    private void navegarUltimo() {
        if (!categorias.isEmpty()) {
            indiceAtual = categorias.size() - 1;
            exibirRegistroAtual();
        }
    }

    private void btnIncluirActionPerformed(ActionEvent evt) {
        String nome = txtNomeCategoria.getText();
        
        if (nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da categoria é obrigatório.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Produto novaCategoria = new Produto();
            novaCategoria.setNomeProduto(nome);
            
            categoriaService.incluir(novaCategoria);
            
            carregarListaCategorias();
            
            if (!categorias.isEmpty()) {
                indiceAtual = categorias.size() - 1;
                exibirRegistroAtual();
                tabelaCategorias.setRowSelectionInterval(indiceAtual, indiceAtual);
            }
            
            JOptionPane.showMessageDialog(this, "Categoria incluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao incluir categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void btnAlterarActionPerformed(ActionEvent evt) {

        if (indiceAtual == -1 || txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um registro para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String novoNome = txtNomeCategoria.getText();
        if (novoNome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da categoria é obrigatório.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(txtId.getText());
            Produto categoriaAlterada = new Produto(id, novoNome);
            
            boolean sucesso = categoriaService.alterar(categoriaAlterada);
            
            if (sucesso) {
                carregarListaCategorias(); 
                exibirRegistroAtual();
                JOptionPane.showMessageDialog(this, "Categoria alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma alteração foi realizada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnExcluirActionPerformed(ActionEvent evt) {
        if (indiceAtual == -1 || txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um registro para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir a categoria " + txtNomeCategoria.getText() + "?", 
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                
                boolean sucesso = categoriaService.excluir(id);

                if (sucesso) {
                    carregarListaCategorias();
                    
                    if (indiceAtual > 0) {
                        indiceAtual--;
                    }
                    exibirRegistroAtual();
                    
                    JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Categoria não encontrada ou não foi excluída.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void btnBuscarActionPerformed(ActionEvent evt) {
        String inputId = JOptionPane.showInputDialog(this, "Digite o ID da categoria a buscar:");
        if (inputId == null || inputId.trim().isEmpty()) {
            return;
        }
        
        try {
            int idBusca = Integer.parseInt(inputId.trim());
            Produto encontrada = categoriaService.buscarPorId(idBusca);
            
            if (encontrada != null) {
                int indiceEncontrado = -1;
                for (int i = 0; i < categorias.size(); i++) {
                    if (categorias.get(i).getIdProduto() == idBusca) {
                        indiceEncontrado = i;
                        break;
                    }
                }
                
                if (indiceEncontrado != -1) {
                    indiceAtual = indiceEncontrado;
                    exibirRegistroAtual();
                    tabelaCategorias.setRowSelectionInterval(indiceAtual, indiceAtual); 
                    JOptionPane.showMessageDialog(this, "Categoria encontrada!", "Busca", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Categoria com ID " + idBusca + " não encontrada.", "Busca", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido. Digite apenas números.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}