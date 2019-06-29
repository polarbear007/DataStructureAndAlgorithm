package _10.cn.itcast.tree;

import java.util.PriorityQueue;

import org.junit.Test;

public class MyPriorityQueueTest {
	@Test
	public void testAddItem() {
		MyPriorityQueue<Integer> priorityQueue = new MyPriorityQueue<Integer>(Integer.class, 3);
		priorityQueue.addItem(5);
		priorityQueue.addItem(4);
		priorityQueue.addItem(10);
		
		// 当队列满了以后，我们再添加元素，会直接被忽略
		// 如果你不想被忽略，要阻塞等待，那就是阻塞队列了，不在目前的讨论范围内
		priorityQueue.addItem(10);
		priorityQueue.addItem(10);
		priorityQueue.addItem(10);
		System.out.println(priorityQueue);
	}
	
	@Test
	public void testRemoveItem() {
		MyPriorityQueue<Integer> priorityQueue = new MyPriorityQueue<Integer>(Integer.class);
		priorityQueue.addItem(10);
		priorityQueue.addItem(5);
		priorityQueue.addItem(4);
		System.out.println("删除前：" + priorityQueue);
		priorityQueue.removeItem();
		System.out.println("删除后：" + priorityQueue);
	}
	
	// 使用二叉堆实现的优先级队列，无法实现：  当优先级相同，先入先出的效果
	@Test
	public void testRemoveItem2() {
		MyPriorityQueue<Teacher> priorityQueue = new MyPriorityQueue<Teacher>(Teacher.class);
		priorityQueue.addItem(new Teacher(10, "张三", 12));
		priorityQueue.addItem(new Teacher(2, "李四", 12));
		priorityQueue.addItem(new Teacher(8, "王五", 12));
		priorityQueue.addItem(new Teacher(9, "赵六", 12));
		priorityQueue.addItem(new Teacher(9, "田七", 12));
		System.out.println(priorityQueue);
		System.out.println(priorityQueue.removeItem());
		System.out.println(priorityQueue.removeItem());
	}
	
	// java 内部的 PriorityQueue 同样无法实现优先级相同时，先入先出
	@Test
	public void testPriorityQueue() {
		PriorityQueue<Teacher> priorityQueue = new PriorityQueue<Teacher>();
		priorityQueue.add(new Teacher(10, "张三", 12));
		priorityQueue.add(new Teacher(2, "李四", 12));
		priorityQueue.add(new Teacher(8, "王五", 12));
		priorityQueue.add(new Teacher(2, "赵六", 12));
		priorityQueue.add(new Teacher(2, "田七", 12));
		System.out.println(priorityQueue);
		System.out.println(priorityQueue.poll());
		System.out.println(priorityQueue.poll());
		System.out.println(priorityQueue);
	}
}
