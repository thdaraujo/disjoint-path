import graph.IGraph;
import graph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/*
 * Based on Sedgewick, Algorithms 4th Ed.
 */
public class DepthFirstOrder {
	private HashSet<Node> marked;
	private Queue<Node> pre; // vertices in preorder
	private Queue<Node> post; // vertices in postorder
	private LinkedList<Node> reversePost; // vertices in reverse postorder
	
	private HashMap<Node, Node> edgeTo;
	private Stack<Node> cycle; // vertices on a cycle (if one exists)
	private HashSet<Node> onStack; // vertices on recursive call stack

	public DepthFirstOrder(IGraph G) {
		pre = new LinkedList<Node>();
		post = new LinkedList<Node>();
		reversePost = new LinkedList<Node>();
		edgeTo = new HashMap<Node, Node>();
		
		int nodeCount = G.getNodes().size();
		marked = new HashSet<Node>(nodeCount);
		onStack = new HashSet<Node>(nodeCount);
		
		for (Node v : G.getNodes())
			if (!marked.contains(v))
				dfs(G, v);
	}

	private void dfs(IGraph G, Node v) {
		onStack.add(v);
		marked.add(v);
		pre.add(v);

		for (Node w : G.getAdjacentNodes(v))
			if (this.hasCycle()) return;
			else if (!marked.contains(w)){
				edgeTo.put(w, v);
				dfs(G, w);
			}
			else if (onStack.contains(w))
			{
				cycle = new Stack<Node>();
				for (Node x = v; x != w; x = edgeTo.get(x)){
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
			}
			onStack.remove(v);
			
		post.add(v);
		reversePost.push(v);
	}

	public LinkedList<Node> pre() {
		return (LinkedList<Node>) pre;
	}

	public LinkedList<Node> post() {
		return (LinkedList<Node>) post;
	}

	public LinkedList<Node> reversePost() {
		return reversePost;
	}
	
	public boolean hasCycle()
	{ 
		return cycle != null; 
	}
	
	public Stack<Node> cycle()
	{ 
		return cycle; 
	}
	
	public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append("DFS" + NEWLINE);
        s.append("hasCycle: " + this.hasCycle() + NEWLINE);
        if(this.cycle != null && !this.cycle.isEmpty()){
        	s.append("cycle: " + NEWLINE);
        	for(Node v : this.cycle()){
        		s.append(v.getLabel().toString() + "->");
        	}
        	s.append(NEWLINE);
        }
        
        s.append("pre-order: ");
        for(Node v : this.pre()){
        	s.append(v.getLabel().toString() + "->");
    	}
    	s.append(NEWLINE);
        
    	s.append("post-order: ");
        for(Node v : this.post()){
        	s.append(v.getLabel().toString() + "->");
    	}
    	s.append(NEWLINE);
        
    	s.append("reverse-post-order (topological): ");
		for(Node v : this.reversePost()){
			s.append(v.getLabel().toString() + "->");
    	}
    	s.append(NEWLINE);
        
        return s.toString();
	}
}
