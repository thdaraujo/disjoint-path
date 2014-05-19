import graph.Edge;
import graph.Node;

import java.util.LinkedList;


public class GraphGenerator {
	public static Graph generateSimpleDigraph(int count){
		Graph simpleDigraph = new Graph();
		
		for(int i = 1; i <= count; i++){
			simpleDigraph.addNode(new Node(i));
		}
		
		LinkedList<Node> nodes = new LinkedList<Node>(simpleDigraph.getNodes());
		
		while(!nodes.isEmpty()){
			Node from = nodes.pop();
			Node to = nodes.pop();
			
			if(from != null && to != null){
				Edge e = new Edge(from, to);
				simpleDigraph.addEdge(e);
			}
		}
		return simpleDigraph;
	}
	
	public static Graph generateExampleDigraph(){
		Graph dg = new Graph();
		
		for(int i = 1; i <= 5; i++){
			dg.addNode(new Node(i));
		}
		
		LinkedList<Node> nodes = new LinkedList<Node>(dg.getSortedNodes());
		
		//1->2
		Node from = nodes.get(0);
		Node to = nodes.get(1);
		Edge e = new Edge(from, to);
		dg.addEdge(e);
		
		//1->3
		from = nodes.get(0);
		to = nodes.get(2);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		//1->5
		from = nodes.get(0);
		to = nodes.get(4);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		//2->3
		from = nodes.get(1);
		to = nodes.get(2);
		e = new Edge(from, to);
		dg.addEdge(e);

		//2->5
		from = nodes.get(1);
		to = nodes.get(4);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		//3->4
		from = nodes.get(2);
		to = nodes.get(3);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		return dg;
	}
}
