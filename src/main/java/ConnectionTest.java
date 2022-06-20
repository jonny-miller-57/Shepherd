import java.sql.*;

/**
 * Runs queries against a back-end database
 */
public class ConnectionTest {

    private Connection conn;

    // Password hashing parameter constants
    private static final int HASH_STRENGTH = 65536;
    private static final int KEY_LENGTH = 128;

    // For check dangling
    private static final String TRANCOUNT_SQL = "SELECT @@TRANCOUNT AS tran_count";
    private PreparedStatement tranCountStatement;

    public static final String DEFAULT_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String DEFAULT_URL = "jdbc:sqlserver://shepherd-server.database.windows.net:1433;database=squamish";
    private static final String DEFAULT_USERNAME = "jmill57";
    private static final String DEFAULT_PASSWORD = "Iloveclimbingv12!";

    private static final String GET_ALL = "SELECT * FROM Boulders";
    private static PreparedStatement getAllStatement;

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = ConnectionTest.getConnection(DEFAULT_DRIVER_CLASS, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
            getAllStatement = conn.prepareStatement(GET_ALL);
            ResultSet result = getAllStatement.executeQuery();
            while (result.next()) {
                System.out.println(result.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionTest.close(conn);
        }
    }

    public static Connection getConnection(String driverClass, String url, String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driverClass);
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
