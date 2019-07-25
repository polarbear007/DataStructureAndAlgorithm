package _22.cn.itcast.directedGraph;

import java.util.LinkedList;

import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;

public class DirectedGraphDFSForAdjacencyList<V> {
	private boolean[] marked;
	private DirectedGraph2<V> graph;
	
	/**
	 * 	通过构建方法传入图对象
	 * @param graph
	 */
	public DirectedGraphDFSForAdjacencyList(DirectedGraph2<V> graph) {
		if(graph != null) {
			this.graph = graph;
		}else {
			throw new RuntimeException("图对象不能为空");
		}
	}
	
	
	public void dfs() {
		marked = new boolean[graph.getVertexCount()];
		for (int i = 0; i < graph.getVertexCount(); i++) {
			dfsFromVertexIndex(i);
		}
	}
	
	
	private void dfsFromVertexIndex(int sourceIndex) {
		// 如果这个顶点还没有遍历过，我们才进入遍历流程
		if(!marked[sourceIndex]) {
			// 首先，获取邻接表
			Vertex<V>[] adjTable = graph.getAdjTable();
			// 再然后，标记已经 遍历
			marked[sourceIndex] = true;
			// 然后直接遍历顶点值
			System.out.println(adjTable[sourceIndex].getValue());
			
			// 再然后，递归遍历其他的相关顶点
			LinkedList<Edge> adjList = adjTable[sourceIndex].getAdjList();
			for (Edge edge : adjList) {
				if(!marked[edge.getToIndex()]) {
					dfsFromVertexIndex(edge.getToIndex());
				}
			}
		}
	}
}
