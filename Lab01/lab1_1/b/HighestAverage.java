package b;

import java.io.*;
import java.util.*;

public class HighestAverage {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java b.HighestAverage <csv-file>");
            return;
        }
        
        Student top = null;
        int count = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length != 3) continue;
                
                try {
                    String numMec = p[0].trim();
                    String name = p[1].trim();
                    double avg = Double.parseDouble(p[2].trim());
                    
                    if (top == null || avg > top.average) {
                        top = new Student(numMec, name, avg);
                    }
                    count++;
                } catch (NumberFormatException e) {
                    // skip invalid lines
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
        
        if (top == null) {
            System.out.println("No students found.");
        } else {
            System.out.println("Top student: " + top.name + " (" + top.numMec + ") with average " + top.average);
            System.out.println("Total students: " + count);
        }
    }
    
    static class Student {
        String numMec, name;
        double average;
        
        Student(String numMec, String name, double average) {
            this.numMec = numMec;
            this.name = name;
            this.average = average;
        }
    }
}
