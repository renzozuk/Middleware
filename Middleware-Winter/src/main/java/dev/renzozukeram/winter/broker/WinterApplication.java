package dev.renzozukeram.winter.broker;

import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http.ServerRequestHandler;
import dev.renzozukeram.winter.patterns.identification.LookupService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WinterApplication {

    private final String hostname;
    private final int port;

    public WinterApplication(Class<?> clazz) {
        this(clazz, "localhost", 8080);
    }

    public WinterApplication(Class<?> clazz, String hostname) {
        this(clazz, hostname, 8080);
    }

    public WinterApplication(Class<?> clazz, int port) {
        this(clazz, "localhost", port);
    }

    public WinterApplication(Class<?>[] classes) {
        this(classes, "localhost", 8080);
    }

    public WinterApplication(Class<?>[] classes, String hostname) {
        this(classes, hostname, 8080);
    }

    public WinterApplication(Class<?>[] classes, int port) {
        this(classes, "localhost", port);
    }

    public WinterApplication(Collection<Class<?>> classes) {
        this(classes, "localhost", 8080);
    }

    public WinterApplication(Collection<Class<?>> classes, String hostname) {
        this(classes, hostname, 8080);
    }

    public WinterApplication(Collection<Class<?>> classes, int port) {
        this(classes, "localhost", port);
    }

    public WinterApplication(Class<?> clazz, String hostname, int port) {

        this.hostname = hostname;
        this.port = port;

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(clazz);
    }

    public WinterApplication(Class<?>[] classes, String hostname, int port) {

        this.hostname = hostname;
        this.port = port;

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(classes);
    }

    public WinterApplication(Collection<Class<?>> classes, String hostname, int port) {

        this.hostname = hostname;
        this.port = port;

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(classes);
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName("localhost"));
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            System.out.printf("Winter application started.\nWaiting connections on http://%s:%d.\n", "localhost", port);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serverSocket.close();
                    executor.shutdown();
                    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                        executor.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.execute(new ServerRequestHandler(clientSocket));
                } catch (IOException e) {
                    if (!serverSocket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }
}
