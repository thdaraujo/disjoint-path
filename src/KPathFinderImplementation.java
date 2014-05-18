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
		Graph GReduced = this.getReducedGraph(GPlusST, GOriginal, topologicalOrder);
		return GReduced;
	}

	/* (non-Javadoc)
	 * @see path.KPathFinder#thereExistsKDisjointPaths(graph.IGraph, java.util.List, java.util.List, java.util.Collection)
	 */
	@Override
	public boolean thereExistsKDisjointPaths(IGraph G, List<Node> sources,
			List<Node> terminals, Collection<List<Node>> arg3) {
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
		
		Node S = new Node("S"); //source
		Node T = new Node("T"); //terminal
		
		gPlusST.addNode(S);
		gPlusST.addNode(T);
		
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
	
	public Graph getReducedGraph(Graph gPlusST, Graph gOriginal, LinkedList<Node> topologicalOrder){
		Graph gReduced = new Graph();
		
		// permutation v x v
		for(Node vi : gPlusST.getNodes()){
			for(Node vj : gPlusST.getNodes()){
				Node Vij = new Node(vi.getLabel() + "," + vj.getLabel());
				gReduced.addNode(Vij);
			}
		}
		
		//TODO add edges
		for(Node v : gReduced.getNodes()){
			for(Node adj : gReduced.getAdjacentNodes(v)){
				//verify conditions
				Edge e = new Edge(v, adj);
				
				boolean first = edgeSatisfiesFirstCondition(e, gOriginal);
				boolean second = edgeSatisfiesSecondCondition(e, gReduced);
				boolean third = edgeSatisfiesThirdCondition(e, gReduced, topologicalOrder);
				
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
	public boolean edgeSatisfiesFirstCondition(Edge e, Graph gOriginal){
		return gOriginal.containsEdge(e);
	}
	
	/*
	 * Second condition: edge is S or ?
	 */
	public boolean edgeSatisfiesSecondCondition(Edge e, Graph gReduced){
		// TODO
		return false;
	}
	
	/*
	 * Third condition: edge is T or its position in the topological order is maximum.
	 */
	public boolean edgeSatisfiesThirdCondition(Edge e, Graph gReduced, LinkedList<Node> topologicalOrder){
		//TODO
		return false;
	}

}
