package application;

public class SessionManager {
	private static SessionManager instance;

    private int userId = -1;
    private String username = null;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        
        return instance;
    }

    public void setUserSession(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void clearSession() {
        userId = -1;
        username = null;
        
        instance = null;
    }
}
