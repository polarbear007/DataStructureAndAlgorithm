package _26.cn.itcast.spt.johnson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph2;
import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;

public class JohnsonForSPTByAdjacencyList<V> {
	private Double[][] dist;
	private int[][] preVertex;
	private Double[] tempDist;
	private DirectedGraph2<V> graph;
	
	/**
	 * 	构造函数传入 graph 对象
	 * @param graph
	 */
	public JohnsonForSPTByAdjacencyList(DirectedGraph2<V> graph) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		this.graph = graph;
		// 使用 Bellman-Ford 算法对原图进行处理，返回以一个临时顶点 T 为起点的最短路径树
		tempDist = getTempDistByBellmanFord(graph);
		
		int vertexCount = graph.getVertexCount();
		
		// 初始化 dist[][] 数组
		dist = new Double[vertexCount][vertexCount];
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				if(i == j) {
					dist[i][j] = 0.0;
				}else {
					dist[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}
		
		// 初始化 preVertex[][] 数组
		preVertex = new int[vertexCount][vertexCount];
		for (int i = 0; i < preVertex.length; i++) {
			Arrays.fill(preVertex[i], -1);
		}
		
		// 有了这个 tempDist 数组以后，我们就可以把每条边都转成非负的权值，然后使用Dijkstra 算法
		//  去求各个以各个顶点为起点的最短路径树（其实就是在维护二维的  dist 和 preVertex 数组）
		//  每次只维护一行
		buildSPT();
	}

	/**
	 * 	执行 n 交 Dijkstra 算法，每次更新一行 dist 
	 */
	private void buildSPT() {
		// 首先，获取 邻接表
		Vertex<V>[] adjTable = graph.getAdjTable();
		// 然后，创建一些后面需要用到的临时变量
		// 我们需要使用一个优化级队列来优化最短横切边的比较
		// 但是要注意，我们这里可不是求最小生成树，比较的不是边本身的权值，而是 （边的权值 + dist[i][fromIndex])
		// 而且，因为考虑到负值，边的权值也不是本身，而是根据 tempDist[] 数组转化过的 权值
		//	因此，为了使用上优先级队列，我们需要再创建一个 EdgeWrapper ，就像以前我们搞 Dijkstra 算法一样
		PriorityQueue<EdgeWrapper> queue = new PriorityQueue<EdgeWrapper>();
		// 我们还需要一个普通的集合，用来保存切分以后的顶点集合
		ArrayList<Integer> selected = new ArrayList<Integer>();
		
		int vertexCount = graph.getVertexCount();
		EdgeWrapper minEdge = null;
		int fromIndex = -1;
		int toIndex = -1;
		LinkedList<Edge> adjList = null;
		
		// 然后我们确定总的循环次数为 n 次，每次循环都确认一个顶点为起点
		for (int i = 0; i < vertexCount; i++) {
			//清空  selected 集合
			selected.clear();
			// 然后，添加 i 作为本次Dijkstra 算法的起点
			selected.add(i);
			// 再然后，我们把起点相关的边，全部添加到优先级队列中
			// 【注意】我们不能添加边对象本身，而是应该添加包装类
			adjList = adjTable[i].getAdjList();
			for (Edge edge : adjList) {
				queue.add(new EdgeWrapper(i, edge));
			}
			
			while(!queue.isEmpty()) {
				// 弹出一条最短横切边的包装类
				minEdge = queue.poll();
				// 然后，我们需要看一下这条边是不是横切边（有可能本来是，但是弹出来的时候，两个顶点都被切分到 selected 集合了）
				fromIndex = minEdge.getEdge().getFromIndex();
				toIndex = minEdge.getEdge().getToIndex();
				
				// 如果不是横切边，那么我们再弹出一条边
				if(selected.contains(toIndex)) {
					continue;
				}else { // 是横切边，那么我们就进行松驰操作
					dist[i][toIndex] = minEdge.getValue();
					preVertex[i][toIndex] = fromIndex;
					// 然后，把 toIndex 添加到 selected 集合中，下次切分的时候，需要把 toIndex 也包含进去
					selected.add(toIndex);
					
					// 然后，我们需要把以 toIndex 作为起点的边，全部添加到 优先级队列中
					//   如果这条边的另一个toIndex 已经存在于 selected 中，那么我们就不要再添加了
					adjList = adjTable[toIndex].getAdjList();
					for (Edge edge : adjList) {
						if(!selected.contains(edge.getToIndex())) {
							queue.add(new EdgeWrapper(i, edge));
						}
					}
				}
			}
		}
	}

