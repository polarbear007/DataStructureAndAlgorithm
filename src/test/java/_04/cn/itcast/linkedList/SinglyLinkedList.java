package _04.cn.itcast.linkedList;

import java.util.Iterator;

/**
 * 	这个单向链表主要实现了以下的功能：
 * 	1、 addFirst()
 * 	2、 addLast()
 *  3、removeFirst()
 *  4、removeLast()
 *  5、addByOrder()  实现自动排序，要求数据要实现 comparable 接口
 *  6、 toString()   打印链表
 *  7、 getFirst()   获取第一个结点的数据
 *  8、 getLast()    获取最后一个结点的数据
 *  9、 getByIndex(int index)   // 根据索引去获取对应的结点数据（模仿LinkedList 集合）
 *  10、 setByIndex(int index, T data)  // 根据索引去修改对应的结点数据
 *  11、removeByIndex(int index)  // 根据索引去删除结点数据 （要记得维护成员变量）
 *  12、 addByIndex(int index, T data)  // 根据索引去添加结点数据（要记得维护成员变量）
 *  13、size()      获取链表长度（因为我们有维护 size，所以直接返回成员变量 size 即可）
 *  14、 addAll(SinglyLinkedList<? extends T> linkedList)     // 直接在最后位置添加另一个集合，不排序
 *  15、 addAllToIndex(int index, SinglyLinkedList<? extends T> linkedList) // 从指定的索引处开始添加另一个集合
 *  15、 addAllByOrder(SinglyLinkedList<? extends T> linkedList)  // 添加另一个集合，要求还能自动排序
 * @author Administrator
 *
 * @param <T>
 */

class SinglyLinkedList<T> implements Iterable<T>{
	// 不管怎么样，我们先声明一个头部结点，这个结点不保存数据
	private Node<T> headNode = new Node<T>();
	// 这个成员变量保存的是结尾结点，当我们保存一个新的结点时，需要更新此变量指向
	private Node<T> lastNode = headNode;
	
	// 这个成员变量用来保存链表的的长度
	// 这里我们并不打算把头结点也算进去，所以初始值为 0
	private Integer size = 0;
	
	// 获取链表的长度，我们直接返回  size 即可
	public Integer size() {
		return this.size;
	}

	// 在最后添加一个链表元素(暂时我们只做最简单的链表，所以添加是肯定成功的)
	public boolean addLast(T data) {
		// 添加一个新的元素，我们就创建一个 node 对象，然后把这个 node 对象作为原先结尾的结点的内部 node
		Node<T> node = new Node<T>();
		node.data = data;
		lastNode.next = node;
		lastNode = node;
		// 最终也一定要更新这个 size ，方便以后遍历 
		size++;
		return true;
	}
	
	// 在最前面添加一个链表元素
	public boolean addFirst(T data) {
		// 先创建要保存的结点
		Node<T> node = new Node<T>();
		node.data = data;
		
		// 取出当前 headNode.next 指向的结点
		Node<T> currFirstNode = headNode.next;
		// 用要保存的结点指向  currFirstNode; 就算 currFirstNode 是 null，也没有关系
		node.next = currFirstNode;
		// 然后使用 headNode.next 指向 node 
		headNode.next = node;
		// 维护  size 
		size++;
		// 如果我们添加的是最后结点，那么我们也要维护一下最后结点 
		if(node.next == null) {
			lastNode = node;
		}
		// 肯定是 返回  true
		return true;
	}
	
	// 删除链表中最后一个元素，并返回该元素
	public T removeLast() {
		// 如果链表为空，我们直接返回 null
		if(size == 0) {
			return null;
		}
		
		// 我们得找到倒数第二个元素，并把这个倒数第二个元素改成最后一个元素
		// 因为我们是单向链表，所以不得不从头遍历一遍
		Node<T> currentNode = headNode;
		while(true) {
			currentNode = currentNode.next;
			if(currentNode.next == lastNode) {
				break;
			}
		}
		
		// 首先，我们应该把要返回的数据保存起来
		T result = currentNode.next.data;
		
		// 拿到倒数第二个元素结点以后，我们把 next 值改成 null ，并维护 size 和 lastNode
		currentNode.next = null;
		lastNode = currentNode;
		// 要记得维护这个 size ，方便以后的遍历 
		size--;
		return result;
	}
	
