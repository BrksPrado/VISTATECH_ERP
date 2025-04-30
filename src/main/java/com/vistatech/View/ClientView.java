package com.vistatech.View;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import javax.swing.border.*;

import com.vistatech.Settings.MachineSettings;

public class ClientView extends JFrame {

    public ClientView(){
        super("Cadastro de clientes");
        MachineSettings machine = new MachineSettings();
        int altura = machine.getResolutiony(0.69);
        int largura = machine.getResolutionx(0.71);
//        setSize(largura, altura);
        setSize(1000,768);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("logotipo.png")).getImage());
        componentsFormulario();

        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCabecalho.setBackground(new Color(59, 89, 182));
        painelCabecalho.setPreferredSize(new Dimension(800, 70));
        add(painelCabecalho, BorderLayout.NORTH); // Adiciona o painel ao topo do JFrame

        JLabel lblLogo = new JLabel(redimensionarImagem("src/main/resources/whitelogo.png", 60, 60));
        JLabel lblTitulo = new JLabel("VistaTech ERP");

        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelCabecalho.add(lblLogo);
        painelCabecalho.add(lblTitulo);
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

    private void componentsFormulario(){
        initLookAndFeel();

        JPanel Painel = new JPanel();
        Painel.setLayout(new FlowLayout(FlowLayout.LEFT));
        Painel.setBackground(new Color(235, 235, 235));
        Painel.setPreferredSize(new Dimension(1000, 760));
        add(Painel,BorderLayout.WEST);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(new Color(230, 230, 230));
        formulario.setPreferredSize(new Dimension(975, 500));
        formulario.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.gray, 0, false),
                        "Dados do cliente",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.ABOVE_TOP),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        ));
        Painel.add(formulario);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LINHA UM DO FORMULARIO
        gbc.gridx=0;gbc.gridy=0;
        JPanel lineOne = new JPanel(new GridBagLayout());
        lineOne.setPreferredSize(new Dimension(520, 30));
        lineOne.setBackground(new Color(230, 230, 230));
        formulario.add(lineOne,gbc);

            //ID
            gbc.weightx = 0;
            gbc.gridx=0;gbc.gridy=0;
            lineOne.add(new JLabel("ID:"),gbc);
            gbc.weightx = 0.05;
            JTextField txtID = new JTextField(4);
            txtID.setPreferredSize(new Dimension(5, 20));
            txtID.setEditable(false);
            txtID.setBackground(new Color(210, 210, 210));
            gbc.gridx=1;
            lineOne.add(txtID,gbc);

            //Pesquisar
            gbc.weightx = 0;
            gbc.gridx = 2;
            JButton btnPesquisar = new JButton();
            btnPesquisar.setPreferredSize( new Dimension(15, 20));
            ImageIcon icon = new ImageIcon("src/main/resources/icons/search.png");
             // Redimensionar imagem para caber dentro do botão
            Image img = icon.getImage().getScaledInstance(
                    btnPesquisar.getPreferredSize().width, // Largura do botão
                    btnPesquisar.getPreferredSize().height, // Altura do botão
                    Image.SCALE_SMOOTH);
            btnPesquisar.setIcon(new ImageIcon(img)); // Define o ícone redimensionado
            lineOne.add(btnPesquisar,gbc);



            //tipo
            gbc.gridx = 3;
            gbc.weightx = 0; // Faz o campo expandir
            lineOne.add(new JLabel("Tipo:"),gbc);
            String[] tipos = {"Física", "Jurídica"};
            gbc.weightx = 0.1; // Faz o campo expandir
            JComboBox<String> comboTipo = new JComboBox<>(tipos);
            comboTipo.setPreferredSize(new Dimension(20, 20));
            gbc.gridx=4;
            lineOne.add(comboTipo,gbc);


            //NOME
            gbc.weightx = 0;
            gbc.gridx=5;
            lineOne.add(new JLabel("Nome:"),gbc);
            gbc.weightx = 1;
            JTextField txtNome = new JTextField(5);
            txtNome.setPreferredSize(new Dimension(5, 20));
            gbc.gridx=6;
            lineOne.add(txtNome,gbc);

            //DT_CADASTRO
            gbc.weightx = 0;
            gbc.gridx=7;
            lineOne.add(new JLabel("DT Cadastro:"),gbc);
            JFormattedTextField txtdtCadastro;
            gbc.weightx = 0.17;
            try{
                txtdtCadastro = new JFormattedTextField(new MaskFormatter("##/##/####"));
                txtdtCadastro.setEditable(false);
                txtdtCadastro.setBackground(new Color(230, 230, 230));
                txtdtCadastro.setColumns(6);
            }catch (Exception e){
                txtdtCadastro = new JFormattedTextField();
            }
            gbc.gridx=8;
            lineOne.add(txtdtCadastro,gbc);

        // LINHA DOIS DO FORMULARIO
        gbc.gridx=0;gbc.gridy=1;
        JPanel lineTwo = new JPanel(new GridBagLayout());
        lineTwo.setPreferredSize(new Dimension(550, 30));
        lineTwo.setBackground(new Color(230, 230, 230));
        formulario.add(lineTwo,gbc);

            //CPF_CNPJ
            gbc.gridy=0;
            gbc.gridx=0;
            gbc.weightx = 0;
            lineTwo.add(new JLabel("CPF/CNPJ:"),gbc);
            gbc.weightx =0.1; // Faz o campo expandir
            JTextField strCpfCnpj = new JTextField(20);
            gbc.gridx=1;
            lineTwo.add(strCpfCnpj,gbc);

            //RG
            gbc.weightx = 0;
            gbc.gridx=2;
            lineTwo.add(new JLabel("RG/IE:"),gbc);
            gbc.weightx = 0.1;
            JTextField strRg = new JTextField(20);
            gbc.gridx=3;
            lineTwo.add(strRg,gbc);

            //EMAIL
            gbc.weightx = 0;
            gbc.gridx=4;
            lineTwo.add(new JLabel("E-mail:"),gbc);
            gbc.weightx = 0.2;
            JTextField strEmail = new JTextField(20);
            gbc.gridx=5;
            lineTwo.add(strEmail,gbc);

            //DT_CADASTRO
            gbc.weightx = 0;
            gbc.gridx=6;
            lineTwo.add(new JLabel("DT Nascimento:"),gbc);
            JFormattedTextField txtDtNascimento;
            gbc.weightx = 0.05;
            try{
                txtDtNascimento = new JFormattedTextField(new MaskFormatter("##/##/####"));
                txtDtNascimento.setColumns(6);
            }catch (Exception e){
                txtDtNascimento = new JFormattedTextField();
            }
            gbc.gridx=7;
            lineTwo.add(txtDtNascimento,gbc);

    }
    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
