package _23.cn.itcast.spt.bellmanFord;

import java.util.Stack;

import org.junit.Test;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _22.cn.itcast.directedGraph.DirectedGraph2;

public class BellmanFordTest {
	// 没有负环的图(邻接矩阵版)
	@Test
	public void testBuildSPT() {
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
		
		BellmanFordForAdjacencyMatrix<String> spt = new BellmanFordForAdjacencyMatrix<>(graph, "A");
		Stack<String> pathTo = spt.pathTo("D");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(spt.minDistanceTo("D"));
		System.out.println(spt.isContainingNegativeCircle());
	}
	
	// 存在负环的图(邻接矩阵版)
	@Test
	public void testBuildSPT2() {
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
		
		BellmanFordForAdjacencyMatrix<String> spt = new BellmanFordForAdjacencyMatrix<>(graph, "A");
		Stack<String> pathTo = spt.pathTo("D");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(spt.minDistanceTo("D"));
		System.out.println(spt.isContainingNegativeCircle());
	}
	
	
	// 没有负环的图(邻接表版)
	@Test
	public void testBuildSPT3() {
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
		
		BellmanFordForAdjacencyList<String> spt = new BellmanFordForAdjacencyList<String>(graph, "A");
		Stack<String> pathTo = spt.pathTo("D");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(spt.minDistanceTo("D"));
		System.out.println(spt.isContainingNegativeCircle());
	}
	
	// 有负环的图(邻接表版)
	@Test
	public void testBuildSPT4() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph2<String> graph = new DirectedGraph2<>(data);
		graph.addEdge("A", "B", -1.0);
		graph.addEdge("A", "C", 4.0);
		graph.addEdge("B", "C", 3.0);
		graph.addEdge("B", "D", 2.0);
		graph.addEdge("B", "E", 2.0);
		graph.addEdge("D", "B", 1.0);
		graph.addEdge("D", "C", 5.0);
		graph.addEdge("E", "D", -5.0);
		
		BellmanFordForAdjacencyList<String> spt = new BellmanFordForAdjacencyList<String>(graph, "A");
		Stack<String> pathTo = spt.pathTo("D");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(spt.minDistanceTo("D"));
		System.out.println(spt.isContainingNegativeCircle());
	}
	
	
	// 没有/ 有负环的图(邻接矩阵版)
	// 只要改一下 ed 边， ed + be + bd < 0 ,那么就是有负环的图
	@Test
	public void testBuildSPT5() {
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
		
		BellmanFordForAdjacencyMatrixWithQueue<String> spt = new BellmanFordForAdjacencyMatrixWithQueue<>(graph, "A");
		Stack<String> pathTo = spt.pathTo("D");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(spt.minDistanceTo("D"));
		System.out.println(spt.isContainingNegativeCircle());
	}
	
	
	// 没有/ 有负环的图(邻接表版)
	// 只要改一下 ed 边， ed + be + bd < 0 ,那么就是有负环的图
	@Test
	public void testBuildSPT6() {
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
		
		BellmanFordForAdjacencyMatrixWithQueue<String> spt = new BellmanFordForAdjacencyMatrixWithQueue<>(graph, "A");
		Stack<String> pathTo = spt.pathTo("D");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(spt.minDistanceTo("D"));
		System.out.println(spt.isContainingNegativeCircle());
	}
}
