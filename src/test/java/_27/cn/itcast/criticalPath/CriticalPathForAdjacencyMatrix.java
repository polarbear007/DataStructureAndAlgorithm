package _27.cn.itcast.criticalPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _24.cn.itcast.spt.topoLogicalSort.TopoLogicalSortForAdjacencyMatrixWithQueue;

/**
 * 	我们规定如果想要求最长的路径，那么原图必须有起点和终点
 * 		1、 终点对应原图的最后一个顶点，索引值为 vertexCount - 1。
 * 		2、 我们必须保证一个拓朴排序以后, 终点必须是拓朴序列的最后一个元素。
 * 		3、 一个顶点指向其他顶点的的权值必须是固定的，比如说A顶点指向B顶点的权值为 2.0， 那么A顶点指向C顶点的权值应该也是2.0
 * @author Administrator
 *
 * @param <V>
 */
public class CriticalPathForAdjacencyMatrix<V> {
	DirectedGraph<V> graph;
	Double[] dist;
	int[] preVertex;
	List<Integer> topologicalList;
	
	/**
	 * 	构造函数
	 * @param graph
	 * @param source
	 */
	public CriticalPathForAdjacencyMatrix(DirectedGraph<V> graph) {
		if(graph == null) {
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
		
		// 调用拓朴排序方法，获取拓朴序列集合（索引）
		TopoLogicalSortForAdjacencyMatrixWithQueue<V> topo = new TopoLogicalSortForAdjacencyMatrixWithQueue<>(graph);
		// 拓扑排序以后，如果我们有设置起点的话，那么这个起点肯定是第一个元素
		//  如果我们有设置终点的话，那么这个终点肯定是最后一个元素
		topologicalList = topo.topoLogicalSortForIndexList();
		
		criticalPathMethod();
	}
	
	/**
	 * 	寻找最长路径
	 * 	其实这个方法跟求最短路径树一样，是在维护  dist 和 preVertex 数组
	 */
	private void criticalPathMethod() {
		// 先获取邻接矩阵
		Double[][] adjMatrix = graph.getAdjMatrix();
		
		// 按照拓朴顺序，我们拿到每一个顶点，并对这些顶点的全部边进行 “松驰” 操作
		for (Integer vIndex : topologicalList) {
			for (int i = 0; i < adjMatrix[vIndex].length; i++) {
				if(adjMatrix[vIndex][i] != null && dist[i] < dist[vIndex] + adjMatrix[vIndex][i]) {
					dist[i] = dist[vIndex] + adjMatrix[vIndex][i];
					preVertex[i] = vIndex;
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
		ArrayList<V> vertexs = graph.getVertexs();
		Stack<V> stack = new Stack<V>();
		
		while(currIndex != -1) {
			stack.add(vertexs.get(currIndex));
			currIndex = preVertex[currIndex];
		}
		return stack;
	}
}
