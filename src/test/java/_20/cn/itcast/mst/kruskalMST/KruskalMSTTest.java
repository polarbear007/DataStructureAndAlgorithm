package _20.cn.itcast.mst.kruskalMST;

import org.junit.Test;

import _16.cn.itcast.undirectedGraph.UndirectedGraph;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3;

public class KruskalMSTTest {
	@Test
	public void testKruskalMST1() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G"};
		UndirectedGraph<String> graph = new UndirectedGraph<>(data);
		graph.addEdge("A", "B", 12);
		graph.addEdge("A", "F", 16);
		graph.addEdge("A", "G", 14);
		graph.addEdge("B", "C", 10);
		graph.addEdge("B", "F", 7);
		graph.addEdge("C", "D", 3);
		graph.addEdge("C", "E", 5);
		graph.addEdge("C", "F", 6);
		graph.addEdge("D", "E", 4);
		graph.addEdge("E", "F", 2);
		graph.addEdge("E", "G", 8);
		graph.addEdge("F", "G", 9);
		
		KruskalMST1<String> kruskalMST1 = new KruskalMST1<>(graph);
		UndirectedGraph<String> mst = kruskalMST1.getMST();
		System.out.println(mst);
	}
	
	@Test
	public void testKruskalMST2() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G"};
		UndirectedGraph3<String> graph = new UndirectedGraph3<>(data);
		graph.addEdge("A", "B", 12);
		graph.addEdge("A", "F", 16);
		graph.addEdge("A", "G", 14);
		graph.addEdge("B", "C", 10);
		graph.addEdge("B", "F", 7);
		graph.addEdge("C", "D", 3);
		graph.addEdge("C", "E", 5);
		graph.addEdge("C", "F", 6);
		graph.addEdge("D", "E", 4);
		graph.addEdge("E", "F", 2);
		graph.addEdge("E", "G", 8);
		graph.addEdge("F", "G", 9);
		
		KruskalMST2<String> kruskalMST2 = new KruskalMST2(graph);
		UndirectedGraph3<String> mst = kruskalMST2.getMST();
		System.out.println(mst);
	}
}
