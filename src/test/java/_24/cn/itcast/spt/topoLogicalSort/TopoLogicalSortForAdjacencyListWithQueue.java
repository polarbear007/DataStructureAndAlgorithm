package _24.cn.itcast.spt.topoLogicalSort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import _22.cn.itcast.directedGraph.DirectedGraph2;
import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;

public class TopoLogicalSortForAdjacencyListWithQueue<V> extends TopoLogicalSortForAdjacencyList<V> {

	public TopoLogicalSortForAdjacencyListWithQueue(DirectedGraph2<V> graph) {
		super(graph);
	}

	/**
	 * 重写父类的 拓朴排序方法
	 */
	@Override
	public List<V> topoLogicSort() {
		// 首先，我们需要创建返回的 List 对象
		ArrayList<V> list = new ArrayList<V>();
		// 然后，我们需要遍历有向图全部的边，初始化 indegree 数组
		initIndegree();
		// 初始化 visited 数组
		initVisited();
		Vertex<V>[] adjTable = graph.getAdjTable();
		
		// 创建一个队列
		LinkedList<Integer> queue = new LinkedList<Integer>();
		// 创建一个一维数组 isInQueue ，记录一个顶点是否在队列中
		boolean[] isInQueue = new boolean[graph.getVertexCount()];
		
		// 把所有入度为0 的顶点索引全部添加到队列中
		for (int i = 0; i < indegree.length; i++) {
			if(indegree[i] == 0) {
				queue.add(i);
				isInQueue[i] = true;
			}
		}
		
		Integer currIndex = -1;
		LinkedList<Edge> adjList = null;
		int toIndex = -1;
		
		while(!queue.isEmpty()) {
			currIndex = queue.poll();
			// 维护 isInQueue 数组
			isInQueue[currIndex] = false;
			// 把这个顶点设置成已访问 
			visited[currIndex] = true;
			// 把这个顶点的值添加到 list 集合中
			list.add(adjTable[currIndex].getValue());
			
			// 再然后，我们去获取这个顶点相邻的顶点
			adjList = adjTable[currIndex].getAdjList();
			for (Edge edge : adjList) {
				toIndex = edge.getToIndex();
				// 不管怎么样，我们先维护 indegree 
				indegree[toIndex]--;
				// 如果相邻的顶点入度为 0 ,并且不在队列中，那么我们就 添加到队列
				if(indegree[toIndex] == 0 && !isInQueue[toIndex]) {
					queue.add(toIndex);
				}
			}
		}
		
		
		if(list.size() < adjTable.length) {
			throw new RuntimeException("此有向图存在环，不可进行拓朴排序");
		}
		return list;
	}
}
