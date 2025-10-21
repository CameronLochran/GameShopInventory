import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String name;
    private String address;
    private double balance;
    private boolean hasMember;
    private List<String> gamesBought;
    private List<String> gamesTradedIn;
    private int customerID;

    // === Next-purchase discount state ===
    private boolean hasNextPurchaseDiscount = false;   // flipped on trade-in, consumed on next buy
    private LocalDate discountExpiresOn = null;        // optional expiry (set on trade-in)

    // Policy knobs
    private static final double NEXT_PURCHASE_DISCOUNT_RATE = 0.10;      // 10%
    private static final Period NEXT_PURCHASE_DISCOUNT_VALIDITY = Period.ofDays(30); // expires in 30 days

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", hasMember=" + hasMember +
                ", gamesBought=" + gamesBought +
                ", gamesTradedIn=" + gamesTradedIn +
                ", customerID=" + customerID +
                ", nextPurchaseDiscountActive=" + isDiscountActive() +
                (discountExpiresOn != null ? (", discountExpiresOn=" + discountExpiresOn) : "") +
                '}';
    }

    public Customer(String name, String address, boolean hasMember, List<String> gamesBought, List<String> gamesTradedIn, int customerID) {
        this.name = name;
        this.address = address;
        this.hasMember = hasMember;
        this.gamesBought = (gamesBought != null) ? gamesBought : new ArrayList<>();
        this.gamesTradedIn = (gamesTradedIn != null) ? gamesTradedIn : new ArrayList<>();
        this.customerID = customerID;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public boolean isHasMember() { return hasMember; }
    public List<String> getGamesBought() { return gamesBought; }
    public List<String> getGamesTradedIn() { return gamesTradedIn; }
    public int getCustomerID() { return customerID; }

    // Optional helper if you need to top up balance elsewhere
    public void addFunds(double amount) {
        if (amount > 0) balance += amount;
    }

    public void buyGame(Game game, Inventory inventory) {
        if (game == null) {
            System.out.println("Game not found in inventory.");
            return;
        }

        // Check stock before pricing
        if (game.getQuantity() <= 0) {
            System.out.println("Out of stock: " + game.getNameOfGame());
            return;
        }

        // Compute effective price with next-purchase discount (if active)
        boolean discountApplied = isDiscountActive();
        double listPrice = game.getPrice();
        double effectivePrice = discountApplied ? round2(listPrice * (1.0 - NEXT_PURCHASE_DISCOUNT_RATE)) : listPrice;

        // Check funds against the effective price
        if (balance < effectivePrice) {
            System.out.println("Insufficient balance. Needed £" + fmt(effectivePrice) + ", have £" + fmt(balance));
            return;
        }

        // Deduct funds and book the purchase
        balance -= effectivePrice;
        gamesBought.add(game.getNameOfGame());

        // Reduce inventory by 1
        Game invGame = inventory.findById(game.getGameId()); // keep your existing API contract
        if (invGame == null) {
            // Defensive fallback; shouldn’t happen if `game` came from the inventory
            System.out.println("Internal warning: game missing from inventory.");
            return;
        }
        inventory.removeStock(invGame.getGameId());

        // Consume the one-time discount if it was applied
        if (discountApplied) {
            clearNextPurchaseDiscount();
            System.out.println("10% next-purchase discount applied: -£" + fmt(round2(listPrice - effectivePrice)));
        } else {
            System.out.println("No discount applied.");
        }

        System.out.println("Purchased: " + game.getNameOfGame() + " for £" + fmt(effectivePrice) + ". Remaining balance: £" + fmt(balance));
    }

    // === Trade-in: grants the next-purchase discount ===
    public void tradeInGame(String gameName) {
        gamesTradedIn.add(gameName);
        grantNextPurchaseDiscount();
        System.out.println("Trade-in received: " + gameName + ". 10% discount granted on your next purchase (valid for "
                + NEXT_PURCHASE_DISCOUNT_VALIDITY.getDays() + " days).");
    }

    public int getTotalGamesBought() {
        return gamesBought.size();
    }


    /** Returns true if the next-purchase discount is currently usable. */
    private boolean isDiscountActive() {
        if (!hasNextPurchaseDiscount) return false;
        if (discountExpiresOn == null) return true; // no expiry set
        if (LocalDate.now().isAfter(discountExpiresOn)) {
            // expired -> auto-clear
            clearNextPurchaseDiscount();
            return false;
        }
        return true;
    }

    /** Called when a trade-in occurs. */
    private void grantNextPurchaseDiscount() {
        this.hasNextPurchaseDiscount = true;
        this.discountExpiresOn = LocalDate.now().plus(NEXT_PURCHASE_DISCOUNT_VALIDITY);
    }

    /** Called after the discounted purchase is completed, or when the discount expires. */
    private void clearNextPurchaseDiscount() {
        this.hasNextPurchaseDiscount = false;
        this.discountExpiresOn = null;
    }

    // --- Small utilities -----------------------------------------------------

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private static String fmt(double v) {
        return String.format("%.2f", v);
    }
}
