package _24.cn.itcast.spt.topoLogicalSort2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph2;
import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;
import _24.cn.itcast.spt.topoLogicalSort.TopoLogicalSortForAdjacencyListWithQueue;

public class TopoLogicalSortForSPTByAdjacencyList<V> {
	DirectedGraph2<V> graph;
	Double[] dist;
	int[] preVertex;
	int sourceIndex;
	List<Integer> topologicalList;
	
	
	public TopoLogicalSortForSPTByAdjacencyList(DirectedGraph2<V> graph, V source) {
		if (graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;

		// 根据传入的起始点值查找对应的索引
		int sourceIndex = graph.getIndexByValue(source);

		// 检查一下起始点索引是否有效
		if (sourceIndex == -1) {
			throw new RuntimeException("指定的起点不存在！");
		}
		// 初始化起始点索引
		this.sourceIndex = sourceIndex;

		// 初始化dist 和 lastVertex 数组
		dist = new Double[graph.getVertexCount()];
		// 首先，放全部的 dist 值都为正无穷大
		Arrays.fill(dist, Double.POSITIVE_INFINITY);
		// 然后起点值为 0.0
		dist[sourceIndex] = 0.0;

		// 初始化 preVertex 数组，包括起点，全部初始化为 -1
		preVertex = new int[graph.getVertexCount()];
		Arrays.fill(preVertex, -1);
		
		TopoLogicalSortForAdjacencyListWithQueue<V> topo = new TopoLogicalSortForAdjacencyListWithQueue<>(graph);
		topologicalList = topo.topoLogicalSortForIndexList();
		
		// 最终，我们需要调用 buildSPT 方法，生成最短路径树（其实并不是树，而是维护两个数组）
		bulidSPT();
	}

	/**
	 * 	构建最短路径树的核心方法
	 */
	private void bulidSPT() {
		// 首先，获取邻接表
		Vertex<V>[] adjTable = graph.getAdjTable();
		// 然后，获取起点在 拓朴序列中的索引位置
		int start = topologicalList.indexOf(sourceIndex);
		
		LinkedList<Edge> adjList = null;
		int toIndex = -1;
		
		for (int i = start; i < adjTable.length; i++) {
			adjList = adjTable[i].getAdjList();
			for (Edge edge : adjList) {
				toIndex = edge.getToIndex();
				if(dist[toIndex] > dist[i] + edge.getWeight()) {
					dist[toIndex] = dist[i] + edge.getWeight();
					preVertex[toIndex] = i;
				}
			}
		}
	}
	
	/**
	 * 确认起点是否可以到达指定的顶点
	 * @param destVertex
	 * @return
	 */
	public boolean hasPathTo(V destVertex) {
		// 首先，我们根据 destVertex 确定其对应的 索引
		int destIndex = graph.getIndexByValue(destVertex);
		// 如果输入的顶点根本就找不到对应的索引，那么说明这个顶点不存在
		if (destIndex == -1) {
			throw new RuntimeException("输入的顶点不存在！");
		}
		// 如果 dist数组对应的值不等于 最大值的话，那么我们就认为起点是可以连通 指定顶点的
		return dist[destIndex] != Double.POSITIVE_INFINITY;
	}

	/**
	 * 返回一个栈集合，里面保存着起点到指定顶点的路径
	 * 
	 * @param destVertex
	 * @return
	 */
	public Stack<V> pathTo(V destVertex) {
		if (hasPathTo(destVertex)) {
			// 首先，我们根据 destVertex 确定其对应的 索引
			int destIndex = graph.getIndexByValue(destVertex);
			// 然后，创建一个 Stack 对象
			Stack<V> stack = new Stack<V>();
			Vertex<V>[] adjTable = graph.getAdjTable();
			// 添加终点
			stack.add(adjTable[destIndex].getValue());
			int tempIndex = preVertex[destIndex];
			// 【注意】 起始顶点的前一个顶点的索引我们设置成 -1 ，这里作为退出循环的条件
			while (tempIndex != -1) {
				stack.add(adjTable[tempIndex].getValue());
				tempIndex = preVertex[tempIndex];
			}
			return stack;
		} else {
			throw new RuntimeException("起点无法连通到指定的顶点");
		}
	}

	/**
	 * 返回起点到指定顶点的最短路路径长度
	 * @param destVertex
	 * @return
	 */
	public Double minDistanceTo(V destVertex) {
		int destIndex = graph.getIndexByValue(destVertex);
		if (destIndex == -1) {
			throw new RuntimeException("指定的顶点不存在");
		} else {
			return dist[destIndex];
		}
	}
}
