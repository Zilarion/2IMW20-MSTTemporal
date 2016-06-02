package transform;


import model.Edge;
import model.TemporalEdge;
import model.TemporalVertex;
import model.Vertex;

import java.util.ArrayList;

/**
 * Created by marcc on 1-6-2016.
 */
public class TVertex {
    protected ArrayList<TEdge> in, out; // :TODO: verify this is always a directed graph
    protected String identifier;
    public final long origId;
    public final float time;

    public TVertex(String id, long origId, float time) {
        this.identifier = id;
        in = new ArrayList<>();
        out = new ArrayList<>();
        this.origId = origId;
        this.time = time;
    }


    /**
     * Gets the identifier of this vertex
     *
     * @return The identifier of the vertex
     */
    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<TEdge> in() {
        return in;
    }

    public ArrayList<TEdge> out() {
        return out;
    }

    /**
     * Adds a new incoming edge
     *
     * @param edge The edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean addInEdge(TEdge edge) {
        return in.add(edge);
    }

    /**
     * Adds a new outgoing edge
     *
     * @param edge The edge to add
     * @return true if properly inserted, false otherwise
     */
    public boolean addOutEdge(TEdge edge) {
        return out.add(edge);
    }

    @Override
    public String toString() {
        return this.identifier + " " + time;
    }


    public TEdge getOutEdge(TVertex v) {
        for (TEdge e : out) {
            if (e.to().equals(v)) {
                return e;
            }
        }
        return null;
    }
}
