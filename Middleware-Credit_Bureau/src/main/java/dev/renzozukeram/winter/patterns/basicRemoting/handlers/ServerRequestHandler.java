package dev.renzozukeram.winter.patterns.basicRemoting.handlers;

import java.net.Socket;

public interface ServerRequestHandler {
    void handle(Socket socket);
}
