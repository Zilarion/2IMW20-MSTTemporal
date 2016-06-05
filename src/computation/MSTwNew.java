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

//            System.out.println("--------------");
//            System.out.println("Running huang i=1");
//            TGraph result = huang(1, k, T.root, new ArrayList<>(X), T);
//            System.out.println(result);
//            System.out.println("--------------");
            System.out.println("Running huang i=3");
            TGraph result = huang(3, k, T.root, new ArrayList<>(X), T);
            System.out.println("---- Final result ----");
            System.out.println(result);
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
     * @param G The graph
     */
    private TGraph huang(int i, int k, TVertex r, List<TVertex> X, TGraph G) {
        TGraph T = new TGraph(); // line 1
        System.out.println("Huang: " + i);
        if (i == 1) { // line 2
            while (k > 0) { // line 3
                // line 4, (r,v) <- arg_(r,v) min cost(r, v) FOR ALL v in X
                TEdge minEdge = minCost(G, X, r);

                // line 5, k <- k - 1
                k--;

                if (minEdge != null) {
                    // line 5, add (r,v) to T
                    T.addUniqueEdge(minEdge);

                    // line 5, remove v from x
                    X.remove(minEdge.to());
                }
            }
        } else { // line 6
            while (k > 0) { // line 7
                // line 8, TBest <- empty density(TBest) = infinity
                TGraph TBest = new TGraph();
                float bestDensity = Float.MAX_VALUE;

                // line 9, foreach vertex v in V do
                for (TVertex v : G.getVertices()) {
                    // line 10, Get (r,v)
                    TEdge e = G.getEdge(r, v);

                    if (e == null) continue;

                    // line 10, Call other algorithm
                    TGraph TPrime = huangB(i-1, k, v, new ArrayList<>(X), e, G);

                    // line 10, TPrime union (r,v)
                    TPrime.addUniqueEdge(e);

                    // line 11
                    float TPrimeDensity = TPrime.den(X);
                    if (bestDensity > TPrimeDensity) {
                        // line 12
                        bestDensity = TPrimeDensity;
                        TBest = TPrime;
                    }
                }

                // Line 13, T = T union Tbest
                T.merge(TBest);

                // Line 13, Calculate X intersection V(TBest)
                List<TVertex> XIntersectTBest = new ArrayList<>(X);
                XIntersectTBest.retainAll(TBest.getVertices());

                // Line 13, k <- k - |X intersection V(Tbest)|
                k = k - XIntersectTBest.size();

                // Line 13, X <- X - V(Tbest)
                X.removeAll(TBest.getVertices());

                if (TBest.getVertices().isEmpty()) {
                    k--;
                }
            }
        }
        // line 14
        return T;
    }

    /**
     * Algorithm 5 in the huang paper
     * @param i Level number
     * @param k Maximum number of available terminals
     * @param r DST root
     * @param X Terminal set
     * @param e The incoming edge of r
     * @param G The graph
     * @return : A tree T with height i rooted at r covering at most k terminals in X so that the density of T U e is the smallest
     */
    private TGraph huangB(int i, int k, TVertex r, List<TVertex> X, TEdge e, TGraph G) {
        TGraph T = new TGraph(), TC = new TGraph(); // Line 1

        System.out.println("-----");
        System.out.println("HuangB_start: " + i);
        if (i == 1) { // Line 2
            while (k > 0) { // Line 3
                // Line 4, (r,v) <- arg_(r,v) min cost(r, v) FOR ALL v in X
                TEdge minEdge = minCost(G, X, r);

                if (minEdge != null) {
                    // Line 5, Tc <-  Tc union (r,v)
                    TC.addUniqueEdge(minEdge);
                }

                // Line 5, k <- k - 1
                k--;

                //Line 6, if den(T union e) > den(Tc union e)
                if (T.den(X) > TC.den(X)) {
                    T = TC; // Line 7
                }

                if (minEdge != null) {
                    // Line 5, X <- X - {v}
                    X.remove(minEdge.to());
                }
            }
        } else { // Line 8
            while (k > 0) { // Line 9
                // Line 10, TBest <- empty, den(TBest) <- infinity
                TGraph TBest = new TGraph();

                // Line 11, For every vertex v in V do
                for (TVertex v : G.getVertices()) {
                    // Line 12, Get rv
                    TEdge rv = r.getOutEdge(v);

                    if (rv == null) continue;

                    // Line 12, T' <- Bi-1
                    TGraph TPrime = huangB(i - 1, k, v, new ArrayList<>(X), rv, G);
                    // Line 12, T' union (r,v)
                    TPrime.addUniqueEdge(rv);

                    // Line 13, if den(Tbest) > den(T')
                    if (TBest.den(X) > TPrime.den(X)) {
                        // Line 14, Tbest <- T'
                        TBest = TPrime;
                    }
                }

                // Line 15, Tc <- Tc union Tbest
                TC.merge(TBest);

                // Line 15, Calculate X intersection V(Tbest)
                List<TVertex> XIntersectTBest = new ArrayList<>(X);
                XIntersectTBest.retainAll(TBest.getVertices());

                // Line 15, k <- k - |X intersection V(Tbest)|
                k = k - XIntersectTBest.size();

                // Line 16
                if (T.den(X) > TC.den(X)) {
                    // Line 17
                    T = TC;
                }

                // Line 15, X <- X - V(Tbest)
                X.removeAll(TBest.getVertices());

                if (TBest.getVertices().isEmpty()) {
                    k--;
                }
            }
        }
        // Line 18
        System.out.println("HuangB_end: " + i);
        System.out.println(T);
        return T;
    }

    /**
     * Gets the minimum cost edge from r to any v in X
     * @param g The graph to use
     * @param X The set of vertices to get the minimum cost to
     * @param r The vertex to find an edge from
     * @return The minimum cost edge if one exists, null otherwise
     */
    private TEdge minCost(TGraph g, List<TVertex> X, TVertex r) {
        // (r,v) <- arg_(r,v) min cost(r, v) FOR ALL v in X
        float minCost = Float.MAX_VALUE;
        TEdge minEdge = null; // (r,v)
        for (TVertex v : X) { // For all v : X get v with min cost
            TEdge e = g.getEdge(r, v);
            if (e != null && e.weight() < minCost) {
                minEdge = e;
                minCost = e.weight();
            }
        }
        return minEdge;
    }
}
