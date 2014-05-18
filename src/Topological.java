import java.util.LinkedList;
import java.util.Stack;

import graph.IGraph;
import graph.Node;

/*
 * Based on Sedgewick, Algorithms 4th Ed.
 */
public class Topological {
	private LinkedList<Node> order; // topological order
	private DepthFirstOrder dfs;
	
	public Topological(IGraph G) {
		this.order = new LinkedList<>();
		this.dfs = new DepthFirstOrder(G);
		if (!dfs.hasCycle()) {
			order = dfs.reversePost();
		}
		else{
			System.out.println("graph has a cycle!");
		}
	}

	public LinkedList<Node> order() {
		return new LinkedList<Node>(order);
	}

	public boolean hasOrder() {
        return order != null;
    }
	
	public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append("Topological Order" + NEWLINE);
        s.append("hasOrder: " + this.hasOrder() + NEWLINE);
        
        if(dfs != null){
        	s.append(dfs.toString());
        }
        s.append(NEWLINE);
        return s.toString();
	}
}
