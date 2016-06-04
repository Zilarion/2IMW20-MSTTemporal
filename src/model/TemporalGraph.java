package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class TemporalGraph extends AbstractGraph<TemporalVertex, TemporalEdge> {

    /**
     * Adds a new temporal vertex in the graph
     * @param identifier The identifier of the temporal vertex
     * @return The temporal vertex instance
     */
    public TemporalVertex addVertex(long identifier) {
        return vertices.put(identifier, new TemporalVertex(identifier));
    }

    public void removeEdge(TemporalEdge edge) {
        this.edges.remove(edge);
        edge.from().removeOutEdge(edge);
        edge.to().removeInEdge(edge);
    }

    /**
     * Adds
     * @param vertex
     * @return
     */
    public TemporalVertex addVertex(TemporalVertex vertex) {
        return vertices.put(vertex.getIdentifier(), vertex);
    }

    /**
     * Gets the vertex instance based on an identifier
     * @param identifier The identifier of the vertex
     * @return The vertex instance if it is contained, null otherwise
     */
    public TemporalVertex getVertex(long identifier) {
        return vertices.get(identifier);
    }
}
