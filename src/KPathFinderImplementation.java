import graph.Edge;
import graph.IGraph;
import graph.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import path.KPathFinder;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 * 
 */

/**
 * @author thiagoaraujo Implements KPathFinder interface.
 */
public class KPathFinderImplementation implements KPathFinder {

	private LinkedList<Node> topologicalOrder;
	private HashMap<Node, Integer> fOrder; //position of node (or the max position) in the topological order.
	private Node S;
	private Node T;
	private Node reducedStart;
	private Node reducedTerminal;
	private HashMap<Node, LinkedList<Node>> mapNodesGraphSTToReduced; //maps a node from graphST to a list of nodes from the reduced graph
	private Graph GReduced;
	
	public KPathFinderImplementation() {
		this.topologicalOrder = new LinkedList<Node>();
		this.fOrder = new HashMap<Node, Integer>();
		this.S = new Node("s"); // source
		this.T = new Node("t"); // terminal
		this.mapNodesGraphSTToReduced = new HashMap<Node, LinkedList<Node>>();
		this.GReduced = new Graph();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.KPathFinder#obtainReduction(graph.IGraph, java.util.List,
	 * java.util.List)
	 */
	@Override
	public IGraph obtainReduction(IGraph G, List<Node> sources, List<Node> terminals) {
		Graph GOriginal = new Graph(G);
		Graph GPlusST = this.getGraphPlusSourceAndTerminal(GOriginal, this.S, this.T, sources, terminals);

		List<Node> topologicalOrder = this.getTopoligcalOrderToUseInReduction(G);

		Graph GReduced = this.getReducedGraph(GOriginal, GPlusST,
				topologicalOrder, new LinkedList<Node>(sources),
				new LinkedList<Node>(terminals));

		this.GReduced = GReduced;
		return this.GReduced;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.KPathFinder#thereExistsKDisjointPaths(graph.IGraph,
	 * java.util.List, java.util.List, java.util.Collection)
	 * 
	 * G = reduced graph (GReduced) obtained by the method "obtainReduction".
	 */
	@Override
	public boolean thereExistsKDisjointPaths(IGraph gReduced, List<Node> sources,
			List<Node> terminals, Collection<List<Node>> kPaths) {
		
		LinkedList<LinkedList<Node>> pathCollection = getKDisjointPaths(this.GReduced, sources.size());
		
		for(LinkedList<Node> pathList : pathCollection){
			kPaths.add(pathList);
		}
		
		return kPaths.size() > 0;
	}

	@Override
	public List<Node> getTopoligcalOrderToUseInReduction(IGraph g) {
		if (this.topologicalOrder == null || this.topologicalOrder.isEmpty()) {
			System.out.println("Getting new topological order for G");
			Graph gOriginal = new Graph(g);
			this.topologicalOrder = getTopologicalOrder(gOriginal);
			this.topologicalOrder.addFirst(this.S);
			this.topologicalOrder.addLast(this.T);
		}
		return this.topologicalOrder;
	}

	/*
	 * Returns a topological order for the graph. If no topological order
	 * exists, returns null (for example: graph has a cycle, and is not a DAG).
	 */
	public LinkedList<Node> getTopologicalOrder(Graph g) {
		Topological topologicalOrder = new Topological(g);

		System.out.println(topologicalOrder.toString());

		if (topologicalOrder.hasOrder()) {
			LinkedList<Node> order = topologicalOrder.order();
			return order;
		} else {
			System.out.println("Graph is not a DAG!");
			return null;
		}
	}

	public Graph getGraphPlusSourceAndTerminal(Graph gOriginal, Node S, Node T,
			List<Node> sources, List<Node> terminals) {
		Graph gPlusST = new Graph(gOriginal);

		gPlusST.addNode(S);
		gPlusST.addNode(T);

		if (sources.size() != terminals.size()) {
			System.out.println("sources and terminals should have the same size!");
			// return gPlusST;
		}

		// S points to every source of G
		for (Node source : sources) {
			Edge StoSource = new Edge(S, source);
			gPlusST.addEdge(StoSource);
		}

		// Every terminal in G points to T
		for (Node terminal : terminals) {
			Edge terminalToT = new Edge(terminal, T);
			gPlusST.addEdge(terminalToT);
		}

		return gPlusST;
	}

	public Graph getReducedGraph(Graph gOriginal, Graph gPlusST,
			List<Node> topologicalOrder, List<Node> sources,
			List<Node> terminals) {
	
		//f(v) for all v
		putAllInOrder(topologicalOrder);

		Graph gReduced = new Graph();

		// permutation v x v
		addNodesByPermutation(topologicalOrder, sources, gReduced);

		//add edges (naive version)
		addEdgesNaiveVersion(gPlusST, sources, terminals, gReduced);

		//faster version
		//addEdgesByAdjacencyVersion(topologicalOrder, gPlusST, mapNodesGraphSTToReduced, gReduced, S, sources, terminals);
		return gReduced;
	}

