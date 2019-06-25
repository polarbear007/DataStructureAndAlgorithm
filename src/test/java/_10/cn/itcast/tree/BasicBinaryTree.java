package _10.cn.itcast.tree;

import java.util.Stack;

import _03.cn.itcast.queue.LinkedListQueue;
import _05.cn.itcast.stack.LinkedListStack;

/**
 * 这是一个最简单的二叉树，结点里面保存的值可以重复，左和右也没有大小区别。
 * 只提供最简单的增加、遍历（四种遍历）、查询是包含某个元素等方法
 * 	删除方法不太好处理，我们这里就先不写了。
 * 1、 add(T data)
 * 2、 contains(T data)
 * 3、 preorderTraversal()
 * 4、 inorderTraversal()
 * 5、 postorderTraversal()
 * 6、 
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class BasicBinaryTree<T> {
	// 一根树肯定要有一个根结点
	private Node<T> rootNode;
	// 维护一个 size 变量，用来表示二叉树里面保存的节点个数
	private int size;
	
	/**
	 * 	这是最基本的二叉树，左右结点没有大小区别，元素也可以重复
	 * 	我们的添加规则是： 从上到下，从左到右依次添加。
	 * 
	 * 	【思路】 每次添加元素的时候，都先层序遍历一次。 我们只需要找到第一个左结点为 null 或者 右结点为 null 的结点即可。
	 * @param data
	 * @return
	 */
	public boolean add(T data) {
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
	
	/**
	 * 	查看二叉树中是否包含指定的元素
	 * 	【说明】因为我们的二叉树没有排序功能，没有去重功能。就是一个最基本的二叉树。
	 * 		  所以我们想要查看是否包含指定的元素，只能使用暴力查找。就是随便用上下面的任意遍历方法，
	 * 			拿到每个元素，一个一个地去比对
	 * @param data
	 * @return
	 */
	public boolean contains(T data) {
		if(rootNode != null) {
			LinkedListStack<Node<T>> nodeStack = new LinkedListStack<>();
			Node<T> currNode = null;
			nodeStack.push(rootNode);
			while(!nodeStack.isEmpty()) {
				// 从栈中弹出一个元素
				currNode = nodeStack.pop();
				// 然后跟目标数据进行比较，如果相等，直接返回 true
				if(currNode.data.equals(data)) {
					return true;
				}
				
				// 再看一下 右结点 是不是 null ，因为先入后出，所以我们先看右边
				if(currNode.right != null) {
					nodeStack.push(currNode.right);
				}
				// 再看一下 左结点 是不是 null
				if(currNode.left != null) {
					nodeStack.push(currNode.left);
				}
			}
		}
		
		// 如果上面的遍历没有找到，那么直接返回 false
		return false;
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
		if(root != null) {
			System.out.println(root.data);
			if(root.left != null) {
				preorderTraversal(root.left);
			}
			if(root.right != null) {
				preorderTraversal(root.right);
			}
		}
	}
	
	// 非递归实现前序遍历：我们需要借助于栈结构来实现
	public void preorderTraversal2() {
		if(rootNode != null) {
			LinkedListStack<Node<T>> nodeStack = new LinkedListStack<>();
			Node<T> currNode = null;
			nodeStack.push(rootNode);
			while(!nodeStack.isEmpty()) {
				// 从栈中弹出一个元素
				currNode = nodeStack.pop();
				// 先打印弹出元素结点的数据
				System.out.println(currNode.data);
				// 再看一下 右结点 是不是 null ，因为先入后出，所以我们先看右边
				if(currNode.right != null) {
					nodeStack.push(currNode.right);
				}
				// 再看一下 左结点 是不是 null
				if(currNode.left != null) {
					nodeStack.push(currNode.left);
				}
			}
		}
	}
	
	// 递归中序遍历： 先遍历左结点，再遍历根结点，最后再遍历右结点
	private void inorderTraversal(Node<T> root) {
		if(root != null) {
			if(root.left != null) {
				inorderTraversal(root.left);
			}
			
			System.out.println(root.data);
			
			if(root.right != null) {
				inorderTraversal(root.right);
			}
		}
	}
	
	// 非递归中序遍历
	public void inorderTraversal2() {
		if(rootNode != null) {
			LinkedListStack<Node<T>> nodeStack = new LinkedListStack<>();
			Node<T> currNode = rootNode;
			nodeStack.push(currNode);
			while(!nodeStack.isEmpty()) {
				// 如果当前元素的左子结点不是 null ，那么我们就一直压栈
				// 如果 currNode 是 null , 那么肯定是已经遍历完右边元素，需要再弹出一个元素，不进入循环
				while(currNode != null && currNode.left != null) {
					nodeStack.push(currNode.left);
					currNode = currNode.left;
				}
				
				// 现在我们可以保证当前元素的左子结点是null ，所以我们可以
				// 直接弹出栈顶元素结点，并输出对应数据
				currNode = nodeStack.pop();
				System.out.println(currNode.data);
				
				// 输出了当前元素结点以后，我们还需要看一下其右结点是不是空
				//   如果不是空，那么我们就把 currNode 指向右结点，同时把这个右结点压入栈中，再继续上面的保存流程
				//   如果是空，那么其实我们应该直接再弹出栈顶元素，但是怕进入第一个循环，我们就把 currNode 赋值为null
				if(currNode.right != null) {
					currNode = currNode.right;
					nodeStack.push(currNode);
				}else {
					currNode = null;
				}
			}
		}
	}
	
	// 递归后序遍历： 先遍历左结点，再遍历右结点，最后再遍历中结点
	private void postorderTraversal(Node<T> root) {
		if(root != null) {
			if(root.left != null) {
				postorderTraversal(root.left);
			}
			
			if(root.right != null) {
				postorderTraversal(root.right);
			}
			
			System.out.println(root.data);
		}
	}
	
	// 非递归后序遍历
	public void postorderTraversal2() {
		if(rootNode != null) {
			Stack<Node<T>> nodeStack = new Stack<>();
			Node<T> currNode = rootNode;
			nodeStack.push(currNode);
			while(!nodeStack.isEmpty()) {
				while(currNode != null && currNode.left != null) {
					nodeStack.push(currNode.left);
					currNode = currNode.left;
				}
				// 我们先不直接弹出栈顶元素，直接先看看栈顶元素是啥就好
				currNode = nodeStack.peek();
				// 如果栈顶的元素是 null ，那么说明栈顶的第二个元素已经是第二次访问了，可以直接弹出并输出
				// 不需要再去判断有没有右结点, 如果再次判断就会出现死循环
				if(currNode == null) {
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
				//  如果右边不是null ，那么我们需要把右边结点压入栈中，并试图保存其左边一串元素
				//   如果右边是null ，那么我们直接输出即可
				if(currNode.right != null) {
					nodeStack.push(null);
					nodeStack.push(currNode.right);
					currNode = currNode.right;
				}else{
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
		if(rootNode != null) {
			nodeQueue = new LinkedListQueue<>();
			nodeQueue.addItem(rootNode);
			while(!nodeQueue.isEmpty()) {
				currNode = nodeQueue.removeItem();
				System.out.println(currNode.data);
				// 如果队列删除的那个元素结点有左子结点，那么我们就把这个左结点保存到队列中
				if(currNode.left != null) {
					nodeQueue.addItem(currNode.left);
				}
				// 如果队列删除的那个元素结点有右子结点，那么我们就把这个右结点保存到队列中
				if(currNode.right != null) {
					nodeQueue.addItem(currNode.right);
				}
			}
		}
	}

	// 结点类
	@SuppressWarnings("hiding")
	private class Node<T>{
		private T data;
		private Node<T> left;
		private Node<T> right;
	}
}
