package application;

import application.util.EmailUtil;
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

public class RegisterController {
	
	@FXML
	private Label label;
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField password;
	
	@FXML
	private TextField email;
	
	@FXML
	private Button submitbtn;

	public void Insert(ActionEvent event) throws Exception{
		System.out.println("User: " + username.getText());
		System.out.println("Password: " + password.getText());
		System.out.println("Email: " + email.getText());
		
		Window owner = submitbtn.getScene().getWindow();

        if (username.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Моля въведете потребител");
            
            return;
        }
        
        if (password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Моля въведете парола");
            
            return;
        }
        
        if (email.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Моля въведете email.");
            
            return;
        }
                
        String usrtxt = username.getText();
        String psw = password.getText();
        String emailText = email.getText();
        
        if (!EmailUtil.validate(emailText)) {
        	showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Невалиден email адрес");
        	
        	return;
        }
        
        JdbcDao jdbcDao = new JdbcDao();
        
        boolean flag = jdbcDao.insertUser(usrtxt, psw, emailText);
        
        if (!flag) {
        	infoBox("Потребителят не беше добавен.", null, "Failed");
            
            label.setText("Грешка при изпълнение на заявка.");
        } else {
        	Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    		currentStage.close();
        	Stage primaryStage = new Stage();
    		
    		Parent root  = FXMLLoader.load(getClass().getResource("LoginFXml.fxml"));
    		
    		Scene scene = new Scene(root,300,300);
    		
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
