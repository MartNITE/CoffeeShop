package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import application.enums.CoffeeCupSizeEnum;
import application.enums.DbTableNameEnum;

public class BrakController {

    @FXML
    private ChoiceBox<String> productChoice;

    @FXML
    private ChoiceBox<Integer> quantityChoice;

    @FXML
    private TextField searchField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button searchButton;
    
    private JdbcDao jdbcDao = null;

    // Full product list
    private LinkedHashMap<String, List<String>> productsMap = null;
    
    private ObservableList<String> allProducts = null;
    
    @FXML
    public void initialize() {
    	jdbcDao = new JdbcDao();
    	
    	// Initialize a map of products (table name, product title)
    	productsMap = new LinkedHashMap<String, List<String>>();
    	productsMap.put(DbTableNameEnum.SANDWICHES.getTableName(), Arrays.asList("Шунка и кашкавал", "Пилешко филе", "Риба тон", "Вегетариански"));
        productsMap.put(DbTableNameEnum.DESSERTS.getTableName(), Arrays.asList("Шоколадов кейк", "Плодова торта", "Тирамису", "Еклери"));
        productsMap.put(DbTableNameEnum.COFFEE_CUPS.getTableName(), Arrays.asList("Еспресо", "Капучино", "Лате"));
    	
        List<String> allProductValues = new ArrayList<>();
        
        for (List<String> list : productsMap.values()) { // Fill all products
        	allProductValues.addAll(list);
        }
    	
    	allProducts = FXCollections.observableArrayList(allProductValues);

        // Fill product and quantity choices
        productChoice.setItems(allProducts);
        quantityChoice.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // Button handlers
        addButton.setOnAction(e -> handleAdd());
        removeButton.setOnAction(e -> handleRemove());
        searchButton.setOnAction(e -> handleSearch());
    }

    // Add
    private void handleAdd() {
    	getUserId(true);
    	
        String product = productChoice.getValue();
        Integer quantity = quantityChoice.getValue();
        
        String tableName = getProductCategoryByName(product);

        if (product != null && tableName != null && (quantity != null && quantity >= 1 && quantity <= 10)) {
        	if (tableName == DbTableNameEnum.SANDWICHES.getTableName()) {
        		addSandwich(product, quantity);
        	} else if (tableName == DbTableNameEnum.DESSERTS.getTableName()) {
        		addDessert(product, quantity);
        	} else if (tableName == DbTableNameEnum.COFFEE_CUPS.getTableName()) {
        		addCoffeeCup(product, quantity);
        	} else {
        		showAlert(AlertType.ERROR, "Грешка", "Категорията на продукта на беше открита.");
        	}
        } else {
            showAlert(AlertType.WARNING, "Грешка", "Моля, избери продукт и брой.");
        }
    }
    
