package com.friends.operators;

import javafx.scene.control.TextField;

public class PiOperator {
    public void handlePiOperator(TextField display) {
        if (display != null) {
            String currentText = display.getText();

            if (currentText.contains("3.14")) {
                return;
            }
            double piValue = 3.14;

            display.setText(currentText + piValue);
        } else {
            System.err.println("Display is null in PiOperator");
        }
    }
}