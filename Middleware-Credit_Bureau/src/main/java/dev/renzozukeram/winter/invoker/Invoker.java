package dev.renzozukeram.winter.invoker;

import dev.renzozukeram.winter.lookup.LookupService;
import dev.renzozukeram.winter.message.HTTPMessage;

import java.lang.reflect.Method;

public class Invoker {

    private final LookupService lookupService;

    public Invoker(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public HTTPMessage invoke(HTTPMessage request) {

        String fullRoute = ""; // change this later
        String httpMethod = request.httpMethod();

        Class<?> clazz = lookupService.getRoute(fullRoute);

        Method targetMethod;
    }
}
