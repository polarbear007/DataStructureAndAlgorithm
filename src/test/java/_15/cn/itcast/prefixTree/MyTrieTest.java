package _15.cn.itcast.prefixTree;

import org.junit.Test;

public class MyTrieTest {
	@Test
	public void testPut() {
		MyTrie<String> trie = new MyTrie<String>();
		trie.put("hello", "你好");
		trie.put("world", "世界");
		trie.put("java", "java是一种编程语言!");
		System.out.println("trie 树保存的 key 数量： " + trie.getKeysCount());
		
		System.out.println(trie.get("hello"));
		System.out.println(trie.get("world"));
		System.out.println(trie.get("java"));
		System.out.println(trie.get("jack"));
	}
	
	@Test
	public void testKeys() {
		MyTrie<String> trie = new MyTrie<String>();
		trie.put("hi", "打招呼，你好");
		trie.put("hello", "你好");
		trie.put("happy", "幸福");
		trie.put("world", "世界");
		trie.put("java", "java是一种编程语言!");
		trie.put("jack", "杰克，人名");
		
		System.out.println(trie.keys());
		System.out.println(trie.keysWithPrefix("h"));
	}
	
	@Test
	public void testRemove() {
		MyTrie<String> trie = new MyTrie<String>();
		trie.put("hi", "打招呼，你好");
		trie.put("hello", "你好");
		trie.put("happy", "幸福");
		trie.put("world", "世界");
		trie.put("java", "java是一种编程语言!");
		trie.put("jack", "杰克，人名");
		
		System.out.println(trie.keys());
		System.out.println(trie.remove("hi"));
		System.out.println(trie.remove("hill"));
		System.out.println(trie.keys());
	}
}
