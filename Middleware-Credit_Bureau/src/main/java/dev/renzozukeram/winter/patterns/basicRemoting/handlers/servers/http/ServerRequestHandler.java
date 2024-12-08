package dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http;

import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.Handler;
import dev.renzozukeram.winter.patterns.basicRemoting.invoker.Invoker;
import dev.renzozukeram.winter.patterns.identification.AbsoluteObjectReference;
import dev.renzozukeram.winter.patterns.identification.ObjectId;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ServerRequestHandler implements Runnable, Handler {

    private Socket clientSocket;

    public ServerRequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        handle(clientSocket);
    }

    @Override
    public void handle(Socket socket) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String headerLine = in.readLine();
            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();

            String httpQueryString = tokenizer.nextToken();
            String[] queryParameters = httpQueryString.split("/");

            if (httpMethod.equals("GET")) {
                Invoker.invoke(new AbsoluteObjectReference(socket.getLocalAddress().toString(), socket.getPort(), new ObjectId(queryParameters[1])), queryParameters[2], Arrays.copyOfRange(queryParameters, 3, queryParameters.length));
                sendResponse(socket, 200, "GET request received successfully");
            } else if (httpMethod.equals("POST")) {
                sendResponse(socket, 200, "POST request received successfully");
            } else if (httpMethod.equals("PUT")) {
                sendResponse(socket, 200, "PUT request received successfully");
            } else if (httpMethod.equals("PATCH")) {
                sendResponse(socket, 200, "PATCH request received successfully");
            }else if (httpMethod.equals("DELETE")) {
                sendResponse(socket, 200, "DELETE request received successfully");
            } else {
                System.out.println("The HTTP method is not recognized");
                sendResponse(socket, 405, "Method Not Allowed");
            }
        } catch (NullPointerException ignored) {} catch (Exception e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(Socket socket, int statusCode, String responseString) {

        String statusLine;
        String serverHeader = "Server: WebServer\r\n";
        String contentTypeHeader = "Content-Type: text/html\r\n";

        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            if (statusCode == 200) {
                statusLine = "HTTP/1.0 200 OK" + "\r\n";
                String contentLengthHeader = "Content-Length: " + responseString.length() + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes(serverHeader);
                out.writeBytes(contentTypeHeader);
                out.writeBytes(contentLengthHeader);
                out.writeBytes("\r\n");
                out.writeBytes(responseString);
            } else if (statusCode == 201) {
                statusLine = "HTTP/1.0 201 Created" + "\r\n";
                String contentLengthHeader = "Content-Length: " + responseString.length() + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes(serverHeader);
                out.writeBytes(contentTypeHeader);
                out.writeBytes(contentLengthHeader);
                out.writeBytes("\r\n");
                out.writeBytes(responseString);
            } else if (statusCode == 204) {
                statusLine = "HTTP/1.0 204 No Content" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            } else if (statusCode == 400) {
                statusLine = "HTTP/1.0 400 Bad Request" + "\r\n";
                String contentLengthHeader = "Content-Length: " + responseString.length() + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes(serverHeader);
                out.writeBytes(contentTypeHeader);
                out.writeBytes(contentLengthHeader);
                out.writeBytes("\r\n");
                out.writeBytes(responseString);
            } else if (statusCode == 405) {
                statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            } else if (statusCode == 409) {
                statusLine = "HTTP/1.0 409 Conflict" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            } else if (statusCode == 500) {
                statusLine = "HTTP/1.0 500 Internal Server Error" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            } else {
                statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
