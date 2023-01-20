package com.akib.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a course.
 * This is the node classes of the graph.
 */
public class Course {
    private String courseID;
    private int numEnrollment;

    private HashSet<Course> conflictingCourse;

    private int timeSlot;                           // the time slot assigned to this course

    public Course(String courseID, int numEnrollment) {
        this.courseID = courseID;
        this.timeSlot = -1;                         // initially no time slot is assigned
        this.numEnrollment = numEnrollment;
        this.conflictingCourse = new HashSet<>();
    }

    public void addConflictingCourse(Course course){
        conflictingCourse.add(course);
    }

    public String getCourseID() {
        return courseID;
    }

    public int getNumConflictingCourse(){
        return conflictingCourse.size();
    }

    public HashSet<Course> getConflictingCourse() {
        return conflictingCourse;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return numEnrollment == course.numEnrollment && courseID.equals(course.courseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseID, numEnrollment);
    }
}
