package _16.cn.itcast.undirectedGraph;

import org.junit.Test;

public class DepthFirstSearchTest2 {
	@Test
	public void testDfs() {
		// 先创建一个图对象
		String[] data = {"A", "B", "C", "D", "E"};
		UndirectedGraph2<String> graph = new UndirectedGraph2<>(data);
		graph.addEdge("A", "B");
		graph.addEdge("A", "D");
		graph.addEdge("A", "E");
		graph.addEdge("B", "C");
		graph.addEdge("B", "E");
		graph.addEdge("C", "D");
		graph.addEdge("C", "E");
		graph.addEdge("D", "E");
		// 再创建一个广度优先遍历的对象
		DepthFirstSearch2<String> search = new DepthFirstSearch2<>(graph);
		search.dfs();
	}
}
