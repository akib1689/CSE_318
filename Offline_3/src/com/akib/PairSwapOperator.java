package com.akib;

import com.akib.model.Course;

import java.util.ArrayList;
import java.util.HashMap;

public class PairSwapOperator {


    public static void applyPairSwap(HashMap<String, Course> courseHashMap){
        double currentPenalty = Main.calculateAveragePenalty();

        ArrayList<Course> courses = new ArrayList<>(courseHashMap.values());

        for (int i = 0; i < courses.size(); i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                if (i==j) continue; // skip if same course
                if (courses.get(i).getTimeSlot() == courses.get(j).getTimeSlot()) continue; // skip if same time slot (no need to swap


                Course course1 = courses.get(i);
                Course course2 = courses.get(j);

                ArrayList<Course> visited1 = new ArrayList<>();
                ArrayList<Course> chain1 = new ArrayList<>();
                ArrayList<Course> visited2 = new ArrayList<>();
                ArrayList<Course> chain2 = new ArrayList<>();
                KempChain.getKempChain(course1, course2.getTimeSlot(), course1.getTimeSlot(), visited1, chain1);
                KempChain.getKempChain(course2, course1.getTimeSlot(), course2.getTimeSlot(), visited2, chain2);
                if (chain1.size() == 1 && chain2.size() == 1){
                    // swap
                    swapPairOfCourses(course1, course2);
                    double newPenalty = Main.calculateAveragePenalty();
                    if (newPenalty < currentPenalty){
                        currentPenalty = newPenalty;
                    }else{
                        swapPairOfCourses(course1, course2);
                    }
                }

            }
        }

    }
    private static void swapPairOfCourses(Course course1, Course course2){
        int temp = course1.getTimeSlot();
        course1.setTimeSlot(course2.getTimeSlot());
        course2.setTimeSlot(temp);
    }

}
