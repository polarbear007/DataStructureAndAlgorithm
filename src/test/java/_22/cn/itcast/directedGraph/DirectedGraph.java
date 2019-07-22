package _22.cn.itcast.directedGraph;

import java.util.ArrayList;

/**
 * 	相比无向图的矩阵实现，此实现的主要改动有：
 * 	1、 邻接矩阵的类型由原来的  int[][] 变成 Double[][]
 * 	2、 原来我们使用 0 表示边不存在，现在因为我们想要支持负权值，0权值，所以就不能再使用 0 表示不存在的边
 * 		这里我们改用 null 表示边不存在
 * 	3、 addEdge 方法原来执行一次，会同步更新二维数组的两个元素，而在有向图中，我们只会更新其中的一个位置。
 * @author Administrator
 *
 * @param <V>
 */
public class DirectedGraph<V> {
	// 保存每个顶点对应的数据
	private ArrayList<V> vertexs = new ArrayList<>();
	// 这个 edgeCount 表示图中的边数量
	private int edgeCount;
	// 使用一个二维数组（矩阵） 来表示顶点之间的关系（其实也可以说是描述边）
	// 这个矩阵里面会显示路径的权值，这里规定一下：
	// 	权值为 null ，说明这条边不存在
	private Double[][] adjMatrix;

	// 返回邻接矩阵
	public Double[][] getAdjMatrix() {
		return adjMatrix;
	}

	// 返回顶点的集合
	public ArrayList<V> getVertexs() {
		return vertexs;
	}

	// 根据顶点的数据，我们初始化 vertexs 集合 和 adjMatrix 矩阵
	public DirectedGraph(V[] data) {
		super();
		if (data == null || data.length <= 1) {
			throw new RuntimeException("顶点的数量不能小于1");
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				vertexs.add(data[i]);
			} else {
				throw new RuntimeException("顶点的数据不能为null");
			}
		}
		// 添加完顶点以后，我们得根据顶点的数量初始化 adjMatrix 矩阵
		adjMatrix = new Double[data.length][data.length];
	}

	/**
	 * 返回顶点的数量
	 * 
	 * @return
	 */
	public int getVertexCount() {
		return vertexs.size();
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
	 * 添加一条边，不带权值（如果我们不设置权值的话，权值默认是1）
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean addEdge(V from, V to) {
		return addEdge(from, to, 1.0);
	}

	/**
	 * 	重载一下 addEdge 方法，方便我们直接输入顶点值
	 * @param from
	 * @param to
	 * @param weight
	 * @return
	 */
	public boolean addEdge(V from, V to, Double weight) {
		int fromIndex = vertexs.indexOf(from);
		int toIndex = vertexs.indexOf(to);
		return addEdge(fromIndex, toIndex, weight);
	}
	
	/**
	 * 添加一条边（附带权值）
	 * 
	 * @param from
	 * @param to
	 * @param weight
	 * @return
	 */
	public boolean addEdge(int fromIndex, int toIndex, Double weight) {
		if(fromIndex < 0 || toIndex < 0 || fromIndex > adjMatrix.length - 1 || toIndex > adjMatrix.length - 1) {
			throw new RuntimeException("顶点不存在！");
		}
		
		if(weight == null) {
			throw new RuntimeException("权值不能为null");
		}
		
		// 如果原来不存在这条边，那么我们需要维护一个 edgeCount 
		if(adjMatrix[fromIndex][toIndex] == null) {
			edgeCount++;
		}
		// 不管原来存不存在这条边，我们都使用 weight 值覆盖二维数组指定位置的值
		adjMatrix[fromIndex][toIndex] = weight;
		
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[  \t");
		for (int i = 0; i < adjMatrix.length; i++) {
			result.append(vertexs.get(i)).append("\t");
		}
		result.append(" ]\n");
		for (int i = 0; i < adjMatrix.length; i++) {
			result.append("[ " + vertexs.get(i)).append("\t");
			for (int j = 0; j < adjMatrix[i].length; j++) {
				result.append(adjMatrix[i][j]).append("\t");
			}
			result.append(" ]\n");
		}
		return result.toString();
	}
}
