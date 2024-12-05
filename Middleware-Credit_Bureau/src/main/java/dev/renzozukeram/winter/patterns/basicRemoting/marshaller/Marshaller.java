package dev.renzozukeram.winter.patterns.basicRemoting.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Marshaller {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> String serialize(T value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }

    public static <T> T deserialize(String value, Class<T> clazz) {
        try {
            return mapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }
}
