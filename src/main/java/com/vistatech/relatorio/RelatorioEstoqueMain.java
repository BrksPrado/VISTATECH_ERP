package com.vistatech.relatorio;

import com.vistatech.estoque.EstoqueModel;

public class RelatorioEstoqueMain {
    public static void main(String[] args) {
        // Inicializa o modelo de estoque
        EstoqueModel estoqueModel = new EstoqueModel();

        // Cria o serviço de relatórios
        RelatorioService relatorioService = new RelatorioService(estoqueModel);

        // Gera o relatório de produtos em PDF
        relatorioService.gerarRelatorioProdutosPDF();
    }
}