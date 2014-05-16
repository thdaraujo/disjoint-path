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

	@Override
	public Collection<Edge> getAdjacentEdges(Node arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Node> getAdjacentNodes(Node arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Node> getNodes() {
		return this.nodes.values();
	}
	
	public void addNode(Node n){
		// TODO
	}
	
	public void addEdge(Edge e){
		// TODO
	}
}
