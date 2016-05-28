package computation;

import dst.DST;
import dst.DSTEdge;
import dst.DSTVertex;
import model.TemporalEdge;
import model.TemporalGraph;
import model.TemporalVertex;

import java.util.*;

/**
 * Created by ruudandriessen on 28/05/16.
 */
public class MSTToDST {
    public static DST transformTemporal(TemporalGraph tg) {
        DST dst = new DST();

        // Fills dst with virtual/dummy vertices and virtual edges
        fillVertices(tg, dst);

        // Fills dst with solid edges
        fillEdges(tg, dst);

        // Returns the transformed tg as a dst
        return dst;
    }

    private static void fillVertices(TemporalGraph T, DST dst) {
        HashMap<Long, TemporalVertex> vertices = T.getVertices();

        long id = 0;
        // Execute for each vertex in G
        for (TemporalVertex v : vertices.values()) {
            if (id > 0) {
                ArrayList<TemporalEdge> inEdges = sortEdges(v);

                // Create virtual vertices for each incoming edge from T in the DST
                DSTVertex previousVertex = null;
                for (int i = 0; i < inEdges.size(); i++) {
                    // Get the next edge
                    TemporalEdge e = inEdges.get(i);

                    // Create new vertex
                    DSTVertex newVertex = new DSTVertex(id, i);

                    // Add edge from previously created vertex to the new vertex
                    if (previousVertex != null) {
                        addEdge(dst, previousVertex, newVertex, 0);
                    }

                    // Insert new vertex into the dst
                    dst.addVirtualVertex(newVertex);

                    // Set this vertex to be previous vertex
                    previousVertex = newVertex;
                }

                // Also create a dummy vertex for each vertex in T
                DSTVertex dummyVertex = new DSTVertex(id);

                // Add edge from last virtual vertex to dummy vertex
                if (previousVertex != null) {
                    addEdge(dst, previousVertex, dummyVertex, 0);
                }
                dst.addVirtualVertex(dummyVertex);
            } else {
                // Take this as the root
                dst.addDummyVertex(new DSTVertex(0));
            }
            id++;
        }
    }

    private static void addEdge(DST dst, DSTVertex v1, DSTVertex v2, int weight) {
        DSTEdge newEdge = new DSTEdge(v1, v2, 0);
        v1.addOutEdge(newEdge);
        v2.addInEdge(newEdge);
        dst.addEdge(newEdge);
    }

    private static void fillEdges(TemporalGraph T, DST dst) {
        ArrayList<TemporalEdge> edges = T.edges();
        HashMap<Long, HashMap<Integer, DSTVertex>> vVertices = dst.getVirtualVertices();

        long id = 0;
        for (TemporalEdge e : edges) {
//            for (DSTVertex v : vVertices.get()) {

//            }
        }

    }

    private static ArrayList<TemporalEdge> sortEdges(TemporalVertex v) {
        ArrayList<TemporalEdge> edges = v.in();
        Collections.sort(edges, (e1, e2) -> Float.compare(e1.end(), e2.end()));
        return edges;
    }
}