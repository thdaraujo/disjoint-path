import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import graph.Edge;
import graph.IGraph;
import graph.Node;

public class Graph implements IGraph {

	private HashMap<Node, LinkedList<Node>> adjNodes;
	
	private int V;
	private HashMap<Integer, Node> nodes;
	
	private int E;
	private HashMap<Integer, Edge> edges;
	
	public Graph(){
		this.adjNodes = new HashMap<Node, LinkedList<Node>>();
		this.nodes = new HashMap<Integer, Node>();
		this.edges = new HashMap<Integer, Edge>();
	}
	
	public int V(){
		return this.V;
	}
	
	public int E(){
		return this.E;
	}
	
	@Override
	public boolean containsEdge(Edge arg0) {
		return this.edges.containsKey(arg0.hashCode());
	}
	
	public boolean containsNode(Node arg0){
		return this.nodes.containsKey(arg0.hashCode());
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
		return this.adjNodes.get(arg0.hashCode());
	}

	@Override
	public Collection<Node> getNodes() {
		return this.nodes.values();
	}
	
	public void addNode(Node n){
		this.nodes.put(n.hashCode(), n);
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
		this.edges.put(e.hashCode(), e);
		this.E++;
	}
	
	public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(this.V + " vertices, " + this.E + " edges " + NEWLINE);
        
        for (Map.Entry<Node, LinkedList<Node>> entry : this.adjNodes.entrySet()) {
            Node key = entry.getKey();
            LinkedList<Node> adjacentList = entry.getValue();
            
            s.append(key.getLabel().toString() + ": ");
            for (Node w : adjacentList) {
                s.append(w.getLabel().toString() + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}
