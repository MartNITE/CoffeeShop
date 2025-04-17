package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class loginController {
	
	@FXML
	private Label lglbl;
	
	@FXML
	private TextField lusrtxt;
	
	@FXML
	private TextField lpsw;
	
	@FXML
	private Button lgbtn;

	@FXML
	public void Login(ActionEvent event) throws Exception{
		System.out.println(lusrtxt.getText());
		System.out.println(lpsw.getText());
		
		Window owner = lgbtn.getScene().getWindow();

        if (lusrtxt.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Моля въведете потребител");
            
            return;
        }
        
        if (lpsw.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Моля въведете парола");
            
            return;
        }
        
        String usrtxt = lusrtxt.getText();
        String psw = lpsw.getText();
        
        JdbcDao jdbcDao = new JdbcDao();
        
        boolean flag = jdbcDao.validateLoginUser(usrtxt, psw);

        if (!flag) {
            infoBox("Please enter correct credentials", null, "Failed");
            
            lglbl.setText("Невалидни данни за вход като user.");
        } else {
            infoBox("Login Successful!", null, "Succeed");
            
            lglbl.setText("Успешен вход.");
        	
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    		currentStage.close();
			
			Stage primaryStage = new Stage();
			
			Parent root  = FXMLLoader.load(getClass().getResource("Nachalo.fxml"));
			
			Scene scene = new Scene(root, 1100, 550);
			
			primaryStage.setResizable(false);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
        }
	}
	
	public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
	
	private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
