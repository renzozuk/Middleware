package dev.renzozukeram.winter.patterns.identification;

import dev.renzozukeram.winter.annotations.*;
import dev.renzozukeram.winter.enums.RequisitionType;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LookupService {

    private static final LookupService instance = new LookupService();

    private final Map<ObjectId, Class<?>> registeredClasses;
    private final Map<MethodIdentifier, Method> remoteObjectMethods;

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

    public Map<ObjectId, Class<?>> getRegisteredClasses() {
        return Map.copyOf(registeredClasses);
    }

    public Map<MethodIdentifier, Method> getRemoteObjectMethods() {
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
                remoteObjectMethods.put(new MethodIdentifier(RequisitionType.GET, method.getAnnotation(Get.class).value()), method);
            } else if (method.isAnnotationPresent(Post.class)) {
                remoteObjectMethods.put(new MethodIdentifier(RequisitionType.POST, method.getAnnotation(Post.class).value()), method);
            } else if (method.isAnnotationPresent(Put.class)) {
                remoteObjectMethods.put(new MethodIdentifier(RequisitionType.PUT, method.getAnnotation(Put.class).value()), method);
            } else if (method.isAnnotationPresent(Patch.class)) {
                remoteObjectMethods.put(new MethodIdentifier(RequisitionType.PATCH, method.getAnnotation(Patch.class).value()), method);
            } else if (method.isAnnotationPresent(Delete.class)) {
                remoteObjectMethods.put(new MethodIdentifier(RequisitionType.DELETE, method.getAnnotation(Delete.class).value()), method);
            }
        }
    }

    public void initializeRoutes(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            initializeRoutes(clazz);
        }
    }

    public void initializeRoutes(Collection<Class<?>> classes) {
        classes.forEach(this::initializeRoutes);
    }
}
