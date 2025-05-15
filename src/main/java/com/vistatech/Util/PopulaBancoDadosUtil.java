package com.vistatech.Util;

import com.vistatech.Model.CadProdutoModel;
import com.vistatech.GettersSetters.ProdutoEstoque;

public class PopulaBancoDadosUtil {

    public static void main(String[] args) {
        CadProdutoModel CadProdutoModel = new CadProdutoModel();

        // Lista de produtos fictícios para uma ótica
        String[] nomes = {
                "Óculos de Grau Masculino", "Óculos de Grau Feminino", "Óculos de Sol Unissex",
                "Lentes de Contato Diárias", "Armação de Titânio", "Colírio Hidratante",
                "Estojo para Óculos", "Limpa-lentes Premium", "Adaptador para Óculos VR",
                "Óculos de Grau Infantil", "Óculos de Sol Esportivo", "Lentes de Contato Mensais",
                "Armação de Acetato", "Colírio Antialérgico", "Estojo de Couro",
                "Limpa-lentes com Spray", "Cordão para Óculos", "Óculos de Leitura",
                "Óculos de Sol Polarizado", "Lentes de Contato Coloridas", "Armação de Metal",
                "Colírio Lubrificante", "Estojo Rígido", "Limpa-lentes em Lenço",
                "Clip para Óculos de Sol", "Óculos de Grau com Proteção UV", "Óculos de Sol Estiloso",
                "Lentes de Contato Tóricas", "Armação de Madeira", "Colírio para Olhos Secos",
                "Estojo de Silicone", "Limpa-lentes com Microfibra", "Capa para Óculos",
                "Óculos de Grau com Blue Cut", "Óculos de Sol Retro", "Lentes de Contato Multifocais",
                "Armação de Fibra de Carbono", "Colírio para Vermelhidão", "Estojo Transparente",
                "Limpa-lentes com Álcool Isopropílico", "Suporte para Óculos", "Óculos de Grau Elegante",
                "Óculos de Sol Aviador", "Lentes de Contato Rígidas", "Armação de Prata",
                "Colírio para Lentes de Contato", "Estojo com Espelho", "Limpa-lentes em Gel",
                "Cordão Elástico para Óculos", "Óculos de Grau com Design Moderno"
        };

        double[] precosCusto = {
                120.00, 130.49, 80.99, 50.50, 200.00, 15.99, 25.49, 10.50, 30.99,
                100.00, 90.49, 60.99, 150.50, 20.99, 35.49, 12.50, 8.99, 70.00,
                110.49, 55.99, 180.50, 18.99, 40.49, 9.50, 5.99, 140.00, 95.49,
                65.99, 220.50, 22.99, 45.49, 11.50, 7.99, 160.00, 85.49, 75.99,
                240.50, 25.99, 50.49, 13.50, 6.99, 170.00, 105.49, 80.99, 260.50,
                28.99, 55.49, 14.50, 4.99, 190.00
        };

        double[] precosVenda = {
                250.00, 270.49, 150.99, 100.50, 400.00, 30.99, 50.49, 20.50, 60.99,
                200.00, 180.49, 120.99, 300.50, 40.99, 70.49, 25.50, 15.99, 140.00,
                220.49, 110.99, 360.50, 35.99, 80.49, 18.50, 10.99, 280.00, 190.49,
                130.99, 440.50, 45.99, 90.49, 22.50, 14.99, 320.00, 170.49, 150.99,
                480.50, 50.99, 100.49, 26.50, 12.99, 340.00, 210.49, 160.99, 520.50,
                55.99, 110.49, 28.50, 8.99, 380.00
        };

        int[] quantidades = {
                30, 25, 40, 100, 15, 50, 60, 80, 20,
                35, 45, 90, 10, 70, 55, 85, 95, 65,
                40, 75, 12, 60, 50, 90, 100, 20, 30,
                80, 8, 55, 45, 70, 95, 25, 35, 65,
                10, 50, 40, 85, 100, 15, 30, 60, 5,
                45, 35, 75, 90, 20
        };

        // Array de tipos correspondentes aos produtos
        String[] tipos = {
                "Óculos de Grau", "Óculos de Grau", "Óculos de Sol", "Lentes de Contato", "Armação", "Colírio",
                "Estojo", "Limpa-lentes", "Acessório", "Óculos de Grau", "Óculos de Sol", "Lentes de Contato",
                "Armação", "Colírio", "Estojo", "Limpa-lentes", "Acessório", "Óculos de Leitura",
                "Óculos de Sol", "Lentes de Contato", "Armação", "Colírio", "Estojo", "Limpa-lentes",
                "Acessório", "Óculos de Grau", "Óculos de Sol", "Lentes de Contato", "Armação", "Colírio",
                "Estojo", "Limpa-lentes", "Acessório", "Óculos de Grau", "Óculos de Sol", "Lentes de Contato",
                "Armação", "Colírio", "Estojo", "Limpa-lentes", "Acessório", "Óculos de Grau", "Óculos de Sol",
                "Lentes de Contato", "Armação", "Colírio", "Estojo", "Limpa-lentes", "Acessório", "Óculos de Grau"
        };

        // For loop para inserir os produtos no banco de dados
        for (int i = 0; i < nomes.length; i++) {
            ProdutoEstoque produtoEstoque = new ProdutoEstoque(
                    0, // Auto Increment
                    nomes[i],
                    precosCusto[i],
                    precosVenda[i],
                    quantidades[i],
                    tipos[i] // Novo campo
            );
            CadProdutoModel.adicionarProduto(produtoEstoque);
        }

        System.out.println("50 produtos inseridos com sucesso!");
    }


}