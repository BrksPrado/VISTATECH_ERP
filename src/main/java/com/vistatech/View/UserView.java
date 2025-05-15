package com.vistatech.View;

import com.vistatech.Connection.DataBaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class UserView extends JFrame {

    public DefaultTableModel modelo;
    public JTable tabela;
    public JLabel lblLogo;
    public JTextField txtNome, txtEmail;
    public JPasswordField pswSenha, pswSenhaconfirm;
    public JCheckBox cbValidadmin, cbValidvendedor;

    public UserView() {
        // Definindo configurações da Janela
        super("Cadastro de usuário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        Components();
        carregarUsuarios();
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("logotipo.png")).getImage());
    }

    private void Components() {
        initLookAndFeel();
        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCabecalho.setBackground(new Color(59, 89, 182));
        painelCabecalho.setPreferredSize(new Dimension(800, 70));
        add(painelCabecalho, BorderLayout.NORTH); // Adiciona o painel ao topo do JFrame

        lblLogo = new JLabel(redimensionarImagem("src/main/resources/whitelogo.png", 60, 60));
        JLabel lblTitulo = new JLabel("VistaTech ERP");

        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelCabecalho.add(lblLogo);
        painelCabecalho.add(lblTitulo);

        JPanel painelItens = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelItens.setBackground(new Color(230, 230, 230));
        painelItens.setPreferredSize(new Dimension(330, 5));
        add(painelItens, BorderLayout.WEST);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(new Color(230, 230, 230));
        painelFormulario.setPreferredSize(new Dimension(320, 280));
        painelFormulario.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.gray, 0, false),
                        "Dados do usuário",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.ABOVE_TOP),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        ));
        painelItens.add(painelFormulario, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(15);
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelFormulario.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        pswSenha = new JPasswordField(15);
        painelFormulario.add(pswSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        painelFormulario.add(new JLabel("Confirme a senha:"), gbc);
        gbc.gridx = 1;
        pswSenhaconfirm = new JPasswordField(15);
        painelFormulario.add(pswSenhaconfirm, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        painelFormulario.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        painelFormulario.add(new JLabel("Admin:"), gbc);
        gbc.gridx = 1;
        cbValidadmin = new JCheckBox();
        cbValidadmin.setBackground(new Color(230, 230, 230));
        painelFormulario.add(cbValidadmin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        painelFormulario.add(new JLabel("Vendedor:"), gbc);
        gbc.gridx = 1;
        cbValidvendedor = new JCheckBox();
        cbValidvendedor.setBackground(new Color(230, 230, 230));
        painelFormulario.add(cbValidvendedor, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.setBackground(new Color(230, 230, 230));
        painelBotoes.setPreferredSize(new Dimension(320, 100));
        painelItens.add(painelBotoes, BorderLayout.SOUTH);

        JButton btnAdicionar = new JButton("Adicionar");
        styleButton(btnAdicionar);
        painelBotoes.add(btnAdicionar);
        btnAdicionar.addActionListener(e -> adicionarUsuario());

        JButton btnAlterar = new JButton("Alterar");
        styleButton(btnAlterar);
        painelBotoes.add(btnAlterar);
        btnAlterar.addActionListener(e -> alterarUsuario());

        JButton btnRemover = new JButton("Remover");
        styleButton(btnRemover);
        painelBotoes.add(btnRemover);
        btnRemover.addActionListener(e -> removerUsuario());

        JButton btnLimpar = new JButton("Limpar");
        styleButton(btnLimpar);
        painelBotoes.add(btnLimpar);
        btnLimpar.addActionListener(e -> limparFormulario());

        JPanel painelTabela = new JPanel(new BorderLayout());
        modelo = new ModeloTabela(); // Tabela personalizada
        tabela = new JTable(modelo);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }

        tabela.setRowSorter(new TableRowSorter<>(modelo));
        JScrollPane listagemProdutos = new JScrollPane(tabela);
        listagemProdutos.setBackground(new Color(230, 230, 230));
        listagemProdutos.setPreferredSize(new Dimension(450, 280));
        listagemProdutos.setBorder(BorderFactory.createTitledBorder("Dados do usuário"));

        add(listagemProdutos, BorderLayout.EAST);
    }

    private void adicionarUsuario() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password, email, admin, vendedor) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                String nome = txtNome.getText();
                String senha = new String(pswSenha.getPassword());
                String senhaConfirm = new String(pswSenhaconfirm.getPassword());
                String email = txtEmail.getText();
                int admin = cbValidadmin.isSelected() ? 1 : 0;
                int vendedor = cbValidvendedor.isSelected() ? 1 : 0;

                if (nome.isEmpty() || senha.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                    return;
                }

                if (!senha.equals(senhaConfirm)) {
                    JOptionPane.showMessageDialog(null, "As senhas não coincidem!");
                    return;
                }
                if(!email.contains("@")){
                    JOptionPane.showMessageDialog(null, "Insira um email valido!");
                    return;
                }

                stmt.setString(1, nome);
                stmt.setString(2, senha);
                stmt.setString(3, email);
                stmt.setInt(4, admin);
                stmt.setInt(5, vendedor);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!");
                carregarUsuarios();
                limparFormulario();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar usuário!");
        }
    }

    private void alterarUsuario() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário para alterar!");
            return;
        }


        int id = (int) tabela.getValueAt(selectedRow, 0);

        int respostaRemovcao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente alterar o usuário de ID: " + id + "??",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (respostaRemovcao == JOptionPane.YES_OPTION) {
            try (Connection conn = DataBaseConnection.getConnection()) {
                String sql = "UPDATE users SET username = ?, password = ?, email = ?, admin = ?, vendedor = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    if (txtNome.getText().isEmpty() || new String(pswSenha.getPassword()).isEmpty() || txtEmail.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                        return;
                    }

                    stmt.setString(1, txtNome.getText());
                    stmt.setString(2, new String(pswSenha.getPassword()));
                    stmt.setString(3, txtEmail.getText());
                    stmt.setInt(4, cbValidadmin.isSelected() ? 1 : 0);
                    stmt.setInt(5, cbValidvendedor.isSelected() ? 1 : 0);
                    stmt.setInt(6, id);

                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Usuário alterado com sucesso!");
                    carregarUsuarios();
                    limparFormulario();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao alterar usuário!");
            }
        }

    }

    private void removerUsuario() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário para remover!");
            return;
        }

        int id = (int) tabela.getValueAt(selectedRow, 0);
        int respostaRemovcao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente remover o usário de ID: " + id + "??",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (respostaRemovcao == JOptionPane.YES_OPTION) {
            try (Connection conn = DataBaseConnection.getConnection()) {
                String sql = "DELETE FROM users WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!");
                    carregarUsuarios();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao remover usuário!");
            }
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        pswSenha.setText("");
        pswSenhaconfirm.setText("");
        txtEmail.setText("");
        cbValidadmin.setSelected(false);
        cbValidvendedor.setSelected(false);
    }

    private void carregarUsuarios() {
        modelo.setRowCount(0); // Limpa tabela
        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getInt("admin") == 1 ? "Sim" : "Não",
                            rs.getInt("vendedor") == 1 ? "Sim" : "Não"
                    };
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar usuários!");
        }
    }

    private ImageIcon redimensionarImagem(String caminho, int largura, int altura) {
        ImageIcon icon = new ImageIcon(caminho);
        Image imagem = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(imagem);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(59, 89, 182));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(59, 89, 182));
                button.setForeground(Color.WHITE);
            }
        });
    }

    class ModeloTabela extends DefaultTableModel {
        public ModeloTabela() {
            super(new String[]{"Id", "Nome", "Email", "Admin", "Vendedor"}, 0);
        }

        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Integer.class;
                case 1:
                case 2:
                case 3:
                case 4:
                    return String.class;
                default:
                    return Object.class;
            }
        }
    }

    public static void main(String[] args) {
        UserView userScreen = new UserView();
        userScreen.setVisible(true);
    }
    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}