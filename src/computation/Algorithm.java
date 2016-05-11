package computation;

import model.AbstractGraph;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public abstract class Algorithm {
    /**
     * Runs the algorithm given a certain graph
     * @param graph The graph to run the algorithm on
     */
    public abstract void run(AbstractGraph graph);
}
