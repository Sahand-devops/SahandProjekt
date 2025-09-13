package com.friends;

import java.util.Stack;

/**
 * Provides methods for evaluating arithmetic expressions.
 * Supports operations including addition, subtraction, multiplication,
 * division, modulo, exponentiation, square root, and factorial.
 */
public class Calculations {

    /**
     * Determines the precedence of an operator.
     *
     * @param op the operator character.
     * @return the precedence level of the operator.
     */
    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/' || op == '%') return 2;
        if (op == '^') return 3;
        return 0;
    }

    /**
     * Applies a binary or unary operation based on the operator provided.
     *
     * @param a  the first operand.
     * @param b  the second operand.
     * @param op the operator character.
     * @return the result of the operation.
     * @throws InvalidExpressionException if the operator is invalid.
     */
    private static double applyOp(double a, double b, char op) throws InvalidExpressionException {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            case '%' -> (a / b) * 100;
            case '√' -> Math.sqrt(a);
            case '^' -> Math.pow(a, b);
            default -> throw new InvalidExpressionException("Invalid operator: " + op);
        };
    }

    /**
     * Computes the factorial of a non-negative integer.
     *
     * @param n the number for which to compute the factorial.
     * @return the factorial of n.
     * @throws InvalidExpressionException if n is negative or not an integer.
     */
    private static double factorial(double n) throws InvalidExpressionException {
        if (n < 0 || n != Math.floor(n)) {
            throw new InvalidExpressionException("Factorial is only defined for non-negative integers.");
        }
        double result = 1;
        for (int i = 1; i <= (int) n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Evaluates an arithmetic expression given as a string.
     * <p>
     * Supports numbers (including decimals), the constant π, square roots (using '√'),
     * factorials, exponentiation (using '^'), parentheses, and binary operators (+, -, *, /, %).
     * </p>
     *
     * @param expression the arithmetic expression to evaluate.
     * @return the result of the evaluation.
     * @throws InvalidExpressionException if the expression is invalid.
     */
    public static double evaluate(String expression) throws InvalidExpressionException {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == ' ') continue;
            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                values.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (c == 'π') {
                values.push(Math.PI);
            } else if (c == '√') {
                i++;
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                if (sb.length() == 0) {
                    throw new InvalidExpressionException("Square root operator must be followed by a number.");
                }
                double num = Double.parseDouble(sb.toString());
                values.push(Math.sqrt(num));
                i--;
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    double b = values.pop();
                    double a = values.pop();
                    char op = operators.pop();
                    values.push(applyOp(a, b, op));
                }
                if (!operators.isEmpty()) {
                    operators.pop();
                }
            } else if (c == '!') {
                if (values.isEmpty()) {
                    throw new InvalidExpressionException("Factorial operator '!' must follow a number or expression.");
                }
                double value = values.pop();
                values.push(factorial(value));
            } else if (c == '^') {
                while (!operators.empty() && precedence(operators.peek()) > precedence(c)) {
                    double b = values.pop();
                    double a = values.pop();
                    char op = operators.pop();
                    values.push(applyOp(a, b, op));
                }
                operators.push(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    double b = values.pop();
                    double a = values.pop();
                    char op = operators.pop();
                    values.push(applyOp(a, b, op));
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            double b = values.pop();
            double a = values.pop();
            char op = operators.pop();
            values.push(applyOp(a, b, op));
        }

        return values.pop();
    }
}
