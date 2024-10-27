import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/hostel?useSSL=false&serverTimezone=UTC";
        String user = "root";  // Your MySQL username
        String password = "Saravana@3128";  // Your MySQL password

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
}

