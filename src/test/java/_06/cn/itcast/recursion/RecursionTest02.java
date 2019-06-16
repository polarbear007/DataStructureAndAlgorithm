package _06.cn.itcast.recursion;

import org.junit.Test;

/**
 * 使用递归逆序打印单向链表
 * @author Administrator
 *
 */
public class RecursionTest02 {
	@Test
	public void test() {
		// 创建一个单向链表
		// 首先，创建一个 headNode
		Node headNode = new Node();
		
		// 循环创建新的结点并拼接到原来的链表上
		Node currNode = headNode;
		for (int i = 1; i <= 10; i++) {
			Node newNode = new Node();
			newNode.num = i;
			currNode.next = newNode;
			currNode = newNode;
		}
		
		// 使用递归逆向遍历单向链表, 我们只需要传入第一个链表元素结点就可以了
		printNode(headNode.next);
	}
	
	public void printNode(Node node) {
		// 如果不是最后一个结点，那么我们就调用 printNode() 并传入下一个结点的对象
		if(node.next != null) {
			printNode(node.next);
		}
		// 如果是最后一个结点，那么我们就直接打印 结点上面的数据
		System.out.println(node.num);
	}
	
	class Node{
		Integer num;
		Node next;
	}
}
