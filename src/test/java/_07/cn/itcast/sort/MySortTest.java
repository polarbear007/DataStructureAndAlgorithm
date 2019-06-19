package _07.cn.itcast.sort;

import java.util.Arrays;

import org.junit.Test;

public class MySortTest {
	@Test
	public void testBubble() {
		int[] arr = {2, 4, 1, 5, 3, 3, 5, 6};
		MySort.bubbleSort(arr);
		System.out.println(Arrays.toString(arr));
	}
	
	@Test
	public void testBubble2() {
		int[] intArray = ArrayUtils.generateRandomIntArray(100);
		System.out.println("排序前： " + Arrays.toString(intArray));
		MySort.bubbleSort(intArray);
		System.out.println("排序后： " + Arrays.toString(intArray));
	}
	
	@Test
	public void testSelectSort() {
		int[] arr = {2, 4, 1, 5, 3, 3, 5, 6};
		MySort.selectSort(arr);
		System.out.println(Arrays.toString(arr));
	}
	
	@Test
	public void testSelectSort2() {
		int[] intArray = ArrayUtils.generateRandomIntArray(100);
		System.out.println("排序前： " + Arrays.toString(intArray));
		MySort.selectSort(intArray);
		System.out.println("排序后： " + Arrays.toString(intArray));
	}
	
	@Test
	public void compareSort() {
		int[] intArray = ArrayUtils.generateRandomIntArray(100000);
		long start = System.currentTimeMillis();
		MySort.bubbleSort(intArray);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,冒泡排序耗时：" + (System.currentTimeMillis() - start));
		
		intArray = ArrayUtils.generateRandomIntArray(100000);
		start = System.currentTimeMillis();
		MySort.selectSort(intArray);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,选择排序耗时：" + (System.currentTimeMillis() - start));
		
		intArray = ArrayUtils.generateRandomIntArray(100000);
		start = System.currentTimeMillis();
		MySort.insertSort(intArray);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,插入排序耗时：" + (System.currentTimeMillis() - start));
		
		intArray = ArrayUtils.generateRandomIntArray(10000000);
		start = System.currentTimeMillis();
		MySort.shellSort(intArray);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,希尔排序耗时：" + (System.currentTimeMillis() - start));
		
		intArray = ArrayUtils.generateRandomIntArray(10000000);
		start = System.currentTimeMillis();
		MySort.quickSort(intArray, 0, intArray.length - 1);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,快速排序耗时：" + (System.currentTimeMillis() - start));
	
		intArray = ArrayUtils.generateRandomIntArray(10000000);
		start = System.currentTimeMillis();
		MySort.mergeSort(intArray);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,归并排序耗时：" + (System.currentTimeMillis() - start));
		
		intArray = ArrayUtils.generateRandomIntArray(10000000);
		start = System.currentTimeMillis();
		MySort.radixSortByLSD(intArray);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,基数排序(最低位优先)耗时：" + (System.currentTimeMillis() - start));
		
		intArray = ArrayUtils.generateRandomIntArray(10000000);
		start = System.currentTimeMillis();
		MySort.radixSortByMSD(intArray);
		System.out.println("数据量:"+(intArray.length / 10000)+"w,基数排序(最高位优先)耗时：" + (System.currentTimeMillis() - start));
	}
	
	@Test
	public void testInsertSort() {
		int[] intArray = ArrayUtils.generateRandomIntArray(5);
		System.out.println("排序前：" + Arrays.toString(intArray));
		MySort.insertSort(intArray);
		System.out.println("排序后：" + Arrays.toString(intArray));
	}
	
	@Test
	public void testShellSort() {
		int[] intArray = ArrayUtils.generateRandomIntArray(10);
		System.out.println("排序前：" + Arrays.toString(intArray));
		MySort.shellSort(intArray);
		System.out.println("排序后：" + Arrays.toString(intArray));
	}
	
	@Test
	public void testQuickSort() {
		int[] intArray = ArrayUtils.generateRandomIntArray(10);
		System.out.println("原始数组：" + Arrays.toString(intArray));
		MySort.quickSort(intArray, 0, intArray.length - 1);
		System.out.println("排序后：" + Arrays.toString(intArray));
	}
	
	@Test
	public void testMergeSort() {
		int[] intArray = ArrayUtils.generateRandomIntArray(20);
		System.out.println("原始数组：" + Arrays.toString(intArray));
		MySort.mergeSort(intArray);
		System.out.println("排序后：" + Arrays.toString(intArray));
	}
	
	@Test
	public void testRadixSort() {
		int[] intArray = ArrayUtils.generateRandomIntArray(150);
		System.out.println("原始数组：" + Arrays.toString(intArray));
		MySort.radixSortByLSD(intArray);
		System.out.println("排序后：" + Arrays.toString(intArray));
	}
	
	@Test
	public void testRadixSort2() {
		int[] intArray = ArrayUtils.generateRandomIntArray(20);
		System.out.println("原始数组：" + Arrays.toString(intArray));
		MySort.radixSortByMSD(intArray);
		System.out.println("排序后：" + Arrays.toString(intArray));
	}

	// 基数排序速度很快，但是占用的内存会很多
	// 假设我们使用 LSD 对一个长度为 1亿的数组排序，那么至少需要
	// 源数组本身 + 10个长度等于源数组本身的桶数组
	// 10000 0000 * 11 * 4 / 1024 / 1024 / 1024 = 4.1 G 内存
	// MDS占用的内存空间会更大
	@Test
	public void testRadixSort3() {
		int[] intArray = ArrayUtils.generateRandomIntArray(100000000);
		MySort.radixSortByLSD(intArray);
	}
	
	// 其实我们前面实现的 基数排序并不完美
	// 如果数组中混入一个或者多个负数，那么会直接报 越界异常
	// 【注意】 这并不是基数排序的问题，而是我们在排序的时候，没有考虑负数的问题
	//       如果想要支持对负数的排序，那么我们可能就不能简单地根据余数找到对应的桶索引了
	//       预先创建的桶的数量至少要 20 个，这里我们知道一下就好了，暂时就不展开了
	@Test
	public void testRadixSort4() {
		int[] intArray = {1, 4, -3, 5};
		MySort.radixSortByLSD(intArray);
		System.out.println(Arrays.toString(intArray));
	}
}
