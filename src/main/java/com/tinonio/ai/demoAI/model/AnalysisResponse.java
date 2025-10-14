package com.tinonio.ai.demoAI.model;

import java.util.List;

public class AnalysisResponse {
    public enum Sentiment { POSITIVE, NEUTRAL, NEGATIVE }
    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }


    private Sentiment sentiment;
    private String summary;
    private Severity severity;
    private List<String> recommendedActions;
    private boolean escalated; // calculado por la app


    public Sentiment getSentiment() { return sentiment; }
    public void setSentiment(Sentiment sentiment) { this.sentiment = sentiment; }


    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }


    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }


    public List<String> getRecommendedActions() { return recommendedActions; }
    public void setRecommendedActions(List<String> recommendedActions) { this.recommendedActions = recommendedActions; }


    public boolean isEscalated() { return escalated; }
    public void setEscalated(boolean escalated) { this.escalated = escalated; }
}
