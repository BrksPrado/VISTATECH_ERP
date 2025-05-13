
package com.vistatech.Main;

import com.vistatech.View.FinanceiroView;
import com.vistatech.Controller.FinanceiroController;

public class FinanceiroMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FinanceiroView view = new FinanceiroView();
            new FinanceiroController(view);
            view.setVisible(true);
        });
    }
}
