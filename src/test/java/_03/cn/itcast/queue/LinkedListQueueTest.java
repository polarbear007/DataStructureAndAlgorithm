package _03.cn.itcast.queue;

import org.junit.Test;

public class LinkedListQueueTest {
	@Test
	public void test() {
		LinkedListQueue<String> queue = new LinkedListQueue<String>();
		queue.addItem("eric");
		queue.addItem("rose");
		queue.addItem("jack");
		System.out.println(queue);
	}
}
