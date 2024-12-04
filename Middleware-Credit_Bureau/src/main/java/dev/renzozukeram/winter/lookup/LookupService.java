package dev.renzozukeram.winter.lookup;

import dev.renzozukeram.winter.annotations.RequestMapping;

import java.util.HashMap;
import java.util.Map;

public class LookupService {
    private Map<String, Class<?>> routes;

    public LookupService() {
        routes = new HashMap<>();
    }

    public void registerRouter(Class<?> clazz) {

        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
            String route = annotation.value();
            routes.put(route, clazz);
        }
    }

    public Class<?> getRoute(String route) {
        return routes.get(route);
    }
}
