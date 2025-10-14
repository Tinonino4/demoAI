package com.tinonio.ai.demoAI.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GooglePlayReview {

    private String text;               // concatenado título + cuerpo (si aplica)
    private String originalText;       // si hay traducción, aquí el original
    private Timestamp lastModified;
    private Integer starRating;        // 1..5
    private String reviewerLanguage;   // ej: "es", "en-US"
    private String device;             // ej: "klte"
    private Integer androidOsVersion;  // ej: 23
    private Integer appVersionCode;
    private String appVersionName;
    private Integer thumbsUpCount;
    private Integer thumbsDownCount;
    // private DeviceMetadata deviceMetadata;

    @Data
    public static class Timestamp {
        private String seconds;  // o long si prefieres
        private Integer nanos;   // opcional
    }

}
