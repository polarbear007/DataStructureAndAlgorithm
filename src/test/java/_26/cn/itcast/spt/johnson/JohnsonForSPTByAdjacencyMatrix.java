package _26.cn.itcast.spt.johnson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

import _22.cn.itcast.directedGraph.DirectedGraph;

public class JohnsonForSPTByAdjacencyMatrix<V> {
	private Double[][] dist;
	private int[][] preVertex;
	private Double[] tempDist;
	private DirectedGraph<V> graph;
	
	/**
	 * 	构造函数传入 graph 对象
	 * @param graph
	 */
	public JohnsonForSPTByAdjacencyMatrix(DirectedGraph<V> graph) {
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
	 * 	运行 V 次 Dijkstra 算法，尝试从各个顶点出发，生成最短路径树
	 */
	private void buildSPT() {
		// 首先，把最根本的邻接矩阵拿到
		Double[][] adjMatrix = graph.getAdjMatrix();
		// 然后，先把进行 Dijkstra 算法需要维护的几个变量先创建好
		// 切分顶点的时候，需要创建一个 selected 集合
		ArrayList<Integer> selected = new ArrayList<Integer>();
		// 每次切分以后，需要通过比较找到最短的横切边，因为是邻接矩阵，没有封装 Edge 对象，
		// 所以我们得创建一个  minWeight 来保存，最短横切边的权值（新权值，非负）
		Double minWeight = Double.POSITIVE_INFINITY;
		// fromIndex 保存最短横切边的起点顶点索引
		int fromIndex = -1;
		// toIndex 保存最短横切边的终点索引
		int toIndex = -1;
		
		// 然后，我们确定总的需要循环 V 次，每次循环，都可以确认一个起点
		int vertexCount = graph.getVertexCount();
		for (int i = 0; i < vertexCount; i++) {
			// 下次循环的时候，需要指定不同的起点，所以我们需要清空一下 selected 集合
			selected.clear();
			
			// 现在我们确认 起点为 i ，先把i 放进 selected 集合
			selected.add(i);
			while(true) {
				// 遍历 selected 切分的顶点集合，找到并遍历全部的横切边
				for (Integer index : selected) {
					for (int j = 0; j < adjMatrix[index].length; j++) {
						// 首先，这条边要存在，也就是  adjMatrix[index][j] != null
						// 然后，这条边的两个顶点不能都出现在 selected 集合中
						// 再然后，这条横切边（要使用新权值 [ weight + T(from) - T(to)]  去比较）要小于当前的 minWeight 
						if(adjMatrix[index][j] != null && !selected.contains(j) && (adjMatrix[index][j] + tempDist[index] - tempDist[j]) < minWeight) {
							// 如果上面的条件都满足，我们就更新  minWeight 、 fromIndex 和 toIndex
							minWeight = adjMatrix[index][j] + tempDist[index] - tempDist[j];
							fromIndex = index;
							toIndex = j;
						}
					}
				}
				
				// 如果上面的循环走下来，都没有成功进行松驰操作过，那么我们就认为最短路径树已经生成了
				if(minWeight == Double.POSITIVE_INFINITY) {
					break;
				}else {
					// 否则，上面的循环结束以后，我们就找到最短的横切边了
					// 再然后，我们就进行松驰操作
					dist[i][toIndex] = dist[i][fromIndex] + minWeight;
					preVertex[i][toIndex] = fromIndex;
					
					// 现在 toIndex 对应的顶点已经确认好最短路径了，我们下次切分的时候需要把这个顶点也放进 selected 
					selected.add(toIndex);
					
					// 为了方便下次循环，我们需要再把minWeight 变回正无穷大
					minWeight = Double.POSITIVE_INFINITY;
				}
			}
			
		}
	}

	/**
	 * 	【注意】 这里我们直接重新实现一遍 Bellman-ford 算法，因为在这个过程中，我们需要添加一个额外的顶点。
	 * 			更重要的原因是，我们不好在 Bellman-Ford 算法中添加一个 返回 dist 数组的方法。这里是特殊的需求。
	 * 
	 * 	添加一个新顶点 T, 使用 Bellman-ford 算法生成以这个新顶点为起点的最短路径树
	 * 	最后返回一个 tempDist 数组，表示新顶点到其他各个顶点的最短距离
	 * 		如果我们在生成最短路径树的时候，发现原图存在负权环，那么直接抛异常！ 
	 * @param graph
	 * @return
	 */
	private Double[] getTempDistByBellmanFord(DirectedGraph<V> graph) {
		// 先获取 邻接矩阵
		Double[][] adjMatrix = graph.getAdjMatrix();
		// 因为我们需要添加一个新的顶点 T ，所以我们需要扩充原来的邻接矩阵
		// 行和列都要添加1
		int vertexCount = graph.getVertexCount();
		Double[][] newAdjMatrix = new Double[vertexCount + 1][vertexCount + 1];
		// 先遍历原来的二维数组，给新的二维数组赋值
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				newAdjMatrix[i][j] = adjMatrix[i][j];
			}
		}
		// 然后，我们让新顶点 T 到其他全部顶点，包括到自己顶点的距离初始化为 0.0
		// 其实就是最后一行，全部初始化成 0.0
		Arrays.fill(newAdjMatrix[vertexCount], 0.0);
		
