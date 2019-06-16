package _06.cn.itcast.recursion;

import java.util.LinkedList;

import org.junit.Test;

public class RecursionTest06 {
	@Test
	public void test() {
		PutEightQueueTask task = new PutEightQueueTask();
		System.out.println(task.countAlternatives());
	}
}

// 之所以在成员位置设置 chessboard 和 count ，是为了让所有的递归方法共享这两个数据
class PutEightQueueTask{
	// 默认棋盘就是 8 * 8 大小 
	// 约定，放置皇后的位置 值为 1 ；  可以放置皇后，但是还没放的位置值为 0； 不可以放置皇后，也没有放置的位置的值为2；
	private int[][] chessboard;
	// 这个是用来保存
	private LinkedList<String> queueIndexList;
	
	// 统计成功次数
	private int count;
	
	public int countAlternatives() {
		// 首先，我们应该先初始化一下 count 值,防止多次执行此方法，会累加
		count = 0;
		// 再初始化一下棋盘
		chessboard = new int[8][8];
		// 初始化一下 queueIndexList
		queueIndexList = new LinkedList<>();
		// 我们从第 0 行开始放置皇后
		putQueueToOneRow(0);
		return count;
	}
	
	// 调用这个方法可以给指棋盘上指定行添加皇后
	private void putQueueToOneRow(int rowIndex) {
		// 如果是最后一行，那么我们直接看这一行的元素中有没有值为 0 的
		// 有的话，就算成功； 没有的话，就算失败
		if(rowIndex == 7) {
			if(checkRow(rowIndex)) {
				count++;
				// 如果成功，那么打印一下当前的棋盘
				printChessBoard();
			}
			return;
		}else {
			// 如果不是最后一行，那么我们需要先看一下这一行有没有元素值为 0 的
			//   如果有，那么我们先放置这一行的皇后，然后再去放置下一行的皇后
			//   如果没有，那么说明这条路径是错的，直接返回
			if(checkRow(rowIndex)) {
				for (int i = 0; i < 8; i++) {
					if(chessboard[rowIndex][i] == 0) {
						// 先放置这一行的皇后
						putQueueTo(rowIndex, i);
						// 放置好以后，再去放置下一行
						putQueueToOneRow(rowIndex + 1);
						// 放置好了下一行以后，为了不影响下一种可能性，我们需要清楚刚才放置的皇后，及其影响的棋盘元素值
						clearQueueFrom(rowIndex, i);
					}
				}
				
			}else {
				// 如果这一行都没得放了，那么我们直接返回即可
				return ;
			}
		}
	}
	
	// 放置皇后, 并更新棋盘
	private void putQueueTo(int rowIndex, int colIndex) {
		chessboard[rowIndex][colIndex] = 1;
		// 把这个皇后的索引保存到  queueIndexList 中
		queueIndexList.addLast(rowIndex + "," + colIndex);
		// 再维护一下棋盘
		updateChessBoardByQueueIndexList();
	}

	// 把放置皇后的位置的值再改回成 0, 并更新棋盘
	private void clearQueueFrom(int rowIndex, int colIndex) {
		// 我们先把与此皇后相关的全部位置置为 0，再用更新方法去维护
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if(i == rowIndex || j == colIndex || i - j == rowIndex - colIndex || j + i == rowIndex + colIndex) {
					chessboard[i][j] = 0;
				}
			}
		}
		// 删除的时候，我们可以直接删除最后一个元素即可
		queueIndexList.removeLast();
		updateChessBoardByQueueIndexList();
	}
	
	private void updateChessBoardByQueueIndexList() {
		if(queueIndexList != null) {
			for (String indexStr : queueIndexList) {
				String[] indexArr = indexStr.split(",");
				int rowIndex = Integer.parseInt(indexArr[0]);
				int colIndex = Integer.parseInt(indexArr[1]);
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if(i == rowIndex || j == colIndex || i - j == rowIndex - colIndex || j + i == rowIndex + colIndex) {
							chessboard[i][j] = 2;
						}
					}
				}
				chessboard[rowIndex][colIndex] = 1;
			}
		}
		//printChessBoard();
	}

	private void printChessBoard() {
		// 更新棋盘的时候，我们都打印一下，方便跟踪（如果不跟踪，可以取消）
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(chessboard[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("********************");
	}

	private boolean checkRow(int rowIndex) {
		for (int i = 0; i < 8; i++) {
			if(chessboard[rowIndex][i] == 0) {
				return true;
			}
		}
		return false;
	}
}
