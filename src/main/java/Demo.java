

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws SQLException, IOException {
//        List<BoulderProblem> problems = Parser.parseBoulderProblems("Squamish_Boulders.csv");
//        for (BoulderProblem problem : problems) {
//            System.out.println(problem.getName());
//        }

        Query q = new Query();
        List<BoulderProblem> boulders = q.getProblems("Squamish");
        q.closeConnection();
        for (BoulderProblem problem : boulders) {
            System.out.println(problem.getName());
        }
    }
}
