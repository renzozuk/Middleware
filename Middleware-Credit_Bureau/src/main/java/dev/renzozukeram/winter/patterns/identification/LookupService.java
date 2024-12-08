package dev.renzozukeram.winter.patterns.identification;

import dev.renzozukeram.winter.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LookupService {

    private static final LookupService instance = new LookupService();

    private final Map<ObjectId, Object> registeredObjects;
    private final Map<String, Method> remoteObjectMethods;

    private LookupService() {
        registeredObjects = new ConcurrentHashMap<>();
        remoteObjectMethods = new HashMap<>();
    }

    public static LookupService getInstance() {
        if (instance == null) {
            return new LookupService();
        }

        return instance;
    }

    public void register(ObjectId id, Object remoteObject) {
        registeredObjects.put(id, remoteObject);
    }

    public Object lookup(ObjectId id) {
        return registeredObjects.get(id);
    }

    public void initializeRoutes(Class<?> clazz) {

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Get.class)) {
                remoteObjectMethods.put(method.getAnnotation(Get.class).value(), method);
            } else if (method.isAnnotationPresent(Post.class)) {
                remoteObjectMethods.put(method.getAnnotation(Post.class).value(), method);
            } else if (method.isAnnotationPresent(Put.class)) {
                remoteObjectMethods.put(method.getAnnotation(Put.class).value(), method);
            } else if (method.isAnnotationPresent(Patch.class)) {
                remoteObjectMethods.put(method.getAnnotation(Patch.class).value(), method);
            } else if (method.isAnnotationPresent(Delete.class)) {
                remoteObjectMethods.put(method.getAnnotation(Delete.class).value(), method);
            }
        }
    }
}
