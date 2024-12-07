package dev.renzozukeram.winter.patterns.identification;

import dev.renzozukeram.winter.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LookupService {
    // singleton
    private final Map<ObjectId, Object> registeredObjects = new ConcurrentHashMap<>();
    private final Map<String, Method> remoteObjectMethods = new HashMap<>();

    public void register(ObjectId id, Object remoteObject) {
        registeredObjects.put(id, remoteObject);
    }

    public Object lookup(ObjectId id) {
        return registeredObjects.get(id);
    }

    public void initializeRoutes(Class<?> clazz) {

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Get.class)) {
                remoteObjectMethods.put(method.getAnnotation(Get.class).router(), method);
            } else if (method.isAnnotationPresent(Post.class)) {
                remoteObjectMethods.put(method.getAnnotation(Post.class).router(), method);
            } else if (method.isAnnotationPresent(Put.class)) {
                remoteObjectMethods.put(method.getAnnotation(Put.class).router(), method);
            } else if (method.isAnnotationPresent(Patch.class)) {
                remoteObjectMethods.put(method.getAnnotation(Patch.class).router(), method);
            } else if (method.isAnnotationPresent(Delete.class)) {
                remoteObjectMethods.put(method.getAnnotation(Delete.class).router(), method);
            }
        }
    }
}
