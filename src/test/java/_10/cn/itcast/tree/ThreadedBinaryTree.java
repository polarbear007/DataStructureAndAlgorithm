package _10.cn.itcast.tree;

import java.util.Stack;

import _03.cn.itcast.queue.LinkedListQueue;
import _05.cn.itcast.stack.LinkedListStack;

public class ThreadedBinaryTree<T> {
	// 一根树肯定要有一个根结点
	private Node<T> rootNode;
	// 维护一个 size 变量，用来表示二叉树里面保存的节点个数
	private int size;
	// 为了使用递归线索化二叉树，我们维护一个 preNode ，让所有的线索化方法可以共享此变量
	private Node<T> preNode;
	
	// 构造方法里面传入一个数组，我们会在构造方法里面遍历这个数组，并把数据的元素都添加到树结构中
	public ThreadedBinaryTree(T[] dataArray) {
		if(dataArray != null && dataArray.length > 0) {
			for (int i = 0; i < dataArray.length; i++) {
				add(dataArray[i]);
			}
			// 添加完全部元素以后，我们直接线索化
			//inOrderThreading();
			//inOrderThreading(rootNode);
			//preOrderThreading(rootNode);
			// preOrderThreading();
			 postOrderThreading(rootNode);
		}
	}
	
	/**
	 * 后序线索化里面的后继结点只会指向父结点，而父结点本身的 right 属性本身可能就是有意义的
	 * 因此，你无法父 前序 和 中序 那样，通过后继结点连成一条线
	 * 
	 * ==> 因此，我们遍历 后继线索化二叉树 的时候，还是得使用栈结构来辅助回溯
	 *     
	 * ==> 【说明】 其实后序线索化二叉树的遍历，不仅不能提高多少性能，还会让遍历的逻辑变得更复杂，如果是为了提高遍历的效率，
	 * 			  我们不建议这样线索化。
	 * 		
	 * @param root
	 */
	public void postOrderTraversal() {
		if(rootNode != null) {
			Stack<Node<T>> nodeStack = new Stack<>();
			Node<T> currNode = rootNode;
			while(true) {
				// 如果 currNode.leftType == 0 ，我们就一直往左下方向走，
				// 如果 currNode.rightType == 0 ，我们就直接压栈， currNode.rightType == 1时不压栈
				// 因为如果一个结点有后继结点的话，他的上一个结点肯定也是 rightType == 1 ，我们可以直接
				// 通过后继结点找到它
				while(currNode.leftType == 0) {
					if(currNode.rightType == 0) {
						nodeStack.push(currNode);
					}
					currNode = currNode.left;
				}
				
				// 退出循环以后，现在currNode.leftType 肯定等于 1 ， 而且其 currNode.rightType 肯定等于1
				// 所以我们直接循环输出一串
				while(currNode.rightType == 1) {
					System.out.println(currNode.data);
					currNode = currNode.right;
				}
				
				// 退出循环以后，现在 currNode.rightType 肯定等于 0， 而且这个结点肯定是现在保存在栈中的
				// 如果currNode 刚好等于栈顶元素，那么我们直接打印，然后弹出栈顶的元素
				if(currNode == nodeStack.peek()) {
					System.out.println(currNode.data);
					if(currNode == rootNode) {
						break;
					}
					nodeStack.pop();
				}
				
				currNode = nodeStack.peek();
				// 当二叉树层数很多时，可能会有多个连续的   xxx, null , xxx,  null 
				// 我们需要循环处理 null 值
				// 一旦我们看到 null 值，我们就 弹出null 值，然后再直接打印下一个元素（因为右子结点已经处理完了，不要再重复处理）
				while(currNode == null) {
					// 先弹出null 值
					nodeStack.pop();
					// 再弹出下一个栈顶元素，这一次我们已经是第二次访问这个元素了，不要再去检查其右边的情况了，直接输出
					currNode = nodeStack.pop();
					System.out.println(currNode.data);
					if(currNode == rootNode) {
						return;
					}else {
						currNode = nodeStack.peek();
					}
				}
				
				// 前面的循环处理，可以保证 currNode 肯定不是 null ，所以我们可以放心地查检右边
				if(currNode.rightType == 0 && currNode.right != null) {
					currNode = currNode.right;
					// 一旦我们切换到右子树，我们就压入一个 null 值，防止下次循环看到当前栈顶结点时，又检查右边，死循环
					nodeStack.push(null);
				}
			}
		}
	}
	
	
	/**
	 * 按后序遍历的顺序对二叉树进行线索化
	 * 使用递归的方式实现
	 * @param root
	 */
	private void postOrderThreading(Node<T> root) {
		if(root != null) {
			if(root.left != null) {
				postOrderThreading(root.left);
			}
			
			if(root.right != null) {
				postOrderThreading(root.right);
			}
			
			// 原来我们是在这里直接打印一下 root 结点携带的数据就可以了
			// System.out.println(root.data);
			
			// 现在我们需要看一下当前结点的左子结点是否是 null
			//   如果是 null ，那么我们就赋与其不一样的含义
			//   如果不是，前面已经递归处理了
			if(root.left == null) {
				root.left = preNode;
				root.leftType = 1;
			}
			
			// 这里我们处理上一个遍历的结点的 right
			if(preNode != null && preNode.right == null) {
				preNode.right = root;
				preNode.rightType = 1;
			}
			
			// 推进 preNode 指针
			preNode = root;
		}
	}
	
