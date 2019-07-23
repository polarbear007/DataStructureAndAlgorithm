package _23.cn.itcast.spt.bellmanFord;

import java.util.Arrays;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph2;
import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;

/**
 * 使用 Bellman-Ford 算法处理 邻接表 实现的有向图， 得到单源最短路径树
 * 
 * @author Administrator
 *
 */
public class BellmanFordForAdjacencyList<V> {
	private DirectedGraph2<V> graph;
	// 记录从起始点到本顶点最短的距离
	private Double[] dist;
	// 记录从起始点到本顶点最短路线的上一个顶点的索引
	private int[] preVertex;
	// 记录起始点的索引
	private int sourceIndex;
	// 记录这个图是否存在 负权环
	private boolean isContainingNegativeCircle;

	/**
	 * 检查是否存在负权环
	 * 
	 * @return
	 */
	public boolean isContainingNegativeCircle() {
		return isContainingNegativeCircle;
	}

	// 构造方法我们只需要传入原图对象就可以了，不需要像 求最小生成树那样检查是否是连通图
	// 因为求最短路径树，我们并不要求原图必须是一个连通图
	public BellmanFordForAdjacencyList(DirectedGraph2<V> graph, V source) {
		if (graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;

		// 根据传入的起始点值查找对应的索引
		int sourceIndex = graph.getIndexByValue(source);

		// 检查一下起始点索引是否有效
		if (sourceIndex == -1) {
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

		// 最终，我们需要调用 buildSPT 方法，生成最短路径树（其实并不是树，而是维护两个数组）
		bulidSPT();
	}

	/**
	 * Bellman-Ford 算法的核心内容
	 */
	private void bulidSPT() {
		// 首先，获取邻接表数组
		Vertex<V>[] adjTable = graph.getAdjTable();
		// 然后我们设置一个 flag 参数，尽量减少循环次数
		boolean flag = false;
		int fromIndex = -1;
		int toIndex = -1;

		// 确定最外层的循环次数
		for (int i = 0; i < adjTable.length - 1; i++) {
			// 内层循环遍历邻接表
			for (int j = 0; j < adjTable.length; j++) {
				// 拿到链表中的每一条边
				for (Edge edge : adjTable[j].getAdjList()) {
					fromIndex = edge.getFromIndex();
					toIndex = edge.getToIndex();
					if (dist[toIndex] > dist[fromIndex] + edge.getWeight()) {
						dist[toIndex] = dist[fromIndex] + edge.getWeight();
						preVertex[toIndex] = fromIndex;
						flag = true;
					}
				}
			}

			// 一次总的循环下来，如果 flag 还是false ，那么说明 dist 数组都没有变过，最短路径树已经找到了
			if (!flag) {
				break;
			} else {
				// 如果flag 改过，那么我们再把 flag 改回 false, 准备下一次循环
				flag = false;
			}
		}

		// 重新来三个循环，我们想要检测是否存在负权环
		for (int i = 0; i < adjTable.length - 1; i++) {
			for (int j = 0; j < adjTable.length; j++) {
				for (Edge edge : adjTable[j].getAdjList()) {
					fromIndex = edge.getFromIndex();
					toIndex = edge.getToIndex();
					if (dist[toIndex] > dist[fromIndex] + edge.getWeight()) {
						// 在负权环内，或者受负权环影响的顶点，距离全部直接设置成负无穷大
						dist[toIndex] = Double.NEGATIVE_INFINITY;
						preVertex[toIndex] = fromIndex;
						flag = true;
					}
				}
			}

			if (!flag) {
				break;
			} else {
				flag = false;
				isContainingNegativeCircle = true;
			}
		}
	}

	/**
	 * 确认起点是否可以到达指定的顶点
	 * 
	 * @param destVertex
	 * @return
	 */
	public boolean hasPathTo(V destVertex) {
		// 首先，我们根据 destVertex 确定其对应的 索引
		int destIndex = graph.getIndexByValue(destVertex);
		// 如果输入的顶点根本就找不到对应的索引，那么说明这个顶点不存在
		if (destIndex == -1) {
			throw new RuntimeException("输入的顶点不存在！");
		}
		if (dist[destIndex] == Double.NEGATIVE_INFINITY) {
			return false;
		}
		// 如果 dist数组对应的值不等于 最大值的话，那么我们就认为起点是可以连通 指定顶点的
		return dist[destIndex] != Double.POSITIVE_INFINITY;
	}

	/**
	 * 返回一个栈集合，里面保存着起点到指定顶点的路径
	 * 
	 * @param destVertex
	 * @return
	 */
	public Stack<V> pathTo(V destVertex) {
		if (hasPathTo(destVertex)) {
			// 首先，我们根据 destVertex 确定其对应的 索引
			int destIndex = graph.getIndexByValue(destVertex);
			// 然后，创建一个 Stack 对象
			Stack<V> stack = new Stack<V>();
			Vertex<V>[] adjTable = graph.getAdjTable();
			// 添加终点
			stack.add(adjTable[destIndex].getValue());
			int tempIndex = preVertex[destIndex];
			// 【注意】 起始顶点的前一个顶点的索引我们设置成 -1 ，这里作为退出循环的条件
			while (tempIndex != -1) {
				stack.add(adjTable[tempIndex].getValue());
				tempIndex = preVertex[tempIndex];
			}
			return stack;
		} else {
			throw new RuntimeException("起点无法连通到指定的顶点， 或者存在负权环！");
		}
	}

	/**
	 * 返回起点到指定顶点的最短路路径长度
	 * 
	 * @param destVertex
	 * @return
	 */
	public Double minDistanceTo(V destVertex) {
		int destIndex = graph.getIndexByValue(destVertex);
		if (destIndex == -1) {
			throw new RuntimeException("指定的顶点不存在");
		} else {
			return dist[destIndex];
		}
	}
}
