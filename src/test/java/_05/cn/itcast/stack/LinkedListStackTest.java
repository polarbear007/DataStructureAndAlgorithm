package _05.cn.itcast.stack;

import org.junit.Test;

public class LinkedListStackTest {
	@Test
	public void testPush() {
		LinkedListStack<Integer> stack = new LinkedListStack<Integer>(5);
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
		stack.push(6);
		System.out.println(stack);
	}
	
	@Test
	public void testPop() {
		LinkedListStack<Integer> stack = new LinkedListStack<Integer>(5);
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
		
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
	}
	
	@Test
	public void testSizeAndLength() {
		LinkedListStack<Integer> stack = new LinkedListStack<Integer>(5);
		stack.push(1);
		stack.push(2);
		stack.push(3);
		
		System.out.println("size: " + stack.size());
		System.out.println("length: " + stack.length());
	}
}
