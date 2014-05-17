import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import graph.Edge;
import graph.IGraph;
import graph.Node;

public class Graph implements IGraph {

	private HashMap<Node, LinkedList<Node>> adjNodes;
	
	private int V;
	private HashMap<Node, Node> nodes;
	
	private int E;
	private HashMap<Edge, Edge> edges;
	
	public Graph(){
		this.adjNodes = new HashMap<Node, LinkedList<Node>>();
		this.nodes = new HashMap<Node, Node>();
		this.edges = new HashMap<Edge, Edge>();
	}
	
	public int V(){
		return this.V;
	}
	
	public int E(){
		return this.E;
	}
	
	@Override
	public boolean containsEdge(Edge arg0) {
		return this.edges.containsKey(arg0);
	}
	
	public boolean containsNode(Node arg0){
		return this.nodes.containsKey(arg0);
	}

	@Override
	public Collection<Edge> getAdjacentEdges(Node arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Node> getAdjacentNodes(Node arg0) {
		if(!containsNode(arg0)){
			System.out.println("node not found!");
			return null;
		}
		return this.adjNodes.get(arg0);
	}

	@Override
	public Collection<Node> getNodes() {
		return this.nodes.values();
	}
	
	public void addNode(Node n){
		this.nodes.put(n, n);
		this.adjNodes.put(n, new LinkedList<Node>());
		this.V++;
	}
	
	public void addEdge(Edge e){
		Node from = e.getFrom();
		Node to = e.getTo();
		
		if(!containsNode(from) || !containsNode(to)){
			System.out.println("node from or to not found!");
			return;
		}
		
		this.adjNodes.get(from).add(to);
		this.edges.put(e, e);
		this.E++;
	}
}
