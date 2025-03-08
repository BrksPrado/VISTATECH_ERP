import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EstoqueModel model = new EstoqueModel();
            EstoqueView view = new EstoqueView();
            new EstoqueController(model, view);
            view.setVisible(true);
        });
    }
}