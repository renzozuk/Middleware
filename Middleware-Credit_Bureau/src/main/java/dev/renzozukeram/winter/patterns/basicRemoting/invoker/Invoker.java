package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RemotingError;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.JsonMarshaller;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.Marshaller;
import dev.renzozukeram.winter.patterns.identification.AbsoluteObjectReference;
import dev.renzozukeram.winter.patterns.identification.LookupService;
import dev.renzozukeram.winter.patterns.identification.ObjectId;

import java.lang.reflect.InvocationTargetException;

public class Invoker {

    private static final Marshaller marshaller = new JsonMarshaller();

    public static <T> String invoke(AbsoluteObjectReference reference, String routeName, Object[] args) throws Exception {

        var lookupService = LookupService.getInstance();

        var remoteObject = lookupService.lookup(new ObjectId(reference.getFullReference().split("/")[2]));

        if (remoteObject == null) {
            throw new RemotingError("Remote object not found");
        }

        for (var node : lookupService.getRemoteObjectMethods().entrySet()) {

            if (routeName.equals(node.getKey().startsWith("/") ? node.getKey().substring(1) : node.getKey())) {
                node.getValue().setAccessible(true);

                try {
                    if (node.getValue().getParameterCount() > 0) {
                        return marshaller.serialize(node.getValue().invoke(remoteObject, args));
                    } else {
                        return marshaller.serialize(node.getValue().invoke(remoteObject));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        throw new RemotingError("Method not found");
    }
}
