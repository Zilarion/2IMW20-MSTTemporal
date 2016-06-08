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
    public TGraph run(TemporalGraph g, TGraph transformed, TGraph algo3, int i) {
        // do algorithm 3 (page 424)
        return algorithm3(algo3, i, transformed.terminals().size(), transformed.root, new ArrayList<>(transformed.terminals()));

    }

    public TGraph algorithm3(TGraph graph, int i, int k, TVertex r, List<TVertex> X) {
        // k is not necessarily equal to |X|

        // line 1 of algorithm 3
        TGraph T = new TGraph();
        // line 2 of algorithm 3
        if (i == 1) {
            // line 3 of algorithm 3
            while (k > 0) {
                // line 4 of algorithm 3
                TEdge edgeMin = null;
                int min = Transform.infinity;
                for (TVertex v : X) {
                    TEdge _e = graph.getEdge(r, v);
                    // This check should be here, since edge(r, v) doesn't have to exist
                    if (_e != null) {
                        // If the edge has a lower weight than the current minimum weight
                        if (_e.weight() < min) {
                            min = _e.weight();
                            edgeMin = _e;
                        }
                    }
                }

                // line 5 of algorithm 3
                // This check should be here, since r doesn't need to have outgoing edges, in which case edgeMin = null
                // and than T <- T union (r, v) remains just T and X <- X - {v} remains just X.
                if (edgeMin != null) {
                    T.addUniqueEdge(edgeMin);
                    X.remove(edgeMin.to());
                }
                // Always decrease k. It doesn't matter if there exists an (r, v).
                k--;
            }
        } else { // line 6 of algorithm 3
            // line 7 of algorithm 3
            while (k > 0) {
                // line 8 of algorithm 3
                TGraph T_best = new TGraph();
                float den = T_best.den(X);
                // line 9 of algorithm 3
                for (TVertex _v : graph.getVertices()) {
                    for (int k_accent = 1; k_accent <= k; k_accent++) {
                        TEdge _e = graph.getEdge(r, _v);

                        // If edge (r, v) doesn't exists, it makes no sense to check for further paths
                        if (_e != null) {
                            // line 10 of algorithm 3
                            TGraph T_accent = this.algorithm3(graph, i - 1, k_accent, _v, new ArrayList<>(X)); // X shouldn't be passed as reference
                            T_accent.addUniqueEdge(_e);

                            // line 11 of algorithm 3
                            float _d = T_accent.den(X);
                            if (den > _d) {
                                // line 12 of algorithm 3
                                den = _d;
                                T_best = T_accent;
                            }
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
                // If it turns out there are no outgoing edges for the given root, T_best is empty, but we still
                // need to decrease k. Otherwise the algorithm would be stuck forever.
                if (T_best.getVertices().isEmpty()) {
                    k--;
                }
            }
        }

        // line 14 of algorithm 3
        return T;
    }

    @Override
    public String toString() {
        return "Old algorithm";
    }
}
