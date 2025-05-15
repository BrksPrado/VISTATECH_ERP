package com.vistatech.Controller;

import com.vistatech.Model.EstoqueModel;
import com.vistatech.View.EstoqueView;
import com.vistatech.GettersSetters.ProductEstoque;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EstoqueController {
    private EstoqueModel model;
    private EstoqueView view;

    public EstoqueController(EstoqueModel model, EstoqueView view) {
        this.model = model;
        this.view = view;


        // Carrega todos os produtos ao iniciar
        carregarProdutos();
    }

    private void carregarProdutos() {
        List<ProductEstoque> productEstoques = model.carregarProdutos();
        view.carregarProdutos(productEstoques);
    }

    class PesquisarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String termo = view.getTermoPesquisa(); // Obtém o termo de pesquisa
            String tipoPesquisa = view.getTipoPesquisa(); // Obtém o tipo de pesquisa selecionado
            boolean apenasVisiveis = view.isApenasVisiveis(); // Obtém o estado de visibilidade
            int tabelaAtiva = view.getTabelaAtiva(); // Obtém o índice da tabela ativa

            // Realiza a pesquisa com o termo, o tipo de pesquisa, a visibilidade e a tabela ativa
            List<ProductEstoque> productEstoques = model.pesquisarProdutos(termo, tipoPesquisa, apenasVisiveis, tabelaAtiva);

            // Carrega os produtos na view
            view.carregarProdutos(productEstoques);
        }
    }
}
