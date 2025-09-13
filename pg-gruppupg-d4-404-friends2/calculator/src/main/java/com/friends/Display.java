package com.friends;

/**
 * The Display class manages the text shown on the calculator display.
 * It holds the previous expression and the current input and provides
 * methods to update and format the display text.
 */
public class Display {

    /**
     * Holds the last evaluated expression (for the top row).
     */
    private static String previousExpression = "";

    /**
     * Holds the current input or answer (for the third row).
     */
    private static String currentInput = "";

    /**
     * Returns the display text.
     * <p>
     * If a previous expression exists, it appears on the first row, followed by two newline characters,
     * then the current input on the third row. If no previous expression exists, two newlines are prepended
     * so that the input always appears on the third row.
     * </p>
     *
     * @return the full display text.
     */
    public static String getDisplayText() {
        return (previousExpression.isEmpty() ? "\n\n" : previousExpression + "\n\n") + currentInput;
    }

    /**
     * Appends a number to the current input.
     *
     * @param numberInput the number to append.
     * @return the updated display text.
     */
    public static String put_display_number(String numberInput) {
        currentInput += numberInput;
        return getDisplayText();
    }

    /**
     * Appends an operator to the current input.
     * If an operator was the last character, it is replaced.
     *
     * @param operatorInput the operator to append.
     * @return the updated display text.
     */
    public static String put_display_operator(String operatorInput) {
        if (currentInput.isEmpty() && operatorInput.equals("√")) {
            currentInput = operatorInput;
        } else if (!currentInput.isEmpty()) {
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' || lastChar == '√') {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
            }
            currentInput += operatorInput;
        }
        return getDisplayText();
    }

    /**
     * Removes the last character from the current input.
     *
     * @return the updated display text.
     */
    public static String remove_display() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }
        return getDisplayText();
    }

    /**
     * Clears both the current input and the previous expression.
     *
     * @return the updated display text.
     */
    public static String clear_display() {
        currentInput = "";
        previousExpression = "";
        return getDisplayText();
    }

    /**
     * Evaluates the current input, updates the previous expression with the evaluated input,
     * and sets the current input to the result. If an error occurs during evaluation,
     * the current input is set to "Error".
     *
     * @return the updated display text.
     */
    public static String processEquals() {
        if (currentInput.isEmpty()) return getDisplayText();
        try {
            double result = Calculations.evaluate(currentInput);
            String resultStr = formatAsIntegerOrDouble(result);
            previousExpression = currentInput;
            currentInput = resultStr;
            DBConnector.insertHistory(previousExpression, currentInput);
            DBConnector.exportHistoryToJSON("history.json");
            DBConnector.exportHistoryToXML("history.xml");
        } catch (InvalidExpressionException e) {

            previousExpression = currentInput;
            currentInput = e.getMessage();
        } catch (Exception e) {
            previousExpression = currentInput;
            currentInput = "Unexpected Error";
        }
        return getDisplayText();
    }

    /**
     * Formats a number as an integer if it has no fractional part, or as a double otherwise.
     *
     * @param number the number to format.
     * @return the formatted number as a string.
     */
    public static String formatAsIntegerOrDouble(double number) {
        return (number == (long) number) ? String.valueOf((long) number) : String.valueOf(number);
    }

    /**
     * Appends parentheses to the current input based on the provided parentheses character.
     * A closing parenthesis is only added if there is a corresponding unmatched opening parenthesis.
     *
     * @param parentheses the parentheses character to append.
     * @return the updated display text.
     */
    public static String put_display_parentheses(String parentheses) {
        int openCount = countOccurrences(currentInput, '(');
        int closeCount = countOccurrences(currentInput, ')');

        if (currentInput.isEmpty() && parentheses.equals(")")) {
            return getDisplayText();
        }

        if (parentheses.equals("(")) {
            currentInput += "(";
        } else if (parentheses.equals(")") && openCount > closeCount) {
            currentInput += ")";
        }
        return getDisplayText();
    }

    /**
     * Counts the number of occurrences of a specified character in a given string.
     *
     * @param str the string in which to count occurrences.
     * @param ch  the character to count.
     * @return the number of occurrences of the character in the string.
     */
    private static int countOccurrences(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) {
                count++;
            }
        }
        return count;
    }

    /**
     * Adds a decimal point to the current input if the last character is a digit and
     * if the last number segment does not already contain a decimal point.
     *
     * @return the updated display text.
     */
    public static String add_comma() {
        if (!currentInput.isEmpty() && Character.isDigit(currentInput.charAt(currentInput.length() - 1))) {
            int lastOperatorIndex = Math.max(currentInput.lastIndexOf('+'),
                    Math.max(currentInput.lastIndexOf('-'),
                            Math.max(currentInput.lastIndexOf('*'),
                                    currentInput.lastIndexOf('/'))));
            String lastNumber = (lastOperatorIndex == -1) ? currentInput : currentInput.substring(lastOperatorIndex + 1);
            if (!lastNumber.contains(".")) {
                currentInput += ".";
            }
        }
        return getDisplayText();
    }

    /**
     * Returns the current input without the previous expression and formatting.
     *
     * @return the current input.
     */
    public static String getCurrentInput() {
        return currentInput;
    }

    /**
     * Sets the current input to the specified value and returns the full display text.
     *
     * @param input the new input value.
     * @return the updated display text.
     */
    public static String setCurrentInput(String input) {
        currentInput = input;
        return getDisplayText();
    }
}
