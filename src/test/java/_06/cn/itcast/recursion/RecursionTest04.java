package _06.cn.itcast.recursion;

import org.junit.Test;

/**
 *	 需求描述：
		假设现在有 ballsNum 个球，basketNum 个篮子，每个篮子最多可以放 basketSize 个球。
		要求我们写一个方法，根据输入的三个参数，返回放置球的组合个数。
		
		比如说：有2个球，2个篮子，每个篮子最多可以放2个球。那么可能的情况如下：(0, 2) (1, 1) (2, 0)，返回3个
		又比如说，有5个球，2个篮子，每个篮子最多放2个球，那么篮子放不下所有的球，返回 0 个
	
	这里给出另一种解法，这是老师的解法，使用一个长度为 basketNum 的一维数组，还可以通过 bas 输出每种组合方案本身。
 * @author Administrator
 *
 */
public class RecursionTest04 {
	@Test
	public void test() {
		FillBaskets fillBaskets = new FillBaskets();
		int result = fillBaskets.countWays(2, 2, 2);
		System.out.println(result);
	}
}

class FillBaskets {
	int count=0;
	int baskets,capacity,balls;
	int[] bas;
	public int countWays(int baskets, int capacity, int balls) {
		if (baskets*capacity<balls)
			return 0;
		this.baskets=baskets;
		this.balls=balls;
		if (balls<capacity)
			capacity=balls;
		this.capacity=capacity;
		bas=new int[baskets];
		
		//从第0个篮子开始放
		putBalls(0);
		return count;
	}
	
	//
	void putBalls(int n) {
		
		//检测是不是到达最后篮子
		if (n==baskets) {
			if (getSumBalls()==balls) {
				count++;
				
				String result = "[";
				for (int i=0;i<baskets;i++) {
					if(i == baskets -1) {
						result += bas[i];
					}else {
						result += ( bas[i] + ", ");
					}
				}
				result += "]";
				System.out.println(result);
			}
				
			return;
		}
		
		//给第n个篮子放球,并且从0->capacity
		//一次轮循的放入
		for (int i=0;i<=capacity;i++) {
			bas[n]=i;
			putBalls(n+1);
		}
	}
	//得到当前篮子里面所有的球
	int getSumBalls() {
		int sum=0;
		for (int i=0;i<baskets;i++) {
			sum+=bas[i];
		}
		return sum;
	}
}
