/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlertBusiness;

import AlertBusiness.Alerte;
import AlertBusiness.Alerte;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

/**
 *
 * @author gboyo
 */


class ObjectMapper {

    private Jsonb jsonb;

    public ObjectMapper() {
        JsonbConfig config = new JsonbConfig().withFormatting(true);
        this.jsonb = JsonbBuilder.create(config);
    }

    public Alerte readValue(String json, Class<Alerte> aClass) {
        try {
            return jsonb.fromJson(json, aClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }

    public byte[] writeValueAsBytes(Alerte alerte) {
        try {
            return jsonb.toJson(alerte).getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }
}
