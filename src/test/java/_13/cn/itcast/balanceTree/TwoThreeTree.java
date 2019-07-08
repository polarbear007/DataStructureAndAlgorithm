package _13.cn.itcast.balanceTree;

import java.util.Arrays;

public class TwoThreeTree<K extends Comparable<K>, V> {
	private Node<K, V> rootNode;
	private int size;
	
	/**
	 * 	这个方法必须从叶子结点开始，向上递归，逐渐使树重新平衡
	 * 	【注意】 必须把删除非叶子结点的需求转成删除叶子结点，然后再从叶子结点开始平衡。
	 * 			不然，如果你删除一个很高层的非叶结点时，既要向下递归，又要向上递归，逻辑会非常混乱
	 * @param index
	 */
	public void rebalanceByIndex(Node<K, V> currNode, int index) {
		// 如果是正常的调用，那么我们就需要先考虑从子结点那里借用
		// 子结点没借到，那么我们就考虑结点本身的 size 是否够用
		//    如果够用，直接把左右子结点合并成一个结点，然后删除本身的 key / value / 右子结点
		//    如果不够用，那么我们就考虑先跟兄弟结点借用
		// 	   	跟兄弟结点借用到了，那么我们就左旋或者右旋处理
		//      跟兄弟结点还是没借到，那么我们就需要跟父结点借用
		//			父结点为空，说明本结点是根结点，我们直接删除就可以了，并让rootNode 指针指向null
		//          父结点不为空，那么我们就使用父结点的 key 覆盖当前要删除的 key ，
		//             然后把父结点的key左右结点合并。 再然后递归删除父结点的那个 key
		
		// 如果是递归调用本方法，那么一定不能向子结点借用元素，具体原因在于左右子结点已经合并了
		// 所以下面从 子结点 借用元素的尝试，需要加一层判断
		if(currNode.degree != currNode.size) {
			Node<K, V> leftChild = currNode.children[index];
			// 如果左子结点不为空，而且元素个数大于 1 （考虑后期向b树转换，我们这里考虑得远一些）
			if(leftChild != null && leftChild.size > (leftChild.ORDER + 1) / 2 - 1) {
				currNode.borrowFromLeftChild(leftChild, index);
				return;
			}
			
			Node<K, V> rightChild = currNode.children[index + 1];
			// 如果右子结点不为空，而且元素个数大于 1 （考虑后期向b 树转换，我们这里并不是真正的 1）
			if(rightChild != null && rightChild.size > (rightChild.ORDER + 1) / 2 - 1) {
				currNode.borrowFromRightChild(rightChild, index);
				return;
			}
		}
		
		// 如果没有从子结点借到元素，那么我们就看一下当前结点的 size 是否大于 1, 直接删除本身，然后合并对应的两个子结点
		if(currNode.size > (currNode.ORDER + 1) / 2 - 1) {
			// 首先看一下是不是叶子结点，如果不是的话，我们还需要合并 index 和 index + 1 处的两个子结点
			// 当然，我们还得考虑是不是递归调用，如果是的话，那么在上次调用的时候，子结点已经合并了，这里不需要再次 处理
			if(!currNode.isLeaf() && currNode.size != currNode.degree) {
				currNode.children[index].mergeNode(currNode.children[index + 1]);
				// 合并以后，删除没有用的右子结点
				currNode.removeChildByIndex(index + 1);
			}
			// 然后直接先删除 index 索引处的 key 和 value 值
			// 【注意】 前面 currNode.size != currNode.degree 的判断依赖于 size ，
			//        所以这个方法一定要后面再执行，不然就判断不出来了
			currNode.removeKeyAndValueByIndex(index);
			return;
		}
		
		// 如果前面都没有成功返回的话，那么我们就要考虑一下能不能从 左右 兄弟结点那里借用 或者 从父结点那里借用
		// 	如果想要从 左右 兄弟结点那里借用，得先保证父结点不能为 null, 也就是说当前结点不能是根结点
		// 	如果想从父结点那里借用，同样要求父结点不能是 null
		if(currNode.parent != null) {
			// 首先，我们根据当前的 key 找到当前结点在父结点的 children 数组的索引位置
			// 因为当我们调用此方法时，已经使用小于目标 key 的最大key 去替换过了，所以现在 findIndexByKey 方法可能是正数
			// 当是正数的时候，这个索引值我们可以直接使用，是负数的话，我们照常加上 阶数
			int childrenIndex = currNode.parent.findIndexByKey(currNode.keys[index]);
			if(childrenIndex < 0) {
				childrenIndex += currNode.parent.ORDER;
			}
			
			// 如果左兄弟结点存在，优先考虑从左兄弟结点那里借用
			if(childrenIndex - 1 >= 0) {
				Node<K, V> leftSibling = currNode.parent.children[childrenIndex - 1];
				// 如果左兄弟结点存在，并且左兄弟结点的元素个数超过最小数量
				if(leftSibling.size > (leftSibling.ORDER + 1) / 2 - 1) {
					// 【注意】 这时父结点的 key 索引应该是 childrenIndex - 1
					currNode.borrowFromLeftSibling(leftSibling, childrenIndex - 1, index);
					return ;
				}
			}
			
			if(childrenIndex + 1 < currNode.parent.degree) {
				// 如果左兄弟结点不存在，或者没有元素可以借用，那么我们才考虑从右兄弟结点那里借用。
				Node<K, V> rightSibling = currNode.parent.children[childrenIndex + 1];
				// 如果右兄弟结点存在，并且右兄弟结点的元素个数超过最小数量
				if(rightSibling.size > (rightSibling.ORDER + 1) / 2 - 1) {
					// 【注意】 这时父结点的 key 索引应该是 childrenIndex
					currNode.borrowFromRightSibling(rightSibling, childrenIndex, index);
					return ;
				}
			}
			
			// 如果跟兄弟结点还是没有借到元素的话，那么就只能跟父结点去借了
			// 前面我们已经检查了 ，父结点肯定不为 null ，才能走到这里
			// 当我们向父结点借用的时候，理论上，我们可以借用  childrenIndex - 1 位置的 key ，也可以借用 childrenIndex 位置的key
			// 一般来说，我们优先借用  childrenIndex - 1 的 key 
			int parentKeyIndex = -1;
			if(childrenIndex - 1 >= 0) {
				// 现在我们是借用 childrenIndex - 1 位置的 key ，也就是说当前结点位于借用的 key 的右边
				parentKeyIndex = childrenIndex - 1;
				// 我们先用父结点的 key / value 来覆盖子结点的 key 和 value ，覆盖以后，暂时先不要删除 key / value
				currNode.keys[index] = currNode.parent.keys[parentKeyIndex];
				currNode.values[index] = currNode.parent.values[parentKeyIndex];
				
				// 紧接着我们需要合并 当前结点 和 左兄弟结点， 最后只在 parent.children 里面保留 左子结点
				currNode.parent.children[childrenIndex - 1].mergeNode(currNode);
				// 然后在 parent.children 数组里面 删除当前结点
				currNode.parent.removeChildByIndex(childrenIndex);
			}else {
				// 现在我们是借用 childrenIndex 位置的 key ，也就是说当前结点位于借用的 key 的左边
				parentKeyIndex = childrenIndex;
				// 我们先用父结点的 key / value 来覆盖子结点的 key 和 value，覆盖以后，暂时先不要删除 key / value
				currNode.keys[index] = currNode.parent.keys[parentKeyIndex];
				currNode.values[index] = currNode.parent.values[parentKeyIndex];
				// 紧接着我们需要合并 当前结点 和 右兄弟结点， 最后只在 parent.children 里面保留当前结点
				currNode.mergeNode(currNode.parent.children[childrenIndex + 1]);
				currNode.parent.removeChildByIndex(childrenIndex + 1);
			}
			
			// 现在不管怎么样， currNode 这一层我们都已经平衡好了，把问题丢给父结点
			// 现在问题转换成  删除父结点的  parentKeyIndex , 我们再一次向上递归调用方法
			rebalanceByIndex(currNode.parent, parentKeyIndex);
		}else {
			// 如果走到这里，说明正常调用方法，是直接要删除根结点的元素的
			// 并且前面跟子结点借用元素，也都失败了
			
			// 如果这个方法是递归调用的，那么我们这里并不需要再去合并左右子结点，因为上一个步骤已经完成了
			// 所以这里必须要加一层判断
			if(currNode.degree != currNode.size) {
				currNode.children[index].mergeNode(currNode.children[index + 1]);
				// 合并以后，我们需要删除 右子结点
				currNode.removeChildByIndex(index + 1);
			}
			
			//   这里我们的逻辑就很简单了，只需要删除指定的 key / value ，然后合并对应的左右子结点
			//   这样处理以后，我们再看当前结点的  size 是否为 0 
			//			如果是0，那么 就 把   currNode 指向合并后的子结点
			//          如果不是0， 那么我们什么都不需要处理
			currNode.removeKeyAndValueByIndex(index);
			
			// 现在我们判断 size 是否为 0 , 如果是 0 ，那么我们就移动 rootNode 指向
			// 	如果不是 0 ，那么我们就什么都不做
			if(currNode.size == 0) {
				rootNode = currNode.children[index];
			}
			
		}
	
	}
	
