import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // take a game board and find the children
        int size;
        int[][] board;

        //-------------test for the game board class----------------
        /*GameBoard gameBoard = new GameBoard(board, size, null);
        GameBoard gameBoard1 = new GameBoard(board2, size, null);
        System.out.println(gameBoard.equals(gameBoard1));
        GameBoard game_board = new GameBoard(board, size, null);
        game_board.printBoard();
        System.out.println("cost: " + game_board.getCost(HeuristicFunction.HAMMING_DISTANCE));
        System.out.println("Children:");
        List<GameBoard> children = game_board.getChild();
        for (GameBoard child : children) {
            child.printBoard();
            System.out.println("cost: " + child.getCost(HeuristicFunction.MANHATTAN_DISTANCE));
            child.printBoardArray();
            System.out.println("----------------------------------------");
        }*/

        //-------------Actual program----------------
        // read the input file
        String input_file = "input.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(input_file));
            String line = reader.readLine();
            // 1st line will contain the size of the board
            size = Integer.parseInt(line);
            // 2nd to (size+1)th line will contain the board
            // each line will contain size number of integers seperated by space
            // the blank space will be represented by *
            board = new int[size][size];
            for (int i = 0; i < size; i++) {
                line = reader.readLine();
                String[] numbers = line.split(" ");
                for (int j = 0; j < size; j++) {
                    if (numbers[j].equals("*")) {
                        board[i][j] = 0;
                    } else {
                        board[i][j] = Integer.parseInt(numbers[j]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //-------------test for the NPuzzle class----------------
        //System.out.println(NPuzzleSolver.boardChecker(board, size));
        NPuzzleSolver.solve(board, size);


    }
}