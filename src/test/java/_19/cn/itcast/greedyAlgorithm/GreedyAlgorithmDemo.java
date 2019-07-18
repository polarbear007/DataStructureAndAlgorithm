package _19.cn.itcast.greedyAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

/**
 * 	演示一下贪心算法的基本思想
 * 	需求： 
 * 		现在有五个电台，K1 K2 K3 K4 K5，不同的电台有不同的覆盖范围。
 * 		K1: {"北京", "上海", "天津"},
 * 		K2: {"广州", "北京", "深圳"},
 * 		K3: {"成都", "上海", "杭州"},
 * 		K4: {"上海", "天津"},
 * 		K5: {"杭州", "大连"}
 *
 * 	要求： 我们希望在在尽量少的电台上投入广告，又同时可以覆盖上面出现的城市。
 * 	
 * 	【暴力匹配算法分析】
 * 		暴力匹配其实就是把 5 个电台进行各种组合，穷尽各种可能性。 这里有5个电台，那么组合的可能性就应该有 2^5 - 1
 * 		如果电台的数量再增加，那么组合的可能性就会呈指数增长。
 * 		穷尽可能性以后，我们再一个组合一个组合地去分析，这种组合是否能满足覆盖全部城市的需求？如果可以，就保存起来。
 * 		最后，我们再统计一下，选出使用电台最少的组合。
 * 
 * 	【贪心算法思路分析】
 * 		贪心算法的基本思想就是每一次选择，都选择最优的。
 * 		在这里，我们可以把选择电台的步骤分解，每次选择电台的时候，都选择覆盖地区最广的。
 * 		如果我们想要选取尽量少的电台，其实就是尽量减少电台间覆盖相同的城市。所以当我们先选出一个电台以后，
 * 		我们应该屏蔽已经覆盖的城市，统计并选出覆盖剩下的城市最多的电台。
 * 
 * 		==> 通过上面的描述，我们应该大概能明白以下几点：
 * 			1、 不管怎么样，我们首先需要遍历全部的电台，把全部电台覆盖的所有城市都拿到，保存到一个未覆盖城市的集合中 。
 * 			2、 然后，我们还需要再遍历全部电台，统计这个电台覆盖了 城市集合中的多少个城市。
 * 			3、 在这些电台中，我们选择一个覆盖 未选择城市 最多的电台。
 * 			4、 然后，我们删除 集合中， 这个选中电台所覆盖的城市。 如果方便，把这个已经选中的电台也删除掉。
 * 			5、 再然后，我们需要重新统计未选中电台 各自覆盖了多少城市。	再从中选出 覆盖 未选择城市 最多的电台。
 * 			....
 * 			一直重复统计，一直到未选择城市集合为空，这个时候说明我们所选择的电台已经覆盖全部的城市了。
 * @author Administrator
 *
 */
public class GreedyAlgorithmDemo {
	
	@Test
	public void testGreedy() {
		// 保存广播 及其覆盖城市信息
		HashMap<String, HashSet<String>> broadcasts = new HashMap<>();
		
		HashSet<String> c1 = new HashSet<>();
		c1.add("北京");
		c1.add("上海");
		c1.add("天津");
		broadcasts.put("K1", c1);
		
		HashSet<String> c2 = new HashSet<>();
		c2.add("广州");
		c2.add("北京");
		c2.add("深圳");
		broadcasts.put("K2", c2);
		
		HashSet<String> c3 = new HashSet<>();
		c3.add("成都");
		c3.add("上海");
		c3.add("杭州");
		broadcasts.put("K3", c3);
		
		HashSet<String> c4 = new HashSet<>();
		c4.add("上海");
		c4.add("天津");
		broadcasts.put("K4", c4);
		
		HashSet<String> c5 = new HashSet<>();
		c5.add("杭州");
		c5.add("大连");
		broadcasts.put("K5", c5);
		
		// 再然后，我们需要维护一个未覆盖的城市集合，其实就是全部电台对应的城市的并集
		HashSet<String> cities = new HashSet<>();
		for (HashSet<String> city : broadcasts.values()) {
			cities.addAll(city);
		}
		
		// 再然后，我们就可以遍历 broadcasts , 求每个电台覆盖的城市数量（跟 cities 集合交集）
		String maxKey = null;
		HashSet<String> tempSet = new HashSet<>();
		int maxSize = 0;
		HashSet<String> selects = new HashSet<>();
		
		while(true) {
			// 遍历 broadcasts ，拿到每个key
			for (String key : broadcasts.keySet()) {
				// 先把本广播覆盖的城市保存到 tempSet 里面
				tempSet.addAll(broadcasts.get(key));
				// 然后用这个去跟 未覆盖的 cities 集合求交集，看看 tempSet 集合里面会剩下多少个元素
				tempSet.retainAll(cities);
				if(tempSet.size() > maxSize) {
					maxKey = key;
					maxSize = tempSet.size();
				}
				// 不管怎么，tempSet 一定要清空，不然 tempSet 里面的数据会越积越大
				tempSet.clear();
			}
			// 当退出循环以后， maxKey 就拿到了覆盖最多城市的电台对应的 key 了
			// 我们应该把这个 key 保存到 selects 集合里面去
			selects.add(maxKey);
			// 现在我们就从 citis 删除 maxKey 对应的广播台覆盖的城市
			cities.removeAll(broadcasts.get(maxKey));
			// 删除完了 cities 集合里面的数据以后，我们需要再清空一下 maxKey、maxsize ，准备进入下一轮循环了
			maxKey = null;
			maxSize = 0;
			
			// 循环的退出条件，就是 cities 集合为空
			if(cities.isEmpty()) {
				break;
			}
		}
		
		System.out.println(selects);
	}
	
	@Test
	public void testRetainAll() {
		ArrayList<Integer> arr1 = new ArrayList<>();
		arr1.add(1);
		arr1.add(2);
		arr1.add(3);
		
		ArrayList<Integer> arr2 = new ArrayList<>();
		arr2.add(3);
		arr2.add(4);
		arr2.add(5);
		arr2.add(6);
		
//		arr1.retainAll(arr2);   // 看一下 retainAll() 方法的效果
		arr1.removeAll(arr2);   // 看一下 removeAll() 方法的效果
		System.out.println(arr1);
		System.out.println(arr2);
	}
}
