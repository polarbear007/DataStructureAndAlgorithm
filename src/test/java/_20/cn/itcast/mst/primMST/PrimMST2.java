package _20.cn.itcast.mst.primMST;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import _16.cn.itcast.undirectedGraph.UndirectedGraph3;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3.Edge;
import _16.cn.itcast.undirectedGraph.UndirectedGraph3.Vertex;

/**
 * 	使用 prim 算法，从 使用 邻接表 实现的图中，找最小生成树
 * @author Administrator
 *
 */
public class PrimMST2<V> {
	// 这个主要是在确认一个图是不是连通图，使用深度优先遍历需要维护的一维数组
	private boolean[] marked;
	private UndirectedGraph3<V> graph;
	
	public PrimMST2(UndirectedGraph3<V> graph) {
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
	 * 	根据原图，返回最小生成树
	 * 	【说明】 这次我们使用邻接表来表示图，所以封装了 Edge 对象
	 * 			因此，我们可以可以使用优先级队列来优化排序的速度。
	 * 			如果我们的顶点有很多的话，比使用邻接矩阵的实现，速度要快很多
	 * 	【说明】 虽然我们这里说的是无向图，但是其实我们的边对象是有严格区分 fromIndex 和 toIndex 的
	 * 			所以我们在判断一条边是不是横切边的时候，我们只需要看看 toIndex 对应的顶点是不是在 selected 集合即可
	 * @return
	 */
	public UndirectedGraph3<V> getMST() {
		// 首先，先获取原图的邻接表
		Vertex<V>[] adjTable = graph.getAdjTable();
		// 根据原图邻接表创建一个包含原图全部顶点、没有边的图
		V[] data = (V[])new Object[adjTable.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = adjTable[i].getValue();
		}
		UndirectedGraph3<V> graph2 = new UndirectedGraph3<>(data);
		
		// 然后，我们需要再创建一个集合，用来装切分以后的顶点（切分会把原图的顶点分成两部分，我们只保存其中一部分就可以）
		ArrayList<Integer> selected = new ArrayList<>();
		// 随便挑一个顶点，进行切分
		selected.add(0);
		// 因为使用邻接表实现的图，有封装  edge 对象，所以我们可以使用一个优先级队列来优化比较的效率
		PriorityQueue<Edge> queue = new PriorityQueue<Edge>();
		// 把挑选的顶点的全部边都添加到优先队列中
		queue.addAll(adjTable[0].getAdjList());
		Edge minEdge = null;
		int fromIndex = -1;
		int toIndex = -1;
		
		while(selected.size() < adjTable.length) {
			// 获取队列第一个元素（其实就是堆顶元素，默认是最小值）
			minEdge = queue.poll();
			// 如果队列弹出一个 null ，说明队列空了，也就是说没有任何有效边了，最小生成树已经构建完毕了
			if(minEdge == null) {
				break;
			}
			// 我们需要判断一下弹出的边是不是横切边（只需要看 toIndex 有没有在 selected 中就可以了）
			// 如果 minEdge 不是横切边，我们直接再弹一条出来
			if(selected.contains(minEdge.getToIndex())) {
				continue;
			}else {
				// 如果 minEdge 是横切边，那么我们就可以确定这条边是最小生成树的一条边了
				fromIndex = minEdge.getFromIndex();
				toIndex = minEdge.getToIndex();
				// 给最小生成树添加一条边
				graph2.addEdge(adjTable[fromIndex].getValue(), adjTable[toIndex].getValue(), minEdge.getWeight());
				// 然后维护selected 集合
				selected.add(toIndex);
				// 再维护优先级队列
				// 我们只添加 toIndex 不在 selected 集合中的边进优先级队列
				for (Edge edge : adjTable[toIndex].getAdjList()) {
					if(!selected.contains(edge.getToIndex())) {
						queue.add(edge);
					}
				}
			}
		}
		
		// 再然后，我们需要创建一个
		return graph2;
	}
	
}
