import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShepherdApp {

    private static String LOGGED_IN_AS;
    /**
     * Establishes an application-to-database connection and runs the Flights
     * application REPL
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, SQLException {
        /* prepare the database connection stuff */
        Query q = new Query();
        homeMenu(q);
        q.closeConnection();
    }

    /**
     * Shepherd application for the specified
     * application-to-database connection
     *
     * @param q Query class that executes the appropriate SQL queries
     * @throws IOException
     */
    private static void homeMenu(Query q) throws IOException, SQLException {
        String response;
        while (true) {
            // print the command options
            System.out.println();
            System.out.println(" *** Please enter one of the following commands *** ");
            System.out.println("> create (to create a new user)");
            System.out.println("> login (to login as existing user)");
            System.out.println("> quit (to quit the app)");

            // read an input command
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("> ");
            String command = reader.readLine();

            // navigate to create menu
            if (command.equals("create")) {
                response = createMenu(q, reader);
            }

            // navigate to login menu
            else if (command.equals("login")) {
                response = loginMenu(q, reader);
            }

            // quit the app
            else if (command.equals("quit")) {
                response = "Goodbye\n";
            }

            else {
                response = "invalid command, try again...";
            }

            System.out.print(response);
            if (response.equals("Goodbye\n")) {
                break;
            }
        }
    }

    private static String createMenu(Query q, BufferedReader reader) throws IOException, SQLException {
        String response;
        while (true) {
            // prompts user for new profile info
            System.out.println("*** Please enter on one line, seperated by a single space, the following attributes (in this order):");
            System.out.println("> first name");
            System.out.println("> last name");
            System.out.println("> email address");
            System.out.println("> username");
            System.out.println("> password");
            System.out.println("> flash grade - the grade that you often most flash");
            System.out.println("> project grade - the upper end of your projecting limit");
            System.out.println("> height in feet");
            System.out.println("> height in inches");
            System.out.println("> ape index");
            System.out.println("> gender");
            System.out.println("> ... or enter 'q' to quit and return to the home menu.");

            // reads user input
            String attributes = reader.readLine();
            String[] tokens = tokenize(attributes);
            if (tokens[0].equals("q") && tokens.length == 1) {
                response = "\nWelcome back to the home menu...\n";
                break;
            } else if (tokens.length == 11) {
                String firstName = tokens[0];
                String lastName = tokens[1];
                String email = tokens[2];
                String username = tokens[3];
                String password = tokens[4];
                String flashGrade = tokens[5];
                String projGrade = tokens[6];
                int heightFeet = Integer.parseInt(tokens[7]);
                int heightInches = Integer.parseInt(tokens[8]);
                int apeIndex = Integer.parseInt(tokens[9]);
                String gender = tokens[10];

                response = q.transaction_creatUser(username, firstName, lastName, email, password, flashGrade, projGrade, heightFeet, heightInches, apeIndex, gender);
                break;
            } else {
                System.out.println("You entered the wrong number of attributes for your new profile.");
            }
        }
        return response;
    }

    private static String loginMenu(Query q, BufferedReader reader) throws IOException, SQLException {
        String response = "\nWelcome back to the home menu...\n";
        while (true) {
            // prompts user for login credentials
            System.out.println("\n*** Please enter your username and password on one line separated by a single space or 'q' to return to home menu:");
            // reads user input
            String attributes = reader.readLine();
            String[] tokens = tokenize(attributes);
            if (tokens[0].equals("q") && tokens.length == 1) {
                return response;
            } else if (tokens.length == 2) {
                String username = tokens[0];
                String password = tokens[1];

                String attempt = q.transaction_loginUser(username, password);
                if (attempt.equals("success")) {
                    LOGGED_IN_AS = username;
                    System.out.println("\nWelcome " + username + ", it's good to have you back.\n");
                    break;
                }
            } else {
                System.out.println("You entered invalid login credentials. To create new user, return to home menu.");
            }
        }

        userHomePage(q, reader);

        return response;
    }

    private static void userHomePage(Query q, BufferedReader reader) throws IOException, SQLException {
        String destination;
        int partySize;

        while (true) {
            System.out.println("\n*** Enter where you are planning on climbing. Your options are:");

            // print a list of destinations for user to select from
            List<String> destinations = q.getDestinations();
            for (String dest : destinations) {
                System.out.println(dest);
            }
            destination = reader.readLine();
            if (destinations.contains(destination)) { // if a valid destination
                ArrayList<Profile> party = getParty(q, reader);
//                for (Profile member : party) {
//                    System.out.println(member.getUsername());
//                }
                String size = party.size() == 1 ? "" : "" + party.size();
                System.out.println("Would you " + size + " like to have a project day or a volume day?");
                System.out.println("Enter 'project' for project day or 'volume' for a high volume day.");
                String dayType = "";
                while (true) {
                    dayType = reader.readLine();
                    try {
                        if (!dayType.equals("project") && !dayType.equals("volume")) {
                            throw new IOException("Please enter 'volume' or 'project'");
                        } else {
                            break;
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
            } else {
                System.out.println("...sorry we do not have data for that destination yet. Make sure you typed in the correct spelling (case sensitive)");
            }
        }


    }

    private static ArrayList<Profile> getParty(Query q, BufferedReader reader) throws IOException {
        int partySize;
        while (true) {
            System.out.println("*** Enter number of climbers in your party (enter '1' for a solo trip): ");
            String[] input = tokenize(reader.readLine());
            try {
                partySize = Integer.parseInt(input[0]);
                break;
            } catch (NumberFormatException e) {
                System.out.println("you must provide an integer for the size of your party");
            }
        }
        return getPartyData(partySize, q,reader);
    }

    private static ArrayList<Profile> getPartyData(int partySize, Query q, BufferedReader reader) throws IOException {
        ArrayList<Profile> party = new ArrayList<>(partySize);
        try {
            party.add(q.getProfile(LOGGED_IN_AS));
        } catch (SQLException e) {
            System.out.println("There was a problem connecting the database, please restart the app and try again...");
        }

        if (partySize > 1) {
            while (true) {
                System.out.println("*** " + LOGGED_IN_AS + ", enter the usernames of your other party members seperated by a single space.");
                String[] input = tokenize(reader.readLine());
                try {
                    for (String user : input) {
                        Profile profile = q.getProfile(user);
                        party.add(profile);
                    }
                    if (party.size() != partySize) {
                        throw new SQLException();
                    }
                    break;
                } catch (SQLException e) {
                    System.out.println("you must provide the correct number of valid usernames...");
                }
            }
        }
        return party;
    }

    /**
     * Tokenize a string into a string array
     */
    private static String[] tokenize(String command) {
        String regex = "\"([^\"]*)\"|(\\S+)";
        Matcher m = Pattern.compile(regex).matcher(command);
        List<String> tokens = new ArrayList<>();
        while (m.find()) {
            if (m.group(1) != null)
                tokens.add(m.group(1));
            else
                tokens.add(m.group(2));
        }
        return tokens.toArray(new String[0]);
    }
}
