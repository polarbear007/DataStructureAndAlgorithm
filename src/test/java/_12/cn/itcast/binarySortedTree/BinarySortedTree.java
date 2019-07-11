package _12.cn.itcast.binarySortedTree;

import java.util.ArrayList;

/**
 * 	二叉排序树，带有排序的功能，元素会按升序排序， 中序遍历时遍历到的元素就是排好顺序的
 * 	因为元素是有序的，所以我们可以使用 二叉查找算法快速定位元素
 * @author Administrator
 *
 * @param <V>
 */
public class BinarySortedTree<K extends Comparable<K>, V>{
	Node<K, V> rootNode;
	int size;
	
	/**
	 * 	返回当前二叉树的结点数量
	 * @return
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * 	添加结点，如果结点存在，则值覆盖； 如果结点不存在，则添加结点，返回 null。
	 * 	【非递归】
	 * @param value
	 * @return
	 */
	public V put(K key, V value) {
		if(key == null) {
			throw new RuntimeException("键不能为空");
		}
		// 如果 key 不为空，我们直接先根据 key 和 value 创建一个 node 对象
		Node<K, V> newNode = new Node<>(key, value);
		int cmp = 0;
		V result = null;
		// 如果根结点为空，那么我们就把 newNode 指定为根结点
		if(rootNode == null) {
			rootNode = newNode;
			size++;
			return null;
		}else {
			Node<K, V> currNode = rootNode;
			while(true) {
				// 如果根结点不为空，那么我们就跟当前结点的 key 比较一下
				cmp = key.compareTo(currNode.key);
				// 如果cmp 等于 0 ，说明 key 相同，值替换
				if(cmp == 0) {
					result = currNode.value;
					currNode.value = value;
					// 返回原先的结点值，不需要维护 size
					return result;
				}else if(cmp > 0) {
					// 如果 cmp 大于0，那么说明添加的 key 比 currNode.key 大，那么我们往右找
					if(currNode.right != null) {
						currNode = currNode.right;
					}else {
						// 如果currNode 右子结点为空，那么我们直接把 newNode 添加到currNode 的右子结点上
						currNode.right = newNode;
						newNode.parent = currNode;
						size++;
						return null;
					}
				}else {
					if(currNode.left != null) {
						currNode = currNode.left;
					}else {
						currNode.left = newNode;
						newNode.parent = currNode;
						size++;
						return null;
					}
				}
			}
		}
	}
	
	/**
	 * 	添加结点，如果结点存在，则值覆盖；如果结点不存在，则添加结点，返回 null
	 * 	【递归】
	 * @param key
	 * @param value
	 * @return
	 */
	public V put2(K key, V value) {
		if(rootNode == null) {
			rootNode = new Node<K, V>(key, value);
			size++;
			return null;
		}else {
			return put(rootNode, key, value);
		}
	}
	
	/**
	 * 	递归添加元素的具体实现
	 * @param root
	 * @param key
	 * @param value
	 * @return
	 */
	private V put(Node<K, V> root, K key, V value){
		V result = null;
		int cmp = key.compareTo(root.key);
		if(cmp == 0) {
			result = root.value;
			root.value = value;
			return result;
		}else if(cmp > 0) {
			if(root.right != null) {
				return put(root.right, key, value);
			}else {
				root.right = new Node<K, V>(key, value, root);
				size++;
				return null;
			}
		}else {
			if(root.left != null) {
				return put(root.left, key, value);
			}else {
				root.left = new Node<K, V>(key, value, root);
				size++;
				return null;
			}
		}
	}
	
	/**
	 * 	根据键找值，如果键存在，返回对应的值； 如果键不存在，返回 null;
	 * 	【非递归实现】
	 * @param key
	 * @return
	 */
	public V get(K key) {
		// 如果 key 是 null ，那么直接返回 null ，因为我们不允许键值为 null
		// 如果 根结点都为 null 了，那么肯定是找不到任何值的，直接返回 null
		if(key == null || rootNode == null) {
			return null;
		}
		int cmp = 0;
		Node<K, V> currNode = rootNode;
		while(true) {
			cmp = key.compareTo(currNode.key);
			if(cmp == 0) {
				return currNode.value;
			}else if(cmp > 0) {
				if(currNode.right != null) {
					currNode = currNode.right;
				}else {
					return null;
				}
			}else {
				if(currNode.left != null) {
					currNode = currNode.left;
				}else {
					return null;
				}
			}
		}
	}
	
