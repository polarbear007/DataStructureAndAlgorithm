package _25.cn.itcast.spt.FloydWarshall;

import java.util.ArrayList;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph;

public class FloydWarshallForSPTByAdjacencyMatrix<V> {
	private Double[][] dist;
	private int[][] preVertex;
	private DirectedGraph<V> graph;
	
	public FloydWarshallForSPTByAdjacencyMatrix(DirectedGraph<V> graph) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;
		
		initDist();
		initPreVertex();
		
		// 最终，我们需要调用 buildSPT 方法，生成最短路径树（其实并不是树，而是维护两个数组）
		bulidSPT();
	}
	
	/**
	 * 	Floyd 算法的核心方法
	 * 	【说明】 理解比较困难，实际上代码没几行。
	 */
	private void bulidSPT() {
		int vertexCount = graph.getVertexCount();
		
		// 先确定总的循环次数为 n
		for (int i = 0; i < vertexCount; i++) {
			// 每次循环，我们都设置 i 对应的顶点为 中间顶点
			// 然后遍历 dist 数组
			for (int j = 0; j < dist.length; j++) {
				for (int k = 0; k < dist[j].length; k++) {
					if(dist[j][k] > dist[j][i] + dist[i][k] ) {
						dist[j][k] = dist[j][i] + dist[i][k];
						preVertex[j][k] = i;
					}
				}
			}
		}
		
		// 判断是否存在负权环
		// 如果存在，我们也不维护了，直接抛异常就行了
		for (int i = 0; i < dist.length; i++) {
			// 如果存在负权环的话，那么肯定会有某个顶点到自己的距离小于 0
			if(dist[i][i] < 0) {
				throw new RuntimeException("此图存在负权环，无法求最短路径了！");
			}
		}
	}

	/**
	 * 	初始化 preVertex 数组
	 */
	private void initPreVertex() {
		// 先获取顶点的个数
		int vertexCount = graph.getVertexCount();
		// 创建 preVertex 数组对象
		preVertex = new int[vertexCount][vertexCount];
		// 获取原图的邻接矩阵
		Double[][] adjMatrix = graph.getAdjMatrix();
		// 遍历邻接矩阵，初始化 preVertex
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				if(adjMatrix[i][j] == null) {
					preVertex[i][j] = -1;
				}else {
					preVertex[i][j] = i;
				}
			}
		}
	}

	/**
	 * 	初始化dist 数组
	 */
	private void initDist() {
		// 先获取顶点的个数
		int vertexCount = graph.getVertexCount();
		// 创建 dist 数组
		dist = new Double[vertexCount][vertexCount];
		// 获取原图的邻接矩阵
		Double[][] adjMatrix = graph.getAdjMatrix();
		// 遍历原图的邻接矩阵，初始化 dist 数组
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				if(i == j) {
					dist[i][j] = 0.0;
				}else if(adjMatrix[i][j] != null) {
					dist[i][j] = adjMatrix[i][j];
				}else {
					// 所有不直接相邻的顶点之间的距离设置成正无穷大
					dist[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}
	}
	
	/**
	 * 	确认两个顶点是否连通
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public boolean isConnected(V fromVertex, V toVertex) {
		// 先获取原图对象的 vertexs 集合
		ArrayList<V> vertexs = graph.getVertexs();
		// 然后，根据输入的两个顶点确认顶点的索引
		int fromIndex = vertexs.indexOf(fromVertex);
		int toIndex = vertexs.indexOf(toVertex);
		if(fromIndex == -1) {
			throw new RuntimeException("起始顶点不存在");
		}
		if(toIndex == -1) {
			throw new RuntimeException("目的地顶点不存在");
		}
		
		// 如果两个顶点都没有什么问题，那么我们看一下 dist[fromIndex][toIndex] 的值是否是正无穷大
		//   等于正无穷大，说明两个顶点没有连通，返回false
		//   不等于正无穷大，说明两个顶点有连通，返回 true
		return  dist[fromIndex][toIndex] != Double.POSITIVE_INFINITY;
	}
	
	/**
	 * 	返回两个连通顶点的最短路径
	 * 	【注意】 如果这两个顶点不连通，那么我们也不需要获取什么最短路径了
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public Stack<V> getShortestPath(V fromVertex, V toVertex){
		// 如果有联通，我们再去求最短路径的路线，如果没有联通，那么我们也不考虑什么最短路径了
		if(isConnected(fromVertex, toVertex)) {
			// 先获取原图对象的 vertexs 集合
			ArrayList<V> vertexs = graph.getVertexs();
			// 然后，根据输入的两个顶点确认顶点的索引
			int fromIndex = vertexs.indexOf(fromVertex);
			int toIndex = vertexs.indexOf(toVertex);
			Stack<V> stack = new Stack<V>();
			// 首先，我们需要把终点的索引添加到栈中
			stack.add(vertexs.get(toIndex));
			
			while(true) {
				if(preVertex[fromIndex][toIndex] != -1) {
					stack.add(vertexs.get(preVertex[fromIndex][toIndex]));
					toIndex = preVertex[fromIndex][toIndex];
				}else {
					return stack;
				}
			}
		}else {
			throw new RuntimeException("这两个顶点并不连通，不存在最短路径");
		}
	}
	
	/**
	 * 	如果两个顶点不连通，那么我们也不求什么最短路径了
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public Double getMinDistance(V fromVertex, V toVertex) {
		// 如果有联通，我们再去求最短路径的路线，如果没有联通，那么我们也不考虑什么最短路径了
		if(isConnected(fromVertex, toVertex)) {
			// 先获取原图对象的 vertexs 集合
			ArrayList<V> vertexs = graph.getVertexs();
			// 然后，根据输入的两个顶点确认顶点的索引
			int fromIndex = vertexs.indexOf(fromVertex);
			int toIndex = vertexs.indexOf(toVertex);
			
			return dist[fromIndex][toIndex];
		}else {
			throw new RuntimeException("这两个顶点并不连通，不存在最短路径");
		}
	}
}
