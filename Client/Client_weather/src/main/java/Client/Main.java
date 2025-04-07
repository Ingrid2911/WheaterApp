package Client;

public class Main {
    public static void main(String[] args) {
        // Instanțiază și pornește aplicația client
        ClientLogic weatherClient = new ClientLogic();
        weatherClient.start(); // Metodă dedicată pentru pornirea aplicației
    }
}