	/**
	 * 	根据 key 找值，递归实现
	 * @param key
	 * @return
	 */
	public V get2(K key) {
		if(key == null || rootNode == null) {
			return null;
		}else {
			return get(rootNode, key).value;
		}
	}
	
	/**
	 * 	根据 key 找对应的值，递归实现
	 * @param root
	 * @param key
	 * @return
	 */
	private Node<K, V> get(Node<K, V> root, K key) {
		if(root == null) {
			return null;
		}
		int cmp = key.compareTo(root.key);
		if(cmp == 0) {
			return root;
		}else if(cmp > 0) {
			return get(root.right, key);
		}else {
			return get(root.left, key);
		}
	}
	
	/**
	 * 	删除指定根结点的子树的最小结点
	 * @param root
	 */
	private Node<K, V> deleteMin(Node<K, V> root) {
		if(root == null) {
			throw new RuntimeException("子树根结点不能为 null");
		}
		Node<K, V> targetNode = min(root);
		// 这里我们不需要考虑 targetNode 为 null 的情况，因为只要 root 不为 null，就肯定会有最小值
		// 某个子树里面最小的结点可以肯定一点就是： 最小值的左子结点为 null
		
		// 如果目标结点的父结点是 null ，那么说明目标结点就是根结点
		// 那么我们就让目标结点的右子结点去代替根结点（右子结点可能为null）
		if(targetNode.parent == null) {
			rootNode = targetNode.right;
		}else {
			// 如果目标结点不是根结点，那么我们就需要判断一下删除结点在父结点的左边还是右边
			if(targetNode.parent.left == targetNode) {
				targetNode.parent.left = targetNode.right;
			}else {
				targetNode.parent.right = targetNode.right;
			}
		}
		// 不管目标结点是不是根结点，只要 targetNode.right 不为空，
		// 我们需要维护一下targetNode.right 结点的parent参数
		if(targetNode.right != null) {
			targetNode.right.parent = targetNode.parent;
		}
		return targetNode;
	}
	
	/**
	 * 	返回以指定结点为根节点的二叉树的最小结点
	 * @param root
	 * @return
	 */
	private Node<K, V> min(Node<K, V> root){
		if(root == null) {
			throw new RuntimeException("子树根结点不能为 null");
		}
		Node<K, V> currNode = root;
		while(true) {
			if(currNode.left != null) {
				currNode = currNode.left;
			}else {
				return currNode;
			}
		}
	}
	
	// 临时方法，方便外部测试私有方法，可以删除
	public void testDeleteMin() {
		deleteMin(rootNode);
	}
	// 临时方法，方便外部测试私有方法，可以删除
	public void testDeleteMax() {
		deleteMax(rootNode);
	}
	
	/**
	 * 	删除指定子树的最大结点
	 * @param root
	 */
	private Node<K, V> deleteMax(Node<K, V> root) {
		if(root == null) {
			throw new RuntimeException("子树根结点不能为 null");
		}
		// 先找到要删除的目标结点
		Node<K, V> targetNode = max(root);
		
		// 因为目标结点是最大值，所以我们可以确认一点：targetNode.right 一定是 null
		// 删除一个最大结点的时候，我们应该考虑以下几个情况：
		// 1、 删除的结点是根结点，也就是父结点为空，我们需要直接让 targetNode.left 变成根结点（targetNode.left可能为空）
		// 2、 删除的结点不是根结点，也就是父结点不为空
		//      我们只需要确认删除结点是父结点的左子结点还是右子结点，然后让父结点指向删除结点的左子结点就可以了
		if(targetNode.parent == null) {
			rootNode = targetNode.left;
		}else {
			if(targetNode == targetNode.parent.left) {
				targetNode.parent.left = targetNode.left;
			}else {
				targetNode.parent.right = targetNode.left;
			}
		}
		
		// 不管目标结点是不是根结点，只要 targetNode.left 不为空，
		// 我们需要维护一下targetNode.left 结点的parent参数
		if(targetNode.left != null) {
			targetNode.left.parent = targetNode.parent;
		}
		
		return targetNode;
	}
	
