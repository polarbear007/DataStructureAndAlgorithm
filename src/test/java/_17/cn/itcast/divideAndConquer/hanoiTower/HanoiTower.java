package _17.cn.itcast.divideAndConquer.hanoiTower;

import org.junit.Test;

public class HanoiTower {
	@Test
	public void testHanoi() {
		hanoi(3, 'A', 'B', 'C');
	}
	
	/**
	 * @param num	表示盘子的数量
	 * @param from	表示起点柱子
	 * @param asist	表示辅助的柱子
	 * @param dest	表示目的地	
	 */
	public void hanoi(int num, char from, char asist, char dest) {
		// 如果只有一个圆盘的话，那么我们直接放到目的地
		if(num == 1) {
			System.out.println("第"+ num +"个圆盘：" + from + "->" + dest);
		}else {
			// 如果有超过1 个圆盘，那么我们就需要先从 from 柱 把 num - 1 个圆盘先移动到asist柱子(此时 dest 柱子是辅助)
			hanoi(num - 1, from, dest, asist);
			// 再然后，我们就可以把最后一个圆盘放到 dest 柱子上
			System.out.println("第"+ num +"个圆盘：" + from + "->" + dest);
			// 再然后，我们就可以把 暂时放在 asist 柱子上的 num - 1 个盘子放到 dest柱子上（此时 from 柱子是辅助）
			hanoi(num - 1, asist, from, dest);
		}
	}
}