	// 删除第一个元素
	public T removeFirst() {
		T result = null;
		// 首先，我们看一下 size 的值，如果值为 0 ，直接返回  null
		if(size == 0) {
			return null;
		}
		// 如果长度不为 0 的话，我们要做以下几件事：
		// 1、 拿到要删除的元素对应的数据（因为 size 不为0， 肯定是有数据的，不需要检查）
		result = headNode.next.data;
		// 2、 把headNode  的next 指向下一个元素，就算没有下一个元素，删掉以后，指向 null 也是对的
		headNode.next = headNode.next.next;
		// 3、 维护 size
		size--;
		// 4、 最后我们还是得检查一下，刚才删除的是不是最后一个元素结点，如果是的话，我们还得维护 lastNode
		//    如果不是，那么就什么都不处理
		if(headNode.next == null) {
			lastNode = headNode;
		}
		return result;
	}
	
	// 按顺序添加
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
		// 如果 size 不为 0， 说明有其他的元素，那么我们就需要遍历全部的元素
		// 拿到各个元素的 data 来比较
		Node<T> preNode = null;
		Node<T> currNode = headNode;
		boolean flag = true;
		while(flag) {
			preNode = currNode;
			currNode = currNode.next;
			// 拿到当前结点跟传入的那个 data 进行比较
			// 如果 compareTo 方法返回值小于零，我们就在 preNode 和 currNode 插入一个新的 结点 
			if(cData.compareTo(currNode.data) < 0) {
				// 然后我们就要创建一个新的 node 插入 preNode 和 currNode 之间
				Node<T> newNode = new Node<T>();
				newNode.data = data;
				newNode.next = currNode;
				preNode.next = newNode;
				// 维护 size
				size++;
				// 因为是在 currNode 之前加入的，所以不需要维护 lastNode
				flag = false;
			}
			
			// 如果链表都遍历完了， compareTo 方法一直不小于零
			// 说明我们需要把这个新结点保存到最后一个元素，那么直接调用 addLast() 就好 了
			if(flag && currNode.next == null) {
				addLast(data);
				// 最后一定要把 flag = false
				flag = false;
			}
		}
		return true;
	}
	
	// 输出链表
	// 我们需要遍历链表，拿到每一个节点保存的数据
	@Override
	public String toString() {
		// 如果是一个空链表，那么我们直接输出  []
		if(size == 0) {
			return "[]";
		}
		// 如果不是空链表，那么我们需要遍历链表，拼接一个字符串
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		Node<T> currNode = headNode;
		while(true) {
			currNode = currNode.next;
			if(currNode.next == null) {
				builder.append(currNode.data);
				break;
			}else {
				builder.append(currNode.data).append(", ");
			}
		}
		
		builder.append("]");
		return builder.toString();
	}
	
	// 获取第一个结点的数据（不删除）
	public T getFirst() {
		if(size > 0) {
			return headNode.next.data;
		}else {
			return null;
		}
	}
	
	// 获取最后一个结点的数据（不删除）
	public T getLast() {
		if(size > 0) {
			return lastNode.data;
		}else {
			return null;
		}
	}
	
	// 获取指定 index 处的结点的数据
	// 因为我们是单向链表，所以也不需要判断正向遍历还是反向遍历快
	// 不支持负索引
	public T getByIndex(int index) {
		// 需要先检查一下指定的索引有没有越界
		// 如果size 等于0的话，那么 index 其实等于任何值都是报异常的
		checkIndex(index);
		
		// 如果索引没有越界，那么我们就开始遍历 链表（这里其实已经保证了链表不为空了）
		// 当 循环停止时， currNode 刚好就是 index 对应的结点对象
		// 比如说 index = 0， 那么 currNode 其实就是对应第一个结点
		// 比如说 index = 1, 那么 currNode 其实就是对应 第二个结点
		Node<T> currNode = headNode;
		for (int i = 0; i < index + 1; i++) {
			currNode = currNode.next;
		}
		
		// 拿到了结点对象，我们只需要返回对应的数据就可以了
		return currNode.data;
	}
	
	// 根据索引修改节点数据， 如果修改成功，返回 true ； 失败返回 false
	public boolean setByIndex(int index, T data) {
		// 首先，还是确认一下这个索引存不存在,如果不存在就报错
		checkIndex(index);
		
		// 如果能走到这里，说明索引没有越界， 我们就开始遍历，找到对应的结点对象
		Node<T> currNode = headNode;
		for (int i = 0; i < index + 1; i++) {
			currNode = currNode.next;
		}
		
		// 根据传入的 data 修改目标结点的数据
		currNode.data = data;
		return true;
	}
	
	// 根据索引，删除结点
	// 删除成功，返回删除的结点的数据
	// 删除失败，返回null
	public T removeByIndex(int index) {
		// 首先，还是确认一下这个索引存不存在,如果不存在就报错
		checkIndex(index);
		
		// 如果能走到这里，说明索引没有越界， 我们就开始遍历，找到要删除的结点的前一个结点
		// 所以这里我们不能像以前那样 i < index + 1,而是应该  i < index
		Node<T> currNode = headNode;
		for (int i = 0; i < index; i++) {
			currNode = currNode.next;
		}
		
		// 现在 currNode 就是要删除的目标结点的上一个结点了
		// 先保存一下要返回的数据 
		T result = currNode.next.data;
		// 然后把 currNode.next 指向下下个结点
		currNode.next = currNode.next.next;
		
		// 维护 size
		size--;
		// 维护 lastNode 
		// 如果删除了目标结点以后， currNode 变成了最后一个元素，那么我们就需要把 lastNode 指向 currNode
		if(currNode.next == null) {
			lastNode = currNode;
		}
		
		// 最后返回 result 即可
		return result;
	}
	
	// 根据索引添加结点数据
	// 添加成功，返回 true， 失败返回 false
	// 【注意】 这里我们不能简单地 checkIndex()，因为可能指定的索引是指向当前最后元素的下一个位置
	//        像这种情况， 是可以接受的，但是 checkIndex 会报错，所以我们的流程要稍微改动一下
	public boolean addByIndex(int index , T data) {
		// 首先，我们要看一下 index 是不是指向最后元素的下一个位置
		if(index == size) {
			return addLast(data);
		}
		
		// 如果不是指向最后元素的下一个位置，那么我们就 checkIndex()
		checkIndex(index);
		
		// 如果前面都没有问题的话，那么我们就遍历链表，找到索引对应的那个结点的前一个结点
		// 所以这里同样使用  i < index
		Node<T> currNode = headNode;
		for (int i = 0; i < index; i++) {
			currNode = currNode.next;
		}
		
		// 现在 currNode 就是前一个结点了，我们只需要新建一个结点
		Node<T> newNode = new Node<T>();
		newNode.data = data;
		newNode.next = currNode.next;
		// 然后把 currNode.next 指向新创建的结点即可
		currNode.next = newNode;
		// 添加以后，我们要维护 size 
		size++;
		// 这里我们不需要考虑维护 lastNode ，因为如果添加的是最后一个元素，那么前面的 addLast() 就会维护了
		return true;
	}
	
	// 反转链表的思路1：
