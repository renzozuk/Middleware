package dev.renzozukeram.winter.annotations;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationValidator {

    public static Map<String, Method> registerMethods(Object obj) {

        Map<String, Method> methods = new HashMap<>();

        Class<?> clazz = obj.getClass();

        for (Method method : clazz.getMethods()) {

            if (method.isAnnotationPresent(Delete.class)) {
                method.setAccessible(true);
                methods.put(method.getName(), method);

//                String router = clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Delete.class).router();
            } else if (method.isAnnotationPresent(Get.class)) {
                method.setAccessible(true);
                methods.put(method.getName(), method);

//                String router = clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Get.class).router();
            } else if (method.isAnnotationPresent(Patch.class)) {
                method.setAccessible(true);
                methods.put(method.getName(), method);

//                String router = clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Patch.class).router();
            } else if (method.isAnnotationPresent(Post.class)) {
                method.setAccessible(true);
                methods.put(method.getName(), method);

//                String router = clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Post.class).router();
            } else if (method.isAnnotationPresent(Put.class)) {
                method.setAccessible(true);
                methods.put(method.getName(), method);

//                String router = clazz.getAnnotation(RequestMap.class).router() + method.getAnnotation(Put.class).router();
            }
        }

        return methods;
    }
}
