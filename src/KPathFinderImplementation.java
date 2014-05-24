import graph.Edge;
import graph.IGraph;
import graph.Node;

import java.util.Collection;
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
	
	public KPathFinderImplementation(){

	}
	
	/* (non-Javadoc)
	 * @see path.KPathFinder#obtainReduction(graph.IGraph, java.util.List, java.util.List)
	 */
	@Override
	public IGraph obtainReduction(IGraph G, List<Node> sources, List<Node> terminals) {
		// TODO Auto-generated method stub
		
		Graph GOriginal = new Graph(G);
		Graph GPlusST = this.getGraphPlusSourceAndTerminal(GOriginal, sources, terminals);
		LinkedList<Node> topologicalOrder = this.getTopologicalOrder(GPlusST);
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
	
	public Graph getGraphPlusSourceAndTerminal(Graph gOriginal, List<Node> sources, List<Node> terminals){
		Graph gPlusST = new Graph(gOriginal);
		
		Node S = new Node("s"); //source
		Node T = new Node("t"); //terminal
		
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
			LinkedList<Node> topologicalOrder, 
			LinkedList<Node> sources, 
			LinkedList<Node> terminals){
		
		Graph gReduced = new Graph();
		
		// permutation v x v
		for(Node vi : gPlusST.getNodes()){
			for(Node vj : gPlusST.getNodes()){
				//label is a list of nodes {vi, vj}
				List<Node> label = new LinkedList<Node>();
				label.add(vi);
				label.add(vj);
				
				Node Vij = new Node(label); //(vi.getLabel() + "," + vj.getLabel());
				gReduced.addNode(Vij);
			}
		}
		
		//TODO add edges
		for(Node v : gReduced.getNodes()){
			for(Node adj : gReduced.getAdjacentNodes(v)){
				//verify conditions
				Edge e = new Edge(v, adj);
				
				boolean first = edgeSatisfiesFirstCondition(e, gOriginal);
				boolean second = edgeSatisfiesSecondCondition(e, gReduced, sources);
				boolean third = edgeSatisfiesThirdCondition(e, gOriginal, gReduced, topologicalOrder, terminals);
				
				if(first && second && third){
					//add edge to g-reduced
					System.out.println(Graph.edgeToString(e) + " added to g-reduced");
					gReduced.addEdge(e);
				}
			}
		}
		
		return gReduced;
	}
	
	/*
	 * First condition: edge exists on original graph.
	 */
	private boolean edgeSatisfiesFirstCondition(Edge e, Graph gOriginal){
		return gOriginal.containsEdge(e);
	}
	
	/*
	 * Second condition: edge is S or ?
	 */
	private boolean edgeSatisfiesSecondCondition(Edge e, Graph gReduced, LinkedList<Node> sources){
		// TODO
		if(sources.contains(e.getTo())){
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
			LinkedList<Node> topologicalOrder, 
			LinkedList<Node> terminals){
		if(terminals.contains(e.getTo()) || topologicalOrder.peekLast() == e.getTo()){
			return true;
		}
		return false;
	}

}
