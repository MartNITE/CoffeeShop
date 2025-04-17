package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class adminController {
	@FXML
    private Label adminLBL;
	
	@FXML
	private TextField adminUSR;
	
	@FXML
	private TextField adminPass;
	
	@FXML
	private Button abt;
	
	@FXML
	public void Admin(ActionEvent event) throws Exception {
		System.out.println(adminUSR.getText());
		System.out.println(adminPass.getText());
		
		Window owner = abt.getScene().getWindow();

        if (adminUSR.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Моля въведете потребител");
            
            return;
        }
        
        if (adminPass.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Моля въведете парола");
            
            return;
        }
        
        String usrtxt = adminUSR.getText();
        String psw = adminPass.getText();
        
        JdbcDao jdbcDao = new JdbcDao();
        
        boolean flag = jdbcDao.validateLoginAdmin(usrtxt, psw);
        
        if (!flag) {
            infoBox("Please enter correct credentials", null, "Failed");
            
            adminLBL.setText("Невалидни данни за вход като admin.");
        } else {
            infoBox("Login Successful!", null, "Succeed");
            
            adminLBL.setText("Успешен вход.");
			
        	Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    		currentStage.close();
			Stage primaryStage = new Stage();
			
			Parent root  = FXMLLoader.load(getClass().getResource("registerfxml.fxml"));
			
			Scene scene = new Scene(root,365,400);
			
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
