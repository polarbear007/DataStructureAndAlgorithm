package _05.cn.itcast.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
	// 这个方法可以把普通的中缀表达式转成逆波兰表达式
	public List<String> transferToReversePolishNotation(String expression) {
		// 事先校验一下
		if(expression == null || expression.trim().equals("")) {
			throw new RuntimeException("算式不能为空");
		}
		
		// 把中缀表达式转成后缀表达式，或者说逆波兰表达式的方法只需要一个符号栈 ops
		// 另外的表达式，我们可以直接使用一个List 集合来保存（不需要使用栈） 
		LinkedListStack<String> ops = new LinkedListStack<>();
		// 保存逆波兰表达式的集合
		ArrayList<String> rpnList = new ArrayList<>();
		
		// 这一次匹配的符号包含括号
		String reg = "(\\d+\\.?\\d*)|([\\+\\-\\*/])|([\\(\\)])";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(expression);
		
		while(m.find()) {
			// 如果是数字，那么直接放进集合中
			if(m.group(1) != null) {
				rpnList.add(m.group(1));
			}else if(m.group(2) != null) {
				String currOperator = m.group(2);
				// 如果当前的符号的优先级小于或者等于栈顶符号，那么我们需要依次弹出栈顶符号到 rpnList 中
				// 如果当前的符号优先级大于栈顶的符号，那么我们就入栈（在循环外部处理）
				// 【注意】 我们在getOperatorPriority() 方法内部做了一些调整， null 和 ( 也可以拿到指定的优先级
				while(getOperatorPriority(currOperator) <= getOperatorPriority(ops.peek())) {
					// 弹出符号栈栈顶的元素，添加到 rpnList
					rpnList.add(ops.pop());
				}
				ops.push(currOperator);
			}else if(m.group(3) != null) {
				String currOperator = m.group(3);
				if(currOperator.equals("(")) {
					ops.push(currOperator);
				}else {
					String topOperator = ops.pop();
					// 只要栈顶元素不是 ( 或者 栈为空，那么就一直弹出符号栈内部的符号到 rpnList
					while(topOperator !=null && !topOperator.equals("(")) {
						rpnList.add(topOperator);
						topOperator = ops.pop();
					}
				}
			}
		}
		
		// 最后，如果符号栈中还有元素，那么我们依次弹出，并添加到 rpnList 中
		String topOperator = ops.pop();
		// 只要栈顶元素不是 ( 或者 栈为空，那么就一直弹出符号栈内部的符号到 rpnList
		while(topOperator !=null && !topOperator.equals("(")) {
			rpnList.add(topOperator);
			topOperator = ops.pop();
		}
		
		return rpnList;
	}
	
	private Integer getOperatorPriority(String operator) {
		// 符号栈如果是空的时候，获取栈顶元素会返回 null 
		// 虽然 null 根本不是运算符，但是为了统一处理，我们这里也给 null 设置个优先级为 0
		// 表示不管你拿到什么运算符，都直接入栈
		if(operator == null){
			return 0;
			// 虽然 ( 不算运算符号，但是我们弹出栈顶的符号可能有 ( ，所以我们也给指定个优先级，为0
			// 这里设置为 0 的目的是，为了让程序看到此符号时，直接入栈，并不是说 ( 的优先级最低
		}else if(operator.equals("(")){
			return 0;
			// 如果是 +-， 优先级设置成1
		}else if(operator.equals("+") || operator.equals("-")) {
			return 1;
			// 如果是 */， 优先级设置成2
		}else if(operator.equals("*") || operator.equals("/")) {
			return 2;
		}else{
			throw new RuntimeException("无法识别该运算符号！");
		}
		
	}
	
	// 这个方法可以根据逆波兰表达式，计算出算式的结果
	public Double calculateByReversePolishNotationList(List<String> rpnList) {
		// 根据逆波兰表达式计算，我们只需要一个栈
		LinkedListStack<Double> nums = new LinkedListStack<Double>();
		
		for (String item : rpnList) {
			// 如果是数字，那么我们直接入栈
			if(item.matches("\\d+\\.?\\d*")) {
				nums.push(Double.valueOf(item));
			}else {
				// 在 rpnList 中，不是数字就是运算符号，不会有括号或者其他字符，所以直接 else
				// 如果是符号，那么我们就从 nums 栈中弹出两个栈顶的数字来进行计算，并把计算结果再压入栈中
				Double num1 = nums.pop();
				Double num2 = nums.pop();
				
				switch (item) {
				case "+":
					nums.push(num2 + num1);
					break;
				case "-":
					nums.push(num2 - num1);
					break;
				case "*":
					nums.push(num2 * num1);
					break;
				case "/":
					if(num1 == 0.0) {
						throw new ArithmeticException("除数不能为0");
					}
					nums.push(num2 / num1);
					break;
				default:
					break;
				}
			}
		}
		// 最后弹出栈顶的元素即可
		// 弹出之前，我们最好再检查一下，栈中是否只有一个元素，如果不是，那么可能算式有问题
		if(nums.length() != 1) {
			throw new RuntimeException("算式解析错误");
		}
		return nums.pop();
	}
	
	public Double parseAndCalculate(String expression) {
		// 事先校验一下
		if(expression == null || expression.trim().equals("")) {
			throw new RuntimeException("算式不能为空");
		}
		// 解析转换成逆波兰表达式
		List<String> list = transferToReversePolishNotation(expression.trim());
		// 解析逆波兰表达式，并计算结果
		return calculateByReversePolishNotationList(list);
	}
}
