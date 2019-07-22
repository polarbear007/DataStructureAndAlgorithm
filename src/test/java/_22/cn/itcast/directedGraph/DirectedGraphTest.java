package _22.cn.itcast.directedGraph;

import org.junit.Test;

public class DirectedGraphTest {
	@Test
	public void testAddEdge() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph<String> graph = new DirectedGraph<>(data);
		graph.addEdge("A", "B", 5.0);
		graph.addEdge("A", "D", 3.0);
		graph.addEdge("A", "E", -2.0);
		graph.addEdge("B", "C", 1.0);
		graph.addEdge("B", "E", 4.0);
		graph.addEdge("C", "D", 3.0);
		graph.addEdge("C", "E", -1.0);
		graph.addEdge("D", "E", 7.0);
		System.out.println(graph);
		System.out.println(graph.getVertexCount());
		System.out.println(graph.getEdgeCount());
	}
	
	@Test
	public void testAddEdge2() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph2<String> graph = new DirectedGraph2<>(data);
		graph.addEdge("A", "B", 5.0);
		graph.addEdge("A", "D", 3.0);
		graph.addEdge("A", "E", -2.0);
		graph.addEdge("B", "C", 1.0);
		graph.addEdge("B", "E", 4.0);
		graph.addEdge("C", "D", 3.0);
		graph.addEdge("C", "E", -1.0);
		graph.addEdge("D", "E", 7.0);
		System.out.println(graph);
		System.out.println(graph.getVertexCount());
		System.out.println(graph.getEdgeCount());
	}
}
