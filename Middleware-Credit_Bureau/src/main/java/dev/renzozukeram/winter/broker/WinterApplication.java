package dev.renzozukeram.winter.broker;

import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http.ServerRequestHandler;
import dev.renzozukeram.winter.patterns.identification.LookupService;

import java.io.IOException;
import java.net.ServerSocket;

public class WinterApplication {

    public WinterApplication(Class<?> clazz) {
        this(clazz, 8080);
    }

    public WinterApplication(Class<?> clazz, int port) {

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(clazz);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Winter application started on port " + port);
            while (true) {
                new Thread(new ServerRequestHandler(serverSocket.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
