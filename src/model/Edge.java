package model;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class Edge extends AbstractEdge<Vertex> {

    /**
     * Creates a new edge given a from and to vertex
     * @param from The vertex this edge starts from
     * @param to The vertex this edge goes to
     */
    public Edge(Vertex from, Vertex to) {
        super(from, to);
    }
}
