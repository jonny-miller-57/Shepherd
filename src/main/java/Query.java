import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.*;

/**
 * Runs queries against a back-end database
 */
public class Query {

    // login status
    private boolean LOGGED_IN = false;

    // username associated with session
    private Profile LOGGED_IN_AS;

    private final Connection conn;

    // Password hashing parameter constants
    private static final int HASH_STRENGTH = 65536;
    private static final int KEY_LENGTH = 128;

    private static final String GET_DESTINATIONS = "SELECT DISTINCT destination FROM Problems";
    private PreparedStatement getDestinationsStatement;

    private static final String GET_USER_DATA = "SELECT username, firstName, lastName, flashGrade, projectGrade, heightFeet, heightInches, apeIndex, gender FROM Users WHERE username = ?";
    private PreparedStatement getUserDataStatement;

    private static final String GET_NAMES = "SELECT name FROM Problems";
    private PreparedStatement getNamesStatement;

    private static final String GET_PROBLEMS = "SELECT destination, area, subarea, boulder, name, grade, stars, description " +
            "FROM Problems WHERE destination = ? ORDER BY name";
    private PreparedStatement getProblemsStatement;

    private static final String LOGIN_ATTEMPT = "SELECT * FROM Users WHERE username = ?";
    private PreparedStatement loginAttemptStatement;

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

    /**
     * Gathers all relevant user information
     * @param username The username to fetch associated user data from
     * @return A profile of the username
     */
    public Profile getProfile(String username) throws SQLException {
        Profile profile = new Profile();
        getUserDataStatement.clearParameters();
        getUserDataStatement.setString(1, username);
        ResultSet profileResults = getUserDataStatement.executeQuery();
        while (profileResults.next()) {
            String user = profileResults.getString("username");
            String first = profileResults.getString("firstName");
            String last = profileResults.getString("lastName");
            String flash = profileResults.getString("flashGrade");
            String proj = profileResults.getString("projectGrade");
            int feet = profileResults.getInt("heightFeet");
            int inches = profileResults.getInt("heightInches");
            int ape = profileResults.getInt("apeIndex");
            String gender = profileResults.getString("gender");
            profile = new Profile(user, first, last, flash, proj, feet, inches, ape, gender);
        }
        return profile;
    }


    public String transaction_creatUser(String username, String first, String last, String email,
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
            return "Thanks " + first + " and Welcome! You will be known around here as " + username + ".\n";
        } catch (SQLException e) {
            return "Failed to create user \n" + e.getMessage();
        } finally {
            checkDanglingTransaction();
        }
    }

    public String transaction_loginUser(String username, String password) throws SQLException {
        if (LOGGED_IN) {
            return "You're already logged in as " + LOGGED_IN_AS.getUsername() + "\n";
        }
        Profile p;
        try {
            loginAttemptStatement.clearParameters();
            loginAttemptStatement.setString(1, username);
            ResultSet loginResults = loginAttemptStatement.executeQuery();
            loginResults.next();

            byte[] salt = loginResults.getBytes("saltedPass");
            byte[] hash = loginResults.getBytes("hashedPass");
            byte[] inputHash = getHash(password, salt);

            if (!Arrays.equals(hash, inputHash)) { // if passwords do not match
                loginResults.close();
                return "invalid password";
            } else { // password matches
                String user = loginResults.getString("username");
                String first = loginResults.getString("firstName");
                String last = loginResults.getString("lastName");
                String gender = loginResults.getString("gender");
                String flashGrade = loginResults.getString("flashGrade");
                String projGrade = loginResults.getString("projectGrade");
                int heightFeet = loginResults.getInt("heightFeet");
                int heightInches = loginResults.getInt("heightInches");
                int ape = loginResults.getInt("apeIndex");
                p = new Profile(user, first, last, flashGrade, projGrade, heightFeet, heightInches, ape, gender);
                LOGGED_IN = true;
                LOGGED_IN_AS = p;
                return "success";
            }
        } catch (SQLException e) {
            return "unknown username: " + username;
        } finally {
            checkDanglingTransaction();
        }
    }

    public Profile getLOGGED_IN_AS() {
        return LOGGED_IN_AS;
    }

    public void logout() {
        this.LOGGED_IN = false;
        this.LOGGED_IN_AS = null;
    }

    /**
     * prepare all the SQL statements in this method.
     */
    private void prepareStatements() throws SQLException {
        getDestinationsStatement = conn.prepareStatement(GET_DESTINATIONS);
        getNamesStatement = conn.prepareStatement(GET_NAMES);
        getProblemsStatement = conn.prepareStatement(GET_PROBLEMS);
        getUserDataStatement = conn.prepareStatement(GET_USER_DATA);
        loginAttemptStatement = conn.prepareStatement(LOGIN_ATTEMPT);
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
//        q.transaction_creatUser("test", "test", "mctester", "test@test.com", "tester", "0", "3", "5", "0", "0", "Male");
//        q.closeConnection();
//    }
}
