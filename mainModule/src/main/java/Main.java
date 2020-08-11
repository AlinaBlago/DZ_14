import java.sql.*;

import static java.util.stream.Collectors.groupingBy;


public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hometask 14\n----------------");
        JDBSData.getLocations();
        JDBSData.getRoutes();
        JDBSData.getProblems();
        JDBSData.putLocationsIntoVertex();
        JDBSData.putRoutes();
        JDBSData.getPass();
        JDBSData.getSolutions();
        JDBSData.printResult();
        JDBSData.loadProperties();

        }
    }



