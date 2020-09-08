package com.omalakhov.data;

import java.util.*;

public class Vertex implements ValueData, Comparable<Vertex> {
	private String value;
	private List<Vertex> adjacentVertices;

	public Vertex(String value) {
		this.value = value;
		adjacentVertices = new ArrayList<>();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Vertex> getAdjacentVertices() {
		return adjacentVertices;
	}

	public void setAdjacentVertices(List<Vertex> adjacentVertices) {
		this.adjacentVertices = adjacentVertices;
	}

	public void addAdjacentVertex(Vertex adjacentVertex) {
		if (adjacentVertices == null) {
			adjacentVertices = new ArrayList<>();
		}
		adjacentVertices.add(adjacentVertex);
	}

	public int getDegree() {
		if (adjacentVertices != null) {
			return adjacentVertices.size();
		}
		return 0;
	}

	public int getDegreeInInducedSubGraph(Set<Vertex> subGraph) {
		if (adjacentVertices != null) {
			return (int) adjacentVertices.stream().filter(subGraph::contains).count();
		}
		return 0;
	}

	public boolean isNecessary(Set<Vertex> kPathVertexCover, int k) {
		//Set<Set<Vertex>> paths = findPathsOfLengthUpToK(kPathVertexCover, k, new HashSet<>());
		Set<Set<Vertex>> paths = iterativeFindPathsOfLengthUpToK(kPathVertexCover, k);
		Optional<Set<Vertex>> pathOfLengthK = paths.stream().filter(path -> path.size() == k).findAny();
		if (pathOfLengthK.isPresent()) {
			return true;
		}
		else {
			for (Set<Vertex> currentPath : paths) {
				Optional<Set<Vertex>> completingPath = paths
						.stream()
						.filter(path -> path.size() >= k + 1 - currentPath.size())
						.filter(path -> {
							Set<Vertex> intersection = new HashSet<>(path);
							intersection.retainAll(currentPath);
							return intersection.size() == 1;
						})
						.findAny();
				if (completingPath.isPresent()) {
					return true;
				}
			}
		}
		return false;
	}

	private Set<Set<Vertex>> findPathsOfLengthUpToK(Set<Vertex> kPathVertexCover, int k, Set<Vertex> path) {
		Set<Set<Vertex>> paths = new HashSet<>();
		path.add(this);
		if (path.size() == k) {
			paths.add(path);
			return paths;
		}
		for (Vertex adjacentVertex : adjacentVertices) {
			if (!path.contains(adjacentVertex) && !kPathVertexCover.contains(adjacentVertex)) {
				paths.addAll(adjacentVertex.findPathsOfLengthUpToK(kPathVertexCover, k, new HashSet<>(path)));
			}
		}
		if (paths.isEmpty()) {
			paths.add(path);
		}
		return paths;
	}

	private Set<Set<Vertex>> iterativeFindPathsOfLengthUpToK(Set<Vertex> kPathVertexCover, int k) {
		Set<Set<Vertex>> paths = new HashSet<>();
		Stack<Set<Vertex>> pathStack = new Stack<>();
		Stack<Vertex> verticesStack = new Stack<>();
		verticesStack.push(this);
		Set<Vertex> initialPath = new HashSet<>(Collections.emptySet());
		pathStack.push(initialPath);
		boolean pathEnds;
		while (!verticesStack.isEmpty()) {
			Vertex vertex = verticesStack.pop();
			Set<Vertex> path = pathStack.pop();
			path.add(vertex);
			if (path.size() == k) {
				paths = new HashSet<>();
				paths.add(path);
				return paths;
			}
			pathEnds = true;
			for (Vertex adjacentVertex : vertex.getAdjacentVertices()) {
				if (!path.contains(adjacentVertex) && !kPathVertexCover.contains(adjacentVertex)) {
					pathEnds = false;
					verticesStack.push(adjacentVertex);
					pathStack.push(new HashSet<>(path));
				}
			}
			if (pathEnds) {
				paths.add(path);
			}
		}
		return paths;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vertex vertex = (Vertex) o;
		return Objects.equals(value, vertex.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public int compareTo(Vertex another) {
		int degreeComparison = Integer.compare(getDegree(), another.getDegree());
		if (degreeComparison == 0) {
			return getValue().compareTo(another.getValue());
		}
		return degreeComparison;
	}
}
