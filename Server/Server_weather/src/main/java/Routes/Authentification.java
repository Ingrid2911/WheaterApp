package Routes;

import Model.User;
import Utils.Database;
import Utils.JsonParser;
import com.google.gson.Gson;


public class Authentification {
    private final Database dbManager;

    public Authentification() {
        this.dbManager = new Database();
    }

    public String signUp(String body) {
        try {
            if (body == null || body.isEmpty()) {
                System.out.println("Body is null or empty");
                return "HTTP/1.1 400 Bad Request\n\nNo request body provided";
            }
            System.out.println("Sign-up request body: " + body); // Log the body
            // Deserializare JSON în obiect User
            User newUser = JsonParser.fromJson(body, User.class);
            if (newUser == null) {
                return "HTTP/1.1 400 Bad Request\n\nInvalid JSON body for sign-up.";
            }
            //System.out.println("User ul deserializat este "+newUser.getIsAdmin());
            dbManager.insertUser(newUser.getUsername(), newUser.getPassword(), newUser.getIsAdmin(), newUser.getLatitude(), newUser.getLongitude());
            System.out.println("IsAdmin: " + newUser.getIsAdmin());
            return "HTTP/1.1 201 Created\n\nUser registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "HTTP/1.1 500 Internal Server Error\n\nFailed to register user";
        }
    }



    public String login(String body) {
        try {
            System.out.println("Login request body: " + body); // Log the request body

            // Deserializare JSON în obiect User
            User loginUser = JsonParser.fromJson(body, User.class);
            if (loginUser == null) {
                return "HTTP/1.1 400 Bad Request\n\nInvalid JSON body for login.";
            }

            boolean isValidUser = dbManager.validateUser(loginUser.getUsername(), loginUser.getPassword());

            if (isValidUser) {
                // Verificăm rolul utilizatorului
                User user = dbManager.getUserByUsername(loginUser.getUsername());
                if (user != null) {
                    if (user.getIsAdmin()) {
                        return "HTTP/1.1 200 OK\n\nLogin successful. You are an Admin.";
                    } else {
                        return "HTTP/1.1 200 OK\n\nLogin successful. You are a regular user.";
                    }
                }
            } else {
                System.out.println("Invalid username or password");
                return "HTTP/1.1 401 Unauthorized\n";
            }

            return "HTTP/1.1 500 Internal Server Error\n\nUnexpected error during login"; // Added this return
        } catch (Exception e) {
            e.printStackTrace();
            return "HTTP/1.1 500 Internal Server Error\n\nFailed to process login";
        }
    }

}

