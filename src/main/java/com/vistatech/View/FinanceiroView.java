// src/main/java/entrada/view/EntradaView.java
package com.vistatech.View;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FinanceiroView extends JFrame {

    // Componentes da interface
    public JTable table;
    public JButton salvarBtn, entradaBtn, saidaBtn, limparBtn;
    public DefaultTableModel tableModel;

    public FinanceiroView(){
        initLookAndFeel();
        configureFrameSettings();
        headerPanel();
        createTable();
        footerPanel();

    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureFrameSettings() {
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Financeiro");
        setIconImage(new ImageIcon("src/main/resources/logotipo.png").getImage());
        setLocationRelativeTo(null);

    }
    private void headerPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 140));
        headerPanel.setBackground(Color.WHITE);

        JPanel listraAzul = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        listraAzul.setPreferredSize(new Dimension(getWidth(), 90));
        listraAzul.setBackground(new Color(59, 89, 182));

        ImageIcon logo = new ImageIcon("src/main/resources/whitelogo.png");
        logo = new ImageIcon(logo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));

        JLabel title = new JLabel("VistaTech ERP - Financeiro");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));

        listraAzul.add(logoLabel);
        listraAzul.add(title);

        JPanel headLowerPanel = new JPanel(new BorderLayout());
        headLowerPanel.setPreferredSize(new Dimension(getWidth(), 30));
        headLowerPanel.setBackground(Color.WHITE);

        JLabel movimentacoesLabel = new JLabel("Movimentações da conta");
        movimentacoesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        movimentacoesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        movimentacoesLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setPreferredSize(new Dimension(400, 30)); // Ajuste o tamanho do painel
        buttonPanel.setBackground(Color.WHITE);

        entradaBtn = new JButton("Entrada");
        entradaBtn.setPreferredSize(new Dimension(100, 30));
        entradaBtn.setBackground(new Color(59, 89, 182));
        entradaBtn.setForeground(Color.WHITE);
        entradaBtn.setFont(new Font("Arial", Font.BOLD, 14));

        saidaBtn = new JButton("Saída");
        saidaBtn.setPreferredSize(new Dimension(100, 30));
        saidaBtn.setBackground(new Color(59, 89, 182));
        saidaBtn.setForeground(Color.WHITE);
        saidaBtn.setFont(new Font("Arial", Font.BOLD, 14));

        limparBtn = new JButton("Limpar");
        limparBtn.setPreferredSize(new Dimension(100, 30));
        limparBtn.setBackground(new Color(59, 89, 182));
        limparBtn.setForeground(Color.WHITE);
        limparBtn.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(entradaBtn);
        buttonPanel.add(saidaBtn);
        buttonPanel.add(limparBtn); // Certifique-se de adicionar o botão ao painel

        headLowerPanel.add(movimentacoesLabel, BorderLayout.WEST);
        headLowerPanel.add(buttonPanel, BorderLayout.EAST);

        headerPanel.add(listraAzul, BorderLayout.NORTH);
        headerPanel.add(headLowerPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
    }


    private void createTable(){
        String[] columnNames = {"ID","NOME", "VALOR", "DATA", "TIPO", "DESCRIÇÃO", "USUÁRIO"};
        Object[][] data = {};

        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(200, 200, 255));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

    }

    private void footerPanel(){
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        salvarBtn = new JButton("Salvar");
        salvarBtn.setPreferredSize(new Dimension(90, 30));
        salvarBtn.setBackground(new Color(59, 89, 182));
        salvarBtn.setForeground(Color.WHITE);
        salvarBtn.setFont(new Font("Arial", Font.BOLD, 14));

        footerPanel.add(salvarBtn);

        add(footerPanel, BorderLayout.SOUTH);
    }

}


