import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueModel {
    public List<Produto> carregarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM produtos";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    double precoCusto = rs.getDouble("preco_custo");
                    double precoVenda = rs.getDouble("preco_venda");
                    int quantidade = rs.getInt("quantidade");
                    produtos.add(new Produto(id, nome, precoCusto, precoVenda, quantidade));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos do banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return produtos;
    }

    public void adicionarProduto(Produto produto) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "INSERT INTO produtos (nome, preco_custo, preco_venda, quantidade) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, produto.getNome());
                stmt.setDouble(2, produto.getPrecoCusto());
                stmt.setDouble(3, produto.getPrecoVenda());
                stmt.setInt(4, produto.getQuantidade());
                stmt.executeUpdate();

                // Recupera o ID gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar produto no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarProduto(Produto produto) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "UPDATE produtos SET nome = ?, preco_custo = ?, preco_venda = ?, quantidade = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, produto.getNome());
                stmt.setDouble(2, produto.getPrecoCusto());
                stmt.setDouble(3, produto.getPrecoVenda());
                stmt.setInt(4, produto.getQuantidade());
                stmt.setInt(5, produto.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar produto no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removerProduto(int id) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "DELETE FROM produtos WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao remover produto do banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}