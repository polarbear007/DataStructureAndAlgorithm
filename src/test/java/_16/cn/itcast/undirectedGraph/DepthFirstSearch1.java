package _16.cn.itcast.undirectedGraph;

/**
 * 	只适用于 用 邻接矩阵实现的 图
 * @author Administrator
 *
 * @param <V>
 */
public class DepthFirstSearch1<V> {
	// marked 数组长度跟图的顶点数组长度一致，索引也是一一对应的。
	// 这个数组元素的值为 true ，表示对应的顶点已经访问过了，不需要再次访问。
	private boolean[] marked;
	// 统计已经标记的顶点数量（维护一下这个变量，省得要用时，得去遍历 marked 数组）
	private int count;
	// 保存外部传进来的图对象
	private UndirectedGraph<V> graph;
	
	// 构造函数，传入一个图对象 和 起点顶点的索引
	public DepthFirstSearch1(UndirectedGraph<V> graph) {
		if(graph != null) {
			marked = new boolean[graph.getVertexCount()];
			this.graph = graph;
		}else {
			throw new RuntimeException("图对象不能为空");
		}
	}
	
	// 从指定顶点开始深度优先遍历，可能并不能遍历全部的顶点（如果这个图不是连通图的话，就会出现这种情况）
	// 所以如果我们想要遍历全部的顶点的话，那么就得遍历 图里面的 顶点集合，从所有的顶点出发去遍历
	public void dfs() {
		// 开始遍历全部顶点之前，我们重置一下 marked 数组
		marked = new boolean[graph.getVertexCount()];
		for (int i = 0; i < graph.getVertexs().size(); i++) {
			dfsFromVertexIndex(i);
		}
	}
	
	// 以指定的顶点开始，使用深度优先算法遍历全部的顶点
	public void dfsFromVertex(V sourceVertex) {
		int sourceIndex = graph.getVertexs().indexOf(sourceVertex);
		if(sourceIndex != -1) {
			dfsFromVertexIndex(sourceIndex);
		}else {
			throw new RuntimeException("起始顶点不存在！");
		}
	}
	
	// 深度优先遍历(需要传入起点索引)
	private void dfsFromVertexIndex(int sourceIndex) {
		if(!marked[sourceIndex]) {
			// 首先，把起点顶点标记为 true
			marked[sourceIndex] = true;
			// 维护一下当前已经遍历的结点数量
			count++;
			// 再打印一下当前遍历的顶点
			System.out.println(graph.getVertexs().get(sourceIndex));
			
			int[][] adjMatrix = graph.getAdjMatrix();
			for (int i = 0; i < adjMatrix[sourceIndex].length; i++) {
				if(adjMatrix[sourceIndex][i] > 0) {
					dfsFromVertexIndex(i);
				}
			}
		}
	}
	
	// 确认一个顶点的索引是否已经被访问过
	public boolean isMarked(int index) {
		return marked[index];
	}
}
