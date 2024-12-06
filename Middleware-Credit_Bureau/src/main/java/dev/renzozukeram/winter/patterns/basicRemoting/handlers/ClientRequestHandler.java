package dev.renzozukeram.winter.patterns.basicRemoting.handlers;

import dev.renzozukeram.winter.patterns.basicRemoting.Request;

public interface ClientRequestHandler {
    Object sendRequest(Request request);
}
