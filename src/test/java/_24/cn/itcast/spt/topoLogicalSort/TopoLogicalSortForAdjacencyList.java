package _24.cn.itcast.spt.topoLogicalSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import _22.cn.itcast.directedGraph.DirectedGraph2;
import _22.cn.itcast.directedGraph.DirectedGraph2.Edge;
import _22.cn.itcast.directedGraph.DirectedGraph2.Vertex;

public class TopoLogicalSortForAdjacencyList<V> {
	boolean[] visited;
	int[] indegree;
	DirectedGraph2<V> graph;
	
	/**
	 * 	构造函数，传入有向图对象
	 * @param graph
	 */
	public TopoLogicalSortForAdjacencyList(DirectedGraph2<V> graph) {
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
	public List<V> topoLogicalSort(){
		// 首先，我们需要创建返回的 List 对象
		ArrayList<V> list = new ArrayList<V>();
		// 然后，我们需要遍历有向图全部的边，初始化 indegree 数组
		initIndegree();
		// 初始化 visited 数组
		initVisited();
		Vertex<V>[] adjTable = graph.getAdjTable();
		
		// 先确认总的循环 n 次
		for (int i = 0; i < graph.getVertexCount(); i++) {
			// 每次循环都是查 indegree 数组中 入度为 0 ， 并且还没有被访问的顶点
			for (int j = 0; j < indegree.length; j++) {
				if(indegree[j] == 0  && !visited[j]) {
					// 找到目标顶点，我们先标记成 已访问
					visited[j] = true;
					// 然后添加这个顶点的值到返回的集合中
					list.add(adjTable[j].getValue());
					
					// 然后，让这个顶点指向的其他顶点的 入度都减1
					for (Edge edge : adjTable[j].getAdjList()) {
						// 不管怎么样，先维护 indegree 数组
						indegree[edge.getToIndex()]--;
					}
				}
			}
		}
		
		// 前面循环处理结束以后，我们再遍历一下 visited 数组，如果发现还有 false 的，说明还是存在环，直接抛异常
//		for (int i = 0; i < visited.length; i++) {
//			if(!visited[i]) {
//				throw new RuntimeException("此有向图存在环，不可进行拓朴排序");
//			}
//		}
		if(list.size() < graph.getVertexCount()) {
			throw new RuntimeException("此有向图存在环，不可进行拓朴排序");
		}
		return list;
	}
	
	/**
	 * 	初始化 visited 数组
	 */
	void initVisited() {
		Arrays.fill(visited, false);
	}

	/**
	 * 	初始化 indegree 数组
	 */
	void initIndegree() {
		// 担心多次执行，我们先把 indegree 数组的全部元素值都初始化成 0
		Arrays.fill(indegree, 0);
		Vertex<V>[] adjTable = graph.getAdjTable();
		LinkedList<Edge> adjList = null;
		for (int i = 0; i < adjTable.length; i++) {
			adjList = adjTable[i].getAdjList();
			for (Edge edge : adjList) {
				indegree[edge.getToIndex()]++;
			}
		}
	}
}
