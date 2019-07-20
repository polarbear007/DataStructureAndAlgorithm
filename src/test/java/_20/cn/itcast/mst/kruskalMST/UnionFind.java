package _20.cn.itcast.mst.kruskalMST;
/**
 * 	使用数组实现一个并查集结构，并提供 union-find 等基本操作
 * @author Administrator
 *
 */
public class UnionFind {
	private int[] parent;
	
	// 根据传入的 len 参数，初始化 parent 数组
	public UnionFind(int len) {
		if(len <= 0) {
			throw new RuntimeException("长度不能为0");
		}
		parent = new int[len];
		makeSet();
	}
	
	/**
	 * 	初始化 parent 数组的值
	 */
	private void makeSet() {
		for (int i = 0; i < parent.length; i++) {
			parent[i] = i;
		}
	}
	
	/**
	 * 	我们努力维护，让每个子结点可以直接找到父结点，减少查找复杂程度 
	 * @param currIndex
	 * @return
	 */
	public int findParent(int currIndex) {
		return parent[currIndex];
	}
	
	/**
	 * 	我们直接比较两个无素的父元素是不是相同就可以了
	 * @param index1
	 * @param index2
	 * @return
	 */
	public boolean isConnected(int index1, int  index2) {
		return parent[index1] == parent[index2];
	}
	
	/**
	 * 	合并两个元素所在的集合
	 * 	【注意】 这两个集合应该是不相交的集合才有意义。所以我们在执行合并之前，应该确保这两个元素没有共同的祖先。
	 * @param index1
	 * @param index2
	 */
	public void union(int index1, int index2) {
		if(isConnected(index1, index2)) {
			throw new RuntimeException("这两个元素在同一个集合中，无需合并！");
		}
		// 这里我们也不考虑啥，直接遍历 parent 数组，让全部等于 parent[index2] 的元素，全部改成 parent[index1]
		// 要覆盖前，我们应该先保存一下 parent[index2] 值
		int targetParentIndex = parent[index2];
		for (int i = 0; i < parent.length; i++) {
			if(parent[i] == targetParentIndex) {
				parent[i] = parent[index1];
			}
		}
	}
}