	/*
	 * Add nodes by permutation.
	 * Not every node is needed for the solution, but we create them anyway.
	 */
	private void addNodesByPermutation(List<Node> topologicalOrder,
			List<Node> sources, Graph gReduced) {
		int k = sources.size();
		List<Node> crossProduct = new LinkedList<Node>();
		crossProduct = crossProduct(k, topologicalOrder, topologicalOrder);

		if (crossProduct != null && !crossProduct.isEmpty()) {
			this.reducedStart = crossProduct.get(0); // first = {s, s, ..., s}
			this.reducedTerminal = crossProduct.get(crossProduct.size() - 1); // last = {t, t, ..., t}
		}

		for (Node n : crossProduct) {
			gReduced.addNode(n);
		}
	}
	
	/*
	 * Naive version: every combination of 2-nodes in gReduced is a candidate for the addition of an edge.
	 * If the edge satisfies 3 conditions for its existance, then it is added to gReduced.
	 */
	private void addEdgesNaiveVersion(Graph gPlusST, List<Node> sources,
			List<Node> terminals, Graph gReduced) {
		
		System.out.println("addEdgesNaiveVersion");
		
		for (Node vi : gReduced.getNodes()) {
			for (Node vj : gReduced.getNodes()) {
				// get node that starts a path
				Edge e = new Edge(vi, vj);
				// verify conditions
				boolean validEdge = verifyConditions(e, gPlusST, sources, terminals);

				if (validEdge) { // add edge to g-reduced
					gReduced.addEdge(e);
				}
			}
		}
	}
	
	/*
	 * Faster version: nodes mapped by adjacency are candidates for the addition of an edge.
	 * We map v nodes from the original graph to a list of nodes of the reduced graph.
	 * Then, we use the adjacent nodes of v to obtain the candidates. 
	 */
	private void addEdgesByAdjacencyVersion(List<Node> topologicalOrder,
			Graph gPlusST, 
			HashMap<Node, LinkedList<Node>> mapNodesGraphSTToReduced, 
			Graph gReduced,
			Node S,
			List<Node> sources,
			List<Node> terminals) {
		
		System.out.println("addEdgesByAdjacencyVersion");
		
		for (Node key : mapNodesGraphSTToReduced.keySet()) {
			List<Node> keyList = mapNodesGraphSTToReduced.get(key);

			for (Node adj : gPlusST.getAdjacentNodes(key)) {
				List<Node> adjList = mapNodesGraphSTToReduced.get(adj);

				for (Node v_i : keyList){
					for (Node v_j : keyList){
						Edge e = new Edge(v_i, v_j);
						if(verifyConditions(e, gPlusST, sources, terminals)){
							gReduced.addEdge(e);
						}
					}
				}

				for (Node v_i : keyList){
					for (Node v_j : adjList){
						Edge e = new Edge(v_i, v_j);
						if(verifyConditions(e, gPlusST, sources, terminals)){
							gReduced.addEdge(e);
						}
					}
				}
			}
		}

		
		
	}

	/*
	 * cross product between topological orders with depth of recursion equals k.
	 */
	public List<Node> crossProduct(int k, List<Node> a, List<Node> b) {
		if (k - 1 == 0)
			return a;
		else {
			LinkedList<Node> crossProduct = new LinkedList<Node>();
			for (Node vi : a) {
				for (Node vj : b) {
					Node product = nodeProduct(vi, vj, k);
					crossProduct.add(product);
				}
			}
			return crossProduct(k - 1, crossProduct, b);
		}
	}
	
	/*
	 * get k disjoint paths for reduced graph using BFS.
	 */
	private LinkedList<LinkedList<Node>> getKDisjointPaths(IGraph gReduced, int k) {
		LinkedList<LinkedList<Node>> collection = new LinkedList<LinkedList<Node>>();
		for(int i = 0; i < k; i++){
			collection.add(new LinkedList<Node>());
		}
		
		// path
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(gReduced, this.reducedStart);
		boolean hasKpaths = bfs.hasPathTo(this.reducedTerminal);
	
		if (hasKpaths) {
			System.out.println("has k paths = " + hasKpaths);
			Stack<Node> path = bfs.pathTo(this.reducedTerminal);
			while(!path.isEmpty()){
				Node p = path.pop();
				List<Node> nodeList = getNodeList(p);
				for(int i = 0; i < k; i++){
					Node pathNode = nodeList.get(i);
					if(pathNode != this.S && pathNode != this.T && !collection.get(i).contains(pathNode)){
						collection.get(i).add(pathNode);
					}
				}
			}
		}
		else{
			System.out.println("k paths not found!");
		}
		
		/* TODO usar no add edges
		System.out.println("adj map ");
		for(Node n : this.mapNodesGraphSTToReduced.keySet()){
			List<Node> adj = this.mapNodesGraphSTToReduced.get(n);
			System.out.print(Graph.labelToString(n.getLabel()) + " --> ");
			for(Node i : adj){
				System.out.print(Graph.labelToString(i.getLabel()) + " ");
			}
			System.out.println("");
		}
		*/
		
		return collection;
	}

