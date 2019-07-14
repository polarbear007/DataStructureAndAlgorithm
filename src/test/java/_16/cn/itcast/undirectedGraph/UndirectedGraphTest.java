package _16.cn.itcast.undirectedGraph;

import org.junit.Test;

public class UndirectedGraphTest {
	@Test
	public void testAddEdge() {
		String[] data = {"A", "B", "C", "D", "E"};
		UndirectedGraph<String> graph = new UndirectedGraph<>(data);
		graph.addEdge("A", "B");
		graph.addEdge("A", "D");
		graph.addEdge("A", "E");
		graph.addEdge("B", "C");
		graph.addEdge("B", "E");
		graph.addEdge("C", "D");
		graph.addEdge("C", "E");
		graph.addEdge("D", "E");
		System.out.println(graph);
		System.out.println(graph.getVertexCount());
		System.out.println(graph.getEdgeCount());
	}
}
