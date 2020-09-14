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

	public Set<TreeNode> findTreeKPathVertexCover(TreeNode root, int k) {
		Set<TreeNode> kPathVertexCover = new HashSet<>();
		while (root.containsProperlyRootedSubTree(k)) {
			kPathVertexCover.add(findNextVertexForTreeKPathVertexCover(root, k));
			root.recalculateHeight();
		}
		return kPathVertexCover;
	}

	private TreeNode findNextVertexForTreeKPathVertexCover(TreeNode treeNode, int k) {
		Optional<TreeNode> subTreeContainingProperlyRootedSubTree = treeNode
				.getChildren()
				.stream()
				.filter(child -> child.containsProperlyRootedSubTree(k))
				.findAny();
		if (subTreeContainingProperlyRootedSubTree.isPresent()) {
			return findNextVertexForTreeKPathVertexCover(subTreeContainingProperlyRootedSubTree.get(), k);
		}
		else {
			if (treeNode.getParent() != null) {
				treeNode.getParent().getChildren().removeIf(treeNode::equals);
			}
			else {
				treeNode.getChildren().clear();
			}
			return treeNode;
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
