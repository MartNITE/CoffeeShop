package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import application.enums.DbTableNameEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class KonsumaciqController {

    @FXML
    private ChoiceBox<String> snch;

    @FXML
    private ChoiceBox<Integer> quantity;

    @FXML
    private Button cnfBtn;
    
    private JdbcDao jdbcDao = null;
    
    // Partial products lists
    private LinkedHashMap<String, List<String>> productsMap = null;
    
    private ObservableList<String> products = null;

    @FXML
    public void initialize() {
    	jdbcDao = new JdbcDao();
    	
    	productsMap = new LinkedHashMap<String, List<String>>();
    	productsMap.put(DbTableNameEnum.SANDWICHES.getTableName(), Arrays.asList("Шунка и кашкавал", "Пилешко филе", "Риба тон", "Вегетариански"));

    	List<String> productsValues = new ArrayList<>();
    	
    	for (List<String> list : productsMap.values()) { // Fill products
    		productsValues.addAll(list);
    	}
    	
    	products = FXCollections.observableArrayList(productsValues);
    	
        snch.setItems(products);
        quantity.setItems(FXCollections.observableArrayList(1, 2, 3));

        // Set event for button
        cnfBtn.setOnAction(event -> handleConfirmation());
    }

    private void handleConfirmation() {
    	getUserId(true);
    	
        String sandwich = snch.getValue();
        Integer qty = quantity.getValue();
        
        String tableName = getProductCategoryByName(sandwich);
        
        if (tableName == null) {
        	showAlert(AlertType.WARNING, "Грешка", "Категорията не е намерена.");
        	
        	return;
        }

        if (sandwich != null && qty != null) {
        	int realQuantity = jdbcDao.getSandwichQuantity(sandwich);
        	
        	if (realQuantity != -1 && realQuantity < qty) {
        		showAlert(AlertType.WARNING, "Грешка", "Няма от желаната наличност в скалда. Моля изберете друга стойност или проверете за наличност.");
        		
        		return;
        	}
        	
        	removeSandwich(sandwich, qty);
        } else {
            showAlert(AlertType.WARNING, "Грешка", "Моля, избери сандвич и брой.");
        }
    }
    
    // ----
    
    private void removeSandwich(String name, int quantity) {
    	int userId = getUserId(true);
    	
    	try {
    		boolean canConsume = userId != -1 && jdbcDao.canConsume(userId, quantity);
    		
    		if (canConsume) {
				if (jdbcDao.updateSandwichDec(name, quantity) && jdbcDao.addConsumption(userId, quantity, false)) {					
					showAlert(AlertType.INFORMATION, "Консумация", "Консумирани са " + quantity + " бр. от " + name + "(сандвич).");
				} else {
					showAlert(AlertType.ERROR, "Грешка", "Операцията не може да бъде извършена.");
				}
    		} else showAlert(AlertType.INFORMATION, "Прекалена консумация", "Вече сте консумирали лимита за деня или количеството надвишава максимума today.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    // ----
    
    // Session manager function(s)
    private int getUserId(boolean notifyOnUserMissing) {
    	int userId = SessionManager.getInstance().getUserId();
    	
    	if (notifyOnUserMissing && userId == -1) {
    		showAlert(AlertType.ERROR, "Грешка", "Сесиен потребител не беше намерен.");
    	}
    	
    	return userId;
    }
    // - Session manager function(s)
    
    // ----
    
    public String getProductCategoryByName(String value) { // Find map key
    	if (productsMap == null) return null;
    	
        for (Map.Entry<String, List<String>> entry : productsMap.entrySet()) {
            if (entry.getValue().contains(value)) {
                return entry.getKey();
            }
        }
        
        return null;
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

