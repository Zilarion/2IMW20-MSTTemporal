package model;

import java.util.ArrayList;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public abstract class AbstractVertex<E> {
    protected ArrayList<E> in, out; // :TODO: verify this is always a directed graph
    protected long identifier;

    /**
     * Creates a new vertex with given identifier
     * @param identifier The identifier of the edge
     */
    public AbstractVertex(long identifier) {
        this.identifier = identifier;
        in = new ArrayList<>();
        out = new ArrayList<>();
    }

    /**
     * Gets the identifier of this vertex
     * @return The identifier of the vertex
     */
    public long getIdentifier() {
        return identifier;
    }
}
