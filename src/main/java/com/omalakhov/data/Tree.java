package com.omalakhov.data;

import com.omalakhov.exception.ApplicationException;

import java.util.*;

import static com.omalakhov.exception.ApplicationException.Type.TREE_ROOT_NOT_FOUND;

public class Tree {
	private Map<String, TreeNode> nodes = new HashMap<>();
	private TreeNode root;

	public Tree(Map<String, String> vertexParentMapping) throws ApplicationException {
		String rootValue = vertexParentMapping
				.entrySet()
				.stream()
				.filter(entry -> entry.getValue() == null || entry.getValue().isEmpty())
				.map(Map.Entry::getKey)
				.findFirst()
				.orElseThrow(() -> new ApplicationException(TREE_ROOT_NOT_FOUND));
		root = new TreeNode(rootValue);
		nodes.put(rootValue, root);
		vertexParentMapping.remove(rootValue);
		for (Map.Entry<String, String> entry : vertexParentMapping.entrySet()) {
			TreeNode node = nodes.computeIfAbsent(entry.getKey(), TreeNode::new);
			TreeNode parentNode = nodes.computeIfAbsent(entry.getValue(), TreeNode::new);
			parentNode.addChild(node);
			node.setParent(parentNode);
		}
		root.calculateHeight();
	}

	public Map<String, TreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, TreeNode> nodes) {
		this.nodes = nodes;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
}
