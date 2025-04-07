package Client;

import java.io.*;
import java.net.*;

public class Requests {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static String getWeatherInformations(String username) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("GET /weather?username="+username+ " HTTP/1.1");
            out.println();
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error connecting to server";
        }
    }



    public static void provisionWeatherData(String filePath) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Trimiteți calea către fișierul JSON
            out.println("POST /weatherInformations HTTP/1.1");
            out.println("Content-Type: application/json");
            out.println();
            out.println(filePath);  // Trimiteți calea către fișierul JSON (sau conținutul fișierului JSON dacă este necesar)

            String response = in.readLine();
            System.out.println("Provision weather data response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String ChangeUserLocation(String username, int latitude, int longitude) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Crearea cererii POST cu parametrii necesari
            out.println("POST /changeLocation HTTP/1.1");
            out.println("Content-Type: application/x-www-form-urlencoded");
            String body = "username=" + username + "&latitude=" + latitude + "&longitude=" + longitude;
            out.println("Content-Length: " + body.length());
            out.println(); // Linie goală pentru semnalizarea sfârșitului header-ului
            out.println(body); // Trimitem corpul cererii

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            System.out.println("Location updated successfully");
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error connecting to server";
        }
        }
    }


