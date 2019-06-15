package _05.cn.itcast.stack;

import java.util.List;

import org.junit.Test;

public class CalculatorTest {
	@Test
	public void testTransferToReversePolishNotation() {
		Calculator c = new Calculator();
		List<String> list = c.transferToReversePolishNotation("1 + 3 * ((2+3 - 4) * 7 + 4) + 5");
		System.out.println(list); // 1 3 2 3 + 4 - 7 * 4 + * + 5 +
		System.out.println(c.calculateByReversePolishNotationList(list));
	}
	
	@Test
	public void test2() {
		Calculator c = new Calculator();
		Double result1 = c.parseAndCalculate("2.37+(5.42 -7.83*0.45 + 7.15)*6.35 - 7.04");
		System.out.println(result1);
		Double result2 = c.parseAndCalculate("2.37+(5.42 -(7.83*0.45 + 7.15))*(6.35 - 7.04)");
		System.out.println(result2);
	}
}
