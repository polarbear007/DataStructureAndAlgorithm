package _06.cn.itcast.recursion;

import java.util.Random;

import org.junit.Test;

/**
 * 演示一下走迷宫的思路：
 * 	 1、 我们使用一个二维数组来模拟迷宫地图
 * 	 2、 其中二维数组的元素值为1 时，表示一堵墙； 为0时，表示这条路还没有走过；  为2时表示走过； 为3时表示走过，但是此路不通。
 * 		为 5 时，表示迷宫起点；   为 6 时，表示迷宫的终点。
 *   3、 一般来说，我们需要指定一个统一的移动策略，比如说在一个地方，先向右走，不行再向下走，不行再向左走，不行再向上走。
 *      如果上下左右都走不通的话，那么这个点就被认为是一个死路，标记为3；
 *   4、 如果发现一条路是死路，那么我们先把这个点标记成3， 然后就会回退，回退到什么地方呢？       
 *   	回退到上一步。回退到上一步以后，再重新执行预定的策略，右下左上。
 *      
 *      回退前跟回退后的情况有什么不一样？     
 *        回退以后，周围就多了一个3，相当于多了一个限定条件，于是在相同的策略下，回退以后的选择就会跟上一次不一样。  
 * @author Administrator
 *
 */
public class RecursionTest05 {
	@Test
	public void test() {
		// 设置迷宫为 10 * 10 
		// 障碍个数为 20
		Labyrinth labyrinth = new Labyrinth(10, 10, 20);
		labyrinth.printMap();
		labyrinth.start();
		labyrinth.printMap();
	}
}

class Labyrinth{
	private Integer cols;
	private Integer rows;
	// 障碍个数
	private Integer blocks;
	private Integer startRowIndex = null;
	private Integer startColIndex = null;
	private Integer endRowIndex = null;
	private Integer endColIndex = null;
	
	private int[][] map;

	public Labyrinth(Integer cols, Integer rows, Integer blocks) {
		super();
		this.cols = cols;
		this.rows = rows;
		this.blocks = blocks;
		init();
	}
	
	public void start() {
		System.out.println("起始坐标为： (" + startRowIndex + ", " + startColIndex + ")");
		System.out.println("终点坐标为： (" + endRowIndex + ", " + endColIndex + ")");
		System.out.println(moveTo(startRowIndex, startColIndex));
	}
	
	// 移动到指定位置
	public boolean moveTo(int rowIndex, int colIndex) {
		// 如果移动的坐标就是目的地坐标，那么返回 true
		if(rowIndex == endRowIndex && colIndex == endColIndex) {
			return true;
			// 如果移动的目标是墙，那么肯定也是返回false
			// 或者移动的目标是2，表示我们刚才已经走过这条路了，不要重复再走了
			// 或者移动的目标是 3，也就是死路，那么也是返回 false
		}else if(map[rowIndex][colIndex] == 1 || map[rowIndex][colIndex] == 2 || map[rowIndex][colIndex] == 3) {
			return false;
			// 如果移动的坐标不是目的地坐标，也不是墙
			// 那么我们应该继续走，根据下一步移动返回值来判断是 true 还是 false
			// 如果下一步返回的是 true ，那么我们就同样返回 true ； 如果下一步返回 false ，那么我们就返回 false;
			// 当然，下一步可能还是没有确认是 true 还是false ，所以下一步还得再调用一次 moveTo() 方法
		}else {
			// 对于当前的点，只要不是墙，也没有被标记成 3 死路，那么我们就可以先把这个点标记成 2，接着再走下一步
			map[rowIndex][colIndex] = 2;
			
			// 先向右移动。 如果右边就是终点，或者往右走，最终可以到达终点，那么会返回true ，其他的可能性就不走了。
			if(moveTo(rowIndex, colIndex + 1)) {
				return true;
				// 向下
			}else if(moveTo(rowIndex + 1, colIndex)) {
				return true;
				// 向左
			}else if(moveTo(rowIndex, colIndex - 1)) {
				return true;
				// 向上
			}else if(moveTo(rowIndex - 1, colIndex)) {
				return true;
			}else {
				// 如果上下左右都走不通，那么说明这条路是死路，我们标记成3，然后返回
				map[rowIndex][colIndex] = 3;
				return false;
			}
		}
	}
	
	private void init() {
		if(cols < 5) {
			throw new RuntimeException("列数不能低于5");
		}
		if(rows < 5) {
			throw new RuntimeException("行数不能低于5");
		}
		// 如果障碍数量超过空白位置的 一半，我们就认为障碍太多了
		if(blocks >= (cols - 2) * (rows - 2) / 2 ) {
			throw new RuntimeException("障碍数量太多了！");
		}
		
		map = new int[rows][cols];
		
		//然后，先把地图的四周都设置成墙
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if(i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
					map[i][j] = 1;
				}
			}
		}
		
		// 再然后，我们就来随机生成障碍
		int count = 0;
		Random random = new Random();
		while(count != blocks) {
			// 生成一个随机的坐标
			int randomRow = random.nextInt(rows - 2) + 1;
			int randomCol = random.nextInt(cols - 2) + 1;
			
			// 如果这个随机坐标值是 0 ，也就是说不是墙的话，我们就设置成墙，然后 count ++
			if(map[randomRow][randomCol] == 0) {
				map[randomRow][randomCol] = 1;
				count++;
			}
		}
		
		// 随机生成起始坐标 和 终点坐标 
		int startCount = 0;
		int endCount = 0;
		while(startCount == 0 || endCount == 0) {
			// 生成一个随机的坐标
			int randomRow = random.nextInt(rows - 2) + 1;
			int randomCol = random.nextInt(cols - 2) + 1;
			
			// 如果生成的坐标对应的元素值是 0， 那么我们才考虑设置成起点坐标或者终点坐标
			if(map[randomRow][randomCol] == 0) {
				if(startCount == 0) {
					map[randomRow][randomCol] = 5;
					startRowIndex = randomRow;
					startColIndex = randomCol;
					startCount = randomCol;
					startCount++;
				}else if(endCount == 0) {
					map[randomRow][randomCol] = 6;
					endRowIndex = randomRow;
					endColIndex = randomCol;
					endCount = randomCol;
					endCount++;
				}
			}
			
		}
	}
	
	public void printMap() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
	}
}
