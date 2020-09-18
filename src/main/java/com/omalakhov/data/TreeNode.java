package com.omalakhov.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNode implements ValueData {
	private String value;
	private TreeNode parent;
	private List<TreeNode> children = new ArrayList<>();
	private int height;

	public TreeNode(String value) {
		this.value = value;
	}

	public boolean subtreeContainsPathOfLength(int pathLength) {
		if (height >= pathLength) {
			return true;
		}
		int maxSubtreeHeight = 0;
		int secondToMaxSubtreeHeight = 0;
		for (TreeNode child : children) {
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

	public int calculateHeight() {
		if (children == null || children.isEmpty()) {
			height = 1;
		}
		else {
			height = children.stream().map(TreeNode::calculateHeight).max(Integer::compareTo).get() + 1;
		}
		return height;
	}

	public int calculateSubtreeSize() {
		if (children == null || children.isEmpty()) {
			return 1;
		}
		return children.stream().map(TreeNode::calculateSubtreeSize).mapToInt(Integer::intValue).sum() + 1;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public void addChild(TreeNode child) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}

	public void removeChild(TreeNode child) {
		children.remove(child);
		propagateHeightChange(child.getHeight());
	}

	public void propagateHeightChange(int oldChildHeight) {
		if (oldChildHeight + 1 == height) {
			int oldHeight = height;
			height = children.stream().map(TreeNode::getHeight).max(Integer::compareTo).orElse(0) + 1;
			if (height != oldHeight && parent != null) {
				parent.propagateHeightChange(oldHeight);
			}
		}
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
		TreeNode treeNode = (TreeNode) o;
		return Objects.equals(value, treeNode.value) &&
				Objects.equals(children, treeNode.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, children);
	}
}
