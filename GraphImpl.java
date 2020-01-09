import java.util.*;


/**
 * Filename:   GraphImpl.java
 * Project:    p4
 * Course:     cs400
 * Authors:
 * Due Date:
 * <p>
 * T is the label of a vertex, and List<T> is a list of
 * adjacent vertices for that vertex.
 * <p>
 * Additional credits:
 * <p>
 * Bugs or other notes:
 *
 * @param <T> type of a vertex
 */
public class GraphImpl<T> implements GraphADT<T> {
    private int size;  // the number of edges in the graph
    private int order;  // the number of vertices in the graph

    // YOU MAY ADD ADDITIONAL private members
    // YOU MAY NOT ADD ADDITIONAL public members

    /**
     * Store the vertices and the vertice's adjacent vertices
     */
    private Map<T, List<T>> verticesMap;


    /**
     * Construct and initialize and empty Graph
     */
    public GraphImpl() {
        verticesMap = new HashMap<T, List<T>>();
        // you may initialize additional data members here
    }

    public void addVertex(T vertex) {
        if (!hasVertex(vertex)) {
            verticesMap.put(vertex, new ArrayList<>());
            order++;
        }
    }

    public void removeVertex(T vertex) {
        verticesMap.remove(vertex);
        order--;
    }

    public void addEdge(T vertex1, T vertex2) {
        verticesMap.get(vertex1).add(vertex2);
        size++;
    }

    public void removeEdge(T vertex1, T vertex2) {
        verticesMap.get(vertex1).remove(vertex2);
        size--;
    }

    public Set<T> getAllVertices() {
        return verticesMap.keySet();
    }

    public List<T> getAdjacentVerticesOf(T vertex) {
        return verticesMap.get(vertex);
    }

    public boolean hasVertex(T vertex) {
        return verticesMap.containsKey(vertex);
    }

    public int order() {
        return order;
    }

    public int size() {
        return size;
    }


    /**
     * Prints the graph for the reference
     * DO NOT EDIT THIS FUNCTION
     * DO ENSURE THAT YOUR verticesMap is being used
     * to represent the vertices and edges of this graph.
     */
    public void printGraph() {

        for (T vertex : verticesMap.keySet()) {
            if (verticesMap.get(vertex).size() != 0) {
                for (T edges : verticesMap.get(vertex)) {
                    System.out.println(vertex + " -> " + edges + " ");
                }
            } else {
                System.out.println(vertex + " -> " + " ");
            }
        }
    }
}
