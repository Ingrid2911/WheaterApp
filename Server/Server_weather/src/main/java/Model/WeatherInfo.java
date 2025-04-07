package Model;

public class WeatherInfo {
    private int longitude;
    private int latitude;
    private String date;
    private String state; //ex: sunny, cloudy, rainy etc.
    private double temperature;
    private String stateDay1;
    private double temperatureDay1;
    private String stateDay2;
    private double temperatureDay2;
    private String stateDay3;
    private double temperatureDay3;

    public WeatherInfo(int longitude, int latitude, String date, String state, double temperature, String stateDay1, double temperatureDay1, String stateDay2, double temperatureDay2, String stateDay3, double temperatureDay3) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.state = state;
        this.temperature = temperature;
        this.stateDay1 = stateDay1;
        this.temperatureDay1 = temperatureDay1;
        this.stateDay2 = stateDay2;
        this.temperatureDay2 = temperatureDay2;
        this.stateDay3 = stateDay3;
        this.temperatureDay3 = temperatureDay3;
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
    public String getStateDay1() {
        return stateDay1;
    }
    public double getTemperatureDay1() {
        return temperatureDay1;
    }
    public String getStateDay2() {
        return stateDay2;
    }
    public double getTemperatureDay2() {
        return temperatureDay2;
    }
    public String getStateDay3() {
        return stateDay3;
    }
    public double getTemperatureDay3() {
        return temperatureDay3;
    }
}
