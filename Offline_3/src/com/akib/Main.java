package com.akib;

import com.akib.model.Course;
import com.akib.model.StudentEnrollment;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

public class Main {
    private static List<StudentEnrollment> enrollments;
    public static void main(String[] args) {
        // take the input from the file
        String datasetName = "car-f-92";
        HashMap<String, Course> courses = takeInput(datasetName);
        enrollments = getStudentEnrollment(datasetName, courses);
        addConflictingCourse(enrollments);
        // solve the problem using the constructive heuristics
        int totalSlots = ConstructiveHeuristicsSolver.solveAndReturnTotalSlots(Heuristics.LARGEST_DEGREE , courses);

        System.out.println("Total Time slots required: "+totalSlots);
        // find the average penalty
        double initialAveragePenalty = calculateAveragePenalty();
        System.out.println("Average penalty after constructive heuristics: " + initialAveragePenalty);

        // try to improve the solution using kempe chain
        KempChain.runKempChainForIteration(courses, 1500);

        double afterKempChangeAveragePenalty = calculateAveragePenalty();
        System.out.println("Average penalty after kemp chain: " + afterKempChangeAveragePenalty);

        // try to improve the solution using pair swap
        PairSwapOperator.applyPairSwap(courses);
        double afterPairSwapAveragePenalty = calculateAveragePenalty();
        System.out.println("Average penalty after pair swap: " + afterPairSwapAveragePenalty);
        ArrayList<Course> timeAscendingCourses = new ArrayList<>(courses.values());
        timeAscendingCourses.sort(Comparator.comparingInt(Course::getTimeSlot));
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("solution/output.txt"))){
            for (Course course: timeAscendingCourses){
                bufferedWriter.write(course.getCourseID()+" "+course.getTimeSlot());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double calculateAveragePenalty(){
        double totalPenalty = 0;
        for (StudentEnrollment enrollment: enrollments){
            totalPenalty += enrollment.getPenalty(PenaltyStrategy.EXPONENTIAL);
        }
        return totalPenalty/enrollments.size();
    }


    private static HashMap<String, Course> takeInput(String datasetName) {
        HashMap<String, Course> courses = new HashMap<>();

        try {
            Scanner scanner = new Scanner(new FileInputStream("dataset/" + datasetName + ".crs"));
            while(scanner.hasNextLine()){
                String[] inputs = scanner.nextLine().split(" ");
                //inputs[0] = course name
                //inputs[1] = how many students have taken the course
                courses.put(inputs[0], new Course(inputs[0], Integer.parseInt(inputs[1])));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return courses;
    }


    private static List<StudentEnrollment> getStudentEnrollment(String datasetName, HashMap<String, Course> courseMap){
        List<StudentEnrollment> enrollments = new ArrayList<>();
        try{
            Scanner scanner = new Scanner(new FileInputStream("dataset/" + datasetName + ".stu"));

            while(scanner.hasNextLine()){
                String[] courseList = scanner.nextLine().split(" ");
                StudentEnrollment enrollment = new StudentEnrollment();
                for (String course: courseList){
                    enrollment.addCourse(courseMap.get(course));
                }
                enrollments.add(enrollment);
            }
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

        return enrollments;
    }

    private static void addConflictingCourse(List<StudentEnrollment> enrollments){
        for (StudentEnrollment enrollment: enrollments){
            for (Course c1: enrollment.getEnrolledCourses()){
                for (Course c2: enrollment.getEnrolledCourses()){
                    if (!c1.equals(c2)){
                        c1.addConflictingCourse(c2);
                        c2.addConflictingCourse(c1);
                    }
                }
            }
        }
    }
}