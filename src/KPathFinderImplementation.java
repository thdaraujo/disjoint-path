import graph.Edge;
import graph.IGraph;
import graph.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import path.KPathFinder;

/**
 * 
 */

/**
 * @author thiagoaraujo Implements KPathFinder interface.
 */
public class KPathFinderImplementation implements KPathFinder {

	private LinkedList<Node> topologicalOrder;
	private HashMap<Node, Integer> fOrder;
	private Node S;
	private Node T;
	private Node reducedStart;
	private Node reducedTerminal;
	private HashMap<Node, LinkedList<Node>> mapNodesGraphSTToReduced; //maps a node from graphST to a list of nodes from the reduced graph
	
	public KPathFinderImplementation() {
		this.topologicalOrder = new LinkedList<Node>();
		this.fOrder = new HashMap<Node, Integer>();
		this.S = new Node("s"); // source
		this.T = new Node("t"); // terminal
		this.mapNodesGraphSTToReduced = new HashMap<Node, LinkedList<Node>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.KPathFinder#obtainReduction(graph.IGraph, java.util.List,
	 * java.util.List)
	 */
	@Override
	public IGraph obtainReduction(IGraph G, List<Node> sources,
			List<Node> terminals) {
		Graph GOriginal = new Graph(G);
		Graph GPlusST = this.getGraphPlusSourceAndTerminal(GOriginal, this.S,
				this.T, sources, terminals);

		List<Node> topologicalOrder = this
				.getTopoligcalOrderToUseInReduction(G);

		Graph GReduced = this.getReducedGraph(GOriginal, GPlusST,
				topologicalOrder, new LinkedList<Node>(sources),
				new LinkedList<Node>(terminals));

		return GReduced;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.KPathFinder#thereExistsKDisjointPaths(graph.IGraph,
	 * java.util.List, java.util.List, java.util.Collection)
	 */
	@Override
	public boolean thereExistsKDisjointPaths(IGraph G, List<Node> sources,
			List<Node> terminals, Collection<List<Node>> kPaths) {
		// TODO Auto-generated method stub
		return false;
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
			System.out
					.println("sources and terminals should have the same size!");
			// return gPlusST;
		}

		// S points to every source of G
		// S -> sources
		for (Node source : sources) {
			Edge StoSource = new Edge(S, source);
			gPlusST.addEdge(StoSource);
		}

		// Every terminal in G points to T
		// terminal -> T
		for (Node terminal : terminals) {
			Edge terminalToT = new Edge(terminal, T);
			gPlusST.addEdge(terminalToT);
		}

