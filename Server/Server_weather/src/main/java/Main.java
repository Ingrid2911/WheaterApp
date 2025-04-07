import Routes.WeatherServer;
import Utils.Database;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        WeatherServer server = new WeatherServer();
        server.start();
    }
}
