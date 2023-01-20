package com.akib.model;

import java.util.ArrayList;
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
}