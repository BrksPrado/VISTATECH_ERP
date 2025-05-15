package com.vistatech.Model;

import com.vistatech.Connection.DBconnectionConnection;
import com.vistatech.GettersSetters.ProdutoEstoque;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CadProdutoModel {
    public List<ProdutoEstoque> carregarProdutos() {
        List<ProdutoEstoque> produtoEstoques = new ArrayList<>();
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT p.pdt_id, p.pdt_nome, p.pdt_preco_custo, p.pdt_preco_venda, p.pdt_tipo, e.est_quantidade " +
                    "FROM produtos p " +
                    "JOIN estoque e ON p.pdt_id = e.est_pdt_id";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("pdt_id");
                    String nome = rs.getString("pdt_nome");
                    double precoCusto = rs.getDouble("pdt_preco_custo");
                    double precoVenda = rs.getDouble("pdt_preco_venda");
                    String tipo = rs.getString("pdt_tipo");
                    int quantidade = rs.getInt("est_quantidade");

                    produtoEstoques.add(new ProdutoEstoque(id, nome, precoCusto, precoVenda, quantidade, tipo));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos do banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
        return produtoEstoques;
    }

    public boolean adicionarProduto(ProdutoEstoque produtoEstoque) {
        // Exibir JOptionPane para confirmação
        String mensagem = "Confirme os dados do produto:\n\n" +
                "Nome: " + produtoEstoque.getNome() + "\n" +
                "Preço de Custo: R$ " + String.format("%.2f", produtoEstoque.getPrecoCusto()) + "\n" +
                "Preço de Venda: R$ " + String.format("%.2f", produtoEstoque.getPrecoVenda()) + "\n" +
                "Quantidade: " + produtoEstoque.getQuantidade() + "\n" +
                "Tipo: " + produtoEstoque.getTipo() ;

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                mensagem,
                "Confirmar Cadastro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        // Se o usuário confirmar, prosseguir com o cadastro
        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Connection conn = DBconnectionConnection.getConnection()) {
                // Inserir na tabela de produtos
                String sqlProduto = "INSERT INTO produtos (pdt_nome, pdt_preco_custo, pdt_preco_venda, pdt_tipo) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmtProduto.setString(1, produtoEstoque.getNome());
                    stmtProduto.setDouble(2, produtoEstoque.getPrecoCusto());
                    stmtProduto.setDouble(3, produtoEstoque.getPrecoVenda());
                    stmtProduto.setString(4, produtoEstoque.getTipo());
                    stmtProduto.executeUpdate();

                    // Recupera o ID gerado para o produto
                    ResultSet rs = stmtProduto.getGeneratedKeys();
                    if (rs.next()) {
                        int produtoId = rs.getInt(1); // Recupera o ID gerado
                        produtoEstoque.setId(produtoId); // Atualiza o ID do objeto Produto

                        // Inserir na tabela de estoque e recuperar o ID gerado
                        String sqlEstoque = "INSERT INTO estoque (est_pdt_id, est_quantidade, oculto) VALUES (?, ?, ?)";
                        try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            stmtEstoque.setInt(1, produtoId);
                            stmtEstoque.setInt(2, produtoEstoque.getQuantidade());
                            stmtEstoque.setBoolean(3, false);
                            stmtEstoque.executeUpdate();

                            ResultSet rsEstoque = stmtEstoque.getGeneratedKeys();
                            if (rsEstoque.next()) {
                                int estoqueId = rsEstoque.getInt(1); // Agora pegamos o ID correto do estoque

                                // Registrar a quantidade inicial como uma movimentação padrão (não editada)
                                String sqlMovimentacao = "INSERT INTO movimentacoes_estoque (mov_est_id, mov_tipo, mov_quantidade, mov_editada) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement stmtMovimentacao = conn.prepareStatement(sqlMovimentacao)) {
                                    stmtMovimentacao.setInt(1, estoqueId); // Agora passamos o ID correto
                                    stmtMovimentacao.setString(2, "ENTRADA");
                                    stmtMovimentacao.setInt(3, produtoEstoque.getQuantidade());
                                    stmtMovimentacao.setBoolean(4, false);
                                    stmtMovimentacao.executeUpdate();
                                }
                            }
                        }


                        // Exibir mensagem de sucesso
                        JOptionPane.showMessageDialog(
                                null,
                                "Produto cadastrado com sucesso!",
                                "Cadastro Concluído",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        return true; // Cadastro confirmado e realizado com sucesso
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao adicionar produto no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Se o usuário cancelar, exibir mensagem de cancelamento
            JOptionPane.showMessageDialog(
                    null,
                    "Cadastro cancelado.",
                    "Cancelado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        return false; // Cadastro cancelado ou falhou
    }

    public void atualizarProduto(ProdutoEstoque produtoEstoque) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            // Atualiza a tabela de produtos
            String sqlProduto = "UPDATE produtos SET pdt_nome = ?, pdt_preco_custo = ?, pdt_preco_venda = ?, pdt_tipo = ? WHERE pdt_id = ?";
            try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {
                stmtProduto.setString(1, produtoEstoque.getNome());
                stmtProduto.setDouble(2, produtoEstoque.getPrecoCusto());
                stmtProduto.setDouble(3, produtoEstoque.getPrecoVenda());
                stmtProduto.setString(4, produtoEstoque.getTipo());
                stmtProduto.setInt(5, produtoEstoque.getId());
                stmtProduto.executeUpdate();
            }

            // Obtém a quantidade atual do estoque
            int quantidadeAtual = obterQuantidadeEstoque(produtoEstoque.getId());

            // Calcula a diferença entre a nova quantidade e a quantidade atual
            int diferenca = produtoEstoque.getQuantidade() - quantidadeAtual;

            if (diferenca != 0) {
                // Registra a movimentação de estoque
                String sqlMovimentacao = "INSERT INTO movimentacoes_estoque (mov_est_id, mov_tipo, mov_quantidade, mov_editada) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmtMovimentacao = conn.prepareStatement(sqlMovimentacao, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmtMovimentacao.setInt(1, produtoEstoque.getId());
                    stmtMovimentacao.setString(2, diferenca > 0 ? "ENTRADA" : "SAIDA");
                    stmtMovimentacao.setInt(3, Math.abs(diferenca));
                    stmtMovimentacao.setBoolean(4, true); // É uma edição manual
                    stmtMovimentacao.executeUpdate();

                    // Obtém o ID da movimentação de estoque gerada
                    ResultSet rs = stmtMovimentacao.getGeneratedKeys();
                    if (rs.next()) {
                        int movEstId = rs.getInt(1); // ID da movimentação de estoque

                    }
                }
            }

            // Atualiza a tabela de estoque
            String sqlEstoque = "UPDATE estoque SET est_quantidade = ? WHERE est_pdt_id = ?";
            try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {
                stmtEstoque.setInt(1, produtoEstoque.getQuantidade());
                stmtEstoque.setInt(2, produtoEstoque.getId());
                stmtEstoque.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar produto no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void removerProduto(int id) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            // Remover da tabela de estoque
            String sqlEstoque = "DELETE FROM estoque WHERE est_pdt_id = ?";
            try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {
                stmtEstoque.setInt(1, id);
                stmtEstoque.executeUpdate();
            }

            // Remover da tabela de produtos
            String sqlProduto = "DELETE FROM produtos WHERE pdt_id = ?";
            try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {
                stmtProduto.setInt(1, id);
                stmtProduto.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao remover produto do banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int obterQuantidadeEstoque(int produtoId) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT est_quantidade FROM estoque WHERE est_pdt_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, produtoId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("est_quantidade");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar quantidade do estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return -1; // Retorna -1 se o estoque não for encontrado
    }
}