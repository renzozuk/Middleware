package dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.http;

import dev.renzozukeram.winter.enums.RequisitionType;
import dev.renzozukeram.winter.message.ResponseEntity;
import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.RequisitionTypeNotSupportedException;
import dev.renzozukeram.winter.patterns.basicRemoting.exceptions.UnexpectedArgumentException;
import dev.renzozukeram.winter.patterns.basicRemoting.handlers.servers.Handler;
import dev.renzozukeram.winter.patterns.basicRemoting.invoker.Invoker;
import dev.renzozukeram.winter.patterns.identification.AbsoluteObjectReference;
import dev.renzozukeram.winter.patterns.identification.ObjectId;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRequestHandler implements Runnable, Handler {

    private static final Logger LOGGER = Logger.getLogger(ServerRequestHandler.class.getName());

    private final Socket clientSocket;
//    private final Semaphore semaphore;

    public ServerRequestHandler(Socket clientSocket/*, Semaphore semaphore*/) {
        this.clientSocket = clientSocket;
//        this.semaphore = semaphore;
        try {
            clientSocket.setSoTimeout(5000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            handle(clientSocket);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            sendResponse(clientSocket, new ResponseEntity(500, "Internal Server Error"));
        } finally {
            if (clientSocket != null && !clientSocket.isClosed() && clientSocket.isConnected()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error closing client socket", e);
                }
            }
        }
    }

    @Override
    public void handle(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            if (socket.isClosed() || !socket.isConnected()) {
                LOGGER.warning("Attempt to handle closed or disconnected socket");
                return;
            }

            String headerLine = in.readLine();
            if (headerLine == null || headerLine.isEmpty()) {
                LOGGER.warning("Received empty request");
                sendResponse(socket, new ResponseEntity(400, "Bad request."));
                return;
            }

            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            if (tokenizer.countTokens() < 2) {
                LOGGER.warning("Malformed request header");
                sendResponse(socket, new ResponseEntity(400, "Malformed request."));
                return;
            }

            StringBuilder requestBuilder = new StringBuilder();
            String line;
            int contentLength = -1;

            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\r\n");

                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            String jsonBody = null;
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                int charsRead = in.read(bodyChars, 0, contentLength);
                if (charsRead > 0) {
                    jsonBody = new String(bodyChars, 0, charsRead);
                }
            }

            String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();
            String[] queryParameters = httpQueryString.split("/");

            if (queryParameters.length < 2) {
                LOGGER.warning("Invalid query parameters");
                sendResponse(socket, new ResponseEntity(400, "Invalid Parameters"));
                return;
            }

            try {
                RequisitionType requisitionType = Arrays.stream(RequisitionType.values())
                        .filter(rt -> rt.toString().equals(httpMethod))
                        .findAny()
                        .orElseThrow(() -> new RequisitionTypeNotSupportedException(httpMethod, Arrays.toString(RequisitionType.values())));
                try {
                    var response = queryParameters.length > 2 ?
                            ((jsonBody != null) ? Invoker.invoke(
                                    new AbsoluteObjectReference(socket.getLocalAddress().toString(), socket.getPort(), new ObjectId(queryParameters[1])),
                                    requisitionType,
                                    queryParameters[2],
                                    Arrays.copyOfRange(queryParameters, 3, queryParameters.length),
                                    jsonBody
                            ) : Invoker.invoke(
                                    new AbsoluteObjectReference(socket.getLocalAddress().toString(), socket.getPort(), new ObjectId(queryParameters[1])),
                                    requisitionType,
                                    queryParameters[2],
                                    Arrays.copyOfRange(queryParameters, 3, queryParameters.length)
                            )) :
                            ((jsonBody != null) ? Invoker.invoke(
                                    new AbsoluteObjectReference(socket.getLocalAddress().toString(), socket.getPort(), new ObjectId(queryParameters[1])),
                                    requisitionType,
                                    jsonBody
                            ) : Invoker.invoke(
                                    new AbsoluteObjectReference(socket.getLocalAddress().toString(), socket.getPort(), new ObjectId(queryParameters[1])),
                                    requisitionType
                            ));

                    sendResponse(socket, response);
                } catch (UnexpectedArgumentException e) {
                    sendResponse(socket, new ResponseEntity(400, e.getMessage()));
                }
            } catch (RequisitionTypeNotSupportedException | IllegalArgumentException e) {
                e.printStackTrace();
                sendResponse(socket, new ResponseEntity(400, e.getMessage() != null ? e.getMessage() : ""));
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(socket, new ResponseEntity(500, e.getMessage() != null ? e.getMessage() : ""));
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Bad request", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
        }
    }

    private void sendResponse(Socket socket, ResponseEntity responseEntity) {

        if (socket == null || socket.isClosed()) {
            LOGGER.warning("Attempting to send response on closed socket");
            return;
        }

        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            String statusLine = switch (responseEntity.getStatusCode()) {
                case 100 -> "HTTP/1.0 100 Continue\r\n";
                case 101 -> "HTTP/1.0 101 Switching Protocols\r\n";
                case 102 -> "HTTP/1.0 102 Processing\r\n";
                case 103 -> "HTTP/1.0 103 Early Hints\r\n";
                case 200 -> "HTTP/1.0 200 OK\r\n";
                case 201 -> "HTTP/1.0 201 Created\r\n";
                case 202 -> "HTTP/1.0 202 Accepted\r\n";
                case 203 -> "HTTP/1.0 203 Non-Authoritative Information\r\n";
                case 204 -> "HTTP/1.0 204 No Content\r\n";
                case 205 -> "HTTP/1.0 205 Reset Content\r\n";
                case 206 -> "HTTP/1.0 206 Partial Content\r\n";
                case 207 -> "HTTP/1.0 207 Multi-Status\r\n";
                case 208 -> "HTTP/1.0 208 Already Reported\r\n";
                case 216 -> "HTTP/1.0 216 IM Used\r\n";
                case 300 -> "HTTP/1.0 300 Multiple Choices\r\n";
                case 301 -> "HTTP/1.0 301 Moved Permanently\r\n";
                case 302 -> "HTTP/1.0 302 Found\r\n";
                case 303 -> "HTTP/1.0 303 See Other\r\n";
                case 304 -> "HTTP/1.0 304 Not Modified\r\n";
                case 305 -> "HTTP/1.0 305 Use Proxy\r\n";
                case 306 -> "HTTP/1.0 306 unused\r\n";
                case 307 -> "HTTP/1.0 307 Temporary Redirect\r\n";
                case 308 -> "HTTP/1.0 308 Permanent Redirect\r\n";
                case 400 -> "HTTP/1.0 400 Bad Request\r\n";
                case 401 -> "HTTP/1.0 401 Unauthorized\r\n";
                case 402 -> "HTTP/1.0 401 Payment Required\r\n";
                case 403 -> "HTTP/1.0 403 Forbidden\r\n";
                case 404 -> "HTTP/1.0 404 Not Found\r\n";
                case 405 -> "HTTP/1.0 405 Method Not Allowed\r\n";
                case 406 -> "HTTP/1.0 406 Not Acceptable\r\n";
                case 407 -> "HTTP/1.0 407 Proxy Authentication Required\r\n";
                case 408 -> "HTTP/1.0 408 Request Timeout\r\n";
                case 409 -> "HTTP/1.0 409 Conflict\r\n";
                case 410 -> "HTTP/1.0 410 Gone\r\n";
                case 411 -> "HTTP/1.0 411 Length Required\r\n";
                case 412 -> "HTTP/1.0 412 Precondition Failed\r\n";
                case 413 -> "HTTP/1.0 413 Content Too Large\r\n";
                case 414 -> "HTTP/1.0 414 URI Too Long\r\n";
                case 415 -> "HTTP/1.0 415 Unsupported Media Type\r\n";
                case 416 -> "HTTP/1.0 416 Range Not Satisfiable\r\n";
                case 417 -> "HTTP/1.0 417 Expectation Failed\r\n";
                case 418 -> "HTTP/1.0 418 I'm a teapot\r\n";
                case 421 -> "HTTP/1.0 421 Misdirected Request\r\n";
                case 422 -> "HTTP/1.0 422 Unprocessable Content\r\n";
                case 423 -> "HTTP/1.0 423 Locked\r\n";
                case 424 -> "HTTP/1.0 424 Failed Dependency\r\n";
                case 425 -> "HTTP/1.0 425 Too Early\r\n";
                case 426 -> "HTTP/1.0 426 Upgrade Required\r\n";
                case 428 -> "HTTP/1.0 428 Precondition Required\r\n";
                case 429 -> "HTTP/1.0 429 Too Many Requests\r\n";
                case 431 -> "HTTP/1.0 431 Request Header Fields Too Large\r\n";
                case 451 -> "HTTP/1.0 451 Unavailable For Legal Reasons\r\n";
                case 501 -> "HTTP/1.0 501 Not Implemented\r\n";
                case 502 -> "HTTP/1.0 502 Bad Gateway\r\n";
                case 503 -> "HTTP/1.0 503 Service Unavailable\r\n";
                case 504 -> "HTTP/1.0 504 Gateway Timeout\r\n";
                case 505 -> "HTTP/1.0 505 HTTP Version Not Supported\r\n";
                case 506 -> "HTTP/1.0 506 Variant Also Negotiates\r\n";
                case 507 -> "HTTP/1.0 507 Insufficient Storage\r\n";
                case 508 -> "HTTP/1.0 508 Loop Detected\r\n";
                case 510 -> "HTTP/1.0 510 Not Extended\r\n";
                case 511 -> "HTTP/1.0 511 Network Authentication Required\r\n";
                default -> "HTTP/1.0 500 Internal Server Error\r\n";
            };

            out.writeBytes(statusLine);
            if (responseEntity.getStatusCode() != 204 && responseEntity.getSerializedResponse() != null) {
                out.writeBytes("Server: WebServer\r\n");
                out.writeBytes("Content-Type: text/html\r\n");
                out.writeBytes("Content-Length: " + responseEntity.getSerializedResponse().length() + "\r\n");
                out.writeBytes("\r\n");
                out.writeBytes(responseEntity.getSerializedResponse());
            } else {
                out.writeBytes("\r\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error sending response", e);
        }
    }
}