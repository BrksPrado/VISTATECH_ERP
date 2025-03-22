package com.vistatech.relatorio;

import com.vistatech.cadastroprodutos.CadProdutoModel;

public class RelatorioProdutosMain {
    public static void main(String[] args) {
        // Inicializa o modelo de estoque
        CadProdutoModel cadProdutoModel = new CadProdutoModel();

        // Cria o serviço de relatórios
        RelatorioService relatorioService = new RelatorioService(cadProdutoModel);

        // Gera o relatório de produtos em PDF
        relatorioService.gerarRelatorioProdutosPDF();
    }
}