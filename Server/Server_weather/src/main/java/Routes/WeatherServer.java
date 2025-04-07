package Routes;

import java.net.*;
import java.io.*;

import Utils.Database;

public class WeatherServer {
    private static final int PORT = 12345;

    // Services
    private final Authentification authService;

    public WeatherServer() {
        this.authService = new Authentification();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Sever started on port " + PORT);
            while (true) {
                Socket received = serverSocket.accept();
                ClientRequest(received);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ClientRequest(Socket received) {
        try (
                //for reading data send by the client
                BufferedReader input = new BufferedReader(new InputStreamReader(received.getInputStream()));
                //for sending responses to the client
                PrintWriter output = new PrintWriter(received.getOutputStream());
        ) {
            //used for build the HTTP request
            StringBuilder builder = new StringBuilder();
            String line;
            //read the request line by line
            while ((line = input.readLine()) != null && !line.isEmpty()) {
                //to keep the HTTP format, we add a newline
                System.out.println(line);
                builder.append(line).append("\n");
            }
            String request = builder.toString();
            System.out.println("Request received: " + request);

            //extract body only for POST requests
            String body = null;
            if (request.startsWith("POST")) {
                char[] bodyRequest = new char[1024];
                input.read(bodyRequest);
                body = new String(bodyRequest).trim();
            }

            //process the request body
            String response = processRequest(request, body);
            //send the response to the client
            output.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processRequest(String request, String body) {
        System.out.println("Request in process: " + request);
        if (request.startsWith("POST /signup")) {
            System.out.println("Sign-up request body:\n" + body); // Log the body
            return authService.signUp(body);  // Pass the body directly
        } else if (request.startsWith("POST /login")) {
            System.out.println("Login request body:\n" + body); // Log the body
            return authService.login(body);  // Pass the body directly
        } else if (request.startsWith("GET /weather")) {
            return WeatherInformationsRequest(request);
        } else if (request.startsWith("POST /weatherInformations")) {
            System.out.println("Provision weather data request body:\n" + body);
            return OfferingWeatherData(body);
        }else if(request.startsWith("POST /changeLocation")) {
            System.out.println("Change location request body:\n" + body);
            return ChangeLocation(body);
        }
        return "Unknown request";
    }

    private String OfferingWeatherData(String body) {
        //path to the file where are weather informations
        String filePath = body.trim();
        if (filePath.isEmpty()) {
            return "No file provided";
        }
        //load the informations from database
        Database db= new Database();
        db.loadWeatherDataFromJson(filePath); // Loads data from the provided file

        return "Weather informations successfully requested";
    }

    private String WeatherInformationsRequest(String request) {
        // Split the request to extract the username
        String[] parts = request.split("\\?");
        if (parts.length > 1) {
            String[] queryParts = parts[1].split("&");
            try {
                // Extrage username-ul din query
                String username = queryParts[0].split("=")[1]; // presupunem că primul parametru este username
                username = username.split(" ")[0];
                System.out.println("Parsed username: " + username);

                // Creează instanța bazei de date și obține informațiile despre vreme
                Database db = new Database();
                String weatherInfo = db.getWeatherForUser(username);

                // Returnează informațiile despre vreme
                return weatherInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return "Invalid parameters in query.";
            }
        }
        return "Invalid query format.";
    }
    private String ChangeLocation(String body) {
        try {
            // Presupunem că body-ul conține datele în format de query-string (similar unui URL query).
            String[] queryParams = body.split("&");
            String username = null;
            int latitude = 0;
            int longitude = 0;

            // Parcurgem parametrii pentru a extrage valorile
            for (String param : queryParams) {
                String[] pair = param.split("=");
                if (pair[0].equals("username")) {
                    username = pair[1];
                    System.out.println("Parsed username: " + username);
                } else if (pair[0].equals("latitude")) {
                    latitude = Integer.parseInt(pair[1]);
                    System.out.println("Parsed latitude: " + latitude);
                } else if (pair[0].equals("longitude")) {
                    longitude = Integer.parseInt(pair[1]);
                    System.out.println("Parsed longitude: " + longitude);
                }
            }

            // Verificăm dacă toate datele sunt corecte
            if (username == null || latitude == 0 || longitude == 0) {
                return "Missing parameters in the request body";
            }

            // Apelăm baza de date pentru actualizarea locației utilizatorului
            Database db = new Database();
            boolean success = db.updateUserLocation(username, latitude, longitude);

            // Returnăm un răspuns corespunzător în funcție de succesul operației
            if (success) {
                return "Location updated successfully for user: " + username;
            } else {
                return "User not found or unable to update location.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing the change location request.";
        }
    }


}
