package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class TemporalGraph extends AbstractGraph {

    /**
     * Adds a new temporal vertex in the graph
     * @param identifier The identifier of the temporal vertex
     * @return The temporal vertex instance
     */
    public TemporalVertex addVertex(long identifier) {
        return (TemporalVertex) vertices.put(identifier, new TemporalVertex(identifier));
    }

    /**
     * Adds
     * @param vertex
     * @return
     */
    public TemporalVertex addVertex(TemporalVertex vertex) {
        return (TemporalVertex) vertices.put(vertex.getIdentifier(), vertex);
    }
}
