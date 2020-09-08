package com.omalakhov.data;

import com.omalakhov.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.omalakhov.exception.ApplicationException.Type.*;

public class Tree implements ValueData {
	private String value;
	private Tree parent;
	private List<Tree> children = new ArrayList<>();
	private int height;

	public Tree(String value) {
		this.value = value;
	}

	public Tree(Map<String, String> vertexParent) throws ApplicationException {
		value = findRoot(vertexParent);
		buildTree(this, vertexParent);
		recalculateHeight();
	}

	private String findRoot(Map<String, String> vertexParent) throws ApplicationException {
		String root = null;
		for (Map.Entry<String, String> entry : vertexParent.entrySet()) {
			if (entry.getValue() == null || entry.getValue().isEmpty()) {
				root = entry.getKey();
				break;
			}
		}
		if (root == null) {
			throw new ApplicationException(TREE_ROOT_NOT_FOUND);
		}
		return root;
	}

	private void buildTree(Tree tree, Map<String, String> vertexParent) {
		vertexParent.forEach((vertex, parent) -> {
			if (tree.getValue().equals(parent)) {
				Tree child = new Tree(vertex);
				tree.addChild(child);
				buildTree(child, vertexParent);
			}
		});
	}

	public boolean containsProperlyRootedSubTree(int pathLength) {
		if (height >= pathLength) {
			return true;
		}
		int maxSubtreeHeight = -1, secondToMaxSubtreeHeight = -1;
		for (Tree child : children) {
			int childHeight = child.getHeight();
			if (childHeight > maxSubtreeHeight) {
				secondToMaxSubtreeHeight = maxSubtreeHeight;
				maxSubtreeHeight = childHeight;
			}
			else if (childHeight > secondToMaxSubtreeHeight) {
				secondToMaxSubtreeHeight = childHeight;
			}
		}
		return maxSubtreeHeight + secondToMaxSubtreeHeight + 1 >= pathLength;
	}

	public int recalculateHeight() {
		if (children == null || children.isEmpty()) {
			height = 1;
		}
		else {
			height = children.stream().map(Tree::recalculateHeight).max(Integer::compareTo).get() + 1;
		}
		return height;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Tree getParent() {
		return parent;
	}

	public void setParent(Tree parent) {
		this.parent = parent;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public void addChild(Tree child) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tree tree = (Tree) o;
		return Objects.equals(value, tree.value) &&
				Objects.equals(children, tree.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, children);
	}
}
