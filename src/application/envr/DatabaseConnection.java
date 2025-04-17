package application.envr;

public class DatabaseConnection {
	public static String DB_HOST = "localhost";
	public static String DB_NAME = "coffeestock";
	public static String DB_PORT = "3307";
	public static String DB_USER = "root";
	public static String DB_PASS = "212243";
	
	public static String DB_CONN_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
}

