package _06.cn.itcast.recursion;

import org.junit.Test;

/**
 * 最简单的递归调用
 * @author Administrator
 *
 */
public class RecursionTest01 {
	@Test
	public void test() {
		printNum(3);
	}
	
	public void printNum(int num){
		if(num > 1) {
			printNum(num - 1);
		}
		System.out.println(num);
	}
	
	@Test
	public void test2() {
		System.out.println(factorial(7));
	}
	
	/**
	 * 阶乘
	 * @param end
	 * @return
	 */
	public int factorial(int end) {
		// 出口
		if(end == 1) {
			return 1;
		}else {
			return factorial(end - 1) * end;
		}
	}
	
	
	@Test
	public void test3() {
		System.out.println(accumulation(100));
	}
	
	public int accumulation(int end) {
		// 设置出口
		if(end == 0) {
			return 0;
		}else {
			// 参数变化的方向应该向着出口移动
			return accumulation(end - 1) + end;
		}
	}
}
