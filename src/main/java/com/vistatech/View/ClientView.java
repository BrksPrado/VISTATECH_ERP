package com.vistatech.View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.border.*;

import com.vistatech.Connection.DataBaseConnection;
import com.vistatech.Settings.MachineSettings;

public class ClientView extends JFrame {
    // Variáveis públicas declaradas no início da classe
    // Variáveis públicas compactadas
    public MachineSettings machine;
    public int altura, largura;
    public JPanel painelCabecalho, painelEsquerda, formulario, painelBotoes, lineOne, lineTwo, lineThree, lineFour, lineFive, lineSix, painelTabela;
    public JLabel lblLogo, lblTitulo;
    public JTextField txtID, txtNome, strCpfCnpj, strRg, strRua, strNumero, strBairro, strCidade, strEstado, strEmail;
    public JFormattedTextField txtNascimento, strCep, strCelular, strTelefone;
    public JComboBox<String> comboTipo;
    public JSeparator divisor, separator;
    public JButton btnAdicionar, btnRemover, btnAlterar, btnLimpar;
    public GridBagConstraints gbc;
    public DefaultTableModel modelo;
    public JTable tabela;
    public DefaultTableCellRenderer centralizado;
    public JScrollPane listagemClientes;

    public ClientView(){
        super("Cadastro de clientes");
        machine = new MachineSettings();
        altura = machine.getResolutiony(0.69);
        largura = machine.getResolutionx(0.71);
        setSize(largura, altura);
        //setSize(1366,768);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("logotipo.png")).getImage());
        componentsFormulario();

        painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCabecalho.setBackground(new Color(59, 89, 182));
        painelCabecalho.setPreferredSize(new Dimension(800, 70));
        add(painelCabecalho, BorderLayout.NORTH); // Adiciona o painel ao topo do JFrame

        lblLogo = new JLabel(redimensionarImagem("src/main/resources/whitelogo.png", 60, 60));
        lblTitulo = new JLabel("VistaTech ERP");

        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelCabecalho.add(lblLogo);
        painelCabecalho.add(lblTitulo);

        carregarClientes();
    }

    public static void main(String[] args) {
        ClientView clientScreen = new ClientView();
        clientScreen.setVisible(true);
    }

    public ImageIcon redimensionarImagem(String caminho, int largura, int altura) {
        ImageIcon icon = new ImageIcon(caminho);
        Image imagem = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(imagem);
    }

