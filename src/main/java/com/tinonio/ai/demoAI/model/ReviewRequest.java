package com.tinonio.ai.demoAI.model;

import jakarta.validation.constraints.NotBlank;

public class ReviewRequest {
    @NotBlank
    private String review;


    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
}
