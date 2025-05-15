package com.vistatech.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnectionConnection {
    // Remover a variável 'driver' pois não é mais necessária
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/my_application";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            // Carregar explicitamente o driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Exibe o erro caso o driver não seja encontrado
            e.printStackTrace();
        }

        // Retorna a conexão com o banco de dados
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

/*

'CREATE TABLE if not exists `produtos` (
        `pdt_id` int NOT NULL AUTO_INCREMENT,
  `pdt_nome` varchar(60) NOT NULL,
  `pdt_preco_custo` decimal(10,2) NOT NULL,
  `pdt_preco_venda` decimal(10,2) NOT NULL,
  `pdt_tipo` varchar(40) DEFAULT NULL,
    PRIMARY KEY (`pdt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci'

'CREATE TABLE if not exists `estoque` (
  `est_id` int NOT NULL AUTO_INCREMENT,
  `est_pdt_id` int NOT NULL,
  `est_quantidade` int NOT NULL,
  `oculto` tinyint(1) DEFAULT ''0'',
  PRIMARY KEY (`est_id`),
  KEY `est_pdt_id` (`est_pdt_id`),
  CONSTRAINT `estoque_ibfk_1` FOREIGN KEY (`est_pdt_id`) REFERENCES `produtos` (`pdt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci'

'CREATE TABLE `movimentacoes_estoque` (
  `mov_id` int NOT NULL AUTO_INCREMENT,
  `mov_est_id` int NOT NULL,
  `mov_tipo` enum(''ENTRADA'',''SAIDA'') NOT NULL,
  `mov_quantidade` int NOT NULL,
  `mov_data` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mov_editada` tinyint(1) DEFAULT ''0'',
  PRIMARY KEY (`mov_id`),
  KEY `mov_est_id` (`mov_est_id`),
  CONSTRAINT `movimentacoes_estoque_ibfk_1` FOREIGN KEY (`mov_est_id`) REFERENCES `estoque` (`est_id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci'



Como funciona o MOV_EDITADA: Ao cadastrar um produto pelo módulo de estoque, por padrão, a movimentação dele será gerada com o status "entrada normal", ou seja,
mov_editada = 0. Esse mesmo status será gerado caso seja reposto um item via módulo de estoque. Caso ocorra uma alteração manual na tela de cadastro, onde seja
retirado uma quantidade de itens, essa movimentação terá o status de mov_editada = 1 (sinalizando que foi uma edição).
    Quem for criar o módulo de vendas, para implementar uma tabela de movimentações de entrada e saída igual ou semelhante à do módulo de estoque, por favor, siga
a lógica explicada aqui, principalmente para desenvolver a movimentação de estoque de "saída normal" que só ocorrerá numa venda de produtos.
*/

}
