package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.envr.DatabaseConnection;

public class Database {
	
	static Connection getConnection() throws ClassNotFoundException {		
        Connection con = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            con = (Connection) DriverManager.getConnection(DatabaseConnection.DB_CONN_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
			
			if (con != null) {
				System.out.println("Connected to DB!");
			} else {
				System.out.println("Error connection to DB.");
			}
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Exception: " + ex.getMessage());
            
            ex.printStackTrace();
        }
        
        return con;
    }
}
