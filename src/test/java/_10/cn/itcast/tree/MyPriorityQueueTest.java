package _10.cn.itcast.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

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
	// 这里的 Teacher 仅仅以 id 属性做为排序的依据！！！
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
	
	@Test
	public void testPeek() {
		MyPriorityQueue<Teacher> priorityQueue = new MyPriorityQueue<Teacher>(Teacher.class);
		priorityQueue.addItem(new Teacher(10, "张三", 12));
		priorityQueue.addItem(new Teacher(2, "李四", 12));
		priorityQueue.addItem(new Teacher(8, "王五", 12));
		priorityQueue.addItem(new Teacher(9, "赵六", 12));
		priorityQueue.addItem(new Teacher(9, "田七", 12));
		System.out.println(priorityQueue.peek());
		System.out.println(priorityQueue);
	}
	
	@Test
	public void testMultiwayMerge() throws FileNotFoundException {
		Scanner[] scs = new Scanner[3];
		for (int i = 0; i < scs.length; i++) {
			scs[i] = new Scanner(new File("merge-sort" + (i + 1) + ".txt"));
		}
		// 创建一个临时数组，用来保存每个文件读取出来的数字
		Integer[] temps = new Integer[3];
		// 最开始，我们需要从每个文件中读取一个数字，先放到 temps 上面
		for (int i = 0; i < temps.length; i++) {
			if(scs[i].hasNextInt()) {
				temps[i] = scs[i].nextInt();
			}
		}
		
		// 然后，我们需要创建一个 长度为 3 的最大堆（或者说优先队列）（因为我们只实现了最大堆，没有去实际最小堆，这里就按最大堆来处理）
		// 每个外部的文件里面的数据排序也是从大到小排序
		// 为了能够快速地根据堆顶元素确定其在源数组的索引位置，我们最好是使用 索引 优先队列
        // 使用索引优先队列，可以根据堆数组索引快速定位该元素在源数组的位置，再然后我们可以确定应该让从哪个文件再获取下一个数字
		// 如果不使用索引优先队列，我们需要根据从队列删除的最大值，再去遍历 temps 数组，找到对应的索引
		// 从而确定应该从哪个文件再获取下一个数字
		// 【说明】索引优先队列虽然很好，但是逻辑实在是太绕了，我实现不出来，到时有需要再去实现吧 （《算法》这本书里面有，可以去官网下载源码）
		MyPriorityQueue<Integer> queue = new MyPriorityQueue<Integer>(Integer.class, 3);
		for (int i = 0; i < temps.length; i++) {
			queue.addItem(temps[i]);
		}
		
		int tempIndex = 0;
		
		while(queue.getSize() != 0) {
			// 从队列中删除一个最大值
			Integer item = queue.removeItem();
			System.out.print(item + " ");
			// 我们需要遍历   temps 数组，确定被删除的这个元素的索引值
			for (int i = 0; i < temps.length; i++) {
				if(temps[i] == item) {
					tempIndex = i;
					break;
				}
			}
			
			// 只要确定了 tempIndex ，我们就可以再去 scs 数组找到对应的文件，并读取下一个数字
			// 如果这个文件已经读完了，那么我们就不再添加到队列，队列的长度就会自动减少一个元素
			// 一直到最后，队列为空，则我们也完成了排序
			if(scs[tempIndex].hasNextInt()) {
				temps[tempIndex] = scs[tempIndex].nextInt();
				queue.addItem(temps[tempIndex]);
			}
		}
	}
}
