package com.friends.controllers;

import com.friends.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Controller class for displaying and managing the XML history of calculator entries.
 * Loads history data from an XML file, displays it in a TableView, and provides options
 * to delete individual or all history entries.
 */
public class XMLHistoryController {

    /**
     * The TableView for displaying history entries.
     */
    @FXML
    private TableView<HistoryController.HistoryEntry> historyTable;

    /**
     * The TableColumn displaying the ID of each history entry.
     */
    @FXML
    private TableColumn<HistoryController.HistoryEntry, Integer> idColumn;

    /**
     * The TableColumn displaying the expression of each history entry.
     */
    @FXML
    private TableColumn<HistoryController.HistoryEntry, String> expressionColumn;

    /**
     * The TableColumn displaying the result of each history entry.
     */
    @FXML
    private TableColumn<HistoryController.HistoryEntry, String> resultColumn;

    /**
     * The TableColumn displaying the timestamp of each history entry.
     */
    @FXML
    private TableColumn<HistoryController.HistoryEntry, String> timestampColumn;

    /**
     * ObservableList holding the history data.
     */
    private final ObservableList<HistoryController.HistoryEntry> historyData = FXCollections.observableArrayList();

    /**
     * Initializes the XMLHistoryController by configuring table columns,
     * loading history from the XML file, and setting the TableView items.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        expressionColumn.setCellValueFactory(new PropertyValueFactory<>("expression"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        loadHistoryFromXML();
        historyTable.setItems(historyData);
    }

    /**
     * Loads history entries from the XML file "history.xml" and populates the historyData list.
     */
    private void loadHistoryFromXML() {
        String filePath = "history.xml";
        try {
            File file = new File(filePath);
            if (!file.exists()) return;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName("entry");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String expression = element.getElementsByTagName("expression").item(0).getTextContent();
                    String result = element.getElementsByTagName("result").item(0).getTextContent();
                    String timestamp = element.getElementsByTagName("timestamp").item(0).getTextContent();
                    historyData.add(new HistoryController.HistoryEntry(id, expression, result, timestamp));
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
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
     * Deletes the selected history entry from the TableView and updates the XML file.
     * Displays an alert indicating success or if no entry is selected.
     */
    @FXML
    private void handleDelete() {
        HistoryController.HistoryEntry selectedEntry = historyTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            historyData.remove(selectedEntry);
            DBConnector.exportHistoryToXML("history.xml");
            showAlert("Success", "The selected entry has been deleted.");
        } else {
            showAlert("No Selection", "Please select an entry to delete.");
        }
    }

    /**
     * Deletes all history entries from the TableView and updates the XML file after confirmation.
     */
    @FXML
    private void handleDeleteAll() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete All");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("This will delete all entries from the XML file.");
        confirmationAlert.showAndWait().ifPresent(response -> {
            historyData.clear();
            DBConnector.exportHistoryToXML("history.xml");
            showAlert("Success", "All entries have been deleted.");
        });
    }

    /**
     * Displays an alert with the specified title and message.
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
}
