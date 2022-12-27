import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //
        // 1. Read the input file.
        // 2. Create the game board.
        String fileName = "data/d-10-01.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            // 1st line the size of the board.
            String line = reader.readLine(); // Reads N=10;
            line = line.substring(2, line.length()-1); // Removes N= and the trailing ;
            int size = Integer.parseInt(line); // Converts the string to an integer.
            int[][] board = new int[size][size];
            reader.readLine(); // Reads start =
            reader.readLine(); // Reads [| of the first line
            for (int i = 0; i < size; i++) {
                line = reader.readLine(); // Reads the line
                if (i != size-1) {
                    line = line.substring(0, line.length() - 2); // Removes the trailing <space> and |
                }else {
                    line = line.substring(0, line.length() - 4); // Removes the trailing <space> and | and ] and ;
                }
                String[] values = line.split(", "); // Splits the line into values
                for (int j = 0; j < size; j++) {
                    board[i][j] = Integer.parseInt(values[j]); // Converts the string to integer
                }
            }
            GameBoard gameBoard = new GameBoard(board);
            CSP_Solver csp_solver = new CSP_Solver(Variable_Heuristic.VAH1);
            gameBoard = csp_solver.preprocessor(gameBoard);
            gameBoard.printBoard();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}