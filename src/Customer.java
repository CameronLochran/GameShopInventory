import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * Customer class representing a customer in the video game store.
 */

public class Customer{


    // === Customer fields ===
    private String name;
    private String address;
    private double balance;
    private List<String> gamesBought;
    private List<String> gamesTradedIn;
    private int customerID;

    // === Discount fields ===
    private boolean hasNextPurchaseDiscount = false;
    private LocalDate discountExpiresOn = null;

    // === Static "database" of customers ===
    private final List<Customer> customerList = new ArrayList<>();

    // === Discount policy ===
    private static final double NEXT_PURCHASE_DISCOUNT_RATE = 0.10;      // 10%
    private static final Period NEXT_PURCHASE_DISCOUNT_VALIDITY = Period.ofDays(30);

    private static final double DEFAULT_BALANCE = 100.0;

    /**
     * toString method to display customer information
     * @return string representation of customer
     */
    @Override
    public String toString() {
        return "Customer{" +
                "ID=" + customerID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", gamesBought=" + gamesBought +
                ", gamesTradedIn=" + gamesTradedIn +
                ", nextPurchaseDiscountActive=" + isDiscountActive() +
                (discountExpiresOn != null ? (", expiresOn=" + discountExpiresOn) : "") +
                '}';
    }

    /**
     * Constructor to initialize a Customer object with its details.
     * @param name Name of the customer
     * @param address Address of the customer
     * @param gamesBought List of games bought by the customer
     * @param gamesTradedIn List of games traded in by the customer
     * @param customerID Unique identifier for the customer
     */

    public Customer(String name, String address, List<String> gamesBought, List<String> gamesTradedIn, int customerID) {
        this.name = name;
        this.address = address;
        this.gamesBought = (gamesBought != null) ? gamesBought : new ArrayList<>();
        this.gamesTradedIn = (gamesTradedIn != null) ? gamesTradedIn : new ArrayList<>();
        this.customerID = customerID;

        this.balance = DEFAULT_BALANCE;
    }

    /**
     * Method to buy a game, applying discount if available.
     * @param game Game object to be purchased
     * @param inventory Inventory object to manage stock
     */

    public void buyGame(Game game, Inventory inventory) {

        // Check for discount
        boolean discountApplied = isDiscountActive();
        double listPrice = game.getPrice();
        double effectivePrice = discountApplied ? round2(listPrice * (1.0 - NEXT_PURCHASE_DISCOUNT_RATE)) : listPrice;

        // Check balance
        if (balance < effectivePrice) {
            System.out.println("Insufficient balance. Needed £" + fmt(effectivePrice) + ", have £" + fmt(balance));
            return;
        }

        // Deduct balance and update records
        balance -= effectivePrice;
        gamesBought.add(game.getNameOfGame());
        Game invGame = inventory.findById(game.getGameId());
        if (invGame == null) {
            System.out.println("Internal warning: game missing from inventory.");
            return;
        }
        inventory.removeStock(invGame.getGameId());

        // Apply discount if available
        if (discountApplied) {
            clearNextPurchaseDiscount();
            System.out.println("10% next-purchase discount applied: -£" + fmt(round2(listPrice - effectivePrice)));
        } else {
            System.out.println("No discount applied.");
        }

        System.out.println("Purchased: " + game.getNameOfGame() + " for £" + fmt(effectivePrice) + ". Remaining balance: £" + fmt(balance));
    }

    /***
     * Method to trade in a game, adding funds and granting discount.
     * @param inventory Inventory object to manage stock
     */

    public void tradeInGame(Inventory inventory) {
        Scanner sc = new Scanner(System.in);
        String gameName;

        System.out.print("Enter the name of the game you want to trade in: ");
        gameName = sc.nextLine();

        System.out.print("Enter the console type (e.g., PS5, Switch, Xbox): ");
        String consoleType = sc.nextLine();

        System.out.print("Enter the release year: ");
        int releaseYear = sc.nextInt();

        System.out.print("Enter how many copies you are selling: ");
        int quantity = sc.nextInt();

        if (quantity > 2){
            System.out.print("Enter the price you want to sell them for: £");
        }else if (quantity == 1){
            System.out.print("Enter the price you want to sell it for: £");
        }

        double price = sc.nextDouble();

        System.out.println("Enter game id: ");
        int id = sc.nextInt();

        // --- Register trade-in ---
        gamesTradedIn.add(gameName);

        // Add funds to customer's balance
        balance += price;
        System.out.println("Game traded in for £" + fmt(price) + ". Your new balance: £" + fmt(balance));


        Game tradedIn = new Game(id, gameName, releaseYear, price, consoleType, quantity);
        inventory.addStock(tradedIn);

        // Grant discount after trade-in
        grantNextPurchaseDiscount();
        System.out.println("10% discount granted on your next purchase (valid for "
                + NEXT_PURCHASE_DISCOUNT_VALIDITY.getDays() + " days).");

        // Optionally let them buy a new game immediately
        sc.nextLine(); // consume newline
        System.out.print("Would you like to use your discount now to buy a game? (yes/no): ");
        String choice = sc.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            System.out.print("Enter the Game ID you want to buy: ");
            id = sc.nextInt();
            Game discountedGame = inventory.findById(id);

            if (discountedGame != null) {
                buyGame(discountedGame, inventory);
            } else {
                System.out.println("No game found with ID " + id + ".");
            }
        } else {
            System.out.println("Discount saved for later! You can use it on your next purchase before "
                    + discountExpiresOn + ".");
        }
    }


    /**
     * Checks if the next purchase discount is active and valid.
     * @return true if discount is active, false otherwise
     */

    private boolean isDiscountActive() {
        if (!hasNextPurchaseDiscount) return false;
        if (discountExpiresOn == null) return true;
        if (LocalDate.now().isAfter(discountExpiresOn)) {
            clearNextPurchaseDiscount();
            return false;
        }
        return true;
    }

    /**
     * Grants a next purchase discount to the customer.
     */

    private void grantNextPurchaseDiscount() {
        this.hasNextPurchaseDiscount = true;
        this.discountExpiresOn = LocalDate.now().plus(NEXT_PURCHASE_DISCOUNT_VALIDITY);
    }

    /**
     * Clears the next purchase discount for the customer.
     */
    private void clearNextPurchaseDiscount() {
        this.hasNextPurchaseDiscount = false;
        this.discountExpiresOn = null;
    }

    // === UTILITIES ===
    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    private static String fmt(double v) { return String.format("%.2f", v); }


//    public void returningCustomerCheck(){
//        if (customerList.size() < 2) {
//            System.out.println("Not enough customers to compare.");
//            return;
//        }
//
//        boolean duplicateFound = false;
//
//        for (int i = 0; i < customerList.size(); i++) {
//            Customer c1 = customerList.get(i);
//
//            for (int j = i + 1; j < customerList.size(); j++) {
//                Customer c2 = customerList.get(j);
//
//                if (c1.customerID == c2.customerID) {
//                    System.out.println("Duplicate ID found: " + c1.customerID);
//                    System.out.println(" → " + c1.name + " and " + c2.name);
//                    duplicateFound = true;
//                }
//            }
//        }
//
//        if (!duplicateFound) {
//            System.out.println("No duplicate customer IDs found.");
//        }
//    }

}
