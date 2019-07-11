package _12.cn.itcast.binarySortedTree;

import java.util.ArrayList;

import org.junit.Test;

import _12.cn.itcast.binarySortedTree.BinarySortedTree.Node;

public class BinarySortedTreeTest {
	@Test
	public void testPut() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put(3, "eric");
		tree.put(1, "eric");
		tree.put(2, "eric");
		tree.put(4, "eric");
		tree.put(5, "eric");
		System.out.println(tree.put(1, "rose"));
		System.out.println(tree.put(3, "jack"));
		System.out.println(tree.put(4, "tom"));
		System.out.println(tree.put(6, "jerry"));
	}
	
	@Test
	public void testPut2() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		tree.put2(1, "eric");
		tree.put2(2, "eric");
		tree.put2(4, "eric");
		tree.put2(5, "eric");
		System.out.println(tree.put2(1, "rose"));
		System.out.println(tree.put2(3, "jack"));
		System.out.println(tree.put2(4, "tom"));
		System.out.println(tree.put2(6, "jerry"));
	}
	
	@Test
	public void testGet() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		tree.put2(1, "rose");
		tree.put2(2, "jack");
		tree.put2(4, "tom");
		tree.put2(5, "jerry");
		
		System.out.println(tree.get(3));
		System.out.println(tree.get(1));
		System.out.println(tree.get(2));
		System.out.println(tree.get(4));
		System.out.println(tree.get(5));
	}
	
	@Test
	public void testGet2() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		tree.put2(1, "rose");
		tree.put2(2, "jack");
		tree.put2(4, "tom");
		tree.put2(5, "jerry");
		
		System.out.println(tree.get2(3));
		System.out.println(tree.get2(1));
		System.out.println(tree.get2(2));
		System.out.println(tree.get2(4));
		System.out.println(tree.get2(5));
	}
	
	// testDeleteMin 是一个临时方法，因为  deleteMin 方法是私有的，所以我们写个临时的方法
	@Test
	public void testDeleteMin() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		tree.put2(1, "rose");
		tree.put2(2, "jack");
		tree.put2(4, "tom");
		tree.put2(5, "jerry");
		
		tree.testDeleteMin();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMin();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMin();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMin();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMin();
		tree.inOrderTraversal();
		System.out.println("************");
	}
	
	@Test
	public void testDeleteMax() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		tree.put2(1, "rose");
		tree.put2(2, "jack");
		tree.put2(4, "tom");
		tree.put2(5, "jerry");
		
		tree.testDeleteMax();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMax();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMax();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMax();
		tree.inOrderTraversal();
		System.out.println("************");
		tree.testDeleteMax();
		tree.inOrderTraversal();
		System.out.println("************");
	}
	
	@Test
	public void testDelete() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(13, "13");
		tree.put2(11, "11");
		tree.put2(18, "18");
		tree.put2(8, "8");
		tree.put2(12, "12");
		tree.put2(15, "15");
		tree.put2(25, "25");
		tree.put2(7, "7");
		tree.put2(10, "10");
		tree.put2(14, "14");
		System.out.println("删除了："  + tree.delete(18));
		tree.inOrderTraversal();
		System.out.println("************");
		System.out.println("删除了："  + tree.delete(7));
		tree.inOrderTraversal();
		System.out.println("************");
		System.out.println("删除了："  + tree.delete(11));
		tree.inOrderTraversal();
		System.out.println("************");
		System.out.println("删除了："  + tree.delete(13));
		tree.inOrderTraversal();
		System.out.println("************");
		System.out.println("删除了："  + tree.delete(5));
		tree.inOrderTraversal();
		System.out.println("************");
	}
	
	@Test
	public void testSize() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		tree.put2(1, "rose");
		tree.put2(2, "jack");
		tree.put2(4, "tom");
		tree.put2(5, "jerry");
		System.out.println(tree.getSize());
		System.out.println("删除了："  + tree.delete(2));
		System.out.println(tree.getSize());
		System.out.println("删除了："  + tree.delete(1));
		System.out.println(tree.getSize());
		System.out.println("删除了："  + tree.delete(4));
		System.out.println(tree.getSize());
		System.out.println("删除了："  + tree.delete(3));
		System.out.println(tree.getSize());
		// 重复删除相同的键
		System.out.println("删除了："  + tree.delete(3));
		System.out.println(tree.getSize());
		System.out.println("删除了："  + tree.delete(5));
		System.out.println(tree.getSize());
	}
	
	@Test
	public void testSize2() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		System.out.println(tree.getSize());
		tree.put2(1, "rose");
		System.out.println(tree.getSize());
		tree.put2(2, "jack");
		System.out.println(tree.getSize());
		tree.put2(4, "tom");
		System.out.println(tree.getSize());
		tree.put2(5, "jerry");
		System.out.println(tree.getSize());
		// 重复添加相同的键
		tree.put2(5, "jerry");
		System.out.println(tree.getSize());
	}
	
	@Test
	public void testFloor() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put(19, "19");
		tree.put(9, "19");
		tree.put(24, "19");
		tree.put(4, "19");
		tree.put(14, "19");
		tree.put(20, "19");
		tree.put(32, "19");
		tree.put(1, "19");
		tree.put(8, "19");
		tree.put(12, "19");
		tree.put(18, "19");
		tree.put(23, "19");
		tree.put(28, "19");
		tree.put(38, "19");
		System.out.println(tree.floor(25));
		System.out.println(tree.floor(15));
		System.out.println(tree.floor(5));
		System.out.println(tree.floor(0));
		
		System.out.println(tree.floor2(25));
		System.out.println(tree.floor2(15));
		System.out.println(tree.floor2(5));
		System.out.println(tree.floor(0));
	}
	
	@Test
	public void testCeiling() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put(19, "19");
		tree.put(9, "19");
		tree.put(24, "19");
		tree.put(4, "19");
		tree.put(14, "19");
		tree.put(20, "19");
		tree.put(32, "19");
		tree.put(1, "19");
		tree.put(8, "19");
		tree.put(12, "19");
		tree.put(18, "19");
		tree.put(23, "19");
		tree.put(28, "19");
		tree.put(38, "19");
		System.out.println(tree.ceiling(25));
		System.out.println(tree.ceiling(15));
		System.out.println(tree.ceiling(5));
		System.out.println(tree.ceiling(0));
		
		System.out.println(tree.ceiling2(25));
		System.out.println(tree.ceiling2(15));
		System.out.println(tree.ceiling2(5));
		System.out.println(tree.ceiling2(0));
	}
	
	@Test
	public void testNodeList() {
		BinarySortedTree<Integer, String> tree = new BinarySortedTree<Integer, String>();
		tree.put2(3, "eric");
		tree.put2(1, "rose");
		tree.put2(2, "jack");
		tree.put2(4, "tom");
		tree.put2(5, "jerry");
		ArrayList<Node<Integer, String>> nodeList = tree.nodeList();
		for (Node<Integer, String> node : nodeList) {
			System.out.println(node);
		}
	}
}
