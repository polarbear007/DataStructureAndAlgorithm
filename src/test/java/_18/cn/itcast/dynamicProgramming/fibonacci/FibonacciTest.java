package _18.cn.itcast.dynamicProgramming.fibonacci;

import org.junit.Test;

public class FibonacciTest {
	/**
	 *	当 n = 10， 耗时：0毫秒
	 *	当 n = 20， 耗时：0毫秒
	 *	当 n = 30， 耗时：0毫秒
	 *	当 n = 40， 耗时：0毫秒
	 *	当 n = 50， 耗时：0毫秒
	 */
	@Test
	public void testFibonacci() {
		long start = System.currentTimeMillis();
		System.out.println(fibonacci(50));
		System.out.println("耗时："+ (System.currentTimeMillis() - start) + "毫秒");
	}
	
	/**
	 * 	 求斐波纳契数列 中第 n 个元素的值
	 * 	【自下而上的动态规划】
	 * @param n
	 * @return
	 */
	public int fibonacci(int n) {
		if(n < 1) {
			throw new RuntimeException("项数必须大于0");
		}
		// 我们创建一个长度为 n + 1 的数组，用来保存斐波纳契数列（0索引处不保存数据）
		int[] arr = new int[n + 1];
		for (int i = 1; i < arr.length; i++) {
			// 如果 i 小于 2 , 那么 arr[i] 等于定值 1
			if(i <= 2) {
				arr[i] = 1;
			}else {
				arr[i] = arr[i-1] + arr[i-2];
			}
		}
		return arr[n];
	}
	
	
	/**
	 * 	 求斐波纳契数列 中第 n 个元素的值
	 * 	【自下而上的动态规划】
	 * @param n
	 * @return
	 */
	public int fibonacci2(int n) {
		if(n < 1) {
			throw new RuntimeException("项数必须大于0");
		}
		if(n <= 2) {
			return 1;
		}
		int preNum1 = 1;
		int preNum2 = 1;
		int currNum = 1;
		int index = 3;
		while(index <= n) {
			currNum = preNum1 + preNum2;
			preNum1 = preNum2;
			preNum2 = currNum;
			index++;
		}
		return currNum;
	}
	
	
	/**
	 * 	普通的递归方法，当 n 的规模扩大时，性能会急剧下降
	 * 	 n = 10 时, 耗时：0毫秒
	 * 	 n = 20 时, 耗时：0毫秒
	 * 	 n = 30 时, 耗时：3毫秒
	 * 	 n = 40 时, 耗时：259毫秒
	 * 	 n = 45 时, 耗时：2830毫秒 
	 * 	 n = 47 时, 耗时：7407毫秒 
	 */
	@Test
	public void testGetFibonacci() {
		long start = System.currentTimeMillis();
		System.out.println(getFibonacci2(50, null));
		System.out.println("耗时："+ (System.currentTimeMillis() - start) + "毫秒");
	}
	
	/**
	 * 	返回 斐波纳契数列  第 n 项的值
	 * 	【普通的递归思路】
	 * @param index
	 * @return
	 */
	public int getFibonacci(int n) {
		if(n < 1) {
			throw new RuntimeException("项数不能小于1");
		}else if(n <= 2) {
			return 1;
		}else {
			return getFibonacci(n-1) + getFibonacci(n-2);
		}
	}
	
	/**
	 * 	返回 斐波纳契数列  第 n 项的值
	 * 	【备忘录法改进的递归方法】
	 * @param index
	 * @return
	 */
	public int getFibonacci2(int n, int[] arr) {
		if(arr == null) {
			arr = new int[n + 1];
			arr[1] = 1;
			arr[2] = 1;
		}
		if(n < 1) {
			throw new RuntimeException("项数不能小于1");
		}else if(n <= 2) {
			return arr[n];
		}else {
			// 如果指定项的值还没有求出来，我们就递归去求解
			// 如果指定项的值已经求出来，并保存到数组中了，我们就不再递归去求解了，直接返回数组保存的解就可以了
			if(arr[n] == 0) {
				return arr[n] = getFibonacci2(n-1, arr) + getFibonacci2(n-2, arr);
			}else {
				return arr[n];
			}
		}
	}
}
