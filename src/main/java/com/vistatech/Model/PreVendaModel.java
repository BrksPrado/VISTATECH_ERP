package com.vistatech.Model;

import com.vistatech.Connection.DataBaseConnection;
import com.vistatech.GettersSetters.Cliente;
import com.vistatech.GettersSetters.Produto;
import com.vistatech.GettersSetters.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.text.NumberFormat;
import java.util.Locale;

public class PreVendaModel {

    private final Map<String, Produto> produtosDB = new HashMap<>();
    private final Map<String, String> clientesDB = new HashMap<>();
    private final Map<String, String> vendedoresDB = new HashMap<>();

    private final List<ItemVenda> carrinho = new ArrayList<>();
    private double descontoPercentual = 0.0;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private Cliente clienteSelecionado;
    private Vendedor vendedorSelecionado;


    // Simulação de dados
    private final List<Cliente> listaClientes = new ArrayList<>();
    private final List<Vendedor> listaVendedores = new ArrayList<>();

    public PreVendaModel() {
        carregarProdutosDB();
        carregarClientesSimulados();
        carregarVendedoresSimulados();
    }

    private void carregarProdutosDB() {
        String sql = "SELECT pdt_id, pdt_nome, pdt_preco_custo, pdt_preco_venda, pdt_tipo FROM produtos";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("pdt_id");
                String nome = rs.getString("pdt_nome");
                double precoCusto = rs.getDouble("pdt_preco_custo");
                double precoVenda = rs.getDouble("pdt_preco_venda");
                String tipo = rs.getString("pdt_tipo");

                Produto produto = new Produto(id, nome, precoCusto, precoVenda, tipo);
                produtosDB.put(id, produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarClientesSimulados() {
        listaClientes.add(new Cliente(1, "João da Silva"));
        listaClientes.add(new Cliente(2, "Maria Oliveira"));
        listaClientes.add(new Cliente(3, "Carlos Souza"));
    }

    private void carregarVendedoresSimulados() {
        listaVendedores.add(new Vendedor(1, "Paulo Mendes"));
        listaVendedores.add(new Vendedor(2, "Fernanda Costa"));
        listaVendedores.add(new Vendedor(3, "Juliana Lima"));
    }

    // ==================== Getters =======================
    public Map<String, Produto> getProdutosDB() {
        return produtosDB;
    }

    public Map<String, String> getClientesDB() {
        return clientesDB;
    }

    public Map<String, String> getVendedoresDB() {
        return vendedoresDB;
    }

    public List<ItemVenda> getCarrinho() {
        return Collections.unmodifiableList(carrinho);
    }

    public double getDescontoPercentual() {
        return descontoPercentual;
    }

    public NumberFormat getCurrencyFormat() {
        return currencyFormat;
    }

    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public Vendedor getVendedorSelecionado() {
        return vendedorSelecionado;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public List<Vendedor> getListaVendedores() {
        return listaVendedores;
    }

    // ==================== Setters =======================


    public void setClienteSelecionado(Cliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    public void setVendedorSelecionado(Vendedor vendedorSelecionado) {
        this.vendedorSelecionado = vendedorSelecionado;
    }

    // ==================== Business logic =================
    public void addOrUpdateProduto(String produtoId, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida");
        Produto p = produtosDB.get(produtoId);
        if (p == null) throw new IllegalArgumentException("vistatech.GettersSetters.Produto não encontrado");

        carrinho.removeIf(iv -> iv.getProduto().getId().equals(produtoId));
        carrinho.add(new ItemVenda(p, quantidade));
    }

    public void removeProduto(String produtoId) {
        carrinho.removeIf(iv -> iv.getProduto().getId().equals(produtoId));
    }

    public void setDescontoPercentual(double desconto) {
        if (desconto < 0 || desconto > 100) throw new IllegalArgumentException("Desconto fora do intervalo");
        this.descontoPercentual = desconto;
    }

    public double calcularTotalBruto() {
        return carrinho.stream()
                .mapToDouble(ItemVenda::getSubtotal)
                .sum();
    }

    public double calcularTotalComDesconto() {
        double total = calcularTotalBruto();
        return total * (1 - descontoPercentual / 100.0);
    }

    public String formatCurrency(double value) {
        return currencyFormat.format(value);
    }

    public Cliente buscarClientePorNome(String nome) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getNome().equalsIgnoreCase(nome)) {
                return cliente;
            }
        }
        return null;
    }

    public Vendedor buscarVendedorPorNome(String nome) {
        for (Vendedor vendedor : listaVendedores) {
            if (vendedor.getNome().equalsIgnoreCase(nome)) {
                return vendedor;
            }
        }
        return null;
    }

    // ==================== Nested class ===================
    public static class ItemVenda {
        private final Produto produto;
        private final int quantidade;

        public ItemVenda(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public Produto getProduto() {
            return produto;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public double getSubtotal() {
            return produto.getPreco() * quantidade;
        }
    }
}
