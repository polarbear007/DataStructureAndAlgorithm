package _16.cn.itcast.undirectedGraph;

import org.junit.Test;

public class BreadthFirstSearchTest1 {
	@Test
	public void testBfs() {
		// 先创建一个图对象
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
		// 再创建一个广度优先遍历的对象
		BreadthFirstSearch1<String> search = new BreadthFirstSearch1<>(graph);
		// search.bfsFromVertex("A");
		search.bfs();
	}
}
