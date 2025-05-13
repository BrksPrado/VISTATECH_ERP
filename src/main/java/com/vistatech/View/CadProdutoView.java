package com.vistatech.View;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import com.vistatech.Controller.CadProdutoController;
import com.vistatech.GettersSetters.ProdutoEstoque;
import com.vistatech.Model.CadProdutoModel;

public class CadProdutoView extends JFrame {
    private JTextField txtNome, txtPrecoCusto, txtPrecoVenda, txtQuantidade;
    private JComboBox<String> comboBoxTipo;
    private JTable tabela;
    private ModeloTabela modelo;
    private JButton btnAdicionar, btnAtualizar, btnRemover, btnLimpar;
    private JLabel lblLogo;
    private CadProdutoModel model;
    private boolean isUpdating = false;


    public CadProdutoView(CadProdutoModel model) {
        this.model = model;

        setTitle("Módulo de Estoque");
        setSize(800, 500);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        initLookAndFeel();
        initComponents();

    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        // Cabeçalho com logotipo e nome do sistema
        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCabecalho.setBackground(new Color(59, 89, 182));

        lblLogo = new JLabel(redimensionarImagem("src/main/resources/whitelogo.png", 65, 65));
        JLabel lblTitulo = new JLabel("VistaTech ERP - Cadastro de Produtos");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        painelCabecalho.add(lblLogo);
        painelCabecalho.add(lblTitulo);

        // Painel do formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Cadastro de Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;

        // Campos do formulário
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(15);
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelFormulario.add(new JLabel("Preço de Custo:"), gbc);
        gbc.gridx = 1;
        txtPrecoCusto = new JTextField(15);
        painelFormulario.add(txtPrecoCusto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        painelFormulario.add(new JLabel("Preço de Venda:"), gbc);
        gbc.gridx = 1;
        txtPrecoVenda = new JTextField(15);
        painelFormulario.add(txtPrecoVenda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        painelFormulario.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        txtQuantidade = new JTextField(15);
        painelFormulario.add(txtQuantidade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        painelFormulario.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        comboBoxTipo = new JComboBox<>(new String[]{"Armação", "Lente de grau", "Lente de contato", "Óculos de Sol",});
        comboBoxTipo.setEditable(true);
        painelFormulario.add(comboBoxTipo, gbc);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new GridLayout(2, 2, 5, 5));

        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnRemover = new JButton("Remover");
        btnLimpar = new JButton("Limpar");

        styleButton(btnAdicionar);
        styleButton(btnAtualizar);
        styleButton(btnRemover);
        styleButton(btnLimpar);

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);

        // Painel esquerdo
        JPanel painelEsquerdo = new JPanel(new BorderLayout(10, 10));
        painelEsquerdo.add(painelFormulario, BorderLayout.CENTER);
        painelEsquerdo.add(painelBotoes, BorderLayout.SOUTH);

        // Painel direito (tabela)
        modelo = new ModeloTabela();
        tabela = new JTable(modelo);



        TableRowSorter<ModeloTabela> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(20);

        tabela.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Verifica se o evento é uma atualização e se a coluna editada é a de quantidade
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4 && !isUpdating) {
                    isUpdating = true; // Bloqueia novas atualizações enquanto processa a atual

                    int row = e.getFirstRow();

                    // Obtém os valores atualizados da linha
                    int id = (int) modelo.getValueAt(row, 0); // ID do produto
                    String nome = (String) modelo.getValueAt(row, 1); // Nome
                    double precoCusto = (double) modelo.getValueAt(row, 2); // Preço de Custo
                    double precoVenda = (double) modelo.getValueAt(row, 3); // Preço de Venda
                    int quantidade = (int) modelo.getValueAt(row, 4); // Quantidade
                    String tipo = (String) modelo.getValueAt(row, 5); // Tipo

                    // Cria o objeto Produto com os valores atualizados
                    ProdutoEstoque produtoEstoque = new ProdutoEstoque(id, nome, precoCusto, precoVenda, quantidade, tipo);

                    // Atualiza o produto no banco de dados
                    model.atualizarProduto(produtoEstoque);

                    // Remove o foco da tabela para evitar que o evento seja disparado novamente
                    tabela.clearSelection();
                    txtNome.requestFocusInWindow(); // Dá foco a um campo do formulário

                    isUpdating = false; // Libera para novas atualizações
                }
            }
        });


        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    // Converte o índice da visão para o índice do modelo
                    int modelIndex = tabela.convertRowIndexToModel(linhaSelecionada);

                    // Obtém os valores da linha selecionada usando o índice do modelo
                    int id = (int) modelo.getValueAt(modelIndex, 0); // ID
                    String nome = (String) modelo.getValueAt(modelIndex, 1); // Nome
                    double precoCusto = (double) modelo.getValueAt(modelIndex, 2); // Preço de Custo
                    double precoVenda = (double) modelo.getValueAt(modelIndex, 3); // Preço de Venda
                    int quantidade = (int) modelo.getValueAt(modelIndex, 4); // Quantidade
                    String tipo = (String) modelo.getValueAt(modelIndex, 5); // Tipo

                    // Preenche os campos do formulário
                    txtNome.setText(nome);
                    txtPrecoCusto.setText(String.format("%.2f", precoCusto));
                    txtPrecoVenda.setText(String.format("%.2f", precoVenda));
                    txtQuantidade.setText(String.valueOf(quantidade));
                    comboBoxTipo.setSelectedItem(tipo);

                    // Remova esta linha para permitir a edição do campo de quantidade
                    // txtQuantidade.setEditable(false);
                }
            }
        });


        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, scrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(false);

        add(painelCabecalho, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(59, 89, 182));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(59, 89, 182));
                button.setForeground(Color.WHITE);
            }
        });
    }

    private ImageIcon redimensionarImagem(String caminho, int largura, int altura) {
        ImageIcon icon = new ImageIcon(caminho);
        Image imagem = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(imagem);
    }

    // Getters e métodos para interação com o Controller
    public void setAdicionarListener(ActionListener listener) {
        btnAdicionar.addActionListener(listener);
    }

    public void setAtualizarListener(ActionListener listener) {
        btnAtualizar.addActionListener(listener);
    }

    public void setRemoverListener(ActionListener listener) {
        btnRemover.addActionListener(listener);
    }

    public void setLimparListener(ActionListener listener) {
        btnLimpar.addActionListener(listener);
    }

    public ProdutoEstoque getProdutoFormulario() {
        resetarCoresCampos();

        String nome = txtNome.getText().trim();
        String precoCustoText = txtPrecoCusto.getText().trim();
        String precoVendaText = txtPrecoVenda.getText().trim();
        String quantidadeText = txtQuantidade.getText().trim();
        String tipo = comboBoxTipo.getSelectedItem().toString().trim();

        boolean erro = false;

        if (nome.isEmpty()) {
            txtNome.setBackground(new Color(255, 200, 200));
            if (!erro) {
                txtNome.requestFocus();
                erro = true;
            }
        }

        Double precoCusto = validarEConverterPreco(precoCustoText, txtPrecoCusto);
        if (precoCusto == null || precoCusto < 0) {
            if (!erro) {
                erro = true;
            }
        }

        Double precoVenda = validarEConverterPreco(precoVendaText, txtPrecoVenda);
        if (precoVenda == null || precoVenda < 0) {
            if (!erro) {
                erro = true;
            }
        }

        int quantidade = 0;
        try {
            quantidade = Integer.parseInt(quantidadeText);
            if (quantidade < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            txtQuantidade.setBackground(new Color(255, 200, 200));
            if (!erro) {
                txtQuantidade.requestFocus();
                erro = true;
            }
        }

        if (erro) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores válidos:\n" +
                    "- Nome: Não pode estar vazio.\n" +
                    "- Preço de Custo e Venda: Devem ser números válidos (ex: 10,50 ou R$ 10,50).\n" +
                    "- Quantidade: Deve ser um número inteiro positivo.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new ProdutoEstoque(0, nome, precoCusto, precoVenda, quantidade, tipo);
    }

    public JTextField getTxtQuantidade() {
        return txtQuantidade;
    }

    private Double validarEConverterPreco(String texto, JTextField campo) {
        texto = texto.replace("R$", "").trim();
        texto = texto.replace(",", ".");

        try {
            if (temMaisDeDuasCasasDecimais(texto)) {
                campo.setBackground(new Color(255, 200, 200));
                campo.requestFocus();
                return null;
            }
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            campo.setBackground(new Color(255, 200, 200));
            campo.requestFocus();
            return null;
        }
    }

    private boolean temMaisDeDuasCasasDecimais(String valor) {
        int indexPonto = valor.indexOf(".");
        if (indexPonto != -1) {
            int casasDecimais = valor.length() - indexPonto - 1;
            return casasDecimais > 2;
        }
        return false;
    }

    private void resetarCoresCampos() {
        txtNome.setBackground(Color.WHITE);
        txtPrecoCusto.setBackground(Color.WHITE);
        txtPrecoVenda.setBackground(Color.WHITE);
        txtQuantidade.setBackground(Color.WHITE);
    }

    public void limparFormulario() {
        txtNome.setText("");
        txtPrecoCusto.setText("");
        txtPrecoVenda.setText("");
        txtQuantidade.setText("");
        comboBoxTipo.setSelectedIndex(0);
    }

    public void adicionarProdutoTabela(ProdutoEstoque produtoEstoque) {
        modelo.addRow(new Object[]{produtoEstoque.getId(), produtoEstoque.getNome(), produtoEstoque.getPrecoCusto(), produtoEstoque.getPrecoVenda(), produtoEstoque.getQuantidade(), produtoEstoque.getTipo()});
    }

    public void atualizarProdutoTabela(ProdutoEstoque produtoEstoque) {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            // Converte o índice da visão para o índice do modelo
            int modelIndex = tabela.convertRowIndexToModel(linhaSelecionada);
            modelo.setValueAt(produtoEstoque.getNome(), modelIndex, 1);
            modelo.setValueAt(produtoEstoque.getPrecoCusto(), modelIndex, 2);
            modelo.setValueAt(produtoEstoque.getPrecoVenda(), modelIndex, 3);
            modelo.setValueAt(produtoEstoque.getQuantidade(), modelIndex, 4);
            modelo.setValueAt(produtoEstoque.getTipo(), modelIndex, 5);
        }
    }

    public void removerProdutoTabela() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            // Converte o índice da visão para o índice do modelo
            int modelIndex = tabela.convertRowIndexToModel(linhaSelecionada);
            modelo.removeRow(modelIndex);
        }
    }



    public int getProdutoSelecionadoId() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            // Converte o índice da visão para o índice do modelo
            int modelIndex = tabela.convertRowIndexToModel(linhaSelecionada);
            return (int) modelo.getValueAt(modelIndex, 0); // Retorna o ID do modelo
        }
        return -1;
    }

    public void carregarProdutos(List<ProdutoEstoque> produtoEstoques) {
        for (ProdutoEstoque produtoEstoque : produtoEstoques) {
            modelo.addRow(new Object[]{produtoEstoque.getId(), produtoEstoque.getNome(), produtoEstoque.getPrecoCusto(), produtoEstoque.getPrecoVenda(), produtoEstoque.getQuantidade(), produtoEstoque.getTipo()});
        }
    }

    public void carregarProdutoSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            // Converte o índice da visão para o índice do modelo
            int modelIndex = tabela.convertRowIndexToModel(linhaSelecionada);

            // Obtém os valores da linha selecionada usando o índice do modelo
            int id = (int) modelo.getValueAt(modelIndex, 0); // ID
            String nome = (String) modelo.getValueAt(modelIndex, 1); // Nome
            double precoCusto = (double) modelo.getValueAt(modelIndex, 2); // Preço de Custo
            double precoVenda = (double) modelo.getValueAt(modelIndex, 3); // Preço de Venda
            int quantidade = (int) modelo.getValueAt(modelIndex, 4); // Quantidade
            String tipo = (String) modelo.getValueAt(modelIndex, 5); // Tipo

            // Preenche os campos do formulário
            txtNome.setText(nome);
            txtPrecoCusto.setText(String.format("%.2f", precoCusto));
            txtPrecoVenda.setText(String.format("%.2f", precoVenda));
            txtQuantidade.setText(String.valueOf(quantidade));
            comboBoxTipo.setSelectedItem(tipo);
        }
    }

    /*public void carregarDados() {
        List<ProdutoEstoque> produtos = model.carregarProdutos();
        modelo.setRowCount(0); // Limpa a tabela
        for (ProdutoEstoque produto : produtos) {
            modelo.addRow(new Object[]{...});
        }
    }*/

    // Classe interna ModeloTabela
    class ModeloTabela extends DefaultTableModel {
        public ModeloTabela() {
            super(new String[]{"ID", "Nome", "Preço de Custo (R$)", "Preço de Venda (R$)", "Quantidade", "Tipo"}, 0);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0: return Integer.class;  // ID
                case 1: return String.class;   // Nome
                case 2: return Double.class;   // Preço de Custo
                case 3: return Double.class;   // Preço de Venda
                case 4: return Integer.class;  // Quantidade
                case 5: return String.class;   // Tipo
                default: return Object.class;
            }
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            // Permite editar apenas as colunas de Nome, Preço de Custo, Preço de Venda e Tipo
            return column != 0 && column != 4; // A coluna 0 (ID) e 4 (Quantidade) não é editável
        }
    }

    public static class Main {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                CadProdutoModel model = new CadProdutoModel();
                CadProdutoView view = new CadProdutoView(model);
                new CadProdutoController(model, view);
                view.setVisible(true);
            });
        }
    }
}
