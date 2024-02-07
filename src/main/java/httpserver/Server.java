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

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4221);
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

    private static void handleRequests(Socket clientSocket) throws IOException {
        var request = readRequest(clientSocket);
        if (request != null) {
            processRequest(request, clientSocket);
        }
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
        System.out.println(data);
        /*
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
         */
        return RequestParser.parse(data);
    }
}
