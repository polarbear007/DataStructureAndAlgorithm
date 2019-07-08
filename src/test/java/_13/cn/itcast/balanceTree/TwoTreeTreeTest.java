package _13.cn.itcast.balanceTree;

import java.util.Arrays;

import org.junit.Test;

import _13.cn.itcast.balanceTree.TwoThreeTree.Node;

public class TwoTreeTreeTest {
	@Test
	public void testFindIndexByKey() {
		Node<Integer, String> node = new Node<Integer, String>(10, "eric");
		System.out.println(node.findIndexByKey(15));
	}
	
	@Test
	public void testPut() {
		TwoThreeTree<Integer, String> tree = new TwoThreeTree<Integer, String>();
		System.out.println(tree.put(12, "eric"));
		// 测试添加相同key ，是否会返回值替换，返回旧值
		System.out.println(tree.put(12, "rose"));
	}
	
	@Test
	public void testPut2() {
		TwoThreeTree<Integer, String> tree = new TwoThreeTree<Integer, String>();
		int[] arr = {8, 6, 10, 15, 26, 3, 7, 14, 12, 23, 30};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		System.out.println();
	}
	
	// 请添加完以后，debug 看一下，然后去 数据结构可视化 网站看一下结构有没有一样
	// 然后，我们也可以修改一下 Node 类里面的  ORDER 属性， 看能不能直接变成 2-3-4 树
	@Test
	public void testPut3() {
		TwoThreeTree<Integer, String> tree = new TwoThreeTree<Integer, String>();
		int[] arr = {5, 9, 10, 54, 74, 32, 12, 33, 44, 25, 84, 16, 24, 95};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		System.out.println();
	}
	
	@Test
	public void testGet() {
		TwoThreeTree<Integer, String> tree = new TwoThreeTree<Integer, String>();
		int[] arr = {5, 9, 10, 54, 74, 32, 12, 33, 44, 25, 84, 16, 24, 95, 11};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		System.out.println(tree.get(23));
		System.out.println(tree.get(32));
		System.out.println(tree.get(24));
	}
	
	@Test
	public void testRemove() {
		TwoThreeTree<Integer, String> tree = new TwoThreeTree<Integer, String>();
		int[] arr = {5, 9, 10, 54, 74, 32, 13, 33, 44, 25, 84, 16, 24, 95, 11};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		 //测试能不能跟左子结点（叶）借用元素
//		 System.out.println(tree.remove(25));
		
		// 测试能不能跟左子结点（非叶） 借用元素
//		tree.put(12, "12");
//		System.out.println(tree.remove(13));
		
		// 测试能不能跟右子结点（叶）借用元素
//		 System.out.println(tree.remove(9));
		
		// 测试能不能跟 右子结点（非叶） 借用元素
//		System.out.println(tree.remove(33));
		
		// 测试当有多个元素的叶子结点，能不能直接删除
//		 System.out.println(tree.remove(10));
		
		// 测试当有多个元素的  非叶子结点，能不能直接删除
//		 System.out.println(tree.remove(84));
		
		// 测试能不能左旋（叶）
//		 System.out.println(tree.remove(5));
		
		// 测试能不能右旋（叶）
//		 System.out.println(tree.remove(32));
		
		// 测试能不能左旋 （非叶）
//		 System.out.println(tree.remove(25));
//		 System.out.println(tree.remove(24));
		
		// 测试能不能右旋 （非叶）
//		tree.put(12, "12");
//		System.out.println(tree.remove(25));
//		System.out.println(tree.remove(24));
		
		// 测试能不能删除根结点
		//System.out.println(tree.remove(13));
		
		System.out.println();
	}
	
	@Test
	public void testRemove2() {
		TwoThreeTree<Integer, String> tree = new TwoThreeTree<Integer, String>();
		int[] arr = {5, 9, 6};
		for (int i = 0; i < arr.length; i++) {
			tree.put(arr[i], String.valueOf(arr[i]));
		}
		System.out.println(tree.remove(5));
		System.out.println();
	}
	
	@Test
	public void testArrayCopyOfRange() {
		int[] arr = {1, 2, 3, 4, 5};
		int[] arr2 = Arrays.copyOfRange(arr, 2, arr.length - 1);
		System.out.println(Arrays.toString(arr2));
	}
}
