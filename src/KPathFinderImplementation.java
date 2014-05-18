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

	private IGraph G;
	private IGraph GPlusST;
	private IGraph GReduced;
	
	public KPathFinderImplementation(){
		this.G = new Graph();
		this.GPlusST = new Graph();
		this.GReduced = new Graph();
	}
	
	/* (non-Javadoc)
	 * @see path.KPathFinder#obtainReduction(graph.IGraph, java.util.List, java.util.List)
	 */
	@Override
	public IGraph obtainReduction(IGraph arg0, List<Node> arg1, List<Node> arg2) {
		// TODO Auto-generated method stub
		
		this.G = arg0;
		
		return null;
	}

	/* (non-Javadoc)
	 * @see path.KPathFinder#thereExistsKDisjointPaths(graph.IGraph, java.util.List, java.util.List, java.util.Collection)
	 */
	@Override
	public boolean thereExistsKDisjointPaths(IGraph arg0, List<Node> arg1,
			List<Node> arg2, Collection<List<Node>> arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Graph addSourceAndTerminal(Graph gOriginal){
		Graph gPlusST = new Graph(gOriginal);
		
		
		
		return gPlusST;
	}
	
	public Graph getReducedGraph(Graph gPlusST){
		Graph gReduced = new Graph(gPlusST);
		
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
