package com.vistatech.Model;

import com.vistatech.Connection.DBconnectionConnection;
import com.vistatech.GettersSetters.MovimentacaoEstoque;
import com.vistatech.GettersSetters.ProductEstoque;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueModel {
    public List<ProductEstoque> carregarProdutos() {
        List<ProductEstoque> productEstoques = new ArrayList<>();
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT p.pdt_id, p.pdt_nome, p.pdt_preco_custo, p.pdt_preco_venda, p.pdt_tipo, e.est_quantidade, e.oculto " +
                    "FROM produtos p " +
                    "JOIN estoque e ON p.pdt_id = e.est_pdt_id " +
                    "WHERE e.oculto = 0"; // Filtra apenas produtos visíveis
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("pdt_id");
                    String nome = rs.getString("pdt_nome");
                    double precoCusto = rs.getDouble("pdt_preco_custo");
                    double precoVenda = rs.getDouble("pdt_preco_venda");
                    String tipo = rs.getString("pdt_tipo");
                    int quantidade = rs.getInt("est_quantidade");
                    boolean oculto = rs.getBoolean("oculto");

                    productEstoques.add(new ProductEstoque(id, nome, precoCusto, precoVenda, quantidade, tipo, oculto));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return productEstoques;
    }

    public List<ProductEstoque> pesquisarProdutos(String termo, String tipoPesquisa, boolean apenasVisiveis, int tabelaAtiva) {
        List<ProductEstoque> productEstoques = new ArrayList<>();
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql;
            if (tabelaAtiva == 2) { // Tabela de movimentações
                // Pesquisa por nome na tabela de movimentações
               /* sql = "SELECT me.mov_id, p.pdt_nome AS nome_produto, me.mov_tipo, me.mov_quantidade, me.mov_data " +
                        "FROM movimentacoes_estoque me " +
                        "JOIN estoque e ON me.mov_est_id = e.est_id " +
                        "JOIN produtos p ON e.est_pdt_id = p.pdt_id " +
                        "WHERE LOWER(p.pdt_nome) LIKE ?";*/
                sql = pesquisarMovimentacoes(termo, tipoPesquisa).toString();

            } else { // Tabela de produtos (visíveis ou ocultos)
                if (tipoPesquisa.startsWith("Quantidade")) {
                    // Determina o operador da pesquisa (menos que ou mais que)
                    String operador = tipoPesquisa.contains("menos") ? "<=" : ">=";

                    // Pesquisa por quantidade
                    sql = "SELECT p.pdt_id, p.pdt_nome, p.pdt_preco_custo, p.pdt_preco_venda, p.pdt_tipo, e.est_quantidade, e.oculto " +
                            "FROM produtos p " +
                            "JOIN estoque e ON p.pdt_id = e.est_pdt_id " +
                            "WHERE e.est_quantidade " + operador + " ? " +
                            (apenasVisiveis ? "AND e.oculto = 0" : "AND e.oculto = 1"); // Filtra produtos visíveis ou ocultos
                } else {
                    // Pesquisa por nome com ordenação personalizada
                    sql = "SELECT p.pdt_id, p.pdt_nome, p.pdt_preco_custo, p.pdt_preco_venda, p.pdt_tipo, e.est_quantidade, e.oculto " +
                            "FROM produtos p " +
                            "JOIN estoque e ON p.pdt_id = e.est_pdt_id " +
                            "WHERE LOWER(p.pdt_nome) LIKE ? " +
                            (apenasVisiveis ? "AND e.oculto = 0" : "AND e.oculto = 1") + // Filtra produtos visíveis ou ocultos
                            " ORDER BY CASE " +
                            "    WHEN LOWER(p.pdt_nome) LIKE ? THEN 1 " + // Produtos que começam com o termo
                            "    ELSE 2 " + // Demais produtos
                            " END, p.pdt_nome ASC"; // Ordem alfabética
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (tabelaAtiva == 2 || !tipoPesquisa.startsWith("Quantidade")) {
                    // Define o parâmetro para a pesquisa por nome
                    stmt.setString(1, "%" + termo.toLowerCase() + "%");
                    if (tabelaAtiva != 2 && !tipoPesquisa.startsWith("Quantidade")) {
                        // Adiciona o segundo parâmetro para o ORDER BY
                        stmt.setString(2, termo.toLowerCase() + "%");
                    }
                } else {
                    // Define o parâmetro para a pesquisa por quantidade
                    int quantidade = Integer.parseInt(termo);
                    stmt.setInt(1, quantidade);
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                        // Cria objetos Produto
                        int id = rs.getInt("pdt_id");
                        String nome = rs.getString("pdt_nome");
                        double precoCusto = rs.getDouble("pdt_preco_custo");
                        double precoVenda = rs.getDouble("pdt_preco_venda");
                        String tipo = rs.getString("pdt_tipo");
                        int quantidade = rs.getInt("est_quantidade");
                        boolean oculto = rs.getBoolean("oculto");

                        productEstoques.add(new ProductEstoque(id, nome, precoCusto, precoVenda, quantidade, tipo, oculto));

                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar produtos no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return productEstoques;
    }

    public List<MovimentacaoEstoque> pesquisarMovimentacoes(String termo, String tipoPesquisa) {
        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql;
            if (tipoPesquisa.startsWith("Quantidade")) {
                // Determina o operador da pesquisa (menos que ou mais que)
                String operador = tipoPesquisa.contains("menos") ? "<=" : ">=";

                // Pesquisa por quantidade
                sql = "SELECT me.mov_id, p.pdt_nome AS nome_produto, me.mov_tipo, me.mov_quantidade, me.mov_data, me.mov.editada " + ///  /////
                        "FROM movimentacoes_estoque me " +
                        "JOIN estoque e ON me.mov_est_id = e.est_id " +
                        "JOIN produtos p ON e.est_pdt_id = p.pdt_id " +
                        "WHERE me.mov_quantidade " + operador + " ?";
            } else {
                // Pesquisa por nome
                sql = "SELECT me.mov_id, p.pdt_nome AS nome_produto, me.mov_tipo, me.mov_quantidade, me.mov_data, me.mov.editada" + /// //////////
                        "FROM movimentacoes_estoque me " +
                        "JOIN estoque e ON me.mov_est_id = e.est_id " +
                        "JOIN produtos p ON e.est_pdt_id = p.pdt_id " +
                        "WHERE LOWER(p.pdt_nome) LIKE ?" +
                        " ORDER BY CASE " +
                        "    WHEN LOWER(p.pdt_nome) LIKE ? THEN 1 " + // Produtos que começam com o termo
                        "    ELSE 2 " + // Demais produtos
                        " END, p.pdt_nome ASC";;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (tipoPesquisa.startsWith("Quantidade")) {
                    int quantidade = Integer.parseInt(termo);
                    stmt.setInt(1, quantidade);
                } else {
                    stmt.setString(1, "%" + termo.toLowerCase() + "%");
                        // Adiciona o segundo parâmetro para o ORDER BY
                        stmt.setString(2, termo.toLowerCase() + "%");
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("mov_id");
                    String nomeProduto = rs.getString("nome_produto");
                    String tipo = rs.getString("mov_tipo");
                    int quantidade = rs.getInt("mov_quantidade");
                    String data = rs.getTimestamp("mov_data").toString();
                    boolean editado = rs.getBoolean("mov_editada"); // Recupera o campo mov_editado

                    movimentacoes.add(new MovimentacaoEstoque(id, nomeProduto, tipo, quantidade, data, editado));
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar movimentações no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return movimentacoes;
    }
    public boolean ocultarProduto(int id) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "UPDATE estoque SET oculto = 1 WHERE est_pdt_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ocultar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<ProductEstoque> carregarProdutosVisiveis() {
        List<ProductEstoque> productEstoques = new ArrayList<>();
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT p.pdt_id, p.pdt_nome, p.pdt_preco_custo, p.pdt_preco_venda, p.pdt_tipo, e.est_quantidade, e.oculto " +
                    "FROM produtos p " +
                    "JOIN estoque e ON p.pdt_id = e.est_pdt_id " +
                    "WHERE e.oculto = 0"; // Filtra produtos visíveis
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("pdt_id");
                    String nome = rs.getString("pdt_nome");
                    double precoCusto = rs.getDouble("pdt_preco_custo");
                    double precoVenda = rs.getDouble("pdt_preco_venda");
                    String tipo = rs.getString("pdt_tipo");
                    int quantidade = rs.getInt("est_quantidade");
                    boolean oculto = rs.getBoolean("oculto");

                    productEstoques.add(new ProductEstoque(id, nome, precoCusto, precoVenda, quantidade, tipo, oculto));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar produtos visíveis.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return productEstoques;
    }
    public boolean tornarProdutoVisivel(int id) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "UPDATE estoque SET oculto = 0 WHERE est_pdt_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tornar o produto visível.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<ProductEstoque> carregarProdutosOcultos() {
        List<ProductEstoque> productEstoques = new ArrayList<>();
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT p.pdt_id, p.pdt_nome, p.pdt_preco_custo, p.pdt_preco_venda, p.pdt_tipo, e.est_quantidade, e.oculto " +
                    "FROM produtos p " +
                    "JOIN estoque e ON p.pdt_id = e.est_pdt_id " +
                    "WHERE e.oculto = 1"; // Filtra produtos ocultos
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("pdt_id");
                    String nome = rs.getString("pdt_nome");
                    double precoCusto = rs.getDouble("pdt_preco_custo");
                    double precoVenda = rs.getDouble("pdt_preco_venda");
                    String tipo = rs.getString("pdt_tipo");
                    int quantidade = rs.getInt("est_quantidade");
                    boolean oculto = rs.getBoolean("oculto");

                    productEstoques.add(new ProductEstoque(id, nome, precoCusto, precoVenda, quantidade, tipo, oculto));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar produtos ocultos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return productEstoques;
    }

    public List<MovimentacaoEstoque> carregarMovimentacoesEstoque() {
        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT me.mov_id, p.pdt_nome AS nome_produto, me.mov_tipo, me.mov_quantidade, me.mov_data, me.mov_editada " +
                    "FROM movimentacoes_estoque me " +
                    "JOIN estoque e ON me.mov_est_id = e.est_id " +
                    "JOIN produtos p ON e.est_pdt_id = p.pdt_id " +
                    "ORDER BY me.mov_id DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("mov_id");
                    String nomeProduto = rs.getString("nome_produto");
                    String tipo = rs.getString("mov_tipo");
                    int quantidade = rs.getInt("mov_quantidade");
                    String data = rs.getTimestamp("mov_data").toString();
                    boolean editado = rs.getInt("mov_editada") == 1; // Recupera o campo mov_editado tinyInt para comparar com o boolean

                    // Cria o objeto MovimentacaoEstoque com o nome do produto
                    movimentacoes.add(new MovimentacaoEstoque(id, nomeProduto, tipo, quantidade, data, editado));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar movimentações de estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return movimentacoes;
    }

    public ProductEstoque obterProdutoPorId(int id) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT p.pdt_id, p.pdt_nome, p.pdt_preco_custo, p.pdt_preco_venda, p.pdt_tipo, e.est_quantidade, e.oculto " +
                    "FROM produtos p " +
                    "JOIN estoque e ON p.pdt_id = e.est_pdt_id " +
                    "WHERE p.pdt_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String nome = rs.getString("pdt_nome");
                    double precoCusto = rs.getDouble("pdt_preco_custo");
                    double precoVenda = rs.getDouble("pdt_preco_venda");
                    String tipo = rs.getString("pdt_tipo");
                    int quantidade = rs.getInt("est_quantidade");
                    boolean oculto = rs.getBoolean("oculto");

                    return new ProductEstoque(id, nome, precoCusto, precoVenda, quantidade, tipo, oculto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public boolean registrarMovimentacao(int estoqueId, String tipo, int quantidade) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            // Registrar a movimentação de estoque
            String sqlMovimentacao = "INSERT INTO movimentacoes_estoque (mov_est_id, mov_tipo, mov_quantidade) VALUES (?, ?, ?)";
            try (PreparedStatement stmtMovimentacao = conn.prepareStatement(sqlMovimentacao, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtMovimentacao.setInt(1, estoqueId);
                stmtMovimentacao.setString(2, tipo);
                stmtMovimentacao.setInt(3, quantidade);
                stmtMovimentacao.executeUpdate();

            }

            // Atualizar a quantidade em estoque
            String sqlEstoque = "UPDATE estoque SET est_quantidade = est_quantidade + ? WHERE est_id = ?";
            try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {
                int quantidadeModificada = tipo.equals("ENTRADA") ? quantidade : -quantidade;
                stmtEstoque.setInt(1, quantidadeModificada);
                stmtEstoque.setInt(2, estoqueId);
                stmtEstoque.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao registrar movimentação de estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public int obterIdEstoquePorProduto(int produtoId) {
        try (Connection conn = DBconnectionConnection.getConnection()) {
            String sql = "SELECT est_id FROM estoque WHERE est_pdt_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, produtoId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("est_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar ID do estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return -1; // Retorna -1 se o estoque não for encontrado
    }
}