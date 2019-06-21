package _08.cn.itcast.search;

import java.util.Arrays;

public class MySearch {
	/**
	 * 线性查找其实也就是我们经常说的暴力查找 从头开始查找，返回第一个值等于目标值的元素的索引； 如果没有找到，返回 -1 优点： 不要求数组是有序的 缺点：
	 * 查找的效率比较低
	 * 
	 * @param arr
	 * @param value
	 * @return
	 */
	public static int sequenceSearch(int[] arr, int value) {
		if (arr == null) {
			throw new RuntimeException("数组不能为 null");
		}
		if (arr.length == 0) {
			return -1;
		}

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == value) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 二分查找 效率较高，但是要求数组是有序的, 而且是从小到大排序的
	 * 
	 * @param arr   源数组
	 * @param value 要查找的目标值
	 * @return 如果找到，返回索引值；如果没有找到，返回 -1
	 */
	public static int binarySearchAsc(int[] arr, int value) {
		if (arr == null) {
			throw new RuntimeException("数组不能为 null");
		}
		if (arr.length == 0) {
			return -1;
		}

		int low = 0;
		int high = arr.length - 1;
		int mid = 0;
		while (low <= high) {
			mid = (low + high) / 2;
			if (arr[mid] > value) {
				high = mid - 1;
			} else if (arr[mid] == value) {
				return mid;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	/**
	 * 二分查找 这个二分查找是专门处理从大到小排序的数组的
	 * 
	 * @param arr
	 * @param value
	 * @return
	 */
	public static int binarySearchDesc(int[] arr, int value) {
		if (arr == null) {
			throw new RuntimeException("数组不能为 null");
		}
		if (arr.length == 0) {
			return -1;
		}

		int low = 0;
		int high = arr.length - 1;
		int mid = 0;
		while (low <= high) {
			mid = (low + high) / 2;
			if (arr[mid] < value) {
				high = mid - 1;
			} else if (arr[mid] == value) {
				return mid;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	/**
	 * 插值查找算法 是对二分查找算法的一种改进思路 【要求】 数组有序，从小到大排序, 最好元素均匀分布，或者拼接均匀分布
	 * 适用于有序序列中元素分布均匀的情况，如果分布不均匀，那么插值查找算法并不能实现效率的提升
	 * 
	 * @param arr
	 * @param targetVal
	 * @return
	 */
	public static int interpolationSearchAsc(int[] arr, int targetVal) {
		if (arr == null) {
			throw new RuntimeException("数组不能为null");
		}
		if (arr.length == 0) {
			return -1;
		} else if (arr.length == 1) {
			return arr[0] == targetVal ? 0 : -1;
		}

		int low = 0;
		int high = arr.length - 1;
		int mid = 0;

		while (low < high) {
			System.out.println("hehe");
			// targetVal 如果已经超出了 arr[low] - arr[high] 的范围，那么下面的 mid 公式可能会创建索引越界
			// 比如说： arr = {1,2,3,4,5,6}
			// 第一次循环 mid = (targetVal - 1) * (5 - 0) / (6 - 1) + 0 = (targetVal - 1) * 5 / 6
			// 如果 targetVal 小于最小值 1 , 那么 targetVal - 1 会是一个负值，最后 mid 的值为 负数，于是 arr[mid] 就报错
			// 如果 targetVal 大于最大值6 ，比如说 targetVal 值为 13， 那么 12 * 5 / 6 = 10，于是 arr[mid] =
			// arr[10]同样报错
			if (targetVal < arr[low] || targetVal > arr[high]) {
				return -1;
			}
			// 我们还需要检查一下 arr[high] 是否等于 arr[low]
			// 如果相等，那么 mid 公式就会报除零异常
			// 比如说 ： arr = {1, 1, 1, 1}
			// tagetVal = 1
			// 第一次循环 mid = (1-1)*(3 - 0) / (1-1) + 0

			// 如果 arr[high] 等于 arr[low]，那么说明 low - high 之间的元素，值都是相等的
			// 如果 arr[low] 的值就是目标值，那么返回 low 索引，如果不是，说明全部都不是，返回 -1
			if (arr[low] == arr[high]) {
				return arr[low] == targetVal ? low : -1;
			}
			mid = (targetVal - arr[low]) * (high - low) / (arr[high] - arr[low]) + low;
			if (arr[mid] == targetVal) {
				return mid;
			} else if (arr[mid] > targetVal) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	/**
	 * 插值查找算法 【要求】 数组有序，从大到小排序, 最好元素均匀分布，或者拼接均匀分布
	 * 
	 * @param arr
	 * @param targetVal
	 * @return
	 */
	public static int interpolationSearchDesc(int[] arr, int targetVal) {
		if (arr == null) {
			throw new RuntimeException("数组不能为null");
		}

		if (arr.length == 0) {
			return -1;
		} else if (arr.length == 1) {
			return arr[0] == targetVal ? 0 : -1;
		}
		int low = 0;
		int high = arr.length - 1;
		int mid = 0;

		while (low < high) {
			System.out.println("hehe");
			// targetVal 如果已经超出了 arr[low] - arr[high] 的范围，那么下面的 mid 公式可能会创建索引越界
			if (targetVal > arr[low] || targetVal < arr[high]) {
				return -1;
			}
			// 我们还需要检查一下 arr[high] 是否等于 arr[low]
			// 如果相等，那么 mid 公式就会报除零异常
			if (arr[low] == arr[high]) {
				return arr[low] == targetVal ? low : -1;
			}
			mid = (targetVal - arr[low]) * (high - low) / (arr[high] - arr[low]) + low;
			if (arr[mid] == targetVal) {
				return mid;
			} else if (arr[mid] < targetVal) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	// 根据指定的长度，生成一个斐波纳契数组
	public static int[] getFibonacciArray(int length) {
		// 因为 int 值的范围问题，如果我们的斐波纳契数组长度大于 45，那么后面的数字可能就会出现负数
		// 所以我们数组的最大长度就设置到 45 就好了
		// 虽然无法刚好到达 int 的最大值，但是 fibonacciArr[44] = 11 3490 3170 , 11亿，已经是很大的数了
		// 一般来说，我们的数据量不会超过 11 亿
		if (length > 45) {
			length = 45;
		}
		int[] fibonacciArr = null;
		if (length > 1) {
			fibonacciArr = new int[length];
			fibonacciArr[0] = 1;
			fibonacciArr[1] = 1;
			for (int i = 2; i < fibonacciArr.length; i++) {
				fibonacciArr[i] = fibonacciArr[i - 1] + fibonacciArr[i - 2];
			}
		} else if (length == 1) {
			return new int[] { 1 };
		} else if (length == 0) {
			return new int[] {};
		}

		return fibonacciArr;
	}

	/**
	 * 斐波纳契查找算法，引入一个斐波纳契数列，然后让 mid 的索引值每次都等于
	 * 
	 * @param arr
	 * @param targetVal
	 * @return
	 */
	public static int fibonacciSearch(int[] arr, int targetVal) {
		if (arr == null) {
			throw new RuntimeException("数组不能为null");
		}
		// 处理原数组长度为1 时，low = high = k = 0
		// 如果我们 while(low < high) , 则不进入循环， 当 arr[0] 就是我们要找的目标值，会被忽略
		// 如果我们 while(low <= high), 进入循环，则 mid = low + fibArr[k-1] - 1; 会出现索引越界
		if (arr.length == 1) {
			return arr[0] == targetVal ? 0 : -1;
		}

		// 先获取一个斐波纳契数列，我们设置 45 ，获取 int 范围内的最大值
		int[] fibArr = getFibonacciArray(45);

		// 定义一些初始化参数
		int k = 0;
		int low = 0;
		int high = arr.length - 1;
		int mid = 0;

		// 根据 high 值 确认对应斐波纳契数列里面的最小值
		while (high > fibArr[k] - 1) {
			k++;
		}

		// 退出循环以后， fibArr[k] - 1 的值肯定大于或者等于 high
		// 再然后，我们就需要看 high 是否刚好等于 fibArr[k] - 1
		// 如果相等，我们直接使用 arr
		// 如果不相等，那么我们就创建一个长度跟 fibArr[k] - 1 一样的 temp 数组
		// 然后把arr 的所有值都复制到 temp 数组上面
		int[] temp = null;
		if (high == fibArr[k] - 1) {
			temp = arr;
		} else {
			// 把 arr 上面的值全部复制到 temp 上
			temp = Arrays.copyOf(arr, fibArr[k] - 1);
			// 如果我们自己创建一个 temp 数组，那么说明 temp 数组的长度肯定大于arr
			// 所以后面还会有一些元素是初始值0 ，我们建议把后面的值全部设置成 arr[high]
			for (int i = high + 1; i < temp.length; i++) {
				temp[i] = arr[high];
			}
		}

		// 现在 temp 数组肯定是符合斐波纳契查找的数组
		while (low < high) {
			// 当前数组长度对应的是 fibArr[k] - 1
			// 那么黄金分割点对应的就是 fibArr[k - 1] - 1
			// 当然，多次分割以后，我们都需要给这个黄金分割点加上一个 low
			// 第一次分割的 low 是0， 我们比较容易理解，如果起点不是 0 ,一定要加上low
			mid = low + fibArr[k - 1] - 1;
			// 然后比较 temp[mid] 跟 targetVal
			// 如果temp[mid] == targetVal ，那么说明 mid 的索引可能就是我们要找的索引
			if (temp[mid] == targetVal) {
				// 因为 temp 的长度比原始的 arr 要长，所以我们需要判断一下 mid 的值是否比 arr.length - 1 大
				if (mid > arr.length - 1) {
					return arr.length - 1;
				}
				// 否则，我们直接返回 mid 即可
				return mid;
			} else if (temp[mid] > targetVal) {
				// 如果 temp[mid] > targetVal ,说明 targetVal 位于 mid 左侧
				// 那么我们同样让 high = mid - 1 ， [low, mid - 1] 距离为 fibArr[k - 1] - 1
				// 其实按理来说，  high 应该等于 mid ，这样子 [low, mid] 距离为  fibArr[k - 1]
				// 这一段长度刚好又满足斐波纳契数列长度，所以分割点索引刚好等于 fibArr[k - 2] - 1
				// 要让下一次 mid = fibArr[k-1] - 1 = fibArr[k - 2] - 1
				// 这里需要 k -= 1
				high = mid - 1;
				k--;
			} else {
				// 如果 temp[mid] < targetVal, 说明 targetVal 位于 mid 右侧
				// 那么我们同样让 low = mid + 1, [mid+1, high] 距离为 fibArr[k - 2]
				// 这一段长度刚好又满足斐波纳契数列长度，所以分割点索引刚好等于 fibArr[k - 3] - 1
				// 要让下一次 mid = fibArr[k-1] - 1 = fibArr[k - 3] - 1
				// 这里需要 k -= 2
				low = mid + 1;
				k -= 2;
			}
		}
		return -1;
	}

	

}