    private void addDessert(String name, int quantity) {
    	try {
			if (jdbcDao.updateDessertInc(name, quantity)) {
				showAlert(AlertType.INFORMATION, "Добавяне", "Добавени са " + quantity + " бр. от " + name + " (десерт).");
			} else {
				showAlert(AlertType.ERROR, "Грешка", "Операцията не може да бъде извършена.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private void addSandwich(String name, int quantity) {
    	try {
			if (jdbcDao.updateSandwichInc(name, quantity)) {
				showAlert(AlertType.INFORMATION, "Добавяне", "Добавени са " + quantity + " бр. от " + name + " (сандвич).");
			} else {
				showAlert(AlertType.ERROR, "Грешка", "Операцията не може да бъде извършена.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private void addCoffeeCup(String name, int quantity) {
    	CoffeeCupSizeEnum coffeeCupSizeEnum = getCoffeeCupSizeEnum(name);
    	
    	if (coffeeCupSizeEnum == null) {
    		showAlert(AlertType.WARNING, "Грешка", "Кафе от този тип не може да бъде добавено.");
    		
    		return;
    	}
    	
    	try {
			if (jdbcDao.updateCoffeeCupInc(coffeeCupSizeEnum, quantity)) {
				showAlert(AlertType.INFORMATION, "Добавяне", "Добавени са " + quantity + " бр. от " + name + " (кафе).");
			} else {
				showAlert(AlertType.ERROR, "Грешка", "Операцията не може да бъде извършена.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    // - Add

    private void handleRemove() {
    	getUserId(true);
    	
        String product = productChoice.getValue();
        Integer quantity = quantityChoice.getValue();
        
        String tableName = getProductCategoryByName(product);

        if (product != null && tableName != null && (quantity != null && quantity >= 1 && quantity <= 10)) {
        	if (tableName == DbTableNameEnum.SANDWICHES.getTableName()) {
        		int realQuantity = jdbcDao.getSandwichQuantity(product);
            	
            	if (realQuantity != -1 && realQuantity < quantity) {
            		showAlert(AlertType.WARNING, "Грешка", "Няма от желаната наличност в скалда. Моля изберете друга стойност или проверете за наличност.");
            		
            		return;
            	}
        		
        		removeSandwich(product, quantity);
        	} else if (tableName == DbTableNameEnum.DESSERTS.getTableName()) {
        		int realQuantity = jdbcDao.getDessertQuantity(product);
            	
            	if (realQuantity != -1 && realQuantity < quantity) {
            		showAlert(AlertType.WARNING, "Грешка", "Няма от желаната наличност в скалда. Моля изберете друга стойност или проверете за наличност.");
            		
            		return;
            	}
        		
        		removeDessert(product, quantity);
        	} else if (tableName == DbTableNameEnum.COFFEE_CUPS.getTableName()) {
        		removeCoffeeCup(product, quantity);
        	} else {
        		showAlert(AlertType.ERROR, "Грешка", "Категорията на продукта на беше открита.");
        	}
        } else {
            showAlert(AlertType.WARNING, "Грешка", "Моля, избери продукт и брой.");
        }
    }
    
    // Remove
    private void removeDessert(String name, int quantity) {
    	try {
			if (jdbcDao.updateDessertDec(name, quantity)) {
				showAlert(AlertType.INFORMATION, "Бракуване", "Бракувани са " + quantity + " бр. от " + name + "(десерт).");
			} else {
				showAlert(AlertType.ERROR, "Грешка", "Операцията не може да бъде извършена.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private void removeSandwich(String name, int quantity) {
    	try {
			if (jdbcDao.updateSandwichDec(name, quantity)) {
				showAlert(AlertType.INFORMATION, "Бракуване", "Бракувани са " + quantity + " бр. от " + name + "(сандвич).");
			} else {
				showAlert(AlertType.ERROR, "Грешка", "Операцията не може да бъде извършена.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private void removeCoffeeCup(String name, int quantity) {
    	CoffeeCupSizeEnum coffeeCupSizeEnum = getCoffeeCupSizeEnum(name);
    	
    	if (coffeeCupSizeEnum == null) {
    		showAlert(AlertType.WARNING, "Грешка", "Кафе от този тип не може да бъде премахнато.");
    		
    		return;
    	}
    	
    	int realQuantity = jdbcDao.getCoffeeCupQuantity(coffeeCupSizeEnum);
    	
    	if (realQuantity != -1 && realQuantity < quantity) {
    		showAlert(AlertType.WARNING, "Грешка", "Няма от желаната наличност в скалда. Моля изберете друга стойност или проверете за наличност.");
    		
    		return;
    	}
    	
    	try {
			if (jdbcDao.updateCoffeeCupDec(coffeeCupSizeEnum, quantity)) {
				showAlert(AlertType.INFORMATION, "Бракуване", "Бракувани са " + quantity + " бр. от " + name + "(кафе).");
			} else {
				showAlert(AlertType.ERROR, "Грешка", "Операцията не може да бъде извършена.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    // - Remove

    private void handleSearch() {
        String searchQuery = searchField.getText().toLowerCase();

        if (searchQuery.isEmpty()) {
            productChoice.setItems(FXCollections.observableArrayList(allProducts));
            showAlert(AlertType.INFORMATION, "Търсене", "Показани са всички продукти.");
        } else {
            List<String> filtered = allProducts.stream()
                    .filter(p -> p.toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                showAlert(AlertType.INFORMATION, "Резултат", "Няма намерени продукти.");
            } else {
                productChoice.setItems(FXCollections.observableArrayList(filtered));
            }
        }
    }
    
    public String getProductCategoryByName(String value) { // Find map key
    	if (productsMap == null) return null;
    	
        for (Map.Entry<String, List<String>> entry : productsMap.entrySet()) {
            if (entry.getValue().contains(value)) {
                return entry.getKey();
            }
        }
        
        return null;
    }
    
    // Coffee cup check
    private CoffeeCupSizeEnum getCoffeeCupSizeEnum(String name) {
    	CoffeeCupSizeEnum coffeeCupSizeEnum;
    	
    	switch (name) {
			case "Еспресо":
				coffeeCupSizeEnum = CoffeeCupSizeEnum.SMALL;
				
				break;
			case "Капучино":
				coffeeCupSizeEnum = CoffeeCupSizeEnum.MEDIUM;
				
				break;
			case "Лате":
				coffeeCupSizeEnum = CoffeeCupSizeEnum.LARGE;
				
				break;
				
			default:
				coffeeCupSizeEnum = null;
		}
    	
    	return coffeeCupSizeEnum;
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

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

