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
        System.out.println(this.algorithm3(transformed, 1));

        // do postprocessing (page 424)

    }

    public TGraph algorithm3(TGraph graph, int i) {
        TGraph T = new TGraph();

        int k = graph.terminals.size();
        List<TVertex> X = new ArrayList<>(graph.terminals);
        TVertex r = graph.root;
        T.addVertex(r);

        if (i == 1) {
            while (k > 0) {
                TVertex vertexMin = X.get(0);
                TEdge edgeMin = null;
                int min = Transform.infinity;
                for (TVertex _v : X) {
                    for (TEdge _e : _v.in()) {
                        if (_e.from() == r) {
                            if (_e.weight() < min) {
                                min = _e.weight();
                                vertexMin = _v;
                                edgeMin = _e;
                            }
                        }
                    }
                }
                if (edgeMin != null) {
                    T.addVertex(vertexMin);
                    T.addEdge(edgeMin);
                    k--;
                    X.remove(vertexMin);
                }
            }
        } else {

        }

        return T;
    }

}
