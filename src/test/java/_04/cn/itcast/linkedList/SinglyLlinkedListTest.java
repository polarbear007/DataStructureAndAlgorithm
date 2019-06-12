package _04.cn.itcast.linkedList;

import java.util.Iterator;

import org.junit.Test;

public class SinglyLlinkedListTest {
	@Test
	public void testSinglyLinkedList() {
		SinglyLinkedList<Student> linkedList = new SinglyLinkedList<Student>();
		linkedList.addByOrder(new Student(3, "小白", 12));
		linkedList.addByOrder(new Student(2, "小花", 12));
		linkedList.addByOrder(new Student(5, "eric", 12));
		linkedList.addByOrder(new Student(1, "小明", 12));
		linkedList.addByOrder(new Student(4, "小黑", 12));

		System.out.println(linkedList);
		System.out.println("*******************");
		System.out.println(linkedList.getByIndex(0));
		System.out.println(linkedList.getByIndex(2));
//		System.out.println(linkedList.getByIndex(5));
//		System.out.println(linkedList.getByIndex(-5));
		System.out.println("*******************");
		System.out.println("修改前：");
		System.out.println(linkedList.getByIndex(2));
		linkedList.setByIndex(2, new Student(3, "哈哈哈", 13));
		System.out.println("修改后： ");
		System.out.println(linkedList.getByIndex(2));
		System.out.println("*******************");
		System.out.println("删除前：");
		System.out.println(linkedList);
		linkedList.removeByIndex(2);
		System.out.println("删除后： ");
		System.out.println(linkedList);
		System.out.println("*******************");
		System.out.println("添加前：");
		System.out.println(linkedList);
		linkedList.addByIndex(4, new Student(3, "哈哈哈", 13));
		System.out.println("添加后： ");
		System.out.println(linkedList);
	}
	
	@Test
	public void testReverse() {
		SinglyLinkedList<Student> linkedList = new SinglyLinkedList<Student>();
		linkedList.addByOrder(new Student(3, "小白", 12));
		linkedList.addByOrder(new Student(2, "小花", 12));
		linkedList.addByOrder(new Student(5, "eric", 12));
		linkedList.addByOrder(new Student(1, "小明", 12));
		linkedList.addByOrder(new Student(4, "小黑", 12));
		System.out.println("反转前:");
		System.out.println(linkedList);
		
		linkedList.reverse();
		System.out.println("反转后：");
		System.out.println(linkedList);
	}
	
	@Test
	public void testIterable() {
		SinglyLinkedList<Student> linkedList = new SinglyLinkedList<Student>();
		linkedList.addByOrder(new Student(3, "小白", 12));
		linkedList.addByOrder(new Student(2, "小花", 12));
		linkedList.addByOrder(new Student(5, "eric", 12));
		linkedList.addByOrder(new Student(1, "小明", 12));
		linkedList.addByOrder(new Student(4, "小黑", 12));
		
		for (Student stu : linkedList) {
			System.out.println(stu);
		}
	}
	
