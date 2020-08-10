import entities.Location;
import entities.Routes;
import entities.Problems;
import entities.Solution;
import minDistanceAlgorithm.Algorithm;
import myClasses.Edge;
import myClasses.Vertex;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.groupingBy;


public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hometask 14\n----------------");

        Properties props = loadProperties();

        String url = props.getProperty("url");

        try (Connection connection = DriverManager.getConnection(url, props)) {

            ArrayList<Location> locationArrayList = new ArrayList<Location>();
            try(PreparedStatement pst = connection.prepareStatement("SELECT * FROM locations;"))
            {
            ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    locationArrayList.add(
                            new Location(
                                    rs.getString(2)
                            )
                    );
                }
            }

            ArrayList<Routes> routesArrayList = new ArrayList<Routes>();
            try(PreparedStatement pst = connection.prepareStatement("SELECT * FROM routes;"))
            {
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    routesArrayList.add(
                            new Routes(
                                    rs.getInt(2) - 1,
                                    rs.getInt(3) ,
                                    rs.getInt(4)
                            )
                    );
                }
            }

            ArrayList<Problems> problemsArrayList = new ArrayList<Problems>();
            try(PreparedStatement pst = connection.prepareStatement("SELECT * FROM problems;"))
            {
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    problemsArrayList.add(
                            new Problems(
                                    rs.getInt(2),
                                    rs.getInt(3)
                            )
                    );
                }
            }

            ArrayList<Vertex> vertexArrayList = new ArrayList<Vertex>();
            for (Location location: locationArrayList) {

                vertexArrayList.add(
                        new Vertex(
                                location.name));
            }

            Map<Integer, List<Routes>> tempConnectionsPerStartIndex = routesArrayList.stream()
                    .collect(groupingBy(Routes::getStartIndex));


            for (Map.Entry<Integer, List<Routes>> entry : tempConnectionsPerStartIndex.entrySet()) {

                int currentStartIndex = entry.getKey();
                List<Routes> tmpConns = entry.getValue();
                Edge[] currentCityEdges = new Edge[tmpConns.size()];

                for (int i = 0 ; i < tmpConns.size() ; i++){
                    currentCityEdges[i] = new Edge(vertexArrayList.get(routesArrayList.get(i).t_id) , tmpConns.get(i).cost);
                }

                if(currentStartIndex == 4){
                    System.out.println("start");
                    for(Routes item : entry.getValue()){
                        System.out.println(item.f_id + " " + item.t_id + "\n");
                    }
                    System.out.println("end");
                    System.out.println(vertexArrayList.size() + "size");
                    System.out.println(vertexArrayList);
                    System.out.println(vertexArrayList.get(3));
                }
                vertexArrayList.get(currentStartIndex).adjacencies = currentCityEdges;
            }


            ArrayList<Solution> solutions = new ArrayList<Solution>();

            for (int i = 0 ; i < problemsArrayList.size(); i++){

                int x = i;

                Vertex cityFrom = null;
                Vertex cityTo = null;

                for(int j = 0 ; j < vertexArrayList.size();j++){
                    if(j == problemsArrayList.get(x).from_id - 1){
                        cityFrom = vertexArrayList.get(j);
                        break;
                    }
                }
                for(int j = 0 ; j < vertexArrayList.size();j++){
                    if(j == problemsArrayList.get(x).to_id -1 ){
                        cityTo = vertexArrayList.get(j);
                        break;
                    }
                }
                Algorithm.computePaths(cityFrom);


                Solution sol = new Solution(0,0);
                sol.id = i;
                sol.cost = (int)cityTo.minDistance;
                solutions.add(sol);


            }


            try (PreparedStatement insertSolution = connection.prepareStatement(
                    "INSERT INTO solutions (problem_id , cost) VALUES (?,?) ON CONFLICT DO NOTHING;",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                for (Solution solution : solutions) {
                    System.out.println("Solution id: " + solution.id + ", cost: " + solution.cost);
                    insertSolution.setInt(1, solution.id+1);
                    insertSolution.setInt(2,solution.cost);

                    insertSolution.addBatch();
                }
                insertSolution.executeBatch();

                ResultSet generatedKeys = null;
                generatedKeys = insertSolution.getGeneratedKeys();

            }


        }
    }


    private static Properties loadProperties () {

        Properties props = new Properties();

        try (InputStream input = Main.class.getResourceAsStream("/jdbs.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return props;
    }
}