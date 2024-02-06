import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try(ServerSocket serverSocket = new ServerSocket(4221)) {
            while(true){
                serverSocket.setReuseAddress(true);
                Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                executorService.submit(() -> {
                    try {
                        handleRequests(clientSocket);
                    } catch (IOException e) {
                        System.out.println("error processing request: " + e);
                        System.out.println("not accepting requests anymore.");
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void handleRequests(Socket clientSocket) throws IOException {
        while(true){
            var request = readRequest(clientSocket);
            processRequest(request, clientSocket);
        }
    }

    public record Request(
            String method,
            String path,
            String httpVersion,
            String host,
            String userAgent
    ) {
    }

    public static Request parseRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        //firstLine
        var splitLine = line.split(" ");

        // Prepare to read headers
        Map<String, String> headers = new HashMap<>();
        for(int i=0;i<2; ++i){
            line = reader.readLine();
            int separator = line.indexOf(":");
            if (separator == -1) {
                continue; // Not a valid header line; ignore it
            }
            String headerName = line.substring(0, separator).trim();
            String headerValue = line.substring(separator + 1).trim();
            headers.put(headerName, headerValue);
        }

        String method, path, httpVersion;
        method = splitLine[0].trim();
        path = splitLine[1].trim();
        httpVersion = splitLine[2].trim();

        return new Request(
                method, path, httpVersion,
                headers.get("Host"),
                headers.get("User-Agent")
        );
    }

    private static void processRequest(Request message, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        if (message.path.equals("/") || message.path.startsWith("/echo/")) {
            var path = extractPath(message.path);
            var response = createResponse(path);
            writer.write(response);
        } else if (message.path.startsWith("/user-agent")) {
            var response = createResponse(message.userAgent);
            writer.write(response);
        } else {
            writer.write("HTTP/1.1 404 Not Found\r\n\r\n");
        }
        writer.flush();
    }

    private static String extractPath(String path) {
        if (path.startsWith("/echo/")) {
            return path.substring(6);
        } else {
            return "";
        }
    }

    private static String createResponse(String extractedString) {
        var response = String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s\r\n",
                extractedString.length(),
                extractedString);
        return response;
    }

    public static Request readRequest(Socket clientSocket) throws IOException {
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        var message = parseRequest(reader);
        String header1 = reader.readLine();
        String header2 = reader.readLine();
        return message;
    }
}
