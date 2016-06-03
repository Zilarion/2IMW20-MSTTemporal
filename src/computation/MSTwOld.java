package computation;

import model.AbstractGraph;
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
        Transform.createTransitiveClosure(transformed);

        // do algorithm 3 (page 424)
        System.out.println("Apply algorithm 3..");
        int k = transformed.terminals().size();
        List<TVertex> X = new ArrayList<>(transformed.terminals());
        TVertex r = transformed.root;
        TGraph tree = this.algorithm3(transformed, 2, k, r, new ArrayList<>(X));
        
        // do postprocessing (page 424)
        System.out.println(this.doPostProcessing(transformed, tree));

    }

    private TGraph doPostProcessing(TGraph transformed, TGraph tree) {
        return tree;
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
