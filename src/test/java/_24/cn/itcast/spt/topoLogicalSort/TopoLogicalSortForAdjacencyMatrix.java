package _24.cn.itcast.spt.topoLogicalSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import _22.cn.itcast.directedGraph.DirectedGraph;

public class TopoLogicalSortForAdjacencyMatrix<V> {
	private boolean[] visited;
	private int[] indegree;
	private DirectedGraph<V> graph;
	
	/**
	 * 	构造函数，传入有向图对象
	 * @param graph
	 */
	public TopoLogicalSortForAdjacencyMatrix(DirectedGraph<V> graph) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为空");
		}
		
		this.graph = graph;
		this.visited = new boolean[graph.getVertexCount()];
		this.indegree = new int[graph.getVertexCount()];
	}
	
	/**
	 * 	对有向图进行拓朴排序，如果发现有环，直接扔异常
	 * @return
	 */
	public List<V> topoLogicSort(){
		// 首先，我们需要创建返回的 List 对象
		ArrayList<V> list = new ArrayList<V>();
		// 然后，我们需要遍历有向图全部的边，初始化 indegree 数组
		initIndegree();
		// 初始化 visited 数组
		initVisited();
		
		Double[][] adjMatrix = graph.getAdjMatrix();
		ArrayList<V> vertexs = graph.getVertexs();
		// 再然后，我们需要遍历 indegree 数组最多 n 次，找到 n 个度为 0 的元素
		for (int i = 0; i < graph.getVertexCount(); i++) {
			for (int j = 0; j < indegree.length; j++) {
				// 找到一个 入度为 0 ,并且还没有访问过的顶点以后，我们就先标记这个顶点为已访问，然后通过邻接矩阵
				// 找到全部以这个顶点为起点的边所指向的其他顶点，并让这些顶点有 indegree -- 
				if(indegree[j] == 0 && !visited[j]) {
					visited[j] = true;
					list.add(vertexs.get(j));
					for (int k = 0; k < adjMatrix[j].length; k++) {
						if(adjMatrix[j][k] != null) {
							// 其实我们可以在这里检查 indegree[k] 是否小于 0，如果小于 0 说明肯定存在环，无法进行拓朴排序
							// 【说明】 如果我们拓朴排序的目的就是查检是否存在有向环，那么这里可以直接返回
							if(--indegree[k] < 0) {
								throw new RuntimeException("此有向图存在环，不可进行拓朴排序");
							}
							
						}
					}
				}
			}
		}
		
		// 前面循环处理结束以后，我们再遍历一下 visited 数组，如果发现还有 false 的，说明还是存在环，直接抛异常
		for (int i = 0; i < visited.length; i++) {
			if(!visited[i]) {
				throw new RuntimeException("此有向图存在环，不可进行拓朴排序");
			}
		}
		return list;
	}
	
	/**
	 * 	初始化 visited 数组
	 */
	private void initVisited() {
		Arrays.fill(visited, false);
	}

	/**
	 * 	初始化 indegree 数组
	 */
	private void initIndegree() {
		// 担心多次执行，我们先把 indegree 数组的全部元素值都初始化成 0
		Arrays.fill(indegree, 0);
		Double[][] adjMatrix = graph.getAdjMatrix();
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				// 如果找到一条边，指向了顶点 j ，那么我们就让  indegree[j]++
				if(adjMatrix[i][j] != null) {
					indegree[j]++;
				}
			}
		}
	}
}
