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
    private Graph parse(File file, int skip, int fromColumn, int toColumn, int weightColumn) {
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
    private TemporalGraph parse(File file, int skip, int fromColumn, int toColumn, int weightColumn, int fromTimeColumn, int toTimeColumn) {
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

    public static void main(String... args) {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // Choose the file
                    Parser parser = new Parser();
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Choose the file containing the data");
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        System.out.println("--EXAMPLE INPUT--");
                        try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                            String line;
                            for (int i = 0; i < 5 && (line = br.readLine()) != null; i++) {
                                System.out.println(line);
                            }
                        } catch (IOException ex) {
                        }
                        System.out.println("--EXAMPLE INPUT--\n");

                        // Get user input
                        Scanner scan = new Scanner(System.in);
                        System.out.print("How many lines to skip: ");
                        int skip = scan.nextInt();
                        System.out.print("Column containing 'from' edge: ");
                        int from = scan.nextInt();
                        System.out.print("Column containing 'to' edge: ");
                        int to = scan.nextInt();
                        System.out.print("Column containing the weight of the edge (-1 unweighted): ");
                        int weight = scan.nextInt();
                        System.out.print("Temporal graph? (Y/N): ");
                        if ("Y".equals(scan.next())) {
                            System.out.print("Column containing the start time (-1 unknown): ");
                            int start = scan.nextInt();
                            System.out.print("Column containing the end time: ");
                            int end = scan.nextInt();
                            TemporalGraph tg = parser.parse(chooser.getSelectedFile(), skip, from, to, weight, start, end);
                            new MSTwNew().run(tg);
                        } else {
                            System.out.println(parser.parse(chooser.getSelectedFile(), skip, from, to, weight));
                        }
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
