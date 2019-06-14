package _05.cn.itcast.stack;
/**
 * 使用数组来实现一个简单的栈结构
 * 栈的特点： 先入后出，只能操作栈顶的数据
 * 
 * push() 入栈
 * pop()  弹栈
 * isFull()  栈是否是满的，如果是满的，那么就无法再添加新的数据了
 * isEmpty() 栈是否是空的，如果是空的，那么就无法再弹出数据了
 * @author Administrator
 *
 */
public class ArrayStack {
	private Object[] arr;
	// 默认的数组长度为 10
	private Integer maxSize = 10;
	// 栈顶的索引，默认值是 -1
	private Integer top = -1;
	
	// 无参构造
	public ArrayStack() {
		super();
		arr = new Object[maxSize];
	}
	
	// 带参构造 
	public ArrayStack(int maxSize) {
		this.maxSize = maxSize;
		arr = new Object[maxSize];
	}
	
	// 获取栈最大的容量
	public Integer size() {
		return this.maxSize;
	}
	
	// 获取栈里当前数据的个数
	public Integer length() {
		return this.top + 1;
	}
	
	// 判断栈是否满了
	public boolean isFull() {
		return this.top == this.maxSize -1;
	}
	
	// 判断栈是否是空的
	public boolean isEmpty() {
		return this.top == -1;
	}
	
	// 入栈操作
	public boolean push(Object item) {
		if(isFull()) {
			System.out.println("栈已经满了，无法添加数据");
			return false;
		}else {
			arr[++top] = item;
			return true;
		}
	}
	
	// 出栈操作
	public Object pop() {
		if(isEmpty()) {
			System.out.println("栈是空的，无法弹出数据");
			return null;
		}else {
			return arr[top--];
		}
	}
	
	// 查看栈顶元素
	public Object peek() {
		if(isEmpty()) {
			System.out.println("栈是空的，没有数据可以弹出");
			return null;
		}else {
			return arr[top];
		}
	}
	
	// 打印 stack 
	@Override
	public String toString() {
		if(isEmpty()) {
			return "[]";
		}
		String result = "[";
		for(int i = top; i >= 0; i--) {
			if(i == 0) {
				result += arr[i];
				break;
			}
			result += (arr[i] + ", ");
		}
		result += "]";
		return result;
	}
}
