package com.tinonio.ai.demoAI.prompts;

public final class StructuredTemplate {
    private StructuredTemplate(){}

    public static final String TEMPLATE = """
Esquema de salida (colócalo en tu plantilla o usa un OutputParser tipado):

{
  "category": "login|onboarding|transferencias|tarjetas|pagos_qr/contactless|bizum/p2p|cuentas|inversiones|hipotecas/prestamos|seguridad/fraude|usabilidad/ui|rendimiento/estabilidad|notificaciones|soporte/atencion|comisiones/precios|documentacion/certificados|version_app/compatibilidad|otro",
  "subcategory": "string|null",
  "journey_stage": "descarga|registro|kYC|primer_login|uso_diario|pago|inversion|soporte|desconocida",
  "platform": "android|ios|web|desconocida",
  "sentiment": "negativo|neutral|positivo",
  "urgency": 1,
  "impact_scope": "masivo|segmentado|aislado|desconocido",
  "app_component": "ui|api|sdk_tercero|push|biometria|pagos|observabilidad|base_datos|desconocido",
  "is_crash": false,
  "is_fraud_risk": false,
  "is_blocker": false,
  "duplicates_hint": "string|null",
  "summary": "string",
  "next_best_action": "abrir_bug|investigar_logs|contactar_cliente|mejora_ux|no_accion",
  "owner_squad": "app_core|login_kdc|pagos|tarjetas|plataforma|seguridad|inversiones|atencion_cliente|otros",
  "evidences": ["string", "string"]
}

Ejemplo 1 (entrada):
"Desde la última actualización no puedo entrar con huella. Me obliga a SMS cada vez y a veces ni llega. Pixel 7, Android 14."

Ejemplo 1 (salida):
{
  "category": "login",
  "subcategory": "biometria",
  "journey_stage": "uso_diario",
  "platform": "android",
  "sentiment": "negativo",
  "urgency": 4,
  "impact_scope": "segmentado",
  "app_component": "biometria",
  "is_crash": false,
  "is_fraud_risk": false,
  "is_blocker": true,
  "duplicates_hint": "biometria-no-funciona-android14",
  "summary": "Biometría no funciona tras update; OTP intermitente.",
  "next_best_action": "abrir_bug",
  "owner_squad": "login_kdc",
  "evidences": ["Android 14", "Pixel 7", "OTP no llega"]
}

Ejemplo 2 (entrada):
"La app va lenta al abrir transferencias, pero termina funcionando. iPhone 12, iOS 17.5."

Ejemplo 2 (salida):
{
  "category": "transferencias",
  "subcategory": "rendimiento_listado",
  "journey_stage": "uso_diario",
  "platform": "ios",
  "sentiment": "negativo",
  "urgency": 3,
  "impact_scope": "segmentado",
  "app_component": "api",
  "is_crash": false,
  "is_fraud_risk": false,
  "is_blocker": false,
  "duplicates_hint": "lento-transferencias-ios",
  "summary": "Lentitud al abrir transferencias, pero hay workaround.",
  "next_best_action": "investigar_logs",
  "owner_squad": "pagos",
  "evidences": ["iOS 17.5", "iPhone 12"]
}

Ejemplo 3 (entrada):
"Me cobraron comisión por cambio de divisa sin avisar. Todo lo demás bien."

Ejemplo 3 (salida):
{
  "category": "comisiones/precios",
  "subcategory": "cambio_divisa",
  "journey_stage": "uso_diario",
  "platform": "desconocida",
  "sentiment": "negativo",
  "urgency": 2,
  "impact_scope": "desconocido",
  "app_component": "desconocido",
  "is_crash": false,
  "is_fraud_risk": false,
  "is_blocker": false,
  "duplicates_hint": "comision-fx-no-avisada",
  "summary": "Queja por comisión FX no comunicada.",
  "next_best_action": "contactar_cliente",
  "owner_squad": "atencion_cliente",
  "evidences": ["Comisión FX"]
}

RESEÑA A CLASIFICAR:
{{review_text}}

RECUERDA: Devuelve SOLO el JSON, sin explicación adicional.
""";
}