    public void componentsFormulario(){
        initLookAndFeel();

        painelEsquerda = new JPanel();
        painelEsquerda.setLayout(new FlowLayout(FlowLayout.LEFT));
        painelEsquerda.setBackground(new Color(230, 230, 230));
        painelEsquerda.setPreferredSize(new Dimension(600, 500));
        add(painelEsquerda,BorderLayout.WEST);

        formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(new Color(230, 230, 230));
        formulario.setPreferredSize(new Dimension(590, 400));
        formulario.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.gray, 0, false),
                        "Dados do cliente",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.ABOVE_TOP),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        ));
        painelEsquerda.add(formulario);

        painelBotoes = new JPanel(new GridBagLayout());
        painelBotoes.setBackground(new Color(230, 230, 230));
        painelBotoes.setPreferredSize(new Dimension(590, 200));

        painelEsquerda.add(painelBotoes);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LINHA UM DO FORMULARIO
        gbc.gridx=0;gbc.gridy=0;
        lineOne = new JPanel(new GridBagLayout());
        lineOne.setPreferredSize(new Dimension(550, 30));
        lineOne.setBackground(new Color(230, 230, 230));
        formulario.add(lineOne,gbc);
        //ID

        gbc.weightx = 0;
        gbc.gridx=0;gbc.gridy=0;
        lineOne.add(new JLabel("ID:"),gbc);
        gbc.weightx = 0.1;
        txtID = new JTextField(4);
        txtID.setPreferredSize(new Dimension(5, 20));
        txtID.setEditable(false);
        gbc.gridx=1;
        lineOne.add(txtID,gbc);

        //NOME
        gbc.weightx = 0;
        gbc.gridx=2;
        lineOne.add(new JLabel("NOME:"),gbc);
        gbc.weightx = 1;
        txtNome = new JTextField(20);
        gbc.gridx=3;
        lineOne.add(txtNome,gbc);

        //NASCIMENTO
        gbc.weightx = 0;
        gbc.gridx=4;
        lineOne.add(new JLabel("NASCIMENTO:"),gbc);
        gbc.weightx = 0.25;
        try{
            txtNascimento = new JFormattedTextField(new MaskFormatter("##/##/####"));
            txtNascimento.setColumns(6);
        }catch (Exception e){
            txtNascimento = new JFormattedTextField();
        }
        gbc.gridx=5;
        lineOne.add(txtNascimento,gbc);
        // LINHA DOIS DO FORMULARIO
        gbc.gridx=0;gbc.gridy=1;
        lineTwo = new JPanel(new GridBagLayout());
        lineTwo.setPreferredSize(new Dimension(550, 30));
        lineTwo.setBackground(new Color(230, 230, 230));
        formulario.add(lineTwo,gbc);


        gbc.gridy=0;
        //tipo
        gbc.weightx = 0; // Faz o campo expandir
        lineTwo.add(new JLabel("TIPO:"),gbc);
        String[] tipos = {"PF", "PJ"};
        comboTipo = new JComboBox<>(tipos);
        comboTipo.setPreferredSize(new Dimension(50, 20));
        gbc.gridx=1;
        lineTwo.add(comboTipo,gbc);

        //CPF/CNPJ
        gbc.gridx=2;
        lineTwo.add(new JLabel("CPF/CNPJ:"),gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // Faz o campo expandir
        strCpfCnpj = new JTextField(20);
        gbc.gridx=3;
        lineTwo.add(strCpfCnpj,gbc);

        //RG
        gbc.weightx = 0;
        gbc.gridx=4;
        lineTwo.add(new JLabel("RG:"),gbc);
        gbc.weightx = 1;
        strRg = new JTextField(20);
        gbc.gridx=5;
        lineTwo.add(strRg,gbc);

        // DIVISORIA
        gbc.gridx=0;gbc.gridy=2;
        divisor = new JSeparator(JSeparator.HORIZONTAL);
        divisor.setPreferredSize(new Dimension(550, 10));
        formulario.add(divisor,gbc);

        // LINHA TRES DO FORMULARIO
        gbc.gridx=0;gbc.gridy=3;
        lineThree = new JPanel(new GridBagLayout());
        lineThree.setPreferredSize(new Dimension(550, 30));
        lineThree.setBackground(new Color(230, 230, 230));
        formulario.add(lineThree,gbc);


        gbc.gridy=0;
        //ENDERECO
        gbc.weightx = 0; // Faz o campo expandir
        lineThree.add(new JLabel("RUA:"),gbc);
        gbc.weightx = 1; // Faz o campo expandir
        strRua = new JTextField(20);
        gbc.gridx=1;
        lineThree.add(strRua,gbc);

        //NUMERO
        gbc.weightx = 0;
        gbc.gridx=2;
        lineThree.add(new JLabel("NUMERO:"),gbc);
        gbc.weightx = 0.1;
        strNumero = new JTextField(20);
        gbc.gridx=3;
        lineThree.add(strNumero,gbc);

        //BAIRRO
        gbc.weightx = 0;
        gbc.gridx=4;
        lineThree.add(new JLabel("BAIRRO:"),gbc);
        gbc.weightx = 1;
        strBairro = new JTextField(20);
        gbc.gridx=5;
        lineThree.add(strBairro,gbc);


        // LINHA QUATRO DO FORMULARIO
        gbc.gridx=0;gbc.gridy=4;
        lineFour = new JPanel(new GridBagLayout());
        lineFour.setPreferredSize(new Dimension(550, 30));
        lineFour.setBackground(new Color(230, 230, 230));
        formulario.add(lineFour,gbc);
        //CIDADE
        gbc.weightx = 0; // Faz o campo expandir
        lineFour.add(new JLabel("CIDADE:"),gbc);
        gbc.weightx = 1; // Faz o campo expandir
        strCidade = new JTextField(20);
        gbc.gridx=1;
        lineFour.add(strCidade,gbc);

        //ESTADO
        gbc.weightx = 0;
        gbc.gridx=2;
        lineFour.add(new JLabel("ESTADO:"),gbc);
        gbc.weightx = 0.5;
        strEstado = new JTextField(20);
        gbc.gridx=3;
        lineFour.add(strEstado,gbc);

        //CEP
        gbc.weightx = 0;
        gbc.gridx=4;
        lineFour.add(new JLabel("CEP:"),gbc);
        gbc.weightx = 0.5;
        try{
            strCep = new JFormattedTextField(new MaskFormatter("#####-###"));
            strCep.setColumns(6);
        }catch (Exception e){
            strCep = new JFormattedTextField();
        }
        gbc.gridx=5;
        lineFour.add(strCep,gbc);

        // DIVISORIA
        gbc.gridy = 5;
        gbc.gridx = 0;
        separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(550, 10));
        formulario.add(separator, gbc);


        // LINHA CINCO DO FORMULARIO
        gbc.gridx=0;gbc.gridy=6;
        lineFive = new JPanel(new GridBagLayout());
        lineFive.setPreferredSize(new Dimension(550, 30));
        lineFive.setBackground(new Color(230, 230, 230));
        formulario.add(lineFive,gbc);
        //CELULAR
        gbc.weightx = 0; // Faz o campo expandir
        lineFive.add(new JLabel("CELULAR:"),gbc);
        gbc.weightx = 0.4; // Faz o campo expandir
        try{
            strCelular = new JFormattedTextField(new MaskFormatter("(##) #####-####"));
            strCelular.setColumns(11);
        }catch (Exception e){
            strCelular = new JFormattedTextField();
        }
        gbc.gridx=1;
        lineFive.add(strCelular,gbc);

        //EMAIL
        gbc.weightx = 0;
        gbc.gridx=2;
        lineFive.add(new JLabel("EMAIL:"),gbc);
        gbc.weightx = 1;
        strEmail = new JTextField(20);
        gbc.gridx=3;
        lineFive.add(strEmail,gbc);

        // LINHA SEIX DO FORMULARIO
        gbc.gridx=0;gbc.gridy=7;
        lineSix = new JPanel(new GridBagLayout());
        lineSix.setPreferredSize(new Dimension(550, 30));
        lineSix.setBackground(new Color(230, 230, 230));
        formulario.add(lineSix,gbc);

        //TELEFONE
        gbc.weightx = 0; // Faz o campo expandir
        lineSix.add(new JLabel("TELEFONE:"),gbc);
        gbc.weightx = 0.3; // Faz o campo expandir
        try{
            strTelefone = new JFormattedTextField(new MaskFormatter("(##) #####-####"));
            strTelefone.setColumns(11);
        }catch (Exception e){
            strTelefone = new JFormattedTextField();
        }
        gbc.gridx=1;
        lineSix.add(strTelefone,gbc);

        gbc.gridy=0;
        gbc.gridx=0;
        btnAdicionar = new JButton("Adicionar");
        styleButton(btnAdicionar);
        painelBotoes.add(btnAdicionar,gbc);
        btnAdicionar.addActionListener(e -> adicionarCliente());

        gbc.gridx=1;
        btnRemover = new JButton("Remover");
        styleButton(btnRemover);
        painelBotoes.add(btnRemover,gbc);
        btnRemover.addActionListener(e -> removerCliente());

        gbc.gridy=1;
        gbc.gridx=0;
        btnAlterar = new JButton("Alterar");
        styleButton(btnAlterar);
        painelBotoes.add(btnAlterar,gbc);
        btnAlterar.addActionListener(e -> alterarCliente());

        gbc.gridy=1;
        gbc.gridx=1;
        btnLimpar = new JButton("Limpar");
        styleButton(btnLimpar);
        painelBotoes.add(btnLimpar,gbc);
        btnLimpar.addActionListener(e -> limparFormulario());

        painelTabela = new JPanel(new BorderLayout());
        modelo = new ModeloTabela();
        tabela = new JTable(modelo);
        centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }

        tabela.setRowSorter(new TableRowSorter<>(modelo));
        listagemClientes = new JScrollPane(tabela);
        listagemClientes.setBackground(new Color(230, 230, 230));
        listagemClientes.setPreferredSize(new Dimension(750, 280));
        listagemClientes.setBorder(BorderFactory.createTitledBorder("Dados do usuário"));

        add(listagemClientes, BorderLayout.EAST);
    }

    private void removerCliente() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela para remover!");
            return;
        }

        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        String nomeCliente = (String) tabela.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente remover o cliente:\n" +
                        "ID: " + id + "\n" +
                        "Nome: " + nomeCliente + "?",
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Connection conn = DataBaseConnection.getConnection()) {
                String sql = "DELETE FROM clients WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
                        carregarClientes();
                    } else {
                        JOptionPane.showMessageDialog(null, "Cliente não encontrado!");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erro ao remover cliente!\n" +
                                "Possível motivo: " + ex.getMessage());
            }
        }
    }

    private void adicionarCliente() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "INSERT INTO clients (nome, tipo, cpf_cnpj, rg, nascimento, rua, numero, bairro, cidade, estado, cep, celular, telefone, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Validação dos campos obrigatórios
                if (txtNome.getText().isEmpty() || strCpfCnpj.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nome e CPF/CNPJ são campos obrigatórios!");
                    return;
                }

                // Formata a data de nascimento (de DD/MM/YYYY para YYYY-MM-DD)
                String dataNascimento = null;
                if (!txtNascimento.getText().isEmpty()) {
                    String[] partesData = txtNascimento.getText().split("/");
                    if (partesData.length == 3) {
                        dataNascimento = partesData[2] + "-" + partesData[1] + "-" + partesData[0];
                    }
                }

                // Preenche os parâmetros
                stmt.setString(1, txtNome.getText());
                stmt.setString(2, comboTipo.getSelectedItem().toString());
                stmt.setString(3, strCpfCnpj.getText().replaceAll("[^0-9]", ""));
                stmt.setString(4, strRg.getText());
                stmt.setString(5, dataNascimento);
                stmt.setString(6, strRua.getText());
                stmt.setString(7, strNumero.getText());
                stmt.setString(8, strBairro.getText());
                stmt.setString(9, strCidade.getText());
                stmt.setString(10, strEstado.getText());
                stmt.setString(11, strCep.getText().replaceAll("[^0-9]", ""));
                stmt.setString(12, strCelular.getText().replaceAll("[^0-9]", ""));
                stmt.setString(13, strTelefone.getText().replaceAll("[^0-9]", ""));
                stmt.setString(14, strEmail.getText());

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
                carregarClientes(); // Método para atualizar a tabela
                limparFormulario(); // Método para limpar os campos
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente: " + ex.getMessage());
        }
    }

    private void alterarCliente() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela para alterar!");
            return;
        }

        int id = (int) tabela.getValueAt(linhaSelecionada, 0);
        String nomeAtual = (String) tabela.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente alterar o cliente:\nID: " + id + "\nNome: " + nomeAtual + "?",
                "Confirmação de Alteração",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Connection conn = DataBaseConnection.getConnection()) {
                String sql = "UPDATE clients SET " +
                        "nome = ?, tipo = ?, cpf_cnpj = ?, rg = ?, nascimento = ?, " +
                        "rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, " +
                        "cep = ?, celular = ?, telefone = ?, email = ? " +
                        "WHERE id = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    // Validação dos campos obrigatórios
                    if (txtNome.getText().isEmpty() || strCpfCnpj.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nome e CPF/CNPJ são campos obrigatórios!");
                        return;
                    }

                    // Formatação da data
                    java.sql.Date dataNascimentoSql = null;
                    // Formata a data de nascimento (de DD/MM/YYYY para YYYY-MM-DD)
                    String dataNascimento = null;
                    if (!txtNascimento.getText().isEmpty()) {
                        String[] partesData = txtNascimento.getText().split("/");
                        if (partesData.length == 3) {
                            dataNascimento = partesData[2] + "-" + partesData[1] + "-" + partesData[0];
                        }
                    }

                    // Preenche os parâmetros
                    stmt.setString(1, txtNome.getText());
                    stmt.setString(2, comboTipo.getSelectedItem().toString());
                    stmt.setString(3, strCpfCnpj.getText().replaceAll("[^0-9]", ""));
                    stmt.setString(4, strRg.getText());
                    stmt.setDate(5, dataNascimentoSql);
                    stmt.setString(6, strRua.getText());
                    stmt.setString(7, strNumero.getText());
                    stmt.setString(8, strBairro.getText());
                    stmt.setString(9, strCidade.getText());
                    stmt.setString(10, strEstado.getText());
                    stmt.setString(11, strCep.getText().replaceAll("[^0-9]", ""));
                    stmt.setString(12, strCelular.getText().replaceAll("[^0-9]", ""));
                    stmt.setString(13, strTelefone.getText().replaceAll("[^0-9]", ""));
                    stmt.setString(14, strEmail.getText());
                    stmt.setInt(15, id);

                    int linhasAfetadas = stmt.executeUpdate();

                    if (linhasAfetadas > 0) {
                        JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso!");
                        carregarClientes();
                        limparFormulario();
                    } else {
                        JOptionPane.showMessageDialog(null, "Nenhum cliente foi alterado. Verifique se o ID ainda existe.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erro ao alterar cliente!\n" +
                                "Motivo: " + ex.getMessage());
            }
        }
    }

    private void limparFormulario() {
        txtID.setText("");
        txtNome.setText("");
        comboTipo.setSelectedIndex(0); // Seleciona o primeiro item (PF)
        strCpfCnpj.setText("");
        strRg.setText("");
        txtNascimento.setText("");
        strRua.setText("");
        strNumero.setText("");
        strBairro.setText("");
        strCidade.setText("");
        strEstado.setText("");
        strCep.setText("");
        strCelular.setText("");
        strTelefone.setText("");
        strEmail.setText("");

        // Opcional: Colocar foco no primeiro campo
        txtNome.requestFocusInWindow();
    }

    private void carregarClientes() {
        modelo.setRowCount(0); // Limpa tabela
        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "SELECT id, nome, cpf_cnpj, cidade, estado, DATE_FORMAT(nascimento, '%d/%m/%Y') as nascimento_formatado FROM clients";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    // Formata CPF/CNPJ baseado no tamanho
                    String cpfCnpj = rs.getString("cpf_cnpj");
                    String documentoFormatado = "";

                    if (cpfCnpj != null) {
                        if (cpfCnpj.length() == 11) { // CPF
                            documentoFormatado = cpfCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
                        } else if (cpfCnpj.length() == 14) { // CNPJ
                            documentoFormatado = cpfCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
                        } else {
                            documentoFormatado = cpfCnpj; // Mantém como está se não for CPF nem CNPJ
                        }
                    }

                    Object[] row = {
                            rs.getInt("id"),
                            rs.getString("nome"),
                            documentoFormatado,
                            rs.getString("cidade"),
                            rs.getString("estado"),
                            rs.getString("nascimento_formatado") // Já vem formatado como DD/MM/YYYY
                    };
                    modelo.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar clientes: " + ex.getMessage());
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(300, 80));

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

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ModeloTabela extends DefaultTableModel {
        public ModeloTabela() {
            super(new String[]{"Id", "Nome", "Cpf/Cnpj", "Cidade", "Estado"}, 0);
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
}