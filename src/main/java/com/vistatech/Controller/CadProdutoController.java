package com.vistatech.Controller;

import com.vistatech.View.CadProdutoView;
import com.vistatech.GettersSetters.ProdutoEstoque;
import com.vistatech.Model.CadProdutoModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CadProdutoController {
    private CadProdutoModel model;
    private CadProdutoView view;

    public CadProdutoController(CadProdutoModel model, CadProdutoView view) {
        this.model = model;
        this.view = view;

        // Configura os listeners dos botões
        this.view.setAdicionarListener(new AdicionarListener());
        this.view.setAtualizarListener(new AtualizarListener());
        this.view.setRemoverListener(new RemoverListener());
        this.view.setLimparListener(new LimparListener());

        // Carrega os produtos do banco de dados ao iniciar
        carregarProdutos();
    }

    private void carregarProdutos() {
        List<ProdutoEstoque> produtoEstoques = model.carregarProdutos();
        view.carregarProdutos(produtoEstoques);
    }

    class AdicionarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ProdutoEstoque produtoEstoque = view.getProdutoFormulario();
            if (produtoEstoque != null) {
                // Adicionar o produto ao banco de dados (e à tabela) apenas se o usuário confirmar
                boolean cadastroConfirmado = model.adicionarProduto(produtoEstoque);
                if (cadastroConfirmado) {
                    view.adicionarProdutoTabela(produtoEstoque); // Adicionar à tabela apenas após confirmação
                    view.limparFormulario();
                }
            }
        }
    }

    class AtualizarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int id = view.getProdutoSelecionadoId();
            if (id != -1) {
                ProdutoEstoque produtoEstoque = view.getProdutoFormulario();
                if (produtoEstoque != null) {
                    produtoEstoque.setId(id);

                    // Obtém a quantidade atual do banco de dados
                    int quantidadeAtual = model.obterQuantidadeEstoque(id);
                    int quantidadeNova = produtoEstoque.getQuantidade();

                    // Verifica se a quantidade foi alterada
                    boolean quantidadeAlterada = quantidadeNova != quantidadeAtual;

                    // Se a quantidade foi alterada, aplica a lógica específica
                    if (quantidadeAlterada) {
                        if (quantidadeNova > quantidadeAtual) {
                            // Se o usuário tentar aumentar a quantidade, exibe um aviso
                            JOptionPane.showMessageDialog(
                                    view,
                                    "Para aumentar a quantidade, utilize o módulo de estoque.",
                                    "Aviso",
                                    JOptionPane.WARNING_MESSAGE
                            );

                            // Restaura o valor anterior da quantidade no formulário
                            view.getTxtQuantidade().setText(String.valueOf(quantidadeAtual));
                            return; // Interrompe a atualização
                        }

                        if (quantidadeNova < quantidadeAtual) {
                            // Se o usuário tentar reduzir a quantidade, exibe uma confirmação
                            int diferenca = quantidadeAtual - quantidadeNova;

                            String mensagem = "Deseja reduzir a quantidade em " + diferenca + " unidades?\n";

                            int confirmacao = JOptionPane.showConfirmDialog(
                                    view,
                                    mensagem,
                                    "Confirmar Redução",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            if (confirmacao != JOptionPane.YES_OPTION) {
                                // Se o usuário cancelar, restaura o valor anterior da quantidade no formulário
                                view.getTxtQuantidade().setText(String.valueOf(quantidadeAtual));
                                return; // Interrompe a atualização
                            }

                        }
                    }

                    // Se a quantidade não foi alterada, exibe a confirmação geral
                    if (!quantidadeAlterada) {
                        String mensagemAtualizacao = "Deseja atualizar o produto com os seguintes dados?\n\n" +
                                "Nome: " + produtoEstoque.getNome() + "\n" +
                                "Preço de Custo: R$ " + String.format("%.2f", produtoEstoque.getPrecoCusto()) + "\n" +
                                "Preço de Venda: R$ " + String.format("%.2f", produtoEstoque.getPrecoVenda()) + "\n" +
                                "Quantidade: " + produtoEstoque.getQuantidade() + "\n" +
                                "Tipo: " + produtoEstoque.getTipo();

                        int confirmacaoAtualizacao = JOptionPane.showConfirmDialog(
                                view,
                                mensagemAtualizacao,
                                "Confirmar Atualização",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );

                        if (confirmacaoAtualizacao != JOptionPane.YES_OPTION) {
                            // Se o usuário cancelar, restaura os valores anteriores no formulário
                            view.carregarProdutoSelecionado();
                            return; // Interrompe a atualização
                        }
                    }

                    // Atualiza o produto no banco de dados
                    model.atualizarProduto(produtoEstoque);
                    view.atualizarProdutoTabela(produtoEstoque);
                    view.limparFormulario();
                }
            }
        }
    }

    class RemoverListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int id = view.getProdutoSelecionadoId();
            if (id != -1) {
                model.removerProduto(id);
                view.removerProdutoTabela();
                view.limparFormulario();
            }
        }
    }

    class LimparListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.limparFormulario();
        }
    }
}