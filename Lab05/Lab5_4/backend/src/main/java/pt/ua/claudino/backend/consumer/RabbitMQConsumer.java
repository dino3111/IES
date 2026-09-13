package pt.ua.claudino.backend.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pt.ua.claudino.backend.config.RabbitMQConfig;
import pt.ua.claudino.backend.model.Course;
import pt.ua.claudino.backend.model.Grade;
import pt.ua.claudino.backend.model.Student;
import pt.ua.claudino.backend.repository.CourseRepository;
import pt.ua.claudino.backend.repository.GradeRepository;
import pt.ua.claudino.backend.repository.StudentRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class RabbitMQConsumer {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public RabbitMQConsumer(StudentRepository studentRepository, 
                            GradeRepository gradeRepository, 
                            CourseRepository courseRepository,
                            SimpMessagingTemplate messagingTemplate) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.courseRepository = courseRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.STUDENT_QUEUE)
    public void consumeStudent(Student student) {
        Student savedStudent = studentRepository.findByEmail(student.getEmail())
                .orElseGet(() -> studentRepository.save(student));
        
        messagingTemplate.convertAndSend("/topic/students", savedStudent);
    }

    @RabbitListener(queues = RabbitMQConfig.GRADE_QUEUE)
    public void consumeGrade(Map<String, Object> gradeData) {
        String studentEmail = (String) gradeData.get("studentEmail");
        String courseName = (String) gradeData.get("courseName");
        String courseCode = (String) gradeData.get("courseCode");
        Double value = Double.valueOf(gradeData.get("value").toString());

        Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            
            // Get or Create Course
            Course course = courseRepository.findByName(courseName)
                    .orElseGet(() -> courseRepository.save(new Course(courseName, courseCode)));

            Grade grade = new Grade(value, student, course);
            Grade savedGrade = gradeRepository.save(grade);
            
            messagingTemplate.convertAndSend("/topic/grades", Map.of(
                    "studentId", student.getId(),
                    "grade", savedGrade
            ));
        }
    }
}
