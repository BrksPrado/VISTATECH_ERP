package Functions;

import java.awt.*;

public class MachineSettings {

    // Variáveis de resolução da tela
    private int screenWidth;
    private int screenHeight;

    // Construtor para inicializar a resolução
    public MachineSettings() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();
        this.screenWidth = dm.getWidth();
        this.screenHeight = dm.getHeight();
    }

    public int getResolutiony(double porcentagem) {
        return (int) (screenHeight * porcentagem);
    }

    public int getResolutionx(double porcentagem) {
        return (int) (screenWidth * porcentagem);
    }
}