	/**
	 * 	根据 key 删除对应的元素。
	 * 		如果存在 key ，则返回对应的 value
	 * 		如果不存在，那么我们就直接返回 null 就好了
	 * @param key
	 * @return
	 */
	public V remove(K key) {
		// 如果 key 是null 或者 树本身就是空树，那么就不需要找了，直接返回 null 就可以了
		if(key == null || rootNode == null) {
			return null;
		}
		
		V result = null;
		Node<K, V> currNode = rootNode;
		int index = 0;
		while(true) {
			// 先根据 key 找对应的结点
			index = currNode.findIndexByKey(key);
			// 如果index 大于0，说明 currNode 就是对应的结点
			if(index >= 0) {
				// 先保存 result, 再去真正删除 键值
				result = currNode.values[index];
				// 如果当前结点是非叶子结点，那么我们需要使用  getMax() 方法，找到保存小于目标 key 的最大值的结点
				if(!currNode.isLeaf()) {
					Node<K, V> maxNode = getMax(currNode.children[index]);
					// 找到最大值所在的结点以后，我们要使用这个结点的最大值去替换目标结点 指定索引位置的 key 和 value
					currNode.keys[index] = maxNode.keys[maxNode.size - 1];
					currNode.values[index] = maxNode.values[maxNode.size - 1];
					// 替换值以后，我们可以把 currNode 的值指向 maxNode
					currNode = maxNode;
					// index 应该替换成 maxNode.size - 1
					index = maxNode.size - 1;
				}
				// 删除目标结点，并重新平衡
				rebalanceByIndex(currNode, index);
				break;
			}else {
				if(currNode.isLeaf()) {
					break;
				}else {
					currNode = currNode.children[index + currNode.ORDER];
				}
			}
		}
		return result;
	}
	
