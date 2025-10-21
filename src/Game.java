public class Game {

    private String nameOfGame;
    private int releaseYear;
    private double price;
    private String consoleType;
    private int quantity;
    private static int gameId;

    public Game(int gameId, String nameOfGame, int releaseYear, String consoleType, int quantity) {
        this.gameId = gameId;
        this.nameOfGame = nameOfGame;
        this.releaseYear = releaseYear;
        this.price = this.price;
        this.consoleType = consoleType;
        this.quantity = quantity;

    }

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

    public String getNameOfGame() {
        return nameOfGame;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getConsoleType() {
        return consoleType;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public static int getGameId() {
        return gameId;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setNameOfGame(String nameOfGame) {
        this.nameOfGame = nameOfGame;
    }

}
