package computation;

/**
 *
 * @author s155679
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import model.AbstractEdge;
import model.AbstractGraph;
import model.Graph;
import model.TemporalEdge;
import model.TemporalGraph;
import model.TemporalVertex;
import model.line_model;

/**
 * Created by ruudandriessen on 25/05/16.
 */
public class MSTa extends Algorithm {

    float ta, tw;
    int len = 0;
    TemporalEdge TEa, TEw, TEe;
    int[] pos;
    float[] a;
    Stack S;
    line_model lm;
    TemporalEdge present;
   HashMap<TemporalVertex, TemporalVertex> P;

    @Override
    public void run(AbstractGraph graph) {
        if (graph instanceof TemporalGraph) {
            TemporalGraph g = (TemporalGraph) graph;
        } else {
            throw new IllegalArgumentException("Cannot use MSTw without temporal graph");
        }
    }

    private  HashMap<TemporalVertex, TemporalVertex> MSTa(TemporalGraph graph, TemporalVertex root) {
        TEa = (TemporalEdge) graph.getVertex(1).getOutEdge(graph.getVertex(1));
        ta = TEa.start();
        lm.setTV1(root);
        lm.setTV2(root);
        lm.setTa(ta);                       //build <r,r,ta>
        while (graph.contains(len)) {
            pos[len] = 1;                   // pos(u) = 1
            a[len] = Integer.MAX_VALUE;     //A(u) = infinate
            len++;
            S.push(lm);             //push <r,r,ta> on stack S
        }
        TEa = (TemporalEdge) graph.getVertex(1).getOutEdge(graph.getVertex(1));
        ta = TEa.start();
        TEw = (TemporalEdge) graph.getVertex(len).getOutEdge(graph.getVertex(len));
        tw = TEw.start();
        // Here finishes initalization
        while (!S.empty()) {
            lm = (line_model) S.pop();          //top(s), Pop(s)
            if (lm.getta() < a[(int) lm.getTV2().getIdentifier()]) {    //tv<A(v)  
                a[(int) lm.getTV2().getIdentifier()] = lm.getta();      
                P.put(lm.getTV2(), lm.getTV1());                               //Adds(Changes) u to P(v)
                if (pos[(int) lm.getTV2().getIdentifier()] <= lm.getTV2().out.size()) {
                    present = (TemporalEdge) lm.getTV2().out.get(pos[(int) lm.getTV2().getIdentifier()]); // what is <v,v'>?
                    while ((pos[(int) lm.getTV2().getIdentifier()] <= lm.getTV2().out.size()) && (a[(int) lm.getTV2().getIdentifier()] <= present.start())) {
                        lm.setTV1(present.from());
                        lm.setTV2(present.to());
                        lm.setTa(present.end());
                        S.push(lm);                     // Push <v, v', tv>
                        pos[(int) lm.getTV2().getIdentifier()]++;   //pos[u]++
                        present = (TemporalEdge) lm.getTV2().out.get(pos[(int) lm.getTV2().getIdentifier()]);
                    }
                }
            }
        }
        return P;
    }

}
