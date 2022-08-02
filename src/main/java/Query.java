import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Runs queries against a back-end database
 */
public class Query {

    private final Connection conn;

    // Password hashing parameter constants
    private static final int HASH_STRENGTH = 65536;
    private static final int KEY_LENGTH = 128;

    private static final String GET_DESTINATIONS = "SELECT DISTINCT destination FROM Problems";
    private PreparedStatement getDestinationsStatement;

    private static final String GET_NAMES = "SELECT name FROM Problems";
    private PreparedStatement getNamesStatement;

    private static final String GET_PROBLEMS = "SELECT destination, area, subarea, boulder, name, grade, stars, description " +
            "FROM Problems WHERE destination = ? ORDER BY name";
    private PreparedStatement getProblemsStatement;

    // For creating new user
    private static final String NEW_USER = "INSERT INTO Users VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement newUserStatement;

    // For check dangling
    private static final String TRANCOUNT_SQL = "SELECT @@TRANCOUNT AS tran_count";
    private PreparedStatement tranCountStatement;

    public Query() throws SQLException, IOException {
        this(null, null, null, null);
    }

    protected Query(String serverURL, String dbName, String adminName, String password)
            throws SQLException, IOException {
        conn = serverURL == null ? openConnectionFromDbConn()
                : openConnectionFromCredential(serverURL, dbName, adminName, password);

        prepareStatements();
    }

    /**
     * Return a connecion by using dbconn.properties file
     *
     * @throws SQLException
     * @throws IOException
     */
    public static Connection openConnectionFromDbConn() throws SQLException, IOException {
        // Connect to the database with the provided connection configuration
        Properties configProps = new Properties();
        configProps.load(new FileInputStream("dbconn.properties"));
        String serverURL = configProps.getProperty("shepherd.server_url");
        String dbName = configProps.getProperty("shepherd.database_name");
        String adminName = configProps.getProperty("shepherd.username");
        String password = configProps.getProperty("shepherd.password");
        return openConnectionFromCredential(serverURL, dbName, adminName, password);
    }

    /**
     * Return a connecion by using the provided parameter.
     *
     * @param serverURL example: example.database.widows.net
     * @param dbName    database name
     * @param adminName username to login server
     * @param password  password to login server
     *
     * @throws SQLException
     */
    protected static Connection openConnectionFromCredential(String serverURL, String dbName,
                                                             String adminName, String password) throws SQLException {
        String connectionUrl =
                String.format("jdbc:sqlserver://%s:1433;databaseName=%s;user=%s;password=%s", serverURL,
                        dbName, adminName, password);
        Connection conn = DriverManager.getConnection(connectionUrl);

        // By default, automatically commit after each statement
        conn.setAutoCommit(true);

        // By default, set the transaction isolation level to serializable
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        return conn;
    }

    /**
     * Get underlying connection
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Closes the application-to-database connection
     */
    public void closeConnection() throws SQLException {
        conn.close();
    }

    /**
     * Gathers names of all boulders from database
     * @return A list of all boulder names
     */
    public List<String> getNames() throws SQLException {
        List<String> boulders = new ArrayList<>();
        ResultSet boulderResults = getNamesStatement.executeQuery();
        while (boulderResults.next()) {
            boulders.add(boulderResults.getString("name"));
        }
        boulderResults.close();
        return boulders;
    }

    /**
     * Gathers names of all unique climbing destinations
     * @return A list of all destination names
     */
    public List<String> getDestinations() throws SQLException {
        List<String> destinations = new ArrayList<>();
        ResultSet destResults = getDestinationsStatement.executeQuery();
        while (destResults.next()) {
            destinations.add(destResults.getString("destination"));
        }
        destResults.close();
        return destinations;
    }

    /**
     * Gathers all relevant boulder problem information
     * @param dest The destination to fetch problems from
     * @return A list of all boulder problems of type BoulderProblem
     */
    public List<BoulderProblem> getProblems(String dest) throws SQLException {
        List<BoulderProblem> problems = new ArrayList<>();
        getProblemsStatement.clearParameters();
        getProblemsStatement.setString(1, dest);
        ResultSet probResults = getProblemsStatement.executeQuery();
        while (probResults.next()) {
            String destination = probResults.getString("destination");
            String area = probResults.getString("area");
            String subarea = probResults.getString("subarea");
            String boulder = probResults.getString("boulder");
            String name = probResults.getString("name");
            String grade = probResults.getString("grade");
            int stars = probResults.getInt("stars");
            String description = probResults.getString("description");

            BoulderProblem problem = new BoulderProblem(destination, area, subarea, boulder, name, grade, stars, description);
            problems.add(problem);
        }
        probResults.close();
        return problems;
    }

    public void transaction_creatUser(String username, String first, String last, String email,
        String password, String flash, String proj, int feet, int inches, int ape, String gender) throws SQLException {
        try {
            byte[] salt = getSalt();
            byte[] hash = getHash(password, salt);

            // set up prepared statements to query db
            newUserStatement.clearParameters();
            newUserStatement.setString(1, username);
            newUserStatement.setString(2, first);
            newUserStatement.setString(3, last);
            newUserStatement.setString(4, email);
            newUserStatement.setBytes(5, salt);
            newUserStatement.setBytes(6, hash);
            newUserStatement.setString(7, flash);
            newUserStatement.setString(8, proj);
            newUserStatement.setInt(9, feet);
            newUserStatement.setInt(10, inches);
            newUserStatement.setInt(11, ape);
            newUserStatement.setString(12, gender);
            newUserStatement.execute();

        } finally {
            checkDanglingTransaction();
        }
    }

    /**
     * prepare all the SQL statements in this method.
     */
    private void prepareStatements() throws SQLException {
        getDestinationsStatement = conn.prepareStatement(GET_DESTINATIONS);
        getNamesStatement = conn.prepareStatement(GET_NAMES);
        getProblemsStatement = conn.prepareStatement(GET_PROBLEMS);
        newUserStatement = conn.prepareStatement(NEW_USER);
        tranCountStatement = conn.prepareStatement(TRANCOUNT_SQL);
    }

    private void resetLock() throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    /**
     * Throw IllegalStateException if transaction not completely complete, rollback.
     *
     */
    private void checkDanglingTransaction() {
        try {
            try (ResultSet rs = tranCountStatement.executeQuery()) {
                rs.next();
                int count = rs.getInt("tran_count");
                if (count > 0) {
                    throw new IllegalStateException(
                            "Transaction not fully commit/rollback. Number of transaction in process: " + count);
                }
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Database error", e);
        }
    }

    private byte[] getSalt() {
        // Generate a random cryptographic salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] getHash(String password, byte[] salt) {
        // Specify the hash parameters
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, HASH_STRENGTH, KEY_LENGTH);

        // Generate the hash
        SecretKeyFactory factory = null;
        byte[] hash = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
            return hash;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException();
        }
    }

    private static boolean isDeadLock(SQLException ex) {
        return ex.getErrorCode() == 1205;
    }

//    public static void main(String[] args) throws IOException, SQLException {
//        /* prepare the database connection stuff */
//        Query q = new Query();
//        System.out.println("made the connection");
//        q.closeConnection();
//    }
}
