package dev.renzozukeram.winter.patterns.basicRemoting.invoker;

import dev.renzozukeram.winter.patterns.basicRemoting.Request;

public interface Invoker {
    void handleRequest(Request request);
}
