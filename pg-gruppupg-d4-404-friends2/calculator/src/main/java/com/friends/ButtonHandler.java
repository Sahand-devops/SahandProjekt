package com.friends;

import com.friends.controllers.MemoryControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import static com.friends.Display.formatAsIntegerOrDouble;

/**
 * Handles button events from the calculator user interface.
 * Connects UI actions with corresponding display and memory operations.
 */
public class ButtonHandler {

    /**
     * The TextArea displaying calculator input and output.
     */
    @FXML
    public TextArea display;

    /**
     * Handles the number button click event.
     * Retrieves the number from the clicked button and updates the display.
     *
     * @param event the ActionEvent triggered by the button click.
     */
    public void handleNumberClick(ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();
        String text = Display.put_display_number(number);
        display.setText(text);
    }

    /**
     * Handles the operator button click event.
     * Retrieves the operator from the clicked button and updates the display.
     *
     * @param event the ActionEvent triggered by the button click.
     */
    public void handleOperatorClick(ActionEvent event) {
        String operator = ((javafx.scene.control.Button) event.getSource()).getText();
        String text = Display.put_display_operator(operator);
        display.setText(text);
    }

    /**
     * Handles the equals button click event.
     * Evaluates the current expression and updates the display with the result.
     *
     */

    public void handleEqualsClick() {
        String text = Display.processEquals();
        display.setText(text);
    }

    /**
     * Handles the clear button click event.
     * Clears the current input and previous expression.
     */
    public void handleClearClick() {
        String text = Display.clear_display();
        display.setText(text);
    }

    /**
     * Handles the delete button click event.
     * Removes the last character from the current input.
     *
     */
    public void handleDeleteClick() {
        String text = Display.remove_display();
        display.setText(text);
    }

    /**
     * Handles the comma button click event.
     * Adds a decimal point to the current number if applicable.
     *
     */
    public void handleCommaClick() {
        String text = Display.add_comma();
        display.setText(text);
    }

    /**
     * Handles the parentheses button click event.
     * Adds the appropriate parenthesis based on the clicked button.
     *
     * @param actionEvent the ActionEvent triggered by the parentheses button.
     */
    public void handleParenthesesClick(ActionEvent actionEvent) {
        String parentheses = ((javafx.scene.control.Button) actionEvent.getSource()).getText();
        String text = Display.put_display_parentheses(parentheses);
        display.setText(text);
    }

    /**
     * Opens the calculation history window.
     */
    @FXML
    public void handleHistoryClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/history.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Calculation History");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.print("Error occurred when trying to create history window");
        }
    }

    /**
     * Handles the memory add (M+) button click event.
     * Adds the current input value to memory and updates the display.
     */
    public void handleMemoryAdd() {
        try {
            double currentValue = Double.parseDouble(Display.getCurrentInput());
            MemoryControl.addMemory(currentValue);
            display.setText(Display.setCurrentInput(formatAsIntegerOrDouble(MemoryControl.getMemory())));
        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }

    /**
     * Handles the memory subtract (M-) button click event.
     * Subtracts the current input value from memory and updates the display.
     */
    public void handleMemorySubtract() {
        try {
            double currentValue = Double.parseDouble(Display.getCurrentInput());
            MemoryControl.subtractMemory(currentValue);
            display.setText(Display.setCurrentInput(formatAsIntegerOrDouble(MemoryControl.getMemory())));
        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }

    /**
     * Handles the memory clear (MC) button click event.
     * Clears the memory and resets the display to zero.
     */
    public void handleMemoryClear() {
        MemoryControl.clearMemory();
        display.setText(Display.setCurrentInput(formatAsIntegerOrDouble(0)));
    }

    /**
     * Handles the memory recall (MR) button click event.
     * Retrieves the memory value and updates the display.
     */
    public void handleMemoryRecall() {
        display.setText(Display.setCurrentInput(formatAsIntegerOrDouble(MemoryControl.getMemory())));
    }
}
