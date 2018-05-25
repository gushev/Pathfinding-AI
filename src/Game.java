import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {

    private Board board;
    private int[] playerPosition;
    private int[] lastPosition;
    private int[] finishPosition;
    private ArrayList<int[]> possibleMoves;
    private Scanner userInput = new Scanner(System.in);


    public Game (Board board) {
        this.board = board;
        this.playerPosition = board.getStartingPosition();
        this.lastPosition = board.getStartingPosition();
        this.finishPosition = board.getFinishPosition();
    }

    public void play () {

        board.displayBoard();

        int iteration = 0;
        int maxIterations = board.getSizeY() * board.getSizeX() - board.getObstacleCount() / 2;
        System.out.println("Max iterations for this board: " + maxIterations);

        while (!Arrays.equals(this.playerPosition, this.finishPosition)) {

            if (iteration > maxIterations) {
                break;
            }

            // Activating the code below will allow you to see the result of each move
            // and progress step by step
            /*System.out.println("Type 'exit' if you want to quit or simply enter any other key to continue with the next step: ");
            String input = userInput.nextLine();
            if (input.equals("exit")) {
                break;
            }*/
            makeMove();

            if (this.lastPosition == this.playerPosition) {
                break;
            }

            iteration++;
        }

        if (Arrays.equals(this.playerPosition, this.finishPosition)) {
            System.out.println("The Player has reached the destination in " + iteration + " moves.");
        } else {
            System.out.println("No path can be found.");
        }
    }

    public void makeMove () {
        getPossibleMoves();

        int[] bestMove = this.getBestMove();

        board.markLocation(this.playerPosition, 'O');
        this.lastPosition = this.playerPosition;

        this.playerPosition = bestMove;
        board.markLocation(bestMove, 'P');

        this.printGameStatus();
    }

    private int[] getBestMove() {
        ArrayList<int[]> candidatesZeroCoef = new ArrayList<>();
        ArrayList<int[]> candidatesOneCoef = new ArrayList<>();
        ArrayList<int[]> candidatesTwoCoef = new ArrayList<>();
        ArrayList<int[]> candidatesThreeCoef = new ArrayList<>();
        ArrayList<int[]> candidatesFourCoef = new ArrayList<>();
        Random randomGenerator = new Random();

        for (int i = 0; i < this.possibleMoves.size(); i++) {

            if (Arrays.equals(this.possibleMoves.get(i), this.lastPosition)) {
                candidatesFourCoef.add(new int[] { this.possibleMoves.get(i)[0], this.possibleMoves.get(i)[1]});
            } else if (!this.isVisited(this.possibleMoves.get(i)) && isGettingPlayerCloser(this.possibleMoves.get(i))) {
                candidatesZeroCoef.add(new int[] { this.possibleMoves.get(i)[0], this.possibleMoves.get(i)[1]});
            } else if (!this.isVisited(this.possibleMoves.get(i)) && !isGettingPlayerCloser(this.possibleMoves.get(i))) {
                candidatesOneCoef.add(new int[] { this.possibleMoves.get(i)[0], this.possibleMoves.get(i)[1]});
            } else if (this.isVisited(this.possibleMoves.get(i)) && isGettingPlayerCloser(this.possibleMoves.get(i))) {
                candidatesTwoCoef.add(new int[] { this.possibleMoves.get(i)[0], this.possibleMoves.get(i)[1]});
            } else if (this.isVisited(this.possibleMoves.get(i)) && !isGettingPlayerCloser(this.possibleMoves.get(i))) {
                candidatesThreeCoef.add(new int[] { this.possibleMoves.get(i)[0], this.possibleMoves.get(i)[1]});
            }
        }

        if(candidatesZeroCoef.size() > 0) {
            int index = randomGenerator.nextInt(candidatesZeroCoef.size());
            return candidatesZeroCoef.get(index);
        }

        if(candidatesOneCoef.size() > 0) {
            int index = randomGenerator.nextInt(candidatesOneCoef.size());
            return candidatesOneCoef.get(index);
        }

        if(candidatesTwoCoef.size() > 0) {
            int index = randomGenerator.nextInt(candidatesTwoCoef.size());
            return candidatesTwoCoef.get(index);
        }

        if(candidatesThreeCoef.size() > 0) {
            int index = randomGenerator.nextInt(candidatesThreeCoef.size());
            return candidatesThreeCoef.get(index);
        }

        if(candidatesFourCoef.size() > 0) {
            int index = randomGenerator.nextInt(candidatesFourCoef.size());
            return candidatesFourCoef.get(index);
        }

        return new int[] {0, 0};
    }

    public ArrayList<int[]> getPossibleMoves() {
        ArrayList<int[]> possibleMoves = new ArrayList<>();

        int[] up = {this.playerPosition[0] + 1, this.playerPosition[1]};
        int[] down = {this.playerPosition[0] - 1, this.playerPosition[1]};
        int[] right = {this.playerPosition[0], this.playerPosition[1] + 1};
        int[] left = {this.playerPosition[0], this.playerPosition[1] - 1};

        if (isValidMove(up)) {
            possibleMoves.add(up);
        }

        if (isValidMove(down)) {
            possibleMoves.add(down);
        }

        if (isValidMove(right)) {
            possibleMoves.add(right);
        }

        if (isValidMove(left)) {
            possibleMoves.add(left);
        }

        this.possibleMoves = possibleMoves;
        return possibleMoves;
    }

    public boolean isValidMove(int[] move) {
        if (isOutOfBounds(move)) {
            return false;
        }

        if (isObstacle(move)) {
            return false;
        }

        return true;
    }

    private boolean isOutOfBounds(int[] move) {
        return move[0] < 0 ||
                move[0] > (this.board.getSizeY() - 1) ||
                move[1] < 0 ||
                move[1] > (this.board.getSizeX() - 1);
    }

    private boolean isObstacle(int[] move) {
        return this.board.getBoard()[move[0]][move[1]] == 'X';
    }

    private boolean isVisited(int[] move) {
        return this.board.getBoard()[move[0]][move[1]] == 'O';
    }

    private boolean isGettingPlayerCloser(int[] move) {
        return this.calculateDistanceToFinish(move) < this.calculateDistanceToFinish(this.playerPosition);
    }

    private int calculateDistanceToFinish(int[] move) {
        return Math.abs(this.finishPosition[0] - move[0]) + Math.abs(this.finishPosition[1] - move[1]);
    }

    public void printGameStatus() {
        System.out.println("Player position: " + Arrays.toString(this.playerPosition));
        for (int i = 0; i < this.possibleMoves.size(); i++) {
            System.out.println("Possible position " + (i + 1) + ": " + Arrays.toString(this.possibleMoves.get(i)));
        }
        board.displayBoard();
        System.out.println("-----------------------");
    }

}
