package _09.cn.itcast.hashTable;

import java.util.Hashtable;
import java.util.Set;

import org.junit.Test;

import _09.cn.itcast.hashTable.SimpleHashTable.Entry;

public class SimpleHashTableTest {
	@Test
	public void testHashCode() {
		Hashtable<String, String> table = new Hashtable<String, String>();
		for (int i = 0; i < 100; i++) {
			table.put(String.valueOf(i), String.valueOf(i));
		}
		System.out.println();
	}
	
	@Test
	public void testPut() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>();
		table.put(1, "小明");
		table.put(2, "小白");
		table.put(3, "小黑");
		
		System.out.println(table);
	}
	
	@Test
	public void testRemove() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>();
		table.put(1, "小明");
		table.put(2, "小白");
		table.put(3, "小黑");
		System.out.println(table.remove(2));
		// 再删除一次，看看里面还有没有 小白， 如果没有返回 null
		System.out.println(table.remove(2));
	}
	
	@Test
	public void testGet() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>();
		table.put(1, "小明");
		table.put(2, "小白");
		table.put(3, "小黑");
		System.out.println(table.get(2));
		// 再一次获取
		System.out.println(table.get(2));
	}
	
	@Test
	public void testToString() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>();
		// 添加之前打印
		System.out.println(table);
		
		table.put(1, "小明");
		table.put(2, "小白");
		table.put(3, "小黑");
		
		// 添加以后打印
		System.out.println(table);
	}
	
	@Test
	public void testEntrySet() {
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>();
		table.put(1, "小明");
		table.put(2, "小白");
		table.put(3, "小黑");
		
		Set<Entry<Integer, String>> entrySet = table.entrySet();
		for (Entry<Integer, String> entry : entrySet) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
	}
	
	// 测试自动扩容，需要 debug 查看内部的 capacity 和 threshold, 还有 table
	@Test
	public void testReHash() {
		// 创建哈希表的时候，设置一个比较小的长度，比较容易看到效果
		SimpleHashTable<Integer, String> table = new SimpleHashTable<Integer, String>(3);
		table.put(1, "小明");
		table.put(2, "小白");
		table.put(3, "小黑");
		table.put(4, "小黄");
		table.put(5, "小红");
		table.put(6, "小蓝");
		table.put(7, "小紫");
		table.put(8, "小灰");
		table.put(9, "小花");
		table.put(10, "小东");
		Set<Entry<Integer, String>> entrySet = table.entrySet();
		for (Entry<Integer, String> entry : entrySet) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
	}
	
	@Test
	public void testReHashOfHashTable() {
		// 创建哈希表的时候，设置一个比较小的长度，比较容易看到效果
		Hashtable<Integer, String> table = new Hashtable<Integer, String>(3);
		table.put(1, "小明");
		table.put(2, "小白");
		table.put(3, "小黑");
		table.put(4, "小黄");
		table.put(5, "小红");
		table.put(6, "小蓝");
		table.put(7, "小紫");
		table.put(8, "小灰");
		table.put(9, "小花");
		table.put(10, "小东");
		Set<java.util.Map.Entry<Integer, String>> entrySet = table.entrySet();
		for (java.util.Map.Entry<Integer, String> entry : entrySet) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
	}
}
