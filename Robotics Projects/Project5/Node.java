import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.geometry.Point2D;

public class Node {
    public int id;
    public Point2D position;
    public ArrayList<Node> neighbors;


    public List<Edge> connections;
    // public ArrayList<Node> neighbors;

    public Node(int id, Point2D point){
        this.id = id;
        connections = new ArrayList<>();
        neighbors = new ArrayList<>();
        position = point;

    }

    public void addEdge(Edge edge){
        this.connections.add(edge);
        this.neighbors.add(edge.end);
    }

    public void addEdges( ArrayList<Edge> edges){
        this.connections.addAll(edges);
        for(Edge edge: edges){
            this.neighbors.add(edge.end);
        }
    }

    public void addNeighbor(Node neighbor, int weight){
        Edge connection = new Edge(this, neighbor, weight);
        this.addEdge(connection);
        this.neighbors.add(neighbor);
    }

    public Point2D getPosition() { return position; }
}
