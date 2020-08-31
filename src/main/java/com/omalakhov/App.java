package com.omalakhov;

import com.omalakhov.data.Tree;
import com.omalakhov.exception.TreeException;
import com.omalakhov.io.Reader;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    public static void main( String[] args ) throws FileNotFoundException, TreeException {
        Tree tree = new Tree(Reader.readTreeVertexParentMapping("D:\\Projects\\kPathNumbers\\src\\main\\java\\com\\omalakhov\\files\\tree.txt"));
        KPathVertexCoverFinder kPVCFinder = new KPathVertexCoverFinder();
        Set<Tree> kPathVertexCover = kPVCFinder.findTreeKPathVertexCover(tree, 3);
        System.out.println(kPathVertexCover.stream().map(Tree::getValue).collect(Collectors.toList()));
    }
}
