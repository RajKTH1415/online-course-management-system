package com.ocms.entity;

import com.ocms.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    private String description;
    private String category;
    private String level;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.PENDING;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
