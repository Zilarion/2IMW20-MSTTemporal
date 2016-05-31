package dst;

import java.util.ArrayList;

/**
 * Created by ruudandriessen on 25/05/16.
 */
public class DSTVertex {
    private long id;
    private int virtualId;
    private ArrayList<DSTEdge> out, in;
    private float virtualTime;

    /**
     * Create non-virtual vertex (virtualid == -1)
     * @param id The identifier of the vertex
     */
    public DSTVertex(long id) {
        this(id, -1, -1);
    }

    /**
     * Create virtual vertex
     * @param id The identifier of the vertex
     * @param virtualId The virtual identifier of the vertex
     */
    public DSTVertex(long id, int virtualId, float virtualTime) {
        this.id = id;
        this.virtualId = virtualId;
        this.virtualTime = virtualTime;
        out = new ArrayList<>();
        in = new ArrayList<>();
    }

    public long id() {
        return id;
    }

    public int virtualId() {
        return virtualId;
    }

    public void addOutEdge(DSTEdge e) {
        this.out.add(e);
    }

    public void addInEdge(DSTEdge e) {
        this.in.add(e);
    }

    public float virtualTime() { return virtualTime; }
}
