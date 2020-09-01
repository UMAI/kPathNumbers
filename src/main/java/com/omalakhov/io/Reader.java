package com.omalakhov.io;

import com.omalakhov.data.Edge;
import com.omalakhov.exception.ApplicationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Reader {
    public static Map<String, String> readTreeVertexParentMapping(String path) throws FileNotFoundException, ApplicationException {
        Map<String, String> vertexParent = new HashMap<>();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] vertexParentEntry = line.trim().replaceAll(" +", " ").split(" ");
            if (vertexParentEntry.length == 1) {
                vertexParent.put(vertexParentEntry[0], null);
            }
            else if (vertexParentEntry.length == 2) {
                vertexParent.put(vertexParentEntry[0], vertexParentEntry[1]);
            }
            else {
                throw new ApplicationException(ApplicationException.Type.WRONG_INPUT_FILE_FORMAT);
            }
        }
        return vertexParent;
    }

    public static Set<Edge> readGraphEdges(String path) throws FileNotFoundException, ApplicationException {
        Set<Edge> edges = new HashSet<>();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] edge = line.trim().replaceAll(" +", " ").split(" ");
            if (edge.length == 2) {
                edges.add(new Edge(edge[0], edge[1]));
            }
            else {
                throw new ApplicationException(ApplicationException.Type.WRONG_INPUT_FILE_FORMAT);
            }
        }
        return edges;
    }
}
