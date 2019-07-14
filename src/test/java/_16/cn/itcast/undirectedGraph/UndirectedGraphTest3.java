package _16.cn.itcast.undirectedGraph;

import org.junit.Test;

public class UndirectedGraphTest3 {
	@Test
	public void testAddEdge() {
		String[] data = {"A", "B", "C", "D", "E"};
		UndirectedGraph3<String> graph = new UndirectedGraph3<>(data);
		graph.addEdge("A", "B", 3);
		graph.addEdge("A", "D");
		graph.addEdge("A", "E");
		graph.addEdge("B", "C", 5);
		graph.addEdge("B", "E");
		graph.addEdge("C", "D");
		graph.addEdge("C", "E");
		graph.addEdge("D", "E");
		System.out.println(graph);
		System.out.println(graph.getVertexCount());
		System.out.println(graph.getEdgeCount());
	}
}
