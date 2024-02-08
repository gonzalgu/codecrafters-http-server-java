package httpserver;

import http.request.Request;
import http.request.RequestParser;
import http.response.ReponseSerializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    String host;
    int port;
    String directory;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(4221)) {
            int clientId = 0;
            while (true) {
                serverSocket.setReuseAddress(true);
                Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("client accepted.");
                new Thread(() -> {
                    try {
                        handleRequests(clientSocket);
                    } catch (IOException e) {
                        System.out.println("error processing request: " + e);
                        System.out.println("not accepting requests anymore.");
                    } finally {
                        try {
                            clientSocket.close();
                            System.out.println("connection closed.");
                        } catch (IOException e) {
                            System.out.printf("error closing socket: %s\n", e.getMessage());
                        }
                    }
                }).start();
            }
        }
    }

    private void handleRequests(Socket clientSocket) throws IOException {
        var request = readRequest(clientSocket);
        if (request != null) {
            processRequest(request, clientSocket);
        }
    }

    private void processRequest(Request message, Socket socket) throws IOException {
        var response = RequestHandler.handleGet(message, this.directory);
        var serialized = ReponseSerializer.serialize(response);
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        System.out.printf("request-received: %s\nresponse-sent: %s\n", message, response);
        writer.write(serialized);
        if(response.getFilePath() != null){
            try (FileInputStream fileInputStream = new FileInputStream(response.getFilePath().toFile())) {
                var contentSize = Integer.parseInt(response.getHeaders().get("Content-Length"));
                for(int i=0;i<contentSize; ++i){
                    writer.write(fileInputStream.read());
                }
            }
        }
        writer.flush();
    }

    public Request readRequest(Socket clientSocket) throws IOException {
        InputStream in = clientSocket.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        if (bytesRead == -1) {
            return null;
        }

        for (int i = 0; i < bytesRead; ++i) {
            stringBuilder.append((char) buffer[i]);
        }

        var data = stringBuilder.toString();
        return RequestParser.parse(data);
    }
}
