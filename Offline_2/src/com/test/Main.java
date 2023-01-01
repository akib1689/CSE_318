package com.test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        //
        // 1. Read the input file.
        // 2. Create the game board.
        if (args.length != 2) {
            System.out.println("Usage: java Main <Requires 2 command line argument>");
            return;
        }

        String fileName = args[0];
        String heuristicName = args[1];
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
            System.out.print(fileName + " , ");
            VariableHeuristic heuristic = VariableHeuristic.valueOf(heuristicName);
            CSP gameBoard = new CSP(size, board);
            ForwardCheckSolver solver = new ForwardCheckSolver(heuristic);
//            BacktrackSolver solver = new BacktrackSolver(heuristic);
            solver.preProcess(gameBoard);
            solver.solve(gameBoard);
            System.out.println(solutionCheck(gameBoard.getVariables()));
//            System.out.println(degreeCheck(gameBoard.getVariables()));
            // print
            /*for (int i = 0; i < gameBoard.size(); i++) {
                for (int j = 0; j < gameBoard.size(); j++) {
                    System.out.print(gameBoard.getVariable(i, j).value() + " ");
                }
                System.out.println();
            }*/

        }catch (IOException e){
            e.printStackTrace();
        }
    }




    private static boolean solutionCheck(Variable[][] variables) {
        int N = variables.length;
        boolean[][] rowCheck = new boolean[N][N];
        boolean[][] colCheck = new boolean[N][N];
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                int val = variables[i][j].value();
                val--;
                if (rowCheck[i][val] || colCheck[j][val]) {
                    return false;
                }
                rowCheck[i][val] = true;
                colCheck[j][val] = true;
            }
        }
        return true;
    }

    private static boolean degreeCheck(Variable[][] variables) {
        int N = variables.length;
        // at first calculate the degree of each variable iterating over the board
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int degree = 0;
                // check the row
                for (int k = 0; k<N; k++) {
                    Variable rowVar = variables[i][k];
                    if (rowVar.value() == 0 && rowVar != variables[i][j]) {
                        degree++;
                    }
                    Variable colVar = variables[k][j];
                    if (colVar.value() == 0 && colVar != variables[i][j]) {
                        degree++;
                    }
                }
                if (degree != variables[i][j].degree()) {
                    return false;
                }
            }
        }
        return true;
    }
}
