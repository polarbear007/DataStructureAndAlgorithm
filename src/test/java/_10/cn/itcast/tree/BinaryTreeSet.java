package _10.cn.itcast.tree;

import java.util.Stack;

import _03.cn.itcast.queue.LinkedListQueue;
import _05.cn.itcast.stack.LinkedListStack;

/**
 * 	使用二叉树实现一个 set 集合，要求这个集合具有去重、排序的功能。
 * 	添加的元素必须实现 comparable 接口（给集合添加比较器对象的方法暂时不要求）
 * @author Administrator
 *
 * @param <T>
 */
public class BinaryTreeSet<T> {
	// 一根树肯定要有一个根结点
	private Node<T> rootNode;
	// 维护一个 size 变量，用来表示二叉树里面保存的节点个数
	private int size;

	/**
	 * 	添加元素，如果该元素不存在，添加成功，返回true;如果该元素存在，添加失败，返回false;
	 * 
	 * @param data
	 * @return
	 */
	public boolean add(T data) {
		Node<T> newNode = null;

		// 首先，看一下 data 是不是 null,如果是 null ，直接抛异常，我们不存 null 值
		if (data == null) {
			throw new NullPointerException("保存的元素不能为null");
		}
		// 然后，把 data 转成 Comparable 接口类型，如果T 没有实现 Comparable 接口，会报转换异常
		Comparable<T> cData = (Comparable<T>) data;

		// 再然后看一下根结点是不是null ，如果是 null，直接添加根结点，然后返回 true
		if (rootNode == null) {
			newNode = new Node<T>();
			newNode.data = data;
			rootNode = newNode;
			return true;
		}

		// 如果根结点不为空，那么我们就需要先遍历二叉树
		Node<T> currNode = rootNode;
		int result = 0;
		while (true) {
			result = cData.compareTo(currNode.data);
			// result 等于 0 ，表示新添加的元素跟当前结点的元素相同，二叉树不允许重复元素，直接返回 false
			if (result == 0) {
				return false;
			} else if (result > 0) {
				// 如果result 大于 0 ，说明添加的元素大于当前结点的元素，我们应该放在右边
				// 放右边之前，我们应该再看一下，右边是否为 null
				// 如果是，我们直接添加；
				// 如果不是，我们移动指针再次判断
				if (currNode.right == null) {
					newNode = new Node<T>();
					newNode.data = data;
					currNode.right = newNode;
					return true;
				} else {
					currNode = currNode.right;
				}
			} else {
				// 如果result 小于 0，说明添加的元素小于当前结点的元素，我们应该放在左边
				if (currNode.left == null) {
					newNode = new Node<T>();
					newNode.data = data;
					currNode.left = newNode;
					return true;
				} else {
					currNode = currNode.left;
				}
			}
		}
	}

	/**
	 * 	判断集合中是否包含指定的元素
	 * 	因为我们的集合是带有顺序的，所我们可以使用二叉查找方法
	 * @param data
	 * @return
	 */
	public boolean contains(T data) {
		// 因为我们的添加方法是不允许添加 null 值的，所以如果你的 data 是 null ，那么肯定是 false
		if(data == null) {
			return false;
		}
		
		if(rootNode == null) {
			return false;
		}
		
		// 这里我们就不检查 data 是否实现了 Comparable 接口，直接类型转换
		Comparable<T> cData = (Comparable<T>) data;
		Node<T> currNode = rootNode;
		while(true) {
			if(cData.compareTo(currNode.data) == 0) {
				return true;
			}else if(cData.compareTo(currNode.data) > 0) {
				// 传入的数据比当前结点的数据大，那么我们指针往右边移
				if(currNode.right == null) {
					return false;
				}else {
					currNode = currNode.right;
				}
			}else {
				// 传入的数据比当前结点的数据小，那么我们指针往左边移
				if(currNode.left == null) {
					return false;
				}else {
					currNode = currNode.left;
				}
			}
		}
	}
	
	public void preorderTraversal() {
		preorderTraversal(rootNode);
	}

	public void inorderTraversal() {
		inorderTraversal(rootNode);
	}

	public void postorderTraversal() {
		postorderTraversal(rootNode);
	}

	// 递归实现前序遍历： 先遍历根结点，再遍历左右
	private void preorderTraversal(Node<T> root) {
		if (root != null) {
			System.out.println(root.data);
			if (root.left != null) {
				preorderTraversal(root.left);
			}
			if (root.right != null) {
				preorderTraversal(root.right);
			}
		}
	}

	// 非递归实现前序遍历：我们需要借助于栈结构来实现
	public void preorderTraversal2() {
		if (rootNode != null) {
			LinkedListStack<Node<T>> nodeStack = new LinkedListStack<>();
			Node<T> currNode = null;
			nodeStack.push(rootNode);
			while (!nodeStack.isEmpty()) {
				// 从栈中弹出一个元素
				currNode = nodeStack.pop();
				// 先打印弹出元素结点的数据
				System.out.println(currNode.data);
				// 再看一下 右结点 是不是 null ，因为先入后出，所以我们先看右边
				if (currNode.right != null) {
					nodeStack.push(currNode.right);
				}
				// 再看一下 左结点 是不是 null
				if (currNode.left != null) {
					nodeStack.push(currNode.left);
				}
			}
		}
	}

