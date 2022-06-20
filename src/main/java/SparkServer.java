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

public class SparkServer {
    public SparkServer() {
    }

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        Spark.get("/hello", new Route() {
            public Object handle(Request request, Response response) {
                return "Sup G";
            }
        });

        Spark.get("/work", new Route() {
            public Object handle(Request request, Response response) {
                return "It's working!";
            }
        });
    }
}