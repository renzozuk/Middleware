package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.annotations.*;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.Marshaller;
import dev.renzozukeram.winter.patterns.identification.lookup.LookupService;
import dev.renzozukeram.winter.message.HTTPMessage;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Invoker {

    private Marshaller marshaller;
    private final LookupService lookupService;

    public Invoker(LookupService lookupService) {
        this.lookupService = lookupService;
    }

//    public HTTPMessage invoke(HTTPMessage request) {
//
//        String fullRoute = ""; // change this later
//        String httpMethod = request.httpMethod();
//
//        Class<?> clazz = lookupService.getRoute(fullRoute);
//
//        Method targetMethod;
//    }
//
//    private String getRouteTemplate(Class<?> clazz, Method targetMethod) {
//        return clazz.getAnnotation(RequestMapping.class).value() + getMethodTemplate(targetMethod); // class template + method template
//    }
//
//    public String getMethodTemplate(Method targetMethod) {
//
//        var annotations = targetMethod.getAnnotations();
//
//        if (annotations.length != 1) {
//            throw new IllegalArgumentException("The method must have an http annotation.");
//        }
//
//        var annotation = annotations[0];
//
//        return switch (annotation) {
//            case Get get -> get.router();
//            case Post post -> post.router();
//            case Put put -> put.router();
//            case Patch patch -> patch.router();
//            case Delete delete -> delete.router();
//            default -> throw new IllegalStateException("Unknown HTTP annotation: " + annotation);
//        };
//    }
//
//    private Method findAnnotatedMethod(Class<?> clazz, String httpMethod, String fullRoute) {
//
//        String baseRoute = clazz.getAnnotation(RequestMapping.class).value();
//
//        if (baseRoute.endsWith("/") && fullRoute.startsWith("/")) {
//            fullRoute = fullRoute.substring(1);
//        }
//
//        String methodRoute = fullRoute.substring(baseRoute.length());
//        methodRoute = methodRoute.split("\\?")[0];
//
//        for (Method method : clazz.getDeclaredMethods()) {
//            if (matchesAnnotation(method, httpMethod, methodRoute)) {
//                return method;
//            }
//        }
//
//        return null;
//    }
//
//    private boolean matchesAnnotation(Method method, String httpMethod, String route) {
//
//        switch (httpMethod) {
//            case "GET":
//                if (method.isAnnotationPresent(Get.class)) {
//                    return matchesRoute(route, method.getAnnotation(Get.class).router());
//                }
//            case "POST":
//                if (method.isAnnotationPresent(Post.class)) {
//                    return matchesRoute(route, method.getAnnotation(Post.class).router());
//                }
//            case "PUT":
//                if (method.isAnnotationPresent(Put.class)) {
//                    return matchesRoute(route, method.getAnnotation(Put.class).router());
//                }
//            case "PATCH":
//                if (method.isAnnotationPresent(Patch.class)) {
//                    return matchesRoute(route, method.getAnnotation(Patch.class).router());
//                }
//            case "DELETE":
//                if (method.isAnnotationPresent(Delete.class)) {
//                    return matchesRoute(route, method.getAnnotation(Delete.class).router());
//                }
//        }
//
//        return false;
//    }
//
//    private boolean matchesRoute(String route, String routeTemplate) {
//
//        Pattern pattern = Pattern.compile(routeTemplate.replace("{", "(?<").replace("}", ">[a-zA-Z0-9]+)").replace("/", "\\/") + "$");
//
//        return pattern.matcher(route).matches();
//    }
}
