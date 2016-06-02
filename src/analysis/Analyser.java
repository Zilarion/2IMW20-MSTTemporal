package analysis;

import computation.Algorithm;
import java.time.Clock;
import java.util.ArrayList;
import model.AbstractGraph;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class Analyser {

    /**
     * Analyses the results of a given algorithm using a certain graph
     *
     * @param alg The algorithm to use
     * @param graph The graph to analyse
     */
    long pretime, runtime;
    private Clock clock;

    public void analyse(ArrayList<Algorithm> alg, ArrayList<AbstractGraph> graph) {

        for (Algorithm instalo : alg) {
            for (AbstractGraph instgraph : graph) {
                pretime = clock.millis();
                instalo.run(instgraph);
                runtime = clock.millis() - pretime;
                System.out.println("Runtime of Algorithm" + instalo + "on the graph" + instgraph + "is" + runtime + "miliseconds");
            }
        }
    }
}
