package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RemotingError;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.JsonMarshaller;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.Marshaller;
import dev.renzozukeram.winter.patterns.identification.AbsoluteObjectReference;
import dev.renzozukeram.winter.patterns.identification.LookupService;
import dev.renzozukeram.winter.patterns.identification.ObjectId;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {

    private static final Marshaller marshaller = new JsonMarshaller();

    public static Object invoke(AbsoluteObjectReference reference, String methodName, Object[] args) throws InvocationTargetException, IllegalAccessException {

        var lookupService = LookupService.getInstance();

        System.out.println(reference.getFullReference());

        var remoteObject = lookupService.lookup(new ObjectId(reference.getFullReference().split("/")[2]));

        if (remoteObject == null) {
            throw new RemotingError("Remote object not found");
        }

        for (Method method : remoteObject.getClass().getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method.invoke(remoteObject, args);
            }
        }

        throw new RemotingError("Method not found");
    }
}
