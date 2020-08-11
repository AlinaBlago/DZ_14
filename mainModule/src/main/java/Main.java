import entities.Location;
import entities.Problems;
import entities.Routes;
import entities.Solution;
import jdbc.JDBCDataImpl;
import myClasses.Vertex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hometask 14\n----------------");

        JDBCDataImpl data = new JDBCDataImpl();
        ArrayList<Location> locations = data.getLocations();
        ArrayList<Routes> routes = data.getRoutes();
        Map<Integer, List<Routes>>  tempConnectionsPerStartIndex = routes.stream()
                .collect(Collectors.groupingBy(Routes::getStartIndex));
        ArrayList<Problems> problems = data.getProblems();
        ArrayList<Vertex> vertices = data.putLocationsIntoVertex(locations);
        data.putRoutes(routes, tempConnectionsPerStartIndex, vertices);
        ArrayList<Solution> solutions = data.getSolutions();
        data.getPass(problems, solutions, vertices);
        data.printResult(solutions);
        data.loadProperties();

        }
    }



