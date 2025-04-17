package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public void Vlez(ActionEvent event) throws Exception{
		Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		currentStage.close();  // Close the current window

		Stage primaryStage = new Stage();
		Parent root  = FXMLLoader.load(getClass().getResource("LoginFXml.fxml"));
		Scene scene = new Scene(root,300,300);
		primaryStage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	public void Registraciq(ActionEvent event)throws Exception{
		Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		currentStage.close();

		Stage primaryStage = new Stage();
		Parent root  = FXMLLoader.load(getClass().getResource("adminFXML.fxml"));
		Scene scene = new Scene(root,300,350);
		primaryStage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root  = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		// DB		
		Database.getConnection();
		// - DB
		
		launch(args);
	}
}
