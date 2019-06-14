package _05.cn.itcast.stack;

import _04.cn.itcast.linkedList.SinglyLinkedList;

public class LinkedListStack<T> {
	private SinglyLinkedList<T> list = new SinglyLinkedList<>();
	private Integer maxSize = 10;

	// 无参构造
	public LinkedListStack() {}

	// 带参构造
	public LinkedListStack(int maxSize) {
		this.maxSize = maxSize;
	}

	// 获取栈最大的容量
	public Integer size() {
		return this.maxSize;
	}

	// 获取栈里当前数据的个数
	public Integer length() {
		return list.size();
	}

	// 判断栈是否满了
	public boolean isFull() {
		return list.size() == maxSize;
	}

	// 判断栈是否是空的
	public boolean isEmpty() {
		return list.size() == 0;
	}

	// 入栈操作
	public boolean push(T item) {
		if(isFull()) {
			System.out.println("栈已经满了，无法添加数据");
			return false;
		}else {
			return list.addFirst(item);
		}
	}

	// 出栈操作
	public T pop() {
		if(isEmpty()) {
			System.out.println("栈是空的，没有数据可以弹出");
			return null;
		}else {
			return list.removeFirst();
		}
	}
	
	// 查看栈顶元素
	public T peek() {
		if(isEmpty()) {
			System.out.println("栈是空的，没有数据可以弹出");
			return null;
		}else {
			return list.getFirst();
		}
	}

	// 打印 stack
	@Override
	public String toString() {
		if(isEmpty()) {
			return "[]";
		}else {
			String result = "[";
			for (T data : list) {
				result += data + ", ";
			}
			// 处理一下最后的 ,
			result = result.replaceAll(", $", "");
			result += "]";
			return result;
		}
	}
}