	/**
	 * 	以指定的结点为根结点，找到并返回这个子树最大的 key 所在的结点
	 * @param node
	 * @return
	 */
	private Node<K, V> getMax(Node<K, V> node) {
		Node<K, V> currNode = node;
		// 如果不是叶子结点，我们就把 currNode 替换成  children[degree - 1]
		while(!currNode.isLeaf()) {
			currNode = currNode.children[currNode.degree - 1];
		}
		
		return currNode;
	}

	/**
	 * 	根据键找值，如果没找到，返回 null
	 * @param key
	 * @return
	 */
	public V get(K key) {
		// 我们是不允许出现 null 值的，所以这里如果 key 是null , 那么肯定是找不到对应的值的 
		// 如果树本身也是空的，那么肯定也是找不到对应的值的
		if(key == null || rootNode == null) {
			return null;
		}
		
		// 最终就是返回 result 
		V result = null;
		Node<K, V> currNode = rootNode;
		int index = 0;
		while(true) {
			index = currNode.findIndexByKey(key);
			// 如果 index 大于或者等于 0 ，那么我们直接根据 index 找到对应的值
			if(index >= 0) {
				result = currNode.values[index];
				break;
			}else {
				// 如果 index 小于 0 ，那么我们要看这个结点是不是叶子结点
				// 如果已经是叶子结点了，那么我们就不再继续找了
				if(currNode.isLeaf()) {
					break;
				}else {
					// 如果不是，那么就移动currNode 的指针，继续遍历
					currNode = currNode.children[index + currNode.ORDER];
				}
			}
		}
		return result;
	}
	
