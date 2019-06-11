package _02.cn.itcast.sparseArray;

import org.junit.Test;

public class SparseArrayTest {
	@Test
	public void testTransferArrayToSparseArray() {
		// 创建一个11行11列的二维数组
		int[][] arr = new int[11][11];
		// 我们随便在这个二维数组里面保存一些数据吧
		arr[1][3] = 1;
		arr[2][6] = 2;
		arr[8][4] = 1;
		// 打印一下二维数组，看一下效果
		printIntArr(arr);
		
		System.out.println("*****************");
		
		// 把这个二维数组转成一个稀疏数组（其实还是一个二维数组）
		int[][] sparseArray = transferArrayToSparseArray(arr);
		printIntArr(sparseArray);
		
		System.out.println("*****************");
		
		// 再把稀疏数组转成普通的二维数组
		int[][] array = transferSparseArrToArray(sparseArray);
		printIntArr(array);
	}
	
	public int[][] transferSparseArrToArray(int[][] sparseArr){
		if(sparseArr == null) {
			return null;
		}
		if(sparseArr.length == 0) {
			throw new RuntimeException("无效稀疏数组！");
		}
		// 应该再校验一下第一行的三个数据有没有完整，但是太麻烦，这里就略过了
		int rowCount = sparseArr[0][0];
		int colCount = sparseArr[0][1];
		// 根据第一行的信息创建一个二维数组
		int[][] arr = new int[rowCount][colCount];
		
		// 要从第二行开始遍历，所以 i 需要从 1 开始 
		for (int i = 1; i < sparseArr.length; i++) {
			arr[sparseArr[i][0]][sparseArr[i][1]] = sparseArr[i][2];
		}
		
		return arr;
	}
	
	public int[][] transferArrayToSparseArray(int[][] arr) {
		if(arr == null) {
			return null;
		}
		int rowCount = arr.length;
		if(rowCount == 0) {
			throw new RuntimeException("二维数组行数不能为0");
		}
		int colCount = arr[0].length;
		
		int itemCount = 0;
		// 先遍历一遍原始二维数组，确认有多少个有效元素
		for (int[] row : arr) {
			for (int item : row) {
				if(item != 0) {
					itemCount++;
				}
			}
		}
		
		// 创建一个 itemCount + 1 行， 3列 的 二维数组
		int[][] sparseArr = new int[itemCount + 1][3];
		sparseArr[0][0] = rowCount;
		sparseArr[0][1] = colCount;
		sparseArr[0][2] = itemCount;
		
		// 从第二行开始的数据，我们就需要重新遍历一次原始二维数组
		// 为了记录稀疏数组，我们创建一个行数
		int rowIndex = 1;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				if(arr[i][j] != 0) {
					sparseArr[rowIndex][0] = i;
					sparseArr[rowIndex][1] = j;
					sparseArr[rowIndex][2] = arr[i][j];
					rowIndex++;
				}
			}
		}
		
		return sparseArr;
	}
	
	public void  printIntArr(int[][] arr) {
		for (int[] row : arr) {
			for (int item : row) {
				System.out.print(item + " ");
			}
			System.out.println();
		}
	}
}
