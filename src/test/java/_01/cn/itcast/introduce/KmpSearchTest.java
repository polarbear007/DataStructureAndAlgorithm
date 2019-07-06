package _01.cn.itcast.introduce;

import org.junit.Test;

// 字符串搜索算法测试
public class KmpSearchTest {
	// 暴力搜索（不显式回溯的算法）
	public Integer violentlySearch1(String source, String target) {
		// 主串长度
		int M = source.length();
		// 子串长度
		int N = target.length();
		
		// 我们只需要遍历主串到  M-N 位置，如果还没找到，那么肯定找不到了
		for (int i = 0; i <= M - N; i++) {
			int j;
			// 每次 j 都是从 0 开始，也就是说主串每进一位，就跟目标串的第一位开始比较
			// 如果相同，子循环就继续下去，如果不相同，子循环就退出，主串继续进一位
			for(j = 0; j < N; j++) {
				if(source.charAt(i + j) != target.charAt(j)) {
					break;
				}
			}
			// 如果子循环退出后， j == N , 说明已经找到目标子串了, 这里返回 i 坐标
			if(j == N) {
				return i;
			}
		}
		// 如果都遍历完了，还没有 return ，那么这里最后 return -1
		return -1;
	}
	
	@Test
	public void testViolentlySearch1() {
		String source = "hello world java and hello world javascript and hello world springboot";
		String target = "hello world javascript";
		Integer index = violentlySearch1(source, target);
		System.out.println(index);
	}
	
	// 上面的暴力搜索是直接使用字符串去处理
	// 网络上一般的处理方案是先把字符串转成数组
	// 显式回溯的暴力查找算法
	public Integer violentlySearch2(String source, String target) {
		char[] sourceArr = source.toCharArray();
		char[] targetArr = target.toCharArray();
		
		// 主串的位置
		int i = 0;
		
		// 子串的位置
		int j = 0;
		
		while(i < sourceArr.length && j < targetArr.length) {
			// 如果这两个相等，我们就同时把 i 和 j 坐标推进
			if(sourceArr[i] == targetArr[j]) {
				i++;
				j++;
			}else {
				// 如果这两个坐标不相等，主串位置要回溯 j-1 个单位，而子串位置要归0
				i = i - (j - 1);
				j = 0;
			}
		}
		
		// 如果 j 等于子串长度，说明已经找到目标子串
		if(j == targetArr.length) {
			return i - j;
		}else {
			// 如果 j 不等于子串长度，说明遍历完主串，还没有找到对应的目标子串，返回 -1
			return -1;
		}
	}
	
	@Test
	public void testViolentlySearch2() {
		Integer index = violentlySearch2("hello world java and hello world javascript and hello world springboot",
										"hello world javascript");
		System.out.println(index);
	}
	
	
	
	// kmp 算法
	@Test
	public void testKmp() {
		String source = "hello world java and hello world javascript and hello world springboot";
		String pattern = "hello world javascript";
		Integer index = KMPSearch2(source, pattern);
		System.out.println(index);
	}
	
	public Integer KMPSearch2(String source, String pattern) {
		if(source == null) {
			throw new RuntimeException("文本串不能为 null");
		}
		int[] next = getNextArr2(pattern);
		if(next == null) {
			return 0;
		}
		
		char[] sourceArr = source.toCharArray();
		char[] patternArr = pattern.toCharArray();
		int sLen = sourceArr.length;
		int pLen = patternArr.length;
		
		int i = 0;
		int j = 0;
		
		while(i <= (sLen - pLen) && j < pLen) {
			if(j == -1 || sourceArr[i] == patternArr[j]) {
				i++;
				j++;
			}else {
					j = next[j];
			}
		}
		
		if(j == pLen) {
			return i - j;
		}
		
		return -1;
	}
	
