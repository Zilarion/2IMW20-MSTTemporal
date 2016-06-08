package transform;

import model.TemporalEdge;
import model.TemporalGraph;
import model.TemporalVertex;

import java.util.*;

/**
 * Created by ruudandriessen on 02/06/16.
 */
public class Transform {
    /**
     * This is infinity
     */
    public static int infinity = Integer.MAX_VALUE;

    /**
     * This method uses Floyd Wharshalls algorithm to turn the graph into the transitive closure of it.
     *
     * @param graph The graph of which you want to get the transitive closure from.
     */
    public static TGraph createTransitiveClosure(TGraph graph) {
        TGraph closure = new TGraph(graph);

        // matrix containing all distances
        int[][] dist = new int[closure.getVertices().size()][closure.getVertices().size()];
        float[][] orig = new float[closure.getVertices().size()][closure.getVertices().size()];

        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[i].length; j++) {
                dist[i][j] = infinity;
                orig[i][j] = infinity;
            }
            for (TEdge edge : closure.getVertices().get(i).out()) {
                int j = closure.getVertices().indexOf(edge.to());
                if (edge.weight() < dist[i][j]) {
                    dist[i][j] = edge.weight();
                    orig[i][j] = edge.weight();
                }
            }
        }

        for (int k = 0; k < closure.getVertices().size(); k++) {
            for (int i = 0; i < closure.getVertices().size(); i++) {
                for (int j = 0; j < closure.getVertices().size(); j++) {
                    if (dist[i][k] != infinity && dist[k][j] != infinity) {
                        if (dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }

        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                if (dist[i][j] != orig[i][j]) {
                    closure.addEdge(new TEdge(closure.getVertices().get(i), closure.getVertices().get(j), dist[i][j]));
                }
            }
        }

        for (TVertex v : closure.getVertices()) {
            if (closure.getEdge(v, v) == null) {
                closure.addEdge(new TEdge(v, v, 0));
            }
        }

        return closure;
    }

    /**
     * Transform graph using the steps on page 423.
     *
     * @param graph The graph you need to transform.
     * @param root  The root vertex of that graph.
     * @return The transformed graph.
     */
    public static TGraph transform(TemporalGraph graph, TemporalVertex root) {
        TGraph transformed = new TGraph();
        Map<TemporalVertex, List<TVertex>> map = new HashMap<>();
        // For each vertex..
        for (TemporalVertex vertex : graph.getVertices().values()) {
            // Start step 1
            // Determine set T(v)
            Set<Float> tv = new TreeSet<>();
            if (vertex == root) {
                // If root, then T(v) = {0}
                tv.add(0f);
            } else {
                for (TemporalEdge e : vertex.in()) {
                    tv.add(e.end());
                }
            }

            // Step 1a: Create the virtual vertices
            List<TVertex> vv = new ArrayList<>();
            int count = 1;
            for (float time : tv) {
                TVertex v = new TVertex(vertex.getIdentifier() + "_" + count, vertex.getIdentifier(), time);
                vv.add(v);
                transformed.addVertex(v);
                if (vertex == root) {
                    transformed.root = v;
                }
                count++;
            }
            // Step 1b: Create dummy vertices for non-roots
            if (vertex != root) {
                TVertex v = new TVertex(Long.toString(vertex.getIdentifier()), vertex.getIdentifier(), infinity);
                vv.add(v);
                transformed.addTerminal(v);
                transformed.addVertex(v);
            }


            // Start step 2
            // Step 2a create the virtual edges
            for (int i = 1; i < vv.size(); i++) {
                TEdge e = new TEdge(vv.get(i - 1), vv.get(i), 0);
                transformed.addEdge(e);
            }

            map.put(vertex, vv);
        }

        // Step 2b: Find the correct vertices and create solid edges
        for (TemporalEdge edge : graph.edges()) {
            List<TVertex> vertices = map.get(edge.from());
            TVertex from = null;
            for (int i = vertices.size() - 1; i >= 0; i--) {
                if (vertices.get(i).time <= edge.start()) {
                    from = vertices.get(i);
                    break;
                }
            }

            vertices = map.get(edge.to());
            TVertex to = null;
            for (int i = 0; i < vertices.size(); i++) {
                if (vertices.get(i).time == edge.end()) {
                    to = vertices.get(i);
                    break;
                }
            }

            if (from != null && to != null) {
                TEdge e = new TEdge(from, to, edge.weight());
                transformed.addEdge(e);
            }
        }
        return transformed;

    }

    private static boolean shortestPath(List<TEdge> edges, TGraph transformed, TVertex from, TVertex to, int maxWeight) {
        for (TEdge edge : to.in()) {
            if (transformed.edges().contains(edge)) {
                if (edge.weight() <= maxWeight) {
                    if (edge.from() == from) {
                        edges.add(edge);
                        return true;
                    } else {
                        if (shortestPath(edges, transformed, from, edge.from(), maxWeight - edge.weight())) {
                            edges.add(edge);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static TemporalGraph doPostProcessing(TemporalGraph original, TGraph transformed, TGraph algo3) {
        // Remove self-loops
        for (TVertex vertex : algo3.getVertices()) {
            for (TEdge edge : new ArrayList<>(vertex.in())) {
                if (edge.from() == edge.to()) {
                    algo3.removeEdge(edge);
                }
            }
        }

        // Step 1a
        for (TEdge edge : new ArrayList<>(algo3.edges())) {
            // Remove self-loops
            if (!transformed.edges().contains(edge)) {
                // Replace by shortest path..
                List<TEdge> shortestPath = new ArrayList<>();
                shortestPath(shortestPath, transformed, edge.from(), edge.to(), edge.weight());
                for (TEdge e : shortestPath) {
                    algo3.addUniqueEdge(e);
                }
                algo3.removeEdge(edge);
            }
        }

        // First clean up the mess
        for (TVertex vertex : algo3.getVertices()) {
            for (TEdge in : new ArrayList<>(vertex.in())) {
                if (!algo3.edges().contains(in)) {
                    algo3.removeEdge(in);
                }
            }
        }

        for (TVertex vertex : algo3.getVertices()) {
            for (TEdge out : new ArrayList<>(vertex.out())) {
                if (!algo3.edges().contains(out)) {
                    algo3.removeEdge(out);
                }
            }
        }

        // Step 1b
        for (TVertex vertex : algo3.getVertices()) {
            if (vertex.in().size() > 1) {
                TEdge smallest = vertex.in().get(0);
                for (TEdge edge : new ArrayList<>(vertex.in())) {
                    if (smallest.weight() > edge.weight()) {
                        algo3.removeEdge(smallest);
                        smallest = edge;
                    } else {
                        algo3.removeEdge(edge);
                    }
                }
            }
        }

        // Step 2a
        for (TemporalEdge edge : new ArrayList<>(original.edges())) {
            boolean found = false;
            for (TEdge _e : algo3.edges()) {
                if (edge.from().getIdentifier() == _e.from().origId && edge.to().getIdentifier() == _e.to().origId && edge.weight() == _e.weight()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                original.removeEdge(edge);
            }
        }

        // Step 2b
        for (TemporalVertex vertex : original.getVertices().values()) {
            if (vertex.in().size() > 1) {
                float minValue = Float.POSITIVE_INFINITY;
                TemporalEdge minEdge = null;
                for (TemporalEdge edge : new ArrayList<>(vertex.in())) {
                    if (minValue > edge.end()) {
                        if (minEdge != null) {
                            original.removeEdge(minEdge);
                        }
                        minValue = edge.end();
                        minEdge = edge;
                    } else {
                        original.removeEdge(edge);
                    }
                }
            }
        }

        return original;
    }
}
