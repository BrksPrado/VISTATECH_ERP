package com.vistatech.Controller;

import com.vistatech.View.FinanceiroView;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class FinanceiroController {
    private final FinanceiroView view;
    private final NumberFormat currencyFormat;
    private Object[][] allData;

    public FinanceiroController(FinanceiroView view) {
        this.view = view;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        initializeController();
    }

    private void initializeController() {
        setupTableRenderer();
        loadMockData();
        setupEventListeners();
        showAllEntries();
    }

    private void setupTableRenderer() {
        view.table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Skip if table model not set yet
                if (view.tableModel.getRowCount() == 0) return c;

                // Get the value from the VALUE column (index 2)
                Object cellValue = view.tableModel.getValueAt(row, 2);
                if (cellValue == null) return c;

                double valor = Double.parseDouble(cellValue.toString().replaceAll("[^\\d.-]", ""));

                if (isSelected) {
                    c.setBackground(new Color(200, 200, 255)); // Selection color
                } else {
                    if (valor >= 0) {
                        c.setBackground(new Color(220, 255, 220)); // Light green for income
                    } else {
                        c.setBackground(new Color(255, 220, 220)); // Light red for expense
                    }
                }
                return c;
            }
        });
    }

    private void loadMockData() {
        this.allData = new Object[][]{
                {1, "Venda Óculos Ray-Ban Aviador", 899.90, "02/05/2023", "Entrada", "Cliente: João Silva", "Vendedor Carlos"},
                {2, "Manutenção Armação", 80.00, "03/05/2023", "Entrada", "Ajuste de hastes", "Técnico José"},
                {3, "Consulta Oftalmológica", 150.00, "03/05/2023", "Entrada", "Dr. Ana Paula", "Recepcionista"},
                {4, "Compra Lentes Transitions", -2250.00, "01/05/2023", "Saída", "Fornecedor LensTech", "Compras"},
                {5, "Aluguel Loja", -4500.00, "01/05/2023", "Saída", "Aluguel mensal", "Financeiro"},
                {6, "Venda Lentes Anti-reflexo", 420.00, "04/05/2023", "Entrada", "Cliente: Marta Rocha", "Vendedor Carlos"},
                {7, "Venda Óculos Infantil", 320.00, "05/05/2023", "Entrada", "Modelo Disney", "Vendedor Ana"},
                {8, "Adaptação de Lentes", 90.00, "05/05/2023", "Entrada", "Cliente idoso", "Técnico José"},
                {9, "Folha de Pagamento", -12500.00, "05/05/2023", "Saída", "Salários", "RH"},
                {10, "Compra Armações Oakley", -3200.00, "02/05/2023", "Saída", "10 unidades", "Compras"},
                {11, "Venda Óculos de Sol", 280.00, "06/05/2023", "Entrada", "Marca: Oakley", "Vendedor Ana"},
                {12, "Venda Lentes Progressivas", 780.00, "07/05/2023", "Entrada", "Cliente: Sr. Roberto", "Vendedor Carlos"},
                {13, "Manutenção Lensômetro", -350.00, "04/05/2023", "Saída", "Manutenção preventiva", "Técnico"},
                {14, "Venda Armação Gucci", 1299.00, "08/05/2023", "Entrada", "Edição limitada", "Vendedor Ana"},
                {15, "Material Limpeza", -120.00, "05/05/2023", "Saída", "Produtos especiais", "Limpeza"},
                {16, "Venda Colírio Systane", 45.00, "09/05/2023", "Entrada", "Pacote 10ml", "Balconista"},
                {17, "Conta de Energia", -580.00, "06/05/2023", "Saída", "Consumo mensal", "Financeiro"},
                {18, "Venda Estojo Premium", 65.00, "10/05/2023", "Entrada", "Couro legítimo", "Balconista"},
                {19, "Internet/Telefone", -199.90, "08/05/2023", "Saída", "Plano empresarial", "TI"},
                {20, "Venda Lenços Umedecidos", 25.00, "11/05/2023", "Entrada", "Pacote c/50", "Balconista"},
                {21, "Marketing Digital", -850.00, "09/05/2023", "Saída", "Google Ads", "Marketing"},
                {22, "Venda Óculos Esportivo", 520.00, "12/05/2023", "Entrada", "Marca: Nike", "Vendedor Carlos"},
                {23, "Seguro do Estabelecimento", -420.00, "10/05/2023", "Saída", "Seguro mensal", "Financeiro"},
                {24, "Venda Lentes Fotossensíveis", 680.00, "13/05/2023", "Entrada", "Cliente: Sra. Beatriz", "Vendedor Ana"},
                {25, "Água", -85.00, "11/05/2023", "Saída", "Consumo mensal", "Financeiro"},
                {26, "Venda Óculos Versace", 1599.00, "14/05/2023", "Entrada", "Edição coleção", "Vendedor Carlos"},
                {27, "Taxas Bancárias", -65.00, "12/05/2023", "Saída", "Tarifas mensais", "Financeiro"},
                {28, "Serviço de Emergência", 120.00, "15/05/2023", "Entrada", "Reparo urgente", "Técnico José"},
                {29, "Treinamento de Equipe", -750.00, "13/05/2023", "Saída", "Curso de vendas", "RH"},
                {30, "Venda Óculos Prada", 1399.00, "16/05/2023", "Entrada", "Modelo PR-57X", "Vendedor Ana"},
                {31, "Compra Equipamentos", -920.00, "14/05/2023", "Saída", "Nova lente teste", "Técnico"},
                {32, "Venda Lentes Blue Cut", 380.00, "17/05/2023", "Entrada", "Proteção digital", "Vendedor Carlos"},
                {33, "Uniforme Funcionários", -320.00, "15/05/2023", "Saída", "Novos uniformes", "RH"},
                {34, "Venda Óculos Dolce&Gabbana", 1499.00, "18/05/2023", "Entrada", "Coleção verão", "Vendedor Ana"},
                {35, "Manutenção Ar-condicionado", -280.00, "16/05/2023", "Saída", "Limpeza anual", "Manutenção"},
                {36, "Venda Lentes Transitions", 620.00, "19/05/2023", "Entrada", "Cliente: Maria Santos", "Vendedor Carlos"},
                {37, "Material Escritório", -150.00, "17/05/2023", "Saída", "Reposição", "Administrativo"},
                {38, "Venda Óculos Infantil", 290.00, "20/05/2023", "Entrada", "Modelo Frozen", "Vendedor Ana"},
                {39, "Taxa Cartão", -32.40, "18/05/2023", "Saída", "Taxa operadora", "Financeiro"},
                {40, "Venda Armação Titanium", 850.00, "21/05/2023", "Entrada", "Leve e resistente", "Vendedor Carlos"},
                {41, "Devolução Fornecedor", -120.00, "19/05/2023", "Saída", "Armação defeituosa", "Compras"},
                {42, "Venda Lentes Multifocais", 880.00, "22/05/2023", "Entrada", "Cliente: Sr. Afonso", "Vendedor Ana"},
                {43, "Serviço Gráfico", -150.00, "20/05/2023", "Saída", "Impressão materiais", "Marketing"},
                {44, "Venda Óculos de Grau", 420.00, "23/05/2023", "Entrada", "Modelo básico", "Vendedor Carlos"},
                {45, "Estacionamento", -28.00, "21/05/2023", "Saída", "Diárias mensais", "Frota"},
                {46, "Venda Lentes Contraste", 580.00, "24/05/2023", "Entrada", "Para dirigir", "Vendedor Ana"},
                {47, "Material Construção", -420.00, "22/05/2023", "Saída", "Reforma vitrine", "Manutenção"},
                {48, "Venda Óculos Esportivo", 520.00, "25/05/2023", "Entrada", "Marca: Adidas", "Vendedor Carlos"},
                {49, "Taxa Bancária", -35.00, "23/05/2023", "Saída", "Tarifa mensal", "Financeiro"},
                {50, "Venda Kit Limpeza", 45.00, "26/05/2023", "Entrada", "Spray + microfibra", "Balconista"},

        };
    }

    private void setupEventListeners() {
        view.entradaBtn.addActionListener(e -> filterPositiveEntries());
        view.saidaBtn.addActionListener(e -> filterNegativeEntries());
        view.limparBtn.addActionListener(e -> showAllEntries());
        view.salvarBtn.addActionListener(e -> showSaveMessage());
    }

    private void showAllEntries() {
        view.tableModel.setRowCount(0);
        for (Object[] row : allData) {
            view.tableModel.addRow(createTableRow(row));
        }
    }


    private void filterPositiveEntries() {
        view.tableModel.setRowCount(0);
        for (Object[] row : allData) {
            if ((Double) row[2] >= 0) {
                view.tableModel.addRow(createTableRow(row));
            }
        }
    }

    private void filterNegativeEntries() {
        view.tableModel.setRowCount(0);
        for (Object[] row : allData) {
            if ((Double) row[2] < 0) {
                view.tableModel.addRow(createTableRow(row));
            }
        }
    }

    private Object[] createTableRow(Object[] data) {
        return new Object[]{
                data[0], // ID
                data[1], // Name
                currencyFormat.format(data[2]), // Formatted value
                data[3], // Date
                data[4], // Type
                data[5], // Description
                data[6]  // User
        };
    }

    private void showSaveMessage() {
        JOptionPane.showMessageDialog(view,
                "Funcionalidade de salvar será implementada em breve",
                "Salvar",
                JOptionPane.INFORMATION_MESSAGE);
    }
}