package com.ocms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "student_id", nullable = false)
    //@JsonBackReference
    private User student;

    @ManyToOne @JoinColumn(name = "course_id", nullable = false)
    //@JsonBackReference
    private Course course;

    private boolean paymentDone = false;

    @ElementCollection
    @CollectionTable(name = "enrollment_completed_lessons", joinColumns = @JoinColumn(name = "enrollment_id"))
    @Column(name = "lesson_id")
    private Set<Long> completedLessonIds = new HashSet<>();
}
