package _14.cn.itcast.redBlackTree;

/**
 * 	最简单的红黑树
 * @author Administrator
 *
 * @param <K>
 * @param <V>
 */
public class RedBlackTree<K extends Comparable<K>, V> {
	private Node<K, V> rootNode;
	private int size;
	
	/**
	 * 	返回当前的红黑树的结点数量
	 * @return
	 */
	public int getSize() {
		return size;
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
	 * 	根据 key 删除对应的结点，如果key 不存在，返回 null; 如果 key 存在，删除对应的结点，并返回删除结点的值；
	 * 	【注意】 删除结点以后，我们都要根据情况检查一下是否需要重新维护平衡
	 * @param key
	 * @return
	 */
	public V remove(K key) {
		// 我们不允许有 null 键，所以 key 为 null ，肯定删除不成功，返回 null
		// 如果根结点是 null ，说明是一个空树，也直接返回一个 null 值
		if(key == null || rootNode == null) {
			return null;
		}
		
		V result = null;
		
		// 再然后，我们先调用 get 方法，通过 key 去找对应的结点
		Node<K, V> targetNode = getNodeByKey(key);
		
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
				// 父结点为 null ，说明是根结点。 直接把根结点设置成 null 就好了，也不需要检查平衡
				if(targetNode.parent == null) {
					rootNode = null;
				}else {
					// 删除之前，我们先再平衡处理
					removeRebalance(targetNode);
					// 真正地删除targetNode 结点
					if(targetNode == targetNode.parent.left){
						targetNode.parent.left = null;
					}else {
						targetNode.parent.right = null;
					}
				}
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
		return result;
	}
	
	/**
	 * 	真正删除之前，我们先根据要删除的结点进行再平衡
	 * @param removedNode
	 */
	private void removeRebalance(Node<K, V> removedNode) {
		Node<K, V> parent = null;
		Node<K, V> leftSibling = null;
		Node<K, V> rightSibling = null;
		Node<K, V> leftChild = null;
		Node<K, V> rightChild = null;
		
		// 走一遍下面的流程，就可以完成再平衡，所以我们这里搞了一个循环
		while(true) {
			// 如果被删除的结点是红色的，那么不需要再平衡，直接返回即可
			// 【循环出口】
			if(removedNode.isRed()) {
				break;
			}else {
				// 如果被删除的结点是黑色，那么我们需要再看一下兄弟结点的颜色
				// 要拿到兄弟结点之前，我们应该先拿到父结点
				parent = removedNode.parent;
				// 如果父结点为 null ，说明现在 removedNode 就是根结点了，像这种情况，我们也不处理了
				// 说明已经递归到头了
				// 【递归出口】
				if(parent == null) {
					break;
				}else {
					// 如果父结点不为 null ，那么我们就看一下被删除的结点在父结点的左侧还是右侧
					// 【注意】 这里有一个推论： 如果被删除结点是黑色，并且父结点不为空，那么肯定有一个不为空的兄弟结点
					//                     因为根据红黑树从任一结点出发，到每个叶子结点的路径的黑色结点数量相同
					//                     现在父结点不为空，被删除的这一侧至少有一个黑色结点，那么另一侧的兄弟结点如果为null
					//                     最终左右路径的黑色结点数量就不一样了
					
					// 被删除结点在父结点的左侧
					if(removedNode == parent.left) {
						// 那么我们就通过父结点去拿到右兄弟结点
						rightSibling = parent.right;
						// 【循环出口】
						if(rightSibling == null) {
							break;
						}
						// 拿到右兄弟结点以后，我们先看一下右兄弟结点是不是红色
						if(rightSibling.isRed()) {
							// 如果右兄弟结点是红色，那么符合  right case , 直接以父结点为中心左旋
							leftRotate(parent);
							// 左旋以后，现在的父结点里面保存的 key / value / color 其实已经变成以前 右兄弟结点的 key / value / color
							// 我们需要改变一下现在父结点的颜色为黑色
							parent.setBlack();
							// 我们需要改变一下 parent.left 的颜色（其实是左旋过程中创建出来的新结点，保存的是以前父结点的key / value / color）
							// 这里所谓的变颜色，其实就是从黑色变成红色
							parent.left.setRed();
						}else {
							// 如果右兄弟结点是黑色，那么我们先试图获取 右兄弟结点的 左右子结点
							leftChild = rightSibling.left;
							rightChild = rightSibling.right;
							// 如果右子结点不为null,并且颜色是红色，那么肯定符合 right right case ， 我们总体左旋
							if(rightChild != null && rightChild.isRed()) {
								leftRotate(parent);
								// 总体左旋以后， rightChild 会被提升到原来 rightSibling 位置，需要变为黑色
								rightChild.setBlack();
								// parent 保存的是以前的 rightSibling ，这个位置的颜色可以保持以前 parent 的颜色
								parent.color = parent.left.color;
								// parent.left 结点是根据原来的父结点创建出来的新结点，父结点的颜色可能是红色，可能是黑色
								// 可是不管原来什么颜色，现在的位置变成原来被删除结点的位置，颜色必须改成黑色
								parent.left.setBlack();
							}else if(leftChild != null && leftChild.isRed()) {
								// 如果不满足右子结点为红色的条件，才会检查左子结点是否为红色
								// 现在左子结点为红色，满足 right left case ，需要先根据 右兄弟结点先右旋，再根据父结点左旋
								rightRotate(rightSibling);
								// 旋转以后，我们暂时不去管什么颜色，继续以parent 为中心左旋
								leftRotate(parent);
								// 现在 parent 位置保存的其实是 leftChild ，颜色应该设置成以前 parent 的颜色
								parent.color = parent.left.color;
								// parent.left 保存的是以前 parent ，以前的颜色可能是红色，可能是黑色，现在统一设置成黑色
								parent.left.setBlack();
								// parent.right 保存的其实还是 rightSibling , 这个位置的颜色就按原来的黑色就好
							}else {
								// 如果右兄弟结点的左右子结点都不是红色，那么我们直接进行变色处理
								// 可能左兄弟结点的两个子结点都是黑色，可能都是 null ，可能只有一个子结点，而且是黑色
								// 右兄弟结点需要变成红色
								rightSibling.setRed();
								// 父结点如果原来是红色，要设置成黑色
								if(parent.isRed()) {
									parent.setBlack();
								}else {
									//  如果父结点本身已经是黑色，就会变成double black, 那么我们就需要递归向上再检查
									removeRebalance(parent);
								}
							}
						}
						
					}else { // 被删除结点在父结点的右侧
						leftSibling = parent.left;
						// 【循环出口】
						if(leftSibling == null) {
							break;
						}
						// 如果左兄弟结点是红色，那么符合 left case , 我们直接右旋
						if(leftSibling.isRed()) {
							rightRotate(parent);
							// parent 保存的是以前左兄弟结点的 key / value / color, 我们需要变一下颜色（其实是变成黑色）
							parent.setBlack();
							// parent.right 是根据parent 创建出来的一个新结点，需要变一下颜色， 其实是从黑色变成红色
							parent.right.setRed();
						}else {
							// 如果左兄弟结点是黑色, 那么我们就先试图获取 左兄弟结点的 左右子结点
							leftChild = leftSibling.left;
							rightChild = leftSibling.right;
							
							// 如果左兄弟结点的 左子结点不为空，而且是红色，那么肯定符合 left left case ，总体右旋
							if(leftChild != null && leftChild.isRed()) {
								rightRotate(parent);
								// 旋转以后， leftChild 会被提升到以前左兄弟结点的位置，颜色需要变成黑色
								leftChild.setBlack();
								// parent 现在保存的是 leftSibling ，但是颜色应该跟以前的 parent 一致
								parent.color = parent.right.color;
								// parent.right 现在保存的是以前的 parent ，以前的 parent 可能是红色，可能是黑色
								// 这里统一设置成黑色
								parent.right.setBlack();
							}else if(rightChild != null && rightChild.isRed()) {
								// 如果左兄弟结点的 左子结点 不是红色，才会走到这里
								// 现在左兄弟结点的 右子结点 是红色，符合 left right case ，需要先以 leftSibling 左旋，再以parent 右旋
								leftRotate(leftSibling);
								rightRotate(parent);
								// 经过两次旋转以后，现在的parent 保存的是以前的 rightChild, 颜色应该跟以前的 parent 一致
								parent.color = parent.right.color;
								// parent.right 保存的是原来的 parent ，原来的颜色可能是红色，可能是黑色，现在统一设置成黑色
								parent.right.setBlack();
								// parent.left 保存的还是原来的 leftSibling ，转了两次以后，回到原来的位置，颜色不需要改变
							}else {
								// 左兄弟结点的两个子结点可能都是黑色，可能只有一个子结点，并且这个子结点也是黑色，也可能没有子结点
								// 不管怎么样，我们都需要进行变色处理
								// 左兄弟结点肯定要设置成红色
								leftSibling.setRed();
								// 父结点应该设置成黑色（但是如果父结点本身就是黑色的话，那么父结点就会成为 double black ，需要递归向上检查）
								if(parent.isRed()) {
									parent.setBlack();
								}else {
									removeRebalance(parent);
								}
							}
						}
					}
				}
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
	 * 	根据键找对应的值
	 * @param key
	 * @return
	 */
	public V get(K key) {
		Node<K, V> targetNode = getNodeByKey(key);
		return targetNode == null ? null : targetNode.value;
	}
	
	/**
	 * 	根据键找对应的结点，跟基本的二叉查找完全一样
	 * @param key
	 * @return
	 */
	private Node<K, V> getNodeByKey(K key) {
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
				break;
			}else if(cmp > 0) {
				if(currNode.right != null) {
					currNode = currNode.right;
				}else {
					break;
				}
			}else {
				if(currNode.left != null) {
					currNode = currNode.left;
				}else {
					break;
				}
			}
		}
		// 最终返回 result
		return currNode;
	}
	
	/**
	 * 	插入一个键值对
	 * 		如果 key 存在，则值替换，不需要对原有的结构进行重新平衡
	 * 		如果key 不存在，则插入新结点，需要检查是否需要进行重新平衡
	 * @param key
	 * @param value
	 * @return
	 */
	public V put(K key, V value) {
		// 我们不允许 key 为null ，所以如果你插入 null 值，我们直接抛异常
		if(key == null) {
			throw new RuntimeException("key不能为null!");
		}
		
		Node<K, V> newNode = null;
		V result = null;
		
		// 如果根结点是null ，那么我们直接以当前的 key value 创建一个新结点，作为根结点
		if(rootNode == null) {
			newNode = new Node<>(key, value);
			rootNode = newNode;
			// 维护 size
			size++;
		}else {
			Node<K, V> currNode = rootNode;
			int cmp = 0;
			while(true) {
				cmp = key.compareTo(currNode.key);
				// 如果 key 刚好等于当前结点的 key, 那么值替换; 不需要考虑再平衡
				if(cmp == 0) {
					result = currNode.value;
					currNode.value = value;
					break;
				}else if(cmp > 0) { // 如果 key 大于当前结点的key ，说明我们需要向右找
					if(currNode.right != null) {
						currNode = currNode.right;
					}else {
						newNode = new Node<K, V>(key, value, currNode);
						currNode.right = newNode;
						// 插入新结点以后，我们需要检查一下是否需要进行重新再平衡
						insertRebalance(newNode);
						// 插入新结点以后，我们需要维护一下 size;
						size++;
						break;
					}
				}else {
					if(currNode.left != null) {
						currNode = currNode.left;
					}else {
						newNode = new Node<K, V>(key, value, currNode);
						currNode.left = newNode;
						insertRebalance(newNode);
						// 插入新结点以后，我们需要维护一下 size;
						size++;
						break;
					}
				}
			}
		}
		
		// 我们规定根结点的颜色是黑色，所以不管前面颜色怎么变化，最终我们都把根结点的颜色再变回黑色
		rootNode.setBlack();
		return result;
	}
	
	/**
	 * 	插入一个新结点以后，需要调用此方法递归向上检查一下是否需要变色，或者旋转
	 * @param newNode
	 */
	private void insertRebalance(Node<K, V> newNode) {
		// 如果递归到最后，新结点为根结点，那么我们就不处理，直接退出就可以了
		if(newNode == rootNode) {
			return;
		}
		// 如果父结点是红色，那么我们肯定需要进行变色或者旋转
		// 这里 newNode 肯定不会是根结点，所以其父结点也不可能为null, 不需要考虑空指针
		if(newNode.parent.isRed()) {
			// 这里我们需要先根据父结点 求出祖父结点 和 叔叔结点
			// 按理说，我们应该考虑一下祖父结点是否存在的问题，但是其实不需要
			// 	因为祖父结点不存在的话，说明父结点是根结点，而根结点不可能是红色，所以也不可能满足前面的条件
			// 	所以我们这里也不需要考虑祖父结点是否为 Null 的问题
			Node<K, V> grandParent = newNode.parent.parent;
			Node<K, V> uncle = grandParent.left == newNode.parent ? grandParent.right : grandParent.left;
			
			// 如果叔叔结点不为null 而且 颜色是红色，那么我们需要改变 父结点、叔叔结点、祖父结点的颜色
			// 再把祖父结点当成新结点，向上递归检查
			if(uncle != null && uncle.isRed()) {
				newNode.parent.changeColor();
				uncle.changeColor();
				grandParent.changeColor();
				// 再然后，我们把祖父结点当成新结点，向上递归检查
				insertRebalance(grandParent);
			}else {
				// 其余的情况，我们都需要进行旋转处理（叔叔结点为 null 或者 叔叔结点为黑色）
				// 首先，我们看一下父结点是祖父结点的左子结点还是右子结点
				boolean isParentLeft = grandParent.left == newNode.parent;
				boolean isLeft = newNode == newNode.parent;
				// 父结点是左子结点
				if(isParentLeft) {
					// 新结点是左子结点，满足 left left case
					// 右旋
					if(isLeft) {
						// 旋转的时候，我们不改变任何结点的颜色
						rightRotate(grandParent);
						// 旋转以后，我们让父结点的 与 父结点的右子结点的颜色变化一下
						newNode.parent.changeColor();  // 原来的父结点是红色，现在要变成黑色
						newNode.parent.right.changeColor(); // 原来的祖父结点是黑色，现在要变成红色
					}else {
						// 新结点是右子结点，满足 left right case
						// 先根据父结点左旋（左旋过程中不改变任何结点的颜色），现在newNode 变成原来父结点的父结点了
						leftRotate(newNode.parent);
						// 我们再根据 newNode.parent 也就是以前的祖父结点进行右旋
						rightRotate(newNode);
						// 现在我们又需要让新结点位置变到了以前祖父结点的位置，我们需要让新结点 与新结点的右子结点的颜色变化一下
						newNode.changeColor();
						newNode.right.changeColor();
					}
				}else {// 父结点是右子结点
					// 新结点是左子结点，满足 right left case
					if(isLeft) {
						// 我们需要先根据父结点右旋，现在 newNode 变成原来父结点的父结点了
						rightRotate(newNode.parent);
						// 再根据 newNode.parent 也就是以前的祖父结点进行左旋
						leftRotate(newNode.parent);
						// 旋转完以后，新结点变到了以前祖父结点的位置，我们需要让新结点和新结点的左子结点的颜色变化一下
						newNode.changeColor();
						newNode.left.changeColor();
					}else { // 如果新结点是右子结点，满足 right right case 
						// 我们直接以祖父结点为中心左旋就可以了
						leftRotate(grandParent);
						// 然后父结点现在在以前祖父结点的位置，需要进行变色
						newNode.parent.changeColor();
						// 父结点的左子结点，也就是以前的祖父结点，也需要进行变色
						newNode.parent.left.changeColor();
					}
				}
			}
		}else { 
			// 如果父结点是黑色，那么说明我们什么都不需要操作
			return;
		}
	}
	
	/**
	 * 	以 root 结点为中心进行右旋
	 * @param root
	 */
	private void rightRotate(Node<K, V> root) {
		// 首先，根据 root 创建一个新的结点
		Node<K, V> newNode = new Node<K, V>(root.key, root.value, root);
		// 把新结点的颜色设置成跟根结点的颜色一样
		newNode.color = root.color;
		
		// 新结点的右子结点指向 root 的右子结点
		newNode.right = root.right;
		if(newNode.right != null) {
			newNode.right.parent = newNode;
		}
		// 然后把 新结点设置成 root 的右子结点
		root.right = newNode;
		// 再把 新结点的 左子结点指向  root.left.right
		newNode.left = root.left.right;
		if(newNode.left != null) {
			newNode.left.parent = newNode;
		}
		
		// 然后，我们使用 root.left 的key value 去替换 root 的key 和 value
		root.key = root.left.key;
		root.value = root.left.value;
		// 颜色值也需要覆盖
		root.color = root.left.color;
		// 再让 root.left 指向 root.left.left
		root.left = root.left.left;
		if(root.left != null) {
			root.left.parent = root;
		}
	}
	
	/**
	 * 	以 root 结点为中心，进行左旋
	 * @param root
	 */
	private void leftRotate(Node<K, V> root) {
		Node<K, V> newNode = new Node<K, V>(root.key, root.value, root);
		// 把新结点的颜色设置成跟根结点的颜色一样
		newNode.color = root.color;
		
		// 先把新结点的左子结点 指向 根结点的左子结点
		newNode.left = root.left;
		if(newNode.left != null) {
			newNode.left.parent = newNode;
		}
		// 再把 根结点的左子结点指向 新结点
		root.left = newNode;
		
		// 再把 新结点的右子结点，指向 根结点的右子结点的左子结点
		newNode.right = root.right.left;
		if(newNode.right != null) {
			newNode.right.parent = newNode;
		}
		
		// 再然后，我们需要把 根结点的 右子结点 的 key value 覆盖根结点的 key value
		root.key = root.right.key;
		root.value = root.right.value;
		// 颜色值也需要覆盖
		root.color = root.right.color;
		// 保存完以后，根结点的右子结点就没用了
		root.right = root.right.right;
		if(root.right != null) {
			root.right.parent = root;
		}
	}

	/**
	 * 	这是红黑树内部的结点对象
	 * @author Administrator
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Node<K extends Comparable<K>, V>{
		private K key;
		private V value;
		private Node<K, V> left;
		private Node<K, V> right;
		private Node<K, V> parent;
		// 因为插入元素的时候，默认颜色是红色，所以我们使用 false 表示红色，true 表示黑色
		private boolean color;
		
		// 根据键值创建结点对象
		public Node(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}
		
		// 根据键值，父结点创建结点对象
		public Node(K key, V value, Node<K, V> parent) {
			super();
			this.key = key;
			this.value = value;
			this.parent = parent;
		}

		/**
		 * 	查看结点的颜色是否是红色
		 * @return
		 */
		public boolean isRed() {
			return color == false;
		}
		
		/**
		 * 	把结点的颜色设置黑色
		 */
		public void setBlack() {
			color = true;
		}
		
		/**
		 * 	把结点的颜色设置红色
		 */
		public void setRed() {
			color = false;
		}
		
		/**
		 * 	变换结点颜色
		 */
		public void changeColor() {
			color = !color;
		}

		@Override
		public String toString() {
			return "Node [key=" + key + ", color=" + (color? "black" : "red") + "]";
		}
	}
}