	/**
	 * 使用前序遍历顺序，遍历线索化以后的二叉树
	 */
	public void preOrderTraversal() {
		Node<T> currNode = rootNode;
		while(currNode != null) {
			while(currNode.leftType == 0) {
				System.out.println(currNode.data);
				currNode = currNode.left;
			}
			
			// currNode.leftType == 1 的结点，一般来说，右结点类型可能也是1
			// 如果右结点类型也是1 ,那么我们再次 推进currNode 指针并输出结点数据
			while(currNode.rightType == 1) {
				System.out.println(currNode.data);
				currNode = currNode.right;
			}
			
			// 线索化以后，只有最后一个结点的 right 属性才会是 null 
			// 如果是最后一个结点，那么我们需要手动推进currNode指针
			// 才能最终退出循环。 退出循环前，再打印一下最后的结点
			if(currNode.right == null) {
				System.out.println(currNode.data);
				currNode = currNode.right;
			}
		}
	}
	
	/**
	 * 按前序遍历的顺序进行线索化
	 * 使用非递归的方式来实现
	 */
	private void preOrderThreading() {
		if(rootNode != null) {
			LinkedListStack<Node<T>> nodeStack = new LinkedListStack<>();
			Node<T> currNode = null;
			nodeStack.push(rootNode);
			while(!nodeStack.isEmpty()) {
				// 从栈中弹出一个元素
				currNode = nodeStack.pop();
				// 以前非递归前序遍历我们是直接打印
				// System.out.println(currNode.data);
				// 现在我们需要判断一下当前结点左结点是否为 null
				//    如果是，我们就赋与这个属性新的意义，把left 指向其前驱结点
				//    如果不是，那么我们后面会进行压栈处理
				if(currNode.left == null) {
					currNode.left = preNode;
					currNode.leftType = 1;
				}
				
				// 因为处理右结点需要下一个遍历的结点，所以我们留到本次循环才处理 preNode的右子结点
				if(preNode != null && preNode.right == null) {
					preNode.right = currNode;
					preNode.rightType = 1;
				}
				
				// 按理说，这个语句应该放到最后，但是因为后面的处理都跟 preNode 无关，所以可以提前
				preNode = currNode;
				
				// 再看一下 右结点 是不是 null ，因为先入后出，所以我们先看右边
				if(currNode.rightType == 0 && currNode.right != null) {
					nodeStack.push(currNode.right);
				}
				// 再看一下 左结点 是不是 null
				if(currNode.leftType == 0 && currNode.left != null) {
					nodeStack.push(currNode.left);
				}
			}
		}
	}
	
	/**
	 * 按前序遍历的顺序进行线索化
	 * 使用递归的方式来实现
	 */
	private void preOrderThreading(Node<T> root) {
		if(root != null) {
			//System.out.println(root.data);
			// 原本的前序遍历只需要输出数据即可
			// 现在我们需要找到 left 或者 right 为 null 的结点，给赋与新的值和意义
			if(root.left == null) {
				root.left = preNode;
				root.leftType = 1;
			}
			
			// 因为设置 right ，需要拿到下一个结点，我们就留到下一次循环再来处理
			if(preNode != null && preNode.right == null) {
				preNode.right = root;
				preNode.rightType = 1;
			}
			
			preNode = root;
			
			// 像以前一样正常递归
			if(root.leftType == 0 && root.left != null) {
				preOrderThreading(root.left);
			}
			
			if(root.rightType == 0 && root.right != null) {
				preOrderThreading(root.right);
			}
		}
	}
	
