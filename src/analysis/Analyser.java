package analysis;

import computation.Algorithm;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import computation.MSTwNew;
import computation.MSTwOld;
import model.AbstractGraph;
import model.Graph;
import model.TemporalGraph;
import model.TemporalVertex;
import parsing.Parser;
import transform.TGraph;
import transform.Transform;

import javax.swing.*;

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

    private long time = 0;
    private final File output;
    private final File[] files;
    private final Parser parser;
    private final Algorithm[] algo = new Algorithm[2];

    public Analyser(File folder) {
        files = folder.listFiles();
        parser = new Parser();
        output = new File(folder.getParentFile().toURI().resolve("output.txt"));
        if (output.exists()) {
            output.delete();
        }
        try {
            output.createNewFile();
        } catch (IOException e) {
        }
        algo[0] = new MSTwOld();
        algo[1] = new MSTwNew();
        System.out.println("Selected folder: " + folder);
    }

    private void writeOutput(Object o) {
        String s = o.toString();
        s += "\n";
        try {
            Files.write(Paths.get(this.output.getPath()), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
        }
    }

    private void startTime() {
        time = System.currentTimeMillis();
    }

    private long time() {
        long t = System.currentTimeMillis() - time;
        time = System.currentTimeMillis();
        return t;
    }

    private void analyse() {
        for (File f : files) {
            writeOutput("Start analysing file: " + f);
            System.out.println("Start analysing file: " + f);
            for (Algorithm a : algo) {
                for (int i : new int[]{1, 2}) {
                    writeOutput(a);
                    writeOutput(i);
                    startTime();
                    TemporalGraph g = parser.parse(f, 0, 0, 1, 2, 3, 4);
                    writeOutput(g);
                    writeOutput("Graph contains " + g.getVertices().size() + " vertices and " + g.edges().size() + " edges");
                    writeOutput("Parsing took: " + time());

                    // Assume interval to be [0, inf) and get root
                    ArrayList<Long> vKeys = new ArrayList<>(g.getVertices().keySet());
                    Collections.sort(vKeys);
                    TemporalVertex root = g.getVertex(vKeys.get(0));

                    // Transform graph (page 423)
                    startTime();
                    TGraph transformed = Transform.transform(g, root);
                    writeOutput("Creating transformed graph took: " + time());

                    // Create transitive closure
                    TGraph output = Transform.createTransitiveClosure(transformed);
                    writeOutput("Creating transitive closure took: " + time());

                    output = a.run(g, transformed, output, i);
                    writeOutput("Running algorithm took: " + time());

                    // do postprocessing (page 424)
                    writeOutput(Transform.doPostProcessing(g, transformed, output));
                    writeOutput("Postprocessing took: " + time());

                }
            }
        }
    }

    public static void main(String... args) {
        try {
            EventQueue.invokeAndWait(() -> {
                // Choose folder in which the data files are..
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose the folder with data sets");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    Analyser analyser = new Analyser(chooser.getSelectedFile());
                    analyser.analyse();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
