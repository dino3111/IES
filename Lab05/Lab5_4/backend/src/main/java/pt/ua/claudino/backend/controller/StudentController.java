package pt.ua.claudino.backend.controller;

import org.springframework.web.bind.annotation.*;
import pt.ua.claudino.backend.model.Student;
import pt.ua.claudino.backend.repository.StudentRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
}
