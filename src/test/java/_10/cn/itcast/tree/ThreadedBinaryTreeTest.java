package _10.cn.itcast.tree;

import org.junit.Test;

public class ThreadedBinaryTreeTest {
	@Test
	public void testInOrderTraversal() {
		Integer[] dataArray = new Integer[20];
		for (int i = 0; i < dataArray.length; i++) {
			dataArray[i] = i + 1;
		}
		ThreadedBinaryTree<Integer> tree = new ThreadedBinaryTree<Integer>(dataArray);
		
		//tree.inOrderTraversal();
		//tree.preOrderTraversal();
		tree.postOrderTraversal();
	}
}
