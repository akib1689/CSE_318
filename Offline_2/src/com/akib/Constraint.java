package com.akib;

import java.util.List;

/**
 * The constraint class. of the CSP.
 * it has
 * condition: a boolean function that is applied to the variables in the scope.
 *            it this case the function is if the variable is in same row or column
 *            then the value of the variable is not equal
 * holds: a boolean that is true if the constraint is satisfied.
 */
public class Constraint {
    public static boolean holds(List<Variable> scope) {
        for (Variable v: scope){
            for (Variable v2: scope){
                if (v != v2 &&
                        (v.getValue() != 0 && v2.getValue() != 0) &&
                        (v.getX() == v2.getX() || v.getY() == v2.getY()) &&
                        v.getValue() == v2.getValue()){
                        return false;
                }
            }
        }

        return true;
    }








}
