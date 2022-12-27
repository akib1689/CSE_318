import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This is the Game_board class. It is used to create the game board and
 * to display the game board.
 * it also acts as the nodes of the priority queue
 * the value of the node is the f(n) value
 * it is a k * k matrix
 * with numbers from 1 to k * k - 1
 * and a blank space
 * the blank space is represented by 0
 * @Author: Akib-1805086
 */
public class GameBoard {
    // the game board
    private final int[][] board;
    // the size of the game board
    private final int size;
    // the cost to reach the current node
    private final int g;

    // the parent of the node
    private final GameBoard parent;

    // the constructor
    public GameBoard(int[][] board, int size, GameBoard parent) {
        this.board = board;
        this.size = size;
        this.parent = parent;
        if (parent == null) {
            this.g = 0;
        } else {
            this.g = parent.g + 1;
        }
    }


    // the cost function
    public int getCost(HeuristicFunction heuristicFunction) {
        switch (heuristicFunction) {
            case MANHATTAN_DISTANCE:
                return g + manhattan_distance();
            case HAMMING_DISTANCE:
                return g + hamming_distance();
            default:
                return 0;
        }
    }

    // the manhattan distance
    private int manhattan_distance() {
        int distance = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    int correct_row = (board[i][j] - 1) / size;
                    int correct_col = (board[i][j] - 1) % size;
                    distance += Math.abs(correct_row - i) + Math.abs(correct_col - j);
                }
            }
        }
        return distance;
    }

    // the hamming distance
    private int hamming_distance() {
        int distance = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    int correct_row = (board[i][j] - 1) / size;
                    int correct_col = (board[i][j] - 1) % size;
                    if (correct_row != i || correct_col != j) {
                        distance++;
                    }
                }
            }
        }
        return distance;
    }


    // find the children from this game board
    public List<GameBoard> getChild(){
        List<GameBoard>children = new ArrayList<>();
        // find the position of the blank space
        int blank_space_row = 0;
        int blank_space_col = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    blank_space_row = i;
                    blank_space_col = j;
                }
            }
        }

        // check if the blank space row + 1 is valid
        if (blank_space_row + 1 < size) {
            // swap the blank space with the element below it
            int[][] new_board = new int[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(board[i], 0, new_board[i], 0, size);
            }
            new_board[blank_space_row][blank_space_col] = new_board[blank_space_row+1][blank_space_col];
            new_board[blank_space_row+1][blank_space_col] = 0;
            // create a new game board
            GameBoard new_game_board = new GameBoard(new_board, size, this);
            // add the new game board to the children list
            children.add(new_game_board);
        }

        // check if the blank space row - 1 is valid
        if (blank_space_row - 1 >= 0) {
            // swap the blank space with the element above it
            int[][] new_board = new int[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(board[i], 0, new_board[i], 0, size);
            }
            new_board[blank_space_row][blank_space_col] = new_board[blank_space_row-1][blank_space_col];
            new_board[blank_space_row-1][blank_space_col] = 0;
            // create a new game board
            GameBoard new_game_board = new GameBoard(new_board, size, this);
            // add the new game board to the children list
            children.add(new_game_board);
        }

        // check if the blank space col + 1 is valid
        if (blank_space_col + 1 < size) {
            // swap the blank space with the element to the right of it
            int[][] new_board = new int[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(board[i], 0, new_board[i], 0, size);
            }
            new_board[blank_space_row][blank_space_col] = new_board[blank_space_row][blank_space_col+1];
            new_board[blank_space_row][blank_space_col+1] = 0;
            // create a new game board
            GameBoard new_game_board = new GameBoard(new_board, size, this);
            // add the new game board to the children list
            children.add(new_game_board);
        }

        // check if the blank space col - 1 is valid
        if (blank_space_col - 1 >= 0) {
            // swap the blank space with the element to the left of it
            int[][] new_board = new int[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(board[i], 0, new_board[i], 0, size);
            }
            new_board[blank_space_row][blank_space_col] = new_board[blank_space_row][blank_space_col-1];
            new_board[blank_space_row][blank_space_col-1] = 0;
            // create a new game board
            GameBoard new_game_board = new GameBoard(new_board, size, this);
            // add the new game board to the children list
            children.add(new_game_board);
        }

        return children;
    }


    // getters
    public int getSize() {
        return this.size;
    }

    // print the game board
    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    System.out.print("*\t");
                } else {
                    System.out.print(board[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    // override the equals method
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        GameBoard other = (GameBoard) obj;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // function to print the parent up to the root
    public void printParent() {
        printParentRecursive(this);
    }

    // function to print the parent up to the root recursively
    private void printParentRecursive(GameBoard game_board) {
        if (game_board.parent != null) {
            printParentRecursive(game_board.parent);
        }
        game_board.printBoard();
        for (int i = 0; i < size*2; i++) {
            System.out.print("--");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str.append(board[i][j]).append("\t");
            }
            str.append("\n");
        }
        return str.toString();
    }

    @Override
    public int hashCode() {

        return Arrays.deepHashCode(board);

    }

    public int[][] getBoard() {
        return board;
    }
}