	// 递归中序遍历： 先遍历左结点，再遍历根结点，最后再遍历右结点
	private void inorderTraversal(Node<T> root) {
		if (root != null) {
			if (root.left != null) {
				inorderTraversal(root.left);
			}

			System.out.println(root.data);

			if (root.right != null) {
				inorderTraversal(root.right);
			}
		}
	}

	// 非递归中序遍历
	public void inorderTraversal2() {
		if (rootNode != null) {
			LinkedListStack<Node<T>> nodeStack = new LinkedListStack<>();
			Node<T> currNode = rootNode;
			nodeStack.push(currNode);
			while (!nodeStack.isEmpty()) {
				// 如果当前元素的左子结点不是 null ，那么我们就一直压栈
				// 如果 currNode 是 null , 那么肯定是已经遍历完右边元素，需要再弹出一个元素，不进入循环
				while (currNode != null && currNode.left != null) {
					nodeStack.push(currNode.left);
					currNode = currNode.left;
				}

				// 现在我们可以保证当前元素的左子结点是null ，所以我们可以
				// 直接弹出栈顶元素结点，并输出对应数据
				currNode = nodeStack.pop();
				System.out.println(currNode.data);

				// 输出了当前元素结点以后，我们还需要看一下其右结点是不是空
				// 如果不是空，那么我们就把 currNode 指向右结点，同时把这个右结点压入栈中，再继续上面的保存流程
				// 如果是空，那么其实我们应该直接再弹出栈顶元素，但是怕进入第一个循环，我们就把 currNode 赋值为null
				if (currNode.right != null) {
					currNode = currNode.right;
					nodeStack.push(currNode);
				} else {
					currNode = null;
				}
			}
		}
	}

	// 递归后序遍历： 先遍历左结点，再遍历右结点，最后再遍历中结点
	private void postorderTraversal(Node<T> root) {
		if (root != null) {
			if (root.left != null) {
				postorderTraversal(root.left);
			}

			if (root.right != null) {
				postorderTraversal(root.right);
			}

			System.out.println(root.data);
		}
	}

	// 非递归后序遍历
	public void postorderTraversal2() {
		if (rootNode != null) {
			Stack<Node<T>> nodeStack = new Stack<>();
			Node<T> currNode = rootNode;
			nodeStack.push(currNode);
			while (!nodeStack.isEmpty()) {
				while (currNode != null && currNode.left != null) {
					nodeStack.push(currNode.left);
					currNode = currNode.left;
				}
				// 我们先不直接弹出栈顶元素，直接先看看栈顶元素是啥就好
				currNode = nodeStack.peek();
				// 如果栈顶的元素是 null ，那么说明栈顶的第二个元素已经是第二次访问了，可以直接弹出并输出
				// 不需要再去判断有没有右结点, 如果再次判断就会出现死循环
				if (currNode == null) {
					// 弹出 null 元素
					nodeStack.pop();
					// 保存 null 后面的那个元素
					currNode = nodeStack.pop();
					// 输出这个元素携带的数据
					System.out.println(currNode.data);
					// 把 currNode 设置成 null ，防止第一个循环再次往左边保存
					currNode = null;
					// 跳过下面的判断，否则可能会一直死循环
					continue;
				}

				// 如果栈顶的元素不是 null ，那么我们再看看栈顶的元素右边是不是 null
				// 如果右边不是null ，那么我们需要把右边结点压入栈中，并试图保存其左边一串元素
				// 如果右边是null ，那么我们直接输出即可
				if (currNode.right != null) {
					nodeStack.push(null);
					nodeStack.push(currNode.right);
					currNode = currNode.right;
				} else {
					// 如果右边没有结点，那么我们就直接弹出
					System.out.println(currNode.data);
					nodeStack.pop();
					currNode = null;
				}
			}
		}
	}

	// 层序遍历： 从上到下，从左到右，一层一层地遍历
	public void levelOrderTraversal() {
		LinkedListQueue<Node<T>> nodeQueue = null;
		Node<T> currNode = null;
		if (rootNode != null) {
			nodeQueue = new LinkedListQueue<>();
			nodeQueue.addItem(rootNode);
			while (!nodeQueue.isEmpty()) {
				currNode = nodeQueue.removeItem();
				System.out.println(currNode.data);
				// 如果队列删除的那个元素结点有左子结点，那么我们就把这个左结点保存到队列中
				if (currNode.left != null) {
					nodeQueue.addItem(currNode.left);
				}
				// 如果队列删除的那个元素结点有右子结点，那么我们就把这个右结点保存到队列中
				if (currNode.right != null) {
					nodeQueue.addItem(currNode.right);
				}
			}
		}
	}

	// 结点类
	private class Node<T> {
		private T data;
		private Node<T> left;
		private Node<T> right;
	}
}
