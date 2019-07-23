package _23.cn.itcast.spt.bellmanFord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph;

/**
 * 	使用 Bellman-Ford 算法处理 邻接矩阵 实现的有向图， 得到单源最短路径树
 * @author Administrator
 *
 */
public class BellmanFordForAdjacencyMatrix<V> {
	DirectedGraph<V> graph;
	// 记录从起始点到本顶点最短的距离
	Double[] dist;
	// 记录从起始点到本顶点最短路线的上一个顶点的索引
	int[] preVertex;
	// 记录起始点的索引
	int sourceIndex;
	// 记录这个图是否存在 负权环
	private boolean isContainingNegativeCircle;
	
	/**
	 * 	检查是否存在负权环
	 * @return
	 */
	public boolean isContainingNegativeCircle() {
		return isContainingNegativeCircle;
	}

	// 构造方法我们只需要传入原图对象就可以了，不需要像 求最小生成树那样检查是否是连通图
	// 因为求最短路径树，我们并不要求原图必须是一个连通图
	public BellmanFordForAdjacencyMatrix(DirectedGraph<V> graph, V source) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;
		
		// 根据传入的起始点值查找对应的索引
		int sourceIndex = graph.getVertexs().indexOf(source);
		
		// 检查一下起始点索引是否有效
		if(sourceIndex == -1) {
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
	 * 	Bellman-Ford 算法的核心内容
	 */
	void bulidSPT() {
		Double[][] adjMatrix = graph.getAdjMatrix();
		boolean flag = false;
		// 最外层的循环 n - 1 次 
		for (int i = 0; i < graph.getVertexCount() - 1; i++) {
			// 遍历邻接矩阵中每一条权值不为 null 的边
			for (int j = 0; j < adjMatrix.length; j++) {
				for (int k = 0; k < adjMatrix[i].length; k++) {
					if(adjMatrix[j][k] != null && dist[k] > dist[j] + adjMatrix[j][k]) {
						dist[k] = dist[j] + adjMatrix[j][k];
						preVertex[k] = j;
						flag = true;
					}
				}
			}
			
			// 稍微优化一下 循环次数，如果 一次循环下来， flag 还是 false ，说明没有改变过 dist 数组
			// 那么我们可以直接确定最短路径树已经形成，不需要循环到 n-1 次
			if(!flag) {
				break;
			}else {
				// 如果 flag 等于 true, 说明本次循环改变了 dist 数组，我们可能还需要进行下一次循环
				// 把  flag 重新变成 false ，准备下次循环
				flag = false;
			}
		}
		
		// 正常情况下，走完上面的三层循环，也就结束了。
		// 但是如果我们想要检测是否存在负权环，那么就需要再执行一套三层循环
		for (int i = 0; i < graph.getVertexCount() - 1; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {
				for (int k = 0; k < adjMatrix[i].length; k++) {
					if(adjMatrix[j][k] != null && dist[k] > dist[j] + adjMatrix[j][k]) {
						// 注意，本次循环一发现可以进行松驰操作的，直接把 dist[k] 赋值为 负无穷
						dist[k] = Double.NEGATIVE_INFINITY;
						preVertex[k] = j;
						flag = true;
					}
				}
			}
			
			if(!flag) {
				break;
			}else {
				// 如果有任何修改，我们就把 这个变量变成 true 
				isContainingNegativeCircle = true;
				flag = false;
			}
		}
	}
	
	/**
	 * 	确认起点是否可以到达指定的顶点
	 * 	【说明】 前面我们求最短路径树时候，会维护 dist 数组。 如果这个顶点对应的 dist 数组值不等于 正无穷大 
	 * 			那么说明起点是可以连通到这个顶点的。
	 * 	【注意】 因为我们引入了负权值，所以其实可能会存在负权环的问题，这里我们需要做个限制，如果 dist 表上记录
	 * 		   起点到指定点的最短距离为 负无穷大，我们这里也返回 false; 
	 * 		   因为如果你返回 true 的话，用户接下来就可能要你返回这条最短路径，在返回最短路径的方法可能会陷入死循环
	 * @param destVertex
	 * @return
	 */
	public boolean hasPathTo(V destVertex) {
		// 首先，我们根据 destVertex 确定其对应的 索引
		int destIndex = graph.getVertexs().indexOf(destVertex);
		// 如果输入的顶点根本就找不到对应的索引，那么说明这个顶点不存在
		if(destIndex == -1) {
			throw new RuntimeException("输入的顶点不存在！");
		}
		if(dist[destIndex] == Double.NEGATIVE_INFINITY) {
			return false;
		}
		// 如果 dist数组对应的值不等于 最大值的话，那么我们就认为起点是可以连通 指定顶点的
		return dist[destIndex] != Double.POSITIVE_INFINITY;
	}
	
	/**
	 * 	返回一个栈集合，里面保存着起点到指定顶点的路径
	 * 	【说明】 因为 preVertex 数组中的每个元素都 只保存起点到对应顶点的 最短路径的上一个顶点
	 * 			所以我们如果想要输出整条最短路径，我们得从指定顶点开始遍历，依次添加到栈结构中，
	 * 			根据栈结构先入后出的特点，就可以输出从起点到终点的全部顶点了
	 * 
	 * 	【注意】 当一个图中存在负权环，并不一定会导致全部的顶点都失效。
	 * 			如果我们要查的顶点 是 不在负权环中的点，那么下面的 pathTo 方法可以正常输出路径
	 * 			如果我们要查的顶点 在负权环中或者会受到负权环的影响 ，那么下面的pathTo 方法就会陷入死循环。
	 * 				因此我们在 hasPathTo 方法里对受负权环影响的顶点，全部返回 false.
	 * 
	 * 	【思考】 什么情况下，负权环会导致全部的顶点失效呢？
	 * 			当起点受到负权环的影响时，其实整棵最短路径树都会受到影响。此时，甚至连通起点到起点的路径长度都会变成负无穷大。
	 * @param destVertex
	 * @return
	 */
	public Stack<V> pathTo(V destVertex){
		if(hasPathTo(destVertex)) {
			// 首先，我们根据 destVertex 确定其对应的 索引
			int destIndex = graph.getVertexs().indexOf(destVertex);
			// 然后，创建一个 Stack 对象
			Stack<V> stack = new Stack<V>();
			ArrayList<V> vertexs = graph.getVertexs();
			// 添加终点
			stack.add(vertexs.get(destIndex));
			int tempIndex = preVertex[destIndex];
			// 【注意】 起始顶点的前一个顶点的索引我们设置成 -1 ，这里作为退出循环的条件
			while(tempIndex != -1) {
				stack.add(vertexs.get(tempIndex));
				tempIndex = preVertex[tempIndex];
			}
			return stack;
		}else{
			throw new RuntimeException("起点无法连通到指定的顶点， 或者存在负权环！");
		}
	}
	
	/**
	 * 	返回起点到指定顶点的最短路路径长度
	 * 	【注意】 如果返回一个整数的最大值，说明这个顶点跟起点不连通
	 * @param destVertex
	 * @return
	 */
	public Double minDistanceTo(V destVertex) {
		int destIndex = graph.getVertexs().indexOf(destVertex);
		if(destIndex == -1) {
			throw new RuntimeException("指定的顶点不存在");
		}else {
			return dist[destIndex];
		}
	}
}
