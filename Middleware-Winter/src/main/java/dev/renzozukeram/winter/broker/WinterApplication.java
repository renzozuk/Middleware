package dev.renzozukeram.winter.broker;

import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http.ServerRequestHandler;
import dev.renzozukeram.winter.patterns.identification.LookupService;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class WinterApplication {

    private final String hostname;
    private final int port;
//    private final Semaphore semaphore = new Semaphore(1);

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

        LookupService.getInstance().initializeRoutes(clazz);
    }

    public WinterApplication(Class<?>[] classes, String hostname, int port) {

        this.hostname = hostname;
        this.port = port;

        LookupService.getInstance().initializeRoutes(classes);
    }

    public WinterApplication(Collection<Class<?>> classes, String hostname, int port) {

        this.hostname = hostname;
        this.port = port;

        LookupService.getInstance().initializeRoutes(classes);
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName(hostname));
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            System.out.println("Winter application started.");
            LookupService.getInstance().getRegisteredClasses().forEach((k, v) ->
                    System.out.printf("Waiting for connections on http://%s:%d/%s.\n", hostname, port, k.getId())
            );

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down server...");
                    serverSocket.close();
                    executor.shutdown();
                    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                        executor.shutdownNow();
                    }
                    System.out.println("Server shut down successfully.");
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    System.err.println("Error during server shutdown: " + e.getMessage());
                }
            }));

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.execute(new ServerRequestHandler(clientSocket/*, semaphore*/));
                } catch (IOException e) {
                    if (!serverSocket.isClosed()) {
                        System.err.println("Error accepting connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }
}
