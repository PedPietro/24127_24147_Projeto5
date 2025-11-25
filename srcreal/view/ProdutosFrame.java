package view;

import model.Produto;
import service.ProdutoService;
import model.Categoria;
import service.CategoriaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProdutosFrame extends JFrame {

    private final ProdutoService produtoService = new ProdutoService();
    private final CategoriaService categoriaService = new CategoriaService(); // Necessário para preencher o ComboBox

    private List<Produto> produtos;
    private int indiceAtual = -1;

    // Campos da tela
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JComboBox<Categoria> cbCategoria; // Caixa de seleção para categorias

    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    public ProdutosFrame() {
        setTitle("Manutenção de Produtos");
        setSize(900, 650); // Um pouco maior pois tem mais colunas
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        initComponents();
        
        carregarComboCategorias(); // Carrega as categorias antes dos produtos
        carregarListaProdutos();
        exibirRegistroAtual();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- Painel de Dados (Formulário) ---
        JPanel panelDados = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // ID
        gbc.gridx = 0; gbc.gridy = 0; panelDados.add(new JLabel("ID:"), gbc);
        txtId = new JTextField(5);
        txtId.setEditable(false); 
        gbc.gridx = 1; gbc.gridy = 0; panelDados.add(txtId, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1; panelDados.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField(30);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panelDados.add(txtNome, gbc);
        gbc.gridwidth = 1; // Reset

        // Preço
        gbc.gridx = 0; gbc.gridy = 2; panelDados.add(new JLabel("Preço (R$):"), gbc);
        txtPreco = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 2; panelDados.add(txtPreco, gbc);

        // Categoria (ComboBox)
        gbc.gridx = 0; gbc.gridy = 3; panelDados.add(new JLabel("Categoria:"), gbc);
        cbCategoria = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; panelDados.add(cbCategoria, gbc);
        gbc.gridwidth = 1;

        add(panelDados, BorderLayout.NORTH);
        
        // --- Painel de Botões ---
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

        // --- Tabela ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "ID Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        add(scrollPane, BorderLayout.SOUTH);
        
        // --- Listeners (Eventos) ---
        btnPrimeiro.addActionListener(e -> navegarPrimeiro());
        btnAnterior.addActionListener(e -> navegarAnterior());
        btnProximo.addActionListener(e -> navegarProximo());
        btnUltimo.addActionListener(e -> navegarUltimo());
        
        btnNovo.addActionListener(e -> limparCampos());
        btnIncluir.addActionListener(this::btnIncluirActionPerformed);
        btnAlterar.addActionListener(this::btnAlterarActionPerformed);
        btnExcluir.addActionListener(this::btnExcluirActionPerformed);
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaProdutos.getSelectedRow() != -1) {
                indiceAtual = tabelaProdutos.getSelectedRow();
                exibirRegistroAtual();
            }
        });
    }
    
    // --- Métodos de Carregamento de Dados ---

    private void carregarComboCategorias() {
        try {
            cbCategoria.removeAllItems();
            // Atenção: Certifique-se que o CategoriaService.listarTodas() retorna List<Categoria>
            // e que a classe Categoria tem o método toString() configurado corretamente.
            List<Categoria> listaCats = categoriaService.listarTodas();
            for (Categoria c : listaCats) {
                cbCategoria.addItem(c);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void carregarListaProdutos() {
        try {
            produtos = produtoService.listarTodas();
            
            if (indiceAtual >= produtos.size()) {
                indiceAtual = produtos.size() > 0 ? produtos.size() - 1 : -1;
            } else if (indiceAtual == -1 && !produtos.isEmpty()) {
                indiceAtual = 0;
            }
            
            carregarProdutosNaTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarProdutosNaTabela() {
        tableModel.setRowCount(0); 
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                p.getIdProduto(), 
                p.getNomeProduto(), 
                p.getPrecoProduto(),
                p.getIdCategoria()
            });
        }
    }

    private void exibirRegistroAtual() {
        if (indiceAtual >= 0 && indiceAtual < produtos.size()) {
            Produto p = produtos.get(indiceAtual);
            txtId.setText(String.valueOf(p.getIdProduto()));
            txtNome.setText(p.getNomeProduto());
            txtPreco.setText(String.valueOf(p.getPrecoProduto()));
            
            // Selecionar a categoria correta no ComboBox
            selecionarCategoriaNoCombo(p.getIdCategoria());
            
        } else {
            limparCampos();
        }
    }
    
    private void selecionarCategoriaNoCombo(int idCat) {
        for (int i = 0; i < cbCategoria.getItemCount(); i++) {
            Categoria c = cbCategoria.getItemAt(i);
            if (c.getIdCategoria() == idCat) {
                cbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        if (cbCategoria.getItemCount() > 0) cbCategoria.setSelectedIndex(0);
        
        txtNome.requestFocus(); 
        indiceAtual = -1; 
        tabelaProdutos.clearSelection();
    }

    // --- Navegação ---

    private void navegarPrimeiro() {
        if (!produtos.isEmpty()) {
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
        if (indiceAtual < produtos.size() - 1 && indiceAtual != -1) {
            indiceAtual++;
            exibirRegistroAtual();
        }
    }

    private void navegarUltimo() {
        if (!produtos.isEmpty()) {
            indiceAtual = produtos.size() - 1;
            exibirRegistroAtual();
        }
    }

    // --- Ações CRUD ---

    private void btnIncluirActionPerformed(ActionEvent evt) {
        if (!validarCampos()) return;
        
        try {
            Produto novoProduto = new Produto();
            novoProduto.setNomeProduto(txtNome.getText());
            novoProduto.setPrecoProduto(Double.parseDouble(txtPreco.getText().replace(",", ".")));
            
            Categoria catSelecionada = (Categoria) cbCategoria.getSelectedItem();
            novoProduto.setIdCategoria(catSelecionada.getIdCategoria());
            
            produtoService.incluir(novoProduto);
            
            carregarListaProdutos();
            
            if (!produtos.isEmpty()) {
                indiceAtual = produtos.size() - 1;
                exibirRegistroAtual();
            }
            
            JOptionPane.showMessageDialog(this, "Produto incluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao incluir: " + e.getMessage());
        }
    }
    
    private void btnAlterarActionPerformed(ActionEvent evt) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para alterar.");
            return;
        }
        if (!validarCampos()) return;
        
        try {
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText();
            Double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
            Categoria catSelecionada = (Categoria) cbCategoria.getSelectedItem();
            
            // Construtor completo do Produto
            Produto produtoAlt = new Produto(id, nome, preco, catSelecionada.getIdCategoria());
            
            if (produtoService.alterar(produtoAlt)) {
                carregarListaProdutos(); 
                // Mantém o índice atual ou busca onde o ID foi parar (caso mude a ordenação)
                exibirRegistroAtual();
                JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma alteração realizada.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage());
        }
    }

    private void btnExcluirActionPerformed(ActionEvent evt) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um registro para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja excluir o produto " + txtNome.getText() + "?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                if (produtoService.excluir(id)) {
                    carregarListaProdutos();
                    if (indiceAtual > 0) indiceAtual--;
                    exibirRegistroAtual();
                    JOptionPane.showMessageDialog(this, "Produto excluído.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }
    }
    
    private void btnBuscarActionPerformed(ActionEvent evt) {
        String inputId = JOptionPane.showInputDialog(this, "Digite o ID do produto:");
        if (inputId == null || inputId.trim().isEmpty()) return;
        
        try {
            int idBusca = Integer.parseInt(inputId.trim());
            Produto encontrado = produtoService.buscarPorId(idBusca);
            
            if (encontrado != null) {
                // Tenta achar na lista atual para selecionar na tabela
                for (int i = 0; i < produtos.size(); i++) {
                    if (produtos.get(i).getIdProduto() == idBusca) {
                        indiceAtual = i;
                        exibirRegistroAtual();
                        tabelaProdutos.setRowSelectionInterval(i, i);
                        return;
                    }
                }
                // Se não achar na lista (paginação ou outro motivo), recarrega
                JOptionPane.showMessageDialog(this, "Produto existe, mas não está na lista visualizada. Recarregando...");
                carregarListaProdutos();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido ou erro na busca.");
        }
    }
    
    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            return false;
        }
        try {
            Double.parseDouble(txtPreco.getText().