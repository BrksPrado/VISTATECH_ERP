package com.vistatech.Main;

import com.vistatech.Model.CadProdutoModel;
import com.vistatech.Service.RelatorioService;

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