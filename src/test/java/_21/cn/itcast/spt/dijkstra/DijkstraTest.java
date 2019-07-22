package _21.cn.itcast.spt.dijkstra;

import java.util.Stack;

import org.junit.Test;

import _16.cn.itcast.undirectedGraph.UndirectedGraph;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3;

public class DijkstraTest {
	@Test
	public void testDijkstraSPT1() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G"};
		UndirectedGraph<String> graph = new UndirectedGraph<>(data);
		graph.addEdge("A", "F", 8);
		graph.addEdge("A", "B", 7);
		graph.addEdge("B", "C", 5);
		graph.addEdge("C", "D", 9);
		graph.addEdge("D", "E", 4);
		graph.addEdge("E", "F", 5);
		graph.addEdge("F", "G", 4);
		graph.addEdge("B", "G", 2);
		graph.addEdge("C", "G", 3);
		graph.addEdge("E", "G", 6);
		
		DijkstraSPT1<String> dijkstraSPT1 = new DijkstraSPT1<>(graph, "G");
		Stack<String> pathTo = dijkstraSPT1.pathTo("A");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(dijkstraSPT1.minDistanceTo("A"));
		
		System.out.println("**********");
		pathTo = dijkstraSPT1.pathTo("C");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(dijkstraSPT1.minDistanceTo("C"));
	}
	
	@Test
	public void testDijkstraSPT2() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G"};
		UndirectedGraph3<String> graph = new UndirectedGraph3<>(data);
		graph.addEdge("A", "F", 8);
		graph.addEdge("A", "B", 7);
		graph.addEdge("B", "C", 5);
		graph.addEdge("C", "D", 9);
		graph.addEdge("D", "E", 4);
		graph.addEdge("E", "F", 5);
		graph.addEdge("F", "G", 4);
		graph.addEdge("B", "G", 2);
		graph.addEdge("C", "G", 3);
		graph.addEdge("E", "G", 6);
		
		DijkstraSPT2<String> dijkstraSPT2 = new DijkstraSPT2<>(graph, "G");
		Stack<String> pathTo = dijkstraSPT2.pathTo("A");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(dijkstraSPT2.minDistanceTo("A"));
		
		System.out.println("**********");
		pathTo = dijkstraSPT2.pathTo("C");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
		System.out.println(dijkstraSPT2.minDistanceTo("C"));
	}
}
