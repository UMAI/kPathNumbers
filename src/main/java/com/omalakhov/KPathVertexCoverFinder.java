package com.omalakhov;

import com.omalakhov.data.Graph;
import com.omalakhov.data.Tree;
import com.omalakhov.data.Vertex;

import java.util.*;

public class KPathVertexCoverFinder {
    public Set<Tree> findTreeKPathVertexCover(Tree tree, int k) {
        Set<Tree> kPathVertexCover = new HashSet<>();
        while (tree.containsProperlyRootedSubTree(k)) {
            kPathVertexCover.add(findNextVertexForTreeKPathVertexCover(tree, k));
            tree.recalculateHeight();
        }
        return kPathVertexCover;
    }

    private Tree findNextVertexForTreeKPathVertexCover(Tree tree, int k) {
        Optional<Tree> subTreeContainingProperlyRootedSubTree = tree.getChildren().stream().filter(child -> child.containsProperlyRootedSubTree(k)).findAny();
        if (subTreeContainingProperlyRootedSubTree.isPresent()) {
            return findNextVertexForTreeKPathVertexCover(subTreeContainingProperlyRootedSubTree.get(), k);
        }
        else {
            if (tree.getParent() != null) {
                tree.getParent().getChildren().removeIf(child -> child == tree);
            }
            else {
                tree.getChildren().clear();
            }
            return tree;
        }
    }

    public Set<Vertex> findGraph3PathVertexCover(Graph graph) {
        Set<Vertex> threePathVertexCover = new HashSet<>();
        addAtLeastKConnectedVerticesTo3PathVertexCover(graph, threePathVertexCover, 4);
        addLooselyConnectedVerticesTo3PathVertexCover(graph, threePathVertexCover);
        return threePathVertexCover;
    }

    private void addAtLeastKConnectedVerticesTo3PathVertexCover(Graph graph, Set<Vertex> threePathVertexCover, int k) {
        Optional<Vertex> kConnectedVertex;
        do {
            kConnectedVertex = graph.getVertices().stream().filter(vertex -> vertex.getDegree() >= k).findAny();
            kConnectedVertex.ifPresent(vertex -> {
                threePathVertexCover.add(vertex);
                graph.removeVertex(vertex);
            });
        }
        while (kConnectedVertex.isPresent());
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

    public Set<Vertex> findGraphKPathVertexCoverPruning(Graph graph, int k) {
        Set<Vertex> kPathVertexCover = new HashSet<>(graph.getVertices());
        for (Vertex vertex : graph.getVertices()) {
            if (!vertex.isNecessary(kPathVertexCover, k)) {
                kPathVertexCover.remove(vertex);
            }
        }
        return kPathVertexCover;
    }
}
