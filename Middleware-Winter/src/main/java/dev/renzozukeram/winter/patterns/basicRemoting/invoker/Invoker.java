package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.annotations.RequestBody;
import dev.renzozukeram.winter.enums.RequisitionType;
import dev.renzozukeram.winter.message.ResponseEntity;
import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RemotingError;
import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.UnexpectedArgumentException;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.JsonMarshaller;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.Marshaller;
import dev.renzozukeram.winter.patterns.identification.LookupService;
import dev.renzozukeram.winter.patterns.identification.MethodIdentifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Invoker {

    private static final Marshaller marshaller = new JsonMarshaller();
    private static final LookupService lookupService = LookupService.getInstance();


    public static ResponseEntity invoke(Object remoteObject, RequisitionType requisitionType) throws Exception {
        return (ResponseEntity) lookupService.getRemoteObjectMethods().get(new MethodIdentifier(requisitionType, "")).invoke(remoteObject);
    }

    public static ResponseEntity invoke(Object remoteObject, RequisitionType requisitionType, String body) throws Exception {

        var method = lookupService.getRemoteObjectMethods().get(new MethodIdentifier(requisitionType, ""));

        var arg = marshaller.deserialize(body, method.getParameterTypes()[0]);

        return (ResponseEntity) method.invoke(remoteObject, arg);
    }

    public static ResponseEntity invoke(Object remoteObject, RequisitionType requisitionType, String routeName, Object[] args) {

        for (var node : lookupService.getRemoteObjectMethods().entrySet()) {

            if (node.getKey().getRequisitionType().equals(requisitionType) && routeName.equals(node.getKey().getRoute().startsWith("/") ? node.getKey().getRoute().substring(1) : node.getKey().getRoute())) {

                node.getValue().setAccessible(true);

                var parametersWithRequestBody = Arrays.stream(node.getValue().getParameters()).filter(parameter -> !Arrays.stream(parameter.getAnnotations()).filter(annotation -> annotation.equals(RequestBody.class)).toList().isEmpty()).toList();

                if (parametersWithRequestBody.size() > 1) {
                    throw new RemotingError("Multiple @RequestBody annotations found");
                }

                var parsedArgs = parseArgs(node.getValue(), args);

                try {
                    if (node.getValue().getParameterCount() > 0) {
                        return (ResponseEntity) node.getValue().invoke(remoteObject, parsedArgs);
                    } else {
                        return (ResponseEntity) node.getValue().invoke(remoteObject);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RemotingError("Method not found");
    }

    public static ResponseEntity invoke(Object remoteObject, RequisitionType requisitionType, String routeName, Object[] args, String body) {

        for (var node : lookupService.getRemoteObjectMethods().entrySet()) {

            if (node.getKey().getRequisitionType().equals(requisitionType) && routeName.equals(node.getKey().getRoute().startsWith("/") ? node.getKey().getRoute().substring(1) : node.getKey().getRoute())) {

                node.getValue().setAccessible(true);

                var parametersWithRequestBody = Arrays.stream(node.getValue().getParameters()).filter(parameter -> !Arrays.stream(parameter.getAnnotations()).filter(annotation -> annotation.equals(RequestBody.class)).toList().isEmpty()).toList();

                if (parametersWithRequestBody.size() > 1) {
                    throw new RemotingError("Multiple @RequestBody annotations found");
                }

                var parsedArgs = parseArgs(node.getValue(), args, body);

                try {
                    if (node.getValue().getParameterCount() > 0) {
                        return (ResponseEntity) node.getValue().invoke(remoteObject, parsedArgs);
                    } else {
                        return (ResponseEntity) node.getValue().invoke(remoteObject);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RemotingError("Method not found");
    }

    private static Object[] parseArgs(Method method, Object[] oldArgs) {

        List<Object> newArgs = new ArrayList<>();

        for (int i = 0; i < method.getParameterCount(); i++) {
            parseArg(method, oldArgs, newArgs, i, false);
        }

        return newArgs.toArray();
    }

    private static Object[] parseArgs(Method method, Object[] oldArgs, String jsonBody) {

        List<Object> newArgs = new ArrayList<>();

        boolean requestBodyAdded = false;

        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].isAnnotationPresent(RequestBody.class)) {
                newArgs.add(marshaller.deserialize(jsonBody, method.getParameterTypes()[i]));
                requestBodyAdded = true;
            } else {
                parseArg(method, oldArgs, newArgs, i, requestBodyAdded);
            }
        }

        return newArgs.toArray();
    }

    private static void parseArg(Method method, Object[] args, List<Object> newArgs, int currentIndex, boolean requestBodyAdded) {

        var searchIndex = requestBodyAdded ? currentIndex - 1 : currentIndex;

        switch (method.getParameterTypes()[currentIndex].getSimpleName()) {
            case "boolean", "Boolean":
                var argContent = (String) args[searchIndex];
                if (!argContent.equalsIgnoreCase("true") && !argContent.equalsIgnoreCase("false")) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), argContent);
                }
                newArgs.add(Boolean.parseBoolean(argContent.toLowerCase()));
                break;
            case "byte", "Byte":
                try {
                    newArgs.add(Byte.parseByte((String) args[searchIndex]));
                } catch (NumberFormatException e) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), (String) args[searchIndex]);
                }
                break;
            case "char", "Character":
                if (((String) args[searchIndex]).length() != 1) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), (String) args[searchIndex]);
                }
                newArgs.add(((String) args[searchIndex]).charAt(0));
                break;
            case "double", "Double":
                try {
                    newArgs.add(Double.parseDouble((String) args[searchIndex]));
                } catch (NumberFormatException e) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), (String) args[searchIndex]);
                }
                break;
            case "float", "Float":
                try {
                    newArgs.add(Float.parseFloat((String) args[searchIndex]));
                } catch (NumberFormatException e) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), (String) args[searchIndex]);
                }
                break;
            case "int", "Integer":
                try {
                    newArgs.add(Integer.parseInt((String) args[searchIndex]));
                } catch (NumberFormatException e) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), (String) args[searchIndex]);
                }
                break;
            case "long", "Long":
                try {
                    newArgs.add(Long.parseLong((String) args[searchIndex]));
                } catch (NumberFormatException e) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), (String) args[searchIndex]);
                }
                break;
            case "short", "Short":
                try {
                    newArgs.add(Short.parseShort((String) args[searchIndex]));
                } catch (NumberFormatException e) {
                    throw new UnexpectedArgumentException(method.getParameterTypes()[currentIndex].getSimpleName(), (String) args[searchIndex]);
                }
                break;
            default:
                newArgs.add(args[searchIndex]);
        }
    }
}
