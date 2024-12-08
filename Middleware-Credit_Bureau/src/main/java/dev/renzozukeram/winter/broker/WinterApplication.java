package dev.renzozukeram.winter.broker;

import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.Handler;
import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http.ServerRequestHandler;
import dev.renzozukeram.winter.patterns.identification.LookupService;
import dev.renzozukeram.winter.remoteObject.CreditBureau;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WinterApplication {

    public WinterApplication() throws IOException {

        var lookup = LookupService.getInstance();
        lookup.initializeRoutes(CreditBureau.class);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                new Thread(new ServerRequestHandler(serverSocket.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
