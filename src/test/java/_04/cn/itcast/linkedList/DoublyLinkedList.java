package _04.cn.itcast.linkedList;

import java.util.Iterator;

/**
 * 这个双向链表主要实现了以下的功能： 
 * 1、 addFirst() 
 * 2、 addLast() 
 * 3、removeFirst() 
 * 4、removeLast()
 * 5、addByOrder() 实现自动排序，要求数据要实现 comparable 接口
 * 6、 toString() 打印链表 
 * 7、 getFirst()获取第一个结点的数据 
 * 8、 getLast() 获取最后一个结点的数据 
 * 9、 getByIndex(int index) //根据索引去获取对应的结点数据（模仿LinkedList 集合） 
 * 10、 setByIndex(int index, T data) //根据索引去修改对应的结点数据 
 * 11、removeByIndex(int index) // 根据索引去删除结点数据 （要记得维护成员变量） 
 * 12、addByIndex(int index, T data) // 根据索引去添加结点数据（要记得维护成员变量） 
 * 13、size()获取链表长度（因为我们有维护 size，所以直接返回成员变量 size 即可） 
 * 14、 addAll(SinglyLinkedList<? extends T> linkedList) // 直接在最后位置添加另一个集合，不排序 
 * 15、 addAllToIndex(int index,SinglyLinkedList<? extends T> linkedList) // 从指定的索引处开始添加另一个集合 
 * 16、addAllByOrder(SinglyLinkedList<? extends T> linkedList) // 添加另一个集合，要求还能自动排序
 * 17、 reverse() 反转双向链表
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class DoublyLinkedList<T> implements Iterable<T>{
	private Node<T> headNode = new Node<T>();
	private Node<T> lastNode = headNode;
	private int size = 0;

	public T getLast() {
		return lastNode.data;
	}

	public T getFirst() {
		if (size == 0) {
			return null;
		} else {
			return headNode.next.data;
		}
	}

	public Integer size() {
		return size;
	}

	/**
	 * 正向打印双向链表
	 */
	@Override
	public String toString() {
		if (size == 0) {
			return "[]";
		} else {
			String result = "[";
			Node<T> currNode = headNode;
			while (true) {
				currNode = currNode.next;
				if (currNode.next == null) {
					result += currNode.data;
					break;
				} else {
					result += (currNode.data + ", ");
				}
			}
			result += "]";
			return result;
		}
	}

	/**
	 * 在最前面添加一个链表元素，需要考虑下面两种情况： 1、 当前链表为空 2、 当前链表不为空
	 * 
	 * @param data
	 * @return
	 */
	public boolean addFirst(T data) {
		Node<T> newNode = new Node<T>();
		newNode.data = data;
		// 当前链表为 空，添加的新结点其实就是最后结点，我们需要维护 lastNode
		if (size == 0) {
			// 直接把新结点的 prev 设置成 headNode
			newNode.prev = headNode;
			// 再把headNode 的next 设置成 newNode
			headNode.next = newNode;

			// 维护 lastNode
			lastNode = newNode;
		} else if (size > 0) {
			// 如果当前链表不为空，那么就是在 headNode 和 原来的第一个结点之前插入一个新结点, 不需要维护lastNode
			// 把当前结点的 prev 设置成 headNode
			newNode.prev = headNode;
			// 把原来headNode 的下一个结点的 prev 设置成新结点
			headNode.next.prev = newNode;
			// 然后把新结点的 next 设置成 headNode 的next
			newNode.next = headNode.next;
			// 最后再去 headNode的下一个结点设置成新结点
			headNode.next = newNode;
		}

		// 不管什么情况，最后都要维护 size
		size++;

		// 肯定是 返回 true
		return true;
	}

	/**
	 * 在最后面添加一个链表元素，因为我们允许重复，允许null值，所以添加任何元素都是返回 true
	 * 
	 * @param data
	 * @return
	 */
	public boolean addLast(T data) {
		Node<T> newNode = new Node<T>();
		newNode.data = data;

		newNode.prev = lastNode;
		lastNode.next = newNode;
		// 维护 lastNode
		lastNode = newNode;
		// 维护 size
		size++;

		return true;
	}

	/**
	 * 在指定的索引处添加链表元素，需要考虑下面几种情况： 
	 *  1、 指定的索引是否对应当前链表的最后一个元素的下一个元素（这是允许的） 
	 *  2、 如果不是上面的特殊情况，我们应该确认索引是否符合要求，如果不符合，直接抛异常 
	 *  3、 如果都不是上面的情况，那么我们就先遍历找到索引对应的元素，然后在这个元素的前面插入一个新的元素
	 * 
	 * @param index
	 * @param data
	 * @return
	 */
	public boolean addByIndex(int index, T data) {
		// 首先，我们应该看一下目标索引是不是对应lastNode 的下一个结点位置，如果是，那就按addLast 处理
		if (index == size) {
			return addLast(data);
		}
		// 然后，先看一下目标索引是不是有效
		checkIndex(index);

		// 前面的检查都不符合时，说明我们是要在指定结点之前插入一个新的节点
		// 先找到索引对应的结点对象，可以正向查找，也可以反向查找，具体我们看index 值是否超过总长度的一半
		// 超过的话，我们就反向查找； 不超过的话，我们就正向查找。
		Node<T> currNode = null;
		if (index > (size / 2)) {
			// 反向查找
			currNode = lastNode;
			for (int i = size - 1; i > index; i--) {
				currNode = currNode.prev;
			}
		} else {
			// 正向查找
			currNode = headNode.next;
			for (int i = 0; i < index; i++) {
				currNode = currNode.next;
			}
		}
		// 现在 currNode 就是索引指向的结点元素，我们需要在其前面插入一个新的结点
		Node<T> newNode = new Node<T>();
		newNode.data = data;
		newNode.prev = currNode.prev;
		newNode.next = currNode;
		currNode.prev = newNode;
		newNode.prev.next = newNode;
		// 维护 size
		size++;
		// 如果是使用这种方式添加，新结点肯定不是最后一个元素，所以我们不需要维护 lastNode
		
		// 全部返回 true
		return true;
	}

	/**
	 * 添加到链表中以后，实现自动排序。要求被添加的数据必须实现  comparable 接口
	 * 其实，这个方法还要求原来的元素也是有序的！！！
	 * @param data
	 * @return
	 */
	public boolean addByOrder(T data) {
		// 检查一下 T 类型有没有实现  comparable 接口，如果没有的话，直接报异常
		if(!(data instanceof Comparable)) {
			throw new RuntimeException("保存的数据没有实现 Comparable 接口，无法进行排序插入");
		}
		
		// 经过前面的检查，如果没有问题，那么就可以执行下面的类型转换
		@SuppressWarnings("unchecked")
		Comparable<T> cData = (Comparable<T>) data;
		
		// 没有其他的元素，直接添加即可
		if(size == 0) {
			return addLast(data);
		}
		
		// 如果有其他的元素，那么我们需要从头遍历，拿到每一个元素来跟传入的 data 进行比较
		Node<T> currNode = headNode.next;
		while(true) {
			// 一旦发现传入的数据比当前元素小，我们就停止遍历
			if(cData.compareTo(currNode.data) < 0) {
				break;
			}
			// 如果找到了最后一个元素，还没有找到满足条件的结点，那么我们直接添加到最后
			if(currNode.next == null) {
				return addLast(data);
			}
			
			currNode = currNode.next;
		}
		
		// 一旦停止遍历，说明 currNode 就是满足条件的结点，我们要在这个结点前面添加一个新结点
		Node<T> newNode = new Node<T>();
		newNode.data = data;
		
		// 从左右设置到右边
		currNode.prev.next = newNode;
		newNode.prev = currNode.prev;
		newNode.next = currNode;
		currNode.prev = newNode;
		
		// 维护 size
		size++;
		// 不需要维护lastNode
		return true;
	}
	
	/**
	 * 测试指定的索引是否越界，如果越界，直接扔异常
	 * @param index
	 */
	private void checkIndex(int index) {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * 删除链表中的第一个元素
	 * @return
	 */
	public T removeFirst() {
		T result = null;
		// 其实还应该考虑 size 为0时的情况，这种情况 返回值为 null ，我们这里就不处理
		if(size == 1){
			// 先保存要返回的数据
		   result = headNode.next.data;
		   // 然后删除目标结点
		   headNode.next = null;
		   // 维护一下 lastNode
		   lastNode = headNode;
		   // 维护 size
		   size--;
		}else if(size > 1) {
			// 先保存要返回的数据
		   result = headNode.next.data;
		   // 然后拿到要删除的那个结点,并删除
		   Node<T> targetNode = headNode.next;
		   targetNode.prev.next = targetNode.next;
		   targetNode.next.prev = targetNode.prev;
		   // 不需要维护 lastNode
		   // 维护 size
		   size--;
		}
		return result;
	}
	

	
	/**
	 * 删除链表中的最后一个元素，直接返回 lastNode 的data
	 *    如果链表为空，那么其实什么都不用做，直接返回  null 即可
	 *    如果链表不为空，那么我们需要维护 lastNode 和 size
	 * @return
	 */
	public T removeLast() {
		T result = null;
		// 只处理 size > 0 的情况
		if(size > 0) {
			result = lastNode.data;
			// 维护 lastNode
			lastNode = lastNode.prev;
			// 维护 size
			size--;
		}
		return result;
	}
	
	/**
	 * 根据指定的索引删除结点，并返回被 删除结点的数据
	 * @return
	 */
	public T removeByIndex(int index) {
		// 啥事都不管，先检查索引是否越界
		checkIndex(index);
		
		// 如果没有越界的话，我们再看看是不是要删除最后一个元素
		if(index == size - 1) {
			return removeLast();
		}
		
		// 先找到索引对应的结点对象，可以正向查找，也可以反向查找，具体我们看index 值是否超过总长度的一半
		// 超过的话，我们就反向查找； 不超过的话，我们就正向查找。
		Node<T> currNode = null;
		if (index > (size / 2)) {
			// 反向查找
			currNode = lastNode;
			for (int i = size - 1; i > index; i--) {
				currNode = currNode.prev;
			}
		} else {
			// 正向查找
			currNode = headNode.next;
			for (int i = 0; i < index; i++) {
				currNode = currNode.next;
			}
		}
		// 现在 currNode 就是索引指向的结点元素，我们需要删除这个结点
		// 并且这个结点肯定不会是最后的结点
		T result = currNode.data;
		currNode.prev.next = currNode.next;
		currNode.next.prev = currNode.prev;
		
		// 因为可以保证不是最后一个元素，所以我们不需要维护 lastNode
		// 维护 size
		size--;
		return result;
	}
	
	/**
	 * 根据索引返回对应的结点数据
	 * @param index
	 * @return
	 */
	public T getByIndex(int index) {
		// 啥事都不管，先检查索引是否越界
		checkIndex(index);
		// 先找到索引对应的结点对象，可以正向查找，也可以反向查找，具体我们看index 值是否超过总长度的一半
		// 超过的话，我们就反向查找； 不超过的话，我们就正向查找。
		Node<T> currNode = null;
		if (index > (size / 2)) {
			// 反向查找
			currNode = lastNode;
			for (int i = size - 1; i > index; i--) {
				currNode = currNode.prev;
			}
		} else {
			// 正向查找
			currNode = headNode.next;
			for (int i = 0; i < index; i++) {
				currNode = currNode.next;
			}
		}
		// 现在 currNode 就是索引指向的结点元素，我们需要返回这个结点对应的数据
		return currNode.data;
	}

	/**
	 * 根据索引替换指定结点的数据，替换成功返回 true , 替换失败返回 false
	 * 一般都是返回 true ，因为我们对数据没有限制
	 * 如果索引越界，直接报异常
	 * @param index
	 * @param data
	 * @return
	 */
	public boolean setByIndex(int index, T data){
		// 什么都不管，直接检查索引
		checkIndex(index);
		// 先找到索引对应的结点对象，可以正向查找，也可以反向查找，具体我们看index 值是否超过总长度的一半
		// 超过的话，我们就反向查找； 不超过的话，我们就正向查找。
		Node<T> currNode = null;
		if (index > (size / 2)) {
			// 反向查找
			currNode = lastNode;
			for (int i = size - 1; i > index; i--) {
				currNode = currNode.prev;
			}
		} else {
			// 正向查找
			currNode = headNode.next;
			for (int i = 0; i < index; i++) {
				currNode = currNode.next;
			}
		}
		// 现在 currNode 就是索引指向的结点元素，我们需要返回这个结点对应的数据
		currNode.data = data;
		return true;
	}
	
	/**
	 * 把传入的集合，全部添加到集合后面
	 * @param linkedList
	 * @return
	 */
	public boolean addAll(DoublyLinkedList<? extends T> linkedList){
		// 遍历传入的集合，并一个个地保存到集合中去
		for (T data : linkedList) {
			addLast(data);
		}
		return true;
	}
	
	/**
	 * 把一个集合添加另一个集合中指定的索引处
	 * @param index
	 * @param linkedList
	 * @return
	 */
	public boolean addAllToIndex(int index,DoublyLinkedList<? extends T> linkedList) {
		// 首先，我们要看一下 index 是不是指向最后元素的下一个位置，如果是直接 addAll()
		if(index == size) {
			return addAll(linkedList);
		}
		// 如果不是添加到最后，就检查一下 index 是否合法
		checkIndex(index);
		
		// 虽然我们可以直接通过 addByIndex() 一个个地添加，但是那样子效率比较低，所以我们
		// 先遍历传入的集合，拼接成一个结点串以后，再统一添加到目标集合中
		Node<T> currNode = new Node<>();
		Node<T> tempHeadNode = currNode;
		for (T data : linkedList) {
			Node<T> newNode = new Node<T>();
			newNode.data = data;
			newNode.prev = currNode;
			currNode.next = newNode;
			currNode = newNode;
		}
		
		// 遍历完以后， tempHeadNode 就是那个串的头，并不保存具体数据
		// currNode 就是那个串最后一个结点
		// 现在我们要遍历当前的链表，根据索引找到对应的结点对象，然后在这个结点的前面插入一串
		Node<T> targetNode = null;
		if (index > (size / 2)) {
			// 反向查找
			targetNode = lastNode;
			for (int i = size - 1; i > index; i--) {
				targetNode = targetNode.prev;
			}
		} else {
			// 正向查找
			targetNode = headNode.next;
			for (int i = 0; i < index; i++) {
				targetNode = targetNode.next;
			}
		}
		// 现在 targetNode 就是我们的目标结点，我们要在其前面添加一串元素
		// 从左到右，一个结点一个结点设置
		targetNode.prev.next = tempHeadNode.next;
		// 设置新添加结点的 prev
		tempHeadNode.next.prev = targetNode.prev;
		// 设置新添加结点的 next 
		currNode.next = targetNode;
		// 设置 targetNode 的 prev
		targetNode.prev = currNode;
		return true;
	}
	
	/**
	 * 把一个集合按照特定的顺序插入到原来的集合中
	 * 要求 data 必须实现 comparable 接口
	 * @param linkedList
	 * @return
	 */
	public boolean addAllByOrder(DoublyLinkedList<? extends T> linkedList) {
		for (T data : linkedList) {
			addByOrder(data);
		}
		return true;
	}
	
	/**
	 * 实现链表的反转
	 */
	public void reverse() {
		// size 等于 0 或者 1 都是没有必要反转的
		if(size > 2) {
			Node<T> currNode = headNode.next;
			Node<T> temp = null;
			while(true) {
				// 使用一个局部变量，来完成值交换功能
				temp = currNode.next;
				currNode.next = currNode.prev;
				currNode.prev = temp;
				// 如果 temp 的值是 null ，那么说明 currNode 已经是以前的最后一个元素了
				if(temp == null) {
					break;
				}
				// 如果前面没有退出循环，说明下面还有结点，我们就把指针往后移
				currNode = temp;
			}
			// 以前的最后一个元素，应该是现在的第一个元素，所以我们使其 prev 为 headNode
			currNode.prev = headNode;
			// 以前的第一个元素，应该是现在的最后一个元素，所以我们应该使其的 next 为 null
			// 以前的第一个元素可以使用 headNode.next 找到, 顺便更新一下 lastNode
			lastNode = headNode.next;
			lastNode.next = null;
			// 最后，我们应该把 headNode 的next 改成 currNode
			headNode.next = currNode;
			// 反转不需要更新 size
		}
	}
	
	@SuppressWarnings("hiding")
	private class Node<T> {
		private T data;
		private Node<T> prev;
		private Node<T> next;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node<T> currNode = headNode;
			@Override
			public boolean hasNext() {
				return currNode.next != null;
			}

			@Override
			public T next() {
				currNode = currNode.next;
				return currNode.data;
			}
		};
	}
}
