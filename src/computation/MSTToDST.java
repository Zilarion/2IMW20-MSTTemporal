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

        // Execute for each vertex in G
        for (long id = 0; id < vertices.size(); id++) {
            TemporalVertex v = vertices.get(id);
            if (id > 0) {
                ArrayList<TemporalEdge> inEdges = sortEdges(v.in());

                // Create virtual vertices for each incoming edge from T in the DST
                DSTVertex previousVertex = null;
                for (int i = 0; i < inEdges.size(); i++) {
                    // Get the next edge
                    TemporalEdge e = inEdges.get(i);

                    // Create new vertex (i+1) to match up with paper, could be just i as virtualId as well
                    DSTVertex newVertex = new DSTVertex(id, i + 1, e.start());

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
                dst.addDummyVertex(dummyVertex);
            } else {
                // Take this as the root
                dst.addVirtualVertex(new DSTVertex(0, 1, Float.MAX_VALUE));
            }
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
        for (TemporalEdge e : edges) {
            System.out.println("--------------------------");
            // Initialize ui (start) and vi (end) to null
            DSTVertex start = null, end = null;

            // Get virtual vertices with the id similar to the edge from (possible ui's)
           ArrayList<DSTVertex> virtualVertices = dst.getVirtualVertices();
            System.out.println("--- ui ---");
            // Find ui
            for (DSTVertex v : virtualVertices) {
                long id = v.virtualId();
                DSTVertex next = dst.getVirtualVerticesOfId(v.id()).get(id+1);
                System.out.println("Checking if: " + e.start() + " <= " + v.virtualTime());
                System.out.println("Checking if " + (id+1) + " is in set.. ");
                if (next != null) {
                    System.out.println("Checking if: " + next.virtualTime() + " > " + e.start());
                }
                // tui <= tu && tui+1 > tu
                if (e.start() <= v.virtualTime() && (next == null || next.virtualTime() > e.start())) {
                    // This is a match
                    start = v;
                    break;
                }
            }
            System.out.println("--- vi ---");

            // Find vi by using the virtual vertices of to (possible vi's)
            for (DSTVertex v : virtualVertices) {
                System.out.println("Checking if: " + e.start() + " = " + v.virtualTime());
                if (e.end() == v.virtualTime()) { // tvj == tv
                    end = v;
                    break;
                }
            }
            if (start != null && end != null) {
                // Add this new edge
                dst.addEdge(new DSTEdge(start, end, e.weight()));
            } else {
                System.out.println("Failed to process edge " + e.from().getIdentifier() + " -> " + e.to().getIdentifier());
                System.out.println(start + " / " + end);
            }
        }

    }

    private static ArrayList<TemporalEdge> sortEdges(ArrayList<TemporalEdge> edges) {
        Collections.sort(edges, (e1, e2) -> Float.compare(e1.start(), e2.start()));
        return edges;
    }
}