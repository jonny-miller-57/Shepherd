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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    }
}