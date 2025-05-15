package com.vistatech.Controller;

import com.vistatech.GettersSetters.Cliente;
import com.vistatech.Model.PreVendaModel;
import com.vistatech.GettersSetters.Produto;
import com.vistatech.GettersSetters.Vendedor;
import com.vistatech.View.PreVendaView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;
import java.util.stream.Collectors;

public class PreVendaController {

    private final PreVendaModel model;
    private final PreVendaView view;

    public PreVendaController(PreVendaModel model, PreVendaView view) {
        this.model = model;
        this.view = view;
        initListeners();
        updateTotal();
        initController();
    }

    public void initController() {
        view.pesquisarClienteButton.addActionListener(e -> abrirDialogSelecaoCliente());
        view.pesquisarVendedorButton.addActionListener(e -> abrirDialogSelecaoVendedor());
    }

    private void abrirDialogSelecaoCliente() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Selecionar Cliente", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(view);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0);
        for (Cliente cliente : model.getListaClientes()) {
            tableModel.addRow(new Object[]{cliente.getId(), cliente.getNome()});
        }

        JTable tabela = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(tabela);
        dialog.add(scroll, BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        btnSelecionar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                int id = (int) tabela.getValueAt(row, 0);
                String nome = (String) tabela.getValueAt(row, 1);
                Cliente selecionado = new Cliente(id, nome);
                model.setClienteSelecionado(selecionado);
                view.setClienteSelecionado(nome);
                dialog.dispose();
            }
        });

        dialog.add(btnSelecionar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void abrirDialogSelecaoVendedor() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Selecionar Vendedor", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(view);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0);
        for (Vendedor vendedor : model.getListaVendedores()) {
            tableModel.addRow(new Object[]{vendedor.getId(), vendedor.getNome()});
        }

        JTable tabela = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(tabela);
        dialog.add(scroll, BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        btnSelecionar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                int id = (int) tabela.getValueAt(row, 0);
                String nome = (String) tabela.getValueAt(row, 1);
                Vendedor selecionado = new Vendedor(id, nome);
                model.setVendedorSelecionado(selecionado);
                view.setVendedorSelecionado(nome);
                dialog.dispose();
            }
        });

        dialog.add(btnSelecionar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Metodo para mostrar a lista de clientes com base no nome
    private void mostrarListaClientes(String nome) {
        // Filtra a lista de clientes com base no nome
        List<Cliente> resultado = model.getListaClientes().stream()
                .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());

        // Limpa a lista antiga e adiciona os novos resultados
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Cliente c : resultado) {
            listModel.addElement(c.getNome());  // Adiciona o nome do cliente na lista
        }

        // Atualiza o JList com os resultados filtrados
        view.getListClientes().setModel(listModel);
    }

    // Metodo para mostrar a lista de vendedores com base no nome
    private void mostrarListaVendedores(String nome) {
        // Filtra a lista de vendedores com base no nome
        List<Vendedor> resultado = model.getListaVendedores().stream()
                .filter(v -> v.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());

        // Limpa a lista antiga e adiciona os novos resultados
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Vendedor v : resultado) {
            listModel.addElement(v.getNome());  // Adiciona o nome do vendedor na lista
        }

        // Atualiza o JList com os resultados filtrados
        view.getListVendedores().setModel(listModel);
    }

