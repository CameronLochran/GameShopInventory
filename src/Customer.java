
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;



public class Customer {

    /**
     * Customer fields
     */
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
    private static final List<Customer> customerList = new ArrayList<>();

    // === Discount policy ===
    private static final double NEXT_PURCHASE_DISCOUNT_RATE = 0.10;      // 10%
    private static final Period NEXT_PURCHASE_DISCOUNT_VALIDITY = Period.ofDays(30);

    /**
     * Customer constructor
     * @param name
     * @param address
     * @param gamesBought
     * @param gamesTradedIn
     * @param customerID
     */
    // === Constructor ===
    public Customer(String name, String address, List<String> gamesBought, List<String> gamesTradedIn, int customerID) {
        this.name = name;
        this.address = address;
        this.gamesBought = (gamesBought != null) ? gamesBought : new ArrayList<>();
        this.gamesTradedIn = (gamesTradedIn != null) ? gamesTradedIn : new ArrayList<>();
        this.customerID = customerID;
    }

    /**
     * Method to buy a game from inventory
     * @param game
     * @param inventory
     */
    public void buyGame(Game game, Inventory inventory) {
        if (game == null) {
            System.out.println("Game not found in inventory.");
            return;
        }

        if (game.getQuantity() <= 0) {
            System.out.println("Out of stock: " + game.getNameOfGame());
            return;
        }

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

    /**
     * Method to trade in a game
     * @param gameName
     */
    public void tradeInGame(String gameName) {
        gamesTradedIn.add(gameName);
        grantNextPurchaseDiscount();
        System.out.println("Trade-in received: " + gameName + ". 10% discount granted on your next purchase (valid for "
                + NEXT_PURCHASE_DISCOUNT_VALIDITY.getDays() + " days).");
    }


    /**
     * Method to check if the discount is active
     * @return boolean
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
     * Method to grant the next purchase discount
     */
    private void grantNextPurchaseDiscount() {
        this.hasNextPurchaseDiscount = true;
        this.discountExpiresOn = LocalDate.now().plus(NEXT_PURCHASE_DISCOUNT_VALIDITY);
    }

    /**
     * Method to clear the next purchase discount
     */
    private void clearNextPurchaseDiscount() {
        this.hasNextPurchaseDiscount = false;
        this.discountExpiresOn = null;
    }

    /**
     * Helper methods for rounding and formatting
     * @param v
     * @return
     */
    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    private static String fmt(double v) { return String.format("%.2f", v); }

//    // === Getters ===
//    public String getName() { return name; }
//    public String getAddress() { return address; }
//    public List<String> getGamesBought() { return gamesBought; }
//    public List<String> getGamesTradedIn() { return gamesTradedIn; }
//    public int getCustomerID() { return customerID; }

    /**
     * toString method
     * @return String representation of the customer
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
}
