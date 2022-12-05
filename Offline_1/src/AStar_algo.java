/**
 * The AStar_algo class is the main class of the A* algorithm.
 * It takes the start game board and huristic function as input.
 * The class method is called by the NPuzzleSolver class.
 * @Author: Akib-1805086
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

public class AStar_algo {
    // A* algorithm
    // take a priority queue
    PriorityQueue<GameBoard> queue;

    // test file

    // maintain a list of visited nodes
    Set<GameBoard> visited;

    // take a start node
    GameBoard start;
    // take a goal node
    GameBoard goal;
    HeuristicFunction heuristicFunction;

    // comparator for the priority queue
    static Comparator<GameBoard> mannhattanDistanceComparator = Comparator.comparingInt(o -> o.getCost(HeuristicFunction.MANHATTAN_DISTANCE));

    static Comparator<GameBoard> hammingDistanceComparator = Comparator.comparingInt(o -> o.getCost(HeuristicFunction.HAMMING_DISTANCE));


    // constructor
    public AStar_algo(GameBoard start, HeuristicFunction heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
        switch (heuristicFunction) {
            case MANHATTAN_DISTANCE:
                queue = new PriorityQueue<>(mannhattanDistanceComparator);
                break;
            case HAMMING_DISTANCE:
                queue = new PriorityQueue<>(hammingDistanceComparator);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + heuristicFunction);
        }
        this.start = start;
        int size = start.getSize();
        int[][] goalBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) {
                    goalBoard[i][j] = 0;
                } else {
                    goalBoard[i][j] = i * size + j + 1;
                }
            }
        }
        this.goal = new GameBoard(goalBoard, size, null);
        this.visited = new HashSet<GameBoard>();

    }

    // the solve function
    public void solve(BufferedWriter writer) throws IOException {
        // add the start node to the queue
        System.out.println("Solving the puzzle using " + heuristicFunction + " as heuristic function");
        queue.add(start);
        visited.add(start);

        // expanded : Those who have entered the queue
        //          the visited list keeps track of the nodes that have been expanded
        // explored : Those who have been removed from the queue
        int explored = 0;
        int count = 0;
        writer.write("Solving the puzzle using " + heuristicFunction + " as heuristic function\n");
        // while the queue is not empty
        while (!queue.isEmpty()) {
            // manually terminate after 100000 iterations
            count++;
            if (count > 100000) {
                System.out.println("Terminated after 100000 iterations");
                return;
            }
            // remove the node with the lowest cost
            GameBoard current = queue.remove();
            explored++;

            // call the print function of this class


            // if the node is the goal node
            if (current.equals(goal)) {
                // print the solution
                // print the number of nodes expanded
                System.out.println("Number of nodes expanded: " + visited.size());
                // print the number of nodes explored
                System.out.println("Number of nodes explored: " + explored);
                // print the number of moves required to solve the problem(cost)
                System.out.println("Number of moves required to solve the problem: " + current.getCost(heuristicFunction));
                // print the solution path
                System.out.println("Solution Path: ");
                current.printParent();
                return;
            } else {
                // find the children of the node
                List<GameBoard> children = current.getChild();
                List<GameBoard> childrenToBeAdded = new ArrayList<>();
                // if any child is present in the visited set
                // do not add it to the queue
                // else add it to the queue
                for (GameBoard child : children) {
                    if (!visited.contains(child)) {
                        visited.add(child);
                        queue.add(child);
                        childrenToBeAdded.add(child);
                    }
                }
                // print
                printChild(childrenToBeAdded, writer, current);
            }
        }
    }

    /**
     * function to print the child of the current game board that is added to the queue in a single line
     * @param: childList: list of children of the current game board that is added to the queue
     * @param: writer: the BufferedWriter object to write to the file
     * @param current: the current game board
     * */
    public void printChild(List<GameBoard> childList, BufferedWriter writer, GameBoard current) throws IOException {


        writer.write("Current Node: \n");
        writer.write(current.toString());
        writer.write("Children: \n");


        // make a single line string taking only the ith row of the child game boards
        // and write it to the file
        for (int i = 0; i < childList.get(0).getSize(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (GameBoard child : childList) {
                stringBuilder.append(child.getBoard()[i][0]);
                for (int j = 1; j < child.getSize(); j++) {
                    stringBuilder.append("\t").append(child.getBoard()[i][j]);
                }
                stringBuilder.append("\t\t");
            }
            writer.write(stringBuilder + "\n");
        }

        // the visited list after each of the removals
        // intermediate states of the game board
        writer.write("Visited: " + visited.size() + "\n");

        writer.write("\n");
        writer.flush();
    }


}
