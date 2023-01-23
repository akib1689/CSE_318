package com.akib;

import com.akib.model.Course;

import java.util.ArrayList;
import java.util.HashMap;

public class KempChain {
    public static void runKempChainForIteration(HashMap<String, Course> courseMap, int iteration){
        int iterationCount = 0;
        while (iterationCount < iteration){
            if (interChange(courseMap))
                iterationCount++;
        }
    }

    private static boolean interChange(HashMap<String, Course> courseMap){
        // generate a random number between 1 to courseMap.size()
        int random1 = (int) (Math.random() * courseMap.size()) + 1;
        // cast the random number to string with 4 digits initial digits are 0
        String courseID1 = String.format("%04d", random1);
        // get the course object from the courseMap
        Course course1 = courseMap.get(courseID1);


        // if the course has no conflicting course then return
        if (course1.getConflictingCourse().size() == 0){
            return false;
        }

        // generate a random number between 0 to course1.getConflictingCourse().size()
        int random2 = (int) (Math.random() * course1.getConflictingCourse().size());

        // get the conflicting course from the course1
        Course course2 = (Course) course1.getConflictingCourse().toArray()[random2];
        int timeSlot1 = course1.getTimeSlot();
        int timeSlot2 = course2.getTimeSlot();

        ArrayList<Course> visited = new ArrayList<>();
        ArrayList<Course> chain = new ArrayList<>();

        getKempChain(course2, timeSlot1, timeSlot2, visited, chain);
        // now we have the chain of courses that need to be interchanged
        // chain has the courses whose time slot in either timeSlot1 or timeSlot2

        // if the course has timeSlot == timeSlot1 then change it to timeSlot2
        // if the course has timeSlot == timeSlot2 then change it to timeSlot1

        for (Course course : chain) {
            if (course.getTimeSlot() == timeSlot1){
                course.setTimeSlot(timeSlot2);
            }else if (course.getTimeSlot() == timeSlot2){
                course.setTimeSlot(timeSlot1);
            }
        }
        return true;
    }

    public static void getKempChain(Course course,int timeSlot1, int timeSlot2, ArrayList<Course> visited, ArrayList<Course> chain){
        visited.add(course);
        chain.add(course);

        for (Course conflictingCourse : course.getConflictingCourse()) {
            // if the conflicting course has the same time slot as the parent course or the course itself
            // then add it to the chain and recursively call the function (the course need to be not visited)
            if (conflictingCourse.getTimeSlot() == timeSlot1 ||
                    conflictingCourse.getTimeSlot() == timeSlot2){
                if (!visited.contains(conflictingCourse)){
                    getKempChain(conflictingCourse, timeSlot1,timeSlot2, visited, chain);
                }
            }
        }
    }
}
