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
		Graph gOriginal = GraphGenerator.generateExampleDigraph();
		for(Node n : gOriginal.getNodes()){
		   Graph.printNode(n, gOriginal.getAdjacentEdges(n));
		}
		System.out.println(gOriginal.toString());
		
		KPathFinderImplementation kPathFinder = new KPathFinderImplementation();
		LinkedList<Node> sources = getSources((LinkedList<Node>) gOriginal.getSortedNodes());
		LinkedList<Node> terminals = getTerminals((LinkedList<Node>) gOriginal.getSortedNodes());
		
		Graph gPlusST = kPathFinder.getGraphPlusSourceAndTerminal(gOriginal, sources, terminals);
		System.out.println(gPlusST.toString());
		
		Graph gReduced = kPathFinder.getReducedGraph(gPlusST, gOriginal, kPathFinder.getTopologicalOrder(gPlusST), sources, terminals);
		System.out.println(gReduced.toString());
		
		IGraph gReducedObtained = kPathFinder.obtainReduction(gOriginal, sources, terminals);
		System.out.println(gReducedObtained.toString());
		
		//TODO arg3 ???
		boolean thereExistsKPaths = kPathFinder.thereExistsKDisjointPaths(gOriginal, sources, terminals, null);
		System.out.println("thereExistsKPaths = " + thereExistsKPaths);
		
	}
	
	public static LinkedList<Node> getSources(LinkedList<Node> nodes){
		LinkedList<Node> sources = new LinkedList<Node>();
		Node v1 = nodes.get(0), v2 = nodes.get(1);
		
		sources.add(v1);
		sources.add(v2);
		
		return sources;
	}
	
	public static LinkedList<Node> getTerminals(LinkedList<Node> nodes){
		LinkedList<Node> terminals = new LinkedList<Node>();
		Node v5 = nodes.get(4), v4 = nodes.get(3);
		
		terminals.add(v5);
		terminals.add(v4);
		
		return terminals;
	}
	
	private static void printProjectName() {
		System.out.println("##   EP4 - Path Finder   ##");
		System.out.println("##### Thiago Araujo #######");
		System.out.println("###########################");
		System.out.println("");
	}

}
