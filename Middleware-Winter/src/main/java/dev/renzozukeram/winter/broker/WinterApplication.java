package dev.renzozukeram.winter.broker;

import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http.ServerRequestHandler;
import dev.renzozukeram.winter.patterns.identification.LookupService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;

public class WinterApplication {

    private final int port;

    public WinterApplication(Class<?> clazz) {
        this(clazz, 8080);
    }

    public WinterApplication(Class<?>[] classes) {
        this(classes, 8080);
    }

    public WinterApplication(Collection<Class<?>> classes) {
        this(classes, 8080);
    }

    public WinterApplication(Class<?> clazz, int port) {

        this.port = port;

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(clazz);
    }

    public WinterApplication(Class<?>[] classes, int port) {

        this.port = port;

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(classes);
    }

    public WinterApplication(Collection<Class<?>> classes, int port) {

        this.port = port;

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(classes);
    }

    public void start() {
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