		// 然后初始化最终要返回的 dist 数组
		Double[] dist = new Double[vertexCount + 1];
		// 初始值除了起点以外，全部初始化为正无穷大
		Arrays.fill(dist, Double.POSITIVE_INFINITY);
		// 起点初始化成 0.0
		dist[vertexCount] = 0.0;
		
		// 再然后，我们就可以开始 Bellman-Ford 算法的核心步骤
		// 为了提高效率，我们使用队列进行优化
		LinkedList<Integer> queue = new LinkedList<Integer>();
		// 为了防止重复添加到队列，我们再维护一个isInQueue 数组记录各个顶点状态
		boolean[] isInQueue = new boolean[vertexCount + 1];
		// 为了检查是否存在负权环，我们需要再维护一个一维数组 count ，统计各个顶点入队列的次数
		int[] count = new int[vertexCount + 1];
		
		// 首先，把起始顶点的索引放入队列中
		queue.add(vertexCount);
		isInQueue[vertexCount] = true;
		count[vertexCount]++;
		
		int currIndex = -1;
		
		// 只要队列不为空，我们就一直循环下去
		while(!queue.isEmpty()) {
			// 弹出一个顶点
			currIndex = queue.poll();
			isInQueue[currIndex] = false;
			
			// 遍历以这个顶点为起点的各条边，并尝试进行松驰操作
			for (int i = 0; i < newAdjMatrix[currIndex].length; i++) {
				if(newAdjMatrix[currIndex][i] != null && dist[i] > dist[currIndex] + newAdjMatrix[currIndex][i]) {
					// 如果可以进行松驰操作，我们就进行正常的松驰操作
					dist[i] = dist[currIndex] + newAdjMatrix[currIndex][i];
					// 然后，再看看这个 i 对应的顶点是否在队列中
					// 		如果不在队列中，我们就添加到队列中
					//      如果在队列中，我们就不重复添加了
					if(!isInQueue[i]) {
						queue.add(i);
						isInQueue[i] = true;
						count[i]++;
					}
					
					// 如果一个顶点，添加到队列中 vertextCount + 1 次的话，那么说明肯定存在负权环
					// 我们直接报异常就可以了
					if(count[i] >= newAdjMatrix.length) {
						throw new RuntimeException("存在负权环，无法使用 Johnson 算法求最短路径");
					}
				}
			}
		}
		
		// 如果没有报异常，我们最终会返回 dist 数组
		return dist;
	}
	
	/**
	 * 	确认两个顶点是否连通
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public boolean isConnected(V fromVertex, V toVertex) {
		// 先获取原图对象的 vertexs 集合
		ArrayList<V> vertexs = graph.getVertexs();
		// 然后，根据输入的两个顶点确认顶点的索引
		int fromIndex = vertexs.indexOf(fromVertex);
		int toIndex = vertexs.indexOf(toVertex);
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
			// 先获取原图对象的 vertexs 集合
			ArrayList<V> vertexs = graph.getVertexs();
			// 然后，根据输入的两个顶点确认顶点的索引
			int fromIndex = vertexs.indexOf(fromVertex);
			int toIndex = vertexs.indexOf(toVertex);
			Stack<V> stack = new Stack<V>();
			// 首先，我们需要把终点的索引添加到栈中
			stack.add(vertexs.get(toIndex));
			
			while(true) {
				if(preVertex[fromIndex][toIndex] != -1) {
					stack.add(vertexs.get(preVertex[fromIndex][toIndex]));
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
			// 先获取原图对象的 vertexs 集合
			ArrayList<V> vertexs = graph.getVertexs();
			// 然后，根据输入的两个顶点确认顶点的索引
			int fromIndex = vertexs.indexOf(fromVertex);
			int toIndex = vertexs.indexOf(toVertex);
			
			return dist[fromIndex][toIndex] - tempDist[fromIndex] + tempDist[toIndex];
		}else {
			throw new RuntimeException("这两个顶点并不连通，不存在最短路径");
		}
	}
}
