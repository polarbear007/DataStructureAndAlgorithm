package _18.cn.itcast.dynamicProgramming.napsackProblem;

import org.junit.Test;

public class _01NapsackProblem {
	@Test
	public void testNapsack() {
		int[] weight = {1, 4, 3, 2};
		int[] value = {1500, 3000, 2000, 5000};
		System.out.println(napsack(4, weight, value));
	}
	
	
	/**
	 * 	使用制表法，从下往上动态规划 01 背包问题
	 * @param capacity	表示 背包的容量
	 * @param weight	保存每个物品对应的重量
	 * @param value		保存每个物品的价格
	 */
	public int napsack(int capacity, int[] weight, int[] value) {
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
					// 再然后，我们根据剩下的空间，读取上一行，第 leftSpace 列的值（那是目前剩余空间为 leftSpace 的最优解） 
					newValue = dp[i-1][leftSpace] + value[i-1];
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
	
	
	@Test
	public void testNapsack2() {
		int[] weight = {1, 4, 3};
		int[] value = {1500, 3000, 2000};
		System.out.println(napsack2(5, weight, value));
	}
	
	/**
	 * 	从上往下递归处理的外层方法
	 * 	这个方法，我们接收正常的参数，然后创建 并初始化 记录状态的二维数组 dp
	 * @param capacity
	 * @param weight
	 * @param value
	 * @return
	 */
	public int napsack2(int capacity, int[] weight, int[] value) {
		if(capacity <= 0) {
			return 0;
		}
		// 为了防止每次递归方法都检查，我们把这些判断，也放在这个条件内部，最多只判断一次
		// weight 和 value 数组如果有一个为空，或者长度不一样，也直接return
		// 如果 weight 和 value 数组 的长度为0，那么我们也不处理
		if(weight == null || value == null) {
			throw new RuntimeException("weight 或者 value 数组不能为null");
		}else if(weight.length != value.length) {
			throw new RuntimeException("weight 或者 value 数组长度必须相等");
		}else if(weight.length == 0) {
			throw new RuntimeException("weight 或者 value 数组长度不能为0");
		}
		
		int[][] dp = new int[weight.length + 1][capacity + 1];
		
		// 初始化全部值为 -1， 第0行 和 第0列的值全部为0
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[i].length; j++) {
				if(i == 0 || j == 0) {
					dp[i][j] = 0;
				}else {
					dp[i][j] = -1;
				}
			}
		}
		
		return napsack(capacity, weight, value, dp, weight.length);
	}
	
	/**
	 * 	从上往下处理
	 * 	使用 备忘录法 优化普通递归产生的重复子问题，减少重复计算
	 * 	【备忘录法一定要使用二维数组来记录全部的子问题最优解】
	 * @param capacity	背包的容量
	 * @param weight	记录全部物品重量的数组
	 * @param value		记录全部物品价格的数组
	 * @param dp		记录状态的二维数组
	 * @param index		这是指定添加 第 index 索引处的物品（外层调用肯定是输入 weight.length）
	 * 					内层调用的时候，会将索引的指针向前推进
	 * @return
	 */
	private int napsack(int capacity, int[] weight, int[] value, int[][] dp, int index) {
		//我们需要把复杂的问题拆解成简单的问题，递归求最优解了
		// 如果背包的容量为0 ，或者小于0，我们不处理，直接 return 0;
		if(capacity == 0) {
			return 0;
		}else { 
			// 如果二维数组已经记录最优解，那么我们就直接返回最优解，不需要执行下面的方法递归计算了
			if(dp[index][capacity] != -1) {
				return dp[index][capacity];
			}
			
			// 如果背包的容量不为0，那么我们就需要判断一下 当前的 index 指向的物品重量是否能够放进背包
			// 如果当前物品小于背包的剩余容量，说明可以考虑放进背包
			if(weight[index - 1] <= capacity) {
				// 我们需要先计算一下，如果放进背包以后，背包的剩余空间大小 
				int leftSpace = capacity - weight[index - 1];
				// 我们需要根据剩余的空间大小 ，再去求这个剩余空间的最优解，加上 新添加的物品的价格
				// 因为是 01背包，所以添加了本物品，剩余空间的最优解不能再包含本物品， index  需要减1
				int newValue = value[index - 1] + napsack(leftSpace, weight, value, dp, index - 1);
				if(newValue > napsack(capacity, weight, value, dp, index - 1)) {
					return dp[index][capacity] = newValue;
				}else {
					return dp[index][capacity] = napsack(capacity, weight, value, dp, index - 1);
				}
			}else {
				// 如果当前物品大于背包的剩余容量，那么直接不考虑放进背包，使用上一次的策略
				return dp[index][capacity] = napsack(capacity, weight, value, dp, index - 1);
			}
		}
	}
}