	private List<Node> getNodeList(Node p) {
		List<Node> nodeList = (List<Node>)p.getLabel();
		return nodeList != null? nodeList : new LinkedList<Node>();
	}

	/*
	 * Cross product of node {a} x {b}
	 */
	public Node nodeProduct(Node a, Node b, int k) {
		Node n;
		if (a.getLabel() instanceof List<?>) {
			List<Node> label = new LinkedList<Node>();
			List<Node> nodeList = getNodeList(a);
			label.addAll(nodeList);
			label.add(b);
			n = new Node(label);
		} else {
			List<Node> label = new LinkedList<Node>();
			label.add(a);
			label.add(b);
			n = new Node(label);
		}
		putInOrder(n, Math.max(getOrder(a), getOrder(b)));
		if (k - 2 == 0){ //add on the last iteration
			mapReducedNode(b, n);
		}
		return n;
	}

	/*
	 * Verifies three conditions for the existence of an edge between two nodes on the reduced Graph.
	 */
	private boolean verifyConditions(Edge e, Graph gPlusST,
			List<Node> sources, List<Node> terminals) {
		List<Node> vNodes = (List<Node>) e.getFrom().getLabel(), 
				   wNodes = (List<Node>) e.getTo().getLabel();

		int diff = 0;
		Node from = e.getFrom(), to = e.getTo();

		if (vNodes.size() != wNodes.size())
			return false;

		for (int i = 0; i < vNodes.size(); i++) {
			Node v_i = vNodes.get(i), w_i = wNodes.get(i);

			Edge e_i = new Edge(v_i, w_i);
			if (v_i != w_i) {
				diff++;
				boolean firstCondition  = edgeSatisfiesFirstCondition(e_i, gPlusST), 
						secondCondition = edgeSatisfiesSecondCondition(v_i, w_i, this.S, this.T, sources, terminals, i), 
						thirdCondition  = edgeSatisfiesThirdCondition(v_i, w_i, from, to, this.T);

				if (!(firstCondition && secondCondition && thirdCondition))
					return false;
			}
		}
		return diff == 1;
	}

	/*
	 * First condition: graph ST contains the edge.
	 */
	private boolean edgeSatisfiesFirstCondition(Edge e, Graph gPlusST) {
		if (gPlusST.containsEdge(e)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Second condition: v_i is S and w_i = sources(i) or w_i is T and v_i = terminals(i)
	 */
	private boolean edgeSatisfiesSecondCondition(Node v_i, Node w_i, Node S,
			Node T, List<Node> sources, List<Node> terminals, int index) {
		if (v_i == S && sources.get(index) != w_i){ //!sources.containsKey(w_i)) {
			return false;
		} else if (w_i == T && terminals.get(index) != v_i){ //!terminals.containsKey(v_i)) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Third condition: edge is T or its position in the topological order is
	 * maximum.
	 */
	private boolean edgeSatisfiesThirdCondition(Node v_i, Node w_i, Node from,
			Node to, Node T) {
		if (w_i == T)
			return true;
		else { 
			// f(w_i) has the max order inside node `to`.
			int max = -1;
			int wOrder = getOrder(w_i);
			
			if (to.getLabel() instanceof List<?>) {
				List<Node> nodeList = getNodeList(to);
				int diff = 0;
				
				for(Node n : nodeList){
					int nOrder = getOrder(n);
					if(n == w_i) diff++;
					else{
						if(nOrder > max) max = nOrder;
					}
				}
				return wOrder > max && diff == 1;
			}
			else{
				return true;
			}	
		}
	}

	/*
	 * Gets position of node in the topological order
	 */
	private int f(Node n, List<Node> topologicalOrder) {
		return topologicalOrder.indexOf(n);
	}

	/*
	 * gets position of node in fOrder.
	 */
	private int getOrder(Node n) {
		if (this.fOrder.containsKey(n)) {
			return this.fOrder.get(n);
		} else {
			System.out.println("key not found in fOrder!");
			return -1;
		}
	}

	/*
	 * Saves the order only once.
	 */
	private void putInOrder(Node n, int value) {
		this.fOrder.put(n, value);
	}

	private void putAllInOrder(List<Node> topologicalOrder) {
		for (int i = 0; i < topologicalOrder.size(); i++) {
			this.fOrder.put(topologicalOrder.get(i), i);
		}
	}
	
	/*
	 * Maps a node from the original graph to a list of nodes in the reduced graph
	 * when the original graph has an edge to the nodes present in adj.
	 * We use it to minimize the set of candidate nodes for the addition of edges.
	 */
	private void mapReducedNode(Node n, Node adj){
		if(!this.mapNodesGraphSTToReduced.containsKey(n)){
			this.mapNodesGraphSTToReduced.put(n, new LinkedList<Node>());
		}
		this.mapNodesGraphSTToReduced.get(n).add(adj);
	}
}
