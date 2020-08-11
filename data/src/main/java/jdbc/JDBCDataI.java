package jdbc;

import entities.Location;
import entities.Problems;
import entities.Routes;
import entities.Solution;
import myClasses.Vertex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public interface JDBCDataI {
    Properties props = loadProperties();
    String url = props.getProperty("url");

    ArrayList<Location> getLocations() throws SQLException;

    ArrayList<Routes> getRoutes() throws SQLException;

    ArrayList<Problems> getProblems() throws SQLException;

    ArrayList<Vertex> putLocationsIntoVertex(ArrayList<Location> locationArrayList);

    ArrayList<Routes> putRoutes(ArrayList<Routes> routesArrayList, Map<Integer,
            List<Routes>> tempConnectionsPerStartIndex, ArrayList<Vertex> vertexArrayList);
    ArrayList<Solution> getSolutions() throws SQLException;
    void getPass(ArrayList<Problems> problemsArrayList, ArrayList solutions,
                 ArrayList<Vertex> vertexArrayList);
    void printResult(ArrayList<Solution> solutions);

    static Properties loadProperties() {
        return null;
    }

}
