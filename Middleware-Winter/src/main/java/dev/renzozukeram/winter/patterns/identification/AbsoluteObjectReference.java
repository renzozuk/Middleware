package dev.renzozukeram.winter.patterns.identification;

import java.util.Objects;

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
        return String.format("%s:%d/%s", host, port, objectId.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbsoluteObjectReference that)) return false;
        return port == that.port && Objects.equals(host, that.host) && Objects.equals(objectId, that.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, objectId);
    }

    @Override
    public String toString() {
        return getFullReference();
    }
}
