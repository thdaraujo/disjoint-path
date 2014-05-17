import java.util.LinkedList;

import graph.Edge;
import graph.Node;

/**
 * 
 */

/**
 * @author thiagoaraujo
 *
 */
public class KPathFinderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		printProjectName();
		
		Graph G = new Graph();
		
		LinkedList<Node> nodes = getNodes(10);
		for(Node n : nodes){
			System.out.println(n.getLabel() + ": " +  n.hashCode());
			G.addNode(n);
		}
		
		LinkedList<Edge> edges = getEdges(new LinkedList<Node>(G.getNodes()));
		for (Edge e : edges) {
			System.out.println(e.toString() + ": " +  e.hashCode());
			G.addEdge(e);
		}
		
		System.out.println(G.toString());
		
	}
	
	public static LinkedList<Node> getNodes(int count){
		LinkedList<Node> nodes = new LinkedList<Node>();
		
		for(int i = 1; i <= count; i++){
			nodes.add(new Node(i));
		}
		
		return nodes;
	}
	
	public static LinkedList<Edge> getEdges(LinkedList<Node> nodes){
		LinkedList<Edge> edges = new LinkedList<Edge>();
		
		while(!nodes.isEmpty()){
			Node from = nodes.pop();
			Node to = nodes.pop();
			
			if(from != null && to != null){
				Edge e = new Edge(from, to);
				edges.add(e);
			}
		}
		
		return edges;
	}
	
	private static void printProjectName() {
		System.out.println("##   EP4 - Path Finder   ##");
		System.out.println("##### Thiago Araujo #######");
		System.out.println("###########################");
		System.out.println("");
	}

}
