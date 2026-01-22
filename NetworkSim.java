import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/*
 * Name: Ehab Elsaigh
 * Project: Network Routing Simulator
 * Description: implementing Dijkstra's algo to simulate network packets 
 * finding the fastest route between cities.
 */

// Basic node class, represents a city (router) in the network
class Node implements Comparable<Node> {
    public final String name;
    public List<Edge> adjacencies = new ArrayList<>();
    
    // variables needed for Dijkstra
    public double minDistance = Double.POSITIVE_INFINITY; // start at infinity
    public Node previous; // keep track of the path

    public Node(String name) { 
        this.name = name; 
    }

    public String toString() { 
        return name; 
    }

    // We need to override this so the PriorityQueue knows how to sort nodes (by smallest distance)
    @Override
    public int compareTo(Node other) {
        return Double.compare(minDistance, other.minDistance);
    }
}

// Helper class for the connections between cities
class Edge {
    public final Node target;
    public final double weight; // this represents latency in ms

    public Edge(Node target, double weight) {
        this.target = target;
        this.weight = weight;
    }
}

// Class to handle the actual pathfinding logic
class Pathfinder {

    public static void computePaths(Node source) {
        source.minDistance = 0.;
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            // get the node with the smallest distance so far
            Node u = queue.poll();

            // loop through all neighbors (edges) of this node
            for (Edge e : u.adjacencies) {
                Node v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;

                // Relaxation step: if we found a shorter path to v through u, update it
                if (distanceThroughU < v.minDistance) {
                    queue.remove(v); // remove it to update priority
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    queue.add(v);
                }
            }
        }
    }

    // Backtrack from the target to the source to build the path list
    public static List<Node> getShortestPathTo(Node target) {
        List<Node> path = new ArrayList<>();
        for (Node vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }
        Collections.reverse(path); // reverse it so it goes source -> target
        return path;
    }
}

public class NetworkSim {
    public static void main(String[] args) {
        // Setup the graph nodes
        Node ottawa = new Node("Ottawa");
        Node montreal = new Node("Montreal");
        Node toronto = new Node("Toronto");
        Node kingston = new Node("Kingston");

        // Define the edges (network links) with weights (latency)
        // Ottawa has two connections
        ottawa.adjacencies.add(new Edge(montreal, 5));
        ottawa.adjacencies.add(new Edge(kingston, 10)); // direct link
        
        // Kingston connects to Toronto
        kingston.adjacencies.add(new Edge(toronto, 10));
        
        // Montreal connects to Toronto but it's a slower link
        montreal.adjacencies.add(new Edge(toronto, 50));

        // --- SCENARIO 1: Standard routing ---
        System.out.println("--- SCENARIO 1: Normal Network Conditions ---");
        
        // Run Dijkstra from Ottawa
        Pathfinder.computePaths(ottawa); 
        
        System.out.println("Calculating fastest path from Ottawa to Toronto...");
        List<Node> path1 = Pathfinder.getShortestPathTo(toronto);
        System.out.println("Route: " + path1);
        // Should go through Kingston (10+10 = 20ms) instead of Montreal (5+50 = 55ms)

        
        // --- SCENARIO 2: Simulating a network failure ---
        System.out.println("\n--- SCENARIO 2: Link Failure (Ottawa-Kingston Cut) ---");
        
        // Reset everything to infinity before running again
        resetGraph(ottawa, montreal, toronto, kingston);
        
        // Simulate the cable cut by clearing Ottawa's edges and only adding Montreal back
        // Basically removing the Kingston edge
        ottawa.adjacencies.clear(); 
        ottawa.adjacencies.add(new Edge(montreal, 5)); 

        Pathfinder.computePaths(ottawa);
        List<Node> path2 = Pathfinder.getShortestPathTo(toronto);
        
        System.out.println("Recalculating route...");
        System.out.println("New Route: " + path2);
        // Should reroute through Montreal now
    }

    // helper function to clear previous results
    private static void resetGraph(Node... nodes) {
        for (Node n : nodes) {
            n.minDistance = Double.POSITIVE_INFINITY;
            n.previous = null;
        }
    }
}