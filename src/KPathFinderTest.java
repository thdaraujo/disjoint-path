import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import graph.Edge;
import graph.IGraph;
import graph.Node;

/**
 * 
 */

/**
 * @author thiagoaraujo
 *
 */
public class KPathFinderTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		printProjectName();
		
		runTestFromExample();
		
	}
	
	public static void runTestFromExample(){
		Graph gOriginal = generateExampleDigraph();
		for(Node n : gOriginal.getNodes()){
			print(n, gOriginal.getAdjacentEdges(n));
		}
		System.out.println(gOriginal.toString());
		
		KPathFinderImplementation kPathFinder = new KPathFinderImplementation();
		LinkedList<Node> sources = getSources((LinkedList<Node>) gOriginal.getSortedNodes());
		LinkedList<Node> terminals = getTerminals((LinkedList<Node>) gOriginal.getSortedNodes());
		
		Graph gPlusST = kPathFinder.getGraphPlusSourceAndTerminal(gOriginal, sources, terminals);
		System.out.println(gPlusST.toString());
		
		Graph gReduced = kPathFinder.getReducedGraph(gPlusST, gOriginal, kPathFinder.getTopologicalOrder(gPlusST));
		System.out.println(gReduced.toString());
		
		IGraph gReducedObtained = kPathFinder.obtainReduction(gOriginal, sources, terminals);
		System.out.println(gReducedObtained.toString());
		
		boolean thereExistsKPaths = kPathFinder.thereExistsKDisjointPaths(gOriginal, sources, terminals, null);
		System.out.println("thereExistsKPaths = " + thereExistsKPaths);
		
	}
	
	public static Graph generateSimpleDigraph(int count){
		Graph simpleDigraph = new Graph();
		
		for(int i = 1; i <= count; i++){
			simpleDigraph.addNode(new Node(i));
		}
		
		LinkedList<Node> nodes = new LinkedList<Node>(simpleDigraph.getNodes());
		
		while(!nodes.isEmpty()){
			Node from = nodes.pop();
			Node to = nodes.pop();
			
			if(from != null && to != null){
				Edge e = new Edge(from, to);
				simpleDigraph.addEdge(e);
			}
		}
		return simpleDigraph;
	}
	
	public static Graph generateExampleDigraph(){
		Graph dg = new Graph();
		
		for(int i = 1; i <= 5; i++){
			dg.addNode(new Node(i));
		}
		
		LinkedList<Node> nodes = new LinkedList<Node>(dg.getSortedNodes());
		
		//1->2
		Node from = nodes.get(0);
		Node to = nodes.get(1);
		Edge e = new Edge(from, to);
		dg.addEdge(e);
		
		//1->5
		from = nodes.get(0);
		to = nodes.get(4);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		//2->3
		from = nodes.get(1);
		to = nodes.get(2);
		e = new Edge(from, to);
		dg.addEdge(e);

		//2->4
		from = nodes.get(1);
		to = nodes.get(3);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		//5->4
		from = nodes.get(4);
		to = nodes.get(3);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		return dg;
	}
	
	public static LinkedList<Node> getSources(LinkedList<Node> nodes){
		LinkedList<Node> sources = new LinkedList<Node>();
		Node s1 = nodes.get(0), s2 = nodes.get(2);
		
		sources.add(s1);
		sources.add(s2);
		
		return sources;
	}
	
	public static LinkedList<Node> getTerminals(LinkedList<Node> nodes){
		LinkedList<Node> terminals = new LinkedList<Node>();
		Node t1 = nodes.get(3), t2 = nodes.get(4);
		
		terminals.add(t1);
		terminals.add(t2);
		
		return terminals;
	}
	
	public static void print(Node n, Collection<Edge> adjEdges){
		System.out.println("Node: " + n.getLabel());
		System.out.println("Edges: " + adjEdges.size());
		
		for(Edge e : adjEdges){
			System.out.println(e.getFrom().getLabel() + "->" + e.getTo().getLabel());
		}
		System.out.println("");
	}
	
	private static void printProjectName() {
		System.out.println("##   EP4 - Path Finder   ##");
		System.out.println("##### Thiago Araujo #######");
		System.out.println("###########################");
		System.out.println("");
	}

}
