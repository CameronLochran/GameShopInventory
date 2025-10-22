/**
 * Represents a video game with its details.
 */
public class Game {

    /**
     * Game fields
     */

    private String nameOfGame;
    private int releaseYear;
    private double price;
    private String consoleType;
    private int quantity;
    private int gameId;

    /**
     * Constructor to initialize a Game object with its details.
     * @param gameId Unique identifier for the game
     * @param nameOfGame Name of the game
     * @param releaseYear Year the game was released
     * @param price Price of the game
     * @param consoleType Type of console the game is for
     * @param quantity Quantity of the game in stock
     */
    public Game(int gameId, String nameOfGame, int releaseYear, double price, String consoleType, int quantity) {
        this.gameId = gameId;
        this.nameOfGame = nameOfGame;
        this.releaseYear = releaseYear;
        this.price = price;
        this.consoleType = consoleType;
        this.quantity = quantity;
    }

    /**
     * Returns a string representation of the Game object.
     * @return
     */
    @Override
    public String toString() {
        return "Game{" +
                "quantity=" + quantity +
                ", nameOfGame='" + nameOfGame + '\'' +
                ", releaseYear=" + releaseYear +
                ", price=" + price +
                ", consoleType='" + consoleType + '\'' +
                ", gameId=" + gameId +
                '}';
    }

    /**
     * Gets the name of the game.
     * @return the name of the game
     */
    public String getNameOfGame() {
        return nameOfGame;
    }

    /**
     * Gets the release year of the game.
     * @return the release year of the game
     */

    public String getConsoleType() {
        return consoleType;
    }

    /**
     * Gets the release year of the game.
     * @return the release year of the game
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Gets the price of the game.
     * @return the price of the game
     */

    public double getPrice() {
        return price;
    }

    /**
     * Gets the quantity of the game in stock.
     * @return the quantity of the game
     */

    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the unique identifier of the game.
     * @return the game ID
     */

    public int getGameId() {
        return gameId;
    }

}
