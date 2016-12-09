package Dialogs;

import java.util.Stack;

public class Trial {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Stack<String> stack = new Stack<String>();
		stack.push("Nitisha");
		stack.push("Vikas");
		stack.push("Pandharpurkar");
		stack.push(stack.pop().concat("r"));
		System.out.println(stack.pop());

	}

}
