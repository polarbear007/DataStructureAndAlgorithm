package _15.cn.itcast.prefixTree;

import java.util.ArrayList;
import java.util.List;

public class MyTrie<V> {
	// 根结点
	private TrieNode<V> rootNode = new TrieNode<V>();
	// 记录单词查找树中保存了多少个key
	private int keysCount;
	
	/**
	 * 	根据 key 删除对应的多个结点
	 * 	【说明】 删除的逻辑跟查找的逻辑很像，同样是遍历 key ，然后从根结点开始查找对应的结点
	 * 			因为我们没有维护 parent 属性，所以我们需要使用一个变量保存父结点
	 * 	【注意】 因为多个key 可能有相同的前缀，所以我们在删除的时候，需要特别注意如果某个结点有多个 key 使用
	 * 			那么我们一定不要删除这个结点，只删除这个 key 特有的结点就可以了。
	 * 			具体操作最好是使用递归，先确定这个 key 存不存在，如果存在，我们就返回被删除结点的 Value 值， 
	 * 			并考虑是否要删除当前的结点
	 * @param key
	 * @return
	 */
	public V remove(String key) {
		V result = null;
		if(key == null || key.length() == 0 || keysCount == 0) {
			return result;
		}
		result = remove(rootNode, key, 0);
		if(result != null) {
			keysCount--;
		}
		return result;
	}
	
	/**
	 * 	让外层的方法去检查 key 是否为 null 或者 空字符串，执行此方法，肯定是合法的 key
	 * @param key
	 * @param index
	 * @return
	 */
	private V remove(TrieNode<V> parent, String key, int index) {
		V result = null;
		TrieNode<V> targetNode = parent.children[key.charAt(index)];
		// 如果 targetNode 不为null ，那么我们才有继续遍历下去的可能
		if(targetNode != null) {
			// targetNode 不为null ，我们还需要再看一下 index 是不是 key 的最后一位索引
			// 如果是，我们也退出递归， 返回当前结点的 value
			if(index == key.length() - 1) {
				//   不管删除不删除这个结点，我们都需要保存一下 result
				result = targetNode.value;
				
				//	因为已经遍历到这个 key 的最后一个字符，所以我们需要看一下当前的结点是否可以删除
				//  判断的标准是看   targetNode.count 是否大于 0
				//     如果 targetNode.count 大于 0 ，那么说明这个结点还有其他的key 使用，我们只需要把 value 删掉就好了
				//     如果 targetNode.count 等于 0（其实不可能小于 0），那么说明这个结点后面已经没有其他结点了，也就是说没有其他key 使用此结点
				//           那么我们就通过  parent 删除掉本结点（记得维护 parent.count）
				if(targetNode.count > 0) {
					targetNode.value = null;
				}else {
					parent.children[key.charAt(index)] = null;
					parent.count--;
				}
				// 最后再返回 result
				return result;
			}else {
				// 如果不是的话，我们需要继续向后递归
				result = remove(targetNode, key, ++index);
				// 这里我们根据 递归后返回的 result 是不是 null 来确认 key 是否存在
				// 如果 result 不为 null ，我们就认为 key 是存在的，那么我们就可以考虑删除当前结点了
				if(result != null) {
					// 是否需要删除结点的依据还是 当前结点的 count 值是否大于 0 
					//   如果大于0 ，那么我们什么都不需要处理，因为这不是最后结点，有保存 value 值
					//   所以我们只需要考虑 count 等于 0 时，就通过 parent 删除目标结点（记得维护 parent.count）
					if(targetNode.count == 0) {
						parent.children[key.charAt(index)] = null;
						parent.count--;
					}
				}
				// 最后返回result 即可
				return result;
			}
		}else {
			// 如果 targetNode 已经是 null 了，那么我们直接返回 null 就可以了，递归结束
			return null;
		}
	}
	
	/**
	 * 	返回全部的 key
	 * @return
	 */
	public List<String> keys(){
		ArrayList<String> keyList = new ArrayList<>();
		// 如果 keysCount 大于 0，我们才查找，否则直接返回一个空的 ArrayList
		if(keysCount > 0) {
			collect(rootNode, keyList);
		}
		return keyList;
	}
	
	/**
	 * 	返回以 指定前缀开头的 key 集合
	 * @param prefix
	 * @return
	 */
	public List<String> keysWithPrefix(String prefix){
		ArrayList<String> keyList = new ArrayList<>();
		TrieNode<V> targetNode = getNodeByPrefix(prefix);
		if(targetNode != null) {
			collect(targetNode, keyList);
		}
		return keyList;
	}
	
