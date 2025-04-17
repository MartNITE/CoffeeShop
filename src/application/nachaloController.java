package application;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class nachaloController {
	
	private List<Stage> openStages = new ArrayList<>();
	
	@FXML
	private Label koie;
	
	@FXML
	public void initialize() {
		SessionManager sessionManager = SessionManager.getInstance();
		
		if (sessionManager != null) {
			koie.setText("Здравей, " + sessionManager.getUsername() + ".");
		}
	}
	
	@FXML
	public void notebtn(ActionEvent event) throws Exception{
		Stage primaryStage = new Stage();
		
		Parent root  = FXMLLoader.load(getClass().getResource("note.fxml"));
		
		Scene scene = new Scene(root,660,600);
		primaryStage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		openStages.add(primaryStage);
	}

	public void Konsumaciq(ActionEvent event) throws Exception{
		Stage primaryStage = new Stage();
		
		Parent root  = FXMLLoader.load(getClass().getResource("Konsumaciqfx.fxml"));
		
		Scene scene = new Scene(root,548, 400);
		primaryStage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
	
		openStages.add(primaryStage);
	}
	
	
	public void Brak(ActionEvent event) throws Exception{
		Stage primaryStage = new Stage();
		
		Parent root  = FXMLLoader.load(getClass().getResource("BrakFX.fxml"));
		
		Scene scene = new Scene(root, 500, 420);
		primaryStage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		openStages.add(primaryStage);
	}
	
	public void Table(ActionEvent event) throws Exception{
		Stage primaryStage = new Stage();
		
		Parent root  = FXMLLoader.load(getClass().getResource("tableFXML.fxml"));
		
		Scene scene = new Scene(root, 1500, 850);
		primaryStage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		openStages.add(primaryStage);
	}
	
	public void LogOut(ActionEvent event) throws Exception{
		// Secondary stage(s)
		if (openStages.size() > 0) { // Has any stages
			for (Stage stage : openStages) {
			    if (stage.isShowing()) stage.close();
			}
		}
		
		try {
			openStages.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Primary stage
    	Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		currentStage.close();
		
		// ----
		
		SessionManager sessionManager = SessionManager.getInstance();
		
		if (sessionManager != null) {
			sessionManager.clearSession();
		} else {
			showAlert(AlertType.ERROR, "Инфо", "Няма текуща потребителска сесия.");
		}
		
		// ----
		
		Stage primaryStage = new Stage();
		
		Parent root  = FXMLLoader.load(getClass().getResource("Main.fxml"));
		
		Scene scene = new Scene(root,600,400);
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
		
	private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

