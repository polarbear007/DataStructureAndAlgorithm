package _23.cn.itcast.spt.bellmanFord;

import java.util.LinkedList;

import _22.cn.itcast.directedGraph.DirectedGraph;

/**
 * 	邻接矩阵优化版
 * @author Administrator
 *
 * @param <V>
 */
public class BellmanFordForAdjacencyMatrixWithQueue<V> extends BellmanFordForAdjacencyMatrix<V>{
	/**
	 * 	“重写”一下父类的构造方法
	 * @param graph
	 * @param source
	 */
	public BellmanFordForAdjacencyMatrixWithQueue(DirectedGraph<V> graph, V source) {
		super(graph, source);
	}

	/**
	 * 	重写一下父类的 buildSPT 方法
	 */
	@Override
	void bulidSPT() {
		// 首先，获取邻接矩阵
		Double[][] adjMatrix = graph.getAdjMatrix();
		// 然后，我们需要创建一个普通的队列（先入先出就可以了）
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(sourceIndex);
		
		// 再然后，我们还需要维护一个一维数组，表示顶点是否在队列中，防止重复添加顶点到队列中
		// 【说明】 其实我们不维护也可以，但是每次都通过 queue.contains() 方法判断的话，效率太低
		boolean[] isInQueue = new boolean[graph.getVertexCount()];
		isInQueue[sourceIndex] = true;
		
		// 再然后，我们还需要维护一个一维数组，统计每个顶点进入队列的次数
		int[] count = new int[graph.getVertexCount()];
		count[sourceIndex]++;
		
		Integer currIndex = 0;
		
		// 如果队列不为空，那么我们就一直循环
		while(!queue.isEmpty()) {
			currIndex = queue.poll();
			isInQueue[currIndex] = false;
			
			// 根据 currIndex 遍历以这个顶点的起点的全部边，然后尝试进行 松驰 操作
			for (int i = 0; i < adjMatrix[currIndex].length; i++) {
				if(adjMatrix[currIndex][i] != null && dist[i] > dist[currIndex] + adjMatrix[currIndex][i]) {
					// 先正常松驰
					dist[i] = dist[currIndex] + adjMatrix[currIndex][i];
					preVertex[i] = currIndex;
					
					// 如果在队列中，或者添加到队列的次数已经大于或者等于 n 次，我们直接不处理
					if(!isInQueue[i] && count[i] < adjMatrix[currIndex].length) {
						queue.add(i);
						isInQueue[i] = true;
						count[i]++;
					}
					
					// 再然后，我们再来判断一下这个顶点添加到队列的次数， 如果已经大于或者等于  n 次
					// 那么说明存在负权环，而且 i 对应的顶点就是负权环上的一个顶点 
					if(count[i] >= adjMatrix[currIndex].length) {
						dist[i] = Double.NEGATIVE_INFINITY;
						isContainingNegativeCircle = true;
					}
				}
			}
		}
	}
}
