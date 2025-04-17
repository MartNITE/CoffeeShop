package application;



import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class note {

    @FXML
    private TextArea TextArea; // Matches fx:id in FXML

    private static final String FILE_PATH = "zaqvki.txt"; // Path to the text file

    @FXML
    public void initialize() {
        // Load the saved text when the program starts
        loadText();
    }

    // This method is called when the "save" button is clicked
    @FXML
    private void saveText() {
        String text = TextArea.getText(); // Get the text from the TextArea
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(text); // Write the text to the file
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
        Stage stage = (Stage) TextArea.getScene().getWindow();
        stage.close();

    }

    // This method loads the text from the file into the TextArea
    private void loadText() {
        File file = new File(FILE_PATH);
        if (file.exists()) { // Check if the file exists
            try (Scanner scanner = new Scanner(file)) {
                StringBuilder content = new StringBuilder();
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n"); // Read the file line by line
                }
                TextArea.setText(content.toString()); // Set the text in the TextArea
            } catch (IOException e) {
                e.printStackTrace(); // Handle file reading errors
            }
        }
    }
}