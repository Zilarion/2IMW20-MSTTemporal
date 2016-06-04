package computation;

import com.sun.org.apache.xpath.internal.SourceTree;
import model.AbstractGraph;
import model.TemporalEdge;
import model.TemporalGraph;
import model.TemporalVertex;
import transform.TEdge;
import transform.TGraph;
import transform.TVertex;
import transform.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ruudandriessen on 25/05/16.
 */
public class MSTwOld extends Algorithm {

    @Override
    public void run(AbstractGraph graph) {
        TemporalGraph g;
        if (graph instanceof TemporalGraph) {
            g = (TemporalGraph) graph;
        } else {
            throw new IllegalArgumentException("Cannot use MSTw without temporal graph");
        }

        // Assume interval to be [0, inf) and get root
        ArrayList<Long> vKeys = new ArrayList<>(g.getVertices().keySet());
        Collections.sort(vKeys);
        TemporalVertex root = g.getVertex(vKeys.get(0));

        // Transform graph (page 423)
        System.out.println("Creating transformed graph..");
        TGraph transformed = Transform.transform(g, root);

        // Create transitive closure
        System.out.println("Creating transitive closure..");
        TGraph algo3 = Transform.createTransitiveClosure(transformed);

        // do algorithm 3 (page 424)
        System.out.println("Apply algorithm 3..");
        int k = transformed.terminals().size();
        List<TVertex> X = new ArrayList<>(transformed.terminals());
        TVertex r = transformed.root;
        algo3 = this.algorithm3(algo3, 2, k, r, new ArrayList<>(X));

        // do postprocessing (page 424)
        System.out.println("Do postprocessing..");
        System.out.println(this.doPostProcessing(g, transformed, algo3));

    }

    private boolean shortestPath(List<TEdge> edges, TGraph transformed, TVertex from, TVertex to, int maxWeight) {
        for (TEdge edge : to.in()) {
            if (transformed.edges().contains(edge)) {
                if (edge.from() == from) {
                    if (edge.weight() <= maxWeight) {
                        edges.add(edge);
                        return true;
                    }
                } else {
                    if (this.shortestPath(edges, transformed, from, edge.from(), maxWeight - edge.weight())) {
                        edges.add(edge);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private TemporalGraph doPostProcessing(TemporalGraph original, TGraph transformed, TGraph algo3) {
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
                this.shortestPath(shortestPath, transformed, edge.from(), edge.to(), edge.weight());
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

    public TGraph algorithm3(TGraph graph, int i, int k, TVertex r, List<TVertex> X) {
        // line 1 of algorithm 3
        TGraph T = new TGraph();

        // line 2 of algorithm 3
        if (i == 1) {
            // line 3 of algorithm 3
            while (k > 0) {
                // line 4 of algorithm 3
                TEdge edgeMin = null;
                int min = Transform.infinity;
                for (TVertex _v : X) {
                    TEdge _e = graph.getEdge(r, _v);
                    if (_e != null) {
                        if (_e.weight() < min) {
                            min = _e.weight();
                            edgeMin = _e;
                        }
                    }
                }

                // line 5 of algorithm 3
                if (edgeMin != null) {
                    T.addUniqueEdge(edgeMin);
                    X.remove(edgeMin.to());
                }
                k--;
            }
        } else { // line 6 of algorithm 3
            // line 7 of algorithm 3
            while (k > 0) {
                // line 8 of algorithm 3
                TGraph T_best = new TGraph();
                float den = T_best.den(graph.terminals());
                // line 9 of algorithm 3
                for (TVertex _v : graph.getVertices()) {
                    for (int k_accent = 1; k_accent <= k; k_accent++) {

                        // line 10 of algorithm 3
                        TGraph T_accent = this.algorithm3(graph, i - 1, k_accent, _v, new ArrayList<>(X)); // X shouldn't be passed as reference
                        TEdge _e = graph.getEdge(r, _v);
                        if (_e != null && !T_accent.getVertices().isEmpty()) {
                            T_accent.addUniqueEdge(_e);
                        }

                        // line 11 of algorithm 3
                        float _d = T_accent.den(graph.terminals());
                        if (den > _d) {
                            // line 12 of algorithm 3
                            den = _d;
                            T_best = T_accent;
                        }
                    }
                }

                // line 13 of algorithm 3
                T.merge(T_best);
                for (TVertex _v : T_best.getVertices()) {
                    if (X.contains(_v)) {
                        X.remove(_v);
                        k--;
                    }
                }
            }
        }

        // line 14 of algorithm 3
        return T;
    }
}
