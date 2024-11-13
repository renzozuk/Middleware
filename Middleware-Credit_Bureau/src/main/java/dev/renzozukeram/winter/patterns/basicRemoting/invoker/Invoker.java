package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.annotations.AnnotationValidator;
import dev.renzozukeram.winter.model.entities.CreditBureau;
import dev.renzozukeram.winter.patterns.basicRemoting.interfaceDescription.InvokerInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class Invoker implements InvokerInterface {

    public static String invoke(String method, String[] parameters, CreditBureau creditBureau) throws InvocationTargetException, IllegalAccessException {

        Map<String, Method> methods = AnnotationValidator.registerMethods(creditBureau);

        if (parameters.length == 0) {
            throw new RuntimeException("No parameters given");
        }

        switch (parameters[0]) {
            case "create":
                methods.get(method).invoke(creditBureau, parameters[1]);
            case "delete":
                methods.get(method).invoke(creditBureau, parameters[1]);
        }

        return "";
    }
}
