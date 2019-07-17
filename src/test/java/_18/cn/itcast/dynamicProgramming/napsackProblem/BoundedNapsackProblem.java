package _18.cn.itcast.dynamicProgramming.napsackProblem;

import org.junit.Test;

/**
 * 	前面我们已经介绍了01背包 和 完全背包， 现在我们再介绍一种背包： 多重背包
 * 	听名字可能觉得有点怪，其实多重背包就是在完全背包的基础上再加一条限制： 每个物品的数量并不是无限多个。
 * 	我们会再用另一个 quantities 数组来保存每个物品的数量限制
 * 
 * 	【说明】 其实完全背包应该改成跟这个一样的逻辑，这样思路才会统一。
 * 			不管什么背包，只要是放入本物品的剩余空间，我们一率不考虑再放入同样的物品。都去找上一行的最优解。
 * @author Administrator
 *
 */
public class BoundedNapsackProblem {
	@Test
	public void testNapsack() {
		int[] weight = {1, 4, 3};
		int[] value = {1500, 3000, 2000};
		int[] quantities = {3, 2, 2};
		System.out.println(boundedNapsack(6, weight, value, quantities));
	}
	
	public int boundedNapsack(int capacity, int[] weight, int[] value, int[] quantities) {
		// 如果背包的容量为0 ，或者小于0，我们不处理，直接 return 0;
		if(capacity <= 0) {
			return 0;
		}
		
		// 同样的道理， weight 和 value 数组如果有一个为空，或者长度不一样，也直接return
		// 如果 weight 和 value 数组 的长度为0，那么我们也不处理
		if(weight == null || value == null || quantities == null) {
			throw new RuntimeException("weight / value 或者 quantities 数组不能为null");
		}else if(weight.length != value.length || weight.length != quantities.length) {
			throw new RuntimeException("weight / value / quantities 数组长度必须相等");
		}else if(weight.length == 0) {
			throw new RuntimeException("weight / value / quantities 数组长度不能为0");
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
		
		// 初始化了 第 0 列 和第0行的数据以后，我们就可以开始考虑添加第一个物品了
		int leftSpace = 0;
		int newValue = 0;
		int maxValue = 0;
		// 再然后，我们就需要遍历并初始化 dp 数组了
		// 因为前面已经初始化完毕，所以这里直接跳过第1行，第1列
		for (int i = 1; i < dp.length; i++) {
			for (int j = 1; j < dp[i].length; j++) {
				// 【注意】第0行没有对应任何物品，我们是从第1行开始的，所以读取物品重量和价格时，记得索引 -1
				// 如果商品的重量小于背包剩余容量，那么可以考虑把这件物品添加到背包里面
				// 跟这里我们需要再额外考虑物品的数量是否大于0 ，如果物品的数量为0，就不需要考虑了
				if(weight[i - 1] <= j && quantities[i - 1] > 0) {
					// 完全背包不需要考虑每种物品的数量问题，可以无限多。
					// 01背包也不需要考虑，每种物品的数量固定为1
					// 而我们这里需要考虑可以给背包放多少个指定的物品
					// 这里我们只好使用遍历，分别假设给背包添加 k 个商品
					// 为了防止干扰，我们这里还是跟 01背包一样，剩余的空间不考虑同种物品，看上一行的最优解
					maxValue = dp[i-1][j];
					// 放入物品的数量 k ，从1开始算，0就是上一个策略，不需要重复计算
					for (int k = 1; k <= quantities[i-1]; k++) {
						// 先计算放入 k 个物品会不会超重
						leftSpace = j - k * weight[i-1];
						if(leftSpace >= 0) {
							// 剩余的空间大于等于0，说明可以放下物品，那么我们就可以计算newValue 的值了
							// 【注意】 因为我们统一考虑放入 k 个物品，所以你额外的空间就一定不要再放同种物品了
							//        所以剩余的空间还是放其他物品，去上一行找
							newValue = k * value[i-1] + dp[i-1][leftSpace];
							if(newValue > maxValue) {
								maxValue = newValue;
							}
						}else {
							// 如果leftSpace 为负数，那说明背包放不下 k 个商品，直接不考虑，后面也不考虑了
							break;
						}
					}
					// 最后， 我们只保存那个 maxValue
					dp[i][j] = maxValue;
				}else {
					// 如果要添加的物品重量超过背包的容量，或者物品数量为0，那么我们直接沿用上一次策略
					dp[i][j] = dp[i-1][j];
				}
			}
		}
		
		// 最后同样是返回最后一行的最后一个元素
		return dp[weight.length][capacity];
	}
}
