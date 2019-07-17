package _18.cn.itcast.dynamicProgramming.napsackProblem;

import org.junit.Test;

/**
 * 	完全背包跟  01 背包最大的区别就在于，当尝试放入一个物品以后，还有剩余的空间时
 * 	因为物品是可以重复的，所以我们不需要找上一行的最优解，只需要在本行找就可以了。
 * 
 * 	【说明】
 * 		因为逻辑几乎一样，我们这里就只实现 填表法就好了，自上而下的递归方法我们就不再重复实现了
 * @author Administrator
 *
 */
public class UnboundedNapsackProblem {
	@Test
	public void testNapsack() {
		int[] weight = {1, 4, 3};
		int[] value = {1500, 7000, 5000};
		System.out.println(unboundedNapsack(4, weight, value));
	}
	
	/**
	 * 	使用从下往上的制表法来解决完全背包问题
	 * @param capacity	背包的最大容量
	 * @param weight	记录全部物品重量的
	 * @param value		记录全部物品的价格
	 * @return
	 */
	public int unboundedNapsack(int capacity, int[] weight, int[] value) {
		// 如果背包的容量为0 ，或者小于0，我们不处理，直接 return 0;
		if(capacity <= 0) {
			return 0;
		}
		
		// 同样的道理， weight 和 value 数组如果有一个为空，或者长度不一样，也直接return
		// 如果 weight 和 value 数组 的长度为0，那么我们也不处理
		if(weight == null || value == null) {
			throw new RuntimeException("weight 或者 value 数组不能为null");
		}else if(weight.length != value.length) {
			throw new RuntimeException("weight 或者 value 数组长度必须相等");
		}else if(weight.length == 0) {
			throw new RuntimeException("weight 或者 value 数组长度不能为0");
		}
		
		// 如果前面的检查都没有什么问题，我们就开始处理背包问题
		// 首先，我们创建一个记录状态的 二维数组, 内层数组长度为 背包最大容量加1，表示假设背包剩余容量。
		//    假如传入的参数为 5， 那么背包的剩余容量就有 0 、 1、 2、 3、 4、 5 这五种情况
		// 	外层数组的长度为 物品数量 + 1 。 之所以长度加1 ，是为了后面方便进行公式计算。
		int[][] dp = new int[weight.length + 1][capacity + 1];
		// 然后初始化第一行和第一列的全部元素值都为 0
		// （其实默认值就是 0，但是我们最好自己初始化一下。因为可能以后别的问题初始化值不是0）
		// 初始化第 0 列的值，全部为0
		for (int i = 0; i < dp.length; i++) {
			dp[i][0] = 0;
		}
		// 初始化第 0 行的值，全部为0
		for (int i = 0; i < dp[0].length; i++) {
			dp[0][i] = 0;
		}
		
		int leftSpace = 0;
		int newValue = 0;
		// 再然后，我们就需要遍历并初始化 dp 数组了
		// 因为前面已经初始化完毕，所以这里直接跳过第1行，第1列
		for (int i = 1; i < dp.length; i++) {
			for (int j = 1; j < dp[i].length; j++) {
				// 【注意】第0行没有对应任何物品，我们是从第1行开始的，所以读取物品重量和价格时，记得索引 -1
				// 如果商品的重量小于背包剩余容量，那么可以考虑把这件物品添加到背包里面
				if(weight[i - 1] <= j) {
					// 我们先计算添加这件商品到背包里面，背包还剩下多少空间
					leftSpace = j - weight[i-1];
					// 再然后，我们根据剩下的空间，读取本行的最优解（跟01背包最大的不同就在这里）
					// 【01背包】 newValue = dp[i-1][leftSpace] + value[i-1];
					newValue = dp[i][leftSpace] + value[i-1];
					// 现在我们需要比较一下， newValue 和 上一次的策略，背包的价格总额大小 
					// 如果本次添加以后，比上一次的策略价格要大，那么我们就确定加入 
					if(newValue > dp[i-1][j]) {
						dp[i][j] = newValue;
					}else {
						// 如果没有比上次大，那么我们就沿用上一次的策略
						dp[i][j] = dp[i-1][j];
					}
				}else {
					// 如果要添加的物品重量超过背包的容量，那么我们直接沿用上一次策略
					dp[i][j] = dp[i-1][j];
				}
			}
		}
		
		// 这个二维数组里面保存的都是最优解。 其实自下往上的处理策略，是不需要用二维数组保存的
		// 用一个一维数组保存也就够了。当我们查看最优解时，看的都是二维数组里面的最后一行。
		// 但是最好，还是使用二维数组，保存全部的数据
		return dp[weight.length][capacity];
	}
}
