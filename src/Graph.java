import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.IGraph;
import graph.Node;

public class Graph implements IGraph {

	private HashMap<Node, LinkedList<Node>> adjNodes;
	private HashMap<Node, LinkedList<Edge>> adjEdges;
	
	private int V;
	private HashMap<Node, Node> nodes;
	
	private int E;
	private HashMap<Edge, Edge> edges;
	
	public Graph(){
		this.adjNodes = new HashMap<Node, LinkedList<Node>>();
		this.adjEdges = new HashMap<Node, LinkedList<Edge>>();
		this.nodes = new HashMap<Node, Node>();
		this.edges = new HashMap<Edge, Edge>();
	}
	
	public Graph(IGraph g){
		this();
		for(Node n : g.getNodes()){
			this.addNode(n);
		}
		
		for(Node n : g.getNodes()){
			Collection<Node> adjNodes = g.getAdjacentNodes(n);
			for(Node v : adjNodes){
				Edge e = new Edge(n, v);
				this.addEdge(e);
			}
		}
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
		return this.adjEdges.get(arg0);
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
	
	public Collection<Node> getSortedNodes(){
		LinkedList<Node> nodes = new LinkedList<Node>(getNodes());
		
		Collections.sort(nodes, new Comparator<Node>() {
	         @Override
	         public int compare(Node o1, Node o2) {
	             return o1.getLabel().toString().compareTo(o2.getLabel().toString());
	         }
	     });
		
		return nodes;
	}
	
	public void addNode(Node n){
		this.nodes.put(n, n);
		this.adjNodes.put(n, new LinkedList<Node>());
		this.adjEdges.put(n, new LinkedList<Edge>());
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
		
		this.adjEdges.get(from).add(e);
		this.adjEdges.get(to).add(e);
		
		this.edges.put(e, e);
		this.E++;
	}
	
	public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(this.V + " vertices, " + this.E + " edges " + NEWLINE);
        
        for (Map.Entry<Node, LinkedList<Node>> entry : this.adjNodes.entrySet()) {
            Node key = entry.getKey();
            LinkedList<Node> adjacentList = entry.getValue();
            
            String label = "";
            if(key.getLabel() instanceof List<?> && key.getLabel() != null){
            	List<Node> nodeList = (List<Node>) key.getLabel();
            	for(Node n : nodeList){
            		label += labelToString(n.getLabel()) + " ";
            	}
            }
            else{
            	label = key.getLabel().toString();
            }
            
            s.append(label + ": ");
            for (Node w : adjacentList) {
                s.append(labelToString(w.getLabel()) + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
	
	public static void printNode(Node n, Collection<Edge> adjEdges){
		System.out.println("Node: " + labelToString(n.getLabel()));
		if(adjEdges != null){
			System.out.println("Edges: " + adjEdges.size());
		}
		
		String sLabel = labelToString(n.getLabel());
	
		if(adjEdges != null){
			System.out.println("Edges: ");
			for(Edge e : adjEdges){
				System.out.println(edgeToString(e));
			}
		}
		
		System.out.println("");
	}
	
	public static String labelToString(Object label)
	{
		StringBuilder s = new StringBuilder();
		if(label != null && label instanceof List<?>){
			s.append("{");
			List<Node> nodeList = (List<Node>) label;
        	for(Node node : nodeList){
        		s.append(node.getLabel() + " ");
        	}
        	s.append("}");
		}
		else{
			s.append("{"+ label + "}");
		}
		return s.toString();
	}
	
	public static String edgeToString(Edge e){
		Node from = e.getFrom();
		Node to = e.getTo();
		if(from != null && to != null){
			return labelToString(from.getLabel()) + "->" + labelToString(to.getLabel());
		}
		return " null ";
	}
}
