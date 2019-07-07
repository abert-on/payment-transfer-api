package com.revolut.util;

import com.google.gson.*;
import com.revolut.account.payment.Payment;

import java.lang.reflect.Type;

public class JsonUtil {

    private static Gson gson = new GsonBuilder().registerTypeAdapter(Payment.class, new PaymentDeserializer()).create();

    public static String objectToJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T jsonToObject(String json, Class<T> toClass) {
        return gson.fromJson(json, toClass);
    }

    /**
     * Custom deserializer for {@link Payment} so that new instances are created with a UUID
     */
    static class PaymentDeserializer implements JsonDeserializer<Payment> {

        @Override
        public Payment deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Payment(jsonElement.getAsJsonObject().get("accountNumber").getAsString(),
                    jsonElement.getAsJsonObject().get("sortCode").getAsString(),
                    jsonElement.getAsJsonObject().get("amount").getAsBigDecimal());
        }
    }
}
