package _07.cn.itcast.sort;

import java.util.Arrays;
import java.util.Random;

import _10.cn.itcast.tree.BinaryHeap;

public class MySort {
	// 冒泡排序法
	public static void bubbleSort(int[] arr) {
		int temp;
		boolean flag = false;
		// 外层循环只需要循环 n - 1 次
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr.length - i - 1; j++) {
				// 如果前面的元素比后面的大，那么我们就交换
				if (arr[j] > arr[j + 1]) {
					temp = arr[j + 1];
					arr[j + 1] = arr[j];
					arr[j] = temp;
					flag = true;
				}
			}
			// 如果本次内层循环有位置变动，那么 flag 的值为变成 true,
			// 我们再次把 flag 变成false, 继续下一次循环
			if (flag) {
				// System.out.println("还不能确认数组有序，可能需要继续循环");
				flag = false;
			} else {
				// 如果本次内层循环没有位置变动，那么 flag 的值为 false
				// 说明当前的数组已经有序，我们直接退出循环
				// System.out.println("已经能确认数组有序，直接退出循环");
				break;
			}
		}
	}

	// 选择排序法
	public static void selectSort(int[] arr) {
		int temp = 0;
		int minIndex = 0;
		for (int i = 0; i < arr.length - 1; i++) {
			// 先假定起始的元素就是最小元素
			minIndex = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[minIndex] > arr[j]) {
					minIndex = j;
				}
			}
			// 如果 minIndex 不等于 i ，那么说明剩下的元素中有 arr[i] 元素小的
			// 那么就交换位置
			// 如果相等的话，那么我们就什么都不处理
			if (minIndex != i) {
				temp = arr[minIndex];
				arr[minIndex] = arr[i];
				arr[i] = temp;
			}
		}
	}

	// 插入排序法
	public static void insertSort(int[] arr) {
		if (arr.length <= 1) {
			return;
		} else {
			int insertValue = 0;
			int insertIndex = 0;
			for (int i = 1; i < arr.length; i++) {
				// 先使用 insertValue 保存当前遍历的元素值
				insertValue = arr[i];
				// insertIndex 的初始值为 i， 先假设要插入的位置就是 i
				insertIndex = i;

				// 如果保存起来的 insertValue 小于 inserIndex 索引前一位对应的元素值，那么说明
				// insertIndex 值不对，我们应该把插入的位置往前移
				// insertIndex 想要往前移，那么 insertIndex 前一位的元素就应该往后移一位

				// 为了保证 insertIndex-- 以后索引不越界，我们要添加这个条件 insertIndex - 1 >= 0
				while (insertIndex - 1 >= 0 && arr[insertIndex - 1] > insertValue) {
					// 把 inserIndex - 1 对应的元素保存到 insertIndex 位置
					arr[insertIndex] = arr[insertIndex - 1];
					// 然后 indexIndex 减1
					insertIndex--;
				}

				// 只要退出循环了， insertIndex 就是插入的位置
				arr[insertIndex] = insertValue;
			}
		}
	}

	// 希尔排序法
	public static void shellSort(int[] arr) {
		if (arr != null && arr.length > 1) {
			int gap = arr.length / 2;
			int insertValue = 0;
			int insertIndex = 0;
			while (gap != 0) {
				// 原数组被分割成 gap 组，每一组的起始索引是 i ，元素间隔是 gap, 终点是可以等于 arr.length - gap + i
				// 如果不理解，可以举个长度比较小的例子，然后代入式子去体会
				for (int i = 0; i < gap; i++) {
					// 虽然每组的起始索引是 i ，但是插入排序是从第二个元素开始排的，所以我们直接从 i + gap 索引开始
					// i 索引对应的元素认为是已经排好了
					for (int j = i + gap; j <= arr.length - gap + i; j += gap) {
						// 先保存一下要插入的值
						insertValue = arr[j];
						// 先假设要插入的索引就是 j
						insertIndex = j;

						// 然后让要插入的值，跟要插入的索引位置的前一个元素比较
						// 如果小于前面的元素，那么说明我们一开始的假设是错的，需要把 insertIndex 往前移
						// insertIndex 想要往前移，你就要先让前一个元素往后移
						// 【注意】 这里的移动，都是以 gap 为单位
						// 为了保证 insertIndex -= gap 以后不越界，我们需要添加此条件 insertIndex - gap >= 0
						while (insertIndex - gap >= 0 && insertValue < arr[insertIndex - gap]) {
							arr[insertIndex] = arr[insertIndex - gap];
							insertIndex -= gap;
						}
						// 如果退出了 while 循环，说明 insertIndex 就是我们想要插入的位置
						if (insertIndex != j) {
							arr[insertIndex] = insertValue;
						}
					}
				}
				// 缩小增量
				gap = gap / 2;
			}
		}
	}

	// 快速排序--- 挖坑法实现
	public static void quickSort(int[] arr, int low, int high) {
		if (low < high) {
			int i = low;
			int j = high;
			int key = arr[i];
			// 只要 i 不大于 j ,我们就继续循环
			while (i < j) {
				// 一下递减，直到找到一个比 key 还小的值
				// 当然，我们得保证 j > i,否则直接跳出循环
				while (j > i && arr[j] >= key) {
					j--;
				}
				// 如果上面是因为找到比基准值小的元素而退出循环，我们就把
				// 这个比基准值小的元素保存到 arr[i] 的位置，同时把 i 索引向后推进一位，
				// 因为现在 arr[i] 肯定比基准值要小，再检查一遍就是浪费性能
				// 不要担心 arr[i] 位置的元素值会被覆盖，因为 最开始我们有 key 保存基准值
				// 后面的循环中，arr[i] 的值会被保存到 arr[j]
				if (arr[j] < key) {
					arr[i] = arr[j];
					i++;
				}

				while (j > i && arr[i] <= key) {
					i++;
				}

				// 同样的道理，如果上面的循环是因为找到比 基准值大的元素
				// 那么我们就把这个比基准值大的元素保存到 arr[j]， 同时把 j 索引向前推一位
				if (arr[i] > key) {
					arr[j] = arr[i];
					j--;
				}
			}
			// 最后，我们最开始拿出来的基准值 key ，保存到 i 或者 j 位置上去
			// 现在 i 和 j 的值肯定是相等的
			// 如果 i 和 j 的值等于 low 的话，那么说明根本就没有出现任何值交换的操作，
			// arr[low] 就是最小值，那么我们也不需要再赋值了
			if (i != low) {
				arr[i] = key;
			}
			quickSort(arr, low, i - 1);
			quickSort(arr, j + 1, high);
		}
	}

	// 快速排序的另一种实现--- 指针交换法
	public static void quickSort2(int[] arr, int low, int high){
		if(arr == null) {
			throw new RuntimeException("数组不能为 null");
		}
		if(low >= high) {
			return;
		}
		
		int l = low;
		int h = high;
		int temp = 0;
		
		// 随机选取一个基准值，防止一直选取到极端值作为基准值
		int pivot = arr[new Random().nextInt(high - low) + low + 1];
		
		while(l < h) {
			// 从左侧开始找，找到一个大于或者等于 pivot 的值才会停下来
			// 最差就是找到跟 pivot 相等的值
			// 如果 arr[l] < pivot ,那么我们就找下一个， l++
			while(arr[l] < pivot) {
				l++;
			}
			
			// 从右侧开始找，找到一个小于或者等于 pivot 的值才会停下来
			// 最差就是找到跟 pivot 相等的值
			// 如果 arr[h] > pivot ，那么我们就找下一个，h--
			while(arr[h] > pivot) {
				h--;
			}
			
			// 这里可能理解起来比较困难，上面的操作可以保证下面两点
			// [low, l)  区间的全部元素都小于 pivot， 不包含 l
			// (h, high] 区间的全部元素都大于 pivot ，不包含 h
			
			// 如果相等，循环外部会另外处理
			// 如果不相等, 有下面两种情况：
			// l > h ，这是最正常的情况，说明该交换都交换过了,直接不处理，下次循环会直接退出
			// 如果 l < h ，说明还是可以正常交换的，执行下面的交换操作
			if(l < h) {
				temp = arr[l];
				arr[l] = arr[h];
				arr[h] = temp;
				l++;
				h--;
				System.out.println(Arrays.toString(arr));
			}
		}
		if(l == h) {
			// 如果刚好 arr[l] == arr[h] == pivot
			// 那么这个值可以不参与排序，这是最理想的情况
			// 我们什么都不处理
			if(arr[l] > pivot) {
				h--;
			}else if(arr[l] < pivot){
				l++;
			}
		}
		
		//  最终数组分割的情况，请参考笔记，这里注释已经不想再写一遍了
		if(low < l - 1) {
			quickSort(arr, low, l - 1);
		}
		if(high > h + 1) {
			quickSort(arr, h + 1, high);
		}
	}
	// 归并排序
	public static void mergeSort(int[] arr) {
		if (arr == null) {
			throw new RuntimeException("数组不能为空！");
		}
		if (arr.length > 1) {
//			int[] newArr = mergeSort(arr, 0, arr.length - 1);
//			// 把排序后的数组复制到原数组上面去
//			System.arraycopy(newArr, 0, arr, 0, newArr.length);
			mergeSort(arr, 0, arr.length - 1, new int[arr.length]);
		}
	}

	// 归并排序的另一种思路
	// 这种思路可以让所有的递归的方法共享一个 temp 数组，不必大量创建 临时数组
	private static void mergeSort(int[] arr, int low, int high, int[] temp) {
		if (low == high) {
			// 如果 low == high ，那么我们根本不需要分割，也不需要合并
			// 更不需要用到 temp
			return;
		}
		int mid = (low + high) / 2;
		// 如果 low 和 high 不相等，那么我们就再分组，分别处理
		mergeSort(arr, low, mid, temp);
		mergeSort(arr, mid + 1, high, temp);

		// 前面已经分组处理好了，虽然没有返回值，但是上一步的处理，可以保证
		// 源数组arr 中两个对应的子序列已经是有序的了
		// 之所以上一步的处理可以保证 arr 中子序列有序的原因就是靠下面的代码实现的
		// 现在我们完全可以假定， arr[low] - arr[mid] 和 arr[mid + 1] - arr[high]
		// 这两段子序列是有序的，我们想要把这两段子序列暂时保存到 temp 上排好序，再统一复制到 arr 上面
		int i = low;
		int j = mid + 1;
		// 这里我们想要直观一点，所以从 low 开始， high 结束
		// 当然，你要从 0 开始 ，high - low 结束也是可以的
		for (int m = low; m <= high; m++) {
			// 如果 i 大于 mid ，那么说明左子序列已经全部取完了
			// 我们可以直接把右子序列剩余的全部元素放进 temp 中
			if (i > mid) {
				while (m <= high) {
					temp[m] = arr[j++];
					m++;
				}
			} else if (j > high) {
				// 如果 i 大于 mid ，那么说明左子序列已经全部取完了
				// 我们可以直接把右子序列剩余的全部元素放进 temp 中
				while (m <= high) {
					temp[m] = arr[i++];
					m++;
				}
			} else if (arr[i] < arr[j]) {
				temp[m] = arr[i++];
			} else {
				temp[m] = arr[j++];
			}
		}

		// 现在我们已经把两个有序的子序列合并成一个有序的序列了
		// 只不过保存在 temp 上面，我们希望把这段有序的序列再复制到 arr 上面
		// 因为 temp 和 arr 上面的索引都是对应的，所以我们直接 System.arrayCopy()
		System.arraycopy(temp, low, arr, low, high - low + 1);
	}

	// 私有的归并排序， 因为归并排序需要创建新的数组，但是我们希望跟其他排序一样
	// 所以在私有方法外面再套一层
	private static int[] mergeSort(int[] arr, int low, int high) {
		if (low == high) {
			return new int[] { arr[low] };
		}
		int mid = (low + high) / 2;
		// 如果 low 和 high 不相等，那么我们就再分组，分别处理
		int[] leftArr = mergeSort(arr, low, mid);
		int[] rightArr = mergeSort(arr, mid + 1, high);

		// 现在我们需要创建一个长度为 high - low + 1 的数组，把两个数组合并起来
		int[] tempArr = new int[high - low + 1];
		// 声明两个索引，用于遍历 leftArr 和 rightArr
		int i = 0;
		int j = 0;
		for (int k = 0; k < tempArr.length; k++) {
			// 左序列已经遍历完了，那么我们也不需要再遍历外层了，直接依次把右序列的元素都添加
			if (i > leftArr.length - 1) {
				while (k < tempArr.length) {
					tempArr[k] = rightArr[j++];
					k++;
				}
				// tempArr[k] = rightArr[j++];
				// 右序列已经遍历完了，那么我们也不需要再遍历外层了，直接依次把左序列的元素都添加
			} else if (j > rightArr.length - 1) {
				while (k < tempArr.length) {
					tempArr[k] = leftArr[i++];
					k++;
				}
				// tempArr[k] = leftArr[i++];
			} else if (leftArr[i] < rightArr[j]) {
				tempArr[k] = leftArr[i++];
			} else {
				tempArr[k] = rightArr[j++];
			}
		}
		return tempArr;
	}

	/**
	 *  因为最高位优先需要递归，我们在外面再包一层方法，这样看起来就跟其他的排序方法没有什么不同
	 * @param arr
	 */
	public static void radixSortByMSD(int[] arr) {
		if(arr == null) {
			throw new RuntimeException("数组不能为null");
		}
		// 注意： 如果最大值是3位数，那么 getMaxLoop(arr) 返回的是3
		// 但是，方法内部调用的   getDigitNum(int source, int digit)  里面的 digit 
		//  2 表示的是百位数，所以我们这里传参的时候，需要传 getMaxLoop(arr) - 1 （这个没统一好，懒得改）
		radixSortByMSD(arr, 0, arr.length - 1,  getMaxLoop(arr) - 1);
	}
	
	
	// 基数排序 MSD 实现
	// 这个方法需要递归
	// 我们为了让下层方法可以直接操作上层方法的桶元素，这里指定了 low 和 high ，而不是传入一个新的数组
	private static void radixSortByMSD(int[] arr, int low, int high, int digit) {
		// 如果传过来的桶里面只有一个有效元素，那么直接返回
		if(high - low <= 0) {
			return;
		}
		// 如果桶有效元素数量超过1 , 那么我们先创建 10个桶及另外一个一维数组，用来保存每个桶实际保存元素个数
		// 不管怎么说，虽然下层方法可以直接操作上层方法的桶元素，但是还是得新创建一组桶来实现排序
		//  【创建新的栈 + 创建新的子桶】，所以说 MSD是比较占用内存的。
		int[][] buckets = new int[10][high - low + 1];
		int[] elementsCounts = new int[10];
		// digitValue 保存的是指定位数上的值
		int digitValue = 0;
		
		// 遍历原数组，根据指定的位数上的值分配到不同的桶，记得维护 elementsCounts
		for (int i = low; i <= high; i++) {
			digitValue = getDigitNum(arr[i], digit);
			buckets[digitValue][elementsCounts[digitValue]] = arr[i];
			elementsCounts[digitValue]++;
		}
		
		// 再然后，如果桶里面的元素个数大于 1 的，那么可能还需要根据下一位数去排序
		// 当然，如果当前已经是根据 个位数排序了(也就是 digit == 0)，那么就没有什么下一层了，我们就不要再往下递归了
		if(digit != 0) {
			for (int i = 0; i < elementsCounts.length; i++) {
				if(elementsCounts[i] > 1) {
					// 向下一层递归，我们需要再考虑桶里面的元素并不是全部都是有效元素的问题
					// 比如说一个桶长度为  10， 里面元素值为：  {1, 2, 3, 5, 0, 0, 0} 
					// 而实际有效的元素可能只有前两个，具体要以  elementsCounts[i] 为准
					// 然后调用自身，让桶里面的有效元素根据下一位数排序
					radixSortByMSD(buckets[i], 0, elementsCounts[i] - 1, digit - 1);
				}
			}
		}
		
		// 高位优先，所有方法都共用了最开始的桶，所以我们可以在所有的子桶都排好序以后，
		// 再统一遍历桶，到原始数组上
		if(low == 0) {
			int tempIndex = low;
			// 前面的递归可以保证虽然 我们只是根据最高位进行排序，但是桶里面的元素都是有序的
			// 所以我们只管遍历全部的桶，把桶里面的数据保存到原数组中即可
			for (int m = 0; m < buckets.length; m++) {
				for (int n = 0; n < elementsCounts[m]; n++) {
					arr[tempIndex++] = buckets[m][n];
				}
				// 当我们遍历完一个桶的元素以后，我们就可以把这个桶的数量设置成 0
				// 初始化一下，方便下次再使用
				elementsCounts[m] = 0;
			}
		}
	}

	// 基数排序 LSD 实现
	public static void radixSortByLSD(int[] arr) {
		if (arr == null) {
			throw new RuntimeException("数组不能为 null");
		}
		// 数组的长度如果小于等于1，那么也不需要排序
		if (arr.length <= 1) {
			return;
		}

		// 如果前面的检查都通过了，那么我们首先可以根据数组的长度，创建十个桶
		// 每个桶的长度为 数组长度。 其实所有的桶刚好组成一个二维数组。
		// 每个桶的索引，刚才表示其对应的编号
		int[][] buckets = new int[10][arr.length];
		// 因为我们使用的是二维数组保存，所以我们还得维护一个一维数组，来保存每个桶实际保存的元素个数
		// 这个数组的索引跟桶的编号是对应的。比如 elementsCounts[0] 保存的是第 0 号桶的元素数量
		// 或者也可以这么理解： elementsCounts[0] 保存的是第 0 号桶下次要保存的索引
		int[] elementsCounts = new int[10];

		// 然后我们应该遍历 arr ，拿到最大值的位数，确定总体循环的次数
		int maxLoop = getMaxLoop(arr);

		// 这个变量用来保存每个元素取到的指定位数上的值
		int digitValue = 0;
		// 这个变量用来保存临时索引，当遍历全部桶，把数据保存回原数组时可以使用
		int tempIndex = 0;

		// 总体的循环 maxLoop 次
		for (int i = 0; i < maxLoop; i++) {
			// 每次循环，我们先遍历原数组，然后根据 i 的值，确定除以几次 10 ，再对 10 取模
			for (int j = 0; j < arr.length; j++) {
				// 根据 i 和 遍历到的数组元素，拿到其对应的位数值。 这个位数值可以确定我们要放到哪个桶里
				digitValue = getDigitNum(arr[j], i);
				// 根据 digitValue 的值，我们需要把这个数组元素保存到指定的桶的指定位置上去
				// 保存以后 elementsCounts[digitValue] 数量记得加1
				buckets[digitValue][elementsCounts[digitValue]] = arr[j];
				elementsCounts[digitValue]++;
			}

			// 上面的 for 循环实际上就是按要求把数组的元素都保存到桶中，实现按指定位数的值排序
			// 现在我们要遍历全部的桶，再把桶中的数据按顺序再保存回数组
			for (int m = 0; m < buckets.length; m++) {
				for (int n = 0; n < elementsCounts[m]; n++) {
					arr[tempIndex++] = buckets[m][n];
				}
				// 当我们遍历完一个桶的元素以后，我们就可以把这个桶的数量设置成 0
				// 初始化一下，方便下次再使用
				elementsCounts[m] = 0;
			}
			// 一次排序过后，我们要重置一下 tempIndex
			tempIndex = 0;
		}
	}

	/**
	 * 根据传入的参数和位数 返回指定位数的值
	 * 
	 * @param source 初始值
	 * @param digit  位数，0表示个位，1表示十位，2表示百位，以此类推
	 * @return
	 */
	private static int getDigitNum(int source, int digit) {
		while (digit != 0) {
			source /= 10;
			digit--;
		}
		return source % 10;
	}

	/**
	 * 根据传入的数组的最大值，返回桶排序总体需要循环的次数
	 * 
	 * @param arr
	 * @return
	 */
	private static int getMaxLoop(int[] arr) {
		int max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		int count = 0;
		while (max != 0) {
			max /= 10;
			count++;
		}
		return count;
	}

	/**
	 * 	推排序
	 * @param arr
	 */
	public static void heapSort(int[] arr) {
		if(arr != null && arr.length > 1) {
			int len = arr.length;
			// 把无序的数组初始化成一个符合堆结构的数组
			for (int i = (len - 2) / 2; i >= 0; i--) {
				sink(arr, i, len);
			}
			
			while(len > 1) {
				exchange(arr, 0, len - 1);
				len--;
				sink(arr, 0, len);
			}
		}
	}
	
	private static void sink(int[] arr, int rootIndex, int len) {
		int leftIndex = 0;
		int rightIndex = 0;
		int maxSonIndex = 0;
		while(true) {
			leftIndex = rootIndex * 2 + 1;
			rightIndex = rootIndex * 2 + 2;
			if(rightIndex <= len - 1) {
				maxSonIndex = arr[leftIndex] - arr[rightIndex] > 0 ? leftIndex : rightIndex;
			}else if(leftIndex == len - 1) {
				maxSonIndex = leftIndex;
			}else {
				break;
			}
			
			// 如果前面没有退出循环，说明 rootIndex 还有子结点，我们就和子结点比较
			//   如果小于子结点，我们就交换， 交换完以后， rootIndex 更新为  maxSonIndex； 
			//   如果不小于子结点，我们就停止循环
			if(arr[rootIndex] < arr[maxSonIndex]) {
				exchange(arr, rootIndex, maxSonIndex);
				rootIndex = maxSonIndex;
			}else {
				break;
			}
		}
	}
	
	private static void exchange(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
