package dst;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ruudandriessen on 25/05/16.
 */
public class DST {
    protected HashMap<Long, HashMap<Integer, DSTVertex>> virtualVertices;
    protected HashMap<Long, DSTVertex> dummyVertices;
    protected ArrayList<DSTEdge> edges;

    public DST() {
        dummyVertices = new HashMap<>();
        virtualVertices = new HashMap<>();
        edges = new ArrayList<>();
    }

    public void addVirtualVertex(DSTVertex v) {
        if (virtualVertices.containsKey(v.id())) {
            // Insert in already existing virtual hash
            this.virtualVertices.get(v.id()).put(v.virtualId(), v);
        } else {
            // Create new virtual hashmap
            HashMap<Integer, DSTVertex> virtualHash = new HashMap<>();
            virtualHash.put(v.virtualId(), v);
            this.virtualVertices.put(v.id(), virtualHash);
            this.virtualVertices.get(v.id()).put(v.virtualId(), v);
        }
    }

    public void addEdge(DSTEdge e) {
        this.edges.add(e);
    }

    public void addDummyVertex(DSTVertex v) {
        this.dummyVertices.put(v.id(), v);
    }

    public DSTVertex getVirtualVertex(long id, int virtualId) {
        if (virtualVertices.containsKey(id)) {
            return virtualVertices.get(id).get(virtualId);
        } else {
            return null;
        }
    }

    public DSTVertex getDummyVertex(long id) {
        return dummyVertices.get(id);
    }

    public DSTVertex getVertex(long id) {
        return getDummyVertex(id);
    }

    public DSTVertex getVertex(long id, int virtualId) {
        if (virtualId == -1) {
            return getDummyVertex(id);
        } else {
            return getVirtualVertex(id, virtualId);
        }
    }

    public HashMap<Integer, DSTVertex> getVirtualVerticesOfId(long id) {
        return virtualVertices.get(id);
    }

    public ArrayList<DSTVertex> getVirtualVertices() {
        ArrayList<DSTVertex> vertices = new ArrayList<>();
        for (HashMap<Integer, DSTVertex> virtualVMap : virtualVertices.values()) {
            vertices.addAll(virtualVMap.values());
        }
        return vertices;
    }

    @Override
    public String toString() {
        String result = "digraph DST {\n";

        for (DSTEdge e : edges) {
            result += e.toString() + "\n";
        }

        result += "}";
        return result;
    }
}
