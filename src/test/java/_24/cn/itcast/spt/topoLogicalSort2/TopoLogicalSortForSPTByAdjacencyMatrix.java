package _24.cn.itcast.spt.topoLogicalSort2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph;
import _24.cn.itcast.spt.topoLogicalSort.TopoLogicalSortForAdjacencyMatrixWithQueue;

public class TopoLogicalSortForSPTByAdjacencyMatrix<V> {
	DirectedGraph<V> graph;
	Double[] dist;
	int[] preVertex;
	int sourceIndex;
	List<Integer> topologicalList;
	
	/**
	 * 	构造函数
	 * @param graph
	 * @param source
	 */
	public TopoLogicalSortForSPTByAdjacencyMatrix(DirectedGraph<V> graph, V source) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;
		
		// 根据传入的起始点值查找对应的索引
		int sourceIndex = graph.getVertexs().indexOf(source);
		
		// 检查一下起始点索引是否有效
		if(sourceIndex == -1) {
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
		
		// 调用拓朴排序方法，获取拓朴序列集合（索引）
		TopoLogicalSortForAdjacencyMatrixWithQueue<V> topo = new TopoLogicalSortForAdjacencyMatrixWithQueue<>(graph);
		topologicalList = topo.topoLogicalSortForIndexList();
		
		// 最终，我们需要调用 buildSPT 方法，生成最短路径树（其实并不是树，而是维护两个数组）
		bulidSPT();
	}
	
	/**
	 * 	从起点开始遍历 拓朴序列
	 */
	private void bulidSPT() {
		// 首先，获取邻接矩阵
		Double[][] adjMatrix = graph.getAdjMatrix();
		// 然后，我们确认一下起点在拓朴序列的索引位置
		int start = topologicalList.indexOf(sourceIndex);
		// 从这个索引位置开始对顶点进行松驰操作
		for (int i = start; i < topologicalList.size(); i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				if(adjMatrix[i][j] != null && dist[j] > dist[i] + adjMatrix[i][j]) {
					dist[j] = dist[i] + adjMatrix[i][j];
					preVertex[j] = i;
				}
			}
		}
	}
	
	/**
	 * 	确认起点是否可以到达指定的顶点
	 * @param destVertex
	 * @return
	 */
	public boolean hasPathTo(V destVertex) {
		// 首先，我们根据 destVertex 确定其对应的 索引
		int destIndex = graph.getVertexs().indexOf(destVertex);
		// 如果输入的顶点根本就找不到对应的索引，那么说明这个顶点不存在
		if(destIndex == -1) {
			throw new RuntimeException("输入的顶点不存在！");
		}
		// 如果 dist数组对应的值不等于 最大值的话，那么我们就认为起点是可以连通 指定顶点的
		return dist[destIndex] != Double.POSITIVE_INFINITY;
	}
	
	/**
	 * 	返回一个栈集合，里面保存着起点到指定顶点的路径
	 * @param destVertex
	 * @return
	 */
	public Stack<V> pathTo(V destVertex){
		if(hasPathTo(destVertex)) {
			// 首先，我们根据 destVertex 确定其对应的 索引
			int destIndex = graph.getVertexs().indexOf(destVertex);
			// 然后，创建一个 Stack 对象
			Stack<V> stack = new Stack<V>();
			ArrayList<V> vertexs = graph.getVertexs();
			// 添加终点
			stack.add(vertexs.get(destIndex));
			int tempIndex = preVertex[destIndex];
			// 【注意】 起始顶点的前一个顶点的索引我们设置成 -1 ，这里作为退出循环的条件
			while(tempIndex != -1) {
				stack.add(vertexs.get(tempIndex));
				tempIndex = preVertex[tempIndex];
			}
			return stack;
		}else{
			throw new RuntimeException("起点无法连通到指定的顶点");
		}
	}
	
	/**
	 * 	返回起点到指定顶点的最短路路径长度
	 * 	【注意】 如果返回一个正无穷大，说明这个顶点跟起点不连通
	 * @param destVertex
	 * @return
	 */
	public Double minDistanceTo(V destVertex) {
		int destIndex = graph.getVertexs().indexOf(destVertex);
		if(destIndex == -1) {
			throw new RuntimeException("指定的顶点不存在");
		}else {
			return dist[destIndex];
		}
	}
}
