import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class financeiroScreen extends JFrame{

    public financeiroScreen() {
        // Configurações básicas da janela
        setTitle("VistaTech ERP - Financeiro");
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon logo = new ImageIcon("src/main/resources/vista_tech.png");
        setIconImage(logo.getImage());



        // Define um Layout básico para a janela
        setLayout(new BorderLayout());

        // Criação do painel superior para título
        JPanel listraAzul = new JPanel(new GridBagLayout());
        listraAzul.setBackground(new Color(59, 89, 182));
        listraAzul.setPreferredSize(new Dimension(listraAzul.getWidth(), 90));

        ImageIcon iconLogo = new ImageIcon("src/main/resources/logotipo.png");
        iconLogo = new ImageIcon(iconLogo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(iconLogo);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));


        JLabel vistaLabel = new JLabel("VistaTech ERP");
        vistaLabel.setForeground(new Color(255,255,255));
        vistaLabel.setFont(new Font("Arial Black", Font.PLAIN, 24));
        vistaLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));


        listraAzul.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        listraAzul.add(logoLabel);
        listraAzul.add(vistaLabel);


        // Painel central com mensagem de boas-vindas
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel financeiroLabel = new JLabel("Financeiro");
        financeiroLabel.setFont(new Font("Arial Black", Font.PLAIN, 18));
        centerPanel.add(financeiroLabel);

        JLabel infoLabel = new JLabel("Clique em uma opção para começar a navegar");
        infoLabel.setForeground(Color.BLACK);
        centerPanel.add(infoLabel);


        JButton btnEntrada = new JButton("Controle de Entrada");
        btnEntrada.setForeground(Color.WHITE);
        btnEntrada.setFont(new Font("Arial Black", Font.PLAIN, 28));
        btnEntrada.setBackground(new Color(59, 89, 182));

        JButton btnSaida = new JButton("Controle de Saída");
        btnSaida.setForeground(Color.WHITE);
        btnSaida.setFont(new Font("Arial Black", Font.PLAIN, 28));
        btnSaida.setBackground(new Color(59, 89, 182));

        JButton btnSaldo = new JButton("Ver Saldo");
        btnSaldo.setForeground(Color.WHITE);
        btnSaldo.setFont(new Font("Arial Black", Font.PLAIN, 28));
        btnSaldo.setBackground(new Color(59, 89, 182));

        Dimension tamanhoPadrao = new Dimension(400, 60); // Aumenta a largura dos botões
        btnEntrada.setPreferredSize(tamanhoPadrao);
        btnSaida.setPreferredSize(tamanhoPadrao);
        btnSaldo.setPreferredSize(tamanhoPadrao);

        btnEntrada.setMaximumSize(tamanhoPadrao);
        btnSaida.setMaximumSize(tamanhoPadrao);
        btnSaldo.setMaximumSize(tamanhoPadrao);

        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        financeiroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrada.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSaida.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 80)));
        centerPanel.add(btnEntrada);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(btnSaida);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(btnSaldo);

        // Adicionando MouseAdapter para mudar o cursor
        MouseAdapter cursorAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };

        btnEntrada.addMouseListener(cursorAdapter);
        btnSaida.addMouseListener(cursorAdapter);
        btnSaldo.addMouseListener(cursorAdapter);

        // Painel inferior com informações de copyright
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 245, 245));
        JLabel footerLabel = new JLabel("Em desenvolvimento © 2025");
        footerLabel.setForeground(new Color(155, 155, 155));
        footerPanel.add(footerLabel);

        // Adicionando painéis à janela principal nas posições adequadas
        add(listraAzul, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

    }


}
