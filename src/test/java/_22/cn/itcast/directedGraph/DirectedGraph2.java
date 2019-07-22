package _22.cn.itcast.directedGraph;

import java.util.LinkedList;

/**
 * 	相比无向图的实现，主要的变动在于：
 * 	1、 边的权值类似由 int 改成 Double ，其实这个变动主要是为了统一邻接表实现的变动。
 * 	2、 addEdge 方法只能添加一条边，一个 edge 对象； 而无向图里面的 addEdge 方法，其实添加的其实是两条权值相同方向相反的边。
 * 	3、 无向图中，添加边的时候，我们没考虑到 顶点相同，值替换的问题。（这里通过 fromIndex 和 toIndex 判断一条边相同，边相同权值替换）
 * 
 * @author Administrator
 *
 * @param <V>
 */
public class DirectedGraph2<V> {
	// 这个 edgeCount 表示图中的边数量
	private int edgeCount;
	// 邻接表，是一个一维数组，元素的类型是每一个顶点
	private Vertex<V>[] adjTable;

	/**
	 * 根据 顶点的数据，我们初始化 邻接表
	 * 
	 * @param vertexCount
	 */
	public DirectedGraph2(V[] data) {
		super();
		if (data == null || data.length <= 1) {
			throw new RuntimeException("顶点的数量必须大于1个");
		}
		adjTable = new Vertex[data.length];
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				adjTable[i] = new Vertex<V>();
				adjTable[i].value = data[i];
			} else {
				throw new RuntimeException("顶点的数据不能有 null 值");
			}
		}
	}

	/**
	 * 返回邻接表
	 * 
	 * @return
	 */
	public Vertex<V>[] getAdjTable() {
		return adjTable;
	}

	/**
	 * 返回顶点的数量
	 * 
	 * @return
	 */
	public int getVertexCount() {
		return adjTable.length;
	}

	/**
	 * 返回边的数量
	 * 
	 * @return
	 */
	public int getEdgeCount() {
		return edgeCount;
	}

	/**
	 * 根据 value 在邻接表中查找对应的顶点的索引
	 * 
	 * @param value
	 * @return
	 */
	private int getIndexByValue(V value) {
		if (value == null) {
			throw new RuntimeException("value 不能为空");
		}
		for (int i = 0; i < adjTable.length; i++) {
			if (value.equals(adjTable[i].value)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 如果我们没有输入权值，默认权值为 1
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean addEdge(V from, V to) {
		return addEdge(from, to, 1.0);
	}

	/**
	 * 	
	 * @param from
	 * @param to
	 * @param weight
	 * @return
	 */
	public boolean addEdge(V from, V to, Double weight) {
		int fromIndex = getIndexByValue(from);
		int toIndex = getIndexByValue(to);
		return addEdge(fromIndex, toIndex, weight);
	}
	
	/**
	 * 添加一条边
	 * 
	 * @param fromIndex
	 * @param toIndex
	 * @param weight
	 * @return
	 */
	public boolean addEdge(int fromIndex, int toIndex, Double weight) {
		// 检查一下顶点的索引是否存在，如果不存在，我们直接扔异常
		if(fromIndex < 0 || fromIndex > adjTable.length - 1 || toIndex < 0 || toIndex > adjTable.length - 1 ) {
			throw new RuntimeException("顶点的索引不存在！");
		}
		// 检查一下 权值是不是 null ，如果是 null 的话，那我们直接报错
		if(weight == null) {
			throw new RuntimeException("权值不能为 null");
		}
		
		// 如果顶点的索引没有问题，那么我们就正常添加边，添加边之前，我们需要检查一下这条边是否已经存在
		// 如果存在，那么我们就更新权值，如果不存在，那么我们就添加边
		// 【注意】 边的 equals 方法，只根据 fromIndex 和 toIndex 来判断，不能根据权值
		Edge newEdge = new Edge(fromIndex, toIndex, weight);
		// 如果链表内已经有对应的边了，那么我们就使用新的边对象来替换
		if(adjTable[fromIndex].adjList.contains(newEdge)) {
			int index = adjTable[fromIndex].adjList.indexOf(newEdge);
			adjTable[fromIndex].adjList.set(index, newEdge);
		}else {
			// 如果新添加的边不存在，那么我们就正常添加即可
			adjTable[fromIndex].adjList.add(newEdge);
			edgeCount++;
		}
		
		// 因为我们没有什么限制，所以添加边肯定都是成功的
		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < adjTable.length; i++) {
			result.append("[ ").append(adjTable[i].value);
			if (!adjTable[i].adjList.isEmpty()) {
				for (Edge edge : adjTable[i].adjList) {
					result.append("->").append(adjTable[edge.toIndex].value).append("(").append(edge.weight)
							.append(")");
				}
			}
			result.append(" ]\n");
		}
		return result.toString();
	}

	@SuppressWarnings("hiding")
	public static class Vertex<V> {
		V value;
		// 这一次每个顶点内部的 链表，保存的不再是其他顶点的索引了，而是 edge 对象， 而edge 对象是保存权值的
		LinkedList<Edge> adjList = new LinkedList<>();

		public V getValue() {
			return value;
		}

		public LinkedList<Edge> getAdjList() {
			return adjList;
		}

		@Override
		public String toString() {
			return "Vertex [value=" + value + "]";
		}
	}

	public static class Edge implements Comparable<Edge> {
		int fromIndex;
		int toIndex;
		Double weight;

		public int getFromIndex() {
			return fromIndex;
		}

		public int getToIndex() {
			return toIndex;
		}

		public double getWeight() {
			return weight;
		}

		public Edge(int fromIndex, int toIndex, Double weight) {
			super();
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
			this.weight = weight;
		}
		
		// 主要是为了优先级队列对边排序，只根据权重排序
		@Override
		public int compareTo(Edge other) {
			return this.weight.compareTo(other.weight);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + fromIndex;
			result = prime * result + toIndex;
			return result;
		}

		// 主要是为了添加边时，检查是否已经存在相同的边
		// indexOf 方法依赖于 equals 方法，我们需要重写这个方法
		// 【注意】 只检查 起点索引 和 终点索引是否相同， 权值不考虑
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Edge other = (Edge) obj;
			if (fromIndex != other.fromIndex)
				return false;
			if (toIndex != other.toIndex)
				return false;
			return true;
		}
	}
}
