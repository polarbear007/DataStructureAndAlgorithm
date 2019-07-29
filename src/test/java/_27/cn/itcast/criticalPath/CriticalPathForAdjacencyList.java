package _27.cn.itcast.criticalPath;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph2;
import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;
import _24.cn.itcast.spt.topoLogicalSort.TopoLogicalSortForAdjacencyListWithQueue;

/**
 * 	我们规定如果想要求最长的路径，那么原图必须有起点和终点
 * 		1、 终点对应原图的最后一个顶点，索引值为 vertexCount - 1。
 * 		2、 我们必须保证一个拓朴排序以后, 终点必须是拓朴序列的最后一个元素。
 * 		3、 一个顶点指向其他顶点的的权值必须是固定的，比如说A顶点指向B顶点的权值为 2.0， 那么A顶点指向C顶点的权值应该也是2.0
 * @author Administrator
 *
 * @param <V>
 */
public class CriticalPathForAdjacencyList<V> {
	DirectedGraph2<V> graph;
	Double[] dist;
	int[] preVertex;
	List<Integer> topologicalList;
	
	public CriticalPathForAdjacencyList(DirectedGraph2<V> graph) {
		if (graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;

		// 初始化dist 和 lastVertex 数组
		dist = new Double[graph.getVertexCount()];
		// 首先，放全部的 dist 值都为0.0
		Arrays.fill(dist, 0.0);

		// 初始化 preVertex 数组，包括起点，全部初始化为 -1
		preVertex = new int[graph.getVertexCount()];
		Arrays.fill(preVertex, -1);
		
		TopoLogicalSortForAdjacencyListWithQueue<V> topo = new TopoLogicalSortForAdjacencyListWithQueue<>(graph);
		topologicalList = topo.topoLogicalSortForIndexList();
		
		// 最终，我们调用  criticalPathMethod 维护 dist 和 preVertex 数组
		criticalPathMethod();
	}
	
	/**
	 * 	核心方法
	 */
	private void criticalPathMethod() {
		// 首先，获取邻接表对象
		Vertex<V>[] adjTable = graph.getAdjTable();
		
		LinkedList<Edge> adjList =  null;
		int fromIndex = -1;
		int toIndex = -1;
		Double weight = null;
		
		// 然后，我们遍历拓朴序列，依次放松即可
		for (Integer vIndex : topologicalList) {
			adjList = adjTable[vIndex].getAdjList();
			for (Edge edge : adjList) {
				fromIndex = edge.getFromIndex();
				toIndex = edge.getToIndex();
				weight = edge.getWeight();
				if(dist[toIndex] < dist[fromIndex] + weight) {
					dist[toIndex] = dist[fromIndex] + weight;
					preVertex[toIndex] = fromIndex;
				}
			}
		}
	}
	
	/**
	 * 	获取最长的路径权值
	 * @return
	 */
	public Double getLongestPathWeight() {
		return dist[graph.getVertexCount() - 1];
	}
	
	/**
	 * 	获取最长的路径
	 * @return
	 */
	public Stack<V> getLongestPath(){
		int currIndex = graph.getVertexCount() - 1;
		Vertex<V>[] adjTable = graph.getAdjTable();
		Stack<V> stack = new Stack<V>();
		
		while(currIndex != -1) {
			stack.add(adjTable[currIndex].getValue());
			currIndex = preVertex[currIndex];
		}
		return stack;
	}
}
