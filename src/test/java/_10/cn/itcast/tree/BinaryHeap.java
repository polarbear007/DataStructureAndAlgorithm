package _10.cn.itcast.tree;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 	下面的二叉堆具有如下的几个主要方法：
 * 	1、 heapify()  把一个无序的数组初始化成一个符合大顶堆规则的数组。
 * 	2、swim()     	对指定位置的元素进行上浮操作，使之浮动到合适的位置
 * 	3、 sink() 		对指定位置的元素进行下沉操作，使之下沉到合适的位置
 * 	4、 removeMax()   删除堆中的最大值，其实就是删除根结点，删除以后，堆的长度会减1，我们需要重新维护大顶堆
 * 	5、 insert()		 添加一个新的元素
 * 	6、 ensureCapacity()  当数组长度不够时，自动扩容
 * 	7、 toString()		 打印堆（我们就按打印基本的数组那样处理就好了）
 * 
 * 	【说明】
 * @author Administrator
 *
 * @param <T>
 */
public class BinaryHeap<T extends Comparable<T>> {
	// java 不支持泛型数组，所以构造方法我们还是得费点劲，传个类型进来才可
	private T[] dataArray;
	private int capacity;
	private int size;
	
	public BinaryHeap(Class<T> type) {
		init(type, 10, null);
	}
	
	public BinaryHeap(Class<T> type, int capacity) {
		init(type, capacity, null);
	}
	
	public BinaryHeap(Class<T> type, T[] dataArray) {
		init(type, null, dataArray);
	}
	
	/**
	 * 初始化方法
	 * @param capacity
	 * @param dataArray
	 */
	@SuppressWarnings("unchecked")
	private void init(Class<T> type, Integer capacity, T[] dataArray) {
		if(dataArray != null) {
			this.dataArray = dataArray;
			this.capacity = dataArray.length;
			this.size = dataArray.length;
			
			// 传入了 dataArray 以后，我们需要把无序的  dataArray 转成大顶 堆
			heapify(dataArray);
			
		}else if(capacity != null) {
			this.capacity = capacity;
			this.dataArray = (T[])Array.newInstance(type, capacity);
		}else {
			this.capacity = 10;
			this.dataArray = (T[])Array.newInstance(type, this.capacity);
		}
	}
	
