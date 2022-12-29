package com.akib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the game board class.
 * This is also the problem class of the CSP.
 * It has a 2D array of variables.
 * It has also access the constraints.
 */
public class GameBoard {
    private final Variable[][] board;

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


    // Getters and Setters.
    public Variable[][] getBoard() {
        return board;
    }

    /**
     * Method to print the board with domain.
     */
    public void printBoardWithDomain() {
        for (Variable[] variables : board) {
            for (int j = 0; j < board.length; j++) {
                variables[j].printVariable();
            }
            System.out.println();
        }
    }
    /**
     * Method to print the board. only the values of the variables.
     */
    public void printBoard() {
        for (Variable[] variables : board) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(variables[j].getValue() + " ");
            }
            System.out.println();
        }
    }

    /**
     * This method checks if the assignment is complete.
     */
    public boolean isComplete() {
        return getNumberOfSolvedVariables() == board.length * board.length;
    }

    public List<Variable> getVariables(){
        List<Variable> variables = new ArrayList<>();
        for (Variable[] value : board) {
            variables.addAll(Arrays.asList(value).subList(0, board.length));
        }
        return variables;
    }

    /**
     * method to get the number of solved variables.
     */
    public int getNumberOfSolvedVariables() {
        int count = 0;
        for (Variable[] variables : board) {
            for (int j = 0; j < board.length; j++) {
                if (variables[j].getValue() != 0) {
                    count++;
                }
            }
        }
        return count;
    }

}
