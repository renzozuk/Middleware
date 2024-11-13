package dev.renzozukeram.winter.servers;

import dev.renzozukeram.winter.servers.handlers.UDPMessageHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer extends Server {
    public UDPServer(int port) {
        this("localhost", port);
    }

    public UDPServer(String address, int port) {
        super(address, port);

        if (port != 8080 && (port < 9001 || port > 10000)) {
            throw new IllegalArgumentException("Invalid port number. The port number must be between 9001 and 10000.");
        }
    }

    @Override
    public void startServer() {
        System.out.printf("UDP server started on port %d.\n", super.getPort());

        try (DatagramSocket serverSocket = new DatagramSocket(super.getPort())) {
            while (true) {
                byte[] receiveMessage = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveMessage, receiveMessage.length);

                serverSocket.receive(receivePacket);

                String reply = UDPMessageHandler.handleRequest(new String(receivePacket.getData()));

                byte[] replyMessage = reply.getBytes();

                DatagramPacket datagramPacket = new DatagramPacket(replyMessage, replyMessage.length, receivePacket.getAddress(), receivePacket.getPort());

                serverSocket.send(datagramPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
