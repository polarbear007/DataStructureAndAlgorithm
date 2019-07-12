package _14.cn.itcast.redBlackTree;

import org.junit.Test;

public class RedBlackTreeTest {
	@Test
	public void testPut() {
		RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
		int[] arr = {5, 10, 12, 2, 7, 15, 6, 18};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		System.out.println(tree.getSize());
	}
	
	@Test
	public void testGet() {
		RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
		int[] arr = {5, 10, 12, 2, 7, 15, 6, 18};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		System.out.println(tree.get(5));
		System.out.println(tree.get(12));
		System.out.println(tree.get(30));
	}
	
	@Test
	public void testRemove() {
		RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
		int[] arr = {25, 15, 65, 75, 58, 20, 12, 14, 18, 23, 51, 60, 66, 98, 63, 85};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		
		// 测试删除一个红色的叶结点（左侧）
		System.out.println(tree.remove(18));
		// 测试删除一个红色的叶结点（右侧）
		System.out.println(tree.remove(23));
		
		// 测试 right right case
		System.out.println(tree.remove(51));
		// 测试 right left case
		System.out.println(tree.remove(66));
		// 测试 left right case
		System.out.println(tree.remove(20));
		// 测试 left left case
		tree.put(55, "55");
		System.out.println(tree.remove(63));
		// 测试 父结点为红色， 右子结点及其子结点都为黑色（需要变色处理，不需要向上递归）
		System.out.println(tree.remove(55));
		// 测试 父结点为红色， 左子结点及其子结点都为黑色（需要变色处理，不需要向上）
		System.out.println(tree.remove(98));
		// 测试 父结点为 黑色， 右子结点及其子结点都为黑色（需要变色，需要向上递归）
		System.out.println(tree.remove(12));
		// 测试 右子结点为 红色，  right case 
		System.out.println(tree.remove(15));
		System.out.println(tree.remove(14));
		
		
		tree.inOrderTraversal();
	}
	
}
