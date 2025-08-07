package com.ocms.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CourseRequest{

    private String title;
    private String description;
    private String category;
    private String level;
    private Double price;
    private List<LessonRequest> lessons;
}
