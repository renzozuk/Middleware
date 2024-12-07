package dev.renzozukeram.winter.patterns.basicRemoting.requestor;

import dev.renzozukeram.winter.annotations.*;
import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RemotingError;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestorImpl implements Requestor {

    private final Map<String, String> routes = new HashMap<>();

    public RequestorImpl(Class<?> clazz) {
    }

    @Override
    public Object invoke(String objectId, String operationName, Object... args) {

        String methodName = routes.get(operationName);

        if (methodName == null) {
            throw new RemotingError("No such operation: " + operationName);
        }

        // completar depois

        return null;
    }

    private Class<?>[] getParameterTypes(Object... args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
