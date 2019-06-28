package _10.cn.itcast.tree;

import java.util.Stack;

import org.junit.Test;

public class BinaryTreeSetSetTest {
	@Test
	public void testCompareTo() {
		Integer m = 1;
		Integer n = 2;
		// compareTo() 返回值小于零时，说明 m 小于 n
		System.out.println(m.compareTo(n));
	}
	
	// 暂时我们没有遍历、打印方法，我们只能先 debug 看一下
	@Test
	public void testAdd() {
		BinaryTreeSet<Integer> tree = new BinaryTreeSet<>();
		tree.add(5);
		tree.add(9);
		tree.add(3);
		tree.add(6);
		System.out.println();
	}
	
	// 添加没有实现 comparable 接口的实体类，看看会不会报错
	// 添加一个 null ，看会不会报错
	@Test
	public void testAdd2() {
		BinaryTreeSet<Teacher> tree = new BinaryTreeSet<Teacher>();
		//tree.add(new Teacher());
		tree.add(null);
	}
	
	// 添加重复的元素，看会不会返回 false
	@Test
	public void testAdd3() {
		BinaryTreeSet<Integer> tree = new BinaryTreeSet<>();
		System.out.println(tree.add(5));
		System.out.println(tree.add(5));
		
		System.out.println(tree.add(9));
		System.out.println(tree.add(3));
		System.out.println(tree.add(6));
		
		System.out.println(tree.add(9));
	}
	
	
	// 测试前序 / 中序 和 后序遍历
	@Test
	public void testTraversal() {
		BinaryTreeSet<Integer> tree = new BinaryTreeSet<>();
		tree.add(10);
		tree.add(5);
		tree.add(15);
		tree.add(3);
		tree.add(7);
		tree.add(12);
		tree.add(17);
		tree.add(1);
		tree.add(4);
		tree.add(6);
		tree.add(9);
		tree.add(11);
		tree.add(14);
		tree.add(16);
		tree.add(19);
		tree.add(2);
		System.out.println("前序遍历：");
		tree.preorderTraversal();
		System.out.println("****************");
		System.out.println("中序遍历：");
		tree.inorderTraversal();
		System.out.println("****************");
		System.out.println("后序遍历：");
		tree.postorderTraversal();
		System.out.println("****************");
		System.out.println("层序遍历：");
		tree.levelOrderTraversal();
	}
	
	// 测试一下非递归的 前、中、后序遍历
	@Test
	public void  testTraversal2() {
		BinaryTreeSet<Integer> tree = new BinaryTreeSet<>();
		tree.add(10);
		tree.add(5);
		tree.add(15);
		tree.add(3);
		tree.add(7);
		tree.add(12);
		tree.add(17);
		tree.add(1);
		tree.add(4);
		tree.add(6);
		tree.add(9);
		tree.add(11);
		tree.add(14);
		tree.add(16);
		tree.add(19);
		tree.add(2);
		System.out.println("非递归前序：");
		tree.preorderTraversal2();
		System.out.println("****************");
		System.out.println("非递归中序：");
		tree.inorderTraversal2();
		System.out.println("****************");
		System.out.println("非递归后序：");
		tree.postorderTraversal2();
		System.out.println("****************");
		System.out.println("递归后序：");
		tree.postorderTraversal();
	}
	
	@Test
	public void testContains() {
		BinaryTreeSet<Integer> tree = new BinaryTreeSet<>();
		tree.add(10);
		tree.add(5);
		tree.add(15);
		tree.add(3);
		tree.add(7);
		tree.add(12);
		tree.add(17);
		tree.add(1);
		tree.add(4);
		tree.add(6);
		tree.add(9);
		tree.add(11);
		tree.add(14);
		tree.add(16);
		tree.add(19);
		tree.add(2);
		System.out.println(tree.contains(17));
		System.out.println(tree.contains(100));
		System.out.println(tree.contains(-89));
		System.out.println(tree.contains(2));
	}
}
