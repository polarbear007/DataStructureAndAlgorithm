package _20.cn.itcast.mst.kruskalMST;

import java.util.ArrayList;

import _16.cn.itcast.undirectedGraph.UndirectedGraph;

/**
 * 	使用 kruskal 算法，从 使用邻接矩阵 的图中寻找最小生成树
 * @author Administrator
 *
 */
public class KruskalMST1<V> {
	// 这个主要是在确认一个图是不是连通图，使用深度优先遍历需要维护的一维数组
	private boolean[] marked;
	private UndirectedGraph<V> graph;
	
	public KruskalMST1(UndirectedGraph<V> graph) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;
		// 判断是否是连通图，如果不是，我们也不去找什么最小生成树了
		if(!isConnectedGraph()) {
			throw new RuntimeException("此图不是连通图，无法找到最小生成树！");
		}
	}
	
	/**
	 * 	判断一个图是否是连通图
	 * @return
	 */
	private boolean isConnectedGraph() {
		// 根据图对象，初始化记录顶点状态的marked 数组
		marked = new boolean[graph.getVertexCount()];
		// 然后从 0 索引对应的顶点开始深度优先遍历
		dfsFromIndex(0);
		// 确认 marked 数组里面是否全部顶点都被标记
		//		如果全部标记成 true ，则认为是连通图
		//      如果没有全部标记true，则认为是非连通图
		for (int i = 0; i < marked.length; i++) {
			// 只要找到一个 false ，我们直接返回 false
			if(!marked[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 	从某个顶点出发，深度优先遍历图。不打印顶点数据，只维护 marked 数组，记录与目标顶点连通的顶点
	 * @param vertexIndex
	 */
	private void dfsFromIndex(int vertexIndex) {
		// 如果这个顶点还没有标记过，我们才再进行遍历
		if(!marked[vertexIndex]) {
			// 遍历一个顶点其实非常简单，我们一来先把这个索引对应的顶点先标记成 true
			marked[vertexIndex] = true;
			int[][] adjMatrix = graph.getAdjMatrix();
			// 再然后，去访问邻接矩阵，查看与之直接连通的顶点
			for (int i = 0; i < adjMatrix[vertexIndex].length; i++) {
				if(adjMatrix[vertexIndex][i] > 0) {
					dfsFromIndex(i);
				}
			}
		}
	}
	
	/**
	 * 	寻找并返回最小生成树
	 * 	【说明】 因为我们使用的是邻接矩阵实现的图，所以这里用不上优先级队列，或者对边的权值进行排序
	 * 			每一次寻找权值最小的边，都需要遍历一次二维数组
	 * @return
	 */
	public UndirectedGraph<V> getMST() {
		ArrayList<V> vertexs = graph.getVertexs();
		
		// 根据原图，初始化一个没有边的子图，我们下面会不断地寻找最小生成树的边，并添加到这个子图中
		V[] array = (V[])new Object[graph.getVertexCount()];
		vertexs.toArray(array);
		UndirectedGraph<V> graph2 = new UndirectedGraph<>(array);
		
		// 我们根据原图顶点的数量初始化一个并查集
		UnionFind uf = new UnionFind(graph.getVertexCount());
		
		// 再然后，我们就可以开始遍历 邻接矩阵， 找出权值最小 的边
		int minWeight = Integer.MAX_VALUE;
		int tempRowIndex = -1;
		int tempColIndex = -1;
		
		int[][] adjMatrix = graph.getAdjMatrix();
		// 如果原图有 n 个顶点，那么我们只需要找到 n - 1 条边就 可以了
		for (int i = 0; i < graph.getVertexCount() - 1; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {
				for (int k = 0; k < adjMatrix[j].length; k++) {
					// 如果 j 和 k 索引对应的顶点不在同一个集合，并且 权值大于 0（权值等于0 的情况是边不存在）
					if(!uf.isConnected(j, k) && adjMatrix[j][k] > 0 && adjMatrix[j][k] < minWeight) {
						minWeight = adjMatrix[j][k];
						tempRowIndex = j;
						tempColIndex = k;
					}
				}
			}
			
			// 每循环一次，我们就从邻接矩阵中找到一条权值最小的边，并且这条边并不会形成环, 我们添加这条边到最小生成树
			graph2.addEdge(vertexs.get(tempRowIndex), vertexs.get(tempColIndex), minWeight);
			// 重置一下 minWeight
			minWeight = Integer.MAX_VALUE;
			// 我们需要把刚才那条边的两个顶点所在的集合进行合并
			uf.union(tempRowIndex, tempColIndex);
		}
		
		return graph2;
	}
}