	/**
	 * 	返回以指定结点为根结点的二叉树的最大结点
	 * @param root
	 * @return
	 */
	private Node<K, V> max(Node<K, V> root){
		if(root == null) {
			throw new RuntimeException("子树根结点不能为 null");
		}
		Node<K, V> currNode = root;
		while(true) {
			if(currNode.right != null) {
				currNode = currNode.right;
			}else {
				return currNode;
			}
		}
	}
	
	/**
	 * 	删除有三种情况：
	 * 	1、 删除的结点是叶子结点，直接删除就可以了
	 * 	2、 删除的结点是非结点，我们转换成删除这个结点的前驱结点或者后继结点，
	 * 		如果前驱或者后继还是非叶结点，我们就继续找其前驱或者后继结点
	 * @param key
	 * @return
	 */
	public V delete(K key) {
		// 我们不允许有 null 键，所以 key 为 null ，肯定删除不成功，返回 null
		// 如果根结点是 null ，说明是一个空树，也直接返回一个 null 值
		if(key == null || rootNode == null) {
			return null;
		}
		
		V result = null;
		
		// 再然后，我们先调用 get 方法，通过 key 去找对应的结点
		Node<K, V> targetNode = get(rootNode, key);
		
		// 如果返回的目标结点是 null ，说明没有找到 key 对应的结点，我们直接返回 null
		if(targetNode == null) {
			return null;
		}else {
			// 如果 targetNode 不为 null ，那么我们先保存 targetNode.value
			result = targetNode.value;
		}
		
		while(true) {
			// 如果目标结点是叶子结点，那么我们需要再看一下目标结点是不是根结点
			if(targetNode.left == null && targetNode.right == null) {
				// 如果目标结点即是叶子结点，又是根结点，那么我们直接让 rootNode 等于 null 就可以了
				if(targetNode.parent == null) {
					rootNode = null;
					// 如果目标结点是父结点的左子结点
				}else if(targetNode.parent.left == targetNode) {
					targetNode.parent.left = null;
				}else {
					targetNode.parent.right = null;
				}
				break;
			}else {
				// 如果目标结点不是叶子结点，并且 targetNode.left 不为空，我们就找到其前驱结点，用前驱结点替换目标结点
				if(targetNode.left != null) {
					Node<K, V> maxNode = max(targetNode.left);
					targetNode.key = maxNode.key;
					targetNode.value = maxNode.value;
					targetNode = maxNode;
				}else {// 如果目标结点不是叶子结点，并且 targetNode.right 不为空，我们就找到其后继结点，用后继结点替换目标结点
					Node<K, V> minNode = min(targetNode.right);
					targetNode.key = minNode.key;
					targetNode.value = minNode.value;
					targetNode = minNode;
				}
			}
		}
		
		// 删除成功以后，我们需要维护 一下 size
		size--;
		return result;
	}
	
	/**
	 * 	中序遍历
	 */
	public void inOrderTraversal() {
		inOrderTraversal(rootNode);
	}
	
	/**
	 * 	中序遍历的具体实现【递归实现】
	 * @param root
	 */
	private void inOrderTraversal(Node<K, V> root) {
		if(root == null) {
			return;
		}
		if(root.left != null) {
			inOrderTraversal(root.left);
		}
		System.out.println(root);
		if(root.right != null) {
			inOrderTraversal(root.right);
		}
	}
	
