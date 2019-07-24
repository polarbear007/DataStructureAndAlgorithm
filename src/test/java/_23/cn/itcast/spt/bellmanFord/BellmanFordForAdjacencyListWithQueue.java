package _23.cn.itcast.spt.bellmanFord;

import java.util.LinkedList;

import _22.cn.itcast.directedGraph.DirectedGraph2;
import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;

public class BellmanFordForAdjacencyListWithQueue<V> extends BellmanFordForAdjacencyList<V> {
	/**
	 * 	带参构造必须跟父类一样
	 * @param graph
	 * @param source
	 */
	public BellmanFordForAdjacencyListWithQueue(DirectedGraph2<V> graph, V source) {
		super(graph, source);
	}
	
	/**
	 * 	重写一下父类的 buildSPT方法
	 */
	@Override
	void bulidSPT() {
		// 首先，获取邻接表
		Vertex<V>[] adjTable = graph.getAdjTable();
		// 然后，我们需要创建一个队列，并添加起始顶点
		LinkedList<Integer> queue = new LinkedList<>();
		queue.add(sourceIndex);
		
		// 然后创建一个 isInQueue 数组，记录顶点是否保存在队列中，防止重复入队列
		boolean[] isInQueue = new boolean[graph.getVertexCount()];
		isInQueue[sourceIndex] = true;
		
		// 再然后，创建一个 count 数组，用来统计一个顶点进入队列几次
		// 主要是为了检查负权环； 
		// 我们认为一个顶点进入队列 n 次，那么说明这个图中有负权环，并且这个顶点就是负权环上的一个顶点
		int[] count = new int[graph.getVertexCount()];
		count[sourceIndex]++;
		
		int n = adjTable.length;
		Integer currIndex = -1;
		int fromIndex = -1;
		int toIndex = -1;
		
		while(!queue.isEmpty()) {
			currIndex = queue.poll();
			isInQueue[currIndex] = false;
			
			// 根据弹出的顶点，我们找到以这个顶点为起点的全部边，尝试进行 “松驰”操作
			for (Edge edge : adjTable[currIndex].getAdjList()) {
				fromIndex = edge.getFromIndex();
				toIndex = edge.getToIndex();
				// 如果这条边可以进行松驰操作，那么我们就先进行正常松驰
				if(dist[toIndex] > dist[fromIndex] + edge.getWeight()) {
					//正常松驰
					dist[toIndex] = dist[fromIndex] + edge.getWeight();
					preVertex[toIndex] = fromIndex;
					
					// 不在队列中，并且进入队列不超过 n 次，我们才进行入队处理
					if(!isInQueue[toIndex] && count[toIndex] < n) {
						queue.add(toIndex);
						isInQueue[toIndex] = true;
						count[toIndex]++;
					}
					
					// 不管前面怎么处理，如果这个顶点已经入队列8次了，那么我们需要把这个顶点的 dist 值
					// 设置成   负无穷大，并且把这个图标记成有负环的图
					if(count[toIndex] >= n) {
						dist[toIndex] = Double.NEGATIVE_INFINITY;
						isContainingNegativeCircle = true;
					}
				}
			}
		}
	}

}
