package _10.cn.itcast.tree;

import org.junit.Test;

public class ArrayBinaryTreeTest {
	@Test
	public void testPreOrderTraversal() {
		int[] arr = new int[10];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}
		ArrayBinaryTree tree = new ArrayBinaryTree(arr);
		tree.preOrderTraversal(0);
		System.out.println("******************");
		tree.inOrderTraversal(0);
		System.out.println("******************");
		tree.postOrderTraversal(0);
		System.out.println("******************");
		tree.levelOrderTraversal();
	}
}
