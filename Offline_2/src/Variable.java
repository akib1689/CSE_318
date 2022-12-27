import java.util.ArrayList;
import java.util.List;

/**
 * This is the variable class of the square present in the board.
 * It has a domain: the set of possible values that the variable can take.
 * It has a method to remove a value from the domain.
 * @Author: Akib-1805086
 * */
public class Variable {
    private final int x, y;                 // The x and y coordinates of the variable.
    private final List<Integer> domain;    // The domain of the variable.
    private int value;                     // The assigned value of the variable.

    /**
     * Constructor for the variable class.
     * This constructor creates a variable with a domain of 1 to 10.
     * and assigns a value to the variable.
     * @param value     the value of the variable. that is to be assigned.
     */
    public Variable(int x, int y, int value, int domainSize) {
        this.x = x;
        this.y = y;
        this.value = value;

        this.domain = new ArrayList<>(10);
        if (value != 0){
            return;
        }
        for (int i = 1; i <= domainSize; i++) {
            this.domain.add(i);
        }
    }

    /**
     * This method removes a value from the domain of the variable.
     * @param value     the value to be removed from the domain.
     */
    public void removeValueFromDomain(int value) {
        // Search for the value in the domain.
        for (int i = 0; i < domain.size(); i++) {
            if (domain.get(i) == value) {
                domain.remove(i);
                break;
            }
        }
    }

    /**
     * This method updates the value of the variable.
     */
    public void updateValue() {
        this.value = this.domain.get(0);
    }

    /**
     * This method updates the domain of the variable.
     * @return
     */


    // Getters and Setters.
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public int getDomainSize() {
        if (value != 0){
            return Integer.MAX_VALUE;
        }
        return domain.size();
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * method to get the forward degree of this variable.
     */
    public int getForwardDegree(List<Variable> variables) {
        int degree = 0;
        for (Variable v : variables) {
            if (v != this) {
                if (v.getX() == this.getX() || v.getY() == this.getY()) {
                    if (v.getValue() == 0) {
                        degree++;
                    }
                }
            }
        }
        return degree;
    }

    /**
     * method to check if the given value is in the domain of this variable.
     * @param value     the value to be checked.
     * @return          if the value is in the domain, return true.
     *                  else return false.
     */
    public boolean isInDomain(int value) {
        for (int i = 0; i < domain.size(); i++) {
            if (domain.get(i) == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * method to get the i'th value from the domain.
     * @param index   the index of the value to be returned.
     * @return        the value at the given index.
     *                if the index is out of bounds, return -1.
     */
    public int getValueFromDomain(int index) {
        if (index < 0 || index >= domain.size()) {
            return -1;
        }
        return domain.get(index);
    }


    /**
     * method to print the variable. with its domain.
     */
    public void printVariable() {
        System.out.print("Variable: (" + this.x + ", " + this.y + ")-> " + this.value + " -> ");
        System.out.println("Domain: " + this.domain);
    }




}
