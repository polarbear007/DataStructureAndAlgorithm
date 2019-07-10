package _12.cn.itcast.binarySortedTree;

import org.junit.Test;

public class AVLTreeTest {
	@Test
	public void testUpdateHeight() {
		AVLTree<Integer, String> tree = new AVLTree<Integer, String>();
		tree.put(3, "eric");
		tree.put(1, "rose");
		tree.put(2, "jack");
		tree.put(4, "tom");
		tree.put(5, "jerry");
		
		tree.inOrderTraversal();
		System.out.println("******************");
		
		tree.delete(2);
		tree.inOrderTraversal();
		System.out.println("******************");
		
		tree.delete(4);
		tree.inOrderTraversal();
	}
	
	/**
	 * 测试左旋
	 * 因为我们暂时不考虑那么多, 就在put方法里面加上左旋条件，看看左旋能不能成功
	 */
	@Test
	public void testLeftRotate() {
		AVLTree<Integer, String> tree = new AVLTree<Integer, String>();
		tree.put(5, "eric");
		tree.put(1, "rose");
		tree.put(6, "jack");
		tree.put(7, "tom");
		tree.put(8, "jerry");
		
		System.out.println(tree.getRootLeftHeight());
		System.out.println(tree.getRootRightHeight());
	}
	
	// 测试右旋
	@Test
	public void testRightRotate() {
		AVLTree<Integer, String> tree = new AVLTree<Integer, String>();
		tree.put(7, "eric");
		tree.put(8, "rose");
		tree.put(6, "jack");
		tree.put(5, "tom");
		tree.put(4, "jerry");
		
		System.out.println(tree.getRootLeftHeight());
		System.out.println(tree.getRootRightHeight());
		System.out.println(tree.getRootNodeHeight());
	}
	
	// 测试先左旋再右旋的情况
	@Test
	public void testDoubleRotate1() {
		AVLTree<Integer, String> tree = new AVLTree<Integer, String>();
		tree.put(10, "eric");
		tree.put(6, "rose");
		tree.put(18, "jack");
		tree.put(5, "tom");
		tree.put(7, "jerry");
		tree.put(9, "mary");
		System.out.println(tree.getRootLeftHeight());
		System.out.println(tree.getRootRightHeight());
		System.out.println(tree.getRootNodeHeight());
		System.out.println(tree.getSize());
	}
	
	// 测试先右旋再左旋
	@Test
	public void testDoubleRotate2() {
		AVLTree<Integer, String> tree = new AVLTree<Integer, String>();
		tree.put(7, "eric");
		tree.put(6, "rose");
		tree.put(18, "jack");
		tree.put(15, "tom");
		tree.put(20, "jerry");
		tree.put(12, "mary");
		System.out.println(tree.getRootLeftHeight());
		System.out.println(tree.getRootRightHeight());
		System.out.println(tree.getRootNodeHeight());
		System.out.println(tree.getSize());
	}
	
	@Test
	public void testRemove() {
		AVLTree<Integer, String> tree = new AVLTree<Integer, String>();
		tree.put(5, "5");
		tree.put(3, "3");
		tree.put(8, "8");
		tree.put(1, "1");
		tree.put(2, "2");
		tree.put(6, "6");
		tree.put(10, "10");
		tree.put(15, "15");
		
		tree.delete(8);
		tree.inOrderTraversal();
	}
}
