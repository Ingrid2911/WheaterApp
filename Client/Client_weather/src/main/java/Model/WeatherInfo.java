package Model;

public class WeatherInfo {
    private int longitude;
    private int latitude;
    private String date;
    private String state; //ex: sunny, cloudy, rainy etc.
    private double temperature;

    public WeatherInfo(int longitude, int latitude, String date, String state, double temperature) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.state = state;
        this.temperature = temperature;
    }
    //getters
    public int getLongitude() {
        return longitude;
    }
    public int getLatitude() {
        return latitude;
    }
    public String getDate() {
        return date;
    }
    public String getState() {
        return state;
    }
    public double getTemperature() {
        return temperature;
    }
}

