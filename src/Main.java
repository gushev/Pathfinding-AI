public class Main {

    public static void main(String[] args) {
	    //Board board = new Board(5, 5, 3);
	    Board board = new Board(12, 18, 60);
	    Game game = new Game(board);
        game.play();
    }
}
