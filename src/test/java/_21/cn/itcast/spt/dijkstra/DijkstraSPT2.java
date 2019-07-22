package _21.cn.itcast.spt.dijkstra;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import _16.cn.itcast.undirectedGraph.UndirectedGraph3;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3.Edge;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3.Vertex;

/**
 * 	Dijkstra 算法构建 单源 最短路径树 （邻接表法）
 * 	【说明】 因为有 Edge 对象，我们可以使用优先级队列来优化横切边的比较。
 * 			又因为并不是直接比较 Edge 对象的 权值，所以我们又在外面包了一层。
 * @author Administrator
 *
 * @param <V>
 */

public class DijkstraSPT2<V> {
	private UndirectedGraph3<V> graph;
	// 记录从起始点到本顶点最短的距离
	private int[] dist;
	// 记录从起始点到本顶点最短路线的上一个顶点的索引
	private int[] preVertex;
	// 记录起始点的索引
	private int sourceIndex;
	
	// 构造方法，传入原图对象 和 初始顶点
	public DijkstraSPT2(UndirectedGraph3<V> graph, V source) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;
		
		// 根据传入的起始点值查找对应的索引
		int sourceIndex = getVertexIndex(source);
		
		// 检查一下起始点索引是否有效
		if(sourceIndex == -1) {
			throw new RuntimeException("指定的起点不存在！");
		}
		// 初始化起始点索引
		this.sourceIndex = sourceIndex;
		
		// 初始化dist 和 lastVertex 数组
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
	 * 	核心方法，使用dijkstra 算法来生成最短路径树
	 * 	【说明】
	 * 		虽然是说构建最短路径树，但是实际上我们只是维护 dist 和 preVertex 数组而已
	 * 	【说明】
	 * 		因为这个类传入的原图对象封装了 edge 对象，所以我们可以使用优先级队列来优化对 横切边的比较
	 */
	private void bulidSPT() {
		// 首先，我们构建一个集合，用来切分最短路径树
		ArrayList<Integer> selected = new ArrayList<Integer>();
		// 刚开始的时候，我们必须把 sourceIndex 加进去，不像最小生成树那样随便切分
		selected.add(sourceIndex);
		// 获取邻接表
		Vertex<V>[] adjTable = graph.getAdjTable();
		
		// 然后，我们还需要构建一个最小堆构建的优先级队列
		// 【注意】 优先级队列中，我们应该使用 边的权值(weight) + 边起始顶点对应 的 dist[fromIndex] 值
		//        而不是边的权值本身。
		//        这样子一来，我们如果使用普通的优先级队列好像就不太合适了，因为边里面的权值你也不能随便改
		//        如果可以实现一个 key - value 形式的优先级队列，那么这里用起来就很舒服了
		//         key 用的  edgeWeight +  dist[edge.fromIndex], value 就用 edge 对象
		// 【说明】 我们偷懒，不想另外再去实现这样的优先级队列，所以我们就取个巧，把 Edge 对象再封装一层
		PriorityQueue<EdgeWrapper> queue = new PriorityQueue<EdgeWrapper>();
		//	首次分切，selected 集合中只有一个起点，所以跟起点相关的全部顶点都是横切边
		for (Edge edge : adjTable[sourceIndex].getAdjList()) {
			queue.add(new EdgeWrapper(edge));
		}
		
		int fromIndex = -1;
		int toIndex  = -1;
		int weight = 0;
		
		// 然后我们就开始 根据优先队列弹出的横切边 找最短路径树的边
		while(true) {
			EdgeWrapper edgeWrapper = queue.poll();
			// 如果优先队列弹出的元素为空，那么说明最短路径树已经构建完成了，退出循环
			if(edgeWrapper == null) {
				break;
			}
			// 横切边必须要符合， toIndex 对应的顶点不在 selected 中。 如果不满足，我们直接再弹出一条边
			if(selected.contains(edgeWrapper.edge.getToIndex())) {
				continue;
			}else {
				toIndex = edgeWrapper.edge.getToIndex();
				fromIndex = edgeWrapper.edge.getFromIndex();
				weight = edgeWrapper.edge.getWeight();
				// 如果横切边符合要求，那么  edge 肯定就是最短路径树的一条边
				// 我们首先维护 dist 数组
				dist[toIndex] = weight + dist[fromIndex];
				// 再维护 preVertex 数组
				preVertex[toIndex] = fromIndex;
				// 再维护 selected 集合
				selected.add(toIndex);
				// 再维护 queue 队列, 把添加添加的点对应的边添加到队列中
				// 注意，添加的时候，尽量屏蔽 toIndex 已经在 selected 集合中的边
				for (Edge edge : adjTable[toIndex].getAdjList()) {
					if(!selected.contains(edge.getToIndex())) {
						queue.add(new EdgeWrapper(edge));
					}
				}
			}
		}
	}
	
	/**
	 * 	根据顶点值获取对应的索引
	 * @param vertex
	 * @return
	 */
	private int getVertexIndex(V vertex) {
		Vertex<V>[] adjTable = graph.getAdjTable();
		for (int i = 0; i < adjTable.length; i++) {
			if(adjTable[i].getValue().equals(vertex)) {
				return i;
			}
		}
		return -1;
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
		int destIndex = getVertexIndex(destVertex);
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
			int destIndex = getVertexIndex(destVertex);
			// 然后，创建一个 Stack 对象
			Stack<V> stack = new Stack<V>();
			Vertex<V>[] adjTable = graph.getAdjTable();
			// 添加终点
			stack.add(adjTable[destIndex].getValue());
			int tempIndex = preVertex[destIndex];
			// 【注意】 起始顶点的前一个顶点的索引我们设置成 -1 ，这里作为退出循环的条件
			while(tempIndex != -1) {
				stack.add(adjTable[tempIndex].getValue());
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
		int destIndex = getVertexIndex(destVertex);
		if(destIndex == -1) {
			throw new RuntimeException("指定的顶点不存在");
		}else {
			return dist[destIndex];
		}
	}
	
	/**
	 * 	为了使用普通的优先队列，又不去改变 edge 对象的值，我们这里创建一个包装类
	 * @author Administrator
	 *
	 */
	class EdgeWrapper implements Comparable<EdgeWrapper>{
		private Edge edge;
		
		public EdgeWrapper(Edge edge) {
			super();
			this.edge = edge;
		}

		@Override
		public int compareTo(EdgeWrapper other) {
			return (edge.getWeight() + dist[edge.getFromIndex()]) - (other.edge.getWeight() + dist[other.edge.getFromIndex()]) ;
		}
	}
}
