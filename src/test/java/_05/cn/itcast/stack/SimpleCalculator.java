package _05.cn.itcast.stack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用双栈来解析 字符串的四则运算式， 没的括号
 * @author Administrator
 *
 */
public class SimpleCalculator {
	
	public Double parseAndCalculate(String statement) {
		LinkedListStack<Double> numStack = new LinkedListStack<>();
		LinkedListStack<String> operatorStack = new LinkedListStack<>();
		
		String reg = "(\\d+\\.?\\d*)|([\\+\\-\\*/])";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(statement);
		while(m.find()) {
			// 拿到一个数字
			if(m.group(1) != null) {
				numStack.push(Double.valueOf(m.group(1)));
			}else if(m.group(2) != null) {
				// 拿到一个运算符号,我们可以先保存一下当前拿到的符号 
				String currOperator = m.group(2);
				// 首先，我们应该看一下符号栈是不是为空
				if(operatorStack.isEmpty()) {
					// 符号 栈为空，那么我们直接push
					operatorStack.push(currOperator);
				}else {
					// 如果符号栈不为空
					// 我们就先看一下当前拿到的符号是 * 或者 /，那么需要看一下栈顶的符号 
					if(currOperator.equals("*") || currOperator.equals("/")) {
						// 先看一下 符号栈顶上的符号是什么
						String topOperator = operatorStack.peek();
						// 如果栈顶的符号是 +-，那么当前的符号优先级高于栈顶符号，我们只需要push 即可
						if(topOperator.equals("+") || topOperator.equals("-")) {
							operatorStack.push(currOperator);
						}else {
							// 如果栈顶的符号不是 +-，其实就是 */，那么我们需要先计算，再push
							Double result = calculate(numStack, operatorStack);
							numStack.push(result);
							operatorStack.push(currOperator);
						}
					}else if(currOperator.equals("+") || currOperator.equals("-")){
						// 如果拿到的符号是 +- 符号，因为 +-是优先级最低的，所以一定要弹出一个符号和两个数字,并计算结果
						Double result = calculate(numStack, operatorStack);
						// 计算好以后，再 push 到numStack
						numStack.push(result);
						// 最后，我们再把currOperator push 到符号栈中
						operatorStack.push(currOperator);
					}
				}
				
			}
		}
		
		// 等我们遍历完以后，其实双栈里面就算有保存符号，也都是平级的运算了，我们只需要多次调用 calculate() 方法
		// 直到符号栈为空，最后返回数字栈里面的栈顶元素即可
		Double result = null;
		while(!operatorStack.isEmpty()) {
			result = calculate(numStack, operatorStack);
			numStack.push(result);
		}
		
		return result;
	}
	
	private Double calculate(LinkedListStack<Double> numStack, LinkedListStack<String> operatorStack) {
		// 从数字栈里面弹出两个数字
		Double num1 = numStack.pop();
		Double num2 = numStack.pop();
		// 从符号栈里面弹出1个符号 
		String operator = operatorStack.pop();
		
		if(num1 == null || num2 == null || operator == null) {
			// 如果在从栈中取数据的过程中有任何问题，我们都认为是算式出错了
			throw new RuntimeException("算式有误！");
		}
		
		// 算式顺序应该是   num2  operator  num1
		switch (operator) {
		case "+":
			return num2 + num1;
		case "-":
			return num2 - num1;
		case "*":
			return num2 * num1;
		case "/":
			if(num1 == 0.0) {
				throw new ArithmeticException("除数不能为0");
			}
			return num2 / num1;
		default:
			throw new RuntimeException("未知符号！");
		}
	}
}
