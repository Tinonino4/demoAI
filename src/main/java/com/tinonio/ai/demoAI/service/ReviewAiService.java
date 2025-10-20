package com.tinonio.ai.demoAI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinonio.ai.demoAI.exception.ValidationException;
import com.tinonio.ai.demoAI.model.AnalysisResponse;
import com.tinonio.ai.demoAI.model.ClassificationResponse;
import com.tinonio.ai.demoAI.model.GooglePlayReview;
import com.tinonio.ai.demoAI.prompts.StructuredTemplate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ReviewAiService {
    private final ChatClient chatClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public ReviewAiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    private static final String SYSTEM_INSTRUCTIONS = """
        Eres un clasificador experto de reseñas de una app bancaria.\s
        Objetivo: transformar una reseña libre en un JSON ESTRICTO que siga el esquema dado.
        REGLAS:
        - No alucines: si un campo no aparece, infiérelo solo si es evidente, si no marca "desconocida" o null.
        - "urgency": 5 si impide operar (no login, no transferir), 4 si rompe funciones clave intermitentes, 3 si degrada la experiencia pero hay workaround, 2 si menor, 1 si cosmético.
        - "sentiment": según tono general, ignora ironías salvo evidencia.
        - "impact_scope": "masivo" si el texto sugiere que “muchos”/“todos” o tras actualización general; "segmentado" si parece afectar a un dispositivo/SO/versión; "aislado" si personal.
        - Salida: SOLO JSON válido, sin comentarios, sin texto extra.
    """;

    public AnalysisResponse analyze(String reviewText) {
        var system = new SystemMessage(SYSTEM_INSTRUCTIONS);
        var user = new UserMessage("REVIEW:\n" + reviewText);
        var prompt = new Prompt(system, user);


        // Call to model and mapping to AnalysisResponse
        AnalysisResponse aiResult = chatClient
                .prompt(prompt)
                .call()
                .entity(AnalysisResponse.class);

        // Business logic: decide if it should be escalated
        applyScalationRule(aiResult);

        return aiResult;
    }

    public AnalysisResponse analyzeGoogleReview(GooglePlayReview gr) {
        // 1) Derivar el texto “útil” (si hay originalText, úsalo)
        String text = (gr.getOriginalText() != null && !gr.getOriginalText().isBlank())
                ? gr.getOriginalText()
                : (gr.getText() == null ? "" : gr.getText());

        // 2) Construir un prompt con contexto adicional (estrella, versión, OS, etc.)
        StringBuilder ctx = new StringBuilder();
        ctx.append("REVIEW (Google Play):\n").append(text).append("\n\n");
        if (gr.getStarRating() != null) ctx.append("starRating: ").append(gr.getStarRating()).append("\n");
        if (gr.getReviewerLanguage() != null) ctx.append("reviewerLanguage: ").append(gr.getReviewerLanguage()).append("\n");
        if (gr.getAppVersionName() != null) ctx.append("appVersionName: ").append(gr.getAppVersionName()).append("\n");
        if (gr.getAppVersionCode() != null) ctx.append("appVersionCode: ").append(gr.getAppVersionCode()).append("\n");
        if (gr.getAndroidOsVersion() != null) ctx.append("androidOsVersion: ").append(gr.getAndroidOsVersion()).append("\n");
        if (gr.getDevice() != null) ctx.append("device: ").append(gr.getDevice()).append("\n");
        if (gr.getThumbsUpCount() != null) ctx.append("thumbsUpCount: ").append(gr.getThumbsUpCount()).append("\n");
        if (gr.getThumbsDownCount() != null) ctx.append("thumbsDownCount: ").append(gr.getThumbsDownCount()).append("\n");
        if (gr.getLastModified() != null) ctx.append("lastModified.seconds: ").append(gr.getLastModified().getSeconds()).append("\n");

        var result = callLlm(ctx.toString());

        // 3) Heurística extra basada en starRating (opcional, negocio local)
        if (gr.getStarRating() != null) {
            int stars = gr.getStarRating();
            // forzar severidad mínima por estrellas bajas
            switch (stars) {
                case 1 -> {
                    if (result.getSeverity() == null || result.getSeverity().ordinal() < AnalysisResponse.Severity.HIGH.ordinal()) {
                        result.setSeverity(AnalysisResponse.Severity.HIGH);
                    }
                }
                case 2 -> {
                    if (result.getSeverity() == null || result.getSeverity().ordinal() < AnalysisResponse.Severity.MEDIUM.ordinal()) {
                        result.setSeverity(AnalysisResponse.Severity.MEDIUM);
                    }
                }
                default -> { /* no-op */ }
            }
        }

        applyScalationRule(result);
        return result;
    }

    private AnalysisResponse callLlm(String userPayload) {
        var system = new SystemMessage(SYSTEM_INSTRUCTIONS);
        var user = new UserMessage(userPayload);
        var prompt = new Prompt(system, user);

        // Llamada y guardrail mínimo (parseo)
        String raw = chatClient.prompt(prompt).call().content();
        AnalysisResponse dto;
        try {
            dto = mapper.readValue(raw, AnalysisResponse.class);
        } catch (Exception e) {
            throw new ValidationException("El modelo no devolvió JSON válido del esquema esperado.");
        }
        if (dto.getSentiment() == null || dto.getSeverity() == null || dto.getSummary() == null || dto.getSummary().isBlank()) {
            throw new ValidationException("Faltan campos obligatorios en la salida del modelo.");
        }
        return dto;
    }

    private static void applyScalationRule(AnalysisResponse aiResult) {
        boolean shouldEscalate = aiResult.getSentiment() == AnalysisResponse.Sentiment.NEGATIVE
                && (aiResult.getSeverity() == AnalysisResponse.Severity.HIGH
                || aiResult.getSeverity() == AnalysisResponse.Severity.CRITICAL);
        aiResult.setEscalated(shouldEscalate);
    }

    public ClassificationResponse classifyStructured(String reviewText) {
        var system = new SystemMessage("Eres un analista CX bancario. Sigue estrictamente las instrucciones.");
        var user = new UserMessage(StructuredTemplate.TEMPLATE.replace("{{review_text}}", reviewText));
        var prompt = new Prompt(system, user);

        String raw = chatClient.prompt(prompt).call().content();

        ClassificationResponse dto;
        try {
            dto = mapper.readValue(raw, ClassificationResponse.class);
        } catch (Exception e) {
            throw new ValidationException("Salida del modelo no cumple el esquema tipado.");
        }

        // Validaciones mínimas extra (ejemplo)
        if (dto.getEvidences() == null || dto.getEvidences().isEmpty()) {
            throw new ValidationException("Faltan evidences.");
        }
        if (dto.getUrgency() == null || dto.getUrgency() < 1) {
            throw new ValidationException("Urgency inválida.");
        }
        return dto;
    }


}
