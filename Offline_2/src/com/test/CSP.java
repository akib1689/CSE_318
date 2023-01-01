package com.test;

public class CSP {
    private Variable[][] variables;

    public CSP (int size, int [][] board){
        variables = new Variable[size][size];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                variables[i][j] = new Variable(i, j, board[i][j], size);
            }
        }
    }

    public Variable getVariable(int x, int y) {
        return variables[x][y];
    }

    public int size(){
        return variables.length;
    }

    public Variable[][] getVariables() {
        return variables;
    }
}