		return gPlusST;
	}

	public Graph getReducedGraph(Graph gOriginal, Graph gPlusST,
			List<Node> topologicalOrder, List<Node> sources,
			List<Node> terminals) {

		HashMap<Node, Node> sourcesMap = new HashMap<Node, Node>();
		HashMap<Node, Node> terminalsMap = new HashMap<Node, Node>();

		for (Node n : sources) {
			sourcesMap.put(n, n);
		}

		for (Node n : terminals) {
			terminalsMap.put(n, n);
		}

		putAllInOrder(topologicalOrder);

		Graph gReduced = new Graph();

		// permutation v x v
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

		for (Node vi : gReduced.getNodes()) {
			for (Node vj : gReduced.getNodes()) {
				// get node that starts a path
				Edge e = new Edge(vi, vj);

				// verify conditions
				boolean validEdge = verifyConditions(e, gPlusST, sourcesMap,
						terminalsMap);

				if (validEdge) {
					// add edge to g-reduced
					gReduced.addEdge(e);
					System.out.println(Graph.edgeToString(e)
							+ " added to g-reduced");
				}
			}
		}

		// path
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(gReduced,
				this.reducedStart);
		boolean hasKpaths = bfs.hasPathTo(this.reducedTerminal);

		System.out.println("has k paths = " + hasKpaths);
		if (hasKpaths) {
			Stack<Node> path = bfs.pathTo(this.reducedTerminal);
			while (!path.isEmpty()) {
				Node p = path.pop();
				System.out.println(Graph.labelToString(p.getLabel()));
			}
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
	
		return gReduced;
	}

	public List<Node> crossProduct(int k, List<Node> a, List<Node> b) {
		if (k - 1 == 0)
			return a;
		else {
			LinkedList<Node> crossProduct = new LinkedList<Node>();
			for (Node vi : a) {
				for (Node vj : b) {
					Node product = nodeProduct(vi, vj);
					crossProduct.add(product);
				}
			}
			return crossProduct(k - 1, crossProduct, b);
		}
	}

	public Node nodeProduct(Node a, Node b) {
		Node n;
		if (a.getLabel() instanceof List<?>) {
			List<Node> label = new LinkedList<Node>();
			List<Node> nodeList = (List<Node>) a.getLabel();
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
		mapReducedNode(b, n);
		return n;
	}

	private Edge getReducedEdge(List<Node> nodesFrom, List<Node> nodesTo) {
		if (nodesFrom.size() != nodesTo.size())
			return null;

		int size = nodesFrom.size();

		for (int i = 0; i < size; i++) {
			Node from = nodesFrom.get(i);
			Node to = nodesTo.get(i);
			if (from != to)
				return new Edge(from, to);
		}
		return null;
	}

	/*
	 * First condition: one of the edges exists on the original graph.
	 */
	private boolean verifyConditions(Edge e, Graph gPlusST,
			HashMap<Node, Node> sources, HashMap<Node, Node> terminals) {
		List<Node> vNodes = (List<Node>) e.getFrom().getLabel(), wNodes = (List<Node>) e
				.getTo().getLabel();

		int diff = 0;
		Node from = e.getFrom(), to = e.getTo();

		if (vNodes.size() != wNodes.size())
			return false;

		for (int i = 0; i < vNodes.size(); i++) {
			Node v_i = vNodes.get(i), w_i = wNodes.get(i);

			Edge e_i = new Edge(v_i, w_i);
			if (v_i != w_i) {
				diff++;
				boolean firstCondition = edgeSatisfiesFirstCondition(e_i,
						gPlusST), secondCondition = edgeSatisfiesSecondCondition(
						v_i, w_i, this.S, this.T, sources, terminals), thirdCondition = edgeSatisfiesThirdCondition(
						v_i, w_i, from, to, this.T);

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
	 * Second condition: from is s and to is a source or to is t and from is
	 * terminal
	 */
	private boolean edgeSatisfiesSecondCondition(Node v_i, Node w_i, Node S,
			Node T, HashMap<Node, Node> sources, HashMap<Node, Node> terminals) {
		if (v_i == S && !sources.containsKey(w_i)) {
			return false;
		} else if (w_i == T && !terminals.containsKey(v_i)) {
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
			// f(w_i) > f(all w_j in to where i != j)
			// f(w_i) max(f(to))
			if (to.getLabel() instanceof List<?>) {
				List<Node> nodeList = (List<Node>) to.getLabel();
				int diff = 0;
				for(Node n : nodeList){
					if(n == w_i) diff++;
					else if(getOrder(w_i) <= getOrder(n)) return false;
				}
				return diff == 1;
			}
			else{
				return true;
			}	
		}
	}

	/*
	 * Get position of node in the topological order
	 */
	private int f(Node n, List<Node> topologicalOrder) {
		return topologicalOrder.indexOf(n);
	}

	private int getOrder(Node n) {
		if (this.fOrder.containsKey(n)) {
			return this.fOrder.get(n);
		} else {
			System.out.println("key not found in fOrder!");
			return -1;
		}
	}

	private void putAllInOrder(List<Node> topologicalOrder) {
		for (int i = 0; i < topologicalOrder.size(); i++) {
			this.fOrder.put(topologicalOrder.get(i), i);
		}
	}

	private void putInOrder(Node n, int value) {
		this.fOrder.put(n, value);
	}
	
	private void mapReducedNode(Node n, Node adj){
		if(!this.mapNodesGraphSTToReduced.containsKey(n)){
			this.mapNodesGraphSTToReduced.put(n, new LinkedList<Node>());
		}
		this.mapNodesGraphSTToReduced.get(n).add(adj);
	}
}
