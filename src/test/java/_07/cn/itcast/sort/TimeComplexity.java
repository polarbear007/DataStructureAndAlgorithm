package _07.cn.itcast.sort;

import org.junit.Test;

public class TimeComplexity {
	// 当 n 增加，循环次数会相应增加， 语句的执行次数就会跟着增加
	public Integer getSum1(int n) {
		int sum = 0;
		for (int i = 0; i < n; i++) {
			sum += i;
		}
		return sum;
	}
	
	// 当 n 增加，语句的执行次数都是 1 ，不会有任何影响
	// 所以 这种算法肯定要比前面的好
	public Integer getSum2(int n) {
		return (1 + n) * n / 2;
	}
	
	
	@Test
	public void test() {
		System.out.println(aFunc(20L));
	}
	
	public long aFunc(long n) {    
	    if (n <= 1) {        
	        return 1;
	    } else {        
	        return aFunc(n - 1) + aFunc(n - 2);
	    }
	}
	
	
}
