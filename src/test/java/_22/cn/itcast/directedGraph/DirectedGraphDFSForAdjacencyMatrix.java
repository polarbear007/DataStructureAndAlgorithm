package _22.cn.itcast.directedGraph;

import java.util.ArrayList;

public class DirectedGraphDFSForAdjacencyMatrix<V> {
	private boolean[] marked;
	private DirectedGraph<V> graph;
	
	/**
	 * 	构建方法，传入 graph 对象
	 * @param graph
	 */
	public DirectedGraphDFSForAdjacencyMatrix(DirectedGraph<V> graph) {
		if(graph != null) {
			this.graph = graph;
		}else {
			throw new RuntimeException("图对象不能为空");
		}
	}
	
	/**
	 * 	在无向图中，从某个顶点出发，可能并不能访问到全部的其他顶点。
	 * 	在有向图中也是一样的，所以我们需要遍历全部的顶点的话，需要多次调用 dfsFromVertexIndex 方法
	 */
	public void dfs() {
		// 初始化一下 marked 数组
		marked = new boolean[graph.getVertexCount()];
		for (int i = 0; i < graph.getVertexCount(); i++) {
			dfsFromVertexIndex(i);
		}
	}
	
	/**
	 * 	指定从哪个顶点开始进行深度优先遍历
	 * 	深度优先遍历相当于以前我们讲过的先序遍历，先遍历当前顶点，再遍历左顶点，最后再遍历右顶点。
	 * @param sourceIndex
	 */
	private void dfsFromVertexIndex(int sourceIndex) {
		if(!marked[sourceIndex]) {
			// 先获取邻接矩阵
			Double[][] adjMatrix = graph.getAdjMatrix();
			// 再获取顶点集合
			ArrayList<V> vertexs = graph.getVertexs();
			// 先遍历本顶点的值
			System.out.println(vertexs.get(sourceIndex));
			// 然后把本顶点标记成已经遍历
			marked[sourceIndex] = true;
			
			// 然后遍历邻接矩阵，找到全部的边
			for (int i = 0; i < adjMatrix[sourceIndex].length; i++) {
				if(!marked[i] && adjMatrix[sourceIndex][i] != null) {
					dfsFromVertexIndex(i);
				}
			}
		}
	}
}
