import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    // Remover a variável 'driver' pois não é mais necessária
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
}
