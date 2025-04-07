package Model;

public class Users {
    private int id;
    private String username;
    private String password;
    private boolean admin; //to see if the user is admin
    private int latitude;//the current latitude for the user
    private int longitude;//the current longitude for the user

    public Users(int id, String username, String password, boolean admin, int latitude, int longitude) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return admin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    public int getLatitude() {
        return latitude;
    }
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }
    public int getLongitude() {
        return longitude;
    }
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
}

