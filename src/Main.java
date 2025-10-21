import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Inventory inventory = new Inventory(" ", 0, 0.0, " ", 0, 0); // shared inventory

        Game game1 = new Game(1234, "Shooting Stars", 2005, 33.0, "Switch", 3);
        Game game2 = new Game(1235, "Mystic Quest", 2012, 45.0, "PlayStation", 5);
        Game game3 = new Game(1236, "Pixel Racer X", 2018, 39.99, "Xbox", 4);
        Game game4 = new Game(1237, "Kingdom Clash", 2020, 59.99, "Switch", 6);
        Game game5 = new Game(1238, "Sky Fortress", 2015, 29.99, "PC", 2);
        Game game6 = new Game(1239, "Ocean Odyssey", 2011, 19.99, "Wii", 5);
        Game game7 = new Game(1240, "Neon Nights", 2022, 54.99, "PlayStation", 7);
        Game game8 = new Game(1241, "Shadow Agents", 2019, 49.99, "Xbox", 3);
        Game game9 = new Game(1242, "Retro Rally", 1999, 14.99, "PC", 8);
        Game game10 = new Game(1243, "Chrono Heroes", 2016, 39.50, "Switch", 6);

        Inventory.games.add(game1);
        Inventory.games.add(game2);
        Inventory.games.add(game3);
        Inventory.games.add(game4);
        Inventory.games.add(game5);
        Inventory.games.add(game6);
        Inventory.games.add(game7);
        Inventory.games.add(game8);
        Inventory.games.add(game9);
        Inventory.games.add(game10);

        decision(input, inventory);
    }

    static void CustomerMenu(Scanner input, Inventory inventory) {
        ArrayList<String> gamesBought = new ArrayList<>();
        ArrayList<String> gamesTradedIn = new ArrayList<>();



        input.nextLine(); // clear newline
        System.out.print("Enter your name: ");
        String name = input.nextLine();

        System.out.print("Enter your address: ");
        String address = input.nextLine();

        System.out.print("Enter your customer ID: ");
        int customerID = input.nextInt();

        System.out.println("Do you want to buy or trade in a game? (1-Buy, 2-Trade In, 3-Neither)");
        int action = input.nextInt();

        Customer customer = new Customer(name, address, gamesBought, gamesTradedIn, customerID);

        if (action == 1) {
            System.out.print("Enter game ID to buy: ");
            int gameId = input.nextInt();
            Game buyGame = inventory.findById(gameId);
            if (buyGame != null) {
                customer.buyGame(buyGame, inventory);
            } else {
                System.out.println("Game not found.");
            }
        } else if (action == 2) {
            input.nextLine(); // consume newline
            customer.tradeInGame(inventory);
        } else {
            System.out.println("No action selected.");
        }

        System.out.println("Customer created: " + customer);
        System.out.println("Returning to main menu...\n");
        decision(input, inventory); // go back to main menu
    }

    static void ManagerMenu(Scanner input, Inventory inventory) {
        int option;

        do {
            System.out.println("\n=== MANAGER MENU === \n1) Order a game \n2) Sell a game  \n3) View all games in stock \n4) Return to main menu");
            System.out.print("Enter option: ");

            option = input.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter quantity: ");
                    int quantity = input.nextInt();
                    inventory.checkStockLimit(quantity);

                    System.out.print("Enter game name: ");
                    input.nextLine(); // consume newline
                    String name = input.nextLine();

                    System.out.print("Enter release year: ");
                    int year = input.nextInt();
                    input.nextLine(); // consume newline

                    System.out.print("Enter console type: ");
                    String console = input.nextLine();

                    System.out.println("Enter a price for the game: ");
                    double price = input.nextDouble();

                    System.out.print("Enter game ID: ");
                    int gameId = input.nextInt();

                    inventory.addStock(new Game(gameId, name, year, price, console, quantity));
                    System.out.println("Game added successfully!");
                    break;

                case 2:
                    System.out.print("Enter game ID to sell: ");
                    int gameIdToSell = input.nextInt();
                    boolean removed = inventory.removeStock(gameIdToSell);
                    if (removed) {
                        System.out.println("Game with ID " + gameIdToSell + " sold.");
                    } else {
                        System.out.println("Game with ID " + gameIdToSell + " not found.");
                    }
                    break;

                case 3:
                    System.out.println("\n====Current Inventory====");
                    System.out.println(inventory.getAllGames());
                    break;

                case 4:
                    System.out.println("Returning to main menu...\n");
                    decision(input, inventory);
                    return; // break out of manager loop

                default:
                    System.out.println("Invalid option. Try again.");
            }

        } while (option != 4);
    }

    static void decision(Scanner input, Inventory inventory) {
        int userType;

        do {
            System.out.println("=== MAIN MENU ===");
            System.out.println("1) Manager");
            System.out.println("2) Customer");
            System.out.println("3) Quit");
            System.out.print("Enter option: ");

            userType = input.nextInt();

            switch (userType) {
                case 1:
                    ManagerMenu(input, inventory);
                    break;
                case 2:
                    CustomerMenu(input, inventory);
                    break;
                case 3:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (userType != 3);
    }

}
