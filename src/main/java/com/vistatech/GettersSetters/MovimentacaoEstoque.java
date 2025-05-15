package com.vistatech.GettersSetters;

public class MovimentacaoEstoque {
    private int id;
    private String nomeProduto; // Adicionar o campo nomeProduto
    private String tipo;
    private int quantidade;
    private String data;
    private boolean editado;

    // Construtor com nomeProduto
    public MovimentacaoEstoque(int id, String nomeProduto, String tipo, int quantidade, String data, boolean editado) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
        this.editado = editado;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNomeProduto() { // Adicionar o método getNomeProduto()
        return nomeProduto;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getData() {
        return data;
    }

    public boolean isEditado() {
        return editado;
    }

    public void setEditado(boolean editado) {
        this.editado = editado;
    }
}