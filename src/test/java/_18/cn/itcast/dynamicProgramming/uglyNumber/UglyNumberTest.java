package _18.cn.itcast.dynamicProgramming.uglyNumber;

import org.junit.Test;

/**
 * 	所谓的 ugly number 是指： 一个正整数分解质因数以后，质因数只包含 2， 3 或者 5。
 * 	比如 10 = 2 * 5 ；   24 = 2 * 3 * 2 * 2 等  
 * 
 * 	【分析】
 * 		我们可以类似 生成斐波纳契数列 那样，生成一个 ugly number 数组，然后返回指定索引处的元素值。
 * 		因为 ugly number 的质因为只包含 2， 3， 5， 所以后面的 ugly number 全部是由前面的某个 ugly number * 2 或者 3 或者5 得到的。
 * 		但是具体每次乘的是哪一个，我们得进行比较才能够知道。
 * 
 * 		因此，我们首先要创建一个长度为 index 的数组，用来保存全部的 ugly number
 * 		另外，我们还需要维护一个长度为 6 的数组，只用其中的 arr[2] / arr[3] / arr[5] 保存 ugly number 数组的索引
 * 		当我们要给 ugly number 数组添加新元素的时候，我们肯定是 通过求 uglyNums[arr[2]] * 2  或者  uglyNums[arr[3]] * 3 或者 uglyNums[arr[5]] * 5
 * 		的最小值得到的。
 * @author Administrator
 *
 */
public class UglyNumberTest {
	@Test
	public void testGetUglyNumber() {
		long start = System.currentTimeMillis();
		System.out.println(getUglyNumber(0));
		System.out.println("耗时：" + (System.currentTimeMillis() - start)  + "毫秒");
	}
	
	/**
	 * 	【从下往上的思路】
	 * 	输入索引，返回指定索引位置的 ugly number
	 * @param index
	 * @return
	 */
	public int getUglyNumber(int index) {
		// 如果出现负索引，我们直接返回 -1
		if(index < 1) {
			return -1;
		}
		// 根据 index  创建一个长度为 index + 1 的数组
		int[] uglyNums = new int[index + 1];
		// 初始化第一个元素值为 1
		uglyNums[0] = 1;
		
		// 再创建一个长度为 6的 primeFactors 数组
		int[] primeFactors = new int[6];
		
		int uglyNum = 0;
		// 首先，计算 temp2/ temp3 / temp5 的值
		int temp2 = uglyNums[primeFactors[2]] * 2;
		int temp3 = uglyNums[primeFactors[3]] * 3;
		int temp5 = uglyNums[primeFactors[5]] * 5;
		
		// 我们遍历 uglyNums 数组，并进行初始化
		for (int i = 1; i < uglyNums.length; i++) {
			// 比较得到三个值的最小值， 并维护 primeFactors 数组 和 新的 temp 值
			//	只有确定使用了最小值，才会去更新对应的 temp 值，省得大量的重复计算
			if(temp2 <= temp3 && temp2 <= temp5) {
				uglyNum = temp2;
			}else if(temp3 <= temp2 && temp3 <= temp5) {
				uglyNum = temp3;
			}else {
				uglyNum = temp5;
			}
			
			// 确定了 uglyNum 以后，我们初始化 uglyNums[i] = uglyNum;
			 uglyNums[i] = uglyNum;
			 
			// 再然后，我们需要再检查一下 uglyNum 具体是等于  当前的 哪一个temp值
			// 有可能一次会有多个相等的 temp 值，所以我们需要分别判断
			if(uglyNum == temp2) {
				primeFactors[2]++;
				temp2 = uglyNums[primeFactors[2]] * 2;
			}
			if(uglyNum == temp3) {
				primeFactors[3]++;
				temp3 = uglyNums[primeFactors[3]] * 3;
			}
			if(uglyNum == temp5){
				primeFactors[5]++;
				temp5 = uglyNums[primeFactors[5]] * 5;
			}
		}
		return uglyNums[index];
	}
}
