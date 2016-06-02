package transform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcc on 1-6-2016.
 */
public class TGraph {
    protected ArrayList<TVertex> vertices;
    protected ArrayList<TEdge> edges;
    public final ArrayList<TVertex> terminals;
    public TVertex root;

    /**
     * Creates an abstract graph by initializing the vertices map
     */
    public TGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        terminals = new ArrayList<>();
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

    public int cost() {
        int cost = 0;
        for (TEdge e : edges) {
            cost += e.weight();
        }
        return cost;
    }

    public float density() {
        if (terminals.size() == 0) {
            return Float.MAX_VALUE;
        }
        return ( (float) cost() / (float)terminals.size());
    }

    public void merge(TGraph g) {
        vertices.addAll(g.getVertices());
        edges.addAll(g.edges());
        terminals.addAll(g.terminals);
    }

    @Override
    public String toString() {
        String result = "digraph DST {\n";

        for (TEdge e : edges) {
            result += e.toString() + "\n";
        }

        result += "}";
        return result;
    }
}
