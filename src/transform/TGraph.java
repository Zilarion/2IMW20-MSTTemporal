package transform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcc on 1-6-2016.
 */
public class TGraph {
    protected final ArrayList<TVertex> vertices;
    protected final ArrayList<TEdge> edges;
    private final ArrayList<TVertex> terminals;
    public TVertex root;

    /**
     * Creates an abstract graph by initializing the vertices map
     */
    public TGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        terminals = new ArrayList<>();
    }

    public void addTerminal(TVertex terminal) {
        this.terminals.add(terminal);
    }

    public ArrayList<TVertex> terminals() {
        return this.terminals;
    }

    public float den(int notCovered) {
        if (this.getVertices().size() == 0) {
            return Float.POSITIVE_INFINITY;
        }

        return ((float) cost() / (float) (this.terminals.size() - notCovered));
    }

    public List<TVertex> getVertices() {
        return vertices;
    }

    public void addEdge(TEdge e) {
        this.edges.add(e);
    }

    public void addUniqueEdge(TEdge e) {
        if (!this.edges.contains(e)) {
            this.edges.add(e);
            //System.out.println(e);
            if (!this.vertices.contains(e.from())) {
                this.vertices.add(e.from());
            }

            if (!this.vertices.contains(e.to())) {
                this.vertices.add(e.to());
            }
        }
    }

    public TEdge getEdge(TVertex from, TVertex to) {
        for (TEdge edge : from.out()) {
            if (edge.to() == to) {
                return edge;
            }
        }

        return null;
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
        return ((float) cost() / (float) terminals.size());
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
