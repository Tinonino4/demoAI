package com.tinonio.ai.demoAI.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ClassificationResponse {

    public enum Category {
        login, onboarding, transferencias, tarjetas, pagos_qr_contactless, bizum_p2p,
        cuentas, inversiones, hipotecas_prestamos, seguridad_fraude, usabilidad_ui,
        rendimiento_estabilidad, notificaciones, soporte_atencion, comisiones_precios,
        documentacion_certificados, version_app_compatibilidad, otro;

        @JsonCreator
        public static Category from(String v) {
            if (v == null) return null;
            return switch (v.toLowerCase().replace("/", "_").replace("-", "_")) {
                case "pagos_qr/contactless" -> pagos_qr_contactless;
                case "bizum/p2p" -> bizum_p2p;
                case "hipotecas/prestamos" -> hipotecas_prestamos;
                case "seguridad/fraude" -> seguridad_fraude;
                case "usabilidad/ui" -> usabilidad_ui;
                case "rendimiento/estabilidad" -> rendimiento_estabilidad;
                case "soporte/atencion" -> soporte_atencion;
                case "comisiones/precios" -> comisiones_precios;
                case "documentacion/certificados" -> documentacion_certificados;
                case "version_app/compatibilidad" -> version_app_compatibilidad;
                default -> Category.valueOf(v.replace("-", "_").replace("/", "_").toLowerCase());
            };
        }

        @JsonValue
        public String toJson() {
            return switch (this) {
                case pagos_qr_contactless -> "pagos_qr/contactless";
                case bizum_p2p -> "bizum/p2p";
                case hipotecas_prestamos -> "hipotecas/prestamos";
                case seguridad_fraude -> "seguridad/fraude";
                case usabilidad_ui -> "usabilidad/ui";
                case rendimiento_estabilidad -> "rendimiento/estabilidad";
                case soporte_atencion -> "soporte/atencion";
                case comisiones_precios -> "comisiones/precios";
                case documentacion_certificados -> "documentacion/certificados";
                case version_app_compatibilidad -> "version_app/compatibilidad";
                default -> name();
            };
        }
    }

    public enum JourneyStage { descarga, registro, kyc, primer_login, uso_diario, pago, inversion, soporte, desconocida }
    public enum Platform { android, ios, web, desconocida }
    public enum Sentiment { negativo, neutral, positivo }
    public enum ImpactScope { masivo, segmentado, aislado, desconocido }
    public enum AppComponent { ui, api, sdk_tercero, push, biometria, pagos, observabilidad, base_datos, desconocido }
    public enum NextBestAction { abrir_bug, investigar_logs, contactar_cliente, mejora_ux, no_accion }
    public enum OwnerSquad { app_core, login_kdc, pagos, tarjetas, plataforma, seguridad, inversiones, atencion_cliente, otros }

    @JsonProperty("category")
    private Category category;

    @JsonProperty("subcategory")
    private String subcategory;

    @JsonProperty("journey_stage")
    private JourneyStage journeyStage;

    @JsonProperty("platform")
    private Platform platform;

    @JsonProperty("sentiment")
    private Sentiment sentiment;

    @JsonProperty("urgency")
    private Integer urgency;

    @JsonProperty("impact_scope")
    private ImpactScope impactScope;

    @JsonProperty("app_component")
    private AppComponent appComponent;

    // ⚠️ Renombrados y mapeados:
    @JsonProperty("is_crash")
    private boolean crash;

    @JsonProperty("is_fraud_risk")
    private boolean fraudRisk;

    @JsonProperty("is_blocker")
    private boolean blocker;

    @JsonProperty("duplicates_hint")
    private String duplicatesHint;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("next_best_action")
    private NextBestAction nextBestAction;

    @JsonProperty("owner_squad")
    private OwnerSquad ownerSquad;

    @JsonProperty("evidences")
    private java.util.List<String> evidences;
}
