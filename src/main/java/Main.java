import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");
            var request = readRequest(clientSocket);
            processRequest(request, clientSocket);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public record Message(String method, String path, String httpVersion){}

    public static Message parseMessage(BufferedReader reader) throws IOException {
        String startLine = reader.readLine();
        var splitLine = startLine.split(" ");
        return new Message(splitLine[0].trim(), splitLine[1].trim(), splitLine[2].trim());
    }

    private static void processRequest(Message message, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        if(message.path.startsWith("/echo/")){
            var extractedString = message.path.substring(6);
            var response = createResponse(extractedString);
            writer.write(response);
        }else{
            writer.write("HTTP/1.1 404 Not Found\r\n\r\n");
        }
        writer.flush();
    }

    private static String createResponse(String extractedString) {
        var response = String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n%s\r\n",
                extractedString.length(),
                extractedString);
        return response;
    }

    public static Message readRequest(Socket clientSocket) throws IOException {
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        var message = parseMessage(reader);
        String header1 = reader.readLine();
        String header2 = reader.readLine();
        return message;
    }
}
