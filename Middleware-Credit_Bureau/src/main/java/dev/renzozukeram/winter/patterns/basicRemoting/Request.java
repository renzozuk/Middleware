package dev.renzozukeram.winter.patterns.basicRemoting;

public class Request {

    private final String objectId;
    private final String operationName;
    private final Object[] parameters;

    public Request(String objectId, String operationName, Object[] parameters) {
        this.objectId = objectId;
        this.operationName = operationName;
        this.parameters = parameters;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getOperationName() {
        return operationName;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
