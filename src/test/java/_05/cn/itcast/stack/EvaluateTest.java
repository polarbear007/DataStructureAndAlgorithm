package _05.cn.itcast.stack;

import org.junit.Test;

public class EvaluateTest {
	@Test
	public void test() {
		Evaluate e = new Evaluate();
		// 没有问题
		System.out.println(e.evaluate("(1+2)"));
		// 有问题
		System.out.println(e.evaluate("(1+2*3)"));
		// 没问题
		System.out.println(e.evaluate("(1+(2*3))"));
		// 有问题
		System.out.println(e.evaluate("(1+2+3)"));
	}
}
