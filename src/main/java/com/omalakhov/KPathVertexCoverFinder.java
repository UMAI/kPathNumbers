package com.omalakhov;

import com.omalakhov.data.Tree;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
}
