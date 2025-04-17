package application;

import java.sql.Connection;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.enums.CoffeeCupSizeEnum;
import application.envr.DatabaseConnection;
import application.models.CoffeeCupModel;
import application.models.DessertModel;
import application.models.SandwichModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class JdbcDao {
	
	private final String SELECT_QUERY_USER = "SELECT * FROM users WHERE username = ? and password = ?";
	private final String SELECT_QUERY_ADMIN = "SELECT * FROM admins WHERE username = ? and password = ?";
	private final String INSERT_QUERY_USER = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
	
	private final String GET_SANDWICHES = "SELECT * FROM sandwiches";
	private final String GET_DESSERTS = "SELECT * FROM desserts";
	private final String GET_COFFEE_CUPS = "SELECT * FROM coffee_cups";
	
	private final String READ_SANDWICH_QUANTITY = "SELECT quantity FROM sandwiches WHERE name = ?";
	private final String READ_DESSERT_QUANTITY = "SELECT quantity FROM desserts WHERE name = ?";
	private final String READ_COFFEE_CUP_QUANTITY = "SELECT quantity FROM coffee_cups WHERE size = ?";
	
	private final String UPDATE_SANDWICHES = "UPDATE sandwiches SET quantity = ?, expiration_date = ? WHERE name = ?";
	private final String UPDATE_DESSERTS = "UPDATE desserts SET quantity = ?, expiration_date = ? WHERE name = ?";
	private final String UPDATE_COFFEE_CUPS = "UPDATE coffee_cups SET quantity = ? WHERE size = ?";
	
	private final String CHECK_CONSUMPTION = "SELECT COALESCE(SUM(quantity), 0) AS total_products_today " +
            "FROM consumption_logs " +
            "WHERE user_id = ? AND DATE(consumed_at) = CURDATE()";
	private final String INSERT_CONSUMPTION = "INSERT INTO consumption_logs (user_id, quantity) VALUES (?, ?)";
	
	public boolean validateLoginUser(String user, String pass) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
            
	    	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY_USER);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);

            if (LOG) System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
        
            if (resultSet.next()) {
            	int userId = resultSet.getInt("user_id");
            	String username = resultSet.getString("username");
            	
            	SessionManager.getInstance().setUserSession(userId, username);
            	
            	return true;
            }
        } catch (SQLException e) {
            if (LOG) printSQLException((SQLException) e);
	    }
     
	    return false;
	 }
	 
	 public boolean validateLoginAdmin(String user, String pass) throws SQLException, ClassNotFoundException {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY_ADMIN);
	        preparedStatement.setString(1, user);
	        preparedStatement.setString(2, pass);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();
	    
	        if (resultSet.next()) return true;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
	 
	     return false;
	 }
	 
	 public boolean insertUser(String username, String password, String email) throws SQLException, ClassNotFoundException {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_USER);
	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, password);
	        preparedStatement.setString(3, email);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        preparedStatement.executeUpdate();
	    
	        return true;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
	 
	     return false;
	 }
	 
	 // Sandwiches
	 public ObservableList<SandwichModel> getSandwiches() throws SQLException, ClassNotFoundException {
		 ObservableList<SandwichModel> sandwichesList = FXCollections.observableArrayList();
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(GET_SANDWICHES);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("sandwich_id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                Date expDate = resultSet.getDate("expiration_date");
                
                sandwichesList.add(new SandwichModel(id, name, quantity, expDate));
            }
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 return sandwichesList;
	 }
	 
	 public int getSandwichQuantity(String sandwichName) {
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(READ_SANDWICH_QUANTITY);
	        preparedStatement.setString(1, sandwichName);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            } else return -1;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 return -1;
	 }
	 
	 public boolean updateSandwichInc(String sandwichName, int withQuantity) throws ClassNotFoundException, SQLException { // Increase
		 int quantity = getSandwichQuantity(sandwichName);
		 
		 if (LOG) {
			 if (quantity == -1) System.out.println("Quantity is invalid");
		 }
		 
		 if (quantity == -1) return false;
		 
		 if (quantity < 0) quantity = 0;
		 		 
		 return updateSandwich(sandwichName, quantity + withQuantity);
	 }
	 
	 public boolean updateSandwichDec(String sandwichName, int withQuantity) throws ClassNotFoundException, SQLException { // Decrease
		 int quantity = getSandwichQuantity(sandwichName);
		 
		 if (LOG) {
			 if (quantity == -1) System.out.println("Quantity is invalid");
		 }
		 
		 if (quantity == -1) return false;
		 
		 int newQuantity = quantity - withQuantity;
		 
		 if (newQuantity < 0) newQuantity = 0;
		 		 
		 return updateSandwich(sandwichName, newQuantity);
	 }
	 
	 private boolean updateSandwich(String sandwichName, int quantity) throws SQLException, ClassNotFoundException {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 
		 System.out.println("Updating sandwich quantity for type \"" + sandwichName + "\" to " + quantity);
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SANDWICHES);
	        preparedStatement.setInt(1, quantity);
	        preparedStatement.setDate(2, new Date(System.currentTimeMillis() + WEEK_MILLIS));
	        preparedStatement.setString(3, sandwichName);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        preparedStatement.executeUpdate();
	        return true;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
	 
	     return false;
	 }
	
	 // Desserts
	 public ObservableList<DessertModel> getDesserts() throws SQLException, ClassNotFoundException {
		 ObservableList<DessertModel> dessertsList = FXCollections.observableArrayList();
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(GET_DESSERTS);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("dessert_id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                Date expDate = resultSet.getDate("expiration_date");
                
                dessertsList.add(new DessertModel(id, name, quantity, expDate));
            }
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 return dessertsList;
	 }
	 
	 public int getDessertQuantity(String dessertName) {
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(READ_DESSERT_QUANTITY);
	        preparedStatement.setString(1, dessertName);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            } else return -1;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 return -1;
	 }
	 
	 public boolean updateDessertInc(String dessertName, int withQuantity) throws ClassNotFoundException, SQLException { // Increase
		 int quantity = getDessertQuantity(dessertName);
		 
		 if (LOG) {
			 if (quantity == -1) System.out.println("Quantity is invalid");
		 }
		 
		 if (quantity == -1) return false;
		 
		 if (quantity < 0) quantity = 0;
		 		 
		 return updateDessert(dessertName, quantity + withQuantity);
	 }
	 
	 public boolean updateDessertDec(String dessertName, int withQuantity) throws ClassNotFoundException, SQLException { // Decrease
		 int quantity = getDessertQuantity(dessertName);
		 
		 if (LOG) {
			 if (quantity == -1) System.out.println("Quantity is invalid");
		 }
		 
		 if (quantity == -1) return false;
		 
		 int newQuantity = quantity - withQuantity;
		 
		 if (newQuantity < 0) newQuantity = 0;
		 		 
		 return updateDessert(dessertName, newQuantity);
	 }
	 
	 private boolean updateDessert(String dessertName, int quantity) throws SQLException, ClassNotFoundException {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 
		 System.out.println("Updating desset quantity for type \"" + dessertName + "\" to " + quantity);
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DESSERTS);
	    	preparedStatement.setInt(1, quantity);
	        preparedStatement.setDate(2, new Date(System.currentTimeMillis() + WEEK_MILLIS));
	    	preparedStatement.setString(3, dessertName);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        preparedStatement.executeUpdate();
	    
	        return true;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
	 
	     return false;
	 }
	 
	 // Coffee cups
	 public ObservableList<CoffeeCupModel> getCoffeeCups() throws SQLException, ClassNotFoundException {
		 ObservableList<CoffeeCupModel> coffeeCupsList = FXCollections.observableArrayList();
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(GET_COFFEE_CUPS);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("cup_id");
                String size = resultSet.getString("size");
                int quantity = resultSet.getInt("quantity");
                
                coffeeCupsList.add(new CoffeeCupModel(id, getCoffeeCupSizeEnum(size), quantity));
            }
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 return coffeeCupsList;
	 }
	 
	 public int getCoffeeCupQuantity(CoffeeCupSizeEnum coffeeCupSize) {
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(READ_COFFEE_CUP_QUANTITY);
	        preparedStatement.setString(1, coffeeCupSize.size);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            } else return -1;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 return -1;
	 }
	 
	 public boolean updateCoffeeCupInc(CoffeeCupSizeEnum coffeeCupSize, int withQuantity) throws ClassNotFoundException, SQLException { // Increase
		 int quantity = getCoffeeCupQuantity(coffeeCupSize);
		 
		 if (LOG) {
			 if (quantity == -1) System.out.println("Quantity is invalid");
		 }
		 
		 if (quantity == -1) return false;
		 
		 if (quantity < 0) quantity = 0;
		 		 
		 return updateCoffeeCup(coffeeCupSize, quantity + withQuantity);
	 }
	 
	 public boolean updateCoffeeCupDec(CoffeeCupSizeEnum cofeeeCupSize, int withQuantity) throws ClassNotFoundException, SQLException { // Decrease
		 int quantity = getCoffeeCupQuantity(cofeeeCupSize);
		 
		 if (LOG) {
			 if (quantity == -1) System.out.println("Quantity is invalid");
		 }
		 
		 if (quantity == -1) return false;
		 
		 int newQuantity = quantity - withQuantity;
		 
		 if (newQuantity < 0) newQuantity = 0;
		 		 
		 return updateCoffeeCup(cofeeeCupSize, newQuantity);
	 }
	 
	 private boolean updateCoffeeCup(CoffeeCupSizeEnum size, int quantity) throws SQLException, ClassNotFoundException {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COFFEE_CUPS);
	    	preparedStatement.setInt(1, quantity);
	    	preparedStatement.setString(2, size.size);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        preparedStatement.executeUpdate();
	    
	        return true;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
	 
	     return false;
	 }
	 
	 // ----
	 
	 public int getTotalProductsToday(int userId) throws SQLException, ClassNotFoundException {
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(CHECK_CONSUMPTION);
	        preparedStatement.setInt(1, userId);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	int consumedPoductsToday = resultSet.getInt("total_products_today");
            	
       		 	if (LOG) System.out.println("Consumed today: " + consumedPoductsToday);
            	
            	return consumedPoductsToday;
            } else {
            	if (LOG) System.out.println("Consumed today: No consumption");
            	
            	return CONSUMED_TODAY_DEFAULT;
            }
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 if (LOG) System.out.println("Consumed today default: No consumption");
	    
	    return CONSUMED_TODAY_DEFAULT;
	 }
	 
	 public boolean canConsume(int userId, int count) throws SQLException, ClassNotFoundException {
		 int totalProductsToday = getTotalProductsToday(userId);
		 
		 return totalProductsToday + count <= MAX_CONSUMED_ITEMS_PER_DAY;
	 }
	 
	 public boolean addConsumption(int userId, int count, boolean forceFixCount) throws SQLException, ClassNotFoundException {	 
		 if (!canConsume(userId, count) || (!forceFixCount && count > MAX_CONSUMED_ITEMS_PER_DAY)) {
	        return false;
		 }
		 
		 int productsCountFixed = (count > MAX_CONSUMED_ITEMS_PER_DAY) ? MAX_CONSUMED_ITEMS_PER_DAY : count;
		 
		 try {
	    	Connection connection = DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
	        
	    	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CONSUMPTION);
	        preparedStatement.setInt(1, userId);
	        preparedStatement.setInt(2, productsCountFixed);
	
	        if (LOG) System.out.println(preparedStatement);
	
	        preparedStatement.executeUpdate();
	    
	        return true;
	     } catch (SQLException e) {
	    	 if (LOG) printSQLException((SQLException) e);
	     }
		 
		 return false;
	 }
	 
	 // ----

	 public static void printSQLException(SQLException ex) {
    	if (!LOG) return;
    	
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
	 }
	 
	 // Coffee cup check
    private CoffeeCupSizeEnum getCoffeeCupSizeEnum(String value) {
    	CoffeeCupSizeEnum coffeeCupSizeEnum;
    	
    	switch (value) {
			case "small":
				coffeeCupSizeEnum = CoffeeCupSizeEnum.SMALL;
				
				break;
			case "medium":
				coffeeCupSizeEnum = CoffeeCupSizeEnum.MEDIUM;
				
				break;
			case "large":
				coffeeCupSizeEnum = CoffeeCupSizeEnum.LARGE;
				
				break;
				
			default:
				coffeeCupSizeEnum = null;
		}
    	
    	return coffeeCupSizeEnum;
    }
	    
	 
	 // Date helpers
	 private static final int CONSUMED_TODAY_DEFAULT = 0;
	 
	 private static final int MAX_CONSUMED_ITEMS_PER_DAY = 3;
	 
	 private static final long WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000L;
	 // - Date helpers
	    
	private static boolean LOG = true;
}
