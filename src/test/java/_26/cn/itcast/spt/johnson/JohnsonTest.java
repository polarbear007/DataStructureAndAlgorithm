package _26.cn.itcast.spt.johnson;

import java.util.Stack;

import org.junit.Test;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _22.cn.itcast.directedGraph.DirectedGraph2;
import _23.cn.itcast.spt.bellmanFord.BellmanFordForAdjacencyMatrix;

public class JohnsonTest {
	@Test
	public void testBellmanFord() {
		String[] data = {"A", "B", "C", "T"};
		DirectedGraph<String> graph = new DirectedGraph<>(data);
		graph.addEdge("A", "B", 2.0);
		graph.addEdge("A", "C", 4.0);
		graph.addEdge("B", "C", -2.0);
		graph.addEdge("C", "A", 3.0);
		graph.addEdge("T", "A", 0.0);
		graph.addEdge("T", "B", 0.0);
		graph.addEdge("T", "C", 0.0);
		
		BellmanFordForAdjacencyMatrix<String> spt = new BellmanFordForAdjacencyMatrix<>(graph, "T");
		System.out.println("T->A:" + spt.minDistanceTo("A"));
		System.out.println("T->B:" + spt.minDistanceTo("B"));
		System.out.println("T->C:" + spt.minDistanceTo("C"));
	}
	
	@Test
	public void testJohnsonSPT() {
		String[] data = {"A", "B", "C"};
		DirectedGraph<String> graph = new DirectedGraph<>(data);
		graph.addEdge("A", "B", 2.0);
		graph.addEdge("A", "C", 4.0);
		graph.addEdge("B", "C", -2.0);
		graph.addEdge("C", "A", 3.0);
		
		JohnsonForSPTByAdjacencyMatrix<String> spt = new JohnsonForSPTByAdjacencyMatrix<>(graph);
		Stack<String> shortestPath = spt.getShortestPath("A", "C");
		while(!shortestPath.isEmpty()) {
			System.out.println(shortestPath.pop());
		}
		
		System.out.println(spt.getMinDistance("A", "C"));
		System.out.println(spt.getMinDistance("B", "A"));
	}
	
	@Test
	public void testJohnsonSPT2() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph<String> graph = new DirectedGraph<>(data);
		graph.addEdge("A", "B", -1.0);
		graph.addEdge("A", "C", 4.0);
		graph.addEdge("B", "C", 3.0);
		graph.addEdge("B", "D", 2.0);
		graph.addEdge("B", "E", 2.0);
		graph.addEdge("D", "B", 1.0);
		graph.addEdge("D", "C", 5.0);
		graph.addEdge("E", "D", -3.0);
		
		JohnsonForSPTByAdjacencyMatrix<String> spt = new JohnsonForSPTByAdjacencyMatrix<>(graph);
		Stack<String> sp = spt.getShortestPath("A", "D");
		while(!sp.isEmpty()) {
			System.out.println(sp.pop());
		}
		
		System.out.println(spt.getMinDistance("A", "D"));
	}
	
	
	@Test
	public void testJohnsonSPT3() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph2<String> graph = new DirectedGraph2<>(data);
		graph.addEdge("A", "B", -1.0);
		graph.addEdge("A", "C", 4.0);
		graph.addEdge("B", "C", 3.0);
		graph.addEdge("B", "D", 2.0);
		graph.addEdge("B", "E", 2.0);
		graph.addEdge("D", "B", 1.0);
		graph.addEdge("D", "C", 5.0);
		graph.addEdge("E", "D", -3.0);
		
		JohnsonForSPTByAdjacencyList<String> spt = new JohnsonForSPTByAdjacencyList<>(graph);
		Stack<String> sp = spt.getShortestPath("A", "D");
		while(!sp.isEmpty()) {
			System.out.println(sp.pop());
		}
		
		System.out.println(spt.getMinDistance("A", "D"));
	}
}
