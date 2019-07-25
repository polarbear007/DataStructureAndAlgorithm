package _24.cn.itcast.spt.topoLogicalSort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import _22.cn.itcast.directedGraph.DirectedGraph;

public class TopoLogicalSortForAdjacencyMatrixWithQueue<V> extends TopoLogicalSortForAdjacencyMatrix<V> {

	public TopoLogicalSortForAdjacencyMatrixWithQueue(DirectedGraph<V> graph) {
		super(graph);
	}

	/**
	 * 重写一下父类的 topoLogicSort 方法
	 */
	@Override
	public List<V> topoLogicSort() {
		// 首先，我们需要创建返回的 List 对象
		ArrayList<V> list = new ArrayList<V>();
		// 然后，我们需要遍历有向图全部的边，初始化 indegree 数组
		initIndegree();
		// 初始化 visited 数组
		initVisited();

		Double[][] adjMatrix = graph.getAdjMatrix();
		ArrayList<V> vertexs = graph.getVertexs();
		
		// 我们再维护一个队列，来优化排序的过程
		LinkedList<Integer> queue = new LinkedList<>();
		// 为了防止一个顶点重复入队列，我们再维护一个  isInQueue 数组
		boolean[] isInQueue = new boolean[graph.getVertexCount()];
		
		// 首先，我们需要遍历 indegree 数组，找到全部入度为 0 的顶点，全部添加到队列中
		for (int i = 0; i < indegree.length; i++) {
			if(indegree[i] == 0) {
				queue.add(i);
				isInQueue[i] = true;
			}
		}
		
		Integer currIndex = -1;
		// 再然后，就是不断地从队列中弹出顶点，并处理的过程
		while(!queue.isEmpty()) {
			currIndex = queue.poll();
			// 首先，我们把这个顶点标记成不在队列中
			isInQueue[currIndex] = false;
			// 然后，我们需要把这个顶点标记成已访问 
			visited[currIndex] = true;
			// 再然后，我们需要把这个顶点添加到 list 集合中
			list.add(vertexs.get(currIndex));
			
			// 再然后，我们需要根据 邻接矩阵，维护邻接顶点的入度
			for (int i = 0; i < adjMatrix[currIndex].length; i++) {
				// 我们只处理 未访问过的顶点
				if(!visited[i] && adjMatrix[currIndex][i] != null) {
					// 如果我们发现一个顶点的度已经降到小于 0  ，那么说明这个图肯定有环
					if(--indegree[i] < 0) {
						throw new RuntimeException("此图含有环，无法进行拓朴排序");
					}
					// 是否放进队列有两个条件： 
					//  1、 没有在队列中，我们不重复添加
					//  2、 入度为 0 ;
					if(indegree[i] == 0 && !isInQueue[i]) {
						queue.add(i);
					}
				}
			}
		}

		// 前面循环处理结束以后，我们再遍历一下 visited 数组，如果发现还有 false 的，说明还是存在环，直接抛异常
		if(list.size() < graph.getVertexCount()) {
			throw new RuntimeException("此图含有环，无法进行拓朴排序");
		}
		return list;
	}
}
