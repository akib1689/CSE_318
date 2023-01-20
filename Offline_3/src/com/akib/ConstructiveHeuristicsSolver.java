package com.akib;

import com.akib.model.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ConstructiveHeuristicsSolver {

    /**
     * This method solves the problem using the constructive heuristics.
     * @param heuristics        the heuristics to be used for solving the problem
     * @return                  the number of time slots required to solve the problem
     *                          (the number of days)
     *                          (the number of colors in the graph coloring problem)
     */
    public static int solveAndReturnTotalSlots(Heuristics heuristics, HashMap<String, Course> courses){
        switch (heuristics){
            case LARGEST_DEGREE:
                return solveLargestDegree(courses);
            case LARGEST_ENROLLMENT:
                return 0;
            case SATURATION_DEGREE:
                return 1;
            case RANDOM:
                return 2;
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
