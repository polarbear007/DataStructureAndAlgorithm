package _06.cn.itcast.recursion;

import org.junit.Test;
/**
 *	 需求描述：
		假设现在有 ballsNum 个球，basketNum 个篮子，每个篮子最多可以放 basketSize 个球。
		要求我们写一个方法，根据输入的三个参数，返回放置球的组合个数。
		
		比如说：有2个球，2个篮子，每个篮子最多可以放2个球。那么可能的情况如下：(0, 2) (1, 1) (2, 0)，返回3个
		又比如说，有5个球，2个篮子，每个篮子最多放2个球，那么篮子放不下所有的球，返回 0 个
 * @author Administrator
 *
 */

public class RecursionTest03 {
	@Test
	public void test() {
		System.out.println(putBallsTask(8, 5, 4));
	}
	
	public int putBallsTask(int ballNum, int basketNum, int basketSize) {
		// 对参数进行一些简单的校验
		if(ballNum <= 0) {
			throw new RuntimeException("球的数量不能小于1个");
		}
		if(basketNum <= 0) {
			throw new RuntimeException("篮子的数量不能小于1个");
		}
		if(basketSize <= 0) {
			throw new RuntimeException("篮子的容量不能小于1个");
		}
		
		// 我们创建一个长度为 1 的数组，让所有的递归的函数共享此变量
		// 用来统计成功的方案个数 result[0]
		int[] result = new int[1];
		// 我们从编号第 0 个篮子开始放球
		putBallsToOneBasket(0, basketNum, basketSize, ballNum, result);
		
		return result[0];
	}
	
	// 往指定 basketId 的篮子里面添加球 
	// 当我们往一个篮子里面放球的时候，我们应该指定如下的参数
	// basketId 是篮子编号，篮子编号都是从 0 开始，然后往上递增，最后面的那个篮子编号为 basketNum - 1
	// basketNum 就是本次篮子的数量，这是为了判断递归的退出条件来指定的
	// basketSize 就是每个篮子的最大容量，我们传这个值的原因也是为了判断递归的退出条件
	// ballsLeft  就是当前剩余的球数，也是为了后面判断递归的退出条件
	public static void putBallsToOneBasket(int basketId, int basketNum, int basketSize, int ballsLeft, int[] result) {
		// 如果已经是最后一个篮子了，那么我们可以来看一下当前剩下的球是否能够全部放进这个篮子
		// 如果可以放进，那么就是可以分配； 如果无法放进全部球，那么就是不可以分配；
		if(basketId == basketNum - 1) {
			if(ballsLeft <= basketSize) {
				result[0]++;
			}
			// 最后停止此路径的模拟
			return;
		}else {
			// 为了优化一下效率，我们可以先检查一下剩余的篮子能不能装下全部剩余的球
			// 如果不能，我们可以直接记录一下错误次数，然后就返回了，省得再一下往下递归
			// 如果可以的话，那么我们就不能直接返回，就算你有 100 个篮子，只有一个球，这个球放在不同的篮子也算不同的组合
			// 剩下的篮子数量(包括当前的篮子)为  basketNum - basketId
			if((basketNum - basketId) * basketSize < ballsLeft) {
				return;
			}
			
			// 如果不是最后一个篮子的话，那么我们就要考虑再调用一次本方法，去封装下一个篮子
			// 关键在于本次我们会封装多少个球呢？  这里有两个限制因素： 篮子的最大容量 和 剩下的球的数量
			// 如果剩下的球的数量大于篮子最大容量，那么我们就有  0 - basketSize 这( basketSize + 1)种可能
			if(basketSize < ballsLeft) {
				for (int i = 0; i <= basketSize; i++) {
					// 这里会产生 basketSize + 1 条路径
					putBallsToOneBasket(basketId + 1, basketNum, basketSize, ballsLeft - i, result);
				}
			}else {
				// 如果剩下的球的数量小于篮子的最大容量，那么我们就有 0 - ballsLeft 这 (ballsLeft + 1)种可能
				for (int i = 0; i <= ballsLeft; i++) {
					// 这里会产生 ballsLeft + 1 条路径
					putBallsToOneBasket(basketId + 1, basketNum, basketSize, ballsLeft - i, result);
				}
			}
		}
	}
}