	// 按理说是不能让外部获取这个 dataArray 的，但是我们为了测试构造方法，这里先放开
	// 后面可以写成 private ，或者删除
	public T[] getDataArray() {
		return dataArray;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getSize() {
		return size;
	}

	/**
	 * 	把无序的数组初始化成大顶堆
	 * 	【思路】
	 * 		1、 从左到右，执行 swim() 方法。因为swim() 方法都是跟父结点比，所以可以从 arr[1] 开始，总共上浮次数需要   n - 1 次；
	 * 		2、 从右到左，执行 sink() 方法。 因为 sink() 方法都是跟子结点比，所以可以从最后一个非叶结点开始， 假设总的元素个数为 n
	 * 			那么最后一个元素的索引为 n - 1, 那么最后一个非叶结点的索引为  (n - 2) / 2。 总共下沉次数需要 (n - 2) / 2，
	 * 			少了将近一半。
	 * @param dataArray
	 */
	private void heapify(Object[] dataArray) {
		// 使用下沉法
		for (int i = (size - 2) / 2; i >= 0; i--) {
			sink(i);
		}
		// 使用上浮法
//		for (int i = 1; i <= size - 1; i++) {
//			swim(i);
//		}
	}
	
	/**
	 * 	调用 swim() 方法的时候，除了 targetIndex 以外所有的元素都符合大顶堆的规则 
	 * 	我们拿 targetIndex 的这个元素依次跟父结点比较，如果大于父结点，就上浮；上浮以后，如果还是大于父结点，就再次上浮...
	 * 	一直到 targetIndex 小于其父结点、 或者 targetIndex == 0 ，也就是变成根结点为止。
	 * 
	 * 	【作用】上浮操作可以保证 [0, targetIndex] 区间的元素满足大顶堆的要求
	 * 
	 *	 使用场景：
	 * 	一般是添加新元素的时候，调用这个方法，一直跟父结点比较，如果大于父结点，就上浮到合适的位置。
	 * @param index
	 */
	public void swim(int targetIndex) {
		T temp = null;
		int parentIndex = (targetIndex - 1) / 2;
		while(parentIndex >= 0 && dataArray[targetIndex].compareTo(dataArray[parentIndex]) > 0) {
			temp = dataArray[targetIndex];
			dataArray[targetIndex] = dataArray[parentIndex];
			dataArray[parentIndex] = temp;
			
			targetIndex = parentIndex;
			parentIndex = (targetIndex - 1) / 2;
		}
	}
	
	/**
	 * 	调用 sink() 方法的时候， 除了 targetIndex 以外的所有子结点元素都符合大顶堆的规则
	 * 	也就是说，如果我们假设 fromIndex 是最大值，那么以 targetIndex 为根结点的二叉树是符合大顶堆规则的
	 * 	而实际上 targetIndex 并不一定比两个子结点大，为了符合大顶堆规则，我们需要先比较两个子结点，找到那个比较大的子结点
	 * 	然后拿 targetIndex 跟 比较大的子结点进行比较， 如果 targetIndex 小于子结点，那么就进行下沉操作（其实就是交换）
	 * 	下沉以后，还要再跟下面的两个子结点进行比较，如果还是小于子结点，就再次下沉...
	 * 	一直到 targetIndex 大于两个子结点，或者 targetIndex 没有子结点为止
	 * 	
	 * 	【作用】 下沉操作可以保证   [targetIndex, size - 1]  区间的元素满足大顶堆的规则 
	 * 
	 * 	【说明】 一般来说，下沉操作的使用场景有两个：
	 * 	1、  删除根结点（最大值）。 
	 * 		 二叉堆的底层实现都是数组，本质是一个完全二叉树。 而数组要删除元素是比较麻烦的，特别是删除 arr[0] ，后面的全部元素都得
	 * 		跟着移动。 所以我们一般都是让 arr[0] 跟最后的元素进行交换，然后改变堆的大小，忽略  arr[len - 1]。
	 * 		交换了以后， 现在的 arr[0] 可能并不符合大顶堆的规则，我们需要进行下沉操作，把 当前的 arr[0] 下沉到合适的位置
	 * 		使现在的数组，又能符合大顶堆的规则 。
	 * 
	 * 	2、  把一个无序的数组初始化成一个堆。从最后一个非叶子结点开始，往左走，一个一个地执行  sink() 方法。
	 * 		
	 * @param targetIndex
	 */
	public void sink(int targetIndex) {
		T temp = null;
		int leftIndex = 0;
		int rightIndex = 0;
		int maxSonIndex = 0;
		while(true) {
			// 根据 targetIndex 算出 leftIndex 和 rightIndex
			leftIndex = targetIndex * 2 + 1;
			rightIndex = targetIndex * 2 + 2;
			// 如果右子结点索引存在，那么左子结点索引肯定也存在，我们先比较两个子结点的大小
			// 保存较大的子结点索引到 maxSonIndex
			if(rightIndex <= size - 1) {
				maxSonIndex = dataArray[leftIndex].compareTo(dataArray[rightIndex]) > 0 ? leftIndex : rightIndex;
			}else if(leftIndex == size - 1) {
				// 如果右子结点不存在，而左子结点刚好是最后一个元素，那么我们就直接把左子结点索引保存到 maxSonIndex
				// 因为 leftIndex 跟 rightIndex 是相邻的，所以不可能出现 leftIndex < size - 1 && rightIndex > size - 1
				maxSonIndex = leftIndex;
			}else {
				break;
			}
			
			// 如果前面没有跳出循环，我们这里就比较  targetIndex 跟   maxSonIndex 的大小 
			// 	如果targetIndex 比  maxSonIndex 小，那么就交换位置，然后  targetIndex 变成 maxSonIndex，继续循环
			//  如果targetIndex 比  maxSonIndex 大，那么说明已经下沉到合适的位置了，退出循环
			if(dataArray[targetIndex].compareTo(dataArray[maxSonIndex]) < 0) {
				temp = dataArray[targetIndex];
				dataArray[targetIndex] = dataArray[maxSonIndex];
				dataArray[maxSonIndex] = temp;
				targetIndex = maxSonIndex;
			}else {
				break;
			}
		}
	}
	
	/**
	 * 	插入元素到堆中，堆的长度会增加
	 * 	插入元素都是在堆的最后一个结点处添加（当前的 size 索引处），然后逐渐上浮，找到合适的位置
	 * 	【注意】 如果底层的数组长度不够用的话，我们会自动扩大长度 为现有长度的两倍。
	 * @param element
	 * @return
	 */
	public boolean insert(T element){
		// 如果当前的 size 已经等于数组最大长度了，那么我们在添加元素之前，需要先对数组进行扩容
		if(size == capacity) {
			ensureCapacity();
		}
		// 添加元素，维护 size;
		dataArray[size++] = element;
		
		// 添加完数据到数组以后，我们需要调用 swim() 方法，让新添加的元素上浮到合适的位置
		// 因为 size 值已经变了，所以新添加元素的索引为 size - 1
		swim(size - 1);
		
		// 其实这里一直是返回 true 的，如果无法扩容是直接报错的
		return true;
	}
	
	private void ensureCapacity() {
		// 注意，这里我们是直接就报错
		// 哈希表不扩容的话，添加新的元素只是让链表的长度长一些而已，并不会造成什么问题
		// 而这里直接使用的是数组结构，无法扩容就意味着无法再添加新元素了
		if(this.capacity * 2 < 0) {
			throw new RuntimeException("无法再扩容了");
		}
		this.dataArray = Arrays.copyOf(this.dataArray, this.capacity * 2);
		this.capacity = this.capacity * 2;
	}
	
	/**
	 * 	因为我们维护的是一个最大堆，所以这里删除的是最大值，其实就是删除根结点
	 * 	又因为其实堆底层实现其实是数组，数组删除元素需要移动大量的元素，所以我们一般不是真正删除，而是让当前的堆最后一个结点元素arr[size - 1]
	 *	去覆盖根结点arr[0]， 然后在  [0, size - 2] 的区间内重新构建一个最大堆
	 * 	
	 * 	[0, size - 2] 区间的数组其实除了 arr[0] 结点不符合最大堆的规则，其他的结点都是符合的
	 * 	所以我们并不需要像 heapify() 方法那样，从最后一个非叶子结点开始，一个一个子树地进行下沉操作
	 *  ===> 我们只需要对   arr[0]  位置的元素进行下沉操作就可以了
	 * @return
	 */
	public T removeMax() {
		T result = null;
		if(size > 1) {
			// 如果堆的大小大于1，那么我们把 dataArray[0] 的元素另外保存以后，就需要使用当前数组的最后一个元素结点
			// 来覆盖  dataArray[0] 
			result = dataArray[0];
			dataArray[0] = dataArray[size - 1];
			// 再然后，我们需要重新在   [0, size - 2] 区间内重新构建堆，不过其实我们只需要对  dataArray[0] 进行下沉操作即可
			sink(0);
			// 维护size;
			size --;
		}else if(size == 1) {
			// 如果堆的大小刚好为1 ,那么就只有一个根结点，我们不需要进行覆盖
			result = dataArray[0];
			// 维护size;
			size --;
		}
		// 如果堆的大小为 0 ，那么我们什么都不需要操作，也没有什么结点可以删除， 最终返回 null 
		
		return result;
	}
	
	@Override
	public String toString() {
		if(size == 0) {
			return "[]";
		}
		String  result = "[";
		for (int i = 0; i < size; i++) {
			result += (dataArray[i] + ", ");
		}
		
		// 处理一下最后一个 逗号
		result = result.replaceAll(", $", "");
		result += "]";
		
		return result;
	}
}
