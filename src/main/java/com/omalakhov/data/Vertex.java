package com.omalakhov.data;

import java.util.*;

public class Vertex {
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
        Set<Vertex> path = new HashSet<>(Collections.singleton(this));
        return disjointKPathExists(kPathVertexCover, k, path);
    }

    private boolean disjointKPathExists(Set<Vertex> kPathVertexCover, int k, Set<Vertex> path) {
        if (path.size() == k + 1) {
            return true;
        }
        for (Vertex adjacentVertex : adjacentVertices) {
            if (!path.contains(adjacentVertex) && !kPathVertexCover.contains(adjacentVertex)) {
                path.add(adjacentVertex);
                if (adjacentVertex.disjointKPathExists(kPathVertexCover, k, path)) {
                    return true;
                }
                else {
                    path.remove(adjacentVertex);
                }
            }
        }
        return false;
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
}
