package _28.cn.itcast.backTracking;

import org.junit.Test;

public class KTTest {
	// 我们运气好，直接在 210 毫秒内就找到一种解！
	@Test
	public void test() {
		KnightTourByBackTracking kt = new KnightTourByBackTracking(8, 0, 0);
		long start = System.currentTimeMillis();
		kt.solveKT();
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "毫秒");
	}
	
	// 但是使用相同的遍历顺序，只是换了一下起点，程序却运行了将近 10 分钟都没有得出一种有效的解。
	@Test
	public void test2() {
		KnightTourByBackTracking kt = new KnightTourByBackTracking(8, 2, 2);
		long start = System.currentTimeMillis();
		kt.solveKT();
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "毫秒");
	}
	
	@Test
	public void test3() {
		KnightTourByWarnsdorff kt = new KnightTourByWarnsdorff(9, 0, 0);
		long start = System.currentTimeMillis();
		kt.solveKT();
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "毫秒");
	}
}
