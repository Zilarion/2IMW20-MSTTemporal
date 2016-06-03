package computation;

import model.AbstractGraph;
import model.TemporalGraph;
import transform.TEdge;
import transform.TGraph;
import transform.TVertex;
import transform.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruudandriessen on 25/05/16.
 */
public class MSTwNew extends Algorithm {
    @Override
    public void run(AbstractGraph graph) {
        if (graph instanceof TemporalGraph) {
            TemporalGraph g = (TemporalGraph) graph;

            int index = findRoot(g);
            TGraph T = Transform.transform(g, g.getVertex(index));
            Transform.createTransitiveClosure(T);


            ArrayList<TVertex> X = T.terminals();
            int k = X.size();

            System.out.println("--------------");
            System.out.println("Running huang i=1");
            TGraph result = huang(1, k, T.root, X, T);
            System.out.println(result);
            System.out.println("--------------");
            System.out.println("Running huang i=2");
            huang(2, k, T.root, X, T);
        } else {
            throw new IllegalArgumentException("Cannot use MSTw without temporal graph");
        }
    }

    private int findRoot(TemporalGraph g) {
        for (int i = 0; i < g.getVertices().size(); i++) {
            if (g.getVertex(i) != null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculates MSTw according to the improved algorithm proposed by Huang
     * @param k The number of covered terminals
     * @param i The height of the tree
     * @param r The root of the tree
     * @param X Terminal set
     */
    private TGraph huang(int i, int k, TVertex r, List<TVertex> X, TGraph g) {
        TGraph T = new TGraph();
        if (i == 1) {
            while (k > 0) {
                // (r,v) <- arg_(r,v) min cost(r, v) FORALL v in X
                TEdge minEdge = minCost(X, r);

                // add (r,v) to T
                T.addEdge(minEdge);

                // k <- k - 1
                k -= 1;

                // remove v from x
                X.remove(minEdge.to());
            }
        } else {
            while (k > 0) {
                // TBest <- empty density(TBest) = infinity
                TGraph TBest = null;
                float bestDensity = Float.MAX_VALUE;

                // foreach vertex v in V do
                for (TVertex v : g.getVertices()) {
                    // Get (r,v)
                    TEdge e = r.getOutEdge(v);
                    // Call other algorithm
                    TGraph TPrime = huangB(i-1, k, v, X, e);

                    float TPrimeDensity = TPrime.density();
                    if (bestDensity > TPrimeDensity) {
                        bestDensity = TPrimeDensity;
                    }

                    // T = T union Tbest
                    T.merge(TBest);
                    // X intersection V(Tbest) and X <- X intersection V(Tbest)
                    X.retainAll(TBest.getVertices());

                    // k <- k - V(Tbest)
                    k = k - TBest.getVertices().size();
                }
            }
        }
        return T;
    }

    /**
     *
     * @param i Level number
     * @param k Maximum number of available terminals
     * @param r DST root
     * @param X Terminal set
     * @param e The incoming edge of r
     * @return : A tree T with height i rooted at r covering at most k terminals in X so that the density of T U e is the smallest
     */
    private TGraph huangB(int i, int k, TVertex r, List<TVertex> X, TEdge e) {
        TGraph dst = new TGraph();
        if (i == 1) {
            while (k > 0) {
                // (r,v) <- arg_(r,v) min cost(r, v) FORALL v in X
                TEdge minEdge = minCost(X, r);

            }
        } else {

        }
        return dst;
    }

    private TEdge minCost(List<TVertex> X, TVertex r) {
        // (r,v) <- arg_(r,v) min cost(r, v) FORALL v in X
        float minCost = Float.MAX_VALUE;
        TEdge minEdge = null; // (r,v)
        for (TVertex v : X) { // For all v : X get v with min cost
            TEdge e = r.getOutEdge(v);
            if (e.weight() < minCost) {
                minEdge = e;
                minCost = e.weight();
            }
        }
        return minEdge;
    }
}
