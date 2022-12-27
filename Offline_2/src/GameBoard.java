import java.util.ArrayList;
import java.util.List;

/**
 * This is the game board class.
 * This is also the problem class of the CSP.
 * It has a 2D array of variables.
 * It has also access the constraints.
 */
public class GameBoard {
    private Variable[][] board;

    /**
     * Constructor for the game board class.
     * This constructor creates a game board with a 2D array of variables.
     * @param board     the 2D array of integers.
     */
    public GameBoard(int[][] board) {
        this.board = new Variable[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                this.board[i][j] = new Variable(i,j,board[i][j], board.length);
            }
        }
    }


    /**
     * This method returns the board.
     * @return  the board.
     */
    public Variable[][] getBoard() {
        return board;
    }

    /**
     * Method to print the board.
     */
    public void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j].printVariable();
            }
            System.out.println();
        }
    }


    /**
     * This method checks if the assignment is complete.
     */
    public boolean isComplete() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Variable> getVariables(){
        List<Variable> variables = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                variables.add(board[i][j]);
            }
        }
        return variables;
    }

}
