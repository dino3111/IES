package org.example.lab5_3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageGrade {
    private String studentId;
    private double grade;
    private String subject;
}
