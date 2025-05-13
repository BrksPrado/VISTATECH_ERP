package CadCliente;


import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;

import Functions.MachineSettings;

public class ClientView extends JFrame {

    public ClientView(){
        super("Cadastro de clientes");
        MachineSettings machine = new MachineSettings();
        int altura = machine.getResolutiony(0.69);
        int largura = machine.getResolutionx(0.71);
        setSize(largura, altura);
        //setSize(1366,768);
        setLocationRelativeTo(null);
        setResizable(true);
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
        //initLookAndFeel();

        JPanel painelEsquerda = new JPanel();
        painelEsquerda.setLayout(new FlowLayout(FlowLayout.LEFT));
        painelEsquerda.setBackground(new Color(230, 230, 230));
        painelEsquerda.setPreferredSize(new Dimension(600, 500));
        add(painelEsquerda,BorderLayout.WEST);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(new Color(230, 230, 230));
        formulario.setPreferredSize(new Dimension(590, 500));
        formulario.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.gray, 0, false),
                        "Dados do cliente",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.ABOVE_TOP),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        ));
        painelEsquerda.add(formulario);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LINHA UM DO FORMULARIO
        gbc.gridx=0;gbc.gridy=0;
        JPanel lineOne = new JPanel(new GridBagLayout());
        lineOne.setPreferredSize(new Dimension(550, 30));
        lineOne.setBackground(new Color(217, 217, 217));
        formulario.add(lineOne,gbc);
        //ID

            gbc.gridx=0;gbc.gridy=0;
            lineOne.add(new JLabel("ID:"),gbc);
            JTextField txtID = new JTextField(2);
            txtID.setEditable(false);
            gbc.gridx=1;
            lineOne.add(txtID,gbc);

            //NOME
            gbc.gridx=2;
            lineOne.add(new JLabel("NOME:"),gbc);
            JTextField txtNome = new JTextField(20);
            gbc.gridx=3;
            lineOne.add(txtNome,gbc);

            //NASCIMENTO
            gbc.gridx=4;
            lineOne.add(new JLabel("NASCIMENTO:"),gbc);
            JFormattedTextField txtNascimento;
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
        JPanel lineTwo = new JPanel(new GridBagLayout());
        lineTwo.setPreferredSize(new Dimension(550, 30));
        lineTwo.setBackground(new Color(217, 217, 217));
        formulario.add(lineTwo,gbc);


            gbc.gridy=0;
            //tipo
            gbc.weightx = 0; // Faz o campo expandir
            lineTwo.add(new JLabel("TIPO:"),gbc);
            String[] tipos = {"PF", "PJ"};
            JComboBox<String> comboTipo = new JComboBox<>(tipos);
            comboTipo.setPreferredSize(new Dimension(50, 20));
            gbc.gridx=1;
            lineTwo.add(comboTipo,gbc);

            //CPF/CNPJ
            gbc.gridx=2;
            lineTwo.add(new JLabel("CPF/CNPJ:"),gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1; // Faz o campo expandir
            JTextField strCpfCnpj = new JTextField(20);
            gbc.gridx=3;
            lineTwo.add(strCpfCnpj,gbc);

            //RG
            gbc.weightx = 0;
            gbc.gridx=4;
            lineTwo.add(new JLabel("RG:"),gbc);
            gbc.weightx = 1;
            JTextField strRg = new JTextField(20);
            gbc.gridx=5;
            lineTwo.add(strRg,gbc);



    }
    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