	public int[] getNextArr2(String pattern) {
		if(pattern == null) {
			throw new RuntimeException("模式串不能为 null");
		}
		if(pattern.length() == 0) {
			return null;
		}
		int[] next = new int[pattern.length()];
		next[0] = -1;
		int i, j;
		for(i = 1, j = 0; i < pattern.length(); i++) {
			if(pattern.charAt(i) == pattern.charAt(j)) {
				j++;
				next[i] = j;
			}else {
				next[i] = 0;
				j = 0;
			}
		}
		return next;
	}
	
	
	public Integer KMPSearch(String source, String pattern) {
		// 如果文本串为 null ,直接报异常
		if(source == null) {
			throw new RuntimeException("文本串不能为 null");
		}
		// 首先，根据 pattern 拿到 next 数组
		int[] next = getNextArr(pattern);
		// 如果数组为 null ,说明 pattern 长度为0
		// 这种情况不需要搜索，直接返回 0
		if(next == null) {
			return 0;
		}
		
		// 接着，我们像以前那样把source 和 pattern 都转成字符数组
		char[] sourceArr = source.toCharArray();
		char[] patternArr = pattern.toCharArray();
		int sLen = sourceArr.length;
		int pLen = patternArr.length;
		
		int i = 0;
		int j = 0;
		
		// 代码结构跟暴力匹配很像
		while(i <= (sLen - pLen) && j < pLen) {
			// 如果相等，i 和 j 都推进一个单位
			if(sourceArr[i] == patternArr[j]) {
				i++;
				j++;
			}else {
				// 不相等的时候，我们应该判断一下 j 的值是否为 0
				// 如果是的话，那么意味着：文本串 的第 i 个字符刚跟 模式串的第一个字符比较就不匹配了
				// 我们应该让 i 向前推进一个单位
				if(j == 0) {
					i++;
				}else {
					// 如果不相等，而且 j 不等于0， 那么我们这次不退回 i ，只移动 j
					// 移动的长度为已经确认的子串长度 - 最大公共元素长度
					// 其实可以转化成  失配索引 - next 值
					// 而失配索引就是 j ，而 next 值就是  next[j], 所以向右位移的距离为  j-next[j]
					// 而向右位移，其实就是 当前坐标 - 位移距离 ===> j - (j - next[j]) = next[j]
					j = next[j];
				}
			}
		}
		
		// 跟以前一样，如果 j == pLen ，说明匹配成功
		if(j == pLen) {
			return i - j;
		}
		
		return -1;
	}
	
	// 输入一个模式串，返回一个 next 数组
	// 这个 next 数组保存着每个 失配的 索引对应的 next 值，其实就是保存着 最大公共元素长度值
	// 我们可以根据这个长度值 配合 失配的索引值 算出 失配时，模式串需要右移多少个单位
	public int[] getNextArr(String pattern) {
		if(pattern == null) {
			throw new RuntimeException("模式串不能为 null");
		}
		if(pattern.length() == 0) {
			return null;
		}
		int[] next = new int[pattern.length()];
		// 无论你的模式串长度是多少，next[0] 的值肯定是 0
		next[0] = 0;
		// 其中的i 表示模式串对应的子串长度， 其实也对应着模式串失配的索引值
		// 因为子串的长度取决于在什么位置失配
		// 而 j 的值，则是指当前要比较子串的哪一个位置上在字符。 （其实这个值也是 next 值）
		int i, j;
		
		// 前面已经说过了，每次只需要看最后一位是否等于子串指定位置的字符
		// 如果等于，那么 next 值在前面的基础上 加1， 子串上需要比较的字符位置也加1 （这里其实都是 j）
		// 如果不等于，那么 next 值归零, 子串上需要比较的字符位置也归零(这里其实都是 j)
		for(i = 1, j = 0; i < pattern.length(); i++) {
			if(pattern.charAt(i) == pattern.charAt(j)) {
				j++;
				next[i] = j;
			}else {
				next[i] = 0;
				j = 0;
			}
		}
		return next;
	}
}
