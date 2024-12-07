package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RemotingError;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.Marshaller;
import dev.renzozukeram.winter.patterns.identification.AbsoluteObjectReference;
import dev.renzozukeram.winter.patterns.identification.LookupService;
import dev.renzozukeram.winter.patterns.identification.ObjectId;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {

    private final Marshaller marshaller;
    private final LookupService lookupService;

    public Invoker(Marshaller marshaller, LookupService lookupService) {
        this.marshaller = marshaller;
        this.lookupService = lookupService;
    }

    public Object invoke(AbsoluteObjectReference reference, String methodName, Object[] args) throws InvocationTargetException, IllegalAccessException {

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
