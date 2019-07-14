package _16.cn.itcast.undirectedGraph;

import java.util.LinkedList;

/**
 * 	为了能够保存边的权值，我们打算再引入一个 Edge 类，这个类里面保存着 两个顶点的索引，还有边的权值
 * @author Administrator
 */
public class UndirectedGraph3<V> {
	// 这个 edgeCount 表示图中的边数量
	private int edgeCount;
	// 邻接表，是一个一维数组，元素的类型是每一个顶点
	private Vertex<V>[] adjTable;
	
	/**
	 * 	根据 顶点的数据，我们初始化 邻接表
	 * @param vertexCount
	 */
	public UndirectedGraph3(V[] data) {
		super();
		if(data == null || data.length <= 1) {
			throw new RuntimeException("顶点的数量必须大于1个");
		}
		adjTable = new Vertex[data.length];
		for (int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				adjTable[i] = new Vertex<V>();
				adjTable[i].value = data[i];
			}else {
				throw new RuntimeException("顶点的数据不能有 null 值");
			}
		}
	}
	
	/**
	 * 	返回顶点的数量
	 * @return
	 */
	public int getVertexCount() {
		return adjTable.length;
	}

	/**
	 * 	返回边的数量
	 * @return
	 */
	public int getEdgeCount() {
		return edgeCount;
	}
	
	/**
	 * 	根据 value 在邻接表中查找对应的顶点的索引
	 * @param value
	 * @return
	 */
	private int getIndexByValue(V value) {
		if(value == null) {
			throw new RuntimeException("value 不能为空");
		}
		for (int i = 0; i < adjTable.length; i++) {
			if(value.equals(adjTable[i].value)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 	如果我们没有输入权值，默认权值为 1
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean addEdge(V from, V to) {
		return addEdge(from, to, 1);
	}
	
	/**
	 * 	添加一条边
	 * @param from
	 * @param to
	 * @param weight
	 * @return
	 */
	public boolean addEdge(V from, V to, int weight) {
		if(weight < 1) {
			throw new RuntimeException("权值不能小于1");
		}
		int fromIndex = getIndexByValue(from);
		int toIndex = getIndexByValue(to);
		// 如果索引值为 -1 , 或者两个顶点的索引值相同，我们都不处理直接返回 false
		if(fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
			// 这里我们根据 fromIndex 和  toIndex 创建两个 Edge 对象
			Edge edge1 = new Edge(fromIndex, toIndex, weight);
			Edge edge2 = new Edge(toIndex, fromIndex, weight);
			adjTable[fromIndex].adjList.add(edge1);
			adjTable[toIndex].adjList.add(edge2);
			// 添加以后，我们维护一下 edgeCount 
			edgeCount++;
			return true;
		}
		// 如果不满足前面的条件，我们直接返回 false
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < adjTable.length; i++) {
			result.append("[ ").append(adjTable[i].value);
			if(!adjTable[i].adjList.isEmpty()) {
				for (Edge edge : adjTable[i].adjList) {
					result.append("->").append(adjTable[edge.toIndex].value).append("(").append(edge.weight).append(")");
				}
				result.append(" ]\n");
			}
		}
		return result.toString();
	}
	
	@SuppressWarnings("hiding")
	class Vertex<V>{
		V value;
		// 这一次每个顶点内部的 链表，保存的不再是其他顶点的索引了，而是  edge 对象， 而edge 对象是保存权值的
		LinkedList<Edge> adjList = new LinkedList<>();
		@Override
		public String toString() {
			return "Vertex [value=" + value + "]";
		}
	}
	
	class Edge{
		int fromIndex;
		int toIndex;
		int weight;
		public Edge(int fromIndex, int toIndex, int weight) {
			super();
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
			this.weight = weight;
		}
	}
}
