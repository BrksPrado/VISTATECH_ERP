import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EstoqueView extends JFrame {
    private JTextField txtNome, txtPrecoCusto, txtPrecoVenda, txtQuantidade;
    private JTable tabela;
    private ModeloTabela modelo; // Usando a classe interna
    private JButton btnAdicionar, btnAtualizar, btnRemover, btnLimpar;
    private JLabel lblLogo;

    public EstoqueView() {
        setTitle("Módulo de Estoque");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        lblLogo = new JLabel(redimensionarImagem("src/main/resources/logotipo.png", 65, 65));
        JLabel lblTitulo = new JLabel("VistaTech ERP");
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

        // Aumentar a largura do grid atribuindo peso horizontal
        gbc.weightx = 1.0;
        gbc.weighty = 0;

        // Ajusta as posições do formulário
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
        modelo = new ModeloTabela(); // Usando a classe interna
        tabela = new JTable(modelo);

        // Configura a ordenação ao clicar no cabeçalho
        TableRowSorter<ModeloTabela> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        // Ajusta tamanho de exibição das informações da tabela
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(20);

        // Adiciona um MouseListener à tabela para preencher os campos ao clicar em uma linha
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtém a linha selecionada
                int linhaSelecionada = tabela.getSelectedRow();

                // Verifica se uma linha foi selecionada
                if (linhaSelecionada != -1) {
                    // Obtém os valores da linha selecionada
                    int id = (int) tabela.getValueAt(linhaSelecionada, 0);
                    String nome = (String) tabela.getValueAt(linhaSelecionada, 1);
                    double precoCusto = (double) tabela.getValueAt(linhaSelecionada, 2);
                    double precoVenda = (double) tabela.getValueAt(linhaSelecionada, 3);
                    int quantidade = (int) tabela.getValueAt(linhaSelecionada, 4);

                    // Preenche os campos do formulário
                    txtNome.setText(nome);
                    txtPrecoCusto.setText(String.format("%.2f", precoCusto)); // Formata com duas casas decimais
                    txtPrecoVenda.setText(String.format("%.2f", precoVenda)); // Formata com duas casas decimais
                    txtQuantidade.setText(String.valueOf(quantidade));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));

        // Divisão vertical entre o formulário e a tabela
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, scrollPane);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(false);

        // Adiciona ao frame
        add(painelCabecalho, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    // Método para aplicar o estilo aos botões
    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(150, 40));

        // Efeitos de hover
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
    public Produto getProdutoFormulario() {
        resetarCoresCampos();

        String nome = txtNome.getText().trim();
        String precoCustoText = txtPrecoCusto.getText().trim();
        String precoVendaText = txtPrecoVenda.getText().trim();
        String quantidadeText = txtQuantidade.getText().trim();

        boolean erro = false;

        // Validação do campo "Nome"
        if (nome.isEmpty()) {
            txtNome.setBackground(new Color(255, 200, 200));
            if (!erro) {
                txtNome.requestFocus();
                erro = true;
            }
        }

        // Validação do campo "Preço de Custo"
        Double precoCusto = validarEConverterPreco(precoCustoText, txtPrecoCusto);
        if (precoCusto == null || precoCusto < 0) {
            if (!erro) {
                erro = true;
            }
        }

        // Validação do campo "Preço de Venda"
        Double precoVenda = validarEConverterPreco(precoVendaText, txtPrecoVenda);
        if (precoVenda == null || precoVenda < 0) {
            if (!erro) {
                erro = true;
            }
        }

        // Validação do campo "Quantidade"
        int quantidade = 0;
        try {
            quantidade = Integer.parseInt(quantidadeText);
            if (quantidade < 0) {
                throw new NumberFormatException(); // Quantidade não pode ser negativa
            }
        } catch (NumberFormatException e) {
            txtQuantidade.setBackground(new Color(255, 200, 200));
            if (!erro) {
                txtQuantidade.requestFocus();
                erro = true;
            }
        }

        // Se houver erro, exibe uma mensagem e retorna null
        if (erro) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores válidos:\n" +
                    "- Nome: Não pode estar vazio.\n" +
                    "- Preço de Custo e Venda: Devem ser números válidos (ex: 10,50 ou R$ 10,50).\n" +
                    "- Quantidade: Deve ser um número inteiro positivo.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new Produto(0, nome, precoCusto, precoVenda, quantidade);
    }

    private Double validarEConverterPreco(String texto, JTextField campo) {
        // Remove o símbolo de moeda (R$) e espaços em branco
        texto = texto.replace("R$", "").trim();

        // Substitui a vírgula por ponto para conversão
        texto = texto.replace(",", ".");

        try {
            // Verifica se o valor tem mais de duas casas decimais
            if (temMaisDeDuasCasasDecimais(texto)) {
                campo.setBackground(new Color(255, 200, 200)); // Destaca o campo em vermelho
                campo.requestFocus(); // Coloca o foco no campo
                return null; // Retorna null para indicar erro
            }

            // Converte para double
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            campo.setBackground(new Color(255, 200, 200)); // Destaca o campo em vermelho
            campo.requestFocus(); // Coloca o foco no campo
            return null; // Retorna null se não for um número válido
        }
    }

    // Método para verificar se um valor tem mais de duas casas decimais
    private boolean temMaisDeDuasCasasDecimais(String valor) {
        int indexPonto = valor.indexOf(".");
        if (indexPonto != -1) {
            // Conta o número de caracteres após o ponto
            int casasDecimais = valor.length() - indexPonto - 1;
            return casasDecimais > 2;
        }
        return false; // Não tem casas decimais
    }

    // Método para restaurar a cor padrão dos campos
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
    }

    public void adicionarProdutoTabela(Produto produto) {
        modelo.addRow(new Object[]{produto.getId(), produto.getNome(), produto.getPrecoCusto(), produto.getPrecoVenda(), produto.getQuantidade()});
    }

    public void atualizarProdutoTabela(Produto produto) {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            modelo.setValueAt(produto.getNome(), linhaSelecionada, 1);
            modelo.setValueAt(produto.getPrecoCusto(), linhaSelecionada, 2);
            modelo.setValueAt(produto.getPrecoVenda(), linhaSelecionada, 3);
            modelo.setValueAt(produto.getQuantidade(), linhaSelecionada, 4);
        }
    }

    public void removerProdutoTabela() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            modelo.removeRow(linhaSelecionada);
        }
    }

    public int getProdutoSelecionadoId() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            return (int) modelo.getValueAt(linhaSelecionada, 0);
        }
        return -1;
    }

    public void carregarProdutos(List<Produto> produtos) {
        for (Produto produto : produtos) {
            modelo.addRow(new Object[]{produto.getId(), produto.getNome(), produto.getPrecoCusto(), produto.getPrecoVenda(), produto.getQuantidade()});
        }
    }

    // Classe interna ModeloTabela
    class ModeloTabela extends DefaultTableModel {
        public ModeloTabela() {
            super(new String[]{"ID", "Nome", "Preço de Custo (R$)", "Preço de Venda (R$)", "Quantidade"}, 0);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0: return Integer.class;  // ID
                case 1: return String.class;   // Nome
                case 2: return Double.class;   // Preço de Custo
                case 3: return Double.class;   // Preço de Venda
                case 4: return Integer.class;  // Quantidade
                default: return Object.class;
            }
        }
    }
}