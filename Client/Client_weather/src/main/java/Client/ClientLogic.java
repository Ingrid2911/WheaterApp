package Client;

import java.util.Scanner;

public class ClientLogic {

    // Metodă pentru pornirea aplicației
    public void start() {
        Scanner scanner = new Scanner(System.in);
        Authentification auth = new Authentification();
        //Requests requestHandler = new Requests();

        // Autentificare utilizator
        boolean isAuthentificated = false;
        boolean isAdmin = false;
        while (!isAuthentificated) {
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumăm linia
            if (choice == 2) {
                // Inregistrează utilizatorul, se cere rolul doar aici
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                System.out.print("Latitude: ");
                Integer latitude = scanner.nextInt();
                System.out.print("Longitude: ");
                Integer longitude = scanner.nextInt();
                System.out.print("Choose role (1 for Admin, 2 for Regular User): ");
                int roleChoice = scanner.nextInt();
                isAdmin = (roleChoice == 1);
                System.out.println("Is Admin: " + isAdmin);
                auth.signUp(username, password, isAdmin,latitude,longitude );
            } else if (choice == 1) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                // Logare utilizator, fără a cere rolul
                isAuthentificated = auth.login(username, password);
            }
        }

        // Restul logicii pentru obținerea vremii
        while (true) {
            int choice = 0;
            if(isAdmin) {
                System.out.println("1. Weather forecast ");
                System.out.println("2. Upload weather forecast");
                System.out.println("3. Change user location");
                System.out.println("4. Exit");
                System.out.print("Select an option: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consumăm linia
            } else {
                System.out.println("5. Weather forecast ");
                System.out.println("6. Change user location");
                System.out.println("7. Exit");
                System.out.print("Select an option: ");
                choice = scanner.nextInt();
            }

            if (choice == 1 || choice==5) {
                System.out.println("Enter your username:");
                String username = scanner.nextLine();
                String weather = Requests.getWeatherInformations(username);
                System.out.println(weather);
            } else if (choice == 2 ) {
                System.out.print("Enter file path for the JSON weather data: ");
                String filePath = scanner.nextLine();
                Requests.provisionWeatherData(filePath);
            }else if(choice == 3 ||choice==6){
                System.out.println("Enter your username:");
                String username = scanner.nextLine();
                System.out.println("Enter new latitude: ");
                Integer newLatitude = scanner.nextInt();
                System.out.println("Enter new longitude: ");
                Integer newLongitude = scanner.nextInt();
                Requests.ChangeUserLocation(username, newLatitude, newLongitude);
            }else if (choice == 4 || choice == 7) {
                System.out.println("Goodbye!");
                break;
            }
        }
    }
}

