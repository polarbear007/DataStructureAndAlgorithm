package _20.cn.itcast.primMST;

import org.junit.Test;

import _16.cn.itcast.undirectedGraph.UndirectedGraph;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3;

public class PrimMSTTest {
	@Test
	public void testPrimMST1() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G"};
		UndirectedGraph<String> graph = new UndirectedGraph<>(data);
		graph.addEdge("A", "F", 1);
		graph.addEdge("A", "B", 2);
		graph.addEdge("B", "C", 3);
		graph.addEdge("C", "D", 4);
		graph.addEdge("D", "E", 5);
		graph.addEdge("E", "F", 6);
		graph.addEdge("F", "G", 7);
		graph.addEdge("B", "G", 8);
		graph.addEdge("C", "G", 9);
		graph.addEdge("E", "G", 10);
		
		PrimMST1<String> primMST = new PrimMST1<>(graph);
		UndirectedGraph<String> mst = primMST.getMST();
		System.out.println(mst);
	}
	
	@Test
	public void testPrimMST2() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G"};
		UndirectedGraph3<String> graph = new UndirectedGraph3<>(data);
		graph.addEdge("A", "F", 1);
		graph.addEdge("A", "B", 2);
		graph.addEdge("B", "C", 3);
		graph.addEdge("C", "D", 4);
		graph.addEdge("D", "E", 5);
		graph.addEdge("E", "F", 6);
		graph.addEdge("F", "G", 7);
		graph.addEdge("B", "G", 8);
		graph.addEdge("C", "G", 9);
		graph.addEdge("E", "G", 10);
		
		PrimMST2<String> primMST2 = new PrimMST2<>(graph);
		UndirectedGraph3<String> mst = primMST2.getMST();
		System.out.println(mst);
	}
}
