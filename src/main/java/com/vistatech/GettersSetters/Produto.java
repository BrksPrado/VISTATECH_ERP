package com.vistatech.GettersSetters;

import java.text.NumberFormat;
import java.util.Locale;

public class Produto {
    private final String id;
    private final String nome;
    private final double precoCusto;
    private final double precoVenda;
    private final String tipo;

    private static final NumberFormat CURRENCY =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public Produto(String id, String nome, double precoCusto, double precoVenda, String tipo) {
        this.id = id;
        this.nome = nome;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPreco() {
        return precoVenda;
    }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + CURRENCY.format(precoVenda) + ")";
    }
}