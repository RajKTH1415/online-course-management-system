package com.ocms.dtos;
import lombok.Data;
@Data
public class ReviewRequest {
    private Integer rating;
    private String comment;
}
