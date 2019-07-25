package _24.cn.itcast.spt.topoLogicalSort2;

import java.util.Stack;

import org.junit.Test;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _22.cn.itcast.directedGraph.DirectedGraph2;

public class TopologicalSortForSPTTest {
	@Test
	public void testBuildSPT() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G", "H"};
		DirectedGraph<String> graph = new DirectedGraph<>(data);
		graph.addEdge("A", "B", 2.0);
		graph.addEdge("A", "C", 3.0);
		graph.addEdge("A", "D", 5.0);
		graph.addEdge("B", "C", 1.0);
		graph.addEdge("B", "E", 6.0);
		graph.addEdge("C", "F", 2.0);
		graph.addEdge("D", "G", 7.0);
		graph.addEdge("E", "F", 3.0);
		graph.addEdge("G", "C", 5.0);
		graph.addEdge("G", "H", 2.0);
		graph.addEdge("H", "B", 10.0);
		graph.addEdge("H", "E", 7.0);
		graph.addEdge("H", "F", 1.0);
		TopoLogicalSortForSPTByAdjacencyMatrix<String> spt = new TopoLogicalSortForSPTByAdjacencyMatrix<>(graph, "D");
		Stack<String> pathTo = spt.pathTo("F");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
	}
	
	
	@Test
	public void testBuildSPT2() {
		String[] data = {"A", "B", "C", "D", "E", "F", "G", "H"};
		DirectedGraph2<String> graph = new DirectedGraph2<>(data);
		graph.addEdge("A", "B", 2.0);
		graph.addEdge("A", "C", 3.0);
		graph.addEdge("A", "D", 5.0);
		graph.addEdge("B", "C", 1.0);
		graph.addEdge("B", "E", 6.0);
		graph.addEdge("C", "F", 2.0);
		graph.addEdge("D", "G", 7.0);
		graph.addEdge("E", "F", 3.0);
		graph.addEdge("G", "C", 5.0);
		graph.addEdge("G", "H", 2.0);
		graph.addEdge("H", "B", 10.0);
		graph.addEdge("H", "E", 7.0);
		graph.addEdge("H", "F", 1.0);
		TopoLogicalSortForSPTByAdjacencyList<String> spt = new TopoLogicalSortForSPTByAdjacencyList<>(graph, "D");
		Stack<String> pathTo = spt.pathTo("F");
		while(!pathTo.isEmpty()) {
			System.out.println(pathTo.pop());
		}
	}
}
