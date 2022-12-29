package com.akib;

/**
 * The following heuristic is present in the problem
 * VAH1: The variable chosen is the one with the least number of values in its domain.
 * VAH2: The variable chosen is the one with the maximum degree to unassigned variables.(max forward degree)
 * VAH3: The variable chosen by VAH1, Tie breaking is done by VAH2.
 * VAH4: The variable chosen by the one that minimizes VAH1 / VAH2 (ratio).
 * VAH5: Randomly choose a variable.
 */
public enum Variable_Heuristic {
    VAH1, VAH2, VAH3, VAH4, VAH5
}
