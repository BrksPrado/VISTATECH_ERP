package com.vistatech.Main;

import com.vistatech.Controller.PreVendaController;
import com.vistatech.Model.PreVendaModel;
import com.vistatech.View.PreVendaView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PreVendaModel model = new PreVendaModel();
            PreVendaView view = new PreVendaView();
            new PreVendaController(model, view);
            view.setVisible(true);
        });
    }
}
