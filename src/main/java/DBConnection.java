import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Dotenv dotenv = Dotenv.load();
            String URL = dotenv.get("DB_URL");
            String USER = dotenv.get("DB_USER");
            String PASSWORD = dotenv.get("DB_PASSWORD");

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found on classpath", e);
            }

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        }
        return connection;
    }
    public static void closeConnection(){
        try{
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