	/**
	 * 	按中序遍历的顺序进行线索化
	 *  线索化的过程，我们使用非递归的方式来实现
	 */
	private void inOrderThreading() {
		if(rootNode != null) {
			LinkedListStack<Node<T>> nodeStack = new LinkedListStack<>();
			// 注意，我们这里维护了一个 preNode ，因为我们需要能够拿到上一个遍历的结点
			Node<T> preNode = null;
			Node<T> currNode = rootNode;
			nodeStack.push(currNode);
			while(!nodeStack.isEmpty()) {
				// 跟中序遍历一样，我们先不断地压栈 并 移动指针，一直到找到最左侧的那个结点
				while(currNode != null && currNode.left != null) {
					nodeStack.push(currNode.left);
					// 这里我们只是移动指针，并压栈，并不是真正遍历，所以 preNode 不需要保存currNode
					// 从栈中弹出的元素才是我们真正遍历的元素，那个 currNode 在修改的时候一定要先保存到 preNode
					currNode = currNode.left;
				}
				
				// 从栈中弹出一个新的 currNode 
				currNode = nodeStack.pop();
				
				// 以前我们是直接打印currNode，然后再看 currNode 右结点是否为空，现在需要多几个步骤
				// 现在我们要看 currNode 的 left 属性是否已经有数据了
				// 如果没有，那么我们就把 left 指向 preNode ，然后 leftType 改成 1
				// 按理说，我们还需要维护 currNode 的 right 属性，但是现在还拿不到下一个遍历的结点，所以我们留下到一次遍历时处理
				if(currNode.left == null) {
					currNode.left = preNode;
					currNode.leftType = 1;
				}
				
				// 同时，我们还要再看看 preNode 的 right 属性是否已经有数据了
				// 如果没有，那么我们就把 preNode.right 指向 currNode ，然后rightType 改成 1
				// 前面我们没有维护 currNode ，因为我们拿不到下一个遍历的元素结点。现在currNode 其实就是下一个遍历的元素
				// 我们留到这里再来维护 上一个节点的 right 属性
				if(preNode != null && preNode.right == null) {
					preNode.right = currNode;
					preNode.rightType = 1;
				}
				
				// 这个是正常的中序遍历
				if(currNode.right != null) {
					// 当我们需要修改 currNode 指向前，我们需要先把 currNode 保存到 preNode 
					preNode = currNode;
					currNode = currNode.right;
					nodeStack.push(currNode);
				}else {
					// 当我们需要修改 currNode 指向前，我们需要先把 currNode 保存到 preNode 
					preNode = currNode;
					currNode = null;
				}
			}
		}
	}
	
	/**
	 * 	使用递归的方式来线索化二叉树，以中序遍历的顺序
	 * 	需要在成员变量里面维护一个 preNode 变量，让全部的递归方法共享此变量。
	 * @param root
	 */
	private void inOrderThreading(Node<T> root) {
		if(root != null) {
			// 如果左节点不是 null ，那么我们就正常递归遍历左边的元素
			if(root.left != null) {
				inOrderThreading(root.left);
			}
			
			// 正常情况下，我们是在这里递归遍历当前结点的数据
			// System.out.println(root.data);
			// 如果当前遍历的结点的左结点是 null ，那么我们需要指定当前结点的左结点为 preNode ， leftType = 1
			if(root.left == null) {
				root.left = preNode;
				root.leftType = 1;
			}
			
			// 维护右节点需要下一个遍历结点对象，我们拿不到
			// 所以我们就都放到下一次循环，在本次循环处理preNode 的 right 属性
			if( preNode != null && preNode.right == null ) {
				preNode.right = root;
				preNode.rightType = 1;
			}
			
			// 处理完以后，我们推进 preNode 的指针
			// root 指针我们不需要维护，由递归去自动维护
			preNode = root;
			
			if(root.right != null) {
				inOrderThreading(root.right);
			}
		}
	}

