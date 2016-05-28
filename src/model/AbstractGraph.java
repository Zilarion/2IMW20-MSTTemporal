package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public abstract class AbstractGraph<V, E> {
    protected HashMap<Long, V> vertices;
    protected ArrayList<E> edges;

    /**
     * Creates an abstract graph by initializing the vertices map
     */
    public AbstractGraph() {
        vertices = new HashMap<>();
        edges = new ArrayList<>();
    }

    /**
     * Returns whether a vertex identifier is in the graph
     * @param identifier The identifier to check
     * @return True if it is contained, false if it is not
     */
    public boolean contains(long identifier) {
        return vertices.containsKey(identifier);
    }

    public HashMap<Long, V> getVertices() {
        return vertices;
    }

    public void addEdge(E e) {
        this.edges.add(e);
    }

    public ArrayList<E> edges() {
        return edges;
    }

    @Override
    public String toString() {
        String result = "digraph MST {\n";
        for (E e : edges) {
            result += e.toString() + "\n";
        }
        result += "}";
        return result;
    }
}
