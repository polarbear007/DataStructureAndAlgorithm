package _03.cn.itcast.queue;

import org.junit.Test;

public class ArrayCircleQueueTest {
	@Test
	public void testArrayCircleQueue() {
		ArrayCircleQueue queue = new ArrayCircleQueue(3);
		queue.addItem("eric");
		queue.addItem("rose");
		queue.addItem("jack");
		queue.addItem("tom");
		System.out.println(queue);
		System.out.println("取出元素： " + queue.getItem());
		System.out.println("取出元素： " + queue.getItem());
		System.out.println("取出元素： " + queue.getItem());
		System.out.println("取出元素： " + queue.getItem());
		queue.addItem("jerry");
		queue.addItem("mary");
		queue.addItem("lily");
		System.out.println(queue);
		System.out.println("取出元素： " + queue.getItem());
		queue.addItem("bob");
		System.out.println(queue);
	}
	
}
