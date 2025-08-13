package com.ocms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String videoUrl; // accept video link or pdfPath
    private Integer duration; // minutes

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}