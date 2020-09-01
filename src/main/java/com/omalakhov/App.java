package com.omalakhov;

import com.omalakhov.data.Graph;
import com.omalakhov.data.Tree;
import com.omalakhov.data.Vertex;
import com.omalakhov.exception.ApplicationException;
import com.omalakhov.io.Reader;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws FileNotFoundException, ApplicationException {
        runTreeExample();
        runGraph3PathExample();
        runGraphKPathPruningExample();
    }

    private static void runTreeExample() throws FileNotFoundException, ApplicationException {
        Tree tree = new Tree(Reader.readTreeVertexParentMapping("D:\\Projects\\kPathNumbers\\src\\main\\java\\com\\omalakhov\\files\\tree.txt"));
        KPathVertexCoverFinder kPVCFinder = new KPathVertexCoverFinder();
        Set<Tree> kPathVertexCover = kPVCFinder.findTreeKPathVertexCover(tree, 3);
        System.out.println(kPathVertexCover.stream().map(Tree::getValue).collect(Collectors.toList()));
    }

    private static void runGraph3PathExample() throws FileNotFoundException, ApplicationException {
        Graph graph = new Graph(Reader.readGraphEdges("D:\\Projects\\kPathNumbers\\src\\main\\java\\com\\omalakhov\\files\\graph.txt"));
        KPathVertexCoverFinder kPVCFinder = new KPathVertexCoverFinder();
        Set<Vertex> threePathVertexCover = kPVCFinder.findGraph3PathVertexCover(graph);
        System.out.println(threePathVertexCover.stream().map(Vertex::getValue).collect(Collectors.toList()));
    }

    private static void runGraphKPathPruningExample() throws FileNotFoundException, ApplicationException {
        Graph graph = new Graph(Reader.readGraphEdges("D:\\Projects\\kPathNumbers\\src\\main\\java\\com\\omalakhov\\files\\graph.txt"));
        KPathVertexCoverFinder kPVCFinder = new KPathVertexCoverFinder();
        Set<Vertex> kPathVertexCover = kPVCFinder.findGraphKPathVertexCoverPruning(graph, 3);
        System.out.println(kPathVertexCover.stream().map(Vertex::getValue).collect(Collectors.toList()));
    }
}
