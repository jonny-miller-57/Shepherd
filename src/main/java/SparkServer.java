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
import java.sql.SQLException;
import java.util.*;

public class SparkServer {
    public SparkServer() {
    }

    public static void main(String[] args) throws SQLException, IOException {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();

        Spark.get("/names", (request, response) -> {
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
        });


        Spark.get("/destinations", (request, response) -> {
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
        });

        Spark.get("/problems", (request, response) -> {
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
        });

        Spark.post("/test", (request, response) -> "OK");
    }

    // checks for null or empty strings. returns a hash map full of errors else an empty map
    private static Map<String, String> validateNewUser(String user, String first, String last, String email, String pass,
        String flash, String proj, String feet, String inches, String ape, String gender) {

        Map<String, String> errors = new HashMap<>();

        if (user == null || user.equals("")) {
            errors.put("user", "invalid username: " + (user == null ? "null" : "empty"));
        }
        if (first == null || first.equals("")) {
            errors.put("first", "invalid first name: " + (first == null ? "null" : "empty"));
        }
        if (last == null || last.equals("")) {
            errors.put("last", "invalid last name: " + (last == null ? "null" : "empty"));
        }
        if (email == null || email.equals("")) {
            errors.put("email", "invalid email: " + (email == null ? "null" : "empty"));
        }
        if (pass == null || pass.equals("")) {
            errors.put("pass", "invalid password: " + (pass == null ? "null" : "empty"));
        }
        if (flash == null || flash.equals("")) {
            errors.put("flash", "invalid flash grade: " + (flash == null ? "null" : "empty"));
        }
        if (proj == null || proj.equals("")) {
            errors.put("proj", "invalid project grade: " + (proj == null ? "null" : "empty"));
        }
        if (feet == null || feet.equals("")) {
            errors.put("feet", "invalid foot height: " + (feet == null ? "null" : "empty"));
        }
        if (inches == null || inches.equals("")) {
            errors.put("inches", "invalid inches height: " + (inches == null ? "null" : "empty"));
        }
        if (ape == null || ape.equals("")) {
            errors.put("ape", "invalid ape index: " + (ape == null ? "null" : "empty"));
        }
        if (inches == null || inches.equals("")) {
            errors.put("gender", "invalid gender entry: " + (gender == null ? "null" : "empty"));
        }

        return errors;
    }
}