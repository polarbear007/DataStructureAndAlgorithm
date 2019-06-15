package _05.cn.itcast.stack;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluate {
	public Double evaluate(String expression) {
		Stack<String> ops = new Stack<String>();
		Stack<Double> vals = new Stack<Double>();
		String reg = "(\\d+\\.?\\d*)|([\\+\\-\\*/])|([\\(\\)])|(sqrt)";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(expression);

		while (m.find()) {
			String s = m.group();
			//System.out.println(s);
			// 如果是左括号，我们什么都不干
			if (s.equals("("))
				;
			else if (s.equals("+"))
				ops.push(s);
			else if (s.equals("-"))
				ops.push(s);
			else if (s.equals("*"))
				ops.push(s);
			else if (s.equals("/"))
				ops.push(s);
			else if (s.equals("sqrt"))
				ops.push(s);
			else if (s.equals(")")) {
				String op = ops.pop();
				double v = vals.pop();
				if (op.equals("+"))
					v = vals.pop() + v;
				else if (op.equals("-"))
					v = vals.pop() - v;
				else if (op.equals("*"))
					v = vals.pop() * v;
				else if (op.equals("/"))
					v = vals.pop() / v;
				else if (op.equals("sqrt"))
					v = Math.sqrt(v);
				vals.push(v);
			} else
				vals.push(Double.parseDouble(s));
		}
		return vals.pop();
	}
}
