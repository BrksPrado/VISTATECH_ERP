package com.vistatech.Controller;


import com.vistatech.Model.FinanceiroModel;
import com.vistatech.View.FinanceiroView;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FinanceiroController {
    private final FinanceiroView view;
    private final FinanceiroModel model;
    private final NumberFormat currencyFormat;

    public FinanceiroController(FinanceiroView view) {
        this.view = view;
        this.model = new FinanceiroModel();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        initializeController();
    }

    private void initializeController() {
        setupTableRenderer();
        setupEventListeners();
        carregarDados();
    }

    private void setupTableRenderer() {
        view.table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (view.tableModel.getRowCount() == 0) return c;

                Object cellValue = view.tableModel.getValueAt(row, 2);
                if (cellValue == null) return c;

                try {
                    double valor = Double.parseDouble(cellValue.toString().replaceAll("[^\\d.-]", ""));

                    if (isSelected) {
                        c.setBackground(new Color(200, 200, 255));
                    } else {
                        if (valor >= 0) {
                            c.setBackground(new Color(220, 255, 220));
                        } else {
                            c.setBackground(new Color(255, 220, 220));
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return c;
            }
        });
    }

    private void setupEventListeners() {
        view.entradaBtn.addActionListener(e -> filtrarEntradas());
        view.saidaBtn.addActionListener(e -> filtrarSaidas());
        view.limparBtn.addActionListener(e -> carregarTodosDados());
        view.salvarBtn.addActionListener(e -> salvarMovimentacao());
    }

    private void carregarDados() {
        carregarTodosDados();
    }

    private void carregarTodosDados() {
        List<Object[]> dados = model.getTodasMovimentacoes();
        atualizarTabela(dados);
    }

    private void filtrarEntradas() {
        List<Object[]> dados = model.getEntradas();
        atualizarTabela(dados);
    }

    private void filtrarSaidas() {
        List<Object[]> dados = model.getSaidas();
        atualizarTabela(dados);
    }

    private void atualizarTabela(List<Object[]> dados) {
        view.tableModel.setRowCount(0);
        for (Object[] row : dados) {
            view.tableModel.addRow(createTableRow(row));
        }
    }

    private Object[] createTableRow(Object[] data) {
        return new Object[]{
                data[0], // ID
                data[1], // Nome
                currencyFormat.format(data[2]), // Valor formatado
                data[3], // Data
                data[4], // Tipo
                data[5], // Descrição
                data[6]  // Usuário
        };
    }

    private void salvarMovimentacao() {
        // Implementar lógica para adicionar nova movimentação
        JOptionPane.showMessageDialog(view,
                "Funcionalidade de salvar será implementada na próxima versão",
                "Salvar",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void fechar() {
        model.fecharConexao();
    }
}