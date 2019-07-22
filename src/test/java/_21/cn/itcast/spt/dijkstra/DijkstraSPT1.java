package _21.cn.itcast.spt.dijkstra;

import java.util.ArrayList;
import java.util.Stack;

import _16.cn.itcast.undirectedGraph.UndirectedGraph;

/**
 * 	传入一个使用 邻接矩阵实现的 无向带权图 和 起始点， 我们就可以使用 Dijkstra 算法求出以这个起始点为根结点的最短路径树
 * @author Administrator
 *
 * @param <V>
 */
public class DijkstraSPT1<V> {
	private UndirectedGraph<V> graph;
	// 记录从起始点到本顶点最短的距离
	private int[] dist;
	// 记录从起始点到本顶点最短路线的上一个顶点的索引
	private int[] preVertex;
	// 记录起始点的索引
	private int sourceIndex;
	
	// 构造方法我们只需要传入原图对象就可以了，不需要像 求最小生成树那样检查是否是连通图
	// 因为求最短路径树，我们并不要求原图必须是一个连通图
	public DijkstraSPT1(UndirectedGraph<V> graph, V source) {
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
		// 
		dist = new int[graph.getVertexCount()];
		for (int i = 0; i < dist.length; i++) {
			if(i == sourceIndex) {
				dist[i] = 0;
			}else {
				dist[i] = Integer.MAX_VALUE;
			}
		}
		
		// 初始化 preVertex 数组，包括起点，全部初始化为 -1
		preVertex = new int[graph.getVertexCount()];
		for (int i = 0; i < preVertex.length; i++) {
			preVertex[i] = -1;
		}
		
		// 最终，我们需要调用 buildSPT 方法，生成最短路径树（其实并不是树，而是维护两个数组）
		bulidSPT();
	}
	
	/**
	 * 	返回一棵最短路径树
	 * 	【说明】 这里面的思路跟 prim 算法求最小生成树非常相似，都是不停地把 最短路径树切分成两个集合
	 * 			然后找到最短路径树的边
	 * 	【说明】 其实另外返回一棵最短路径树并不是很有必要，因为我们树本身并没有提供查起点到终点的路线、距离等 api
	 * 			所以如果我们想要返回这些信息，还是得靠本类中的 dist 和  preVertex 去获取
	 * @param sourceIndex	起始点索引
	 */
	private void bulidSPT(){
		// 我们就可以创建一个集合，从起始点开始进行切分了
		ArrayList<Integer> selected = new ArrayList<>();
		selected.add(sourceIndex);
		// 因为我们这个原图是使用邻接矩阵实现的，所以我们用不了 优先级队列，确认横切边，只能通过遍历二维数组
		int[][] adjMatrix = graph.getAdjMatrix();
		// 我们需要使用几个变量来找到所有横切边中的最小值
		int minDistance = Integer.MAX_VALUE;
		int fromIndex = 0;
		int toIndex = 0;
		
		while(true){
			// 遍历 selected 集合，找到全部已经选中的顶点
			// 通过这些顶点去找本次切分对应的全部横切边
			for (Integer vIndex : selected) {
				for (int i = 0; i < adjMatrix[vIndex].length; i++) {
					// 横切边要求 权值大于0，我们事先规定权值等于0 则为不存在
					// 横切边还要求另一个顶点必须不在集合中（也就是 i 不在 selected 中）
					// 横切边的权值加上 边的起点记录的 dist ，小于 minDistance ，我们进入下面的逻辑
					if(adjMatrix[vIndex][i] > 0 && !selected.contains(i) && minDistance > adjMatrix[vIndex][i] + dist[vIndex]) {
						minDistance = adjMatrix[vIndex][i] + dist[vIndex];
						fromIndex = vIndex;
						toIndex = i;
					}
				}
			}
			
			// 不管怎么样，只要前面的循环走下来， minDistance 的值小于 Integer.MAX_VALUE 
			// 说明我们找到一个最短的横切边了
			if(minDistance < Integer.MAX_VALUE) {
				// 首先，我们更新  dist 数组
				dist[toIndex] = minDistance;
				// 然后更新 preVertex 数组
				preVertex[toIndex] = fromIndex;
				// 再然后，我们需要把  toIndex 对应的顶点也添加到 selected 集合中，下次切分的时候，一起包含此顶点
				selected.add(toIndex);
				
				// 最后，我们需要再初始化一下 minDistance
				minDistance = Integer.MAX_VALUE;
			}else {
				// 如果 minDistance 的值如果没有被更新，说明已经找不到横切边了，我们就停止循环
				break;
			}
		}
	}
	
	/**
	 * 	确认起点是否可以到达指定的顶点
	 * 	【说明】 前面我们求最短路径树时候，会维护 dist 数组。 如果这个顶点对应的 dist 数组值不等于 Integer.max_value 
	 * 			那么说明起点是可以连通到这个顶点的。
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
		// 如果 dist数组对应的值不等于 最大值的话，那么我们就认为起点是可以连通 指定顶点的
		return dist[destIndex] != Integer.MAX_VALUE;
	}
	
	/**
	 * 	返回一个栈集合，里面保存着起点到指定顶点的路径
	 * 	【说明】 因为 preVertex 数组中的每个元素都 只保存起点到对应顶点的 最短路径的上一个顶点
	 * 			所以我们如果想要输出整条最短路径，我们得从指定顶点开始遍历，依次添加到栈结构中，
	 * 			根据栈结构先入后出的特点，就可以输出从起点到终点的全部顶点了
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
			throw new RuntimeException("起点无法连通到指定的顶点");
		}
	}
	
	/**
	 * 	返回起点到指定顶点的最短路路径长度
	 * 	【注意】 如果返回一个整数的最大值，说明这个顶点跟起点不连通
	 * @param destVertex
	 * @return
	 */
	public int minDistanceTo(V destVertex) {
		int destIndex = graph.getVertexs().indexOf(destVertex);
		if(destIndex == -1) {
			throw new RuntimeException("指定的顶点不存在");
		}else {
			return dist[destIndex];
		}
	}
}
