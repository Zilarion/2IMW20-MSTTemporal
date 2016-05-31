package dst;

/**
 * Created by ruudandriessen on 28/05/16.
 */
public class DSTEdge {
    private DSTVertex from, to;
    private float weight;

    public DSTEdge(DSTVertex from, DSTVertex to, float weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public String toString() {
        String label = "\"[" + weight + "]\"";
        String fromLabel = "\"" + from.id() + (from.virtualId() == -1 ? "" : "_" + from.virtualId()) + "\"";
        String toLabel = "\"" + to.id() + (to.virtualId() == -1 ? "" : "_" + to.virtualId()) + "\"";
        return fromLabel + " -> " + toLabel + "[label=" + label + "]";
    }
}