	/**
	 * 	重新实现一下 Bellman-Ford 算法的队列优化
	 * @param graph
	 * @return
	 */
	private Double[] getTempDistByBellmanFord(DirectedGraph2<V> graph) {
		// 首先，一来先获取邻接表
		Vertex<V>[] adjTable = graph.getAdjTable();
		int vertexCount = graph.getVertexCount();
		
		// 然后，我们需要添加一个临时顶点 T 
		Vertex<V>[] newAdjTable = new Vertex[vertexCount + 1];
		// 遍历原来的邻接表，把全部数据放进新的邻接表中
		for (int i = 0; i < adjTable.length; i++) {
			newAdjTable[i] = adjTable[i];
		}
		// 创建一个新的顶点对象，放进新的邻接表中
		Vertex<V> newVertex = new Vertex<V>();
		LinkedList<Edge> adjList = newVertex.getAdjList();
		for (int i = 0; i < newAdjTable.length; i++) {
			// 添加 临时顶点到各个顶点的边， 权值都为 0.0
			adjList.add(new DirectedGraph2.Edge(vertexCount, i, 0.0));
		}
		newAdjTable[vertexCount] = newVertex;
		
		// 然后，我们需要创建并初始化最终要返回的 dist 对象
		Double[] dist = new Double[vertexCount + 1];
		Arrays.fill(dist, Double.POSITIVE_INFINITY);
		dist[vertexCount] = 0.0;
		
		// 我们使用队列优化的 bellman-Ford 算法，这样效率高一些
		//	所以我们需要再创建一个队列 
		LinkedList<Integer> queue = new LinkedList<Integer>();
		// 为了防止重复添加队列，我们维护一个 isInQueue 数组
		boolean[] isInQueue = new boolean[vertexCount + 1];
		// 为了发现负环，我们再维护一个数组，统计各个顶点添加进队列的次数
		int[] count = new int[vertexCount + 1];
		// 然后，我们需要把 起点放进队列中
		queue.add(vertexCount);
		isInQueue[vertexCount] = true;
		count[vertexCount]++;
		
		int currIndex = -1;
		int fromIndex = -1;
		int toIndex = -1;
		Double weight = null;
		
		while(!queue.isEmpty()) {
			currIndex = queue.poll();
			isInQueue[currIndex] = false;
			
			// 我们拿到一个顶点以后，就找到这个顶点全部的边，然后尝试进行松驰操作
			adjList = newAdjTable[currIndex].getAdjList();
			for (Edge edge : adjList) {
				fromIndex = edge.getFromIndex();
				toIndex = edge.getToIndex();
				weight = edge.getWeight();
				
				// 因为是链表结构，能拿到边对象，肯定就存在这条边，所以我们只需要判断能不能进行松驰操作即可
				if(dist[toIndex] > dist[fromIndex] + edge.getWeight()) {
					// 不管怎么样，我们先正常松驰操作，不需要维护 preVertex 
					dist[toIndex] = dist[fromIndex] + weight;
					
					// 正常松驰操作以后，我们看一下 toIndex 是否在队列中，如果不在，那么我们就 添加到队列
					if(!isInQueue[toIndex]) {
						queue.add(toIndex);
						count[toIndex]++;
					}
					
					// 我们不管前面是否添加到队列中，如果 toIndex 这个顶点添加到队列中超过  n-1 次
					// 第 n - 1 次没事，但是第 n 次添加，那么我们就马上 可以确认，这个顶点是在某个负权环上面
					// 我们就认为存在负权环，直接扔异常
					if(count[toIndex] >= newAdjTable.length) {
						throw new RuntimeException("此图存在负权环，无法求最短路径");
					}
				}
			}
		}
		
		return dist;
	}
	
