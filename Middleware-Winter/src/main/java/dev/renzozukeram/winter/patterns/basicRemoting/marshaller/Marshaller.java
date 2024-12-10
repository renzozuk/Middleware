package dev.renzozukeram.winter.patterns.basicRemoting.marshaller;

public interface Marshaller {
    <T> String serialize(T value);
    <T> T deserialize(String value, Class<T> clazz);
}
