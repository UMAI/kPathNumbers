package com.omalakhov;

import com.omalakhov.data.Graph;
import com.omalakhov.data.Tree;
import com.omalakhov.data.Vertex;
import com.omalakhov.exception.ApplicationException;
import com.omalakhov.io.Reader;
import com.omalakhov.io.Writer;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.omalakhov.exception.ApplicationException.Type.*;

public class App {
	private enum Algorithm {
		TREE_K_PATH_VERTEX_COVER("treeKPVC", "Tree k-path vertex cover"),
		GRAPH_THREE_PATH_VERTEX_COVER("graph3PVC", "Graph 3-path vertex cover"),
		GRAPH_K_PATH_VERTEX_COVER_PRUNING("graphKPVC", "Pruning algorithm for k = {0}"),
		GRAPH_K_PATH_SEQUENCE_PRUNING("graphKPSeq", "Pruning algorithm for k = {0}");

		private final String cmdArg;
		private final String outputText;

		Algorithm(String cmdArg, String outputText) {
			this.cmdArg = cmdArg;
			this.outputText = outputText;
		}

		public static Algorithm getAlgorithm(String cmdArg) throws ApplicationException {
			for (Algorithm algorithm : values()) {
				if (algorithm.cmdArg.equals(cmdArg)) {
					return algorithm;
				}
			}
			throw new ApplicationException(ALGORITHM_NOT_FOUND, cmdArg);
		}

		public String getOutputText(String... params) {
			String outputTextTemplate = outputText;
			for (int i = 0; i < params.length; i++) {
				outputTextTemplate = outputTextTemplate.replace("{" + i + "}", params[i]);
			}
			return outputTextTemplate;
		}
	}

	private enum Option {
		ALGORITHM("-alg"),
		SORTED("-sorted"),
		K("-k"),
		INPUT_FILE("-input"),
		OUTPUT_FILE("-output");

		private final String optionFlag;

		Option(String optionFlag) {
			this.optionFlag = optionFlag;
		}

		public static Option getOption(String optionFlag) throws ApplicationException {
			for (Option option : values()) {
				if (option.optionFlag.equals(optionFlag)) {
					return option;
				}
			}
			throw new ApplicationException(COMMAND_LINE_OPTION_INVALID, optionFlag);
		}
	}

	public static void main(String[] args) throws FileNotFoundException, ApplicationException {
		Algorithm algorithm = Algorithm.GRAPH_K_PATH_SEQUENCE_PRUNING;
		List<Integer> kValues = Collections.singletonList(1);
		boolean sorted = true;
		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				Option option = Option.getOption(args[i]);
				switch (option) {
					case INPUT_FILE:
						Reader.getInstance().open(args[i + 1]);
						break;
					case OUTPUT_FILE:
						Writer.getInstance().open(args[i + 1]);
						break;
					case ALGORITHM:
						algorithm = Algorithm.getAlgorithm(args[i + 1]);
						break;
					case SORTED:
						sorted = Boolean.parseBoolean(args[i + 1]);
						break;
					case K:
						kValues = Arrays.stream(args[i + 1].split(",")).map(Integer::valueOf).collect(Collectors.toList());
				}
			}
		}
		switch (algorithm) {
			case TREE_K_PATH_VERTEX_COVER:
				runTreeKPathVertexCover(kValues.get(0));
				break;
			case GRAPH_THREE_PATH_VERTEX_COVER:
				runGraph3PathExample();
				break;
			case GRAPH_K_PATH_VERTEX_COVER_PRUNING:
				runGraphKPathPruningExample(kValues.get(0), sorted);
				break;
			case GRAPH_K_PATH_SEQUENCE_PRUNING:
				runGraphPathSequencePruningExample(kValues, sorted);
		}
	}

	private static void runTreeKPathVertexCover(int k) throws ApplicationException {
		Map<String, String> treeVertexParentMapping = Reader.getInstance().readTreeVertexParentMapping();
		Tree tree = new Tree(treeVertexParentMapping);
		long startTime = System.currentTimeMillis();
		Set<Tree> kPathVertexCover = KPathVertexCoverFinder.getInstance().findTreeKPathVertexCover(tree, k);
		long runningTime = System.currentTimeMillis() - startTime;
		Writer.getInstance().printVertexCover(Algorithm.TREE_K_PATH_VERTEX_COVER.getOutputText(), runningTime, kPathVertexCover, treeVertexParentMapping.size());
	}

	private static void runGraph3PathExample() throws ApplicationException {
		Graph graph = new Graph(Reader.getInstance().readGraphEdges());
		int graphSize = graph.getNumberOfVertices();
		long startTime = System.currentTimeMillis();
		Set<Vertex> threePathVertexCover = KPathVertexCoverFinder.getInstance().findGraph3PathVertexCover(graph);
		long runningTime = System.currentTimeMillis() - startTime;
		Writer.getInstance().printVertexCover(Algorithm.GRAPH_THREE_PATH_VERTEX_COVER.getOutputText(), runningTime, threePathVertexCover, graphSize);
	}

	private static void runGraphKPathPruningExample(int k, boolean sorted) throws ApplicationException {
		Graph graph = new Graph(Reader.getInstance().readGraphEdges());
		int graphSize = graph.getNumberOfVertices();
		long startTime = System.currentTimeMillis();
		Set<Vertex> initialVertexCover = sorted ? new TreeSet<>(graph.getVertices()) : new HashSet<>(graph.getVertices());
		Set<Vertex> kPathVertexCover = KPathVertexCoverFinder.getInstance().findGraphKPathVertexCoverPruning(initialVertexCover, k);
		long runningTime = System.currentTimeMillis() - startTime;
		Writer.getInstance().printVertexCover(Algorithm.GRAPH_K_PATH_VERTEX_COVER_PRUNING.getOutputText(String.valueOf(k)), runningTime, kPathVertexCover, graphSize);
	}

	private static void runGraphPathSequencePruningExample(List<Integer> kValues, boolean sorted) throws ApplicationException {
		Graph graph = new Graph(Reader.getInstance().readGraphEdges());
		int graphSize = graph.getNumberOfVertices();
		Set<Vertex> prevVertexCover = sorted ? new TreeSet<>(graph.getVertices()) : new HashSet<>(graph.getVertices());
		long startTime = System.currentTimeMillis();
		for (Integer k : kValues) {
			Set<Vertex> iPathVertexCover = KPathVertexCoverFinder.getInstance().findGraphKPathVertexCoverPruning(prevVertexCover, k);
			long runningTime = System.currentTimeMillis() - startTime;
			Writer.getInstance().printVertexCover(Algorithm.GRAPH_K_PATH_SEQUENCE_PRUNING.getOutputText(String.valueOf(k)), runningTime, iPathVertexCover, graphSize);
			prevVertexCover = sorted ? new TreeSet<>(iPathVertexCover) : new HashSet<>(iPathVertexCover);
		}
	}
}
