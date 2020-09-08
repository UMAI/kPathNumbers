package com.omalakhov.data;

import java.util.*;

public class Graph {
	private Set<Vertex> vertices;
	private int numberOfEdges;

	public Graph(Set<Edge> edges) {
		numberOfEdges = edges.size();
		Map<String, Vertex> verticesMap = new HashMap<>();
		Vertex vertexFrom, vertexTo;
		for (Edge edge : edges) {
			if (!verticesMap.containsKey(edge.getFrom())) {
				vertexFrom = new Vertex(edge.getFrom());
				verticesMap.put(edge.getFrom(), vertexFrom);
			}
			else {
				vertexFrom = verticesMap.get(edge.getFrom());
			}
			if (!verticesMap.containsKey(edge.getTo())) {
				vertexTo = new Vertex(edge.getTo());
				verticesMap.put(edge.getTo(), vertexTo);
			}
			else {
				vertexTo = verticesMap.get(edge.getTo());
			}
			vertexFrom.addAdjacentVertex(vertexTo);
			vertexTo.addAdjacentVertex(vertexFrom);
		}
		vertices = new HashSet<>(verticesMap.values());
	}

	public Set<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(Set<Vertex> vertices) {
		this.vertices = vertices;
	}

	public void removeVertex(Vertex vertexToRemove) {
		vertexToRemove
				.getAdjacentVertices()
				.stream()
				.map(Vertex::getAdjacentVertices)
				.forEach(adjacentVertices -> adjacentVertices.removeIf(vertexToRemove::equals));
		vertices.removeIf(vertexToRemove::equals);
		numberOfEdges -= vertexToRemove.getDegree();
	}

	public int getNumberOfVertices() {
		if (vertices != null) {
			return vertices.size();
		}
		return 0;
	}

	public int getNumberOfEdges() {
		return numberOfEdges;
	}
}
