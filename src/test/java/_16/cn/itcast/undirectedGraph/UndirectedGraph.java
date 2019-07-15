package _16.cn.itcast.undirectedGraph;

import java.util.ArrayList;

public class UndirectedGraph<V> {
	// 保存每个顶点对应的数据
	private ArrayList<V> vertexs = new ArrayList<>();
	// 这个 edgeCount 表示图中的边数量
	private int edgeCount;
	// 使用一个二维数组（矩阵） 来表示顶点之间的关系（其实也可以说是描述边）
	// 这个矩阵里面会显示路径的权值，这里规定一下：
	//  权值为 0 ，说明这条边不存在 
	//  权值大于0，说明这条边存在
	private int[][] adjMatrix;
	
	// 返回邻接矩阵
	public int[][] getAdjMatrix(){
		return adjMatrix;
	}
	
	// 返回顶点的集合
	public ArrayList<V> getVertexs(){
		return vertexs;
	}
	
	// 根据顶点的数据，我们初始化  vertexs 集合 和 adjMatrix 矩阵
	public UndirectedGraph(V[] data) {
		super();
		if(data == null || data.length <= 1) {
			throw new RuntimeException("顶点的数量不能小于1");
		}
		for (int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				vertexs.add(data[i]);
			}else {
				throw new RuntimeException("顶点的数据不能为null");
			}
		}
		// 添加完顶点以后，我们得根据顶点的数量初始化 adjMatrix 矩阵
		adjMatrix = new int[data.length][data.length];
	}
	
	/**
	 * 	返回顶点的数量
	 * @return
	 */
	public int getVertexCount() {
		return vertexs.size();
	}

	/**
	 * 	返回边的数量
	 * @return
	 */
	public int getEdgeCount() {
		return edgeCount;
	}
	
	/**
	 * 	添加一条边，不带权值（如果我们不设置权值的话，权值默认是1）
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean addEdge(V from, V to) {
		return addEdge(from, to, 1);
	}
	
	/**
	 * 	添加一条边（附带权值）
	 * @param from
	 * @param to
	 * @param weight
	 * @return
	 */
	public boolean addEdge(V from, V to, int weight) {
		if(weight < 1) {
			throw new RuntimeException("权值不能小于1");
		}
		int fromIndex = vertexs.indexOf(from);
		int toIndex = vertexs.indexOf(to);
		// 我们不允许一个顶点连接自己，形成 自环
		if(fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
			// 因为是一个无向图，所以我们其实 from 和 to 本身没有方向区别，我们得更新两个位置的权值 
			adjMatrix[fromIndex][toIndex] = weight;
			adjMatrix[toIndex][fromIndex] = weight;
			edgeCount++;
			return true;
		}
		
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