	/**
	 * 	结点元素存满了以后的裂变方法
	 * 	【说明】 结点裂变的索引值位置就使用   阶数除以 2 ，2-3 树就是 3 / 2 = 1， 也就是把索引1所在的键值往上提 
	 * @param targetNode   要分裂的目标结点
	 * @param key		       要添加的新键
	 * @param value		       要添加的新值
	 */
	private void splitNode(Node<K, V> targetNode) {
		Node<K, V> parent = targetNode.parent;
		int splitIndex = targetNode.ORDER / 2;
		// 确定了分裂的索引以后，我们可以先根据这个索引把要提升的 键值保存起来
		K splitKey = targetNode.keys[splitIndex];
		V splitValue = targetNode.values[splitIndex];
		
		// 再把分裂索引右边的 keys / values / children 复制一份，生成一个新的结点
		K[] newKeys = (K[])new Comparable[targetNode.ORDER];
		System.arraycopy(targetNode.keys, splitIndex + 1, newKeys, 0, targetNode.keys.length - splitIndex - 1);
		V[] newValues = (V[])new Object[targetNode.ORDER];
		System.arraycopy(targetNode.values, splitIndex + 1, newValues, 0, targetNode.keys.length - splitIndex - 1);
		Node<K, V>[] newChildren = new Node[targetNode.ORDER + 1];
		if(!targetNode.isLeaf()) {
			System.arraycopy(targetNode.children, splitIndex + 1, newChildren, 0, targetNode.keys.length - splitIndex);
		}
		Node<K, V> newNode = new Node<>(newKeys, newValues, newChildren);
 		
		// 最后，再把已经另外保存的所有相关数据从 targetNode 中删除
		Arrays.fill(targetNode.keys, splitIndex, targetNode.keys.length, null);
		Arrays.fill(targetNode.values, splitIndex, targetNode.values.length, null);
		// 如果targetNode 不是叶子结点，那就需要维护一下 children 数组，把对应的值从 targetNode 中删除
		if(!targetNode.isLeaf()) {
			Arrays.fill(targetNode.children, splitIndex + 1, targetNode.children.length, null);
			// 因为这些子结点已经分配给了 newNode ，所以我们还得遍历一下  newNode 的全部子结点，维护这些子结点的parent属性
			for (int i = 0; i < newChildren.length; i++) {
				if(newChildren[i] != null) {
					newChildren[i].parent = newNode;
				}
			}
		}
		
		// 现在 targetNode 已经分裂完毕，只剩下 [0, splitIndex - 1] 这些元素, 数量为 splitIndex
		targetNode.size = splitIndex;
		if(!targetNode.isLeaf()) {
			// 如果是非叶子结点， targetNode 的 children 只剩下 [0, splitIndex] ，数量为 splitIndex + 1
			targetNode.degree = splitIndex + 1;
		}
		
		// 如果要裂变的目标结点的父结点为空，说明是根结点，我们直接根据分裂索引指向的键值创建一个新的父结点
		// 并让 rootNode 指向这个父结点
		if(parent == null) {
			parent = new Node<>(splitKey, splitValue);
			rootNode = parent;
			
			// 再然后，让父结点的 children[0] 指向 targetNode;  让 children[1] 指向 newNode
			parent.children[0] = targetNode;
			parent.children[1] = newNode;
			parent.degree = 2;
		}else {
			// 如果父结点不为空，那么我们就需要根据 splitKey 把 键值插入父结点
			// 首先，我们得根据 key 找到插入的位置
			//  findIndexByKey 这个方法不仅可以搜索跟 key 值相等的索引值位置，
			//   如果在 keys 数组里面找不到对应的 key 值，那么就会根据比较结果返回应该去 children 数组
			//    中的哪个索引位置的子结点找。 现在我们也不是要去查找值，而是要往父结点插入值。
			//    而其实返回的负索引值刚好就是我们要插入的位置。
			//    【如果你理解不了，可以跟踪一下这个方法 再加上 画图比较一下】
			
			// 【说明】 那么这个方法有没有可能返回正值？  
			//    在这里是不可能返回正值的，因为我们不允许重复的 key，所以你不可以在父结点找到一个跟当前结点一样的key.
			int insertIndex = parent.findIndexByKey(splitKey) + parent.ORDER;
			// 插入上提的键值到父结点，并让父结点指向新分裂出来的结点
			parent.insertByIndex(insertIndex, splitKey, splitValue, newNode);
		}
		// 维护targetNode 和  newNode 的parent 属性
		targetNode.parent = parent;
		newNode.parent = parent;
		
		// 最后，我们再来检查一下 parent 是不是已经满了，如果满了的话，那么还需要再次分裂
		if(parent.isFull()) {
			splitNode(parent);
		}
	}
	
