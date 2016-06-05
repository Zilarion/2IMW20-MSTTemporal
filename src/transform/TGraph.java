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

    public TGraph (TGraph copy) {
        vertices = new ArrayList<>(copy.vertices);
        edges = new ArrayList<>(copy.edges);
        terminals = new ArrayList<>(copy.terminals);
        root = copy.root;
    }

    public void addTerminal(TVertex terminal) {
        this.terminals.add(terminal);
    }

    public ArrayList<TVertex> terminals() {
        return this.terminals;
    }

    public float den(List<TVertex> terminals) {
        int covered = 0;
        for (TVertex v : vertices) {
            if (terminals.contains(v)) {
                covered++;
            }
        }
        if (covered == 0) {
             return Float.POSITIVE_INFINITY;
        } else {
            return ((float) cost() / (float) (covered));
        }
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
            if (!this.vertices.contains(e.from())) {
                this.vertices.add(e.from());
            }

            if (!this.vertices.contains(e.to())) {
                this.vertices.add(e.to());
            }
        }
    }

    public void removeEdge(TEdge edge) {
        this.edges.remove(edge);
        edge.from.removeOutEdge(edge);
        edge.to.removeInEdge(edge);

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

    public void merge(TGraph g) {
        for (TVertex v: g.getVertices()) {
            if (!vertices.contains(v)) {
                vertices.add(v);
            }
        }
        for (TEdge e: g.edges()) {
            if (!edges.contains(e)) {
                edges.add(e);
            }
        }
        for (TVertex v: g.terminals()) {
            if (!terminals.contains(v)) {
                terminals.add(v);
            }
        }
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
