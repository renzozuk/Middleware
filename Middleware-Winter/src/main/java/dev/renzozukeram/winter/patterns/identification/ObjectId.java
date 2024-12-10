package dev.renzozukeram.winter.patterns.identification;

import java.util.UUID;

public class ObjectId {

    private final String id;

    public ObjectId() {
        this.id = UUID.randomUUID().toString();
    }

    public ObjectId(String predefinedId) {
        this.id = predefinedId;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectId)) return false;
        ObjectId objectId = (ObjectId) o;
        return id.equals(objectId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
