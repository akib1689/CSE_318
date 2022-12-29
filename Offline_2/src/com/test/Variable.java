package com.test;

import java.util.Arrays;
import java.util.Objects;

public class Variable {
    private final int x, y;                 // The x and y coordinates of the variable.

    private int value;                      // The assigned value of the variable.

    private final int [] domain;                  // The domain of the variable.


    /**
     * Constructor for the variable class.
     * This constructor creates a variable with a domain of 1 to 10.
     * and assigns a value to the variable.
     * @param x         the x coordinate of the variable.
     * @param y         the y coordinate of the variable.
     * @param value     the value of the variable. that is to be assigned.
     *                  if the value is 0 then the domain is set to the domain array. from domainSize to 1.
     *                  if the value is not 0 then the domain is set to the value only.
     * @param domainSize    the size of the domain.
     */
    public Variable(int x, int y, int value, int domainSize) {
        this.x = x;
        this.y = y;
        this.value = value;

        if (value != 0){
            this.domain = new int[1];
            this.domain[0] = value;
            return;
        }
        this.domain = new int[domainSize];
        for (int i = 0; i < domainSize; i++) {
            this.domain[i] = domainSize - i;
        }
    }






    // Getters and Setters.
    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int value() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int[] domain() {
        return domain;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return x == variable.x && y == variable.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
