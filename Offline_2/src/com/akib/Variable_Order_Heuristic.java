package com.akib;

import java.util.ArrayList;
import java.util.List;

/**
 * This has a method to get the next variable to be assigned.
 * it takes a list of variables and a variable heuristic.
 * the variable heuristic is used to choose the next variable.
 * the variable heuristic class contains all the necessary heuristics and the explanation of the heuristics.
 */
public class Variable_Order_Heuristic {
    public static Variable getNextVariable(List<Variable> variables, Variable_Heuristic variable_heuristic) {
        switch (variable_heuristic) {
            case VAH1:
                return VAH1(variables);
            case VAH2:
                return VAH2(variables);
            case VAH3:
                return VAH3(variables);
            case VAH4:
                return VAH4(variables);
            case VAH5:
                return VAH5(variables);
            default:
                return null;
        }
    }

    /**
     * This method returns the variable with the least number of values in its domain.
     * @param variables     the list of variables.
     * @return              the variable with the least number of values in its domain.
     */
    private static Variable VAH1(List<Variable> variables) {
        Variable minDomainVariable = variables.get(0);
        for (Variable variable : variables) {
            if (variable.getValue() != 0){
                continue;
            }
            if (variable.getDomainSize() < minDomainVariable.getDomainSize()) {
                minDomainVariable = variable;
            }
        }
        return minDomainVariable;
    }

    /**
     * This method returns the variable with the maximum degree to unassigned variables.
     * @param variables     the list of variables.
     * @return              the variable with the maximum degree to unassigned variables.
     */
    private static Variable VAH2(List<Variable> variables) {
        Variable maxDegreeVariable = variables.get(0);
        // first count the number of unassigned variables. around the variable.
        for (Variable variable: variables){
            if (variable.getValue() != 0){
                continue;
            }

            int degree = variable.getForwardDegree(variables);
            int maxDegree = maxDegreeVariable.getForwardDegree(variables);
            if (degree > maxDegree){
                maxDegreeVariable = variable;
            }
        }
        return maxDegreeVariable;
    }

    /**
     * This method returns the variable with the least number of values in its domain.
     * if there are more than one variable with the least number of values in its domain.
     * then the variable with the maximum degree to unassigned variables is returned.
     * @param variables     the list of variables.
     * @return              the variable with the least number of values in its domain.
     *                      if there are more than one variable with the least number of values in its domain.
     *                      then the variable with the maximum degree to unassigned variables is returned.
     */
    private static Variable VAH3(List<Variable> variables) {
        Variable minDomainVariable = variables.get(0);
        for (Variable variable : variables) {
            if (variable.getValue() != 0){
                continue;
            }
            if (variable.getDomainSize() < minDomainVariable.getDomainSize()) {
                minDomainVariable = variable;
            }
        }
        // if there are more than one variable with the least number of values in its domain.
        // then the variable with the maximum degree to unassigned variables is returned.
        for (Variable variable : variables) {
            if (variable.getValue() != 0){
                continue;
            }
            if (variable.getDomainSize() == minDomainVariable.getDomainSize()) {
                if (variable.getForwardDegree(variables) > minDomainVariable.getForwardDegree(variables)) {
                    minDomainVariable = variable;
                }
            }
        }
        return minDomainVariable;
    }

    /**
     * This method returns the variable that minimizes VAH1 / VAH2 (ratio).
     */
    private static Variable VAH4(List<Variable> variables) {
        Variable minRatioVariable = variables.get(0);
        for (Variable variable : variables) {
            if (variable.getValue() != 0){
                continue;
            }
            double ratio = variable.getRatioOfDomainWithForwardDegree(variables);
            double minRatio = minRatioVariable.getRatioOfDomainWithForwardDegree(variables);
            if (ratio < minRatio) {
                minRatioVariable = variable;
            }
        }
        return minRatioVariable;
    }

    /**
     * This method returns a random variable.
     * @param variables     the list of variables.
     * @return              a random variable.
     */
    private static Variable VAH5(List<Variable> variables) {
        //make a list of unassigned variables.
        List<Variable> unassignedVariables = new ArrayList<>();
        for (Variable variable : variables) {
            if (variable.getValue() == 0) {
                unassignedVariables.add(variable);
            }
        }

        return unassignedVariables.get((int) (Math.random() * unassignedVariables.size()));
    }
}
