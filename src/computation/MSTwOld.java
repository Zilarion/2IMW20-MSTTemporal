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
        TGraph tree = this.algorithm3(transformed, 2, k, r, X);

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

                    if (_e.weight() < min) {
                        min = _e.weight();
                        edgeMin = _e;
                    }
                }

                // line 5 of algorithm 3
                T.addUniqueEdge(edgeMin);
                k--;
                X.remove(edgeMin.to());
            }
        } else {
            while (k > 0) {
                // line 8 of algorithm 3
                TGraph Tbest = new TGraph();
                float den = Tbest.den(X.size());

                // line 9 of algorithm 3
                for (TVertex _v : graph.getVertices()) {
                    for (int k1 = 1; k1 <= k; k++) {

                        // line 10 of algorithm 3
                        TGraph T1 = this.algorithm3(graph, i - 1, k1, _v, new ArrayList<>(X));
                        TEdge _e = graph.getEdge(r, _v);
                        if (_e != null) {

                            T1.addUniqueEdge(_e);

                            // line 11 of algorithm 3
                            float _d = T1.den(X.size());
                            if (den > _d) {
                                // line 12 of algorithm 3
                                den = _d;
                                Tbest = T1;
                            }
                        }
                    }
                }


                // line 13 of algorithm 3
                T.merge(Tbest);
                for (TVertex _v : Tbest.getVertices()) {
                    if (X.contains(_v)) {
                        k--;
                    }
                }
                X.removeAll(Tbest.getVertices());

            }
        }

        // line 14 of algorithm 3
        return T;
    }

}
