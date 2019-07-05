package _12.cn.itcast.binarySortedTree;

public class AVLTree<K extends Comparable<K>, V> {
	private Node<K, V> rootNode;
	private int size;
	
	public int getSize() {
		return size;
	}

	/**
	 * 	返回指定结点的高度，因为我们可能会拿返回值去做算术运算，所以当指定结点 为null时
	 * 	其高度也初始化为 0 , 不然会报异常
	 * @param node
	 * @return
	 */
	private Integer getHeight(Node<K, V> node) {
		if(node != null) {
			return node.height;
		}else {
			return 0;
		}
	}
	
	/**
	 * 	返回根结点右子树的 高度
	 * @return
	 */
	public Integer getRootRightHeight() {
		return getHeight(rootNode.right);
	}
	
	/**
	 * 	返回根结点左子树的 高度
	 * @return
	 */
	public Integer getRootLeftHeight() {
		return getHeight(rootNode.left);
	}
	
	/**
	 * 	返回根结点的高度，如果根结点为 null ，那么返回 0
	 * @return
	 */
	public Integer getRootNodeHeight() {
		return getHeight(rootNode);
	}
	
	/**
	 * 	右旋 操作
	 * @param root
	 */
	private void rightRotate(Node<K, V> root) {
		System.out.println("进行右旋");
		// 右旋之前，我们应该先看一下根结点的左子树是否需要先 进行 左旋
		if(getHeight(root.left.right) - getHeight(root.left.left) > 0) {
			leftRotate(root.left);
		}
		
		//根据根结点的 key 和 value 创建一个新的结点
		Node<K, V> newNode = new Node<K, V>(root.key, root.value);
		// 然后把新结点的 right 指向 根结点的 right 
		newNode.right = root.right;
		if(newNode.right != null) {
			newNode.right.parent = newNode;
		}
		
		// 再然后把新结点的 left 指向根结点的 左子结点的 右子结点
		newNode.left = root.left.right;
		if(newNode.left != null) {
			newNode.left.parent = newNode;
		}
		
		// 最后，让 根结点的 right 指向新结点
		root.right = newNode;
		newNode.parent = root;
		
		
		// 再然后，我们要让根结点的 左子结点 的键和值替换根结点的键 和值
		root.key = root.left.key;
		root.value = root.left.value;
		
		// 再然后，我们还要让根结点的 左子结点 指向 原来左子结点的左子结点
		root.left = root.left.left;
		if(root.left != null) {
			root.left.parent = root;
		}
		
		// 最后，我们一定要记得更新那个新结点的高度（会顺带连同根结点一起更新）
		updateHeight(newNode);
	}
	
	/**
	 * 	以指定结点为根结点，进行左旋操作
	 * @param root
	 */
	private void leftRotate(Node<K, V> root) {
		// 进行左旋转之前，先检查一下是否需要进行右旋
		if(getHeight(root.right.left) - getHeight(root.right.right) > 0) {
			rightRotate(root.right);
		}
		// 首先，根据当前的根结点创建一个新的结点
		Node<K, V> newNode = new Node<K, V>(root.key, root.value);
		// 把新结点的左子结点指向根结点的左子结点
		newNode.left = root.left;
		if(newNode.left != null) {
			newNode.left.parent = newNode;
		}
		// 把新结点的右子结点指向 根结点的 右子结点的左子结点
		newNode.right = root.right.left;
		if(newNode.right != null) {
			newNode.right.parent = newNode;
		}
		// 再然后，让根结点的 左子结点 指向  新结点
		root.left = newNode;
		newNode.parent = root;
		
		// 再然后，我们需要让根结点的右子结点的 键和值 都赋给根结点
		root.key = root.right.key;
		root.value = root.right.value;
		
		// 再然后，我们需要让根结点的右子结点指向 根结点右子结点的右子结点
		root.right = root.right.right;
		if(root.right != null) {
			root.right.parent = root;
		}
		
		// 左旋完成以后，我们 需要更新一下 newNode 和 root 的height 属性
		// 因为newNode 的父结点就是 root，所以其实我们只需要指定更新 newNode 即可
		updateHeight(newNode);
	}
	
