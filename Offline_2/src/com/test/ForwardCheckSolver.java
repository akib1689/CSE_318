package com.test;

import java.util.HashSet;
import java.util.List;

public class ForwardCheckSolver {

    private VariableOrderHeuristic heuristic;
    private final VariableHeuristic variableHeuristic;

    private static long nodeCount = 0;
    private static long backtrack = 0;



    public ForwardCheckSolver(VariableHeuristic heuristic) {
        this.variableHeuristic = heuristic;
        this.heuristic = null;
    }



    public void solve (CSP csp){
        if (heuristic == null){
            heuristic = new VariableOrderHeuristic(csp.getVariables());
        }
        long start = System.nanoTime();
        boolean ret_val = backtrack(csp);
        long end = System.nanoTime();
        if (ret_val) {
            long duration = end - start;
            // format time according what fits best
            int count = 0;
            StringBuilder sb = new StringBuilder();
            while (count < 2 && duration > 0) {
                if (duration < 1000) {
                    sb.append(duration).append(" ns");
                    duration = 0;
                } else if (duration < 1000000) {
                    sb.append(duration / 1000).append(" us ");
                    duration = duration % 1000;
                } else if (duration < 1000000000) {
                    sb.append(duration / 1000000).append(" ms ");
                    duration = duration % 1000000;
                } else if (duration < 1000000000L * 60) {
                    sb.append(duration / 1000000000).append(" s ");
                    duration = duration % 1000000000;
                }else if (duration < 1000000000L * 60 * 60) {
                    sb.append(duration / 1000000000 / 60).append(" min ");
                    duration = duration % (1000000000L * 60);
                }else {
                    sb.append(duration / 1000000000 / 60 / 60).append(" hrs ");
                    duration = duration % (1000000000L * 60 * 60);
                }
                count++;
            }
            System.out.println("Forward Check Solver , " + variableHeuristic + " , " + nodeCount + " , " + backtrack + " , " + sb);

        } else {
            System.out.println("Not solved");
        }

    }

    private boolean backtrack(CSP csp){
        if (heuristic.isComplete()){
            return true;
        }

        Variable variable = heuristic.getNextVariables(variableHeuristic);
        List<Integer> domain = heuristic.getDomainInOrderOfIncreasingConstraint(variable);
//        List<Integer> domain = variable.domain();
        for (int value : domain) {
            variable.setValue(value);
            HashSet <Variable> variables_to_remove = new HashSet<>();
            nodeCount++;
            if (processVariableWithForwardCheck(csp, variable, variables_to_remove)){
                if (backtrack(csp)){
                    return true;
                }
            }
            addVariableValueToDomain(csp, variable, variables_to_remove);
            variable.setValue(0);
        }
        heuristic.addUnassignedVariable(variable);
        backtrack++;
        return false;
    }

    public void preProcess(CSP csp){
        for (int i = 0; i < csp.size(); i++) {
            for (int j = 0; j < csp.size(); j++) {
                Variable variable = csp.getVariable(i, j);
                if (variable.value() != 0){
                    processVariable(csp, variable);
                }
            }
        }
    }

    private void processVariable(CSP csp, Variable variable){
        for (int i = 0; i < csp.size(); i++) {
            Variable row_variable = csp.getVariable(variable.x(), i);
            if (row_variable.value() == 0){
                row_variable.removeValueFromDomain(variable.value());
            }
            Variable column_variable = csp.getVariable(i, variable.y());
            if (column_variable.value() == 0){
                column_variable.removeValueFromDomain(variable.value());
            }
            if (variable != row_variable){
                row_variable.decreaseForwardDegree();
            }
            if (variable != column_variable){
                column_variable.decreaseForwardDegree();
            }
        }
    }

    private boolean processVariableWithForwardCheck(CSP csp, Variable variable, HashSet<Variable> set){
        for (int i = 0; i < csp.size(); i++) {
            Variable row_variable = csp.getVariable(variable.x(), i);
            if (row_variable.value() == 0 && row_variable.domain().contains(variable.value())){
                row_variable.removeValueFromDomain(variable.value());
                set.add(row_variable);
                if (row_variable.domain().size()==0){
                    return false;
                }
            }
            Variable column_variable = csp.getVariable(i, variable.y());
            if (column_variable.value() == 0 && column_variable.domain().contains(variable.value())){
                column_variable.removeValueFromDomain(variable.value());
                set.add(column_variable);
                if (column_variable.domain().size() == 0){
                    return false;
                }
            }
            if (variable != row_variable){
                row_variable.decreaseForwardDegree();
            }
            if (variable != column_variable){
                column_variable.decreaseForwardDegree();
            }
        }

        return true;
    }

    private void addVariableValueToDomain(CSP csp, Variable variable, HashSet<Variable> set){
        for (int i = 0; i < csp.size(); i++) {
            Variable row_variable = csp.getVariable(variable.x(), i);
            if (set.contains(row_variable)){
                row_variable.addValueToDomain(variable.value());
            }
            Variable column_variable = csp.getVariable(i, variable.y());
            if (set.contains(column_variable)){
                column_variable.addValueToDomain(variable.value());
            }
            if (variable != row_variable){
                row_variable.increaseForwardDegree();
            }
            if (variable != column_variable){
                column_variable.increaseForwardDegree();
            }
        }
    }



}
