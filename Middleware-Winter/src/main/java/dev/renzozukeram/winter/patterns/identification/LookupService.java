package dev.renzozukeram.winter.patterns.identification;

import dev.renzozukeram.winter.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LookupService {

    private static final LookupService instance = new LookupService();

    private final Map<ObjectId, Class<?>> registeredClasses;
    private final Map<String, Method> remoteObjectMethods;

    private LookupService() {
        registeredClasses = new ConcurrentHashMap<>();
        remoteObjectMethods = new HashMap<>();
    }

    public static LookupService getInstance() {
        if (instance == null) {
            return new LookupService();
        }

        return instance;
    }

    public Map<String, Method> getRemoteObjectMethods() {
        return Map.copyOf(remoteObjectMethods);
    }

    private void register(ObjectId id, Class<?> remoteObject) {
        registeredClasses.put(id, remoteObject);
    }

    public Object lookup(ObjectId id) throws Exception {
        return registeredClasses.get(id).getDeclaredConstructor().newInstance();
    }

    public void initializeRoutes(Class<?> clazz) {

        register(new ObjectId(clazz.getAnnotation(RequestMapping.class).value().startsWith("/") ?
                clazz.getAnnotation(RequestMapping.class).value().substring(1) :
                clazz.getAnnotation(RequestMapping.class).value()), clazz);

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
