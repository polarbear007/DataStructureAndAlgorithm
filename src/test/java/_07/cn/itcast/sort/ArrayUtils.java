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
	
	/**
	 * 把原来的数组进行反转
	 * @param arr
	 */
	public static void reverse(int[] arr) {
		if(arr == null) {
			throw new RuntimeException("数组不能为空");
		}
		if(arr.length <= 1) {
			return;
		}
		int low = 0;
		int high = arr.length - 1;
		int temp = 0;
		while(low < high) {
			temp = arr[low];
			arr[low] = arr[high];
			arr[high] = temp;
			low ++;
			high --;
		}
	}
}
