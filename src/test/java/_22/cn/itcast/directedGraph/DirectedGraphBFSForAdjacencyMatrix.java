package _22.cn.itcast.directedGraph;

import java.util.ArrayList;

import _03.cn.itcast.queue.LinkedListQueue;

public class DirectedGraphBFSForAdjacencyMatrix<V> {
	private boolean[] marked;
	private DirectedGraph<V> graph;

	// 构造方法
	public DirectedGraphBFSForAdjacencyMatrix(DirectedGraph<V> graph) {
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

	/**
	 * 广度优先遍历不需要递归，我们只需要维护一个队列就可以了 广度优先遍历相当于以前我们学的 层序遍历
	 */
	public void bfsFromVertexIndex(int sourceIndex) {
		// 为了方便，我们先根据 图对象获取全部的顶点集合
		ArrayList<V> vertexs = graph.getVertexs();
		// 根据图对象，获取邻接第矩阵
		Double[][] adjMatrix = graph.getAdjMatrix();
		// 广度优先遍历需要维护一个队列
		LinkedListQueue<Integer> queue = new LinkedListQueue<>();
		// 如果起始索引对应的顶点还没有被标记过，那么我们就 把这个索引值放进队列
		if (!marked[sourceIndex]) {
			queue.addItem(sourceIndex);
		} 

		Integer currIndex = -1;
		// 接下来就是不断地从队列里面弹出顶点，并添加全部的关联顶点
		while (true) {
			// 如果队列为空，我们就不再循环了，直接退出循环，结束遍历
			if (queue.isEmpty()) {
				break;
			}
			// 从队列里面弹出一个顶点
			currIndex = queue.removeItem();
			// 如果弹出的这个顶点还没有被标记，那么我们就读取 邻接矩阵， 把与之相关的全部非标记顶点加入队列
			if (!marked[currIndex]) {
				// 如果这个顶点还 没有标记过，我们首先输出这个顶点的内容
				System.out.println(vertexs.get(currIndex));
				// 再把这个顶点标记成已经访问过
				marked[currIndex] = true;
				// 再去读取邻接矩阵，把所有非标记的顶点索引都加入队列
				for (int i = 0; i < adjMatrix[currIndex].length; i++) {
					if (adjMatrix[currIndex][i] != null) {
						queue.addItem(i);
					}
				}
			}
		}
	}
}
