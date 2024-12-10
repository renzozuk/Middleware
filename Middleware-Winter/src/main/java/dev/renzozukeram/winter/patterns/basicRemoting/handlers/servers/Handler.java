package dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers;

import java.net.Socket;

public interface Handler {
    void handle(Socket socket);
}
