package _20.cn.itcast.mst.kruskalMST;

import java.util.LinkedList;
import java.util.PriorityQueue;

import _16.cn.itcast.undirectedGraph.UndirectedGraph3;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3.Edge;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3.Vertex;

/**
 * 	使用 kruskal 算法，从 使用邻接表 的图中寻找最小生成树
 * @author Administrator
 *
 * @param <V>
 */
public class KruskalMST2<V> {
	// 这个主要是在确认一个图是不是连通图，使用深度优先遍历需要维护的一维数组
	private boolean[] marked;
	private UndirectedGraph3<V> graph;
	
	public KruskalMST2(UndirectedGraph3<V> graph) {
		if(graph == null) {
			throw new RuntimeException("图对象不能为null");
		}
		// 初始化内部的 图对象
		this.graph = graph;
		// 判断是否是连通图，如果不是，我们也不去找什么最小生成树了
		if(!isConnectedGraph()) {
			throw new RuntimeException("此图不是连通图，无法找到最小生成树！");
		}
	}
	
	/**
	 * 	判断一个图是否是连通图
	 * @return
	 */
	private boolean isConnectedGraph() {
		// 根据图对象，初始化记录顶点状态的marked 数组
		marked = new boolean[graph.getVertexCount()];
		// 然后从 0 索引对应的顶点开始深度优先遍历
		dfsFromIndex(0);
		// 确认 marked 数组里面是否全部顶点都被标记
		//		如果全部标记成 true ，则认为是连通图
		//      如果没有全部标记true，则认为是非连通图
		for (int i = 0; i < marked.length; i++) {
			// 只要找到一个 false ，我们直接返回 false
			if(!marked[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 	从某个顶点出发，深度优先遍历图。不打印顶点数据，只维护 marked 数组，记录与目标顶点连通的顶点
	 * @param vertexIndex
	 */
	private void dfsFromIndex(int vertexIndex) {
		if(!marked[vertexIndex]) {
			// 首先，把起点顶点标记为 true
			marked[vertexIndex] = true;
			
			// 拿到当前顶点的 邻接链表
			LinkedList<Edge> adjList = graph.getAdjTable()[vertexIndex].getAdjList();
			if(!adjList.isEmpty()) {
				for (Edge edge : adjList) {
					if(!marked[edge.getToIndex()]) {
						dfsFromIndex(edge.getToIndex());
					}
				}
			}
		}
	}
	
	/**
	 * 	获取并返回最小生成树
	 * 	【说明】 使用邻接表实现的图，因为有封装  Edge 对象，所以我们可以使用优先级队列来对全部的边进行排序
	 * 			这样子我们每次获取权值最小的边，只需要弹出队列中最小的元素即可。（比每次遍历二维数组要快很多很多）
	 * @return
	 */
	public UndirectedGraph3<V> getMST(){
		// 首先，先获取原图的邻接表
		Vertex<V>[] adjTable = graph.getAdjTable();
		// 根据原图邻接表创建一个包含原图全部顶点、没有边的图
		V[] data = (V[])new Object[adjTable.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = adjTable[i].getValue();
		}
		UndirectedGraph3<V> graph2 = new UndirectedGraph3<>(data);
		
		// 然后，我们同样需要根据顶点的数量，来维护一个并查集
		UnionFind uf = new UnionFind(graph.getVertexCount());
		
		// 再然后，我们可以把全部的边，都添加到一个优先级队列中（记得是使用最小堆实现的优先级队列才可以）
		PriorityQueue<Edge> queue = new PriorityQueue<Edge>();
		for (Vertex<V> vertex : adjTable) {
			// 虽然无向图，边的数量会有冗余，但是没有关系
			queue.addAll(vertex.getAdjList());
		}
		
		// 再接着就是从优先级队列中不断弹出权值最小的边
		Edge minEdge = null;
		int fromIndex = -1;
		int toIndex = -1;
		int edgeCount = 0;
		// 最小生成数的边数为顶点个数 -1 ，所以最终我们只需要找到 graph.getVertexCount() - 1 边就可以了
		while(edgeCount < graph.getVertexCount() - 1) {
			minEdge = queue.poll();
			fromIndex = minEdge.getFromIndex();
			toIndex = minEdge.getToIndex();
			// 我们通过并查集来看一下这条权值最小的边的两个顶点是否在同一个集合
			// 以此判断添加这条边是否会形成回路（重复的边也会被认为形成回路而丢弃）
			if(uf.isConnected(fromIndex, toIndex)) {
				continue;
			}
			
			// 如果能走到这里，说明这条边肯定是最小生成树的边，那么我们就把这条边添加到 graph2
			graph2.addEdge(adjTable[fromIndex].getValue(), adjTable[toIndex].getValue(), minEdge.getWeight());
			// 我们需要同步维护并查集，让 fromIndex 和 toIndex 所在的集合合并成一个集合
			uf.union(fromIndex, toIndex);
			edgeCount++;
		}
		
		return graph2;
	}
}
