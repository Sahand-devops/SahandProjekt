package com.friends.controllers;

import com.friends.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;

/**
 * Controller class for managing the history view.
 * Loads history entries from a JSON file, displays them in a table,
 * and provides functionality to delete individual or all entries.
 */
public class HistoryController {

    /**
     * The TableView displaying history entries.
     */
    @FXML
    private TableView<HistoryEntry> historyTable;

    /**
     * The TableColumn displaying the ID of each history entry.
     */
    @FXML
    private TableColumn<HistoryEntry, Integer> idColumn;

    /**
     * The TableColumn displaying the expression of each history entry.
     */
    @FXML
    private TableColumn<HistoryEntry, String> expressionColumn;

    /**
     * The TableColumn displaying the result of each history entry.
     */
    @FXML
    private TableColumn<HistoryEntry, String> resultColumn;

    /**
     * The TableColumn displaying the timestamp of each history entry.
     */
    @FXML
    private TableColumn<HistoryEntry, String> timestampColumn;

    /**
     * The observable list containing the history data.
     */
    private final ObservableList<HistoryEntry> historyData = FXCollections.observableArrayList();

    /**
     * Initializes the controller by setting up table columns, loading history data from JSON,
     * and assigning the data to the TableView.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        expressionColumn.setCellValueFactory(new PropertyValueFactory<>("expression"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadHistoryFromJSON();
        historyTable.setItems(historyData);
    }

    /**
     * Loads history entries from the "history.json" file and populates the historyData list.
     */
    private void loadHistoryFromJSON() {
        String filePath = "history.json";
        System.out.println("Loading history from: " + filePath);

        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder jsonContent = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                jsonContent.append((char) c);
            }
            System.out.println("JSON Content: " + jsonContent);

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.getJSONObject(i);
                historyData.add(new HistoryEntry(
                        record.getInt("id"),
                        record.getString("expression"),
                        record.getString("result"),
                        record.getString("timestamp")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error loading JSON: " + e.getMessage());
        }
    }

    /**
     * Closes the history window.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) historyTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Deletes the selected history entry from the TableView and the database,
     * then updates the JSON file. Displays an alert indicating success or if no entry was selected.
     */
    @FXML
    private void handleDelete() {
        HistoryEntry selectedEntry = historyTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            historyData.remove(selectedEntry);
            DBConnector.deleteHistoryEntry(selectedEntry.getId());
            DBConnector.exportHistoryToJSON("history.json");
            showAlert("Success", "The selected entry has been deleted.");
        } else {
            showAlert("No Selection", "Please select an entry to delete.");
        }
    }

    /**
     * Deletes all history entries from the TableView and the database after user confirmation,
     * then updates the JSON file.
     */
    @FXML
    private void handleDeleteAll() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete All");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("This will delete all entries from the database and JSON file.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            DBConnector.clearHistory();
            historyData.clear();
            DBConnector.exportHistoryToJSON("history.json");
            showAlert("Success", "All entries have been deleted.");
        });
    }

    /**
     * Opens a new window that displays the history in XML format.
     */
    @FXML
    public void handleXMLHistoryClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/xml_history.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Calculation History (XML)");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    /**
     * Displays an alert with the specified title and content message.
     *
     * @param title   the title of the alert.
     * @param content the content message of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * A nested static class representing a history entry.
     */
    public static class HistoryEntry {
        private final int id;
        private final String expression;
        private final String result;
        private final String timestamp;

        /**
         * Constructs a HistoryEntry with the specified details.
         *
         * @param id         the unique identifier of the entry.
         * @param expression the mathematical expression.
         * @param result     the result of the expression.
         * @param timestamp  the timestamp of when the entry was created.
         */
        public HistoryEntry(int id, String expression, String result, String timestamp) {
            this.id = id;
            this.expression = expression;
            this.result = result;
            this.timestamp = timestamp;
        }

        /**
         * Returns the ID of the history entry.
         *
         * @return the entry ID.
         */
        public int getId() {
            return id;
        }

        /**
         * Returns the expression of the history entry.
         *
         * @return the mathematical expression.
         */
        public String getExpression() {
            return expression;
        }

        /**
         * Returns the result of the history entry.
         *
         * @return the result.
         */
        public String getResult() {
            return result;
        }

        /**
         * Returns the timestamp of the history entry.
         *
         * @return the timestamp.
         */
        public String getTimestamp() {
            return timestamp;
        }
    }
}
