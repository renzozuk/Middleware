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

    private void initializeRoutes(Class<?> clazz) {

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Get.class)) {
                routes.put(method.getAnnotation(Get.class).router(), method.getName());
            } else if (method.isAnnotationPresent(Post.class)) {
                routes.put(method.getAnnotation(Post.class).router(), method.getName());
            } else if (method.isAnnotationPresent(Put.class)) {
                routes.put(method.getAnnotation(Put.class).router(), method.getName());
            } else if (method.isAnnotationPresent(Patch.class)) {
                routes.put(method.getAnnotation(Patch.class).router(), method.getName());
            } else if (method.isAnnotationPresent(Delete.class)) {
                routes.put(method.getAnnotation(Delete.class).router(), method.getName());
            }
        }
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
