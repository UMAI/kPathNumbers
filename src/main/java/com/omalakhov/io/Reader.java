package com.omalakhov.io;

import com.omalakhov.exception.TreeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Reader {
    public static Map<String, String> readTreeVertexParentMapping(String path) throws FileNotFoundException, TreeException {
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
                throw new TreeException(TreeException.Type.WRONG_INPUT_FILE_FORMAT);
            }
        }
        return vertexParent;
    }
}
