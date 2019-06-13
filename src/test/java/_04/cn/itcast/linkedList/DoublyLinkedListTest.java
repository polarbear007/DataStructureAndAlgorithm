package _04.cn.itcast.linkedList;

import org.junit.Test;

public class DoublyLinkedListTest {
	
	@Test
	public void testAddFirst() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addFirst(new Student(1, "eric", 12));
		linkedList.addFirst(new Student(2, "rose", 12));
		linkedList.addFirst(new Student(3, "jack", 12));
		System.out.println(linkedList);
		System.out.println(linkedList.size());
		System.out.println("first: " + linkedList.getFirst());
		System.out.println("last: " + linkedList.getLast());
	}
	
	@Test
	public void testAddLast() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(3, "jack", 12));
		System.out.println(linkedList);
		System.out.println(linkedList.size());
		System.out.println("first: " + linkedList.getFirst());
		System.out.println("last: " + linkedList.getLast());
	}
	
	@Test
	public void testAddByInde() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(3, "jack", 12));
		
		System.out.println("插入前：");
		System.out.println(linkedList);
		
		// 在第二个元素前面添加一个新的元素
		linkedList.addByIndex(1, new Student(10, "tom", 12));
		System.out.println("插入后：");
		System.out.println(linkedList);
		
		// 测试在最后元素后面添加新元素
		linkedList.addByIndex(4, new Student(11, "hahah", 12));
		System.out.println("测试在最后元素后面添加新元素：");
		System.out.println(linkedList);
		System.out.println(linkedList.getLast());
		System.out.println(linkedList.size());
		
		// 测试索引越界
		linkedList.addByIndex(6, new Student(11, "hahah", 12));
		System.out.println("测试索引越界：");
		System.out.println(linkedList);
	}
	
	@Test
	public void testAddByOrder() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(4, "jack", 12));
		
		System.out.println(linkedList);
		linkedList.addByOrder(new Student(3, "mary", 12));
		System.out.println(linkedList);
		System.out.println(linkedList.size());
		System.out.println(linkedList.getLast());
	}
	
	@Test
	public void testRemoveFirst() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		// 测试当链表为空时，删除第一个元素
		System.out.println(linkedList.removeFirst());
		System.out.println(linkedList.size());
		System.out.println(linkedList.getLast());
		System.out.println("**********************");
		// 添加一个元素
		linkedList.addLast(new Student(1, "eric", 12));
		System.out.println(linkedList.removeFirst());
		System.out.println(linkedList.size());
		System.out.println(linkedList.getLast());
		System.out.println("**********************");
		// 当集合有多个元素的时候
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(4, "jack", 12));
		System.out.println(linkedList.removeFirst());
		System.out.println(linkedList.size());
		System.out.println(linkedList.getLast());
	}
	
	@Test
	public void testRemoveLast() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(4, "jack", 12));
		System.out.println(linkedList);
		
		System.out.println(linkedList.removeLast());
		System.out.println(linkedList.size());
		System.out.println(linkedList.getLast());
	}
	
	@Test
	public void testRemoveByIndex() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(4, "jack", 12));
		
		// System.out.println(linkedList.removeByIndex(0));
		// System.out.println(linkedList.removeByIndex(1));
		// System.out.println(linkedList.removeByIndex(2));
		System.out.println(linkedList.size());
		System.out.println(linkedList.getLast());
	}
	
	@Test
	public void testGetByIndex() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(4, "jack", 12));
		
		System.out.println(linkedList.getByIndex(0));
		System.out.println(linkedList.getByIndex(1));
		System.out.println(linkedList.getByIndex(2));
	}
	
	@Test
	public void testSetByIndex() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(4, "jack", 12));
		
		System.out.println(linkedList.getByIndex(2));
		linkedList.setByIndex(2, new Student(3, "jack", 12));
		System.out.println(linkedList.getByIndex(2));
	}
	
	@Test
	public void testIterable() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(3, "jack", 12));
		
		for (Student stu : linkedList) {
			System.out.println(stu);
		}
	}
	
	@Test
	public void testAddAll() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(3, "jack", 12));
		
		DoublyLinkedList<Student> linkedList2 = new DoublyLinkedList<Student>();
		linkedList2.addLast(new Student(4, "tom", 12));
		linkedList2.addLast(new Student(5, "jerry", 12));
		linkedList2.addLast(new Student(6, "mary", 12));
		linkedList.addAll(linkedList2);
		
		for (Student stu : linkedList) {
			System.out.println(stu);
		}
		
		System.out.println(linkedList.size());
		System.out.println(linkedList.getLast());
	}
	
	@Test
	public void testAddAllByIndex() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addLast(new Student(1, "eric", 12));
		linkedList.addLast(new Student(2, "rose", 12));
		linkedList.addLast(new Student(3, "jack", 12));
		
		DoublyLinkedList<Student> linkedList2 = new DoublyLinkedList<Student>();
		linkedList2.addLast(new Student(4, "tom", 12));
		linkedList2.addLast(new Student(5, "jerry", 12));
		linkedList2.addLast(new Student(6, "mary", 12));
		
		linkedList.addAllToIndex(3, linkedList2);
		for (Student student : linkedList) {
			System.out.println(student);
		}
	}
	
	@Test
	public void testAddAllByOrder() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addByOrder(new Student(1, "eric", 12));
		linkedList.addByOrder(new Student(3, "jack", 12));
		linkedList.addByOrder(new Student(2, "rose", 12));
		
		DoublyLinkedList<Student> linkedList2 = new DoublyLinkedList<Student>();
		linkedList2.addLast(new Student(4, "tom", 12));
		linkedList2.addLast(new Student(6, "mary", 12));
		linkedList2.addLast(new Student(5, "jerry", 12));
		
		linkedList.addAllByOrder(linkedList2);
		for (Student student : linkedList) {
			System.out.println(student);
		}
		System.out.println("***********************");
		System.out.println(linkedList.getLast());
		System.out.println(linkedList.size());
	}
	
	@Test
	public void testReverse() {
		DoublyLinkedList<Student> linkedList = new DoublyLinkedList<Student>();
		linkedList.addByOrder(new Student(1, "eric", 12));
		linkedList.addByOrder(new Student(3, "jack", 12));
		linkedList.addByOrder(new Student(2, "rose", 12));
		
		linkedList.reverse();
		for (Student stu : linkedList) {
			System.out.println(stu);
		}
		
		System.out.println("最后的元素： " + linkedList.getLast());
	}
}