	/**
	 * 	非递归实现
	 * 	要找 小于 等于 key 的最大值
	 * 	如果我们理解了递归的实现，再来看非递归实现，会发现道理是一样的
	 * @param key
	 * @return
	 */
	public K floor(K key) {
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
	 * 	非递归实现
	 * 	找大于或者等于  key 的 最小值
	 * @param key
	 * @return
	 */
	public K ceiling(K key) {
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
	 * 	删除有三种情况：
	 * 	1、 删除的结点是叶子结点，直接删除就可以了
	 * 	2、 删除的结点有一个子结点，左子结点，或者右子结点
	 * 	3、 删除的结点有两个子结点
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
		
		// 再然后，我们就可以开始考虑删除的三种情况了
		// 如果是叶子结点，那么我们需要考虑两个情况： 
		//   1、目标结点是根结点
		//   2、 目标结点不是根结点
		if(targetNode.left == null && targetNode.right == null) {
			// 目标结点是根结点，说明整个树就一个根结点
			if(targetNode.parent == null) {
				rootNode = null;
			}else {
				// 如果目标结点是父结点的左结点
				if(targetNode.parent.left == targetNode) {
					targetNode.parent.left = null;
				}else {
					targetNode.parent.right = null;
				}
				// 更新一下 height 属性
				updateHeight(targetNode.parent);
			}
			// 如果目标结点左结点不为空，而右结点为空
			// 那么我们需要考虑两个情况：
			//   1、 目标结点是根结点
			//   2、 目标结点不是根结点
		}else if(targetNode.left != null && targetNode.right == null) {
			// 如果删除的是根结点，那么我们直接使 rootNode 指向 targetNode.left 就可以了
			if(targetNode.parent == null) {
				rootNode = targetNode.left;
			}else {
				// 如果目标结点是父结点的左结点
				if(targetNode.parent.left == targetNode) {
					targetNode.parent.left = targetNode.left;
				}else {
					// 如果目标结点是父结点的右结点
					targetNode.parent.right = targetNode.left;
				}
				// 更新一下 height 属性
				updateHeight(targetNode.parent);
			}
			// 不管是什么目标结点的父结点是什么情况，不管目标结点是父结点的左子结点还是右子结点
			// 最后 目标结点的左子结点的父结点都要指向目标结点的父结点（有可能为 null）
			targetNode.left.parent = targetNode.parent;
		}else if(targetNode.left == null && targetNode.right != null) {
			// 如果删除的是根结点，那么我们直接使 rootNode 指向 targetNode.right 就可以了
			if(targetNode.parent == null) {
				rootNode = targetNode.right;
			}else {
				// 如果目标结点是父结点的左结点
				if(targetNode.parent.left == targetNode) {
					targetNode.parent.left = targetNode.right;
				}else {
					// 如果目标结点是父结点的右结点
					targetNode.parent.right = targetNode.right;
				}
				// 更新一下 height 属性
				updateHeight(targetNode.parent);
			}
			// 不管是什么目标结点的父结点是什么情况，不管目标结点是父结点的左子结点还是右子结点
			// 最后 目标结点的右子结点的父结点都要指向目标结点的父结点（有可能为 null）
			targetNode.right.parent = targetNode.parent;
		}else {
			// 现在只剩下 目标结点 有两个子结点的这种可能了
			// 这里我们有两种处理思路：
			// 1、 先删除目标结点的  右子树的最小值，然后 使用右子树的最小值来替换目标结点的值
			// 2、 先删除目标结点的  左子树的最大值，然后 使用左子树的最大值来替换目标结点的值
			V deleteMin = deleteMin(targetNode.right);
			targetNode.value = deleteMin;
		}
		// 删除成功以后，我们需要维护 一下 size
		size--;
		
		// 删除元素以后，我们要检查一下左子树和右子树的高度，看看是否需要进行旋转
		if(getRootRightHeight() - getRootLeftHeight() > 1) {
			leftRotate(rootNode);
		}else if(getRootLeftHeight() - getRootRightHeight() > 1) {
			rightRotate(rootNode);
		}
		
		return result;
	}
	
	/**
	 * 	删除指定根结点的子树的最小结点
	 * @param root
	 */
	private V deleteMin(Node<K, V> root) {
		if(root == null) {
			throw new RuntimeException("子树根结点不能为 null");
		}
		Node<K, V> targetNode = min(root);
		// 这里我们不需要考虑 targetNode 为 null 的情况，因为只要 root 不为 null，就肯定会有最小值
		V result = targetNode.value;
		// 某个子树里面最小的结点可以肯定两点就是： 左子结点为 null
		// 另外一点就是： 如果目标结点的父结点不为 null ，那么目标结点肯定是父结点的左子结点
		
		// 如果目标结点的父结点是 null ，那么说明目标结点就是根结点
		// 那么我们就让目标结点的右子结点(没有左子结点)去代替根结点
		if(targetNode.parent == null) {
			rootNode = targetNode.right;
		}else {
			// 如果目标结点不是根结点，那么我们就需要让目标结点的父结点的 left 指向目标结点的右子结点
			targetNode.parent.left = targetNode.right;
			// 更新一下 height 属性，因为 targetNode 已经被删除，所以我们不需要考虑targetNode 的 height 值
			updateHeight(targetNode.parent);
		}
		// 不管目标结点是不是根结点，只要 targetNode.right 不为空，
		// 我们需要维护一下targetNode.right 结点的parent参数
		if(targetNode.right != null) {
			targetNode.right.parent = targetNode.parent;
		}
		return result;
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
	
	/**
	 * 	删除指定子树的最大结点
	 * @param root
	 */
	private V deleteMax(Node<K, V> root) {
		if(root == null) {
			throw new RuntimeException("子树根结点不能为 null");
		}
		// 先找到要删除的目标结点
		Node<K, V> targetNode = max(root);
		// 这里我们不需要考虑 targetNode 为 null 的情况，因为只要 root 不为 null，就肯定会有最大值
		V result = targetNode.value;
		// 因为目标结点是最大值，所以我们可以确认两点：
		// 1、 targetNode.right 一定是 null
		// 2、 targetNode 的parent 属性如果不是 null 的话，那么 targetNode 一定是父结点的右子结点
		
		// 如果目标结点的父结点为 null ，那么说明 targetNode 就是根结点
		// 那么我们直接把 rootNode 替换为 targetNode.left 
		if(targetNode.parent == null) {
			rootNode = targetNode.left;
		}else {
			// 如果目标结点不是根结点，那么我们需要让父结点的右子结点指向 目标结点的左子结点
			targetNode.parent.right = targetNode.left;
			// 更新一下 height 属性值, 因为targetNode 本身已经被删除了，所以我们不需要更新 targetNode
			updateHeight(targetNode.parent);
		}
		// 不管目标结点是不是根结点，只要 目标结点的左子结点不为空，我们都需要维护一下
		if(targetNode.left != null) {
			targetNode.left.parent = targetNode.parent;
		}
		
		return result;
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
		Node<K, V> newNode = new Node<K, V>(key, value);
		int cmp = 0;
		V result = null;
		// 如果根结点为空，那么我们就把 newNode 指定为根结点
		if(rootNode == null) {
			rootNode = newNode;
			size++;
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
					break;
				}else if(cmp > 0) {
					// 如果 cmp 大于0，那么说明添加的 key 比 currNode.key 大，那么我们往右找
					if(currNode.right != null) {
						currNode = currNode.right;
					}else {
						// 如果currNode 右子结点为空，那么我们直接把 newNode 添加到currNode 的右子结点上
						currNode.right = newNode;
						newNode.parent = currNode;
						// 维护size 和  height
						size++;
						updateHeight(currNode);
						break;
					}
				}else {
					if(currNode.left != null) {
						currNode = currNode.left;
					}else {
						currNode.left = newNode;
						newNode.parent = currNode;
						// 维护size 和  height
						size++;
						updateHeight(currNode);
						break;
					}
				}
			}
		}
		
		// 添加元素以后，我们要检查一下左子树和右子树的高度，看看是否需要旋转
		if(getRootRightHeight() - getRootLeftHeight() > 1) {
			leftRotate(rootNode);
		}else if(getRootLeftHeight() - getRootRightHeight() > 1) {
			rightRotate(rootNode);
		}
		
		return result;
	}
	
	/**
	 * 	添加或者删除结点的时候，需要更新一下 指定结点的 height 属性值
	 * @param node
	 */
	private void updateHeight(Node<K, V> node) {
		// 如果一个结点变成了叶子结点，那么直接设置成 0
		if(node.left == null && node.right == null) {
			node.height = 0;
		}else {
			// 如果一个结点不是叶子结点，那么我们取 子结点的高度最大值 再加 1
			node.height = Math.max(node.left == null ? 0 : node.left.height, node.right == null ? 0 : node.right.height) + 1;
		}
		if(node.parent != null) {
			updateHeight(node.parent);
		}
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
	
	public static class Node<K, V>{
		private K key;
		private V value;
		private Node<K, V> left;
		private Node<K, V> right;
		private Node<K, V> parent;
		private int height;
		
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
			return "Node [key=" + key + ", value=" + value + ", height=" + height + "]";
		}
	}
}
