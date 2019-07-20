package _20.cn.itcast.mst.primMST;

import java.util.ArrayList;

import _16.cn.itcast.undirectedGraph.UndirectedGraph;

/**
 * 	使用 prim 算法，从 用 邻接矩阵 实现的无向图 最找最小生成树
 * @author Administrator
 *
 */
public class PrimMST1<V> {
	// 这个主要是在确认一个图是不是连通图，使用深度优先遍历需要维护的一维数组
	private boolean[] marked;
	private UndirectedGraph<V> graph;
	
	public PrimMST1(UndirectedGraph<V> graph) {
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
	 * 	【说明】 因为我们是使用邻接矩阵实现的图，所以我们是直接通过索引访问二维数组，得到边的权重的
	 * 			这里的索引 和 边的权重是两个相对独立的数据。 
	 * 			这里我们根据比较权重，得到权值最小的横切边时，必须额外保存这条边对应的两个顶点的索引。
	 * 			最终再根据 索引 + 最小权值 给最小生成树添加一条边。
	 * 		 	
	 * 			===> 这里只能如此了，用不上优先级队列来比较 横切边的权值。（很多本次循环比较过的权值，下次循环还得重新比较）
	 */
	public UndirectedGraph<V> getMST() {
		ArrayList<V> vertexs = graph.getVertexs();
		
		// 根据原图，初始化一个没有边的子图，我们下面会不断地寻找最小生成树的边，并添加到这个子图中
		V[] array = (V[])new Object[graph.getVertexCount()];
		vertexs.toArray(array);
		UndirectedGraph<V> graph2 = new UndirectedGraph<>(array);
		
		// 用来保存切分的顶点集合（切分后顶点会被分成两部分，我们这部分都是已经确认过的顶点）
		ArrayList<Integer> selected = new ArrayList<>();
		// 首先，我们把索引为0的顶点添加到 selected 集合中
		selected.add(0);
		int[][] adjMatrix = graph.getAdjMatrix();
		int minWeight = Integer.MAX_VALUE;
		int tempRowIndex = -1;
		int tempColIndex = -1;
		// 如果 selected 集合里面的顶点数量小于全部顶点，那么我们就一直循环
		while(selected.size() < graph.getVertexCount()) {
			// 我们遍历 selected 集合，找到全部的横切边，然后通过优先队列来
			for (Integer vIndex : selected) {
				for (int i = 0; i < adjMatrix[vIndex].length; i++) {
					// 如果这条边指向的顶点已经保存到 selected 了，那么我们就直接跳过
					// 如果这条边的权值小于当前最小权值，我们也跳过
					if(!selected.contains(i) && adjMatrix[vIndex][i] > 0 && adjMatrix[vIndex][i] < minWeight) {
						minWeight = adjMatrix[vIndex][i];
						tempRowIndex = vIndex;
						tempColIndex = i;
					}
				}
			}
			// 一个循环过后，我们肯定能找到一个 minWeight ，tempRowIndex, tempColIndex
			// 我们就根据这个，添加边到 最小生成树中
			graph2.addEdge(vertexs.get(tempRowIndex), vertexs.get(tempColIndex), minWeight);
			// 再然后，我们也要把这条边的另一个顶点，也就是   tempColIndex 添加到 selected 中
			selected.add(tempColIndex);
			// 为了不影响下次循环，我们让 minWeight 再次等于最大整数
			minWeight = Integer.MAX_VALUE;
		}
		
		// 退出while 循环以后， graph2 就是我们想要的最小生成树了，我们可以看一下
		return graph2;
	}
}
