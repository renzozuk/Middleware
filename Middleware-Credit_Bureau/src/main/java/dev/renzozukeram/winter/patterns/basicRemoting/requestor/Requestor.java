package dev.renzozukeram.winter.patterns.basicRemoting.requestor;

public interface Requestor {
    Object invoke(String objectId, String operationName, Object... args);
}
