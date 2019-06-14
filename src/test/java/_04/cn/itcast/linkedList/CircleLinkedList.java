package _04.cn.itcast.linkedList;

/**
 * 单向循环链表
 * @author Administrator
 *
 */
public class CircleLinkedList{
	private Node headNode = new Node();
	private Node lastNode = headNode;
	
	public void printLinkedList() {
		Node currNode = headNode;
		while(true) {
			currNode = currNode.next;
			System.out.println(currNode.data);
			if(currNode.next == headNode.next) {
				break;
			}
		}
	}
	
	public void initLinkedList(int count) {
		// 先根据 count 值创建一个 count 个结点的单向环形链表
		if(count <= 0) {
			throw new RuntimeException("count值必须大于0");
		}
		// 创建链表
		for(int i = 1; i <= count; i++) {
			Node newNode = new Node();
			newNode.data = i;
			if(headNode.next == null) {
				headNode.next = newNode;
			}
			newNode.next = lastNode.next;
			lastNode.next = newNode;
			lastNode = newNode;
		}
	}
	
	/**
	 * 
	 * @param count     是指结点的个数
	 * @param offsets   是指开始的时候，指针需要从 headNode 开始移动 offsets次数（从编号第 offsets 开始）
	 * @param steps     是指每次指针移动 steps - 1 次以后，一个结点要出圈
	 * @return
	 */
	public Integer play(int count, int offsets, int steps) {
		// 校验一下基本的参数合不合法
		if(offsets < 0 || steps <= 0) {
			throw new RuntimeException("offsets 必须大于0； steps 必须大于1");
		}
		// 根据 count 值 初始化环形链表
		initLinkedList(count);
		
		// 先创建两个辅助的指针
		// 单向链表中，要想删除 currNode ，必须使用 preNode
		Node preNode = null;
		Node currNode = headNode;
		
		// 先根据 offsets 把指针移动到指定位置
		for (int i = 0; i < offsets; i++) {
			preNode = currNode;
			currNode = currNode.next;
		}
		
		// 再然后就是循环删除结点了
		while(true) {
			// 循环的退出条件是只剩下一个结点
			if(preNode == currNode) {
				break;
			}
			
			// 如果还有两个或者以上的结点的话，那么我们就继续移动 steps - 1 次以后，删除 currNode结点
			for (int i = 0; i < steps - 1; i++) {
				preNode = currNode;
				currNode = currNode.next;
			}
			System.out.println("编号为：" + currNode.data + " 的结点被删除了");
			// 如果删除的是 lastNode ，我们得维护一下
			if(currNode == lastNode) {
				lastNode = preNode;
			}
			currNode = currNode.next;
			preNode.next = currNode;
			
		}
		
		// 现在currNode 和 preNode 都指向当前唯一的有效节点，这个节点上面的数据就是我们想要的数据
		System.out.println("最后剩下的结点编号为：" + currNode.data);
		return currNode.data;
	}
	
	private class Node{
		private Integer data;
		private Node next;
	}
}
