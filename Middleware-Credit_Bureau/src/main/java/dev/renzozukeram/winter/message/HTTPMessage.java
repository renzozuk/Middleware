package dev.renzozukeram.winter.message;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONObject;

public record HTTPMessage(
        String httpMethod,
        String resource,
        JSONObject body,
        int statusCode,
        String statusMessage,
        Map<String, String> headers
) implements Serializable {}
