package model;

import java.util.HashMap;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public abstract class AbstractGraph<V> {
    protected HashMap<Long, V> vertices;

    /**
     * Creates an abstract graph by initializing the vertices map
     */
    public AbstractGraph() {
        vertices = new HashMap<>();
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
}
