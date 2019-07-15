package _16.cn.itcast.undirectedGraph;

import java.util.LinkedList;

/**
 * 	使用邻接表来表示图，每个顶点类里面的 LinkdedList 集合保存的是连接的其他顶点的索引
 * 	这种结构跟使用邻接队列来表示图的方式非常像，外层同样是一个数组，内部不再是一个数组，而是使用链表来表示
 * 	只记录与本顶点相邻的所有顶点索引，不相邻的顶点，我们不关心
 * 
 * 	优点： 相比 邻接阵列， 减少了很多冗余
 * 	缺点： 但是因为 LinkedList 里面只保存其他顶点的索引，我们无法父邻接阵列那样保存路径的权值。
 * @author Administrator
 *
 * @param <V>
 */
public class UndirectedGraph2<V> {
	// 这个 edgeCount 表示图中的边数量
	private int edgeCount;
	// 邻接表，是一个一维数组，元素的类型是每一个顶点
	private Vertex<V>[] adjTable;

	/**
	 * 	根据 顶点的数据，我们初始化 邻接表
	 * @param vertexCount
	 */
	public UndirectedGraph2(V[] data) {
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
	 * 	返回全部顶点的数组
	 * @return
	 */
	public Vertex<V>[] getVertexs(){
		return adjTable;
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
	 * 	添加一条边
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean addEdge(V from, V to) {
		int fromIndex = getIndexByValue(from);
		int toIndex = getIndexByValue(to);
		// 如果索引值为 -1 , 或者两个顶点的索引值相同，我们都不处理直接返回 false
		if(fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
			// 因为是无向图，所以其实 from 和 to 并没有先后关系，我们添加一条边，其实是在两个顶点的 adjList 都添加边
			adjTable[fromIndex].adjList.add(toIndex);
			adjTable[toIndex].adjList.add(fromIndex);
			// 添加以后，我们维护一下 edgeCount 
			edgeCount++;
			return true;
		}
		// 如果不满足前面的条件，我们直接返回 false
		return false;
	}
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < adjTable.length; i++) {
			result += "[ " + adjTable[i].value;
			if(!adjTable[i].adjList.isEmpty()) {
				for (Integer vertexIndex : adjTable[i].adjList) {
					result += "->" + adjTable[vertexIndex].value;
				}
				result += " ]\n";
			}
		}
		return result;
	}

	/**
	 * 	顶点类
	 * 	可以传入一个 value 泛型,表示这个结点携带的数据
	 * 
	 * 	每个顶点可能会连接到多个其他的顶点，所以我们又设置了一个 adjList 属性，这是一个链表
	 * 	当这个顶点指向了另一个顶点，我们就在链表中添加另一个顶点在 邻接表（数组）中的索引值
	 * @author Administrator
	 *
	 * @param <V>
	 */
	@SuppressWarnings("hiding")
	class Vertex<V>{
		V value;
		LinkedList<Integer> adjList = new LinkedList<>();
		@Override
		public String toString() {
			return "Vertex [value=" + value + "]";
		}
		
		public LinkedList<Integer> getAdjList(){
			return adjList;
		}
	}
}
