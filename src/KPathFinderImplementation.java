import graph.Edge;
import graph.IGraph;
import graph.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import path.KPathFinder;

/**
 * 
 */

/**
 * @author thiagoaraujo
 * Implements KPathFinder interface.
 */
public class KPathFinderImplementation implements KPathFinder {
	
	private LinkedList<Node> topologicalOrder;
	private Node S;
	private Node T;
	private Node reducedStart;
	private Node reducedTerminal;
	
	public KPathFinderImplementation(){
		this.topologicalOrder = new LinkedList<Node>();
		this.S = new Node("s"); //source
		this.T = new Node("t"); //terminal
	}
	
	/* (non-Javadoc)
	 * @see path.KPathFinder#obtainReduction(graph.IGraph, java.util.List, java.util.List)
	 */
	@Override
	public IGraph obtainReduction(IGraph G, List<Node> sources, List<Node> terminals) {
		Graph GOriginal = new Graph(G);
		Graph GPlusST = this.getGraphPlusSourceAndTerminal(GOriginal, this.S, this.T, sources, terminals);
		
		List<Node> topologicalOrder = this.getTopoligcalOrderToUseInReduction(G);
		
		Graph GReduced = this.getReducedGraph(GOriginal, 
				GPlusST, 
				topologicalOrder, 
				new LinkedList<Node>(sources), 
				new LinkedList<Node>(terminals));
		
		return GReduced;
	}

	/* (non-Javadoc)
	 * @see path.KPathFinder#thereExistsKDisjointPaths(graph.IGraph, java.util.List, java.util.List, java.util.Collection)
	 */
	@Override
	public boolean thereExistsKDisjointPaths(IGraph G, List<Node> sources,
			List<Node> terminals, Collection<List<Node>> kPaths) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<Node> getTopoligcalOrderToUseInReduction(IGraph g) {
		if(this.topologicalOrder == null || this.topologicalOrder.isEmpty())
		{
			System.out.println("Getting new topological order for G");
			Graph gOriginal = new Graph(g);
			this.topologicalOrder = getTopologicalOrder(gOriginal);
			this.topologicalOrder.addFirst(this.S);
			this.topologicalOrder.addLast(this.T);	
		}
		return this.topologicalOrder;
	}
	
	/*
	 * Returns a topological order for the graph.
	 * If no topological order exists, returns null (for example: graph has a cycle, and is not a DAG).
	 */
	public LinkedList<Node> getTopologicalOrder(Graph g){
		Topological topologicalOrder = new Topological(g);
		
		System.out.println(topologicalOrder.toString());
		
		if(topologicalOrder.hasOrder()){
			LinkedList<Node> order = topologicalOrder.order();	
			return order;
		}
		else{
			System.out.println("Graph is not a DAG!");
			return null;
		}
	}
	
	public Graph getGraphPlusSourceAndTerminal(Graph gOriginal, Node S, Node T, List<Node> sources, List<Node> terminals){
		Graph gPlusST = new Graph(gOriginal);
		
		gPlusST.addNode(S);
		gPlusST.addNode(T);
		
		if(sources.size() != terminals.size()){
			System.out.println("sources and terminals should have the same size!");
			//return gPlusST;
		}
		
		//S points to every source of G
		//S -> sources
		for(Node source : sources){
			Edge StoSource = new Edge(S, source);
			gPlusST.addEdge(StoSource);
		}
		
		//Every terminal in G points to T
		//terminal -> T
		for(Node terminal : terminals){
			Edge terminalToT = new Edge(terminal, T);
			gPlusST.addEdge(terminalToT);
		}
		
		return gPlusST;
	}
	
