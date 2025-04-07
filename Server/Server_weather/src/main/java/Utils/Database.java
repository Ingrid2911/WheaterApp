package Utils;

import Model.WeatherInfo;
import Model.User;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:weather_info.db";

    public Database() {
        createDatabase();
    }

    private void createDatabase() {
        try (
                Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement() ) {

//          String deleteTable ="DROP TABLE IF EXISTS Users;\n";
//            stmt.execute(deleteTable);
            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "is_admin BOOLEAN NOT NULL," +
                    "latitude INTEGER," +
                    "longitude INTEGER)";
            stmt.execute(createUsersTable);

//            String dropWeatherTable = "DROP TABLE IF EXISTS WeatherData";
//            stmt.execute(dropWeatherTable);
            //System.out.println("WeatherData table dropped.");
            String createWeatherTable = "CREATE TABLE IF NOT EXISTS WeatherData (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "latitude INTEGER, " +
                    "longitude INTEGER, " +
                    "date TEXT, " +
                    "state TEXT, " +
                    "temperature REAL, " +
                    "stateDay1 TEXT, " +
                    "temperatureDay1 REAL, " +
                    "stateDay2 TEXT, " +
                    "temperatureDay2 REAL, " +
                    "stateDay3 TEXT, " +
                    "temperatureDay3 REAL)";
            stmt.execute(createWeatherTable);

        } catch (SQLException e) {
            e.printStackTrace(); // as putea folosi o biblioteca de logare
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, is_admin, latitude, longitude FROM Users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {
            prepStatement.setString(1, username);//set the parameter "username"
            ResultSet result = prepStatement.executeQuery();//execute the query

            if (result.next()) {
                return new User(
                        result.getInt("id"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getBoolean("is_admin"),
                        result.getInt("latitude"),
                        result.getInt("longitude")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // as putea folosi o biblioteca de logare
        }
        return null; // Dacă utilizatorul nu este găsit
    }

    public void insertWeatherCondition(double lat, double lon, String date, String state, double temp, String stateDay1, double temperatureDay1, String stateDay2, double temperatureDay2,  String stateDay3, double temperatureDay3) {
        String sql = "INSERT INTO WeatherData (latitude, longitude, date, state, temperature, stateDay1, temperatureDay1,stateDay2, temperatureDay2, stateDay3, temperatureDay3 ) VALUES (?, ?, ?, ?, ?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (int) lat);  // Convert latitude to integer
            pstmt.setInt(2, (int) lon);  // Convert longitude to integer
            pstmt.setString(3, date);    // Date remains a string
            pstmt.setString(4, state);   // Weather state remains a string
            pstmt.setDouble(5, temp);    // Temperature remains a double
            pstmt.setString(6, stateDay1);
            pstmt.setDouble(7, temperatureDay1);
            pstmt.setString(8, stateDay2);
            pstmt.setDouble(9, temperatureDay2);
            pstmt.setString(10, stateDay3);
            pstmt.setDouble(11, temperatureDay3);

            pstmt.executeUpdate(); // Execute the insertion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadWeatherDataFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            // Inițializarea unui obiect Gson
            Gson gson = new Gson();
            // Definirea tipului așteptat (listă de WeatherCondition)
            Type type = new TypeToken<List<WeatherInfo>>() {}.getType();
            // Citirea fișierului JSON și parsarea acestuia într-o listă de obiecte
            List<WeatherInfo> weatherData = gson.fromJson(reader, type);

            // Iterează prin lista de obiecte WeatherCondition și le adaugă în baza de date
            for (WeatherInfo weather : weatherData) {
                System.out.println("Inserting weather data: " + weather.getLatitude() + ", " + weather.getLongitude() + ", " + weather.getDate() + ", " + weather.getState() + ", " + weather.getTemperature());
                insertWeatherCondition(weather.getLatitude(), weather.getLongitude(),
                        weather.getDate(), weather.getState(), weather.getTemperature(),
                        weather.getStateDay1(),weather.getTemperatureDay1(),
                        weather.getStateDay2(),weather.getTemperatureDay2(),
                        weather.getStateDay3(),weather.getTemperatureDay3());
            }

            System.out.println("Weather data successfully loaded from JSON.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading weather data from JSON.");
        }
    }

    public void insertUser(String username, String password, boolean isAdmin, int latitude, int longitude) {
        String sql = "INSERT INTO Users (username, password, is_admin, latitude, longitude) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Log the input values for debugging
            System.out.println("Attempting to insert user: " + username);

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setBoolean(3, isAdmin);
            pstmt.setInt(4,latitude);
            pstmt.setInt(5,longitude);

            pstmt.executeUpdate();
            System.out.println("User added successfully: " + username);
            System.out.println("isAdmin in database: " + isAdmin);

        } catch (SQLException e) {
            // Check if the error is due to a UNIQUE constraint violation
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("Error: Username '" + username + "' already exists.");
                throw new RuntimeException("User already exists: " + username, e);
            }

            // Log unexpected errors
            e.printStackTrace();
            throw new RuntimeException("Failed to insert user: " + username, e);
        }
    }
    public boolean validateUser(String username, String password) {
        String sql = "SELECT password FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);  // Make sure the password check is working
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any database-related issues
        }
        return false;
    }
    public String getWeatherForUser(String username) {
        // SQL pentru a obține latitudinea și longitudinea utilizatorului
        String userLocationQuery = "SELECT latitude, longitude FROM Users WHERE username = ?";
        System.out.println("UserLocationQuery: " + userLocationQuery);
        // SQL pentru a obține informațiile despre vreme pe baza locației
        String weatherQuery = "SELECT date, state, temperature, stateDay1, temperatureDay1, stateDay2, temperatureDay2, stateDay3, temperatureDay3 FROM WeatherData WHERE latitude = ? AND longitude = ?";
        System.out.println("WeatherQuery: " + weatherQuery);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Prima interogare: obținerea locației utilizatorului
            try (PreparedStatement userStmt = conn.prepareStatement(userLocationQuery)) {
                userStmt.setString(1, username);
                System.out.println("UserStatement: " + userStmt);
                ResultSet userResult = userStmt.executeQuery();
                //System.out.println("rezultatul interogarii este "+userResult.next());

                if (userResult.next()) {
                    // Obține latitudinea și longitudinea utilizatorului
                    int latitude = userResult.getInt("latitude");
                    int longitude = userResult.getInt("longitude");

                    // A doua interogare: obținerea datelor despre vreme pentru locația respectivă
                    try (PreparedStatement weatherStmt = conn.prepareStatement(weatherQuery)) {
                        weatherStmt.setInt(1, latitude);
                        weatherStmt.setInt(2, longitude);
                        ResultSet weatherResult = weatherStmt.executeQuery();

                        StringBuilder weatherInfo = new StringBuilder();

                        // Parcurge rezultatele și construiește informația despre vreme
                        while (weatherResult.next()) {
                            String date = weatherResult.getString("date");
                            String state = weatherResult.getString("state");
                            double temperature = weatherResult.getDouble("temperature");

                            String stateDay1 = weatherResult.getString("stateDay1");
                            String temperatureDay1 = weatherResult.getString("temperatureDay1");
                            String stateDay2 = weatherResult.getString("stateDay2");
                            String temperatureDay2 = weatherResult.getString("temperatureDay2");
                            String stateDay3 = weatherResult.getString("stateDay3");
                            String temperatureDay3 = weatherResult.getString("temperatureDay3");
                            weatherInfo.append("Date: ").append(date)
                                    .append(", State: ").append(state)
                                    .append(", Temperature: ").append(temperature).append("°C\n")
                                    .append(" StateDay1: ").append(stateDay1)
                                    .append(", TemperatureDay1: ").append(temperatureDay1).append("°C\n")
                                    .append(" StateDay2: ").append(stateDay2)
                                    .append(", TemperatureDay2: ").append(temperatureDay2).append("°C\n")
                                    .append(" StateDay3: ").append(stateDay3)
                                    .append(", TemperatureDay3: ").append(temperatureDay3).append("°C");
                        }

                        // Dacă există informații despre vreme, returnează-le
                        if (weatherInfo.length() > 0) {
                            return weatherInfo.toString();
                        } else {
                            return "No weather data found for your location.";
                        }
                    }
                } else {
                    return "User not found or location not set.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while retrieving weather data.";
        }
    }
    public boolean updateUserLocation(String username, int newLatitude, int newLongitude) {
        // SQL pentru actualizarea locației utilizatorului
        String sql = "UPDATE Users SET latitude = ?, longitude = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setarea noilor valori pentru latitudine, longitudine și username
            pstmt.setInt(1, newLatitude);
            pstmt.setInt(2, newLongitude);
            pstmt.setString(3, username);

            // Executarea interogării
            int rowsAffected = pstmt.executeUpdate();

            // Verifică dacă modificarea a avut succes
            if (rowsAffected > 0) {
                System.out.println("Location updated successfully for user: " + username);
                return true;
            } else {
                System.out.println("No user found with username: " + username);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while updating location for user: " + username);
            return false;
        }
    }



}
