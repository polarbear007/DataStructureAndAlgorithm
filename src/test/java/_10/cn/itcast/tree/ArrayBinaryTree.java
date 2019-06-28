package _10.cn.itcast.tree;

import _03.cn.itcast.queue.LinkedListQueue;

/**
 * 	【说明】 这里我们只是为了引入一个二叉树的顺序存储，为后面的堆排序做一下铺垫
 * 		   就不搞什么泛型，直接在内部构建一个 int[] 数组演示一下就好了
 * 
 * 	这个二叉树内部没有什么结点对象，只有一个纯粹的 int[] 类型数组。
 * 	但是我们同样可以把这个数组看成是一个完全二叉 树，进行前序、中序、后序遍历，甚至层序遍历也是没有问题的
 * @author Administrator
 *
 */
public class ArrayBinaryTree {
	private int[] dataArray;

	public ArrayBinaryTree(int[] dataArray) {
		super();
		this.dataArray = dataArray;
	}
	
	/**
	 * 	使用递归来实现前序遍历
	 */
	public void preOrderTraversal(int rootIndex) {
		// 根据根结点的索引值，我们可以计算得出其对应的左子结点索引值为  rootIndex * 2 + 1
		// 右子结点的索引值为  rootIndex * 2 + 2
		
		// 如果 dataArray 本身就是 null 了，那么肯定没有遍历的 需要了
		// 如果根结点存在，先打印根结点，再考虑打印左子结点和右子结点
		// 如果根结点都不存在了，那么肯定没有什么左子结点和右子结点了
		if(dataArray != null && rootIndex <= dataArray.length - 1) {
			System.out.println(dataArray[rootIndex]);
			
			// 如果左子结点存在，则递归打印左子结点
			if(rootIndex * 2 + 1 <= dataArray.length - 1) {
				preOrderTraversal(rootIndex * 2 + 1);
			}
			
			if(rootIndex * 2 + 2 <= dataArray.length - 1) {
				preOrderTraversal(rootIndex * 2 + 2);
			}
		}
	}
	
	
	/**
	 * 	使用递归来实现中序遍历
	 */
	public void inOrderTraversal(int rootIndex) {
		// 根据根结点的索引值，我们可以计算得出其对应的左子结点索引值为  rootIndex * 2 + 1
		// 右子结点的索引值为  rootIndex * 2 + 2
		
		// 如果 dataArray 本身就是 null 了，那么肯定没有遍历的 需要了
		// 如果根结点存在，先打印根结点，再考虑打印左子结点和右子结点
		// 如果根结点都不存在了，那么肯定没有什么左子结点和右子结点了
		if(dataArray != null && rootIndex <= dataArray.length - 1) {
			// 如果左子结点存在，则递归打印左子结点
			if(rootIndex * 2 + 1 <= dataArray.length - 1) {
				inOrderTraversal(rootIndex * 2 + 1);
			}
			
			System.out.println(dataArray[rootIndex]);
			
			// 如果右子结点存在，则递归打印右子结点
			if(rootIndex * 2 + 2 <= dataArray.length - 1) {
				inOrderTraversal(rootIndex * 2 + 2);
			}
		}
	}
	
	/**
	 * 	使用递归来实现后序遍历
	 */
	public void postOrderTraversal(int rootIndex) {
		// 根据根结点的索引值，我们可以计算得出其对应的左子结点索引值为  rootIndex * 2 + 1
		// 右子结点的索引值为  rootIndex * 2 + 2
		
		// 如果 dataArray 本身就是 null 了，那么肯定没有遍历的 需要了
		// 如果根结点存在，先打印根结点，再考虑打印左子结点和右子结点
		// 如果根结点都不存在了，那么肯定没有什么左子结点和右子结点了
		if(dataArray != null && rootIndex <= dataArray.length - 1) {
			// 如果左子结点存在，则递归打印左子结点
			if(rootIndex * 2 + 1 <= dataArray.length - 1) {
				postOrderTraversal(rootIndex * 2 + 1);
			}
			
			// 如果右子结点存在，则递归打印右子结点
			if(rootIndex * 2 + 2 <= dataArray.length - 1) {
				postOrderTraversal(rootIndex * 2 + 2);
			}
			
			System.out.println(dataArray[rootIndex]);
		}
	}
	
	/**
	 * 	使用队列结构实现层序遍历
	 * 	【说明】 其实我们直接遍历数组就是层序遍历了，这里只是演示一下可以像二叉树的层序遍历那样去处理
	 */
	public void levelOrderTraversal() {
		if(dataArray != null && dataArray.length > 0) {
			LinkedListQueue<Integer> queue = new LinkedListQueue<Integer>();
			int currIndex = 0;
			queue.addItem(currIndex);
			while(!queue.isEmpty()) {
				// 从队列中取出一个元素
				currIndex = queue.removeItem();
				System.out.println(dataArray[currIndex]);
				// 如果弹出的索引存在左子索引，我们就把左子索引放进队列
				if(currIndex * 2 + 1 <= dataArray.length - 1) {
					queue.addItem(currIndex * 2 + 1);
				}
				// 如果弹出的索引存在右子索引，我们就把右子索引放进队列
				if(currIndex * 2 + 2 <= dataArray.length - 1) {
					queue.addItem(currIndex * 2 + 2);
				}
			}
		}
		
	}
}
