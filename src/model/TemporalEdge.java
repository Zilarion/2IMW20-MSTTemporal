package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class TemporalEdge extends AbstractEdge<TemporalVertex> {
    private float start, end; // :TODO: check if start and end are required or only a single time is required

    public TemporalEdge(TemporalVertex from, TemporalVertex to) {
        super(from, to);
    }

    /**
     * Creates a new temporal edge given two vertices and start and end time
     * @param from The vertex from
     * @param to
     * @param start
     * @param end
     */
    public TemporalEdge(TemporalVertex from, TemporalVertex to, float start, float end) {
        this(from, to);
        this.start = start;
        this.end = end;
    }

    /**
     * Sets the start time of this edge
     * @param start The start time
     */
    public void setStart(float start) {
        this.start =start;
    }


    /**
     * Sets the end time of this edge
     * @param end The end time
     */
    public void setEnd(float end) {
        this.end = end;
    }

    /**
     * Gets the start time of the edge
     * @return The start time of the edge
     */
    public float start() {
        return start;
    }

    /**
     * Gets the end time of the edge
     * @return The end time of the edge
     */
    public float end() {
        return end;
    }

    @Override
    public String toString() {
        String label = "\"<" + this.start() + ", " + this.end() + ">[" + this.weight() + "]\"";
        return from.getIdentifier() + " -> " + to.getIdentifier() + " [label=" + label + "]";
    }
}
