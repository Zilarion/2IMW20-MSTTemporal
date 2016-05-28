package computation;

import model.*;
import dst.DST;

import java.util.List;

/**
 * Created by ruudandriessen on 25/05/16.
 */
public class MSTwNew extends Algorithm {
    @Override
    public void run(AbstractGraph graph) {
        if (graph instanceof TemporalGraph) {
            TemporalGraph g = (TemporalGraph) graph;
//            dst = transform(g)
//            huang(dst)
        } else {
            throw new IllegalArgumentException("Cannot use MSTw without temporal graph");
        }
    }

    /**
     * Calculates MSTw according to the improved algorithm proposed by Huang
     * @param k The number of covered terminals
     * @param i The height of the tree
     * @param r The root of the tree
     * @param X Terminal set
     */
    private DST huang(int k, int i, TemporalVertex r, List<TemporalVertex> X, TemporalGraph g) {
        DST T = new DST();
//        if (i == 1) {
//            while (k > 0) {
//                // (r,v) <- arg_(r,v) min cost(r, v) FORALL v in X
//                float minCost = Float.MAX_VALUE;
//                TemporalEdge minEdge = null; // (r,v)
//                for (TemporalVertex v : X) { // For all v : X get v with min cost
//                    TemporalEdge e = r.getOutEdge(v);
//                    if (e.weight() < minCost) {
//                        minEdge = e;
//                        minCost = e.weight();
//                    }
//                }
//
//                // add (r,v) to T
//                T.add(minEdge);
//
//                // k <- k - 1
//                k -= 1;
//
//                // remove v from x
//                X.remove(minEdge.to());
//            }
//        } else {
//            while (k > 0) {
//                // TBest <- empty density(TBest) = infinity
//                DST TBest = null;
//                float bestDensity = Float.MAX_VALUE;
//
//                // foreach vertex v in V do
//                for (TemporalVertex v : g.getVertices().values()) {
//                    // Get (r,v)
//                    TemporalEdge e = r.getOutEdge(v);
//                    // Call other algorithm
//                    DST TPrime = huangB(i-1, k, v, X, e);
//
//                    float TPrimeDensity = TPrime.density();
//                    if (bestDensity > TPrimeDensity) {
//                        bestDensity = TPrimeDensity;
//                    }
//
//                    T.merge(TBest);
//                    k = k - X.size() INTERSECT V(TBest);
//                    X.removeAll(TBest.vertices());
//                }
//            }
//        }
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
    private DST huangB(int i, int k, TemporalVertex r, List<TemporalVertex> X, TemporalEdge e) {
        DST dst = new DST();
        if (i == 1) {
            while (k > 0) {

            }
        } else {

        }
        return dst;
    }
}
