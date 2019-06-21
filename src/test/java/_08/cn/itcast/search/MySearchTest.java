package _08.cn.itcast.search;

import java.util.Arrays;

import org.junit.Test;

import _07.cn.itcast.sort.ArrayUtils;
import _07.cn.itcast.sort.MySort;

public class MySearchTest {
	/**
	 * 线性查找算法对数组没有任何要求
	 */
	@Test
	public void testSequenceSearch() {
		int[] arr = ArrayUtils.generateRandomIntArray(10);
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.sequenceSearch(arr, 5);
		System.out.println(index);
	}
	
	/**
	 * 二分查找算法要求数组必须是有序的
	 */
	@Test
	public void testBinarySearch() {
		int[] arr = ArrayUtils.generateRandomIntArray(10);
		// 我们需要先对 arr 进行排序
		MySort.selectSort(arr);
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.binarySearchAsc(arr, 5);
		System.out.println(index);
	}
	
	/**
	 * 如果数组无序，那么二分查找可能会不起作用
	 */
	@Test
	public void testBinarySearch2() {
		int[] arr = {1, 5, 3, 2, 4, 6};
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.binarySearchAsc(arr, 5);
		System.out.println(index);
	}
	
	/**
	 * 如果数组有序，但是不是从小到大，那么我们之前写的那个二分查找算法就不起作用
	 * 我们得修改算法，才可以实现功能
	 */
	@Test
	public void testBinarySearch3() {
		int[] arr = {6, 5, 4, 3, 2, 1, 0};
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.binarySearchAsc(arr, 5);
		System.out.println(index);
	}
	
	@Test
	public void testBinarySearch4() {
		int[] arr = {6, 5, 4, 3, 2, 1, 0};
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.binarySearchDesc(arr, 5);
		System.out.println(index);
	}
	
	@Test
	public void testInterpolationSearchAsc() {
		int[] intArray = ArrayUtils.generateRandomIntArray(100);
		MySort.selectSort(intArray);
		System.out.println("原始数组：" + Arrays.toString(intArray));
		int index = MySearch.interpolationSearchAsc(intArray, 1);
		System.out.println(index);
	}
	
	@Test
	public void testInterpolationSearchDesc() {
		int[] intArray = ArrayUtils.generateRandomIntArray(100);
		MySort.selectSort(intArray);
		ArrayUtils.reverse(intArray);
		System.out.println("原始数组：" + Arrays.toString(intArray));
		int index = MySearch.interpolationSearchDesc(intArray, 1);
		System.out.println(index);
	}
	
	// 测试除零异常问题
	@Test
	public void testInterpolationSearch1() {
		int[] arr = {1, 1, 1};
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.interpolationSearchAsc(arr, 1);
		System.out.println(index);
	}
	
	// 测试索引越界问题
	@Test
	public void testInterpolationSearch2() {
		int[] arr = {1, 2, 3};
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.interpolationSearchAsc(arr, 1000);
		System.out.println(index);
	}
	
	// 测试只有一个元素的问题
	@Test
	public void testInterpolationSearch3() {
		int[] arr = {1};
		System.out.println("原始数组：" + Arrays.toString(arr));
		int index = MySearch.interpolationSearchAsc(arr, 1);
		System.out.println(index);
	}
	
	@Test
	public void testFibonacciSearch() {
		int[] intArray = ArrayUtils.generateRandomIntArray(10);
		MySort.selectSort(intArray);
		System.out.println("原始数组：" + Arrays.toString(intArray));
		int index = MySearch.fibonacciSearch(intArray, 1);
		System.out.println(index);
	}
	
	// 测试只有一个元素的时候，能不能正常处理
	@Test
	public void testFibonacciSearch2() {
		int[] intArray = {1};
		System.out.println("原始数组：" + Arrays.toString(intArray));
		int index = MySearch.fibonacciSearch(intArray, 1);
		System.out.println(index);
	}
}
