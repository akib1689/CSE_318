package com.akib;

import com.akib.model.Course;

import java.util.*;

public class ConstructiveHeuristicsSolver {

    /**
     * This method solves the problem using the constructive heuristics.
     * @param heuristics        the heuristics to be used for solving the problem
     * @return                  the number of time slots required to solve the problem
     *                          (the number of days)
     *                          (the number of colors in the graph coloring problem)
     */
    public static int solveAndReturnTotalSlots(Heuristics heuristics, HashMap<String, Course> courses){
        switch (heuristics) {
            case LARGEST_DEGREE :
                return solveLargestDegree(courses);
            case LARGEST_ENROLLMENT :
                return solveLargestEnrollment(courses);
            case SATURATION_DEGREE:
                    return solveDSaturAlgo(courses);
            case RANDOM :
                return solveRandom(courses);
            default:
                return -1;
        }
    }

    private static int solveLargestDegree(HashMap<String, Course> courses){
        ArrayList<Course> courseList = new ArrayList<>(courses.values());
        // sort the courses in descending order of their degree
        courseList.sort(Comparator.comparingInt(Course::getNumConflictingCourse).reversed());
        return solveForTimeTable(courseList);
    }

    private static int solveLargestEnrollment(HashMap<String, Course> courses) {
        ArrayList<Course> courseList = new ArrayList<>(courses.values());
        courseList.sort(Comparator.comparingInt(Course::getNumEnrollment).reversed());
        return solveForTimeTable(courseList);
    }

    private static int solveRandom(HashMap<String, Course> courses) {
        ArrayList<Course> courseList = new ArrayList<>(courses.values());
        Collections.shuffle(courseList);

        return solveForTimeTable(courseList);
    }

    /**
     * Algo link can be found in wiki one implementation is found in geeks for geeks
     * both link is given below
     * (NOTE:The implementation is in C++)
     * @Link: https://en.wikipedia.org/wiki/DSatur
     * @Link: https://www.geeksforgeeks.org/dsatur-algorithm-for-graph-coloring/
     */
    private static int solveDSaturAlgo(HashMap<String, Course> courses) {
        ArrayList<Course> courseList = new ArrayList<>(courses.values());                       // convert the hashmap to arraylist
        courseList.sort(Comparator.comparingInt(Course::getNumConflictingCourse).reversed());   // sort the courses in descending order of their degree
        courseList.get(0).setTimeSlot(0);                                                       // Assign the vertex with the highest degree course time slot 0


        int totalSlot = 1;                              // keeps the total number of time slot assigned till now
        // initialize the time slot to 1
        // as 0 is already assigned
        for(int i=1; i<courseList.size(); i++) {
            HashSet<Integer> temp;                      // keeps track of adj nodes' time slot of all the variable
            HashSet<Integer> selected=null;             // keeps track of adj nodes' time slot of max saturation degree variable
            int maxSaturationDegree=-1, maxIndex=-1; // index of the maximum degree and saturation degree of the variable

            for(int j=0; j<courseList.size(); j++) {
                if(courseList.get(j).getTimeSlot() == -1) {
                    temp = new HashSet<>();

                    for(Course course: courseList.get(j).getConflictingCourse()) {
                        if(course.getTimeSlot() != -1) {
                            temp.add(course.getTimeSlot());         // the course already assigned time slot
                            // need to update
                        }
                    }

                    if(temp.size()>maxSaturationDegree ||
                            (temp.size()==maxSaturationDegree &&
                                    (courseList.get(j).getNumConflictingCourse() > courseList.get(maxIndex).getNumConflictingCourse()))) {
                        maxSaturationDegree = temp.size();
                        maxIndex = j;
                        selected = temp;
                    }
                }
            }

            int suggestedTimeSlot = 0;
            while(courseList.get(maxIndex).getTimeSlot() == -1) {

                // ignore the warning selected is not null
                if(!selected.contains(suggestedTimeSlot)) {
                    courseList.get(maxIndex).setTimeSlot(suggestedTimeSlot);
                    if(suggestedTimeSlot == totalSlot) {
                        totalSlot++;
                    }
                } else {
                    suggestedTimeSlot++;
                }
            }
        }
        return totalSlot;
    }


    private static int solveForTimeTable(ArrayList<Course> courseList){
        int totalSlots = 0;
        for (Course course: courseList){
            // sort the conflicting courses in ascending order of their time slot
            ArrayList<Course> conflictingCourses = new ArrayList<>(course.getConflictingCourse());
            conflictingCourses.sort(Comparator.comparingInt(Course::getTimeSlot));

            int suggestedTimeSlot = 0;
            for (Course conflictingCourse: conflictingCourses){
                if (conflictingCourse.getTimeSlot() != -1){
                    // if the conflicting course has a time slot assigned
                    if (conflictingCourse.getTimeSlot() == suggestedTimeSlot){
                        // if the conflicting course has the same time slot as the suggested time slot
                        // then increment the suggested time slot
                        suggestedTimeSlot++;
                    } else if (conflictingCourse.getTimeSlot() > suggestedTimeSlot ){
                        // if the conflicting course has a time slot greater than the suggested time slot
                        // then assign the suggested time slot to the course
                        // we may get some penalty for this but primary goal is to minimize the number of time slots
                        course.setTimeSlot(suggestedTimeSlot);
                    }
                }
            }
            if (course.getTimeSlot() == -1){
                // if the course does not have a time slot assigned yet
                if (suggestedTimeSlot == totalSlots){
                    // if the suggested time slot is greater than the total number of time slots
                    // then increment the total number of time slots
                    course.setTimeSlot(totalSlots);
                    totalSlots++;
                } else {
                    // if the suggested time slot is less than the total number of time slots(this must hold)
                    // then assign the suggested time slot to the course
                    course.setTimeSlot(suggestedTimeSlot);
                }
            }
        }

        return totalSlots;
    }






}