	/**
	 * 	插入元素，如果元素已经存在，那么我们就替换元素，并返回旧的元素值
	 * 			如果元素不存在，那么我们就正常插入
	 * 	【注意】	我们插入元素的位置，都是在叶子结点的位置插入的, 完成插入以后，再检查一下是否需要进行裂变。
	 * @param key
	 * @param value
	 * @return
	 */
	public V put(K key, V value) {
		// 先检查一下 key 是否为 null，如果为 Null ，我们直接报异常
		if(key == null) {
			throw new RuntimeException("key 不能为 null");
		}
		
		V result = null;
		// 如果根结点为 null ，那么我们直接根据 key / value 创建一个新结点，
		// 然后使用 rootNode 指向这个新结点
		if(rootNode == null) {
			rootNode = new Node<K, V>(key, value);
			size++;
		}else {
			Node<K, V> currNode = rootNode;
			// 保存根据 key 找到的索引值
			int index = 0;
			while(true) {
				index = currNode.findIndexByKey(key);
				// 如果 index 是正数，那么说明key 已经存在，我们就值替换就可以了
				if(index >= 0) {
					// 先保存原来的值
					result = currNode.values[index];
					// 然后替换新的值
					currNode.values[index] = value;
					break;
				}else { 
					// 如果索引值小于零，那么说明没有直接找到该键值的索引，我们这里再加上阶数，把索引变成正数
					index += currNode.ORDER;
					// 如果 index 是负数，那么说明 key 没在在当前结点上，返回的是 children 数组的索引
					// 我们先判断一下这个结点是不是叶子结点，如果不是叶子结点，那么我们就根据children 索引
					// 去下一个结点寻找了
 					if(!currNode.isLeaf()) {
 						currNode = currNode.children[index];
 					}else { 
 						// 如果是叶子结点，那么我们也不往下面去找了，直接准备添加新元素了
 						// 添加的索引其实就是 index
 						// 这里我们可以直接添加，因为 keys 和 values 数组长度预留一位，所以我们可以先添加，再判断是否已经满了
 						currNode.insertByIndex(index, key, value);
 						// 如果已经满了，那么我们需要对当前结点进行分裂，保持 2-3树的规则 
 						if(currNode.isFull()) {
 							splitNode(currNode);
 						}
 						// 只要不是key 相同，替换旧值，我们都需要维护 size
 						size++;
 						// 最后再退出循环即可
 						break;
 					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 	为了方便外部测试 node 方法，我们这里把 Node 设置成 public static 
	 * 	【说明】 keys 和 values 是平行数组，当我们添加、删除、移动keys 数组的时候，values 数组一定要跟着移动 
	 * 	
	 * 	【说明】 keys 和 values 数组的长度，按理说只要   阶数 - 1   就够了， 但是为了方便，我们直接让其长度等于 阶数
	 * 		  children 数组的长度，按理说只要  阶数 就够了，但是为了方便先添加再裂变，我们直接让其长度等于 阶数 + 1
	 *
	 * @author Administrator
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Node<K extends Comparable<K>, V>{
		// 平衡树的阶，是指所有节点的最大度数，或者说是指定一个结点最多可以有多少个子结点
		private final int ORDER = 3;
		// size 指当前结点实际保存的键值数量，如果size == 3 说明这个结点已经满了
		private int size;
		// 结点的度，是指当前结点实际拥有的子结点数量
		// 叶子结点的度一度是 0； 
		// 非叶子结点的size 为1 的话，那么 degree 肯定为 2； 
		// 非叶子结点的 size 为2 的话，那么degree 肯定为 3；
		private int degree;
		// 保存多个键的数组
		private K[] keys;
		// 保存多个值的数组
		private V[] values;
		// 保存多个子结点的数组 
		private Node<K, V>[] children;
		// 保存父结点
		private Node<K, V> parent;
		
		// 构造函数
		public Node() {
			super();
		}
		
		// 构造函数
		public Node(K[] keys, V[] values, Node<K, V>[] children) {
			this.keys = keys;
			this.values = values;
			this.children = children;
			// 维护 size 和 degree
			for (int i = 0; i < keys.length; i++) {
				if(keys[i] != null) {
					size++;
				}else {
					break;
				}
			}
			
			for (int i = 0; i < children.length; i++) {
				if(children[i] != null) {
					degree++;
				}else {
					break;
				}
			}
		}

		// 构造函数
		public Node(K key, V value) {
			keys = (K[])new Comparable[ORDER];
			values = (V[]) new Object[ORDER];
			children = new Node[ORDER + 1];
			keys[0] = key;
			values[0] = value;
			size++;
		}
		
		// 是否是叶子结点
		public boolean isLeaf() {
			return degree == 0;
		}
		// 此结点是否已经满了，如果满了，则需要裂变
		// 所谓的满了，应该是等于 树的阶 - 1 , 但是我们为了先添加后检查，所以数组长度全部加1
		public boolean isFull() {
			return size == ORDER;
		}
		
		// 根据传入的 key 返回 key 在 keys 数组中对应的索引值
		// 因为 keys 数组的最长度就为 3 , 所以我们返回的正常索引就 0 和 1
		// 【说明】 如果传入的 key 跟 keys 都没有相等的，那么很可能就需要再去找对应的子结点
		//        所以我们这里直接顺便把对应的子结点的索引也给返回出去
		//        2-3 树子结点最多可能有三个，0、1、2，所以我们整体左移三个单位，根据比较结果，
		//        返回 -3， -2， -1
		// 【说明】 这里我们不考虑 结点 keys 里面没有任何元素的情况，因为正常的2-3树里面不会出现这种情况
		public int findIndexByKey(K key) {
			int cmp = 0;
			int i = 0;
			while(true) {
				if(keys[i] != null) {
					cmp = key.compareTo(keys[i]);
					if(cmp == 0) {
						return i;
					}else if(cmp > 0) {
						// 如果 key 大于 keys[i], 那么我们让 i++, 向后查找
						// 如果 i++ 以后， keys[i] 不为 null ，那么我们就进行下一次循环
						i++;
						// 如果 i 索引已经越界，或者如果后面已经没有其他键了
						// 那么我们也不用进行下一次循环了，可以直接确定并返回 children 数组索引
						// 为了表示区别，我们让 children 索引全部减去 阶数
						if(i > keys.length - 1 || keys[i] == null) {
							return i - ORDER;
						}
					}else {
						// 如果 key 小于 keys[i], 那么我们也不需要向后找了，可以直接确定并返回 children 数组索引
						return i - ORDER;
					}
				}
			}
		}
		
		/**
		 * 	根据指定的索引值插入键值
		 * 	【注意】 我们需要同时操作 keys 和  values 数组
		 * 	【注意】 如果 keys[index] 上面的元素已经存在，那么我们需要先后移，给新元素腾个位置出来
		 * 	【说明】 腾位置的时候，我们不需要考虑会越界，因为我们在插入之前，会先调用 isFull 方法检查是否已经满了
 		 * @param index
		 * @param key
		 * @param value
		 */
		public void insertByIndex(int index, K key, V value) {
			// 先根据索引进行位移
			// 因为我们事先已经规定 key 不能为 null ，所以我们这里可以通过不为空判断
			if(keys[index] != null) {
				for (int i = size - 1; i >= index; i--) {
					keys[i + 1] = keys[i];
					values[i + 1] = values[i];
				}
			}
			// 不管 Index 位置有没有元素，现在都可以直接插入元素了
			keys[index] = key;
			values[index] = value;
			// 插入以后，记得维护 size 
			size++;
		}
		
		/**
		 * 	根据指定的索引，插入 键 、 值 和 新的子结点
		 * @param index
		 * @param key
		 * @param value
		 * @param newNode
		 */
		public void insertByIndex(int index, K key, V value, Node<K, V> newNode) {
			// 先根据索引进行位移
			// 因为我们事先已经规定 key 不能为 null ，所以我们这里可以通过不为空判断
			if(keys[index] != null) {
				for (int i = size - 1; i >= index; i--) {
					keys[i + 1] = keys[i];
					values[i + 1] = values[i];
					// keys 和 values 从 Index 位置开始移，而 children 只需要从 index + 1 处开始移
					children[i + 2] = children[i + 1];
				}
			}
			// 不管 Index 位置有没有元素，现在都可以直接插入元素了
			keys[index] = key;
			values[index] = value;
			children[index + 1] = newNode;
			// 插入以后，记得维护 size 
			size++;
			degree++;
		}
		
		/**
		 * 	根据索引值删除 键 和 值
		 * @param index
		 */
		public void removeKeyAndValueByIndex(int index) {
			for (int i = index; i < size; i++) {
				keys[i] = keys[i+1];
				values[i] = values[i+1];
			}
			// 最后，我们要记得维护一下size 属性
			size--;
		}
		
		/**
		 * 	根据索引值删除 children 数组里面的元素
		 * 	为了保证有序，我们是从指定索引位置开始，让后面的元素值覆盖前面的元素值
		 * @param index
		 */
		public void removeChildByIndex(int index) {
			for (int i = index; i < degree; i++) {
				children[i] = children[i + 1];
			}
			// 最后，我们要记得维护一下degree 属性
			degree--;
		}
		
		
		/**
		 * 	把其他结点的数据合并到本结点
		 * 	一般都是左结点去合并右结点
		 * @param otherNode
		 */
		public void mergeNode(Node<K, V> otherNode) {
			// 先复制数据
			System.arraycopy(otherNode.keys, 0, keys, size, otherNode.size);
			System.arraycopy(otherNode.values, 0, values, size, otherNode.size);
			System.arraycopy(otherNode.children, 0, children, size + 1, otherNode.size + 1);
			// 然后遍历children 数组，设置所有子结点的parent 属性
			for (int i = 0; i < otherNode.children.length; i++) {
				if(otherNode.children[i] != null) {
					otherNode.children[i].parent = this;
				}
			}
			// 维护 size 和 degree
			size = size + otherNode.size;
			degree = degree + otherNode.degree;
		}
		
		/**
		 * 	借用左子结点的最大值的 key / value 来替换当前结点的 index 位置的 key 和 value
		 * @param leftChild
		 * @param index
		 */
		public void borrowFromLeftChild(Node<K, V> leftChild, int index) {
			// 把使用左子结点的 key / value 值去覆盖当前结点的 key /value ，因为是覆盖，所以不需要维护 size / degree
			keys[index] = leftChild.keys[leftChild.size - 1];
			values[index] = leftChild.values[leftChild.size - 1];
			// 然后，删除左子结点的最大值 key / value
			leftChild.removeKeyAndValueByIndex(leftChild.size - 1);
			// 再然后，如果左子结点不是叶子结点，那么我们还要合并  children数组  leftChild.size 和 leftChild.size + 1 索引位置的结点
			// 【注意】 删除 key 和 value 时 size 已经减1了
			if(!leftChild.isLeaf()) {
				// 因为我们把非叶子结点的删除都转换成了删除叶子结点，所以可能会存在重复合并的问题, 这里预防一下
				if(leftChild.size != leftChild.degree) {
					leftChild.children[leftChild.size].mergeNode(leftChild.children[leftChild.size + 1]);
				}
				// 合并以后，右子结点的值就没用了，我们删除右子结点
				leftChild.removeChildByIndex(leftChild.size + 1);
			}
		}
		
		/**
		 * 	借用右子结点的最小值 的 key / value 来替换当前结点的 Index 位置的 key 和 value
		 * @param rightChild
		 * @param index
		 */
		public void borrowFromRightChild(Node<K, V> rightChild, int index) {
			// 把使用右子结点的 key / value 值去覆盖当前结点的 key /value ，因为是覆盖，所以不需要维护 size / degree
			keys[index] = rightChild.keys[0];
			values[index] = rightChild.values[0];
			// 然后，删除右子结点的最小值 key / value
			rightChild.removeKeyAndValueByIndex(0);
			// 再然后，如果右子结点不是叶子结点，那么我们还要合并  children数组    0 和 1  索引位置的结点
			if(!rightChild.isLeaf()) {
				// 因为我们把非叶子结点的删除都转换成了删除叶子结点，所以可能会存在重复合并的问题, 这里预防一下
				if(rightChild.size == rightChild.degree) {
					rightChild.children[0].mergeNode(rightChild.children[1]);
				}
				// 合并以后，我们应该删除没用的右子结点
				rightChild.removeChildByIndex(1);
			}
		}
		
		/**
		 * 	借用 左兄弟结点的最大值， 其实就是右旋
		 * 	【注意】 我们在这个方法里面不检查，所以调用方法前，应该先检查一下  leftSibling 元素个数足够借
		 * @param leftSibling
		 * @param index
		 */
		public void borrowFromLeftSibling(Node<K, V> leftSibling, int parentKeyIndex, int currentKeyIndex) {
			// 首先，我们让父结点的 key / value 覆盖当前结点的 key / value
			keys[currentKeyIndex] = parent.keys[parentKeyIndex];
			values[currentKeyIndex] = parent.values[parentKeyIndex];
			
			// 然后，左兄弟结点的最大值 key / value 去覆盖父结点的 key / value
			parent.keys[parentKeyIndex] = leftSibling.keys[leftSibling.size - 1];
			parent.values[parentKeyIndex] = leftSibling.values[leftSibling.size - 1];
			// 然后 左兄弟结点的 key 和 value 就可以删除了
			leftSibling.removeKeyAndValueByIndex(leftSibling.size - 1);
			
			// 如果不是叶子结点，我们再来维护  children
			if(!isLeaf()) {
				// 再然后，我们把当前结点要删除的那个 key 对应的两个左右子结点合并成一个结点
				// 因为我们把非叶子结点的删除都转换成了删除叶子结点，所以可能会存在重复合并的问题, 这里预防一下
				if(size != degree) {
					children[currentKeyIndex].mergeNode(children[currentKeyIndex + 1]);
				}
				// 合并以后，这个合并的结点应该放在原来右子结点的位置, currentKeyIndex 位置应该让出来，放左兄弟结点的右子结点
				children[currentKeyIndex + 1] = children[currentKeyIndex];
				// 【注意】 前面删除兄弟结点的 key / value 时，已经维护过 size, 所以这里应该 size + 1
				children[currentKeyIndex] = leftSibling.children[leftSibling.size + 1];
				// 最后，我们还应该再维护 兄弟结点的 children
				leftSibling.removeChildByIndex(leftSibling.size + 1);
			}
		}
		
		/**
		 * 	借用右兄弟结点的最小值，其实就是左旋
		 * 	【注意】 我们在这个方法里面不检查，所以调用方法前，应该先检查一下  rightSibling 元素个数足够借
		 * @param rightSibling
		 * @param parentKeyIndex
		 * @param currentKeyIndex
		 */
		public void borrowFromRightSibling(Node<K, V> rightSibling, int parentKeyIndex, int currentKeyIndex) {
			// 首先，我们让父结点的 key / value 覆盖当前结点的 key / value
			keys[currentKeyIndex] = parent.keys[parentKeyIndex];
			values[currentKeyIndex] = parent.values[parentKeyIndex];
			
			// 然后，右兄弟结点的最小值 key / value 去覆盖父结点的 key / value
			parent.keys[parentKeyIndex] = rightSibling.keys[0];
			parent.values[parentKeyIndex] = rightSibling.values[0];
			// 然后 右兄弟结点的 key 和 value 就可以删除了
			rightSibling.removeKeyAndValueByIndex(0);
			
			if(!isLeaf()) {
				// 再然后，我们把当前结点要删除的那个 key 对应的两个左右子结点合并成一个结点
				// 合并以后，右子结点的值就没用了，刚好把位置让出来
				// 因为我们把非叶子结点的删除都转换成了删除叶子结点，所以可能会存在重复合并的问题, 这里预防一下
				if(size != degree) {
					children[currentKeyIndex].mergeNode(children[currentKeyIndex + 1]);
				}
				// 现在我们需要把右兄弟结点第一个key 的左子结点，放到刚才合并以后让出来的右子结点的位置
				children[currentKeyIndex + 1] = rightSibling.children[0];
				// 最后，右兄弟结点第一个key 的左子结点可以删除了，我们维护一下兄弟结点的 children
				rightSibling.removeChildByIndex(0);
			}
		}
		
		@Override
		public String toString() {
			return Arrays.toString(keys);
		}
	}
}
