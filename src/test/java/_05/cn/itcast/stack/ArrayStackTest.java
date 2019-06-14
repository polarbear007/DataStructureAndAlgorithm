package _05.cn.itcast.stack;

import org.junit.Test;

public class ArrayStackTest {
	@Test
	public void testPush() {
		ArrayStack stack = new ArrayStack(5);
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
		ArrayStack stack = new ArrayStack(5);
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
}
