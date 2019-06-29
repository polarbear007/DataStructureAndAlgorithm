package _10.cn.itcast.tree;

import java.util.Arrays;

import org.junit.Test;

public class BinaryHeapTest {
	@Test
	public void testConstructor() {
		BinaryHeap<Integer> heap1 = new BinaryHeap<Integer>(Integer.class);
		System.out.println(heap1.getCapacity());
		System.out.println(heap1.getDataArray().length);
		
		BinaryHeap<Integer> heap2 = new BinaryHeap<Integer>(Integer.class, 20);
		System.out.println(heap2.getCapacity());
		System.out.println(heap2.getDataArray().length);
		
		Integer[] arr = {1, 2, 4};
		BinaryHeap<Integer> heap3 = new BinaryHeap<Integer>(Integer.class, arr);
		System.out.println(heap3.getCapacity());
		System.out.println(heap3.getDataArray().length);
	}
	
	// 说明，我们暂时还没有写  heapify() 方法， 所以我们就手动先初始化一个符合大顶堆规则的数组
	// 一个降序排序的数组肯定是符合大顶堆
	// 为了演示上浮的效果，我们故意在数组的最后加入一个比较大的值，看看能不能上浮到指定位置
	@Test
	public void testSwim() {
		Integer[] arr = {8, 7, 6, 5, 4, 3, 2, 1, 10};
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class, arr);
		heap.swim(8);
		System.out.println(Arrays.toString(arr));
	}
	
	
	// 跟上面一样，我们暂时只能先手动初始化一个符合大顶堆规则的数组
	// 为了演示下沉效果，我们故意把 arr[0] 改成一个比较小的值，看看能不能下沉到指定位置
	@Test
	public void testSink() {
		Integer[] arr = {3, 8, 7, 6, 5, 4, 3, 2, 1};
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class, arr);
		heap.sink(0);
		System.out.println(Arrays.toString(arr));
	}
	
	@Test
	public void testHeapify() {
		Integer[] arr = {8, 5, 10, 6, 7, 3, 8, 1, 4};
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class, arr);
		System.out.println(Arrays.toString(arr));
	}
	
	@Test
	public void testRemoveMax() {
		Integer[] arr = {8, 5, 10, 6, 7, 3, 8, 1, 4};
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class, arr);
		System.out.println(Arrays.toString(arr));
		Integer result = heap.removeMax();
		System.out.println("被删除的元素：" + result);
		System.out.println(Arrays.toString(arr));
	}
	
	@Test
	public void testInsert() {
		Integer[] arr = {8, 7, 6, 5, 4, 3, 2, 1};
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>(Integer.class, arr);
		// 初始化后的数组
		System.out.println(Arrays.toString(heap.getDataArray()));
		// 添加一个新元素以后，再看看数组长度、元素排列有没有变化 
		heap.insert(10);
		System.out.println(Arrays.toString(heap.getDataArray()));
		// 再添加一个新元素，看看数组长度和元素的排列有没有变化
		heap.insert(20);
		System.out.println(Arrays.toString(heap.getDataArray()));
	}
}
