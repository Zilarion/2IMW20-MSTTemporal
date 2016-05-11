package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public abstract class AbstractEdge {
    protected AbstractVertex from, to;
    protected float weight; // :TODO: check if weight is a float or always an int
    // :TODO: check where weight should be, TemporalEdge, Edge or AbstractEdge

    /**
     * Creates a new edge given a from and to vertex
     * @param from The vertex this edge starts from
     * @param to The vertex this edge goes to
     */
    public AbstractEdge(AbstractVertex from, AbstractVertex to) {
        this.from = from;
        this.to = to;
    }
    /**
     * Sets the vertex this edge comes from
     * @param from The vertex this edge comes from
     */
    public void setFrom(AbstractVertex from) {
        this.from = from;
    }

    /**
     * Sets the vertex this edge goes to
     * @param to The vertex this edge goes to
     */
    public void setTo(AbstractVertex to) {
        this.to = to;
    }

    /**
     * Sets the weight of the edge
     * @param weight The weight of the edge
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Returns the weight of the edge
     * @return The weight of the edge
     */
    public float weight() {
        return weight;
    }
}
