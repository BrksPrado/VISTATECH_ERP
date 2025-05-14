package MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import CadUsuario.UserView;
import CadCliente.ClientView;
import com.vistatech.Controller.EstoqueController;
import com.vistatech.Controller.FinanceiroController;
import com.vistatech.Model.CadProdutoModel;
import com.vistatech.Model.FinanceiroModel;
import com.vistatech.View.CadProdutoView;
import com.vistatech.View.EstoqueView;
import com.vistatech.Model.EstoqueModel;
import com.vistatech.Controller.CadProdutoController;
import com.vistatech.View.FinanceiroView;

public class MainScreenView extends JFrame {

    private static final Dimension FIXED_DIMENSION = new Dimension(1600, 800);

    public MainScreenView(boolean adminValidation) {
        setTitle("Sistema ERP - Tela Inicial");
        //initLookAndFeel();
        setIconImage(new ImageIcon("src/main/resources/logotipo.png").getImage());

        // Inicializa a janela como maximizada
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);


        // Listener para monitorar mudanças no estado da janela
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (!isMaximized() && !isMinimized()) {
                    setResizable(true);
                    setSize(FIXED_DIMENSION);
                } else if (isMaximized()) {
                    setResizable(true);
                }
            }
        });

        // Listener adicional para redimensionamento (usar tamanho fixo quando restaurada)
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Quando não está maximizada nem minimizada, mantém o tamanho fixo
                if (!isMaximized() && !isMinimized()) {
                    setSize(FIXED_DIMENSION);
                }
            }
        });

        setLayout(new BorderLayout());

        JPanel hotbar = new JPanel();
        hotbar.setLayout(new GridLayout(1, 0, 10, 0)); // Distribui os botões igualmente no espaço
        hotbar.setBackground(new Color(59, 89, 182)); // Cor de fundo do cabeçalho
        hotbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margens internas

        //Menu suspenso de cadastro --------------------------
        JPopupMenu popMenuCadastro = new JPopupMenu();
        popMenuCadastro.setBackground(new Color(59, 89, 182));
        popMenuCadastro.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        popMenuCadastro.setPreferredSize(new Dimension(250, 200));

        JMenuItem menuUsuario = new JMenuItem("Usuário");
        popMenuCadastro.add(menuUsuario);
        JMenuItem menuCliente = new JMenuItem("Cliente");
        popMenuCadastro.add(menuCliente);
        JMenuItem menuProduto = new JMenuItem("Produto");
        popMenuCadastro.add(menuProduto);

        JMenuItem[] menuItens = {menuUsuario, menuCliente, menuProduto};

        for (JMenuItem item : menuItens) {
           estilizarMenuitens(item);
        }

        // Botões do menu principal e funcionalidades
        String[] modules = {"Cadastro", "Pré Venda", "Estoque", "Financeiro", "Notas Eletrônicas", "Relatórios", "Configurações"};
        for (String module : modules) {
            JButton button = createHoverButton(module);

            // Adiciona funcionalidade ao botão "Cadastro"
            if (module.equals("Cadastro")) {
                button.addActionListener(e -> popMenuCadastro.show(button, 0, button.getHeight()));
                menuUsuario.addActionListener(e -> {
                    if (adminValidation == false) {
                        JOptionPane.showMessageDialog(null, "Sem permissão de acesso!!");
                    }else{
                        UserView userView = new UserView(); userView.setVisible(true);
                    }
                });
                menuCliente.addActionListener(e -> {ClientView clientView = new ClientView(); clientView.setVisible(true);});
                menuProduto.addActionListener(e -> {
                    CadProdutoModel model = new CadProdutoModel();
                    CadProdutoView produtoView = new CadProdutoView(model);
                    new CadProdutoController(model, produtoView); // Inicializa o controlador
                    produtoView.setVisible(true);
                });
            }

            // Adiciona funcionalidade ao botão "Estoque Produtos"
            if (module.equals("Estoque")) {
                button.addActionListener(e -> {
                    EstoqueModel estoqueModel = new EstoqueModel(); // Instancia o modelo
                    EstoqueView estoqueView = new EstoqueView(estoqueModel); // Passa o modelo ao construtor
                    EstoqueController estoqueController = new EstoqueController(estoqueModel, estoqueView); // Inicializa o controlador
                    estoqueView.setVisible(true);
                });
            }

            if (module.equals("Financeiro")) {
                button.addActionListener(e -> {
                    FinanceiroView financeiroView = new FinanceiroView();
                    new FinanceiroModel();
                    new FinanceiroController(financeiroView);
                    financeiroView.setVisible(true);
                });
            }

            hotbar.add(button);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Redimensiona a imagem da logomarca
        ImageIcon originalLogo = new ImageIcon("src/main/resources/logotipo.png");
        Image scaledImage = originalLogo.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledLogo = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledLogo);

        // Adiciona o texto ao lado da logomarca
        JLabel textLabel = new JLabel("<html><b>VISTATECH ERP</b><br><i>Sistema de Gestão Oftalmológica</i></html>");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setForeground(new Color(59, 89, 182));

        logoPanel.add(logoLabel);
        logoPanel.add(textLabel);
        mainPanel.add(logoPanel);

        add(hotbar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Cria um botão com efeito hover para mudar sua cor.
     */
    private JButton createHoverButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.WHITE); // Cor de fundo padrão
        button.setForeground(new Color(59, 89, 182)); // Cor do texto
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(59, 89, 182), 2), // Borda fixa azul
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // Margens internas
        ));
        button.setOpaque(true);

        // Adiciona efeito de hover (somente altera a cor de fundo)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(230, 230, 250)); // Cor ao passar o mouse
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.WHITE); // Volta ao branco
            }
        });

        return button;
    }
    public static void estilizarMenuitens(JMenuItem item) {
        item.setBackground(new Color(255, 255, 255));
        item.setForeground(new Color(0, 0, 0));
        item.setFont(new Font("Arial", Font.BOLD, 13));
        item.setHorizontalTextPosition(SwingConstants.CENTER);
        item.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void main(String[] args) {
        MainScreenView mainScreen = new MainScreenView(false);
        mainScreen.setVisible(true);
    }
    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Verifica se a janela está maximizada
    private boolean isMaximized() {
        return (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
    }

    // Verifica se a janela está minimizada
    private boolean isMinimized() {
        return (getExtendedState() & JFrame.ICONIFIED) == JFrame.ICONIFIED;
    }
}

