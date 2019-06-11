package _03.cn.itcast.queue;

import org.junit.Test;

// 实用数组来实现队列的功能
// 队列的基本特征： 先入先出

public class ArrayQueueTest {
	@Test
	public void testArrayQueue() {
		ArrayQueue queue = new ArrayQueue(3);
		queue.addItem("eric");
		queue.addItem("rose");
		queue.addItem("jack");
		queue.addItem("tom");
		System.out.println(queue);
		System.out.println("取出元素： " + queue.getItem());
		System.out.println("取出元素： " + queue.getItem());
		System.out.println("取出元素： " + queue.getItem());
		System.out.println("取出元素： " + queue.getItem());
		System.out.println(queue);
	}
}

class ArrayQueue{
	// 内部应该要有一个 数组 成员变量
	private Object[] array;
	
	// 记录队列头的索引 - 1
	private int front = -1;
	// 记录队列尾的索引
	private int rear = -1;
	// 记录内部数组的最大长度
	private int maxSize;
	
	// 在构造函数中，去初始化这个 array 数组对象
	public ArrayQueue() {
		super();
		// 默认情况下，我们创建一个长度为 10 的数组
		this.array = new Object[10];
	}
	
	// 带参构造，可以设置内部数组长度
	public ArrayQueue(Integer maxSize) {
		this.maxSize = maxSize;
		array = new Object[maxSize];
	}
	
	public boolean addItem(Object item) {
		if(!isFull()) {
			array[++this.rear] = item;
			return true;
		}else {
			System.out.println("队列已经满了，无法保存新的元素");
			return false;
		}
	}
	
	public Object getItem() {
		if(isEmpty()) {
			System.out.println("队列是空的，没有元素可取！");
			return null;
		}else {
			return array[++front];
		}
	}
	
	public boolean isEmpty() {
		return this.rear - this.front == 0;
	}
	
	public boolean isFull() {
		return rear == maxSize - 1;
	}

	@Override
	public String toString() {
		// 如果是一个空的队列，那么直接返回  []
		if(isEmpty()) {
			return "[]";
		}
		
		String  result = "[";
		
		// 如果一个队列不是空的，那么说明 this.rear 肯定不为 -1 ,所以我们不需要考虑 -1 的情况
		// 我们取数据，是从  this.front + 1 的索引开始取的，所以不需要考虑 -1 的情况
		for (int i = this.front + 1; i <= this.rear; i++) {
			if(i == this.rear) {
				result += array[i];
			}else {
				result += (array[i] + ", ");
			}
		}
		result += "]";
		return result;
	}
}
