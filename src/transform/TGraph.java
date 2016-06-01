package transform;

import model.AbstractGraph;
import model.Edge;
import model.Vertex;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by marcc on 1-6-2016.
 */
public class TGraph {
    protected HashMap<String, TVertex> vertices;
    protected ArrayList<TEdge> edges;

    /**
     * Creates an abstract graph by initializing the vertices map
     */
    public TGraph() {
        vertices = new HashMap<>();
        edges = new ArrayList<>();
    }

    /**
     * Returns whether a vertex identifier is in the graph
     *
     * @param identifier The identifier to check
     * @return True if it is contained, false if it is not
     */
    public boolean contains(String identifier) {
        return vertices.containsKey(identifier);
    }

    public HashMap<String, TVertex> getVertices() {
        return vertices;
    }

    public void addEdge(TEdge e) {
        this.edges.add(e);
    }

    public ArrayList<TEdge> edges() {
        return edges;
    }

    /**
     * Adds a new vertex in the graph
     *
     * @param vertex The vertex
     * @return The vertex instance
     */
    public TVertex addVertex(TVertex vertex) {
        return vertices.put(vertex.getIdentifier(), vertex);
    }

    /**
     * Gets the vertex instance based on an identifier
     *
     * @param identifier The identifier of the vertex
     * @return The vertex instance if it is contained, null otherwise
     */
    public TVertex getVertex(String identifier) {
        return vertices.get(identifier);
    }


    @Override
    public String toString() {
        return "Transformed graph with " + edges.size() + " edges and " + vertices.size() + " vertices";
//        String result = "Transformed graph {\n";
//        for (TVertex v : vertices.values()) {
//            result += "\t" + v.toString() + "\n";
//        }
//        for (TEdge e : edges) {
//            result += "\t" + e.toString() + "\n";
//        }
//        result += "}";
//        return result;
    }
}
