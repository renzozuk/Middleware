package dev.renzozukeram.winter.message;

import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.JsonMarshaller;
import dev.renzozukeram.winter.patterns.basicRemoting.marshaller.Marshaller;

public class ResponseEntity {

    private final Marshaller marshaller = new JsonMarshaller();

    private final int statusCode;
    private Object nonSerializedResponse;
    private String serializedResponse;

    public ResponseEntity(int statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseEntity(int statusCode, Object nonSerializedResponse) {
        this.statusCode = statusCode;
        this.nonSerializedResponse = nonSerializedResponse;
        this.serializedResponse = marshaller.serialize(nonSerializedResponse);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getNonSerializedResponse() {
        return nonSerializedResponse;
    }

    public String getSerializedResponse() {
        return serializedResponse;
    }
}
