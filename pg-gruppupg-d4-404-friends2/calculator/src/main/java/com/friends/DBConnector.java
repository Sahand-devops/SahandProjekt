package com.friends;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;

/**
 * Handles database connection and operations related to the calculator's history.
 */
public class DBConnector {

    /**
     * The URL for the database connection.
     */
    private static final String URL = "jdbc:mariadb://localhost:3306/Calculator";

    /**
     * The username for the database.
     */
    private static final String USER = "root";

    /**
     * The password for the database.
     */
    private static final String PASSWORD = "p";

    /**
     * Creates a connection to the database.
     *
     * @return a {@link Connection} to the database.
     * @throws SQLException if the connection fails.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Creates the history table in the database if it does not already exist.
     */
    public static void createHistoryTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS history ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "expression VARCHAR(255), "
                + "result VARCHAR(255), "
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'history' created or already exists.");
        } catch (SQLException e) {
            System.out.print("Error connecting to database/creating table");
        }
    }

    /**
     * Inserts a new history record into the database.
     *
     * @param expression the mathematical expression that was calculated.
     * @param result     the result of the calculation.
     */
    public static void insertHistory(String expression, String result) {
        String insertSQL = "INSERT INTO history (expression, result) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, expression);
            pstmt.setString(2, result);
            pstmt.executeUpdate();
            System.out.println("New row added to the table.");
        } catch (SQLException e) {
            System.out.print("Error connecting to database or inserting into table");
        }
    }

    /**
     * Exports the entire history from the database to a JSON file.
     *
     * @param filePath the file path where the JSON file should be saved.
     */
    public static void exportHistoryToJSON(String filePath) {
        String query = "SELECT * FROM history";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            JSONArray jsonArray = new JSONArray();

            while (rs.next()) {
                JSONObject record = new JSONObject();
                record.put("id", rs.getInt("id"));
                record.put("expression", rs.getString("expression"));
                record.put("result", rs.getString("result"));
                record.put("timestamp", rs.getTimestamp("timestamp").toString());
                jsonArray.put(record);
            }

            try (FileWriter file = new FileWriter(filePath)) {
                file.write(jsonArray.toString(4));
                System.out.println("Data exported to " + filePath + " successfully!");
            } catch (IOException e) {
                System.err.println("Error writing JSON to file: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Exports the entire history from the database to an XML file.
     *
     * @param filePath the file path where the XML file should be saved.
     */
    public static void exportHistoryToXML(String filePath) {
        String query = "SELECT * FROM history";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("history");
            document.appendChild(rootElement);

            while (rs.next()) {
                Element entry = document.createElement("entry");

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(rs.getInt("id"))));
                entry.appendChild(id);

                Element expression = document.createElement("expression");
                expression.appendChild(document.createTextNode(rs.getString("expression")));
                entry.appendChild(expression);

                Element result = document.createElement("result");
                result.appendChild(document.createTextNode(rs.getString("result")));
                entry.appendChild(result);

                Element timestamp = document.createElement("timestamp");
                timestamp.appendChild(document.createTextNode(rs.getTimestamp("timestamp").toString()));
                entry.appendChild(timestamp);

                rootElement.appendChild(entry);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("Data exported to " + filePath + " successfully!");
        } catch (Exception e) {
            System.err.println("Error exporting XML: " + e.getMessage());
        }
    }

    /**
     * Returns the absolute file path for a JSON file.
     *
     * @param s the relative file path.
     * @return the absolute file path.
     */
    public static String getJsonFilePath(String s) {
        return Paths.get(s).toAbsolutePath().toString();
    }

    /**
     * Returns the absolute file path for an XML file.
     *
     * @param s the relative file path.
     * @return the absolute file path.
     */
    public static String getXmlFilePath(String s) {
        return Paths.get(s).toAbsolutePath().toString();
    }

    /**
     * Deletes a specific history record from the database based on the given ID.
     *
     * @param id the ID of the record to be deleted.
     */
    public static void deleteHistoryEntry(int id) {
        String deleteSQL = "DELETE FROM history WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Entry with ID " + id + " deleted from the database.");
        } catch (SQLException e) {
            System.out.print("An error occurred when deleting from table");
        }
    }

    /**
     * Deletes all history records from the database.
     */
    public static void clearHistory() {
        String clearSQL = "DELETE FROM history";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(clearSQL)) {
            pstmt.executeUpdate();
            System.out.println("All entries deleted from the database.");
        } catch (SQLException e) {
            System.out.print("");
        }
    }

    /**
     * Main method to create the history table and export history to JSON and XML files.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        createHistoryTable();
        exportHistoryToJSON(getJsonFilePath("calculator/src/main/resources/history.json"));
        exportHistoryToXML(getXmlFilePath("calculator/src/main/resources/history.xml"));
    }
}
