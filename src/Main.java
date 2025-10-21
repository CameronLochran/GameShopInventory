import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Who is using the program? \n 1) Manager \n 2) Customer");
        Scanner userInput = new Scanner(System.in);
        int userType = userInput.nextInt();

        /**
         * Creating an Inventory object to manage the stock of games.
         * The parameters are placeholders since the Inventory class extends Game.
         */
        if (userType == 1) {

            Inventory inventory = new Inventory(" ", 0, 0.0, " ", 0);


            Scanner input = new Scanner(System.in);

            System.out.println("Enter one of the options below: \n 1) Order a game \n 2) Sell a game \n 3) View all games in stock \n 4) Quit");
            int option = input.nextInt();

            /**
             * Loop to continuously prompt the user for options until they choose to exit (option 4).
             * Handles adding, removing, and viewing games in the inventory.
             * Includes input validation and error handling for quantities exceeding 10.
             * Re-prompts the user after each action to ensure continuous interaction.
             * @param option
             */

            while (option != 4) {
                if (option == 1) {
                    System.out.println("Enter quantity: ");
                    int quantity = input.nextInt();
                    inventory.checkQuantity(quantity);

                    System.out.print("Enter game name: ");
                    input.nextLine(); // consume leftover newline
                    String name = input.nextLine();

                    System.out.print("Enter release year: ");
                    int year = input.nextInt();
                    input.nextLine(); // consume newline

                    System.out.print("Enter console type: ");
                    String console = input.nextLine();

                    System.out.print("Enter game ID: ");
                    int gameId = input.nextInt();

                    inventory.addStock(new Game(gameId, name, year, console, quantity));


                } else if (option == 2) {
                    System.out.print("Enter game ID to remove: ");
                    int gameIdToRemove = input.nextInt();
                    boolean removed = inventory.removeStock(gameIdToRemove);
                    if (removed) {
                        System.out.println("Game removed successfully.");
                    } else {
                        System.out.println("Game not found.");
                    }

                } else if (option == 3) {
                    inventory.getAllGames();

                } else {
                    System.out.println("Invalid option. Please try again.");
                }

                // Re-prompt the user after handling the option
                System.out.print("\nEnter an option (1-Add, 2-Remove, 3-View, 4-Exit): ");
                option = input.nextInt();
            }

        }else CustomerMenu(userInput);
    }

    static void CustomerMenu(Scanner input) {

        Customer customer = new Customer("", "", false, null, null, -1);

        ArrayList<String> gamesBought = new ArrayList<>();
        ArrayList<String> gamesTradedIn = new ArrayList<>();

        System.out.print("Enter your name: ");
        input.nextLine(); // consume leftover newline
        String name = input.nextLine();

        System.out.print("Enter your address: ");
        String address = input.nextLine();

        System.out.print("Do you have a membership? (true/false): ");
        boolean hasMember = input.nextBoolean();

        System.out.print("Enter your customer ID: ");
        int customerID = input.nextInt();

        System.out.println("Do you want to buy or trade in a game? (1-Buy, 2-Trade In, 3-Neither)");
        int action = input.nextInt();

        Inventory inventory = new Inventory(" ", 0, " ", -1, 0);
        if (action == 1) {
            Game buyGame = inventory.findById();
            customer.buyGame(buyGame, inventory); // Call the buyGame method
        } else if (action == 2) {
            System.out.print("Enter the name of the game to trade in: ");
            input.nextLine(); // consume leftover newline
            String gameName = input.nextLine();
            customer.tradeInGame(gameName);
        }

        System.out.println("Customer created: " + customer);
    }
    
}
