package com.ocms.dtos;
import lombok.Data;
@Data
public class LessonRequest {
    private String title;
    private String videoUrl;
    private Integer duration;
}
