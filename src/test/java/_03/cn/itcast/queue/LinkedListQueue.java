package _03.cn.itcast.queue;
/**
 * 使用链表来实现队列的功能： 先入先出
 * 链表我们已经实现了： addLast() 和 removeFirst() 的方法了
 * 只要我们添加元素的时候，一直往链表后面添加，删除的时候，一直从链表的第一个删除，那么就实现了队列的先入先出的功能了
 * 使用链表来模拟队列的功能，我们不需要设置 front 和 rear 变量
 * @author Administrator
 *
 */

import _04.cn.itcast.linkedList.SinglyLinkedList;

public class LinkedListQueue<T> {
	private SinglyLinkedList<T> list = new SinglyLinkedList<>();
	private Integer maxSize;
	
	public LinkedListQueue() {
		// 默认内部链表的最大长度为 10
		maxSize = 10;
	}
	
	// 有参构造可以设置队列的最大长度
	public LinkedListQueue(Integer maxSize) {
		this.maxSize = maxSize;
	}
	
	public boolean isFull() {
		return list.size() == maxSize;
	}
	
	public boolean isEmpty() {
		return list.size() == 0;
	}
	
	// 如果队列已经满了，那么我们就返回false ，添加失败
	// 如果队列还没有满 的话，那么就正常添加
	public boolean addItem(T data) {
		if(list.size() < maxSize) {
			return list.addLast(data);
		}else {
			return false;
		}
	}
	
	public T removeItem() {
		if(isEmpty()) {
			return null;
		}else {
			return list.getFirst();
		}
	}
	
	public String toString() {
		if(isEmpty()) {
			return "[]";
		}else {
			String result = "[";
			for (T data : list) {
				result += (data + ", ");
			}
			
			// 处理一下最后一个  ", "
			result = result.replaceAll(", $", "");
			result += "]";
			return result;
		}
	}
}
