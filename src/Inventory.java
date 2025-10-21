import java.util.ArrayList;

public class Inventory extends Game {

    public static ArrayList<Game> games;
    private int maxQuantity = 10;

    /**
     * Constructor to initialize the inventory with an empty list of games.
     *
     * @param nameOfGame
     * @param releaseYear
     * @param consoleType
     * @param gameId
     */
    public Inventory(String nameOfGame, int releaseYear, double price , String consoleType, int gameId, int quantity) {
        super(gameId, nameOfGame, releaseYear, consoleType, quantity);
        games = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "games=" + games +
                '}';
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    /**
     * Returns a string representation of all games in the inventory.
     * @return a string listing all games
     * @param game
     */
    public void addStock(Game game){
        games.add(game);
    }

    /**
     * Finds a game in the inventory by its ID.
     * @param gameId
     * @return the game found or null if not found
     */
    public Game findById(int gameId){
        for (Game game: games)
        {
            if (game.getGameId() == gameId) return game;
        }
        return null;
    }

    /**
     * Removes a game from the inventory based on its ID.
     * @param gameId
     * @return true if the game was successfully removed, false otherwise
     */
    public boolean removeStock(int gameId) {
        Game gameToRemove = findById(gameId);
        if (gameToRemove != null) {
            games.remove(gameToRemove);
            return true; // Successfully removed
        }
        return false;
    }

    public void getAllGames(){
        if (games.isEmpty()){
            System.out.println("No games in stock");
        }else System.out.println("Games in stock: ");
        for (Game game: games)
        {
            System.out.println(game);
        }
    }

    // method for stock per game max 10
    public boolean isStockFull(Game game) {
        if (game.getQuantity() > maxQuantity) {
            System.out.println("Stock is full for this game." );
            return true;
        }
        return false;
    }

    /**
     * Checks if the quantity exceeds 10 and throws an exception if it does.
     * Catches the exception, prints an error message, and exits the program.
     * @param quantity
     */

    public void checkQuantity(int quantity) {
        try {
            if (quantity > 10) {
                throw new IllegalArgumentException("Cannot order more than 10 games at a time.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Please restart the program and enter a valid quantity.");
            System.exit(0);
        }
    }
}