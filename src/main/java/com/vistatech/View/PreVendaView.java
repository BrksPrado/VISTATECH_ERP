package com.vistatech.View;

import com.vistatech.GettersSetters.Cliente;
import com.vistatech.GettersSetters.Vendedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class PreVendaView extends JFrame {

    private ImageIcon iconeCheckmark;
    private ImageIcon iconePencil;
    private JLabel lblLogo;

    public final JTextField clienteField = new JTextField(10);
    public final JLabel clienteNomeLabel = new JLabel("Nome do Cliente: ");
    public final JButton pesquisarClienteButton = new JButton("Pesquisar Cliente");

    public final JTextField vendedorField = new JTextField(10);
    public final JLabel vendedorNomeLabel = new JLabel("Nome do Vendedor: ");
    public final JButton pesquisarVendedorButton = new JButton("Pesquisar Vendedor");

    public final JTextField produtoField = new JTextField(2);
    public final JButton pesquisarProdutoButton = new JButton("⌕");
    public final JLabel produtoNomeLabel = new JLabel("Nome: ");
    public final JLabel produtoPrecoLabel = new JLabel("Preço: ");
    public final JTextField quantidadeField = new JTextField(5);

    public final JButton adicionarButton = new JButton("Adicionar");
    public final JButton removerButton = new JButton("Remover");

    private final String[] colunas = {"ID", "Nome", "Preço", "Quantidade", "Subtotal"};
    public final DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
    public final JTable produtosTable = new JTable(tableModel);

    public final JTextField descontoField = new JTextField(5);
    public final JButton aplicarDescontoButton = new JButton("Aplicar Desconto");
    public final JLabel totalLabel = new JLabel("Total: R$ 0,00");
    public final JButton finalizarVendaButton = new JButton("Finalizar Venda");

    private final DefaultListModel<String> listModelClientes = new DefaultListModel<>();
    private final DefaultListModel<String> listModelVendedores = new DefaultListModel<>();
    private final JList<String> listClientes = new JList<>(listModelClientes);
    private final JList<String> listVendedores = new JList<>(listModelVendedores);

    public PreVendaView() {
        super("VistaTech ERP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 950);
        setLocationRelativeTo(null);

        // Cores do sistema
        Color primaryColor = new Color(59, 89, 182); // Cor original do cabeçalho
        Color secundaryColor = new Color(41, 128, 185);
        Color lightGray = new Color(245, 245, 245);
        Color darkGray = new Color(52, 73, 94);

        // Painel do cabeçalho
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lblLogo = new JLabel(redimensionarImagem("src/main/resources/whitelogo.png", 50, 50));
        JLabel lblTitulo = new JLabel("Pré-Venda");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        headerPanel.add(lblLogo);
        headerPanel.add(lblTitulo);

        // Estilização dos campos de pesquisa
        JPanel searchFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        searchFieldsPanel.setBackground(Color.WHITE);
        searchFieldsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Estilo para campos de texto
        clienteField.setPreferredSize(new Dimension(250, 35));
        vendedorField.setPreferredSize(new Dimension(250, 35));
        produtoField.setPreferredSize(new Dimension(250, 35));
        quantidadeField.setPreferredSize(new Dimension(100, 35));

        // Estilização dos rótulos
        clienteNomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        vendedorNomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        produtoNomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        produtoPrecoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Estilização da tabela de produtos
        produtosTable.setRowHeight(40);
        produtosTable.setIntercellSpacing(new Dimension(10, 10));
        produtosTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        produtosTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        produtosTable.getTableHeader().setBackground(darkGray);
        produtosTable.getTableHeader().setForeground(Color.WHITE);
        produtosTable.setSelectionBackground(new Color(232, 241, 249));
        produtosTable.setSelectionForeground(Color.BLACK);

        // Painel de totais e desconto com destaque
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        totalsPanel.setBackground(new Color(245, 245, 245));
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Estilização do campo de desconto
        JLabel descontoLabel = new JLabel("DESCONTO (%):");
        descontoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        descontoField.setPreferredSize(new Dimension(100, 40));
        descontoField.setFont(new Font("Segoe UI", Font.BOLD, 18));
        descontoField.setHorizontalAlignment(JTextField.CENTER);

        // Estilização do total
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalLabel.setForeground(new Color(41, 128, 185));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Botões principais
        finalizarVendaButton.setPreferredSize(new Dimension(200, 50));
        finalizarVendaButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        finalizarVendaButton.setBackground(new Color(46, 204, 113));
        finalizarVendaButton.setForeground(Color.WHITE);

        aplicarDescontoButton.setPreferredSize(new Dimension(150, 40));
        aplicarDescontoButton.setBackground(secundaryColor);
        aplicarDescontoButton.setForeground(Color.WHITE);

        // Atualização do layout dos painéis
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPanel(), buildRightPanel());
        splitPane.setDividerLocation(300);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageIcon carregarIcone(String caminho) {
        URL url = getClass().getResource(caminho);
        if (url == null) {
            System.err.println("Recurso não encontrado: " + caminho);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image imagemRedimensionada = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        return new ImageIcon(imagemRedimensionada);
    }

    private ImageIcon redimensionarImagem(String caminho, int largura, int altura) {
        ImageIcon imageIcon = new ImageIcon(caminho);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(largura, altura, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    private void initComponents() {
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        produtosTable.setDefaultEditor(Object.class, null);
        produtosTable.setFillsViewportHeight(true);
        produtosTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        produtosTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        produtosTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        produtosTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        produtosTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCabecalho.setBackground(new Color(59, 89, 182));

        lblLogo = new JLabel(redimensionarImagem("src/main/resources/whitelogo.png", 65, 65));
        JLabel lblTitulo = new JLabel("VistaTech ERP - Pré-Venda");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        painelCabecalho.add(lblLogo);
        painelCabecalho.add(lblTitulo);

        add(painelCabecalho, BorderLayout.NORTH);

        listClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listClientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selecionado = listClientes.getSelectedValue();
                if (selecionado != null) {
                    String[] partes = selecionado.split(" - ");
                    clienteField.setText(partes[0]);
                    clienteNomeLabel.setText("Nome do Cliente: " + partes[1]);
                }
            }
        });

        listVendedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listVendedores.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selecionado = listVendedores.getSelectedValue();
                if (selecionado != null) {
                    String[] partes = selecionado.split(" - ");
                    vendedorField.setText(partes[0]);
                    vendedorNomeLabel.setText("Nome do Vendedor: " + partes[1]);
                }
            }
        });
    }

    public void setClienteSelecionado(String nome) {
        clienteNomeLabel.setText("Nome do Cliente: " + nome);
    }

    public void setVendedorSelecionado(String nome) {
        vendedorNomeLabel.setText("Nome do Vendedor: " + nome);
    }


    public void atualizarListaClientes(java.util.List<Cliente> clientes, String filtro) {
        listModelClientes.clear();
        clientes.stream()
                .filter(c -> String.valueOf(c.getId()).contains(filtro) || c.getNome().toLowerCase().contains(filtro.toLowerCase()))
                .forEach(c -> listModelClientes.addElement(c.getId() + " - " + c.getNome()));
    }

    public void atualizarListaVendedores(java.util.List<Vendedor> vendedores, String filtro) {
        listModelVendedores.clear();
        vendedores.stream()
                .filter(v -> String.valueOf(v.getId()).contains(filtro) || v.getNome().toLowerCase().contains(filtro.toLowerCase()))
                .forEach(v -> listModelVendedores.addElement(v.getId() + " - " + v.getNome()));
    }

    public JTextField getTxtNomeCliente() {
        return clienteField;
    }

    public JTextField getTxtNomeVendedor() {
        return vendedorField;
    }

    public JList<String> getListClientes() {
        return listClientes;
    }

    public JList<String> getListVendedores() {
        return listVendedores;
    }

    // Atualizando o metodo buildLeftPanel
    private JPanel buildLeftPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(224, 224, 224)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        ));

        // Estilizando labels
        JLabel clienteLabel = new JLabel("CLIENTE");
        clienteLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel vendedorLabel = new JLabel("VENDEDOR");
        vendedorLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        p.add(clienteLabel);
        p.add(Box.createVerticalStrut(5));
        p.add(clienteField);
        p.add(Box.createVerticalStrut(5));
        p.add(clienteNomeLabel);
        p.add(Box.createVerticalStrut(10));
        p.add(pesquisarClienteButton);
        p.add(Box.createVerticalStrut(20));

        p.add(vendedorLabel);
        p.add(Box.createVerticalStrut(5));
        p.add(vendedorField);
        p.add(Box.createVerticalStrut(5));
        p.add(vendedorNomeLabel);
        p.add(Box.createVerticalStrut(10));
        p.add(pesquisarVendedorButton);

        return p;
    }


    private JPanel buildRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rightPanel.setBackground(Color.WHITE);

        // Painel de produtos com layout melhorado
        JPanel produtoPanel = new JPanel(new GridBagLayout());
        produtoPanel.setBackground(Color.WHITE);
        produtoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        "Informações do Produto"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID do Produto com ícone de pesquisa
        JLabel idLabel = new JLabel("ID / CÓDIGO");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 0;
        produtoPanel.add(idLabel, gbc);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        produtoField.setPreferredSize(new Dimension(200, 35));
        pesquisarProdutoButton.setPreferredSize(new Dimension(35, 35));
        pesquisarProdutoButton.setFocusPainted(false);
        pesquisarProdutoButton.setBackground(new Color(41, 128, 185));
        pesquisarProdutoButton.setForeground(Color.WHITE);
        pesquisarProdutoButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(produtoField, BorderLayout.CENTER);
        searchPanel.add(pesquisarProdutoButton, BorderLayout.EAST);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        produtoPanel.add(searchPanel, gbc);

        // Nome do Produto
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel nomeLabel = new JLabel("NOME DO PRODUTO");
        nomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        produtoPanel.add(nomeLabel, gbc);

        produtoNomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        produtoPanel.add(produtoNomeLabel, gbc);

        // Preço
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel precoLabel = new JLabel("PREÇO UNITÁRIO");
        precoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        produtoPanel.add(precoLabel, gbc);

        produtoPrecoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        produtoPrecoLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        produtoPanel.add(produtoPrecoLabel, gbc);

        // Quantidade
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.0;
        JLabel qtdLabel = new JLabel("QUANTIDADE");
        qtdLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        produtoPanel.add(qtdLabel, gbc);

        quantidadeField.setPreferredSize(new Dimension(100, 35));
        quantidadeField.setHorizontalAlignment(JTextField.CENTER);
        quantidadeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        produtoPanel.add(quantidadeField, gbc);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoesPanel.setBackground(Color.WHITE);

        adicionarButton.setPreferredSize(new Dimension(150, 40));
        adicionarButton.setBackground(new Color(46, 204, 113));
        adicionarButton.setForeground(Color.WHITE);
        adicionarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adicionarButton.setFocusPainted(false);

        removerButton.setPreferredSize(new Dimension(150, 40));
        removerButton.setBackground(new Color(231, 76, 60));
        removerButton.setForeground(Color.WHITE);
        removerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removerButton.setFocusPainted(false);

        botoesPanel.add(adicionarButton);
        botoesPanel.add(removerButton);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        produtoPanel.add(botoesPanel, gbc);

        // Tabela de produtos
        produtosTable.setRowHeight(40);
        produtosTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTableHeader header = produtosTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scrollPane = new JScrollPane(produtosTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Painel de totais com destaque
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        totalPanel.setBackground(new Color(245, 245, 245));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Desconto
        JLabel descontoLabel = new JLabel("DESCONTO (%)");
        descontoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        descontoField.setPreferredSize(new Dimension(80, 40));
        descontoField.setFont(new Font("Segoe UI", Font.BOLD, 18));
        descontoField.setHorizontalAlignment(JTextField.CENTER);

        aplicarDescontoButton.setPreferredSize(new Dimension(150, 40));
        aplicarDescontoButton.setBackground(new Color(41, 128, 185));
        aplicarDescontoButton.setForeground(Color.WHITE);
        aplicarDescontoButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aplicarDescontoButton.setFocusPainted(false);

        // Total em destaque
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        totalLabel.setForeground(new Color(41, 128, 185));

        // Botão finalizar
        finalizarVendaButton.setPreferredSize(new Dimension(200, 50));
        finalizarVendaButton.setBackground(new Color(46, 204, 113));
        finalizarVendaButton.setForeground(Color.WHITE);
        finalizarVendaButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        finalizarVendaButton.setFocusPainted(false);

        totalPanel.add(descontoLabel);
        totalPanel.add(descontoField);
        totalPanel.add(aplicarDescontoButton);
        totalPanel.add(totalLabel);
        totalPanel.add(finalizarVendaButton);

        // Montagem final do painel
        rightPanel.add(produtoPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(totalPanel, BorderLayout.SOUTH);

        return rightPanel;
    }
}
