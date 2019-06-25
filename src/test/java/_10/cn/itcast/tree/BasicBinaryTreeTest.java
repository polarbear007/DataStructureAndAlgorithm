package _10.cn.itcast.tree;

import org.junit.Test;

public class BasicBinaryTreeTest {

	@Test
	public void testAdd1() {
		BasicBinaryTree<Integer> tree = new BasicBinaryTree<Integer>();
		tree.add(1);
		tree.add(2);
		tree.add(3);
		tree.add(4);
		// 因为 add1 方法是按从左到右，从上到下的方式添加的
		// 所以如果我们使用层序遍历得到的元素顺序跟添加顺序一样，说明我们的添加方法应该没问题
		tree.levelOrderTraversal();
	}

	@Test
	public void testContains() {
		BasicBinaryTree<Integer> tree = new BasicBinaryTree<Integer>();
		tree.add(1);
		tree.add(2);
		tree.add(3);
		tree.add(4);
		System.out.println(tree.contains(3));
		System.out.println(tree.contains(5));
	}
}
