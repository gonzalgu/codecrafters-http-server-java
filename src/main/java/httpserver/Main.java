package httpserver;

import http.request.Request;
import http.request.RequestParser;
import http.response.ReponseSerializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        try (
                ExecutorService executorService = Executors.newCachedThreadPool();
                ServerSocket serverSocket = new ServerSocket(4221)
        ) {
            while (true) {
                serverSocket.setReuseAddress(true);
                Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                executorService.submit(() -> {
                    try {
                        handleRequests(clientSocket);
                    } catch (IOException e) {
                        System.out.println("error processing request: " + e);
                        System.out.println("not accepting requests anymore.");
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.printf("error closing socket: %s\n", e.getMessage());
                        }
                    }
                });
            }
        }
    }

    private static void handleRequests(Socket clientSocket) throws IOException {
        var request = readRequest(clientSocket);
        processRequest(request, clientSocket);
    }

    private static void processRequest(Request message, Socket socket) throws IOException {
        var response = RequestHandler.handleRequest(message);
        var serialized = ReponseSerializer.serialize(response);
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        System.out.printf("request-received: %s\nresponse-sent: %s\n", message, response);
        writer.write(serialized);
        writer.flush();
    }

    public static Request readRequest(Socket clientSocket) throws IOException {
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        var lines = new ArrayList<String>();
        while(reader.ready()){
            String line = reader.readLine();
            if(line == null){
                break;
            }
            lines.add(line);
        }
        String requestData = String.join("\r\n", lines);
        return RequestParser.parse(requestData);
    }
}