	/**
	 * 	这个方法的作用是取 小于 或者 等于 key 的最大值。
	 * 		如果树里面有指定的key ，那么就返回这个key.
	 *  	如果树里面没有指定的key, 那么就返回小于这个key 的最大的key.
	 *  比如说，[1, 2, 3, 5] 中，我们想要找5，那么直接返回 5；如果我们想要找4，因为不存在，所以返回小于4的最大值3.
	 *  
	 *  【思路】
	 *  	1、 最直观的思路其实就是中序遍历，然后挨个比较，找到第一个大于指定 key 的前一个结点的key.【效率太低】
	 *  	
	 *  	2、 因为二叉排序树本身是有序的，所以我们可以使用更高效的二叉查找算法去实现。 具体的实现思路跟二叉查找很像。
	 *  		首先，我们要跟根结点比较，如果key 大于根结点，那么我们就向右找。一直找到第一个大于key 的结点，
	 *  			这个时候我们的查找方向要变成向左查找了。
	 *  			在开始向左查找之前，我们应该保存一下上一个结点的 key 值，因为那是目前我们能找到的小于 key 的最大值。
	 *  			其实向左查找就像以前我们查找 min ，一路向左，一直找到一个小于等于 key 的值为止，min 是走到底。
	 *  			如果向左走到底了，还是没有找到小于等于key 的结点，那么我们就直接返回事先保存的那个key 值。
	 *  
	 *  		然后，如果 key 小于根结点，那么我们就向左找。一直找到第一个小于 key 的结点，同样，我们需要转变方向，开始向右转了。
	 *  			向右转没有关系，你就正常向右转。当你向右转以后，找到第一个大于 key 的结点时，这个时候你又需要转变方向了。
	 *  			那么这时你就需要保存一下上一个结点的 key 值了，因为这是目前我们能找到的小于 key的最大值。
	 *  			接下来就是一路向左，一直找到一个小于等于 key 的值为止， min 是走到底。
	 *  			如果向左走到底了，还是没有找到小于等于 key 的结点，那么我们就直接返回事先 保存的那个  key 值。
	 *  
	 *  		【说明】 上面的查找逻辑是不是感觉很绕？
	 *  			   其实如果我们去画个图，然后通过图去理解就会发现，floor 方法其实就是正常的二叉查找，只不过有点像范围查找。
	 *  			 在每次要从向右转成向左查找的时候，我们要特别注意保存上一个结点的 key 值。
	 *  			 其实我们可以在每次向右转的时候，都保存 key 值，然后向左转的时候不保存。
	 * @param key
	 * @return
	 */
	public K floor(K key) {
		Node<K, V> x = floor(rootNode, key);
		if(x == null) {
			return null;
		}
		return x.key;
	}
	
	/**
	 * 	《算法》 书上的递归实现
	 * @param x
	 * @param key
	 * @return
	 */
	private Node<K, V> floor(Node<K, V> x, K key){
		// 如果 x 是 null ，那么什么都不用比了，直接返回 null
		if(x == null) {
			return null;
		}
		int cmp = key.compareTo(x.key);
		// 如果 key 等于 当前结点的 key 值，那么这个结点就是我们想找的，直接返回
		if(cmp == 0) {
			return x;
		}
		// 如果 key 小于当前结点的 key 值，也就是说当前结点大于 key ，这肯定不是我们想找的，正常向左查找
		if(cmp < 0) {
			return floor(x.left, key);
		}
		// 如果 key 大于当前结点的 Key 值，也就是说当前结点小于 key ，这个值可能就是我们要找的
		// 因为我们想要的是小于 key 的最大值，所以我们继续递归向右找看看有没有更大的
		// 如果有更大的，那么返回的 t 就不会为 null ， 如果没有更大的，那么返回的 t 就会是 null 
		Node<K, V> t = floor(x.right, key);
		if(t != null) {
			return t;
		}else {
			return x;
		}
	}
	
	/**
	 * 	非递归实现
	 * 	要找 小于 等于 key 的最大值
	 * 	如果我们理解了递归的实现，再来看非递归实现，会发现道理是一样的
	 * @param key
	 * @return
	 */
	public K floor2(K key) {
		if(key == null || rootNode == null) {
			return null;
		}
		int cmp = 0;
		Node<K, V> currNode = rootNode;
		K floorKey = null;
		while(true) {
			cmp = key.compareTo(currNode.key);
			// 如果 key 刚好等于 当前结点的 key, 那么直接返回
			if(cmp == 0) {
				return currNode.key;
				// 如果 key 大于当前结点的 key ，反过来说， 当前结点的 key 小于 key ，可能是我们要找的结点
				// 我们先保存起来，然后继续向右找看看有没有更大的
			}else if(cmp > 0) {
				floorKey = currNode.key;
				currNode = currNode.right;
			}else {
				// 如果 key 小于当前结点的key ，反过来说，当前结点的 key 大于 key ，这肯定不是我们想要的结点，直接
				// 向左查找
				currNode = currNode.left;
			}
			
			// 如果 currNode 为 null ，说明遍历到头了，我们直接输出 floorKey 就好了
			if(currNode == null) {
				return floorKey;
			}
		}
	}
	