	/**
	 * 	从某个结点开始递归收集下面全部的 key， 如果起始结点是根结点，则是收集全部的 key
	 * 		
	 * @param startNode
	 * @param keyList
	 */
	private void collect(TrieNode<V> startNode, List<String> keyList) {
		
		// 先看看起始结点有没有对应的 value ，如果有的话，直接放进 keyList
		if(startNode.value != null) {
			keyList.add(startNode.prefix);
		}
		// 再然后，我们看一下 startNode.count 是否大于零，如果大于零说明 children 数组中有有效的子结点
		if(startNode.count > 0) {
			int getCount = 0;
			// 遍历 children 数组
			for (int i = 0; i < startNode.children.length; i++) {
				if(startNode.children[i] != null) {
					collect(startNode.children[i], keyList);
					// 如果 已经找到全部的子结点了，我们就不要再继续往下遍历了
					if(++getCount == startNode.count) {
						break;
					}
				}
			}
		}
		
	}
	
	/**
	 * 	判断是否存在某个 key , 其实跟 get 方法完全一样
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		TrieNode<V> targetNode = getNodeByPrefix(key);
		if(targetNode != null && targetNode.value != null) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 	返回树中有多少个 key
	 * @return
	 */
	public int getKeysCount() {
		return keysCount;
	}

	
	
	/**
	 * 	根据 key 查找对应的单词信息
	 * @param key
	 * @return
	 */
	public V get(String key) {
		TrieNode<V> targetNode = getNodeByPrefix(key);
		if(targetNode != null && targetNode.value != null) {
			return targetNode.value;
		}else {
			return null;
		}
	}
	
	/**
	 * 	根据前缀获取对应的结点
	 * 	如果前缀不存在，则直接返回 null
	 * @param prefix
	 * @return
	 */
	public TrieNode<V> getNodeByPrefix(String prefix){
		// 因为我们是不允许键值为 null 的，也不允许key 为空字符串，所以key 如果为 null 或者 空字符串，直接返回  null
		if(prefix == null || prefix.length() == 0) {
			return null;
		}
		if(keysCount == 0) {
			return null;
		}else {
			TrieNode<V> currNode = rootNode;
			for (int i = 0; i < prefix.length(); i++) {
				if(currNode.count > 0 && currNode.children[prefix.charAt(i)] != null) {
					currNode = currNode.children[prefix.charAt(i)];
				}else {
					return null;
				}
			}
			return currNode;
		}
	}
	
	/**
	 * 	插入单词
	 * @param key
	 */
	public V put(String key, V value) {
		// 如果 key 是null 或者 空字符串，我们直接返回
		if(key == null || key.length() == 0) {
			throw new RuntimeException("key 不能为null 或者 空字符串");
		}
		V result = null;
		// 首先，指定当前结点为 根结点
		TrieNode<V> currNode = rootNode;
		int index = 0;
		boolean isNew = false;
		for (int i = 0; i < key.length(); i++) {
			index = key.charAt(i);
			// 如果这个结点不存在，我们就
			if(currNode.children[index] == null) {
				currNode.children[index] = new TrieNode<V>();
				// 设置前缀
				currNode.children[index].prefix = key.substring(0, i+1);
				currNode.count++;
				if(!isNew) {
					isNew = true;
				}
			}
			currNode = currNode.children[index];
		}
		// 先保存原来的value
		result = currNode.value;
		// 再使用新的 value 去覆盖
		currNode.value = value;
		// 如果是新单词，那么这里的 isNew 就会变成 true
		if(isNew) {
			keysCount++;
		}
		// 最后返回 result
		return result;
	}
	
	/**
	 * 	单词查找树的结点对象（每个结点对象）
	 * @author Administrator
	 *
	 */
	@SuppressWarnings("hiding")
	private class TrieNode<V>{
		@SuppressWarnings("unchecked")
		// 保存子结点的数组
		TrieNode<V>[] children = new TrieNode[256];
		// 保存当前结点对应的 前缀，省得查找的时候再去拼接字符串 
		String prefix = null;
		// 保存具体的数据（不一定所有的结点都有值，只有 key 的最后一个字符对应的结点才会有值）
		V value = null;
		// 为了防止无效的遍历，我们使用 count 记录一下 children 数组里面实际有效的子结点个数
		int count;
		
		@Override
		public String toString() {
			return "TrieNode [prefix=" + prefix + ", value=" + value + ", count=" + count + "]";
		}
	}
}
