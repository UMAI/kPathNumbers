package com.omalakhov.io;

import com.omalakhov.data.Edge;
import com.omalakhov.exception.ApplicationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Reader {
	private static Reader instance;
	private Scanner scanner;

	private Reader() {}

	public static Reader getInstance() {
		if (instance == null) {
			instance = new Reader();
		}
		return instance;
	}

	public void open(String inputFileName) throws FileNotFoundException {
		scanner = new Scanner(new File(inputFileName));
	}

	public void close() {
		scanner.close();
	}

	public Map<String, String> readTreeVertexParentMapping() throws ApplicationException {
		Map<String, String> vertexParent = new HashMap<>();
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

	public Set<Edge> readGraphEdges() throws ApplicationException {
		Set<Edge> edges = new HashSet<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] edge = line.trim().replaceAll(" +", " ").split(" ");
			if (edge.length == 2) {
				edges.add(new Edge(edge[0], edge[1]));
			}
			else {
				edge = line.trim().split("\t");
				if (edge.length == 2) {
					edges.add(new Edge(edge[0], edge[1]));
				}
				else {
					throw new ApplicationException(ApplicationException.Type.WRONG_INPUT_FILE_FORMAT);
				}
			}
		}
		return edges;
	}
}
