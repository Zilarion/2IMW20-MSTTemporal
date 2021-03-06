package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public abstract class AbstractEdge<T> {
    protected T from, to;
    protected int weight;

    /**
     * Creates a new edge given a from and to vertex
     * @param from The vertex this edge starts from
     * @param to The vertex this edge goes to
     */
    public AbstractEdge(T from, T to) {
        this.from = from;
        this.to = to;
    }
    /**
     * Sets the vertex this edge comes from
     * @param from The vertex this edge comes from
     */
    public void setFrom(T from) {
        this.from = from;
    }

    /**
     * Sets the vertex this edge goes to
     * @param to The vertex this edge goes to
     */
    public void setTo(T to) {
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
    public T from() {
        return from;
    }

    /**
     * The vertex this edge is to
     * @return The to vertex
     */
    public T to() {
        return to;
    }

}
