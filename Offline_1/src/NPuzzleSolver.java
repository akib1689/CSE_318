import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This is the NPuzzleSolver class. It is used to solve the NPuzzle problem.
 * It uses the A* algorithm to solve the problem.
 * It takes the input from the user and displays the solution.
 * It also displays the number of nodes expanded and the time taken to solve the problem.
 * It also displays the solution path.
 * It also displays the number of moves required to solve the problem.
 * It also displays the number of nodes in the solution path.
 * @Author: Akib-1805086
 */
public class NPuzzleSolver {

    /**
     * main function to solve the NPuzzle problem
     * @param board the initial board
     *              it is a k * k matrix
     *              with numbers from 1 to k * k - 1
     *              and a blank space (represented by 0)
     *              the blank space can be anywhere in the matrix
     *
     * @param size the size of the board (k)
     */
    public static void solve(int[][] board, int size) {
        if (boardChecker(board, size)) {
            System.out.println("The board is solvable");
            // create the start node
            GameBoard start = new GameBoard(board, size, null);
            // create the A* algorithm object
            AStar_algo hammingSolve = new AStar_algo(start, HeuristicFunction.HAMMING_DISTANCE);
            AStar_algo manhattanSolve = new AStar_algo(start, HeuristicFunction.MANHATTAN_DISTANCE);
            // solve the problem
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("intermediate.txt"));
                hammingSolve.solve(writer);
                manhattanSolve.solve(writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The board is not solvable");
        }

    }

    /**
     * Checks if the input is valid or not.
     * @param   board   the input board
     * @param   size    the size of the board
     * @return  true    if the input is valid
     *          false   otherwise
     */
    public static boolean boardChecker(int[][] board, int size) {
        // grid size 𝑘 check if k is even or odd
        if (size % 2 == 0){
            // even board
            // find the position of the blank space (represented by 0)
            int blank_space_row = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] == 0) {
                        blank_space_row = i;
                        break;
                    }
                }
            }
            // find the number of inversions
            int inversions = countNumberOfInversion(board, size);

            if (blank_space_row % 2 == 0) {
                // blank space is in an even row
                // the board is solvable if:
                // the blank is on an even row counting from the bottom (second-last, fourth-last, etc.) and number
                // of inversions is odd.
                return inversions % 2 == 1;
            } else {
                // blank space is in an odd row
                // the board is solvable if:
                // the blank is on an odd row counting from the bottom (last, third-last, fifth-last, etc.) and number
                // of inversions is even
                return inversions % 2 == 0;
            }
        } else {
            // odd board
            // count the number of inversions
            int inversions = countNumberOfInversion(board, size);

            // if the number of inversions is odd, the board is solvable
            return inversions % 2 == 0;
        }
    }

    /**
     * Checks the number of inversion in the board.
     * @param board the board to check
     * @param size the size of the board
     */
    private static int countNumberOfInversion(int[][] board, int size) {
        // at first we will convert the 2D array into 1D array
        int [] arr1D = new int[size*size];
        int k = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                arr1D[k] = board[i][j];
                k++;
            }
        }

        // now we will count the number of inversion
        int inversion = 0;
        for (int i = 0; i < arr1D.length; i++) {
            for (int j = i + 1; j < arr1D.length; j++) {
                if (arr1D[i] > 0 && arr1D[j] > 0 &&  arr1D[i] > arr1D[j]) {
                    inversion++;
                }
            }
        }

        return inversion;
    }
}
