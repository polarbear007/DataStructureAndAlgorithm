package _07.cn.itcast.sort;

import java.util.Random;

public class ArrayUtils {
	public static int[] generateRandomIntArray(int length) {
		if(length <= 0) {
			throw new RuntimeException("数组长度不能为0");
		}else {
			int[] arr = new int[length];
			Random r = new Random();
			for (int i = 0; i < length; i++) {
				arr[i] = r.nextInt(length); 
			}
			return arr;
		}
	}
}
