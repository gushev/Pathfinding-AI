import java.util.Random;

public class Board {

    private char[][] board;
    private int obstacleCount;
    private int sizeY;
    private int sizeX;
    private int[] startingPosition;
    private int[] finishPosition;

    public Board(int sizeY, int sizeX, int obstacleCount) {
        this.board = new char [sizeY] [sizeX];
        this.obstacleCount = obstacleCount;
        this.sizeY = sizeY;
        this.sizeX = sizeX;
        this.fillBoard();
    }

    private void fillBoard() {

        for (int i=0; i < this.board.length; i++) {
            for (int j=0; j < this.board[i].length; j++) {
                this.board[i][j] = '.';
            }
        }

        this.placeObjects('X', this.obstacleCount); // Place Obstacles
        this.startingPosition = this.placeObjects('P', 1); // Place Player
        this.finishPosition= this.placeObjects('E', 1); // Place Finish

        // Activating the code below will allow you to place objects at custom locations
        /*this.placeObject(4, 5, 'X');
        this.placeObject(3, 5, 'X');
        this.placeObject(2, 6, 'X');
        this.startingPosition = this.placeObject(4, 0, 'P');
        this.finishPosition = this.placeObject(4, 6, 'E');*/
    }

    public int getObstacleCount() {
        return this.obstacleCount;
    }

    public int[] placeObjects(char objectSign, int count) {

        int currentNumberOfObjects = 0;
        int [] randomCell = getRandomCell();

        while (currentNumberOfObjects < count) {

            if (this.board[randomCell[0]][randomCell[1]] == '.') {
                this.board[randomCell[0]][randomCell[1]] = objectSign;
                currentNumberOfObjects += 1;
            } else {
                randomCell = getRandomCell();
            }
        }

        return randomCell;
    }

    public int[] placeObject(int y, int x, char objectSign) {
        this.board[y][x] = objectSign;
        return new int[] {y, x};
    }

    private int[] getRandomCell() {
        Random rand = new Random();
        return new int[] {
            rand.nextInt((this.sizeY - 1) + 1),
            rand.nextInt((this.sizeX - 1) + 1)
        };
    }

    public void displayBoard() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Legend: P (Player), E (End), X (Obstacle), O (Visited Location)");
    }

    public void markLocation(int[] location, char mark) {
        this.board[location[0]][location[1]] = mark;
    }

    public char[][] getBoard() {
        return board;
    }

    public int[] getStartingPosition() {
        return startingPosition;
    }

    public int[] getFinishPosition() {
        return finishPosition;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public int getSizeX() {
        return this.sizeX;
    }
}
