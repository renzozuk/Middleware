package dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http;

import dev.renzozukeram.winter.message.ResponseEntity;
import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RemotingError;
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

            var response = queryParameters.length > 2 ?
                    Invoker.invoke(new AbsoluteObjectReference(socket.getLocalAddress().toString(), socket.getPort(), new ObjectId(queryParameters[1])), queryParameters[2], Arrays.copyOfRange(queryParameters, 3, queryParameters.length)) :
                    Invoker.invoke(new AbsoluteObjectReference(socket.getLocalAddress().toString(), socket.getPort(), new ObjectId(queryParameters[1]))) ;

            if (response.getClass() != ResponseEntity.class) {
                throw new RemotingError("The method must return a ResponseEntity");
            }

            if (httpMethod.equals("GET") || httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("PATCH") || httpMethod.equals("DELETE")) {
                sendResponse(socket, response);
            } else {
                sendResponse(socket, new ResponseEntity(405, "HTTP requisition type not supported"));
            }
        } catch (NullPointerException ignored) {} catch (Exception e) {
            sendResponse(socket, new ResponseEntity(400, e.getMessage()));
        }

        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(Socket socket, ResponseEntity responseEntity) {

        String statusLine;
        String serverHeader = "Server: WebServer\r\n";
        String contentTypeHeader = "Content-Type: text/html\r\n";
        String contentLengthHeader = "Content-Length: " + responseEntity.getSerializedResponse().length() + "\r\n";

        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            switch (responseEntity.getStatusCode()) {
                case 200:
                    statusLine = "HTTP/1.0 200 OK" + "\r\n";
                    break;
                case 201:
                    statusLine = "HTTP/1.0 201 Created" + "\r\n";
                    break;
                case 204:
                    statusLine = "HTTP/1.0 204 No Content" + "\r\n";
                    break;
                case 400:
                    statusLine = "HTTP/1.0 400 Bad Request" + "\r\n";
                    break;
                case 405:
                    statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
                    break;
                case 409:
                    statusLine = "HTTP/1.0 409 Conflict" + "\r\n";
                    break;
                case 500:
                    statusLine = "HTTP/1.0 500 Internal Server Error" + "\r\n";
                    break;
                default:
                    statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
                    break;
            }

            out.writeBytes(statusLine);
            if (responseEntity.getStatusCode() != 204 && responseEntity.getSerializedResponse() != null) {
                out.writeBytes(serverHeader);
                out.writeBytes(contentTypeHeader);
                out.writeBytes(contentLengthHeader);
                out.writeBytes("\r\n");
                out.writeBytes(responseEntity.getSerializedResponse());
            } else {
                out.writeBytes("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
