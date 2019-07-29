package _27.cn.itcast.criticalPath;

import java.util.Stack;

import org.junit.Test;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _22.cn.itcast.directedGraph.DirectedGraph2;

public class CriticalPathTest {
	@Test
	public void testCriticalPath() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "结束"};
		DirectedGraph<String> graph = new DirectedGraph<>(data);
		graph.addEdge("A", "C", 5.0);
		graph.addEdge("A", "D", 5.0);
		graph.addEdge("C", "F", 2.0);
		graph.addEdge("D", "F", 1.0);
		graph.addEdge("F", "结束", 3.0);
		graph.addEdge("B", "E", 3.0);
		graph.addEdge("E", "G", 4.0);
		graph.addEdge("E", "H", 4.0);
		graph.addEdge("G", "结束", 1.0);
		graph.addEdge("H", "结束", 2.0);
		
		CriticalPathForAdjacencyMatrix<String> cp = new CriticalPathForAdjacencyMatrix<>(graph);
		System.out.println(cp.getLongestPathWeight());
		Stack<String> longestPath = cp.getLongestPath();
		while(!longestPath.isEmpty()) {
			System.out.println(longestPath.pop());
		}
	}
	
	@Test
	public void testCriticalPath2() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "结束"};
		DirectedGraph2<String> graph = new DirectedGraph2<>(data);
		graph.addEdge("A", "C", 5.0);
		graph.addEdge("A", "D", 5.0);
		graph.addEdge("C", "F", 2.0);
		graph.addEdge("D", "F", 1.0);
		graph.addEdge("F", "结束", 3.0);
		graph.addEdge("B", "E", 3.0);
		graph.addEdge("E", "G", 4.0);
		graph.addEdge("E", "H", 4.0);
		graph.addEdge("G", "结束", 1.0);
		graph.addEdge("H", "结束", 2.0);
		
		CriticalPathForAdjacencyList<String> cp = new CriticalPathForAdjacencyList<>(graph);
		System.out.println(cp.getLongestPathWeight());
		Stack<String> longestPath = cp.getLongestPath();
		while(!longestPath.isEmpty()) {
			System.out.println(longestPath.pop());
		}
	}
}
