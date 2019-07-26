package _25.cn.itcast.spt.FloydWarshall;

import java.util.Stack;

import org.junit.Test;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _22.cn.itcast.directedGraph.DirectedGraph2;

public class FloydWarshallForSPTTest {
	@Test
	public void test1() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph<String> graph = new DirectedGraph<>(data);
		graph.addEdge("A", "B", -1.0);
		graph.addEdge("A", "C", 4.0);
		graph.addEdge("B", "C", 3.0);
		graph.addEdge("B", "D", 2.0);
		graph.addEdge("B", "E", 2.0);
		graph.addEdge("D", "B", 1.0);
		graph.addEdge("D", "C", 5.0);
		graph.addEdge("E", "D", -5.0);
		
		FloydWarshallForSPTByAdjacencyMatrix<String> spt = new FloydWarshallForSPTByAdjacencyMatrix<>(graph);
		Stack<String> sp = spt.getShortestPath("A", "D");
		while(!sp.isEmpty()) {
			System.out.println(sp.pop());
		}
		
		System.out.println(spt.getMinDistance("A", "D"));
	}
	
	@Test
	public void test2() {
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
		
		FloydWarshallForSPTByAdjacencyList<String> spt = new FloydWarshallForSPTByAdjacencyList<>(graph);
		Stack<String> sp = spt.getShortestPath("A", "D");
		while(!sp.isEmpty()) {
			System.out.println(sp.pop());
		}
		
		System.out.println(spt.getMinDistance("A", "D"));
	}
}
