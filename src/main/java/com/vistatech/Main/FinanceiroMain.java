package com.vistatech.Main;

import com.vistatech.View.FinanceiroView;
import com.vistatech.Controller.FinanceiroController;
import javax.swing.*;

public class FinanceiroMain {
    public static void main(String[] args) {
        // Garante que a interface rode na thread correta
        SwingUtilities.invokeLater(() -> {
            // Cria a view
            FinanceiroView view = new FinanceiroView();

            // Cria o controller passando apenas a view
            new FinanceiroController(view);

            // Configurações finais da janela
            view.setVisible(true);
            view.setLocationRelativeTo(null); // Centraliza a janela
        });
    }
}