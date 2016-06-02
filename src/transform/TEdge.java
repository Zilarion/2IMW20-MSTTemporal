package transform;

import model.Edge;
import model.Vertex;

/**
 * Created by marcc on 1-6-2016.
 */
public class TEdge {
    protected TVertex from, to;
    protected int weight;

    /**
     * Creates a new edge given a from and to vertex
     * @param from The vertex this edge starts from
     * @param to The vertex this edge goes to
     */
    public TEdge(TVertex from, TVertex to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;

        from.addOutEdge(this);
        to.addInEdge(this);
    }
    /**
     * Sets the vertex this edge comes from
     * @param from The vertex this edge comes from
     */
    public void setFrom(TVertex from) {
        this.from = from;
    }

    /**
     * Sets the vertex this edge goes to
     * @param to The vertex this edge goes to
     */
    public void setTo(TVertex to) {
        this.to = to;
    }

    /**
     * Sets the weight of the edge
     * @param weight The weight of the edge
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Returns the weight of the edge
     * @return The weight of the edge
     */
    public int weight() {
        return weight;
    }


    /**
     * The vertex this edge is from
     * @return The from vertex
     */
    public TVertex from() {
        return from;
    }

    /**
     * The vertex this edge is to
     * @return The to vertex
     */
    public TVertex to() {
        return to;
    }

    @Override
    public String toString() {
        String label = "\"[" + this.weight() + "]\"";
        return from.getIdentifier() + " -> " + to.getIdentifier() + " [label=" + label + "]";
    }
}