	@Test
	public void testIterable2() {
		SinglyLinkedList<Student> linkedList = new SinglyLinkedList<Student>();
		linkedList.addByOrder(new Student(3, "小白", 12));
		linkedList.addByOrder(new Student(2, "小花", 12));
		linkedList.addByOrder(new Student(5, "eric", 12));
		linkedList.addByOrder(new Student(1, "小明", 12));
		linkedList.addByOrder(new Student(4, "小黑", 12));
		
		Iterator<Student> it = linkedList.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	// 添加另外的一个集合， 直接往现在的集合尾部添加
	@Test
	public void testAddAll() {
		SinglyLinkedList<Student> linkedList1 = new SinglyLinkedList<Student>();
		linkedList1.addByOrder(new Student(3, "小白", 12));
		linkedList1.addByOrder(new Student(2, "小花", 12));
		linkedList1.addByOrder(new Student(5, "eric", 12));
		linkedList1.addByOrder(new Student(1, "小明", 12));
		linkedList1.addByOrder(new Student(4, "小黑", 12));
		
		SinglyLinkedList<Student> linkedList2 = new SinglyLinkedList<Student>();
		linkedList2.addByOrder(new Student(6, "rose", 12));
		linkedList2.addByOrder(new Student(7, "jack", 12));
		linkedList2.addByOrder(new Student(8, "tom", 12));
		linkedList2.addByOrder(new Student(9, "jerry", 12));
		linkedList2.addByOrder(new Student(10, "mary", 12));
		linkedList1.addAll(linkedList2);
		
		for (Student stu : linkedList1) {
			System.out.println(stu);
		}
		
		System.out.println(linkedList1.size());
	}
	
	// 把一个集合插入到指定的索引处
	@Test
	public void testAddAllToIndex() {
		SinglyLinkedList<Student> linkedList1 = new SinglyLinkedList<Student>();
		linkedList1.addByOrder(new Student(3, "小白", 12));
		linkedList1.addByOrder(new Student(2, "小花", 12));
		linkedList1.addByOrder(new Student(5, "eric", 12));
		linkedList1.addByOrder(new Student(1, "小明", 12));
		linkedList1.addByOrder(new Student(4, "小黑", 12));
		
		SinglyLinkedList<Student> linkedList2 = new SinglyLinkedList<Student>();
		linkedList2.addByOrder(new Student(6, "rose", 12));
		linkedList2.addByOrder(new Student(7, "jack", 12));
		linkedList2.addByOrder(new Student(8, "tom", 12));
		linkedList2.addByOrder(new Student(9, "jerry", 12));
		linkedList2.addByOrder(new Student(10, "mary", 12));
		// 从第3个元素的位置插入一个集合
		linkedList1.addAllToIndex(5, linkedList2);
		
		for (Student data : linkedList1) {
			System.out.println(data);
		}
		
		System.out.println(linkedList1.size());
		System.out.println(linkedList1.getLast());
	}
	
	@Test
	public void testAddAllByOrder() {
		SinglyLinkedList<Student> linkedList1 = new SinglyLinkedList<Student>();
		linkedList1.addByOrder(new Student(3, "小白", 12));
		linkedList1.addByOrder(new Student(2, "小花", 12));
		linkedList1.addByOrder(new Student(5, "eric", 12));
		linkedList1.addByOrder(new Student(1, "小明", 12));
		linkedList1.addByOrder(new Student(4, "小黑", 12));
		
		SinglyLinkedList<Student> linkedList2 = new SinglyLinkedList<Student>();
		linkedList2.addLast(new Student(9, "jerry", 12));
		linkedList2.addLast(new Student(7, "jack", 12));
		linkedList2.addLast(new Student(8, "tom", 12));
		linkedList2.addLast(new Student(10, "mary", 12));
		linkedList2.addLast(new Student(6, "rose", 12));
		
		linkedList1.addAllByOrder(linkedList2);
		
		for (Student stu : linkedList1) {
			System.out.println(stu);
		}
		
		System.out.println(linkedList1.size());
		System.out.println(linkedList1.getLast());
	}
	
	
	// 需求： 逆序打印一个单向链表
	// 思路： 我们前面已经实现了通过索引获取指定的链表元素，所以我们可以在外面直接使用for 去逆向遍历
	@Test
	public void test1() {
		SinglyLinkedList<Student> linkedList = new SinglyLinkedList<Student>();
		linkedList.addByOrder(new Student(3, "小白", 12));
		linkedList.addByOrder(new Student(2, "小花", 12));
		linkedList.addByOrder(new Student(5, "eric", 12));
		linkedList.addByOrder(new Student(1, "小明", 12));
		linkedList.addByOrder(new Student(4, "小黑", 12));
		System.out.println("正向打印：");
		System.out.println(linkedList);
		
		// 因为我们不知道怎么让一个链表可以使用for each 遍历，所以我们就先用索引的方式去遍历 
		for (int i = linkedList.size() - 1; i >= 0; i--) {
			System.out.println(linkedList.getByIndex(i));
		}
	}
}
