package com.akib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static final int MAX_value = Integer.MAX_VALUE >> 2;
    public static final int MIN_value = Integer.MIN_VALUE >> 2;
    public static void main(String[] args) {
        //
        // 1. Read the input file.
        // 2. Create the game board.
        String fileName = "data/d-09-01.txt";
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
                line = line.trim(); // Removes the leading and trailing spaces.
                if (i != size-1) {
                    line = line.substring(0, line.length() - 2); // Removes the trailing <space> and |
                }else {
                    line = line.substring(0, line.length() - 4); // Removes the trailing <space> and | and ] and ;
                }
                String[] values = line.split(","); // Splits the line into values
                for (int j = 0; j < size; j++) {
                    board[i][j] = Integer.parseInt(values[j].trim()); // Converts the string to integer
                }
            }

            for (Variable_Heuristic heuristic: Variable_Heuristic.values()) {
                GameBoard gameBoard = new GameBoard(board);
                CSP_Solver csp_solver = new CSP_Solver(heuristic);
                long startTime = System.nanoTime();
                gameBoard = csp_solver.preprocessor(gameBoard);
//            gameBoard.printBoardWithDomain();
                gameBoard = csp_solver.solve(gameBoard);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                if (gameBoard != null){
//                    System.out.println("Solved with " + heuristic);
                    System.out.println(heuristic + " => " + csp_solver.nodesExpanded + ", " + csp_solver.backtracks + ", " + duration / 1000.0);
//                    System.out.println("number of nodes expanded: " + csp_solver.nodesExpanded);
//                    System.out.println("number of backtracks: " + csp_solver.backtracks);
//                    System.out.println("Assignment");
//                    gameBoard.printBoard();
                }else {
                    System.out.println("Not Solved");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}