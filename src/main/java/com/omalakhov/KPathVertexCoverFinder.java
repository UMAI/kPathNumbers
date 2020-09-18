package com.omalakhov;

import com.omalakhov.data.Graph;
import com.omalakhov.data.TreeNode;
import com.omalakhov.data.Vertex;

import java.util.*;

public class KPathVertexCoverFinder {
	private static KPathVertexCoverFinder instance;

	private KPathVertexCoverFinder() {}

	public static KPathVertexCoverFinder getInstance() {
		if (instance == null) {
			instance = new KPathVertexCoverFinder();
		}
		return instance;
	}

	public Set<TreeNode> findTreeKPathVertexCoverIterative(TreeNode root, int k) {
		Set<TreeNode> kPathVertexCover = new HashSet<>();
		Stack<TreeNode> tmpStack = new Stack<>();
		Stack<TreeNode> verticesPostorder = new Stack<>();
		tmpStack.push(root);
		while (!tmpStack.isEmpty()) {
			TreeNode currentVertex = tmpStack.pop();
			if (currentVertex.getChildren() != null && !currentVertex.getChildren().isEmpty()) {
				currentVertex.getChildren().forEach(tmpStack::push);
			}
			verticesPostorder.push(currentVertex);
		}
		while (!verticesPostorder.isEmpty()) {
			TreeNode currentVertex = verticesPostorder.pop();
			if (currentVertex.subtreeContainsPathOfLength(k)) {
				kPathVertexCover.add(currentVertex);
				if (currentVertex.getParent() != null) {
					currentVertex.getParent().removeChild(currentVertex);
				}
				else {
					currentVertex.getChildren().clear();
				}
			}
		}
		return kPathVertexCover;
	}

	private void recursivelyAddVerticesToKPathVertexCover(Set<TreeNode> kPathVertexCover, TreeNode currentVertex, int k) {
		if (currentVertex.getChildren() != null && !currentVertex.getChildren().isEmpty()) {
			List<TreeNode> currentVertexChildren = new ArrayList<>(currentVertex.getChildren());
			currentVertexChildren.forEach(child -> recursivelyAddVerticesToKPathVertexCover(kPathVertexCover, child, k));
		}
		if (currentVertex.subtreeContainsPathOfLength(k)) {
			kPathVertexCover.add(currentVertex);
			if (currentVertex.getParent() != null) {
				currentVertex.getParent().removeChild(currentVertex);
			}
			else {
				currentVertex.getChildren().clear();
			}
		}
	}

	public Set<Vertex> findGraph3PathVertexCover(Graph graph) {
		Set<Vertex> threePathVertexCover = new HashSet<>();
		addAtLeastKConnectedVerticesTo3PathVertexCover(graph, threePathVertexCover, 4);
		addLooselyConnectedVerticesTo3PathVertexCover(graph, threePathVertexCover);
		return threePathVertexCover;
	}

	private void addAtLeastKConnectedVerticesTo3PathVertexCover(Graph graph, Set<Vertex> threePathVertexCover, int k) {
		Set<Vertex> vertices = new TreeSet<>(graph.getVertices());
		vertices.stream().filter(vertex -> vertex.getDegree() >= k).forEach(vertex -> {
			threePathVertexCover.add(vertex);
			graph.removeVertex(vertex);
		});
	}

	private void addLooselyConnectedVerticesTo3PathVertexCover(Graph graph, Set<Vertex> threePathVertexCover) {
		if (graph.getNumberOfEdges() <= graph.getNumberOfVertices()) {
			addAtLeastKConnectedVerticesTo3PathVertexCover(graph, threePathVertexCover, 2);
		}
		else {
			addLooselyConnectedSubGraphTo3PathVertexCover(graph, threePathVertexCover);
		}
	}

	private void addLooselyConnectedSubGraphTo3PathVertexCover(Graph graph, Set<Vertex> threePathVertexCover) {
		List<Vertex> vertexList = new ArrayList<>(graph.getVertices());
		Set<Vertex> set1 = new HashSet<>(vertexList.subList(0, graph.getNumberOfVertices() / 2));
		Set<Vertex> set2 = new HashSet<>(vertexList.subList(graph.getNumberOfVertices() / 2, graph.getNumberOfVertices()));
		Optional<Vertex> vertexToMove;
		do {
			vertexToMove = set1.stream().filter(vertex -> vertex.getDegreeInInducedSubGraph(set1) > 1).findAny();
			if (vertexToMove.isPresent()) {
				set1.remove(vertexToMove.get());
				set2.add(vertexToMove.get());
			}
			else {
				vertexToMove = set2.stream().filter(vertex -> vertex.getDegreeInInducedSubGraph(set2) > 1).findAny();
				if (vertexToMove.isPresent()) {
					set2.remove(vertexToMove.get());
					set1.add(vertexToMove.get());
				}
			}
		}
		while (vertexToMove.isPresent());
		if (set1.size() <= set2.size()) {
			threePathVertexCover.addAll(set1);
		}
		else {
			threePathVertexCover.addAll(set2);
		}
	}

	public Set<Vertex> findGraphKPathVertexCoverPruning(Set<Vertex> initialVertexCover, int k) {
		Set<Vertex> kPathVertexCover = new HashSet<>(initialVertexCover);
		for (Vertex vertex : initialVertexCover) {
			if (!vertex.isNecessary(kPathVertexCover, k)) {
				kPathVertexCover.remove(vertex);
			}
		}
		return kPathVertexCover;
	}
}
