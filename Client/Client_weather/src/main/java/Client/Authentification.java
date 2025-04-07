package Client;

import java.io.*;
import java.net.*;

public class Authentification {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    // Modificată semnătura pentru a include rolul
    public void signUp(String username, String password, boolean isAdmin, int latitude, int longitude) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {


            //Creăm obiectul JSON cu username, password și rolul isAdmin
            String jsonBody = "{\n" +
                    "  \"username\": \"" + username + "\",\n" +
                    "  \"password\": \"" + password + "\",\n" +
                    "  \"isAdmin\": " + isAdmin + ",\n" +
                    "  \"latitude\": " + latitude + ",\n" +
                    "  \"longitude\": " + longitude + "\n" +
                    "}";
            System.out.println("Sign-up request JSON: " + jsonBody); // Log the request body

            if(socket.isConnected()) {
                // Trimitem cererea POST către server
                out.println("POST /signup HTTP/1.1");
                out.println("Content-Type: application/json");
                out.println(); // blank line between headers and body
                out.println(jsonBody);
                out.flush();
                System.out.println("The request has been sent to the server.");
            }else System.out.println("The conexion is closed");


            // Citim și logăm răspunsul de la server
            String response = in.readLine();
            if (response != null && !response.isEmpty()) {
                System.out.println("The response: " + response);
                if (response.contains("201 Created")) {
                    System.out.println("Sign-up successful! You can now log in.");
                } else {
                    System.out.println("Sign-up failed: User already exists.");
                }
            } else {
                System.out.println("The response is empty.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Funcția de login a fost lăsată neschimbată, pentru că deja era funcțională
    public boolean login(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String jsonBody = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
            System.out.println("Login request JSON: " + jsonBody); // Log the request body

            out.println("POST /login HTTP/1.1");
            out.println("Content-Type: application/json");
            out.println(); // Blank line
            out.println(jsonBody);

            String response = in.readLine();
            //System.out.println("Login response: " + response); // Log the response
            if (response.contains("200 OK")) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Login failed: " + response);
                System.out.println("You need to register first! The username is not registered.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

