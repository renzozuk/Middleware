package dev.renzozukeram.winter.util;

import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.MarshallerImpl;

public class Mapper {

    public static Object mapToType(String value, Class<?> targetType) {

        if (value == null) {
            return null;
        }

        return switch (targetType.getName()) {
            case "java.lang.String" -> value;
            case "java.lang.Integer" -> Integer.parseInt(value);
            case "java.lang.Long" -> Long.parseLong(value);
            case "java.lang.Double" -> Double.parseDouble(value);
            case "java.lang.Boolean" -> Boolean.parseBoolean(value);
            default -> MarshallerImpl.deserialize(value, targetType);
        };
    }
}
