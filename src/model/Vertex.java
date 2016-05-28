package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class Vertex extends AbstractVertex<Edge> {
    /**
     * Creates a new vertex with given identifier
     *
     * @param identifier The identifier of the edge
     */
    public Vertex(long identifier) {
        super(identifier);
    }

    /**
     * Adds a new incoming edge
     * @param edge The edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean addInEdge(Edge edge) {
        return in.add(edge);
    }

    /**
     * Adds a new outgoing edge
     * @param edge The edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean addOutEdge(Edge edge) {
        return out.add(edge);
    }
}