	/**
	 * 	中序遍历： 因为我们是遍历线索化后的二叉树，所以我们遍历的逻辑，也要发生变化
	 * 	【注意】线索化的二叉树遍历不再需要栈，也不需要递归。因我们在线索化的时候，已经把遍历的路线都串起来了。
	 * 			虽然没有链表那么简单，但是如果这个二叉树是一个完全二叉树，那么遍历起来，其实跟链表已经差不多了。
	 */
	public void inOrderTraversal() {
		Node<T> currNode = rootNode;
		// 遍历到最后一个元素的时候， currNode.right 其实是一个 null 值
		// 这个时候，我们就退出循环
		while(currNode != null) {
			// 首先，直接找到第一个 currNode.leftType == 1 的元素， 并打印该元素
			// 其实就是往左下方向一直走
			while(currNode.leftType == 0) {
				currNode = currNode.left;
			}
			
			System.out.println(currNode.data);
			// 然后如果这个元素的  currNode.rightType == 1 ，那么我们直接移动指针，并输出该结点
			// 一般来说，线索化以后，这个 currNode.right 指向的其实是 根结点，这个根结点可能就是本结点的父结点，也可能是祖先结点
			// 以前我们一般需要通过栈结构，先把根结点压栈，然后再依次弹出。 现在我们直接就可以通过 currNode.right 直接访问
			// 所以现在的中序遍历，我们不需要什么栈，当然也不需要递归
			
			// 就完全二叉树来说，这个 currNode.right 其实是一个根结点，而这个根结点一般来说都是有右子结点的
			// 所以一般来说，这个循环并不会执行很多次。最一般的情况，都是一次。
			// 当然，也有特殊的情况，比如说， currNode 是父结点的左结点，然后currNode.right 指向父结点
			// 而父结点只有左子结点，所以父结点的右结点又是指向另一个父结点，这种情况比较少
			while(currNode.rightType == 1) {
				currNode = currNode.right;
				System.out.println(currNode.data);
			}
			
			// 只要退出循环，currNode.rightType 的值就是 0
			// 这种情况，我们同样是移动指针，但是并不马上处理
			// 这个时候，我们交给下一个循环去处理，往左下方向一直走，走到底。
			currNode = currNode.right;
		}
	}
	
	/**
	 * 	这个方法是直接从 BasicBinaryTree 那里复制的，就是按从上到下，从左到右的规则添加元素
	 * 	我们会稀有化此方法，仅在构造方法中调用
	 * @param data
	 * @return
	 */
	private boolean add(T data) {
		// 首先，看一下 data 是不是 null,如果是 null ，直接抛异常，我们不存 null 值
		if(data == null) {
			throw new NullPointerException("保存的元素不能为null");
		}
		
		// 最基本的二叉树对大小顺序没有要求，对是否重复也没有要求
		// 保存数据的时候，我们按从上到下，从左到右的规则去保存
		// 我们没有任何要求，最后肯定是可以保存成功的，所以直接创建结点对象
		Node<T> newNode = new Node<T>();
		newNode.data = data;
		
		LinkedListQueue<Node<T>> nodeQueue = null;
		Node<T> currNode = null;
		if(rootNode != null) {
			nodeQueue = new LinkedListQueue<>();
			nodeQueue.addItem(rootNode);
			while(!nodeQueue.isEmpty()) {
				// 从队列中删除的元素其实就是我们当前遍历的元素
				currNode = nodeQueue.removeItem();
				
				// 先看看当前遍历的结点左结点是否是 null ，
				//   如果不是，就正常层序遍历
				//   如果是，那么当前结点就是我们要找的结点，我们直接把新添加的结点放到 currNode.left
				if(currNode.left != null) {
					nodeQueue.addItem(currNode.left);
				}else {
					currNode.left = newNode;
					size ++;
					return true;
				}
				
				// 先看看当前遍历的结点右结点是否是 null ，
				//   如果不是，就正常层序遍历
				//   如果是，那么当前结点就是我们要找的结点，我们直接把新添加的结点放到 currNode.right
				if(currNode.right != null) {
					nodeQueue.addItem(currNode.right);
				}else {
					currNode.right = newNode;
					size ++;
					return true;
				}
			}
		}else {
			// 如果根结点是空的，那么我们就直接
			rootNode = newNode;
			size ++;
			return true;
		}
		// 其实这里是走不到的
		return false;
	}
	
	// 结点类
	@SuppressWarnings("hiding")
	private class Node<T>{
		private T data;
		private Node<T> left;
		private Node<T> right;
		private int leftType;
		private int rightType;
		@Override
		public String toString() {
			return "Node [data=" + data + "]";
		}
	}
}
