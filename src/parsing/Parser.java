package parsing;

import computation.MSTwNew;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/**
 * Created by ruudandriessen on 11/05/16.
 */
public class Parser {

    /**
     * Parse non-temporal data.
     *
     * @param file         File containing data.
     * @param skip         Number of lines to skip.
     * @param fromColumn   Column number of 'from' edge.
     * @param toColumn     Column number of 'to' edge.
     * @param weightColumn Column number of containing the weight of the edge.
     */
    public Graph parse(File file, int skip, int fromColumn, int toColumn, int weightColumn) {
        Graph graph = new Graph();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            for (int i = 0; i < skip; i++) {
                line = br.readLine();
            }
            while ((line = br.readLine()) != null) {
                String[] data = line.trim().split("\\s+");
                long from = Long.parseLong(data[fromColumn]);
                long to = Long.parseLong(data[toColumn]);
                int weight = 0;
                if (weightColumn != -1) {
                    weight = Integer.parseInt(data[weightColumn]);
                }

                Vertex fromV, toV;
                // Create vertex if it doesn't exist yet
                if (!graph.contains(from)) {
                    graph.addVertex(from);

                }
                fromV = graph.getVertex(from);

                // Create vertex if it doesn't exist yet
                if (!graph.contains(to)) {
                    graph.addVertex(to);
                }
                toV = graph.getVertex(to);

                // Make edge aware of vertices and weight
                Edge edge = new Edge(fromV, toV);
                edge.setWeight(weight);

                // Make vertices aware of the edge between them
                fromV.addOutEdge(edge);
                toV.addInEdge(edge);
                graph.addEdge(edge);
            }
        } catch (IOException ex) {
        }
        return graph;
    }

    /**
     * Parse temporal data.
     *
     * @param file           File containing data.
     * @param skip           Number of lines to skip.
     * @param fromColumn     Column number of 'from' edge.
     * @param toColumn       Column number of 'to' edge.
     * @param weightColumn   Column number of containing the weight of the edge.
     * @param fromTimeColumn Column number containing the start time.
     * @param toTimeColumn   Column number containing the end time.
     */
    public TemporalGraph parse(File file, int skip, int fromColumn, int toColumn, int weightColumn, int fromTimeColumn, int toTimeColumn) {
        TemporalGraph graph = new TemporalGraph();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            for (int i = 0; i < skip; i++) {
                line = br.readLine();
            }
            while ((line = br.readLine()) != null) {
                String[] data = line.trim().split("\\s+");
                long from = Long.parseLong(data[fromColumn]);
                long to = Long.parseLong(data[toColumn]);
                int weight = 0;
                if (weightColumn != -1) {
                    weight = Integer.parseInt(data[weightColumn]);
                }

                TemporalVertex fromV, toV;
                // Create vertex if it doesn't exist yet
                if (!graph.contains(from)) {
                    graph.addVertex(from);

                }
                fromV = graph.getVertex(from);

                // Create vertex if it doesn't exist yet
                if (!graph.contains(to)) {
                    graph.addVertex(to);
                }
                toV = graph.getVertex(to);

                // Make edge aware of vertices, times and weight
                TemporalEdge edge = new TemporalEdge(fromV, toV);
                long startTime = 0, endTime = 0;
                if (fromTimeColumn != -1) {
                    startTime = Long.parseLong(data[fromTimeColumn]);
                } else {
                    startTime = Long.parseLong(data[toTimeColumn]);
                }
                endTime = Long.parseLong(data[toTimeColumn]);
                edge.setStart(startTime);
                edge.setEnd(endTime);
                edge.setWeight(weight);

                // Make vertices aware of the edge between them
                fromV.addOutEdge(edge);
                toV.addInEdge(edge);
                graph.addEdge(edge);
            }
        } catch (IOException ex) {
        }
        return graph;
    }
}
