package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.annotations.RequestBody;
import dev.renzozukeram.winter.enums.RequisitionType;
import dev.renzozukeram.winter.message.ResponseEntity;
import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RemotingError;
import dev.renzozukeram.winter.patterns.identification.AbsoluteObjectReference;
import dev.renzozukeram.winter.patterns.identification.LookupService;
import dev.renzozukeram.winter.patterns.identification.MethodIdentifier;
import dev.renzozukeram.winter.patterns.identification.ObjectId;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Invoker {

    public static ResponseEntity invoke(AbsoluteObjectReference reference, RequisitionType requisitionType) throws Exception {

        var lookupService = LookupService.getInstance();

        var remoteObject = lookupService.lookup(new ObjectId(reference.getFullReference().split("/")[2]));

        if (remoteObject == null) {
            throw new RemotingError("Remote object not found");
        }

        return (ResponseEntity) lookupService.getRemoteObjectMethods().get(new MethodIdentifier(requisitionType, "")).invoke(remoteObject);
    }

    public static ResponseEntity invoke(AbsoluteObjectReference reference, RequisitionType requisitionType, String routeName, Object[] args) throws Exception {

        var lookupService = LookupService.getInstance();

        var remoteObject = lookupService.lookup(new ObjectId(reference.getFullReference().split("/")[2]));

        if (remoteObject == null) {
            throw new RemotingError("Remote object not found");
        }

        for (var node : lookupService.getRemoteObjectMethods().entrySet()) {

            if (node.getKey().getRequisitionType().equals(requisitionType) && routeName.equals(node.getKey().getRoute().startsWith("/") ? node.getKey().getRoute().substring(1) : node.getKey().getRoute())) {

                node.getValue().setAccessible(true);

                var parametersWithRequestBody = Arrays.stream(node.getValue().getParameters()).filter(parameter -> !Arrays.stream(parameter.getAnnotations()).filter(annotation -> annotation.equals(RequestBody.class)).toList().isEmpty()).toList();

                if (parametersWithRequestBody.size() > 1) {
                    throw new RemotingError("Multiple @RequestBody annotations found");
                }

                try {
                    if (node.getValue().getParameterCount() > 0) {
                        return (ResponseEntity) node.getValue().invoke(remoteObject, args);
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
}
