package dev.renzozukeram.winter.patterns.identification;

public class AbsoluteObjectReference {

    private final String host;
    private final int port;
    private final ObjectId objectId;

    public AbsoluteObjectReference(String host, int port, ObjectId objectId) {
        this.host = host;
        this.port = port;
        this.objectId = objectId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public String getFullReference() {
        return String.format("remote://%s:%d/%s", host, port, objectId.toString());
    }
}
