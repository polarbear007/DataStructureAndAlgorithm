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

class ArrayCircleQueue{
	// 内部应该要有一个 数组 成员变量
	private Object[] array;
	
	// 记录队列头的索引 - 1
	private int front = -1;
	// 记录队列尾的索引
	private int rear = -1;
	// 记录内部数组的最大长度
	private int maxSize;
	
	// 在构造函数中，去初始化这个 array 数组对象
	public ArrayCircleQueue() {
		super();
		// 默认情况下，我们创建一个长度为 10 的数组
		array = new Object[10];
	}
	
	// 带参构造，可以设置内部数组长度
	public ArrayCircleQueue(Integer maxSize) {
		this.maxSize = maxSize;
		array = new Object[maxSize];
	}
	
	// 【注意】 使用环形队列的时候，队列为空，跟队列为满的情况非常像
	//        视觉上来看， front 和 rear 索引是重复的
	//        如果 front == rear ，表示队列为空
	//        如果 front + maxSize == rear ，表示队列是满的
	
	// 判断队列是否已经满了
	public boolean isFull() {
		return rear - front == maxSize;
	}
	
	// 判断队列是否为空
	public boolean isEmpty() {
		return rear == front;
	}
	
	// 添加元素，只要队列没满，都可以添加
	public boolean addItem(Object item) {
		if(isFull()) {
			System.out.println("队列已经满了，无法再添加元素");
			return false;
		}else {
			array[++rear % maxSize] = item;
			return true;
		}
	}
	
	// 只要队列不为空，我们就可以从队列中取元素
	public Object getItem() {
		if(isEmpty()) {
			System.out.println("队列为空，没有元素可以取出");
			return null;
		}else {
			return array[++front % maxSize];
		}
	}

	@Override
	public String toString() {
		
		if(isEmpty()) {
			return "[]";
		}
		
		String result = "[";
		for (int i = front + 1; i <= rear; i++) {
			if(i == rear) {
				result += array[i % maxSize];
			}else {
				result += (array[i % maxSize] + ", ");
			}
		}
		result += "]";
		return result;
	}
}
