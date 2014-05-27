import graph.Edge;
import graph.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class GraphGenerator {
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
		
		//1->3
		from = nodes.get(0);
		to = nodes.get(2);
		e = new Edge(from, to);
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

		//2->5
		from = nodes.get(1);
		to = nodes.get(4);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		//3->4
		from = nodes.get(2);
		to = nodes.get(3);
		e = new Edge(from, to);
		dg.addEdge(e);
		
		return dg;
	}
	
	public static void santiagoTest(Graph graph, List<Node> s, List<Node> t) {
		Node n1 = new Node("1");
		Node n2 = new Node("2");
		Node n3 = new Node("3");
		Node n4 = new Node("4");
		Node n5 = new Node("5");
		
		Edge e1 = new Edge(n1, n2);
		Edge e2 = new Edge(n1, n3);
		Edge e3 = new Edge(n1, n5);
		Edge e4 = new Edge(n3, n4);
		Edge e6 = new Edge(n2, n3);
		Edge e7 = new Edge(n2, n5);
		
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
		graph.addNode(n5);
		
		
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);
		graph.addEdge(e6);
		graph.addEdge(e7);
		
		s.add(n1); s.add(n2);
		t.add(n5); t.add(n4);
		
		return;
	}
	
	public static int disjointBigGraph(Graph graph, List<Node> si, List<Node> ti) {
		Set<Node> nodes = new HashSet<Node>();

        Node n00 = new Node(new String("00"));
        Node n01 = new Node(new String("01"));
        Node n02 = new Node(new String("02"));
        Node n03 = new Node(new String("03"));
        Node n04 = new Node(new String("04"));
        Node n05 = new Node(new String("05"));
        Node n06 = new Node(new String("06"));
        Node n07 = new Node(new String("07"));
        Node n08 = new Node(new String("08"));
        Node n09 = new Node(new String("09"));
        Node n10 = new Node(new String("10"));
        Node n11 = new Node(new String("11"));
        Node n12 = new Node(new String("12"));
        Node n13 = new Node(new String("13"));
        Node n14 = new Node(new String("14"));
        Node n15 = new Node(new String("15"));
        Node n16 = new Node(new String("16"));
        Node n17 = new Node(new String("17"));
        Node n18 = new Node(new String("18"));
        Node n19 = new Node(new String("19"));

        nodes.add(n00);
        nodes.add(n01);
        nodes.add(n02);
        nodes.add(n03);
        nodes.add(n04);
        nodes.add(n05);
        nodes.add(n06);
        nodes.add(n07);
        nodes.add(n08);
        nodes.add(n09);
        nodes.add(n10);
        nodes.add(n11);
        nodes.add(n12);
        nodes.add(n13);
        nodes.add(n14);
        nodes.add(n15);
        nodes.add(n16);
        nodes.add(n17);
        nodes.add(n18);
        nodes.add(n19);

        for(Node n : nodes){
        	graph.addNode(n);
        }

        graph.addEdge(new Edge(n00, n16));
        graph.addEdge(new Edge(n00, n04));
        graph.addEdge(new Edge(n00, n14));
        graph.addEdge(new Edge(n11, n15));
        graph.addEdge(new Edge(n11, n06));
        graph.addEdge(new Edge(n06, n05));
        graph.addEdge(new Edge(n04, n05));
        graph.addEdge(new Edge(n03, n06));
        graph.addEdge(new Edge(n06, n09));
        graph.addEdge(new Edge(n09, n07));
        graph.addEdge(new Edge(n07, n08));
        graph.addEdge(new Edge(n07, n10));
        graph.addEdge(new Edge(n03, n07));
        graph.addEdge(new Edge(n04, n03));
        graph.addEdge(new Edge(n03, n01));
        graph.addEdge(new Edge(n01, n02));
        graph.addEdge(new Edge(n12, n01));
        graph.addEdge(new Edge(n10, n13));
        graph.addEdge(new Edge(n16, n17));
        graph.addEdge(new Edge(n17, n18));
        graph.addEdge(new Edge(n19, n18));
        graph.addEdge(new Edge(n12, n19));
        graph.addEdge(new Edge(n17, n12));

        si.add(n00);
        ti.add(n06);
        si.add(n01);
        ti.add(n02);
        
        /*
        si.add(n16);
        ti.add(n19);
        si.add(n09);
        ti.add(n13);
        si.add(n11);
        ti.add(n15);
        */
        
        return 5;
	}
}
