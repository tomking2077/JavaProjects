import java.util.*;

public class Graph {
    ArrayList<Node> nodes;
    Node source;
    ArrayList<Node> previous;
    ArrayList<Integer> distance;

    Graph(){
        this.nodes = new ArrayList<>();
        this.previous = new ArrayList<>();
        this.distance = new ArrayList<>();
    }

    public void addNodes( List<Node> nodes){
        this.nodes.addAll(nodes);
    }

    public void Djikstra(Node A) {
        /*PriorityQueue<Node> minHeap = new PriorityQueue();
        minHeap.addAll(nodes);
        */

        //Setting previous and distance arrays to null
        for (int i = 0; i < nodes.size(); i++) {
            distance.add(Integer.MAX_VALUE);
            previous.add(null);
        }

        //0 corresponds to source
        distance.set(A.id, 0);

        ArrayList<Node> unvisited = new ArrayList<>();
        unvisited = nodes;

        System.out.println("Sizes----  Unvisited: " + unvisited.size() + " Distance: " + distance.size() + " Previous: " + previous.size() );

		int count = 0;
        //while (!unvisited.isEmpty()) {
        for(int i = 0; i < nodes.size()-1; i++){
            Node current = unvisited.get(findMinIndex(distance, unvisited));
			//System.out.print
            System.out.println("Visiting: " + current.id);
            //System.out.println("Node removed: " + current.id);
            unvisited.set(current.id, null);

            for (Edge edge : current.connections) {
                if (unvisited.get(edge.end.id) != null) {
                    int newDistance = distance.get(current.id) + edge.weight;
                    if (newDistance < distance.get(edge.end.id)) {
                        distance.set(edge.end.id, newDistance);
                        previous.set(edge.end.id, current);
                        System.out.println("Setting distance[" + edge.end.id + "] = " + newDistance + "    previous[" + edge.end.id + "] = " + current.id  );
                    }
                }
            }
            System.out.println("After traversing neighbors");
        }
    }

    public void printGraph(){
        for(Node element : nodes){
            System.out.println("Adjacency List for node " + element.id);
            System.out.print("Head");
            for(Edge edge: element.connections){
                System.out.print(" -> " + edge.end.id);
            }

            System.out.print("\n\n");
        }
    }

    int findMinIndex(ArrayList<Integer> alist, ArrayList<Node> legal){
        int index = -1;
        int min = 9999;
        for(int i = 0; i < alist.size(); i++){
            if(legal.get(i) != null) {
                if (alist.get(i) < min) {
                    min = alist.get(i);
                    index = i;
                }
            }
        }
        return index;
    }
}

