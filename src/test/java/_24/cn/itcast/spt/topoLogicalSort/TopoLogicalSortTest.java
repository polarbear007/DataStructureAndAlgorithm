package _24.cn.itcast.spt.topoLogicalSort;

import java.util.List;

import org.junit.Test;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _22.cn.itcast.directedGraph.DirectedGraph2;

public class TopoLogicalSortTest {
	@Test
	public void testSort1() {
		String[] data = {"A", "B", "C"};
		DirectedGraph<String> graph = new DirectedGraph<String>(data);
		graph.addEdge("A", "B");
		graph.addEdge("B", "C");
		
		graph.addEdge("C", "A");  // 会形成环
		//graph.addEdge("A", "C");
		
		TopoLogicalSortForAdjacencyMatrix<String> topoLogic = new TopoLogicalSortForAdjacencyMatrix<>(graph);
		List<String> list = topoLogic.topoLogicalSort();
		System.out.println(list);
	}
	
	@Test
	public void testSort2() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph2<String> graph = new DirectedGraph2<String>(data);
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("C", "D");
		graph.addEdge("D", "E");
		graph.addEdge("E", "C");
		
		TopoLogicalSortForAdjacencyList<String> topoLogic = new TopoLogicalSortForAdjacencyList<>(graph);
		List<String> list = topoLogic.topoLogicalSort();
		System.out.println(list);
	}
	
	
	@Test
	public void testSortWithQueue() {
		String[] data = {"A", "B", "C"};
		DirectedGraph<String> graph = new DirectedGraph<String>(data);
		graph.addEdge("A", "B");
		graph.addEdge("B", "C");
		
		graph.addEdge("C", "A");  // 会形成环
		graph.addEdge("A", "C");
		
		TopoLogicalSortForAdjacencyMatrix<String> topoLogic = new TopoLogicalSortForAdjacencyMatrixWithQueue<>(graph);
		List<String> list = topoLogic.topoLogicalSort();
		System.out.println(list);
	}
	
	
	@Test
	public void testSortWithQueue2() {
		String[] data = {"A", "B", "C", "D", "E"};
		DirectedGraph2<String> graph = new DirectedGraph2<String>(data);
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("C", "D");
		graph.addEdge("D", "E");
		graph.addEdge("E", "C");
		
		TopoLogicalSortForAdjacencyList<String> topoLogic = new TopoLogicalSortForAdjacencyListWithQueue<>(graph);
		List<String> list = topoLogic.topoLogicalSort();
		System.out.println(list);
	}
}
