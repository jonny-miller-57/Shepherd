import java.util.*;

/**
 * This class is an immutable representation of a directed graph.
 * Think of this as a collection of Nodes, each storing some data, which can be
 * connected from a source Node to a destination Node by none or more edges.
 * Each Node has unique data. Node-data (N) must be immutable; in the case
 * where N is mutable, the behavior of this Graph is unspecified.
 */
public final class Graph<N> {
    private final boolean DEBUG = false;
    private final Map<N, Node> nodes;

    // This Graph is represented by nodes in which each node is associated
    // with a list of its outgoing edges.
    //
    // RI: nodes != null
    // AF(this) = nodes + UNION of each node.edges

    /**
     * Creates an empty Graph containing no Nodes with no edges.
     * @spec.effects Creates an empty graph
     */
    public Graph() {
        this.nodes = new HashMap<>();
        checkRep();
    }

    /**
     * Adds a node to this graph
     * @param data Data that new node will contain
     * @spec.requires data != null
     * @spec.modifies this
     * @spec.effects Adds a childless node to this graph or does nothing if already exists
     */
    public void addNode(N data) {
        checkRep();
        assert data != null;

        if (!nodes.containsKey(data)) {
            nodes.put(data, new Node(data));
        }
        checkRep();
    }

    /**
     * Adds an edge from source node to destination node
     * @param src Parent node for edge
     * @param dest Child node to connect to from src
     * @spec.requires src != null, dest != null, src and dest are present
     * @spec.modifies this
     * @spec.effects Adds an edge from src node to dest node or does nothing if already exists
     */
    public void addEdge(N src, N dest) {
        checkRep();
        assert src != null && dest != null;
        assert nodes.containsKey(src) && nodes.containsKey(dest);

        Node source = nodes.get(src);
        Node destination = nodes.get(dest);

        //Edge edge = new Edge(label, nodes.get(dest));

        if (!source.containsChild(destination)) { // add it if edge does not exist
            source.children.add(destination);
        }

        checkRep();
    }

    /**
     * Returns a list of all Nodes present in this graph
     * @return returns a list of the Nodes in this
     */
    public List<N> listNodes() {
        checkRep();
        ArrayList<N> nodeList = new ArrayList<>(nodes.keySet());
        checkRep();

        return nodeList;
    }

    /**
     * Returns a list of Nodes who are the children of parent
     * @param parent The Node of whose children are desired
     * @spec.requires parent != null
     * @return a map of Nodes who are children of parent
     * @throws IllegalArgumentException if parent is does not exist in this graph
     */
    public List<N> listChildren(N parent) {
        checkRep();

        assert parent != null;
        if (!nodes.containsKey(parent)) { // if parent is not in graph
            throw new IllegalArgumentException("parent node does not exist");
        }

        List<N> childrenList = new LinkedList<>();
        Node p = nodes.get(parent);
        List<Node> children = new ArrayList<>(p.children);

        for (Node child : children) {
            childrenList.add(child.data);
        }

        checkRep();
        return childrenList;
    }

    /**
     * Returns whether a node is present in this
     * @param node Node to check if present
     * @return returns true if node is in this; else false
     */
    public boolean containsNode(N node) {
        return nodes.containsKey(node);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Graph<?>))
            return false;
        Graph<?> other = (Graph<?>) o;
        return this.nodes.equals(other.nodes);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(nodes.size());
        result = 31 * result + nodes.hashCode();
        return result;
    }

    private void checkRep() {
        assert nodes != null;
        if (DEBUG) {
            for (Map.Entry<N, Node> entry : nodes.entrySet()) {
                assert entry != null;
            }
        }
    }

    private class Node{
        // This node is represented by an adjacency list of edges where each edge
        // is an outgoing edge from this to a destination node

        private final N data;
        private final List<Node> children;

        // RI: children != null
        // AF(this) = data + children

        // private constructor for inner Node class
        private Node(N data) {
            this.data = data;
            this.children = new LinkedList<>();
            checkRep();
        }

        // helper method that returns true if this node has the edge inQuestion
        // already attached to it, otherwise returns false.
        private boolean containsChild(Node inQuestion) {
            checkRep();
            for (Node child : children) {
                if (child.equals(inQuestion)) {
                    return true;
                }
            }
            checkRep();
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Graph<?>.Node))
                return false;
            Graph<?>.Node other = (Graph<?>.Node) o;
            return this.data.equals(other.data);
        }

        @Override
        public int hashCode() {
            int result = this.data.hashCode();
            result = 31 * result + Integer.hashCode(children.size());
            return result;
        }

        private void checkRep() {
            assert children != null;
        }
    }

}
