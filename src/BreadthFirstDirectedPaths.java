import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import graph.IGraph;
import graph.Node;



/**
 *  Based on <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class BreadthFirstDirectedPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private HashSet<Node> marked;  // marked[v] = is there an s->v path?
    private HashMap<Node, Node> edgeTo;
    private HashMap<Node, Integer> distTo;      // distTo[v] = length of shortest s->v path

    /**
     * Computes the shortest path from <tt>s</tt> and every other vertex in graph <tt>G</tt>.
     * @param G the digraph
     * @param s the source vertex
     */
    public BreadthFirstDirectedPaths(IGraph G, Node s) {
    	marked = new HashSet<Node>();
        distTo = new HashMap<Node, Integer>();
        edgeTo = new HashMap<Node, Node>();
        for (Node n : G.getNodes()){ 
        	distTo.put(n, INFINITY);
        }
        
        bfs(G, s);
    }
    
    // BFS from single source
    private void bfs(IGraph G, Node s) {
        Queue<Node> q = new LinkedList<Node>();
        marked.add(s);
        distTo.put(s, 0);
        q.add(s);
        //q.enqueue(s);
        while (!q.isEmpty()) {
            Node v = q.remove();
            for (Node w : G.getAdjacentNodes(v)) {
                if (!marked.contains(w)) {
                    edgeTo.put(w, v);
                    distTo.put(w, distTo.get(v) + 1);
                    marked.add(w);
                    q.add(w);
                }
            }
        }
    }

   
    /**
     * Is there a directed path from the source <tt>s</tt> (or sources) to vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a directed path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(Node v) {
        return marked.contains(v);
    }

    /**
     * Returns the number of edges in a shortest path from the source <tt>s</tt>
     * (or sources) to vertex <tt>v</tt>?
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(Node v) {
        if(distTo.containsKey(v)){
        	return distTo.get(v);
        }
        return INFINITY;
    }

    /**
     * Returns a shortest path from <tt>s</tt> (or sources) to <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     */
    public Stack<Node> pathTo(Node v) {
        if (!hasPathTo(v)) return null;
        Stack<Node> path = new Stack<Node>();
        Node x;
        for (x = v; distTo.get(x) != 0; x = edgeTo.get(x))
            path.push(x);
        path.push(x);
        return path;
    }
}