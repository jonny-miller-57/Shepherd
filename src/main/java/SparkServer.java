//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import utils.CORSFilter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SparkServer {
    public SparkServer() {
    }

    public static void main(String[] args) throws SQLException, IOException {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();

        Spark.get("/names", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                Query q;
                List<String> boulders = null;
                try {
                    q = new Query();
                    boulders = q.getNames();
                    q.closeConnection();
                } catch (SQLException | IOException e) {
                    System.out.println(e.getMessage());
                }
                Gson gson = new Gson();
                return gson.toJson(boulders);
            }
        });


        Spark.get("/destinations", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                Query q;
                List<String> destinations = null;
                try {
                    q = new Query();
                    destinations = q.getDestinations();
                    q.closeConnection();
                } catch (SQLException | IOException e) {
                    System.out.println(e.getMessage());
                }
                Gson gson = new Gson();
                return gson.toJson(destinations);
            }
        });

        Spark.get("/problems", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Query q;
                String destString = request.queryParams("dest");

                // checks for valid request
                if(destString == null || destString.equals("select")) {
                    // You can also have a message in "halt" that is displayed in the page.
                    Spark.halt(400, "must select a destination");
                }

                // attempts to fill request with a given query
                List<String> problemNames = new ArrayList<>();
                try {
                    q = new Query();
                    List<BoulderProblem> boulderProblems = q.getProblems(destString);
                    q.closeConnection();
                    for (BoulderProblem problem : boulderProblems) {
                        problemNames.add(problem.getName());
                    }
                } catch (SQLException | IOException e) {
                    System.out.println(e.getMessage());
                }
                Gson gson = new Gson();
                return gson.toJson(problemNames);
            }
        });

        Spark.post("/new-user", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Query q;
                String user = request.queryParams("user");
                String first = request.queryParams("first");
                String last = request.queryParams("last");
                String email = request.queryParams("email");
                String pass = request.queryParams("pass");
                String flash = request.queryParams("flash");
                String proj = request.queryParams("proj");
                String feet = request.queryParams("feet");
                String inches = request.queryParams("inches");
                String ape = request.queryParams("ape");
                String gender = request.queryParams("gender");

                // checks for valid request
                Map<String, String> errors = validateNewUser(user, first, last, email, pass, flash, proj, feet, inches, ape, gender);

                // halts on invalid requests
                if(!errors.isEmpty()) { // checking for null parameters
                    Spark.halt(400, "invalid request: " + errors);
                }

                try {
                    int intFeet = Integer.parseInt(feet);
                    int intInches = Integer.parseInt(inches);
                    int intApe = Integer.parseInt(ape);
                    q = new Query();
                    q.transaction_creatUser(user,first, last, email, pass, flash, proj, intFeet, intInches, intApe, gender);
                    q.closeConnection();

                } catch (SQLException | IOException | NumberFormatException e) {
                    Spark.halt(400, "something went wrong: " + e.getMessage());
                }
                Gson gson = new Gson();
                return gson.toJson("success");
            }
        });
    }

    // checks for null or empty strings. returns a hash map full of errors else an empty map
    private static Map<String, String> validateNewUser(String user, String first, String last, String email, String pass,
        String flash, String proj, String feet, String inches, String ape, String gender) {

        Map<String, String> errors = new HashMap<>();

        if (user == null || user.equals("")) {
            errors.put("user", "invalid username");
        }
        if (first == null || first.equals("")) {
            errors.put("first", "invalid first name");
        }
        if (last == null || last.equals("")) {
            errors.put("last", "invalid last name");
        }
        if (email == null || email.equals("")) {
            errors.put("email", "invalid email");
        }
        if (pass == null || pass.equals("")) {
            errors.put("pass", "invalid password");
        }
        if (flash == null || flash.equals("")) {
            errors.put("flash", "invalid flash grade");
        }
        if (proj == null || proj.equals("")) {
            errors.put("proj", "invalid project grade");
        }
        if (feet == null || feet.equals("")) {
            errors.put("feet", "invalid foot height");
        }
        if (inches == null || inches.equals("")) {
            errors.put("inches", "invalid inches height");
        }
        if (ape == null || ape.equals("")) {
            errors.put("ape", "invalid ape index");
        }
        if (inches == null || inches.equals("")) {
            errors.put("gender", "invalid gender entry");
        }

        return errors;
    }
}