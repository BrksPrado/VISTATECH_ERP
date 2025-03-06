package org.example;

import org.example.DataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("Prado bonitão V1.0.1");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("logotipo.png")).getImage());

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Painel central para os campos e título
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;

        // Painel do título e logo
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titlePanel.setBackground(Color.WHITE);

        // Adiciona a logo no título
        ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("logotipo.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        titlePanel.add(logoLabel);

        // Título estilizado
        JLabel titleLabel = new JLabel("VistaTech ERP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(59, 89, 182));
        titlePanel.add(titleLabel);

        // Adiciona o painel do título ao painel central
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        centerPanel.add(titlePanel, constraints);

        // Campo "Usuário"
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel userLabel = new JLabel("Usuário:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(userLabel, constraints);

        constraints.gridx = 1;
        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(userField, constraints);

        // Campo "Senha"
        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(passwordField, constraints);

        // Botão "Login" estilizado
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 3;
        JButton loginButton = new JButton("Logar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(59, 89, 182));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Adiciona efeito 3D ao botão
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(79, 109, 202));
                loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(59, 89, 182));
            }
        });

        // Ação do botão de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passwordField.getPassword());

                if (!username.isEmpty() && !password.isEmpty()) {
                    new MainScreen();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Username inserido: " + username);
                    System.out.println("Password inserida: " + password);

                    System.out.println("Validador BD: " + DataBaseConnection.validateLogin(username, password));

                    // Validação no banco
                    if (!username.isEmpty() && !password.isEmpty()) {
                        if (DataBaseConnection.validateLogin(username, password)) {
                            JOptionPane.showMessageDialog(LoginScreen.this, "Login bem-sucedido!");
                            new MainScreen();
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(LoginScreen.this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(LoginScreen.this, "Por favor, preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }});

        centerPanel.add(loginButton, constraints);

        // Rodapé
        JLabel footerLabel = new JLabel("Em Desenvolvimento © 2025");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Adiciona painéis ao JFrame
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(footerLabel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
    // Metodo para validar login no banco de dados
    private boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = SHA2(?, 256)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}