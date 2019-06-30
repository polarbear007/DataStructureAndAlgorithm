package _10.cn.itcast.tree;

/**
 * 	使用前面实现的二叉堆实现一个基本的 优先级队列，可以实现每次都优先删除优先级高的元素
 * 	【问题】 优先级相同的元素，无法实现先入先出。
 * @author Administrator
 *
 * @param <T>
 */
public class MyPriorityQueue<T extends Comparable<T>> {
	private BinaryHeap<T> heap;
	
	public MyPriorityQueue(Class<T> tpye) {
		this(tpye, 10);
	}

	public MyPriorityQueue(Class<T> type, int maxCapacity) {
		super();
		heap = new BinaryHeap<T>(type, maxCapacity);
	}
	
	public int getSize() {
		return heap.getSize();
	}
	
	public int getCapacity() {
		return heap.getCapacity();
	}
	
	public boolean isFull() {
		return heap.getSize() == heap.getCapacity();
	}
	
	/**
	 * 	添加元素到队列中，除非队列满了，一般都是可以添加成功的
	 * @param item
	 * @return
	 */
	public boolean addItem(T item) {
		// 虽然堆是可以自动扩容的，但是队列一般不需要扩容，所以我们这里再检查一下
		// 如果队列已经满了，那么我们就再执行 插入操作了
		if(isFull()) {
			return false;
		}else {
			heap.insert(item);
			return true;
		}
	}
	
	/**
	 * 	删除队列的元素
	 * 	队列的基本特点就是： 先入先出。  
	 * 	优先级队列的特点是： 如果优先级相同，就先入先出。如果优先级不同，那么优先删除优先级高的元素。
	 * 
	 * 	大顶堆的结构，虽然不是完全有序的，但是可以保证根结点是最大值。所以我们直接每次删除根结点，就可以实现优先级队列的需求。
	 * @return
	 */
	public T removeItem() {
		return heap.removeMax();
	}
	
	public T peek() {
		return heap.peek();
	}
	
	@Override
	public String toString() {
		return heap.toString();
	}
}
