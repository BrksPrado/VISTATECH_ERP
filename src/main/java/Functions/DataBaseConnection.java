package Functions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//-- Criar o banco de dados 'my_application' caso ele ainda não exista
//CREATE DATABASE IF NOT EXISTS my_application;

//-- Utilizar o banco de dados 'my_application'
//USE my_application;

//-- Criar a tabela 'users' apenas se ela ainda não existir
//CREATE TABLE IF NOT EXISTS users (
//    -- ID único e auto-incremental, usado como chave primária
//    id INT AUTO_INCREMENT PRIMARY KEY,

//    -- Nome de usuário, obrigatório e com limite de 50 caracteres
//    username VARCHAR(50) NOT NULL,

//    -- Senha do usuário, opcional, armazenada de forma case-sensitive
//    password VARCHAR(255) NULL COLLATE utf8mb4_bin,

//    -- Indica se o usuário é administrador: 0 = não, 1 = sim (valor padrão é 0)
//    admin TINYINT(1) DEFAULT 0,

//    -- Endereço de e-mail do usuário, pode ser nulo
//    email VARCHAR(255) NULL,

//    -- Indica se o usuário é vendedor: 0 = não, 1 = sim (valor padrão é 0)
//    vendedor TINYINT(1) DEFAULT 0
//);

//-- Garantir que o campo 'username' não pode ser utilizado por mais de um usuário
//ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (username);

public class DataBaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/estoque_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

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

    public static boolean validateLogin(String name, String password){
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Exibir os valores enviados para depuração
            // System.out.println("Validando login com os seguintes valores:");
            // System.out.println("Username: " + name);
            // System.out.println("Password: " + password);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Registro encontrado no banco:");
                    System.out.println("Username: " + resultSet.getString("username"));
                    System.out.println("Password: " + resultSet.getString("password"));
                    return true;
                } else {
                    System.out.println("Nenhum registro encontrado no banco para os valores fornecidos.");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validAdmin(String name, String password){
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND admin = 1";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

}}