	/**
	 * 	递归实现
	 * 	这个方法说白了，就是找大于 或者 等于 key 的最小值。
	 * @param key
	 * @return
	 */
	public K ceiling(K key) {
		Node<K, V> x = ceiling(rootNode, key);
		if(x == null) {
			return null;
		}
		return x.key;
	}
	
	/**
	 * 	递归具体实现
	 * @param x
	 * @param key
	 * @return
	 */
	private Node<K, V> ceiling(Node<K, V> x, K key) {
		// 如果当前结点是  null ，那么不用比较了，直接返回 null
		if(x == null) {
			return null;
		}
		int cmp = key.compareTo(x.key);
		// 如果 key 等于 当前结点的 key 值，那么这个结点就是我们要找的结点，直接返回
		if(cmp == 0) {
			return x;
		}
		// 如果 key 大于 当前结点的 key 值，也就是说当前结点小于 key ，不是我们要找的，我们直接向右查找 
		if(cmp > 0) {
			return ceiling(x.right, key);
		}
		// 如果 key 小于 当前结点的 key 值，也就是说 当前结点 大于 key， 这可能是我们要找的，我们先保存一下
		// 然后向左查找看看有没有更小的 
		// （如果递归向左查找，没有找到，最后返回个 null ，那么就返回当前x ； 如果找到了，那么我们就用那个更小的 t）
		Node<K, V> t = ceiling(x.left, key);
		if(t != null) {
			return t;
		}else {
			return x;
		}
	}
	
	/**
	 * 	非递归实现
	 * 	找大于或者等于  key 的 最小值
	 * @param key
	 * @return
	 */
	public K ceiling2(K key) {
		if(key == null || rootNode == null) {
			return null;
		}
		int cmp = 0;
		Node<K, V> currNode = rootNode;
		K ceilingKey = null;
		while(true) {
			cmp = key.compareTo(currNode.key);
			if(cmp == 0) {
				return currNode.key;
			}else if(cmp > 0) {
				currNode = currNode.right;
			}else {
				// 如果 key 小于 currNode 的 key ，也就是说当前结点的 key 大于 key ，这个结点可能是我们想找的
				// 我们就保存一下这个结点的 key ，然后再继续向左找看看，有没有更小的，如果有，替换；
				// 如果没有，最后就输出这个 ceilingKey
				ceilingKey = currNode.key;
				currNode = currNode.left;
			}
			
			if(currNode == null) {
				return ceilingKey;
			}
		}
	}
	
	/**
	 * 	使用中序遍历，返回一个 list 集合，方便外部遍历树
	 * @return
	 */
	public ArrayList<Node<K, V>> nodeList(){
		ArrayList<Node<K, V>> list = new ArrayList<>();
		nodeList(rootNode, list);
		return list;
	}
	
	private void nodeList(Node<K, V> root, ArrayList<Node<K, V>> list) {
		if(root == null) {
			return;
		}
		if(root.left != null) {
			nodeList(root.left, list);
		}
		
		list.add(root);
		
		if(root.right != null) {
			nodeList(root.right, list);
		}
	}
	
	/**
	 * 	为了方便删除元素，我们给结点对象添加了一个 parent 属性
	 * @author Administrator
	 *
	 * @param <V>
	 */
	@SuppressWarnings("hiding")
	public static class Node<K, V>{
		private K key;
		private V value;
		private Node<K, V> left;
		private Node<K, V> right;
		private Node<K, V> parent;
		
		public Node(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}
		
		public Node(K key, V value, Node<K, V> parent) {
			super();
			this.key = key;
			this.value = value;
			this.parent = parent;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "Node [key=" + key + ", value=" + value + "]";
		}
	}
}
