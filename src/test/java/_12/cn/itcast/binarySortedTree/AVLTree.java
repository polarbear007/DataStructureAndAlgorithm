package _12.cn.itcast.binarySortedTree;

public class AVLTree<K extends Comparable<K>, V> {
	private Node<K, V> rootNode;
	private int size;
	
	public int getSize() {
		return size;
	}

	/**
	 * 	返回指定结点的高度，因为我们可能会拿返回值去做算术运算，所以当指定结点 为null时
	 * 	其高度也初始化为 -1, 不然会报异常
	 *  1、 不存在的结点高度为 -1
	 * 	2、 叶子结点高度为 0
	 * 	3、 非叶子结点的高度为其左右子结点中 较高的结点的高度 + 1
	 * @param node
	 * @return
	 */
	private Integer getHeight(Node<K, V> node) {
		if(node != null) {
			return node.height;
		}else {
			return -1;
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
	 * 	删除的情况其实主要有两种：
	 * 	1、 删除叶子结点，这种我们直接找到父结点，然后确定在父结点的哪一侧，直接通过父结点删除
	 * 	2、 删除非叶子结点，我们全部转化成删除叶子结点的情况。
	 * 		如果左子结点不为 null ，我们就找左子树最大值来替换本结点，然后删除最大值所在的结点。（如果该结点不是叶子结点，重复此步骤）
	 * 		如果左子结点为null ，那么右子结点肯定不为null, 那么我们就找右子树最小值来替换本结点，然后删除最小值所在的结点。
	 * 		（同样，如果最小值不是叶子结点，我们重复此步骤）
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
			// 如果 targetNode 刚好是叶子结点，那么我们啥也不用担心，直接 判断 targetNode 在 parentNode 的左侧还是右侧
			// 然后设置成 null 就可以了
			if(targetNode.left == null && targetNode.right == null) {
				// 父结点为 null ，说明是根结点
				if(targetNode.parent == null) {
					rootNode = null;
				}else if(targetNode == targetNode.parent.left){
					targetNode.parent.left = null;
				}else {
					targetNode.parent.right = null;
				}
				updateHeight(targetNode.parent);
				break;
			}else {
				// 如果targetNode 不是叶子结点，那么我们就看看 targetNode.left 是不是空，
				// 如果左子结点不为空，那么我们就优先删除左子树的最大值来替换 targetNode
				if(targetNode.left != null) {
					Node<K, V> maxNode = max(targetNode.left);
					targetNode.key = maxNode.key;
					targetNode.value = maxNode.value;
					targetNode = maxNode;
				}else{
					// 如果 targetNode 左子结点为空，那么我们才去找右子树的最小值来替换 targetNode
					Node<K, V> minNode = min(targetNode.right);
					targetNode.key = minNode.key;
					targetNode.value = minNode.value;
					targetNode = minNode;
				}
				// 【注意】 targetNode 虽然替换成了  maxNode 或者 minNode ，但是并不一定是叶子结点
				// 		  如果 targetNode 替换后，还不是叶子结点，我们就重复上面的步骤，继续替换下去，一直找到叶子结点为止
			}
		}
		
		// 删除了结点以后，我们应该再检查一下，删除以后是否还满足AVL树的规范
		// 我们按照删除的路径，从被实际被删除的结点的父结点开始，一路向上检查
		rebalance(targetNode.parent);
		
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
			// 添加完结点以后我们需要从新添加的结点 开始 检查平衡性
			rebalance(newNode);
		}
		
		return result;
	}
	
	/**
	 * 	从新添加或者删除的结点开始，逐步向上检查
	 * 	当检查到一个不平衡的结点时，我们就以这个结点为中心进行旋转
	 * 	具体应该怎么旋转，应该根据四种情况来分析：
	 * 	1、 left left case
	 * 	2、 left right case 
	 * 	3、 right right case
	 * 	4、 right left case
	 * @param currNode
	 */
	private void rebalance(Node<K, V> currNode) {
		int balance = 0;
		int subBalance = 0;
		while(currNode != null) {
			balance = getBalance(currNode);
			// 说明总体左子树高度大于右子树，需要右旋
			if(balance > 1) {
				// 我们再检查一下 左子树 的左右子树高度差
				subBalance = getBalance(currNode.left);
				// 如果左子树高于右子树，满足 left left case
				if(subBalance > 0) {
					rightRotate(currNode);
				}else {
					// 这里我们不需要考虑 subBalance == 0 的情况，因为不可能出现
					// 如果左子树低于右子树，满足 left right case
					leftRotate(currNode.left);
					rightRotate(currNode);
				}
				break;
			}else if(balance < -1){
				// 说明总体左子树高度小于右子树，需要左旋
				// 我们再检查一下 右子树 的左右子树高度差
				subBalance = getBalance(currNode.right);
				// 如果左子树高于右子树，满足 right left case
				if(subBalance > 0) {
					rightRotate(currNode.right);
					leftRotate(currNode);
				}else {
					// 这里我们不需要考虑 subBalance == 0 的情况，因为不可能出现
					// 满足 right right  case ，直接左旋
					leftRotate(currNode);
				}
				break;
			}else {
				currNode = currNode.parent;
			}
		}
	}
	
	/**
	 * 	如果返回值大于 1 , 则说明我们总体需要左旋
	 * 	如果返回值小于 -1， 则说明我们总体需要右旋
	 * @param node
	 * @return
	 */
	private int getBalance(Node<K, V> node) {
		return getHeight(node.left) - getHeight(node.right);
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
