package _13.cn.itcast.balanceTree;

/**
 * 	比较好的B树 结点、键值对象结构，以后有需要再去重写原来的方法吧
 * @author Administrator
 *
 */
public class Node {
	private final int ORDER;
	private Node parent;
	private Entry[] entries;
	
	public Node(int order){
		this.ORDER = order;
		entries = new Entry[this.ORDER];
	}
}

class Entry<Key extends Comparable<Key>, Value>{
	private Key key;
	private Value value;
	private Node left;
	private Node right;
}
