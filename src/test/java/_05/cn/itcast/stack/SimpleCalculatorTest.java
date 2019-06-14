package _05.cn.itcast.stack;

import org.junit.Test;

public class SimpleCalculatorTest {
	@Test
	public void test1() {
		SimpleCalculator calculator = new SimpleCalculator();
		Double result = calculator.parseAndCalculate("10*2+7-5/2");
		System.out.println(result);
	}
	
	@Test
	public void test2() {
		SimpleCalculator calculator = new SimpleCalculator();
		Double result = calculator.parseAndCalculate("10.6*22.123+7.3-52.6/");
		System.out.println(result);
	}
}
