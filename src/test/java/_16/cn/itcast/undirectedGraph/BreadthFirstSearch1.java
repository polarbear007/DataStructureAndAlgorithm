package _16.cn.itcast.undirectedGraph;

import java.util.ArrayList;

import _03.cn.itcast.queue.LinkedListQueue;

/**
 * 	广度优先遍历  使用 邻接矩阵 实现的图
 * @author Administrator
 *
 */
public class BreadthFirstSearch1<V> {
	// 我们需要维护一个一维数组 marked，记录每个顶点的状态
	private boolean[] marked;
	// 接收传入的图对象
	private UndirectedGraph<V> graph;
	
	// 构造方法，传入 图 对象，初始化 marked 数组
	public BreadthFirstSearch1(UndirectedGraph<V> graph) {
		if(graph != null) {
			marked = new boolean[graph.getVertexCount()];
			this.graph = graph;
		}else {
			throw new RuntimeException("图对象不能为空");
		}
	}
	
	// 确认一个顶点的索引是否已经被访问过
	public boolean isMarked(int index) {
		return marked[index];
	}
	
	/**
	 * 	跟深度优先遍历一样，为了防止遗漏，我们需要以所有顶点为起点，全部遍历一次
	 */
	public void bfs() {
		// 开始遍历全部顶点之前，我们重置一下 marked 数组
		marked = new boolean[graph.getVertexCount()];
		for (int i = 0; i < marked.length; i++) {
			if(!marked[i]) {
				bfsFromVertexIndex(i);
			}
		}
	}
	
	/**
	 * 	以某个顶点为起点，使用广度优先算法遍历与其相连通的全部顶点
	 * @param sourceVertex
	 */
	public void bfsFromVertex(V sourceVertex) {
		int sourceIndex = graph.getVertexs().indexOf(sourceVertex);
		if(sourceIndex != -1) {
			bfsFromVertexIndex(sourceIndex);
		}else {
			throw new RuntimeException("起始顶点不存在！");
		}
	}
	
	/**
	 * 	以某个顶点的索引为起始点，使用广度优先算法遍历与其相连通的全部顶点
	 * 	由于外部可能并不清楚索引值，所以我们这个方法私有
	 * 	【说明】 广度优先算法遍历相当于层序遍历，我们维护一个队列，就不递归了
	 * @param sourceIndex
	 */
	private void bfsFromVertexIndex(int sourceIndex) {
		// 为了方便，我们先根据 图对象获取全部的顶点集合
		ArrayList<V> vertexs = graph.getVertexs();
		// 根据图对象，获取邻接第矩阵
		int[][] adjMatrix = graph.getAdjMatrix();
		// 广度优先遍历需要维护一个队列
		LinkedListQueue<Integer> queue = new LinkedListQueue<>();
		// 如果起始索引对应的顶点还没有被标记过，那么我们就 把这个索引值放进队列
		if(!isMarked(sourceIndex)) {
			queue.addItem(sourceIndex);
		}else {
			throw new RuntimeException("起始点索引已经遍历过了");
		}
		
		Integer currIndex = -1;
		// 接下来就是不断地从队列里面弹出顶点，并添加全部的关联顶点
		while(true) {
			// 如果队列为空，我们就不再循环了，直接退出循环，结束遍历
			if(queue.isEmpty()) {
				break;
			}
			// 从队列里面弹出一个顶点
			currIndex = queue.removeItem();
			// 如果弹出的这个顶点还没有被标记，那么我们就读取 邻接矩阵， 把与之相关的全部非标记顶点加入队列
			if(!isMarked(currIndex)) {
				// 如果这个顶点还 没有标记过，我们首先输出这个顶点的内容
				System.out.println(vertexs.get(currIndex));
				// 再把这个顶点标记成已经访问过
				marked[currIndex] = true;
				// 再去读取邻接矩阵，把所有非标记的顶点索引都加入队列
				for (int i = 0; i < adjMatrix[currIndex].length; i++) {
					if(adjMatrix[currIndex][i] >= 1) {
						queue.addItem(i);
					}
				}
			}
		}
	}
}