//    view.pesquisarClienteButton().addActionListener(e -> {
//        String nomeDigitado = view.getCampoCliente().getText();
//        Cliente cliente = model.buscarClientePorNome(nomeDigitado);
//        if (cliente != null) {
//            view.setClienteSelecionado(cliente);
//        } else {
//            JOptionPane.showMessageDialog(view, "Cliente não encontrado!");
//        }
//    });
//
//    view.pesquisarVendedorButton().addActionListener(e -> {
//            String nomeDigitado = view.getCampoVendedor().getText();
//            Vendedor vendedor = model.buscarVendedorPorNome(nomeDigitado);
//            if (vendedor != null) {
//                view.setVendedorSelecionado(vendedor);
//            } else {
//                JOptionPane.showMessageDialog(view, "Vendedor não encontrado!");
//            }
//        });

    private void initListeners() {
        // Listener para selecionar cliente da lista
        view.getListClientes().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String nomeClienteSelecionado = view.getListClientes().getSelectedValue();
                if (nomeClienteSelecionado != null) {
                    // Preenche o campo de texto com o nome selecionado
                    view.getTxtNomeCliente().setText(nomeClienteSelecionado);

                    // Encontra o cliente no modelo
                    Cliente cliente = model.getListaClientes().stream()
                            .filter(c -> c.getNome().equals(nomeClienteSelecionado))
                            .findFirst()
                            .orElse(null);

                    if (cliente != null) {
                        model.setClienteSelecionado(cliente);
                    }
                }
            }
        });

        // Listener para selecionar vendedor da lista
        view.getListVendedores().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String nomeVendedorSelecionado = view.getListVendedores().getSelectedValue();
                if (nomeVendedorSelecionado != null) {
                    // Preenche o campo de texto com o nome selecionado
                    view.getTxtNomeVendedor().setText(nomeVendedorSelecionado);

                    // Encontra o vendedor no modelo
                    Vendedor vendedor = model.getListaVendedores().stream()
                            .filter(v -> v.getNome().equals(nomeVendedorSelecionado))
                            .findFirst()
                            .orElse(null);

                    if (vendedor != null) {
                        model.setVendedorSelecionado(vendedor);
                    }
                }
            }
        });

        // Pesquisa de Cliente ao pressionar Enter
        view.getTxtNomeCliente().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String nome = view.getTxtNomeCliente().getText().trim();
                    if (!nome.isEmpty()) {
                        mostrarListaClientes(nome);
                    }
                }
            }
        });

                // Pesquisa de Vendedor ao pressionar Enter
                view.getTxtNomeVendedor().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            String nome = view.getTxtNomeVendedor().getText().trim();
                            if (!nome.isEmpty()) {
                                mostrarListaVendedores(nome);
                            }
                        }
                    }
                });


        // ==== vistatech.GettersSetters.Produto field ====
        view.produtoField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                buscarProduto(view.produtoField.getText());
            }
        });

        view.pesquisarProdutoButton.addActionListener(e -> pesquisarProduto());

        view.adicionarButton.addActionListener(e -> adicionarOuAtualizarProduto());
        view.removerButton.addActionListener(e -> removerProduto());

        view.aplicarDescontoButton.addActionListener(e -> aplicarDesconto());

        view.descontoField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateTotal();
            }
        });

        view.finalizarVendaButton.addActionListener(e -> finalizarVenda());
    }

    // =================== Helpers ======================
    private void addAutoComplete(JTextField field, java.util.Map<String, String> dataMap, java.util.function.Consumer<String> onSelect) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }

            private void update() {
                String text = field.getText();
                if (text.length() < 1) return;

                JPopupMenu popup = new JPopupMenu();
                dataMap.entrySet().stream()
                        .filter(entry -> entry.getKey().startsWith(text) ||
                                entry.getValue().toLowerCase().contains(text.toLowerCase()))
                        .forEach(entry -> {
                            JMenuItem item = new JMenuItem(entry.getKey() + " - " + entry.getValue());
                            item.addActionListener(a -> {
                                field.setText(entry.getKey());
                                onSelect.accept(entry.getValue());
                            });
                            popup.add(item);
                        });
                if (popup.getComponentCount() > 0) {
                    popup.show(field, 0, field.getHeight());
                }
            }
        });
    }

    private void buscarProduto(String produtoId) {
        Produto p = model.getProdutosDB().get(produtoId);
        if (p != null) {
            view.produtoNomeLabel.setText("Nome: " + p.getNome());
            view.produtoPrecoLabel.setText("Preço: " + model.formatCurrency(p.getPreco()));
        } else {
            view.produtoNomeLabel.setText("Nome: ");
            view.produtoPrecoLabel.setText("Preço: ");
        }
    }

    private void pesquisarProduto() {
        String termo = JOptionPane.showInputDialog(view, "Digite ID ou Nome do produto:");
        if (termo == null || termo.isEmpty()) return;

        java.util.List<Produto> results = model.getProdutosDB().values().stream()
                .filter(p -> p.getId().contains(termo) || p.getNome().toLowerCase().contains(termo.toLowerCase()))
                .toList();

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nenhum produto encontrado");
            return;
        }
        Produto selecionado = (Produto) JOptionPane.showInputDialog(
                view, "Selecione o produto:", "Pesquisar vistatech.GettersSetters.Produto",
                JOptionPane.PLAIN_MESSAGE, null, results.toArray(), results.get(0)
        );
        if (selecionado != null) {
            view.produtoField.setText(selecionado.getId());
            buscarProduto(selecionado.getId());
        }
    }

    private void adicionarOuAtualizarProduto() {
        String id = view.produtoField.getText();
        int qtd;
        try {
            qtd = Integer.parseInt(view.quantidadeField.getText());
            model.addOrUpdateProduto(id, qtd);
            atualizarTabela();
            updateTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Quantidade inválida");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }
    }

    private void removerProduto() {
        int row = view.produtosTable.getSelectedRow();
        if (row == -1) return;
        String prodId = view.tableModel.getValueAt(row, 0).toString();
        model.removeProduto(prodId);
        view.tableModel.removeRow(row);
        updateTotal();
    }

    private void aplicarDesconto() {
        try {
            double d = Double.parseDouble(view.descontoField.getText());
            model.setDescontoPercentual(d);
            updateTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Desconto inválido");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage());
        }
    }

    private void updateTotal() {
        view.totalLabel.setText("Total: " + model.formatCurrency(model.calcularTotalComDesconto()));
    }

    private void atualizarTabela() {
        view.tableModel.setRowCount(0);
        for (PreVendaModel.ItemVenda iv : model.getCarrinho()) {
            String preco = model.formatCurrency(iv.getProduto().getPreco());
            String subtotal = model.formatCurrency(iv.getSubtotal());
            view.tableModel.addRow(new Object[]{
                    iv.getProduto().getId(),
                    iv.getProduto().getNome(),
                    preco,
                    iv.getQuantidade(),
                    subtotal
            });
        }
    }

    private void finalizarVenda() {
        if (model.getCarrinho().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Carrinho vazio!");
            return;
        }
        double total = model.calcularTotalComDesconto();
        JOptionPane.showMessageDialog(view, "Venda finalizada! Total: " + model.formatCurrency(total), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        // Limpa carrinho
        model.getCarrinho().clear();
        view.tableModel.setRowCount(0);
        updateTotal();
    }
}