//	public void reverse() {
//		Node<T> reverseHeadNode = new Node<T>();
//		Node<T> currNode = headNode;
//		while(true) {
//			// 注意： 这里我们得用 headNode 去取第一个元素，取完就删，所以不会重复，不像以前遍历链表使用
//			//      currNode = currNode.next
//			currNode = headNode.next;
//	
//			if(currNode != null) {
//				// 把 headNode 指向 currNode 的下一个结点，这样其实 currNode 就被删除了
//				headNode.next = currNode.next;
//				// 现在我们就可以把 currNode 插入到 reverseHeadNode 上面去（要 addFirst 那种效果）
//				currNode.next = reverseHeadNode.next;
//				reverseHeadNode.next = currNode;
//				if(currNode.next == null) {
//					// currNode.next == null 说明是 reverseHeadNode上添加的第一个元素，其实也是 lastNode
//					lastNode = currNode;
//				}
//			}else {
//				// 如果 currNode 已经是 null 了，那么说明原来的链表已经全部删光了
//				// 我们需要把 headNode 替换成 reverseHeadNode ，最后退出循环
//				headNode = reverseHeadNode;
//				break;
//			}
//		}
//		
//	}
	
	public void reverse() {
		Node<T> reverseHeadNode = new Node<T>();
		Node<T> currNode = headNode.next;
		Node<T> next = null;
		while(currNode != null) {
			// 先使用 next 保存下一个可遍历的结点对象地址
			next = currNode.next;
			// 只要下一个遍历的结点对象地址保存起来，那么 currNode 节点对象就可以插入到 reverseHeadNode 上
			currNode.next = reverseHeadNode.next;
			reverseHeadNode.next = currNode;
			// 维护一下  lastNode 
			if(currNode.next == null) {
				lastNode = currNode;
			}
			// 再把 next 保存到 currNode 上面来
			currNode = next;
		}
		
		// 上面遍历完以后， 全部的元素其实都被 reverseHeadNode 串起来了,原来的headNode 没有什么用了
		headNode = reverseHeadNode;
	}
	
	/**
	 * 【作用】直接在最后位置添加另一个集合，不排序。
	 * @param linkedList
	 * @return  添加成功返回 true, 添加失败，返回false;(其实我们没有太多规则，肯定返回true)
	 */
	public boolean addAll(SinglyLinkedList<? extends T> linkedList) {
		// 一般来说， for each 遍历前要判断一下集合是否为 null, 不然会报空指针，但是这里我们不处理
		// 如果要添加的集合有效，而且长度不为 0 ,我们就遍历那个集合，保存到原来的链表上
		for(T data : linkedList) {
			addLast(data);
		}
		// 直接全部返回 true
		return true;
	}
	/**
	 * 从指定的索引处开始添加另一个集合
	 * @param index
	 * @param linkedList
	 */
	public boolean addAllToIndex(int index, SinglyLinkedList<? extends T> linkedList) {
		// 首先，我们要看一下 index 是不是指向最后元素的下一个位置
		if(index == size) {
			return addAll(linkedList);
		}
		// 如果不是添加到最后的索引处，我们就要确认一下指定的索引是否有效,如果无效，直接抛异常
		checkIndex(index);
		// 如果前面的检查都没有什么问题了，那么我们就遍历要添加的集合，然后保存到指定的索引位置
		// 【思路1】我们可以 遍历传入的集合，然后直接调用以前的 addByIndex() 添加到指定的索引位置 
		//        为了保证添加的顺序，我们让 Index 自增
//		for (T data : linkedList) {
//			addByIndex(index++, data);
//		}
		
		// 【思路2】 前面的那种方法虽然可以实现麻烦，但是其实每次调用 addByIndex() 都得重新遍历链表
		//         如果两个链表都很长的话，效率可能会很低
		//        所以我们最好是 遍历要添加的集合，然后自己拼接一个 Node 串， 最后把这整个串都插入到指定的位置
		//        这样子，两个集合都只遍历一次
		
		// 遍历完以后， tempHead 保存的是头， currNode 保存的尾
		Node<T>  currNode = new Node<T>();
		Node<T> tempHead = currNode;
		for (T data : linkedList) {
			Node<T> targetNode = new Node<T>();
			targetNode.data = data;
			currNode.next = targetNode;
			currNode = targetNode;
		}
		
		// 我们要想插入这一串的结点，需要拿到目标结点的前一个结点
		// 遍历过后，targetNode 就是目标结点的前一个结点
		Node<T> targetNode = headNode;
		for(int i = 0; i < index; i++) {
			targetNode = targetNode.next;
		}
		
		// 现在我们可以把前面拼接的一串结点插入到  targetNode 后面了
		currNode.next = targetNode.next;
		// tempHead 没有数据，不需要保存，直接保存其下一个结点即可
		targetNode.next = tempHead.next;
		// 维护 size 
		size += linkedList.size();
		// 因为不可能是添加到最后，所以我们不需要考虑维护 lastNode
		
		return true;
	}
	
	/**
	 * 添加另一个集合，要求还能自动排序(当然，前面也必须已经排好序了)
	 * @param linkedList
	 * @return
	 */
	public boolean addAllByOrder(SinglyLinkedList<? extends T> linkedList) {
		for (T data : linkedList) {
			addByOrder(data);
		}
		return true;
	}
	
	// 因为很多方法都需要引用此功能，所以我们直接封装成一个方法
	private boolean checkIndex(int index) {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		return true;
	}
	
	// 先声明一个简单的结点对象，内部类
	@SuppressWarnings("hiding")
	private class Node<T> {
		private T data;
		private Node<T> next;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			// 在迭代器内部，定义一个局部变量，保存头结点地址
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
