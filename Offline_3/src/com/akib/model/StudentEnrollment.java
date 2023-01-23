package com.akib.model;

import com.akib.PenaltyStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This is the edge constraining class
 * if any student takes more than one course then there is an edge in the graph between the 2 course
 */
public class StudentEnrollment {
    private List<Course> enrolledCourse;

    public StudentEnrollment() {
        enrolledCourse = new ArrayList<>();
    }

    public void addCourse(Course course){
        enrolledCourse.add(course);
    }

    public List<Course> getEnrolledCourses(){
        return this.enrolledCourse;
    }

    @Override
    public String toString() {
        return enrolledCourse.toString();
    }

    public double getPenalty(PenaltyStrategy penaltyStrategy){
        double penalty = 0;
        Collections.sort(enrolledCourse, Comparator.comparingInt(Course::getTimeSlot));
        for (int i = 0; i < enrolledCourse.size(); i++) {
            for (int j = i + 1; j < enrolledCourse.size(); j++) {
                int gap = enrolledCourse.get(j).getTimeSlot() - enrolledCourse.get(i).getTimeSlot();
                if (penaltyStrategy == PenaltyStrategy.LINEAR) {
                    penalty += getLinearPenalty(gap);
                } else if (penaltyStrategy == PenaltyStrategy.EXPONENTIAL) {
                    penalty += getExponentialPenalty(gap);
                }
            }
        }

        return penalty;
    }

    private double getLinearPenalty(int gap){
        if (gap <= 5){
            return 2 * (5 - gap);   //penalty = 2*(5-n)
        } else {
            return 0;
        }
    }

    private double getExponentialPenalty(int gap){
        if (gap <= 5){
            return Math.pow(2, 5 - gap);
        } else {
            return 0;
        }
    }
}