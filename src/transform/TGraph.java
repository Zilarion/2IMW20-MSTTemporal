package transform;

import model.AbstractGraph;
import model.Edge;
import model.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by marcc on 1-6-2016.
 */
public class TGraph {
    protected ArrayList<TVertex> vertices;
    protected ArrayList<TEdge> edges;
    public TVertex root;

    /**
     * Creates an abstract graph by initializing the vertices map
     */
    public TGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public List<TVertex> getVertices() {
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
    public void addVertex(TVertex vertex) {
        vertices.add(vertex);
    }


    @Override
    public String toString() {
        return "Transformed graph with " + edges.size() + " edges and " + vertices.size() + " vertices";
//        String result = "Transformed graph {\n";
//        for (TVertex v : vertices) {
//            result += "\t" + v.toString() + "\n";
//        }
//        for (TEdge e : edges) {
//            result += "\t" + e.toString() + "\n";
//        }
//        result += "}";
//        return result;
    }
}
