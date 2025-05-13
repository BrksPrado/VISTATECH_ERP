// src/main/java/com/vistatech/model/financeiroModel.java
package com.vistatech.Model;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceiroModel {
    private List<Object[]> movimentacoes;
    private Connection connection;

    public FinanceiroModel() {
        this.movimentacoes = new ArrayList<>();
        conectarBancoDados();
        criarTabelaSeNaoExistir();
    }

    private void conectarBancoDados() {
        try {
            String url = "jdbc:mysql://localhost:3306/estoque_db";
            String usuario = "root";
            String senha = "admin"; // substitua pela sua senha

            this.connection = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage(),
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS movimentacoes_financeiro (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100) NOT NULL," +
                "valor DECIMAL(10,2) NOT NULL," +
                "data VARCHAR(10) NOT NULL," +
                "tipo VARCHAR(10) NOT NULL," +
                "descricao VARCHAR(200)," +
                "usuario VARCHAR(50))";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adicionarMovimentacao(Object[] movimentacao) {
        String sql = "INSERT INTO movimentacoes_financeiro (nome, valor, data, tipo, descricao, usuario) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, (String) movimentacao[1]);
            pstmt.setDouble(2, (Double) movimentacao[2]);
            pstmt.setString(3, (String) movimentacao[3]);
            pstmt.setString(4, (String) movimentacao[4]);
            pstmt.setString(5, (String) movimentacao[5]);
            pstmt.setString(6, (String) movimentacao[6]);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> getTodasMovimentacoes() {
        List<Object[]> movimentacoes = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes_financeiro ORDER BY data DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] mov = new Object[]{
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("valor"),
                        rs.getString("data"),
                        rs.getString("tipo"),
                        rs.getString("descricao"),
                        rs.getString("usuario")
                };
                movimentacoes.add(mov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movimentacoes;
    }

    public List<Object[]> getEntradas() {
        return filtrarMovimentacoes("valor > 0");
    }

    public List<Object[]> getSaidas() {
        return filtrarMovimentacoes("valor < 0");
    }

    private List<Object[]> filtrarMovimentacoes(String condicao) {
        List<Object[]> movimentacoes = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes_financeiro WHERE " + condicao + " ORDER BY data DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] mov = new Object[]{
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("valor"),
                        rs.getString("data"),
                        rs.getString("tipo"),
                        rs.getString("descricao"),
                        rs.getString("usuario")
                };
                movimentacoes.add(mov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movimentacoes;
    }

    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}