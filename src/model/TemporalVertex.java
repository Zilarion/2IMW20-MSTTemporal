package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class TemporalVertex extends AbstractVertex<TemporalEdge> {
    /**
     * Creates a Temporal Vertex with the given identifier
     * @param identifier The identifier of the temporal vertex
     */
    public TemporalVertex(long identifier) {
        super(identifier);
    }

    /**
     * Adds a new incoming temporal edge
     * @param edge The temporal edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean addInEdge(TemporalEdge edge) {
        return in.add(edge);
    }

    /**
     * Adds a new outgoing temporal edge
     * @param edge The temporal edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean addOutEdge(TemporalEdge edge) {
        return out.add(edge);
    }

    /**
     * Adds a new incoming temporal edge
     * @param edge The temporal edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean removeInEdge(TemporalEdge edge) {
        return in.remove(edge);
    }

    /**
     * Adds a new outgoing temporal edge
     * @param edge The temporal edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean removeOutEdge(TemporalEdge edge) {
        return out.remove(edge);
    }

    public TemporalEdge getOutEdge(TemporalVertex v) {
        for (TemporalEdge e : out) {
            if (e.to().equals(v)) {
                return e;
            }
        }
        return null;
    }
}
