package com.vistatech.View;

import com.vistatech.Connection.DBconnectionConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CaixaTesteView extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField txtProdutoId, txtQuantidade;
    private JLabel lblSaldoCaixa;
    private double saldoCaixa = 10000.00; // Saldo inicial do caixa

    public CaixaTesteView() {
        setTitle("Módulo de Caixa");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        carregarMovimentacoes();
    }

    private void initComponents() {
        // Painel de formulário para venda
        JPanel painelVenda = new JPanel(new GridLayout(4, 2, 10, 10));
        painelVenda.setBorder(BorderFactory.createTitledBorder("Simulação de Venda"));

        painelVenda.add(new JLabel("ID do Produto:"));
        txtProdutoId = new JTextField();
        painelVenda.add(txtProdutoId);

        painelVenda.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        painelVenda.add(txtQuantidade);

        JButton btnVender = new JButton("Confirmar Venda");
        btnVender.addActionListener(new VenderListener());
        painelVenda.add(btnVender);

        // Exibição do saldo do caixa
        painelVenda.add(new JLabel("Saldo do Caixa:"));
        lblSaldoCaixa = new JLabel(String.format("R$ %.2f", saldoCaixa));
        painelVenda.add(lblSaldoCaixa);

        // Tabela de movimentações do caixa
        modelo = new DefaultTableModel(new String[]{"ID", "Produto", "Valor", "Tipo", "Data"}, 0);
        tabela = new JTable(modelo);

        JScrollPane scrollPane = new JScrollPane(tabela);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(painelVenda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void carregarMovimentacoes() {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT mvc.mvc_id, p.pdt_nome AS nome_produto, mvc.mvc_valor, mvc.mvc_tipo, mvc.mvc_data " +
                    "FROM movimentacoes_caixa mvc " +
                    "JOIN movimentacoes_estoque me ON mvc.mvc_mov_id = me.mov_id " +
                    "JOIN estoque e ON me.mov_est_id = e.est_id " +
                    "JOIN produtos p ON e.est_pdt_id = p.pdt_id";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("mvc_id");
                    String nomeProduto = rs.getString("nome_produto");
                    double valor = rs.getDouble("mvc_valor");
                    String tipo = rs.getString("mvc_tipo");
                    String data = rs.getTimestamp("mvc_data").toString();

                    modelo.addRow(new Object[]{id, nomeProduto, valor, tipo, data});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar movimentações do caixa.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Listener para o botão de venda
    class VenderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int produtoId = Integer.parseInt(txtProdutoId.getText());
                int quantidade = Integer.parseInt(txtQuantidade.getText());

                // Verifica se a quantidade é válida
                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(CaixaTesteView.this, "A quantidade deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Realiza a venda
                realizarVenda(produtoId, quantidade);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CaixaTesteView.this, "Por favor, insira valores válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para realizar a venda
    private void realizarVenda(int produtoId, int quantidade) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            // Verifica se o produto existe e obtém o preço de venda
            String sqlProduto = "SELECT pdt_id, pdt_preco_venda FROM produtos WHERE pdt_id = ?";
            try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {
                stmtProduto.setInt(1, produtoId);
                ResultSet rs = stmtProduto.executeQuery();

                if (rs.next()) {
                    double precoVenda = rs.getDouble("pdt_preco_venda");

                    // Verifica se há estoque suficiente
                    String sqlEstoque = "SELECT est_quantidade FROM estoque WHERE est_pdt_id = ?";
                    try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {
                        stmtEstoque.setInt(1, produtoId);
                        ResultSet rsEstoque = stmtEstoque.executeQuery();

                        if (rsEstoque.next()) {
                            int quantidadeAtual = rsEstoque.getInt("est_quantidade");

                            if (quantidade > quantidadeAtual) {
                                JOptionPane.showMessageDialog(this, "Estoque insuficiente.", "Erro", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Atualiza o estoque
                            String sqlAtualizaEstoque = "UPDATE estoque SET est_quantidade = est_quantidade - ? WHERE est_pdt_id = ?";
                            try (PreparedStatement stmtAtualizaEstoque = conn.prepareStatement(sqlAtualizaEstoque)) {
                                stmtAtualizaEstoque.setInt(1, quantidade);
                                stmtAtualizaEstoque.setInt(2, produtoId);
                                stmtAtualizaEstoque.executeUpdate();
                            }

                            // Registra a movimentação de estoque (SAIDA)
                            String sqlMovimentacaoEstoque = "INSERT INTO movimentacoes_estoque (mov_est_id, mov_tipo, mov_quantidade, mov_editada) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement stmtMovimentacaoEstoque = conn.prepareStatement(sqlMovimentacaoEstoque, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                stmtMovimentacaoEstoque.setInt(1, produtoId);
                                stmtMovimentacaoEstoque.setString(2, "SAIDA");
                                stmtMovimentacaoEstoque.setInt(3, quantidade);
                                stmtMovimentacaoEstoque.setBoolean(4, false); // Não é uma edição manual
                                stmtMovimentacaoEstoque.executeUpdate();

                                // Obtém o ID da movimentação de estoque gerada
                                ResultSet rsMovimentacao = stmtMovimentacaoEstoque.getGeneratedKeys();
                                if (rsMovimentacao.next()) {
                                    int movEstId = rsMovimentacao.getInt(1);

                                    // Calcula o valor total da venda
                                    double valorTotal = precoVenda * quantidade;

                                    // Atualiza o saldo do caixa
                                    saldoCaixa += valorTotal;
                                    lblSaldoCaixa.setText(String.format("R$ %.2f", saldoCaixa));

                                    // Registra a movimentação no caixa (ENTRADA)
                                    String sqlMovimentacaoCaixa = "INSERT INTO movimentacoes_caixa (mvc_mov_id, mvc_valor, mvc_tipo) VALUES (?, ?, ?)";
                                    try (PreparedStatement stmtMovimentacaoCaixa = conn.prepareStatement(sqlMovimentacaoCaixa)) {
                                        stmtMovimentacaoCaixa.setInt(1, movEstId);
                                        stmtMovimentacaoCaixa.setDouble(2, valorTotal);
                                        stmtMovimentacaoCaixa.setString(3, "ENTRADA");
                                        stmtMovimentacaoCaixa.executeUpdate();
                                    }
                                }
                            }

                            // Atualiza a tabela de movimentações do caixa
                            carregarMovimentacoes();

                            JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Produto não encontrado no estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao realizar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CaixaTesteView view = new CaixaTesteView();
            view.setVisible(true);
        });
    }
}