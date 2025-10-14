package com.tinonio.ai.demoAI.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinonio.ai.demoAI.model.AnalysisResponse;
import com.tinonio.ai.demoAI.model.GooglePlayReview;
import com.tinonio.ai.demoAI.service.ReviewAiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ui")
public class ReviewWebController {

    private final ReviewAiService aiService;
    private final ObjectMapper objectMapper;

    public ReviewWebController(ReviewAiService aiService, ObjectMapper objectMapper) {
        this.aiService = aiService;
        this.objectMapper = objectMapper;
    }

    // GET /ui -> render de la página
    @GetMapping
    public String page() {
        return "reviews"; // busca templates/reviews.html
    }

    // POST /ui/analyze -> devuelve el fragmento (card)
    @PostMapping("/analyze")
    public String analyze(@RequestParam("review") String reviewText, Model model) {
        AnalysisResponse result = aiService.analyze(reviewText);
        model.addAttribute("originalReview", reviewText);
        model.addAttribute("result", result);
        return "fragments/review-card :: card";
    }

    @PostMapping("/analyze-google")
    public String analyzeGoogleJson(@RequestParam("reviewJson") String reviewJson, Model model) {
        try {
            GooglePlayReview gr = objectMapper.readValue(reviewJson, GooglePlayReview.class);
            AnalysisResponse result = aiService.analyzeGoogleReview(gr);

            // Usamos `text` u `originalText` para mostrar en la tarjeta
            String displayText = (gr.getOriginalText() != null && !gr.getOriginalText().isBlank())
                    ? gr.getOriginalText()
                    : (gr.getText() == null ? "" : gr.getText());

            model.addAttribute("originalReview", displayText);
            model.addAttribute("result", result);
            model.addAttribute("googleMeta", gr); // pasamos metadatos a la vista
            return "fragments/review-card-google :: card";
        } catch (Exception e) {
            model.addAttribute("message", "JSON de Google Play inválido: " + e.getMessage());
            return "fragments/error-card :: card";
        }
    }
}
