package com.vistatech.View;

import com.vistatech.Controller.EstoqueController;
import com.vistatech.Model.EstoqueModel;
import com.vistatech.GettersSetters.MovimentacaoEstoque;
import com.vistatech.GettersSetters.ProductEstoque;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class EstoqueView extends JFrame {
    private JTextField txtPesquisa;
    private JTable tabela;
    private ModeloTabela modelo;
    private JButton btnOcultar;
    private JButton btnTornarVisivel;
    private JButton btnReporEstoque;
    private JComboBox<String> comboBoxTipoPesquisa;
    private JComboBox<String> comboBoxExibicao;
    private JLabel lblLogo;
    private Timer timerPesquisa; // Timer para adicionar um delay na pesquisa
    private EstoqueModel model;

    private ImageIcon iconeCheckmark;
    private ImageIcon iconePencil;

    // Construtor recebe EstoqueModel como parâmetro
    public EstoqueView(EstoqueModel model) {
        this.model = model;
        setTitle("Consulta de Estoque");
        setSize(800, 500);
        setLocationRelativeTo(null);

        iconeCheckmark = carregarIcone("/icons/check.png");
        iconePencil = carregarIcone("/icons/edited.png");

        // Verifique se os ícones foram carregados corretamente
        if (iconeCheckmark == null) {
            System.err.println("Erro ao carregar ícone checkmark.png");
        }
        if (iconePencil == null) {
            System.err.println("Erro ao carregar ícone pencil.png");
        }



        setLayout(new BorderLayout(10, 10));
        initLookAndFeel();
        initComponents();
    }
    private ImageIcon carregarIcone(String caminho) {
        URL url = getClass().getResource(caminho);
        if (url == null) {
            System.err.println("Recurso não encontrado: " + caminho);
            return null;
        }
        // Carrega o ícone original
        ImageIcon icon = new ImageIcon(url);

        Image imagemRedimensionada = icon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        return new ImageIcon(imagemRedimensionada);
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
        JLabel lblTitulo = new JLabel("VistaTech ERP - Consulta de Estoque");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        painelCabecalho.add(lblLogo);
        painelCabecalho.add(lblTitulo);

        // Painel de pesquisa (esquerda)
        JPanel painelPesquisa = new JPanel(new GridBagLayout());
        painelPesquisa.setBorder(BorderFactory.createTitledBorder("Pesquisar"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;

        // ComboBox para selecionar o tipo de pesquisa
        JLabel lblPesquisarPor = new JLabel("Pesquisar Por:");
        lblPesquisarPor.setForeground(Color.BLACK); // Cor do texto
        lblPesquisarPor.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte

        // Adicionar a legenda ao painel de pesquisa
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        painelPesquisa.add(lblPesquisarPor, gbc);

        gbc.gridx = 1;
        // ComboBox para selecionar o tipo de pesquisa
        comboBoxTipoPesquisa = new JComboBox<>(new String[]{
                "Nome do Produto",
                "Quantidade (menos que)",
                "Quantidade (mais que)"
        });
        comboBoxTipoPesquisa.addActionListener(e -> {
            txtPesquisa.setText(""); // Limpa o campo de pesquisa
        });
        painelPesquisa.add(comboBoxTipoPesquisa, gbc);

        // Campo de pesquisa
        JLabel lblTermo = new JLabel("Termo de pesquisa:");
        lblTermo.setForeground(Color.BLACK); // Cor do texto
        lblTermo.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte

        // Adicionar a legenda ao painel de pesquisa
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        painelPesquisa.add(lblTermo, gbc);

        gbc.gridx = 1;
        txtPesquisa = new JTextField(20);
        painelPesquisa.add(txtPesquisa, gbc);


        // Adicionar uma legenda para o JComboBox
        JLabel lblExibicao = new JLabel("Exibir:");
        lblExibicao.setForeground(Color.BLACK); // Cor do texto
        lblExibicao.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte

        // Adicionar a legenda ao painel de pesquisa
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        painelPesquisa.add(lblExibicao, gbc);

        // Adicionar um JComboBox para alternar entre produtos visíveis, ocultos e movimentações
        comboBoxExibicao = new JComboBox<>(new String[]{"Produtos Visíveis", "Produtos Ocultos", "Movimentações de Estoque"});
        styleComboBox(comboBoxExibicao); // Aplicar o estilo personalizado
        comboBoxExibicao.addActionListener(e -> {
            txtPesquisa.setText("");
            int indice = comboBoxExibicao.getSelectedIndex();
            alternarTabela(indice);

        });

        // Adicionar o JComboBox ao painel de pesquisa
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        painelPesquisa.add(comboBoxExibicao, gbc);

        btnOcultar = new JButton("Ocultar Produto");
        styleButton(btnOcultar);
        btnOcultar.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tabela.getValueAt(selectedRow, 0);
                ocultarProduto(id);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para ocultar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Adicionar o botão ao painel de pesquisa
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        painelPesquisa.add(btnOcultar, gbc);

        // Adicionar um botão para tornar o produto visível
        btnTornarVisivel = new JButton("Tornar Visível");
        styleButton(btnTornarVisivel);
        btnTornarVisivel.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tabela.getValueAt(selectedRow, 0);
                tornarProdutoVisivel(id);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para tornar visível.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Adicionar o botão ao painel de pesquisa
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        painelPesquisa.add(btnTornarVisivel, gbc);
        // Inicialmente, ocultar o botão "Tornar Visível"
        btnTornarVisivel.setVisible(false);

        btnReporEstoque = new JButton("Repor Estoque");
        styleButton(btnReporEstoque);
        btnReporEstoque.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0) {
                int produtoId = (int) tabela.getValueAt(selectedRow, 0);
                reporEstoque(produtoId);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para repor o estoque.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

// Adicionar o botão ao painel de pesquisa
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        painelPesquisa.add(btnReporEstoque, gbc);


        // Tabela de produtos (direita)
        modelo = new ModeloTabela();
        tabela = new JTable(modelo);

        TableRowSorter<ModeloTabela> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));

        // Usando JSplitPane para dividir a tela em duas partes
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelPesquisa, scrollPane);
        splitPane.setDividerLocation(300); // Define a posição inicial do divisor
        splitPane.setResizeWeight(0.3); // Define a proporção de redimensionamento
        splitPane.setEnabled(false);

        // Layout principal
        add(painelCabecalho, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Aplica o renderizador personalizado à coluna "Editada"
        tabela.getColumnModel().getColumn(4).setCellRenderer(new IconCellRenderer(iconeCheckmark, iconePencil));

        // Configura a pesquisa síncrona
        configurarPesquisaSincrona();
    }

    public class IconCellRenderer extends DefaultTableCellRenderer {
        private ImageIcon iconeCheckmark;
        private ImageIcon iconePencil;

        public IconCellRenderer(ImageIcon iconeCheckmark, ImageIcon iconePencil) {
            this.iconeCheckmark = iconeCheckmark;
            this.iconePencil = iconePencil;
            setHorizontalAlignment(JLabel.CENTER); // Centraliza o ícone
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Verifica se o modelo da tabela é ModeloTabelaMovimentacoes
            if (table.getModel() instanceof ModeloTabelaMovimentacoes) {
                // Verifica se a coluna é a coluna "Status"
                if (column == 4) {
                    int editada = (int) value; // O valor da célula é 0 ou 1
                    if (editada == 0) {
                        setIcon(iconeCheckmark); // Ícone de checkmark para normal
                        setToolTipText("Entrada/Saída Padrão"); // Legenda para o ícone de checkmark
                    } else {
                        setIcon(iconePencil); // Ícone de lápis para editado
                        setToolTipText("Saída Editada no Cadastro"); // Legenda para o ícone de lápis
                    }
                    setText(""); // Remove o texto
                }
            } else {
                // Para outras tabelas, não renderiza ícones
                setIcon(null);
                setText(value != null ? value.toString() : ""); // Restaura o texto original
            }

            return this;
        }
    }



    public String getTipoPesquisa() {
        return comboBoxTipoPesquisa.getSelectedItem().toString();
    }

    private void ocultarProduto(int id) {
        // Verificar se o produto tem estoque maior que 0
        ProductEstoque productEstoque = model.obterProdutoPorId(id); // Método para obter o produto pelo ID
        if (productEstoque != null && productEstoque.getQuantidade() > 0) {
            // Exibir aviso informativo se o estoque for maior que 0
            JOptionPane.showMessageDialog(this,
                    "Atenção: Este produto ainda tem estoque disponível!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }

        // Confirmar a ocultação do produto
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja ocultar este produto?",
                "Confirmar Ocultação",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = model.ocultarProduto(id);
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                        "Produto ocultado com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                carregarProdutos(model.carregarProdutos()); // Recarrega a lista de produtos
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao ocultar o produto.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void tornarProdutoVisivel(int id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja tornar este produto visível?", "Confirmar Visibilidade", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = model.tornarProdutoVisivel(id);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Produto tornado visível com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                alternarTabela(comboBoxExibicao.getSelectedIndex()); // Recarrega a tabela atual
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao tornar o produto visível.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alternarTabela(int indice) {
        if (indice == 0) {
            // Exibir produtos visíveis
            tabela.setModel(modelo); // Restaura o modelo original da tabela
            carregarProdutos(model.carregarProdutosVisiveis());
            btnOcultar.setVisible(true);
            btnTornarVisivel.setVisible(false);
            btnReporEstoque.setVisible(true);

            // Remove o renderizador de ícones (não é necessário para produtos)
            tabela.getColumnModel().getColumn(4).setCellRenderer(null);
        } else if (indice == 1) {
            // Exibir produtos ocultos
            tabela.setModel(modelo); // Restaura o modelo original da tabela
            carregarProdutos(model.carregarProdutosOcultos());
            btnOcultar.setVisible(false);
            btnTornarVisivel.setVisible(true);
            btnReporEstoque.setVisible(false);

            // Remove o renderizador de ícones (não é necessário para produtos)
            tabela.getColumnModel().getColumn(4).setCellRenderer(null);
        } else if (indice == 2) {
            // Exibir movimentações de estoque
            ModeloTabelaMovimentacoes modeloTabelaMovimentacoes = new ModeloTabelaMovimentacoes();
            tabela.setModel(modeloTabelaMovimentacoes); // Define o novo modelo de tabela
            carregarMovimentacoesEstoque(model.carregarMovimentacoesEstoque());
            btnOcultar.setVisible(false);
            btnTornarVisivel.setVisible(false);
            btnReporEstoque.setVisible(false);

            // Aplica o renderizador de ícones à coluna "Editada"
            tabela.getColumnModel().getColumn(4).setCellRenderer(new IconCellRenderer(iconeCheckmark, iconePencil));
        }

        // Redefine o TableRowSorter após a troca de modelos
        TableRowSorter<?> sorter = new TableRowSorter<>(tabela.getModel());
        tabela.setRowSorter(sorter);
    }
    private void carregarMovimentacoesEstoque(List<MovimentacaoEstoque> movimentacoes) {
        SwingUtilities.invokeLater(() -> {
            if (tabela.getModel() instanceof ModeloTabelaMovimentacoes) {
                ModeloTabelaMovimentacoes modeloTabelaMovimentacoes = (ModeloTabelaMovimentacoes) tabela.getModel();
                modeloTabelaMovimentacoes.setRowCount(0); // Limpa o modelo antes de adicionar novas linhas

                for (MovimentacaoEstoque movimentacao : movimentacoes) {
                    modeloTabelaMovimentacoes.addRow(new Object[]{
                            movimentacao.getId(),
                            movimentacao.getNomeProduto(),
                            movimentacao.getTipo(),
                            movimentacao.getQuantidade(),
                            movimentacao.isEditado() ? 1 : 0, // Exibe 1 para editado, 0 para não editado
                            movimentacao.getData()
                    });
                }

                // Redefine o TableRowSorter após carregar os dados
                TableRowSorter<ModeloTabelaMovimentacoes> sorter = new TableRowSorter<>(modeloTabelaMovimentacoes);
                tabela.setRowSorter(sorter);

               //sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
            }
        });
    }

    private void configurarPesquisaSincrona() {
        timerPesquisa = new Timer(300, e -> {
            String termo = txtPesquisa.getText().trim();
            String tipoPesquisa = comboBoxTipoPesquisa.getSelectedItem().toString();
            int tabelaAtiva = comboBoxExibicao.getSelectedIndex();

            if (termo.isEmpty()) {
                // Se o campo estiver vazio, carrega todos os produtos ou movimentações, conforme a seleção
                if (tabelaAtiva == 0) {
                    carregarProdutos(model.carregarProdutosVisiveis());
                } else if (tabelaAtiva == 1) {
                    carregarProdutos(model.carregarProdutosOcultos());
                } else if (tabelaAtiva == 2) {
                    carregarMovimentacoesEstoque(model.carregarMovimentacoesEstoque());
                }
            } else {
                // Realiza a pesquisa com o termo digitado e o tipo de pesquisa
                if (tabelaAtiva == 2) {
                    List<MovimentacaoEstoque> movimentacoes = model.pesquisarMovimentacoes(termo, tipoPesquisa);
                    carregarMovimentacoesEstoque(movimentacoes);
                } else {
                    boolean apenasVisiveis = tabelaAtiva == 0;
                    List<ProductEstoque> productEstoques = model.pesquisarProdutos(termo, tipoPesquisa, apenasVisiveis, tabelaAtiva);
                    carregarProdutos(productEstoques);
                }
            }
        });
        timerPesquisa.setRepeats(false); // Garante que o timer só execute uma vez após o delay

        // Adiciona um DocumentListener ao campo de pesquisa
        txtPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timerPesquisa.restart(); // Reinicia o timer ao digitar
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timerPesquisa.restart(); // Reinicia o timer ao apagar
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Não é necessário para JTextField
            }
        });
    }
    public boolean isApenasVisiveis() {
        return comboBoxExibicao.getSelectedIndex() == 0; // 0 = Produtos Visíveis, 1 = Produtos Ocultos
    }

    public int getTabelaAtiva() {
        return comboBoxExibicao.getSelectedIndex(); // Retorna o índice da tabela ativa
    }

    private void reporEstoque(int produtoId) {
        String quantidadeStr = JOptionPane.showInputDialog(this, "Informe a quantidade a ser adicionada:", "Repor Estoque", JOptionPane.QUESTION_MESSAGE);
        if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
            try {
                int quantidade = Integer.parseInt(quantidadeStr);
                if (quantidade > 0) {
                    int estoqueId = model.obterIdEstoquePorProduto(produtoId);
                    if (estoqueId != -1) {
                        boolean sucesso = model.registrarMovimentacao(estoqueId, "ENTRADA", quantidade);
                        if (sucesso) {
                            JOptionPane.showMessageDialog(this, "Estoque reposto com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            carregarProdutos(model.carregarProdutos()); // Recarrega a lista de produtos
                        } else {
                            JOptionPane.showMessageDialog(this, "Erro ao repor o estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Estoque não encontrado para o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que 0.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(59, 89, 182));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(59, 89, 182));
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(59, 89, 182));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Arial", Font.BOLD, 14));
        comboBox.setFocusable(false);
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

    }

    private ImageIcon redimensionarImagem(String caminho, int largura, int altura) {
        ImageIcon icon = new ImageIcon(caminho);
        Image imagem = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(imagem);
    }

    public String getTermoPesquisa() {
        return txtPesquisa.getText().trim();
    }

    public void carregarProdutos(List<ProductEstoque> productEstoques) {
        SwingUtilities.invokeLater(() -> {
            modelo.setRowCount(0); // Limpa a tabela antes de carregar novos dados
            for (ProductEstoque productEstoque : productEstoques) {
                modelo.addRow(new Object[]{
                        productEstoque.getId(),
                        productEstoque.getNome(),
                        productEstoque.getPrecoCusto(),
                        productEstoque.getPrecoVenda(),
                        productEstoque.getQuantidade(),
                        productEstoque.getTipo()
                });
            }

            // Redefine o TableRowSorter após carregar os dados
            TableRowSorter<ModeloTabela> sorter = new TableRowSorter<>(modelo);
            tabela.setRowSorter(sorter);
        });
    }

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
            // Colunas não editaveis
            return false;
        }
    }



    // Classe interna ModeloTabelaMovimentacoes
    class ModeloTabelaMovimentacoes extends DefaultTableModel {
        public ModeloTabelaMovimentacoes() {
            super(new String[]{"ID", "Produto", "Tipo", "Quantidade", "Status", "Data"}, 0);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0: return Integer.class;  // ID
                case 1: return String.class;   // Produto
                case 2: return String.class;   // Tipo
                case 3: return Integer.class;  // Quantidade
                case 4: return Integer.class;  // Editado (1 ou 0)
                case 5: return String.class;  // Data
                default: return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            // Colunas não editáveis
            return false;
        }
    }
    public static class Main {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                EstoqueModel model = new EstoqueModel();
                EstoqueView view = new EstoqueView(model);
                new EstoqueController(model, view);
                view.setVisible(true);
            });
        }
    }
}
