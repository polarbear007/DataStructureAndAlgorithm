package _22.cn.itcast.directedGraph;

import java.util.LinkedList;

import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;

public class DirectedGraphBFSForAdjacencyList<V> {
	private boolean[] marked;
	private DirectedGraph2<V> graph;
	
	/**
	 * 	构造方法
	 * @param graph
	 */
	public DirectedGraphBFSForAdjacencyList(DirectedGraph2<V> graph) {
		if (graph != null) {
			this.graph = graph;
		} else {
			throw new RuntimeException("图对象不能为null");
		}
	}
	
	public void bfs() {
		// 首先，我们需要先初始化 marked 数组
		marked = new boolean[graph.getVertexCount()];
		for (int i = 0; i < marked.length; i++) {
			if(!marked[i]) {
				bfsFromVertexIndex(i);
			}
		}
	}
	
	private void bfsFromVertexIndex(int sourceIndex) {
		// 首先，获取邻接表对象
		Vertex<V>[] adjTable = graph.getAdjTable();
		// 然后，我们创建一个队列
		LinkedList<Integer> queue = new LinkedList<Integer>();
		// 其实我们最好是在进入方法前就检查的，但是以前这样写，懒得改了
		if(!marked[sourceIndex]) {
			queue.add(sourceIndex);
		}
		
		Integer currIndex = -1;
		
		while(!queue.isEmpty()) {
			// 从队列中弹出一个顶点的索引
			currIndex = queue.poll();
			// 如果这个顶点已经遍历过了，那么我们就再弹出一个
			if(marked[currIndex]) {
				continue;
			}else {
				// 如果这个顶点还没有遍历过，那么我们就打印一下这个顶点的值
				System.out.println(adjTable[currIndex].getValue());
				// 再标记成已经遍历
				marked[currIndex] = true;
				// 再把这个顶点指向的其他顶点都放入队列
				for (Edge edge : adjTable[currIndex].getAdjList()) {
					if(!marked[edge.getToIndex()]) {
						queue.add(edge.getToIndex());
					}
				}
			}
		}
	}
}
