package computation;

import model.*;
import treemodel.Node;
import treemodel.Tree;

import java.util.List;

/**
 * Created by ruudandriessen on 25/05/16.
 */
public class MSTwNew extends Algorithm {
    @Override
    public void run(AbstractGraph graph) {
        if (graph instanceof TemporalGraph) {
            // ok
        } else {
            throw new IllegalArgumentException("Cannot use MSTw without temporal graph");
        }
    }

    /**
     * Calculates MSTw according to the improved algorithm proposed by Huang
     * @param k The number of covered terminals
     * @param i The height of the tree
     * @param root The root of the tree
     * @param X Terminal set
     */
    private void huang(int k, int i, Vertex r, List<AbstractVertex> X, Graph g) {
        Tree T = new Tree();
        if (i == 1) {
            while (k > 0) {
                // (r,v) <- arg_(r,v) min cost(r, v) FORALL v in X
                float minCost = Float.MAX_VALUE;
                AbstractEdge minEdge = null; // (r,v)
                for (AbstractVertex v : X) { // For all v : X get v with min cost
                    AbstractEdge e = r.getOutEdge(v);
                    if (e.weight() < minCost) {
                        minEdge = e;
                        minCost = e.weight();
                    }
                }

                // add (r,v) to T
                T.add(minEdge);

                // k <- k - 1
                k -= 1;

                // remove v from x
                X.remove(minEdge.to());
            }
        } else {
            while (k > 0) {
                // TBest <- empty density(TBest) = infinity
                Tree TBest = null;
                float bestDensity = Float.MAX_VALUE;

                // foreach vertex v in V do
                for (AbstractVertex v : g.getVertices().values()) {
                    // Get (r,v)
                    AbstractEdge e = r.getOutEdge(v);
                    // Call other algorithm
                    Tree TPrime = huangB(i-1, k, v, X, e);

                    float TPrimeDensity = TPrime.density();
                    if (bestDensity > TPrimeDensity) {
                        bestDensity = TPrimeDensity;
                    }

                    T.merge(TBest);
                    k = k - X.size() INTERSECT V(TBest);
                    X.removeAll(TBest.vertices());
                }
            }
        }
        return T;
    }

    /**
     *
     * @param i Level number
     * @param k Maximum number of available terminals
     * @param r Tree root
     * @param X Terminal set
     * @param e The incoming edge of r
     * @return : A tree T with height i rooted at r covering at most k terminals in X so that the density of T U e is the smallest
     */
    private void huangB(int i, int k, Vertex r, List<AbstractVertex> X, AbstractEdge e) {
        if (i == 1) {
            while (k > 0) {

            }
        } else {

        }
    }
}
