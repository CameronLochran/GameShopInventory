import java.util.ArrayList;

public class Inventory extends Game {

    public static ArrayList<Game> games;
    private int maxQuantity = 10;

    /**
     * Constructor to initialize the inventory with an empty list of games.
     * @param nameOfGame
     * @param releaseYear
     * @param consoleType
     * @param gameId
     */
    public Inventory(String nameOfGame, int releaseYear, double price , String consoleType, int gameId, int quantity) {
        super(gameId, nameOfGame, releaseYear, price ,consoleType, quantity);
        games = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "games=" + games +
                '}';
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

    public boolean getAllGames(){
        if (games.isEmpty()){
            System.out.println("No games in stock");
        }else if (!games.isEmpty()){
            System.out.println("Games in stock: ");
            for (Game game: games){
                System.out.println(game);
            }
        }

        return false;
    }

    /**
     * Checks if the quantity exceeds 10 and throws an exception if it does.
     * Catches the exception, prints an error message, and exits the program.
     * @param quantity
     */

    public void checkStockLimit(int quantity) {
        try {
            if (quantity > maxQuantity) {
                throw new IllegalArgumentException("Quantity exceeds maximum limit of " + maxQuantity);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}