package _09.cn.itcast.hashTable;

import java.util.HashSet;
import java.util.Set;

/**
 * 实现一个最简单的哈希表，底层结构使用数组 + 链表 最基本的功能：
 *  1、 put(K key, V value) 
 *  2、 remove(Key key)
 *  3、 get(Key key) 
 *  4、 size() 
 *  5、 toString() // 打印哈希表 
 *  6、 entrySet() 
 *  7、 rehash()   这个方法可以实现对哈希表进行扩容，防止链表的长度过长，影响查询的效率
 * 返回所有的键值对对象集合
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class SimpleHashTable<K, V> {
	// 默认的数组长度为 10，也就是说桶的数量为 10
	private int capacity;
	// 注意： 我们无法在 new 数组的时候指定泛型，但是可以在接收的时候指定泛型，如下：
	private Entry<K, V>[] table;
	// 维护 当前哈希表的元素个数
	private int size = 0;
	// 加载因子，当元素个数超过 arr.length * loadFactor 时，我们就对当前的哈希表进行扩容
	private float loadFactor;
	// 这个阈值是由  arr.length * loadFactor 算出来的，因为每次添加的时候都需要用到，所以我们直接设置一个变量
	// 省得每次都要计算，扩容的时候，我们再更新一下此变量
	private int threshold;
	
	public SimpleHashTable() {
		this(10, 0.75f);
	}
	
	public SimpleHashTable(int capacity) {
		this(capacity, 0.75f);
	}
	
	public SimpleHashTable(int capacity, float loadFactor) {
		this.capacity = capacity;
		this.loadFactor = loadFactor;
		this.threshold = Math.round(capacity * loadFactor - 0.5f);
		table = new Entry[capacity];
	}
	
	/**
	 * 调用此方法可以对原有的哈希表进行扩容
	 */
	private void rehash() {
		// 先拿到原来的 table 数组
		Entry<K, V>[] oldTable = table;
		// 然后计算扩容以后的数组长度
		// 有可能原来的数组长度 * 2 以后，超过了 int 的范围，所以我们需要特别处理一下
		// 如果超过 Int 范围，会变成负数，结果反而会变小，如果是这种情况，我们就不扩容了，直接return
		// 【说明】 扩容只是为了尽量减少每条链表的长度，但是如果数组已经非常长了，我们就不扩容了，链表长度长一些就长一些
		//        并不需要报错
		int newCapacity = this.capacity * 2;
		if(newCapacity < this.capacity) {
			return;
		}
		
		// 维护一下 capacity / threshold
		this.capacity = newCapacity;
		this.threshold = Math.round(capacity * this.loadFactor - 0.5f);
		
		// 如果是正常扩容，我们就创建一个新的数组
		table = new Entry[newCapacity];
		
		// 然后遍历原来的数组全部元素,重新读取索引值，然后添加到新的数组上面去
		Entry<K, V> currEntry = null;
		Entry<K, V> nextEntry = null;
		int newIndex = 0;
		for (int i = 0; i < oldTable.length; i++) {
			// 遍历链表
			if(oldTable[i] != null) {
				currEntry = oldTable[i];
				while(true) {
					nextEntry = currEntry.next;
					// 重新计算哈希值对应的索引， 哈希值不需要变化 
					newIndex = currEntry.hash % newCapacity;
					// 然后添加到当前的新数组上面去（不要使用addEntry, 因为我们不需要重新创建Entry 对象）
					if(table[newIndex] == null) {
						table[newIndex] = currEntry;
					}else {
						currEntry.next = table[newIndex];
						table[newIndex] = currEntry;
					}
					// 如果下一个结点是 null，说明已经到最后结点了
					if(nextEntry == null) {
						break;
					}else {
						currEntry = nextEntry;
					}
				}
			}
		}
	}
	
	/**
	 * 返回全部的 键值对 对象集合 通过遍历这个集合，相当于遍历 哈希表 的全部元素
	 * 我们这里直接使用现成的 HashSet 集合就好了，省得再自己去实现一遍遍历接口
	 * @return
	 */
	public Set<Entry<K, V>> entrySet() {
		HashSet<Entry<K, V>> entrySet = new HashSet<Entry<K, V>>();
		Entry<K, V> currEntry = null;
		if (size == 0) {
			return entrySet;
		} else {
			for (int i = 0; i < table.length; i++) {
				if (table[i] != null) {
					currEntry = table[i];
					while (true) {
						entrySet.add(currEntry);
						currEntry = currEntry.next;
						if (currEntry == null) {
							break;
						}
					}
				}
			}
		}
		return entrySet;
	}

	@Override
	public String toString() {
		String result = null;
		if (size == 0) {
			return "[]";
		} else {
			result = "[";
			for (int i = 0; i < table.length; i++) {
				if (table[i] != null) {
					Entry<K, V> currEntry = table[i];
					while (true) {
						result += (currEntry.value + ", ");
						currEntry = currEntry.next;
						if (currEntry == null) {
							break;
						}
					}
				}
			}
			// 处理一下结尾处的那个 ,
			result = result.replaceAll(", $", "");
			result += "]";
		}
		return result;
	}

	/**
	 * 返回哈希表的
	 * 
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * 根据key 去获取元素值，如果没有找到对应的元素，返回 null
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key) {
		// 根据 key 获取 hashCode 和 对应的索引值
		int hashCode = getHashCodeByKey(key);
		int index = hashCode % capacity;
		// 然后，我们搜索这个 key 是否存在
		if (table[index] != null) {
			// 获取元素值，只需要一个 currEntry 就可以了
			Entry<K, V> currEntry = table[index];
			while (true) {
				if (currEntry.hash == hashCode && currEntry.key.equals(key)) {
					return currEntry.value;
				}
				currEntry = currEntry.next;
				// 如果当前的 entry 已经是链表最后了，那么我们就退出遍历链表
				if (currEntry.next == null) {
					break;
				}
			}
		}
		return null;
	}

	/**
	 * 根据 key 去删除元素，如果元素存在，则删除元素，并返回被删除的元素值 如果元素不存在，则返回 null
	 * 
	 * @param key
	 * @return
	 */
	public V remove(K key) {
		// 根据 key 获取 hashCode 和 对应的索引值
		int hashCode = getHashCodeByKey(key);
		int index = hashCode % capacity;

		// 然后，我们搜索这个 key 是否存在
		if (table[index] != null) {
			// 因为我们是单向链表，要删除一个结点，需要先获取上一个节点
			Entry<K, V> preEntry = null;
			Entry<K, V> currEntry = table[index];
			while (true) {
				// 如果 currEntry 就是要删除的结点，那么我们要看一下 preEntry 是否为 null
				// 如果是，说明 currEntry 是第一个元素，所我们直接让 table[index] = currEntry.next
				if (currEntry.hash == hashCode && currEntry.key.equals(key)) {
					if (preEntry == null) {
						table[index] = currEntry.next;
					} else {
						preEntry.next = currEntry.next;
					}
					// 仅在成功删除时，维护 size
					size--;

					// 删除好结点以后，我们就返回 currEntry.value
					return currEntry.value;
				}

				preEntry = currEntry;
				currEntry = currEntry.next;
				// 如果当前的 entry 已经是链表最后了，那么我们就退出遍历链表
				if (currEntry.next == null) {
					break;
				}
			}
		}
		return null;
	}

	/**
	 * 添加一个元素
	 * 
	 * @param key   指定key
	 * @param value 指定元素值
	 * @return
	 */
	public V put(K key, V value) {
		// 添加元素之前，我们需要先根据这个 key 的哈希值看一下这个哈希值是否已经存在
		// 存在就返回旧元素的值，然后添加新元素的值
		int hashCode = getHashCodeByKey(key);
		int index = hashCode % capacity;
		// 如果数组上索引对应的元素不为 null ，那么说明可能会存在，我们需要搜索链表确认
		if (table[index] != null) {
			Entry<K, V> currEntry = table[index];
			while (true) {
				if (currEntry.hash == hashCode && currEntry.key.equals(key)) {
					// 我们先根据 hash 值为找，hash值一样，那么key就可能一样
					// 如果 hash 值不一样，那么key 肯定不一样。

					// 如果哈希值相同，元素值也相同 ，那么我们就替换值
					V oldVal = currEntry.value;
					currEntry.value = value;
					return oldVal;
				}

				currEntry = currEntry.next;
				// 如果当前的 entry 已经是链表最后了，那么我们就退出遍历链表
				if (currEntry.next == null) {
					break;
				}
			}
		}

		// 前面如果没有发生 key 相同，值替换的操作，那么说明这个 key 是新的，我们得自己创建 Entry 对象,并保存
		addEntry(hashCode, index, key, value);

		// 只要是我们创建 key 保存的，那么这里就是返回 null
		return null;
	}

	/**
	 * 创建并添加一个 Entry 对象 【注意】 我们这里暂时不考虑给数组扩容的问题，固定 10 个桶，数组长度固定为 10
	 * 
	 * @param hash
	 * @param index
	 * @param key
	 * @param value
	 */
	private void addEntry(int hash, int index, K key, V value) {
		// 直接创建 entry 对象
		Entry<K, V> entry = new Entry<>();
		entry.hash = hash;
		entry.key = key;
		entry.value = value;
		
		// 添加 entry 之前，我们应该先判断一下 size 有没有小于 threshold 
		// 如果小于，正常添加；  如果等于或者大于，那么我们就需要先对原来的哈希表进行扩容，调用 rehash 方法
		if(size >= threshold) {
			rehash();
			
			// 扩容以后， index 肯定要重新计算
			index = hash % capacity;
		}

		// 不管你扩不扩容，最后这里都会添加对应的元素
		if (table[index] == null) {
			table[index] = entry;
		} else {
			// 我们懒得再去遍历链表，然后添加在最后位置，所以直接添加在最前面
			entry.next = table[index];
			table[index] = entry;
		}

		// 仅在添加成功以后，维护 size
		size++;
	}

	// 根据 key 获取哈希值
	// 我们不打算产生负值，所以这里简单取个绝对值就好了
	private int getHashCodeByKey(K key) {
		return Math.abs(key.hashCode());
	}

	// 通过 entrySet 去遍历哈希表时，外部需要拿到这个 Entry 对象，所以我们这里必须 public 修饰
	// 另外，为了简洁，我们再添加一个静态修饰符，如果不添加的话，外部访问必须加前缀
	// SimpleHashTable<K, V>.Entry<K, V>
	/**
	 * @author Administrator
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Entry<K, V> {
		private int hash;
		private K key;
		private V value;
		private Entry<K, V> next;

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		// 因为 entrySet() 方法使用的是   HashSet 集合来保存 Entry 对象
		// HashSet 要求集合元素必须实现 hashCode 和  equals 方法
		// 这里我们想要区分  Entry 对象，其实只需要根据  hash 值和  key 就够了，甚至只需要key 就够了
		// 因为 哈希表里面的 key 肯定是唯一的；
		// 如果只使用 hash 值的话，那么可能会因为值相同，而被  HashSet 替换掉，只保留一个元素
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry<K, V> other = (Entry) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}
	}
}
