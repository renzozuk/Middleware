package dev.renzozukeram.winter.patterns.identification;

public class ObjectId {

    private final String id;

    public ObjectId(String id) {
        this.id = id;
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
}