	public Graph getReducedGraph(Graph gOriginal,
			Graph gPlusST, 
			List<Node> topologicalOrder, 
			List<Node> sources, 
			List<Node> terminals){
		
		HashMap<Node, Node> sourcesMap = new HashMap<Node, Node>();
		HashMap<Node, Node> terminalsMap = new HashMap<Node, Node>();
		
		for(Node n : sources){
			sourcesMap.put(n, n);
		}
		
		for(Node n : terminals){
			terminalsMap.put(n, n);
		}
		
		Graph gReduced = new Graph();
		
		// permutation v x v
		int k = sources.size();
		List<Node> crossProduct = new LinkedList<Node>();
		crossProduct = crossProduct(k, topologicalOrder, topologicalOrder);
		
		if(crossProduct != null && !crossProduct.isEmpty()){
			this.reducedStart = crossProduct.get(0); //first = {s, s, ..., s}
			this.reducedTerminal = crossProduct.get(crossProduct.size() - 1); //last = {t, t, ..., t}
		}
		
		for(Node n : crossProduct){
			gReduced.addNode(n);
		}
		
		//TODO add edges
		for(Node vi : gReduced.getNodes()){
			for(Node vj : gReduced.getNodes()){
				//get node that starts a path
				Edge e = getReducedEdge((List<Node>)vi.getLabel(), (List<Node>)vj.getLabel());
				if(e == null) continue;
				
				//verify conditions
				boolean first = edgeSatisfiesFirstCondition(e, gPlusST);
				boolean second = edgeSatisfiesSecondCondition(e, gReduced, sourcesMap);
				boolean third = edgeSatisfiesThirdCondition(e, gOriginal, gReduced, topologicalOrder, terminalsMap);
				
				if(first && second && third){
					//add edge to g-reduced
					Edge eReduced = new Edge(vi, vj);
					gReduced.addEdge(eReduced);
					System.out.println(Graph.edgeToString(eReduced) + " added to g-reduced");
				}
				else{
					//System.out.println(Graph.edgeToString(e) + " conditions are: " + first + "\t" + second + "\t" + third);
				}
			}
		}
		
		return gReduced;
	}
	
	public List<Node> crossProduct(int k, List<Node> a, List<Node> b){
		if(k == 0) return a;
		else
		{
			LinkedList<Node> crossProduct = new LinkedList<Node>();
			for(Node vi : a){
				for(Node vj : b){
					Node product = nodeProduct(vi, vj);
					crossProduct.add(product);
					
				}
			}
			return crossProduct(k-1, crossProduct, b);
		}
	}
	
	public Node nodeProduct(Node a, Node b){
		if(a.getLabel() instanceof List<?>){
			List<Node> label = new LinkedList<Node>();
			List<Node> nodeList = (List<Node>)a.getLabel();
			label.addAll(nodeList);
			label.add(b);
			return new Node(label);
		}
		else{
			List<Node> label = new LinkedList<Node>();
			label.add(a);
			label.add(b);
			return new Node(label);
		}
	}
	
	
	
	private Edge getReducedEdge(List<Node> nodesFrom, List<Node> nodesTo) {
		if(nodesFrom.size() != nodesTo.size()) return null;
		
		int size = nodesFrom.size();
		
		for(int i = 0; i < size; i++){
			Node from = nodesFrom.get(i);
			Node to = nodesTo.get(i);
			if(from != to) return new Edge(from, to);
		}
		return null;
	}
	
	/*
	 * First condition: edge exists on original graph.
	 */
	private boolean edgeSatisfiesFirstCondition(Edge e, Graph gPlusST){
		return gPlusST.containsEdge(e);
	}
	
	/*
	 * Second condition: edge is S or ?
	 */
	private boolean edgeSatisfiesSecondCondition(Edge e, Graph gReduced, HashMap<Node, Node> sources){
		Node to = e.getTo();
		if(sources.containsKey(to)){
			return true;
		}
		return false;
	}
	
	/*
	 * Third condition: edge is T or its position in the topological order is maximum.
	 */
	private boolean edgeSatisfiesThirdCondition(Edge e, 
			Graph gReduced, 
			Graph gOriginal, 
			List<Node> topologicalOrder, 
			HashMap<Node, Node> terminals){
		
		//TODO 
		Node from = e.getFrom();
		Node to = e.getTo();
		
		if(terminals.containsKey(to)){
			return true;
		}
		else if(to != null && topologicalOrder.indexOf(to) > topologicalOrder.indexOf(from)){
			return true;
		}
		return false;
	}
}
