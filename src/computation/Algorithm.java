package computation;

import model.TemporalGraph;
import model.TemporalVertex;
import transform.TGraph;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public abstract class Algorithm {
    /**
     * Runs the algorithm given a certain graph
     * @param graph The graph to run the algorithm on
     */
    public abstract TGraph run(TemporalGraph graph, TGraph transformed, TGraph closure, int i);
}