	/**
	 * 	确认两个顶点是否连通
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public boolean isConnected(V fromVertex, V toVertex) {
		// 然后，根据输入的两个顶点确认顶点的索引
		int fromIndex = graph.getIndexByValue(fromVertex);
		int toIndex = graph.getIndexByValue(toVertex);
		if(fromIndex == -1) {
			throw new RuntimeException("起始顶点不存在");
		}
		if(toIndex == -1) {
			throw new RuntimeException("目的地顶点不存在");
		}
		
		// 如果两个顶点都没有什么问题，那么我们看一下 dist[fromIndex][toIndex] 的值是否是正无穷大
		//   等于正无穷大，说明两个顶点没有连通，返回false
		//   不等于正无穷大，说明两个顶点有连通，返回 true
		return  dist[fromIndex][toIndex] != Double.POSITIVE_INFINITY;
	}
	
	/**
	 * 	返回两个连通顶点的最短路径
	 * 	【注意】 如果这两个顶点不连通，那么我们也不需要获取什么最短路径了
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public Stack<V> getShortestPath(V fromVertex, V toVertex){
		// 如果有联通，我们再去求最短路径的路线，如果没有联通，那么我们也不考虑什么最短路径了
		if(isConnected(fromVertex, toVertex)) {
			Vertex<V>[] adjTable = graph.getAdjTable();
			// 根据输入的两个顶点确认顶点的索引
			int fromIndex = graph.getIndexByValue(fromVertex);
			int toIndex = graph.getIndexByValue(toVertex);
			Stack<V> stack = new Stack<V>();
			// 首先，我们需要把终点的索引添加到栈中
			stack.add(adjTable[toIndex].getValue());
			
			while(true) {
				if(preVertex[fromIndex][toIndex] != -1) {
					stack.add(adjTable[preVertex[fromIndex][toIndex]].getValue());
					toIndex = preVertex[fromIndex][toIndex];
				}else {
					return stack;
				}
			}
		}else {
			throw new RuntimeException("这两个顶点并不连通，不存在最短路径");
		}
	}
	
	/**
	 * 	如果两个顶点不连通，那么我们也不求什么最短路径了
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public Double getMinDistance(V fromVertex, V toVertex) {
		// 如果有联通，我们再去求最短路径的路线，如果没有联通，那么我们也不考虑什么最短路径了
		if(isConnected(fromVertex, toVertex)) {
			// 根据输入的两个顶点确认顶点的索引
			int fromIndex = graph.getIndexByValue(fromVertex);
			int toIndex = graph.getIndexByValue(toVertex);
			
			return dist[fromIndex][toIndex] - tempDist[fromIndex] + tempDist[toIndex];
		}else {
			throw new RuntimeException("这两个顶点并不连通，不存在最短路径");
		}
	}
	
	/**
	 * 	Dijkstra 算法的优先队列优化，需要我们比较  边的权值 + 边的起点距离起点的最短距离 
	 * 	而不像 Prim 算法求最小生成树那样，只需要比较边的权值就可以找到最短横切边
	 * 	【因此】我们对普通的 边 进行包装，把包装类对象放进优化队列中，优化横切边的比较
	 * 	 【注意】 相比普通的 Dijkstra 算法，只维护一个一维的  dist 数组，本算法维护的是二维的 dist 数组
	 * 			所以我们还得再额外传进来一个 起点的索引才能找到   【边的起点距离起点的最短距离】 
	 * @author Administrator
	 *
	 */
	class EdgeWrapper implements Comparable<EdgeWrapper>{
		private Edge edge;
		private int sourceIndex;
		
		public EdgeWrapper(int sourceIndex, Edge edge) {
			this.sourceIndex = sourceIndex;
			this.edge = edge;
		}
		
		public Edge getEdge() {
			return edge;
		}

		// 因为 compareTo 方法需要比较的值计算比较麻烦，我们专门用一个方法来获取这个比较的值
		// 首先，我们计算转换过后的非负权值为：   edge.getWeight() + tempDist[edge.getFromIndex()] - tempDist[edge.getToIndex()]
		// 然后，我们还需要加上， 上一个顶点距离起点的最短距离   dist[sourceIndex][edge.getFromIndex]
		public Double getValue() {
			return dist[sourceIndex][edge.getFromIndex()] + edge.getWeight() + tempDist[edge.getFromIndex()] - tempDist[edge.getToIndex()];
		}

		@Override
		public int compareTo(EdgeWrapper other) {
			return this.getValue().compareTo(other.getValue());
		}
		
	}
}
