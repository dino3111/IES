package pt.ua.claudino.lab3_3;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique ID (Primary Key)

    @Column(name = "full_name", nullable = false)
    private String name; // Non-empty name

    @Column(nullable = false, unique = true)
    private String email; // Unique email
}