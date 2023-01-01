package com.test;

import java.util.*;

public class VariableOrderHeuristic {
    private final Set<Variable> unassignedVariables;

    public VariableOrderHeuristic(Variable[][]variables) {
        this.unassignedVariables = new HashSet<>();
        for (Variable[] variable : variables) {
            for (Variable value : variable) {
                if (value.value() == 0) {
                    unassignedVariables.add(value);
                }
            }
        }
    }


    public void addUnassignedVariable(Variable variable){
        unassignedVariables.add(variable);
    }


    public Variable getNextVariables(VariableHeuristic heuristic){
        switch (heuristic){
            case VAH1:
                return VAH1();
            case VAH2:
                return VAH2();
            case VAH3:
                return VAH3();
            case VAH4:
                return VAH4();
            case VAH5:
                return VAH5();
            default:
                return null;
        }
    }

    public boolean isComplete(){
        return unassignedVariables.isEmpty();
    }

    // Variable with the smallest domain
    private Variable VAH1(){
        Variable minDomainVariable = null;
        for (Variable variable : unassignedVariables) {
            if (minDomainVariable == null){
                minDomainVariable = variable;
            }
            if (variable.domain().size() < minDomainVariable.domain().size()) {
                minDomainVariable = variable;
            }
        }
        unassignedVariables.remove(minDomainVariable);
        return minDomainVariable;
    }

    // Variable with the max forward degree
    private Variable VAH2(){
        Variable maxDegreeVariable = null;
        for (Variable v1 : unassignedVariables) {
            if (maxDegreeVariable == null){
                maxDegreeVariable = v1;
            }
            if (v1.degree() > maxDegreeVariable.degree()) {
                maxDegreeVariable = v1;
            }
            /*int degree = 0;
            for (Variable v2: unassignedVariables) {
                if (v1 == v2){
                    continue;
                }
                if (v1.x() == v2.x() ||             //they are in the same row
                        v1.y() == v2.y()) {         //they are in the same column
                    degree++;
                }
            }
            v1.setDegree(degree);
            if (maxDegreeVariable == null){
                maxDegreeVariable = v1;
            }

            if (v1.degree() > maxDegreeVariable.degree()) {
                maxDegreeVariable = v1;
            }*/

        }
        unassignedVariables.remove(maxDegreeVariable);
        return maxDegreeVariable;
    }


    // variable with the smallest domain tie break with max forward degree
    private Variable VAH3(){
        Variable minDomainVariable = null;
        for (Variable variable : unassignedVariables) {
            if (minDomainVariable == null){
                minDomainVariable = variable;
            }
            if (variable.domain().size() < minDomainVariable.domain().size()) {
                minDomainVariable = variable;
            }
        }

        // now tie break
        Variable maxDegreeVariable = minDomainVariable;
        for (Variable v1 : unassignedVariables) {
            if (v1.domain().size() == minDomainVariable.domain().size()){
                if (v1.degree() > maxDegreeVariable.degree()) {
                    maxDegreeVariable = v1;
                }
            }
        }
        unassignedVariables.remove(maxDegreeVariable);
        return maxDegreeVariable;
    }

    // variable with the minimum of domain size / forward degree (ratio)
    private Variable VAH4(){
        Variable minRatioVariable = null;
        double minRatio = Double.MAX_VALUE;
        for (Variable variable : unassignedVariables) {
            if (minRatioVariable == null){
                minRatioVariable = variable;
            }
            // check if the degree becomes 0
            double ratio;
            if (variable.degree() == 0){
                ratio = Double.MAX_VALUE;
            } else {
                ratio = (double) variable.domain().size() / variable.degree();
            }
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioVariable = variable;
            }
        }

        unassignedVariables.remove(minRatioVariable);

        return minRatioVariable;
    }

    // random variable
    private Variable VAH5(){
        int randomIndex = (int) (Math.random() * unassignedVariables.size());
        Variable randomVariable = (Variable) unassignedVariables.toArray()[randomIndex];
        unassignedVariables.remove(randomVariable);
        return randomVariable;
    }


    // sort the domain of the variable in increasing order of constraint
    public List<Integer> getDomainInOrderOfIncreasingConstraint(Variable variable){
        int constraint;
        HashMap<Integer, Integer> domainConstraint = new HashMap<>();
        for (int i : variable.domain()) {
            constraint = 0;
            for (Variable v: unassignedVariables){
                if ((variable.x() == v.x() || variable.y() == v.y())                //they are in the same row or column
                        && v.domain().contains(i)){                                 //the value is in the domain of the variable
                    constraint++;
                }
            }
            domainConstraint.put(i, constraint);
        }
        List<Integer> orderedDomain = new ArrayList<>(domainConstraint.keySet());
        orderedDomain.sort(Comparator.comparingInt(domainConstraint::get));
        return orderedDomain;
    }
}
