package com.tinonio.ai.demoAI.api;

import com.tinonio.ai.demoAI.model.AnalysisResponse;
import com.tinonio.ai.demoAI.model.GooglePlayReview;
import com.tinonio.ai.demoAI.model.ReviewRequest;
import com.tinonio.ai.demoAI.service.ReviewAiService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {


    private final ReviewAiService aiService;


    public ReviewController(ReviewAiService aiService) {
        this.aiService = aiService;
    }


    @PostMapping(path = "/analyze", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AnalysisResponse analyze(@RequestBody @Valid ReviewRequest request) {
        return aiService.analyze(request.getReview());
    }

    @PostMapping(path = "/analyze-google", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AnalysisResponse analyzeGoogle(@RequestBody GooglePlayReview payload) {
        return aiService.analyzeGoogleReview(payload);
    }


}
