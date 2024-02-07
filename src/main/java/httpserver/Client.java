package httpserver;

import http.request.Request;
import http.request.RequestSerializer;
import http.response.Response;
import http.response.ResponseParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private static String host;
    private static int port;

    private static SocketChannel client;
    private static ByteBuffer buffer;

    private static Client instance;

    public static Client start(String host, int port) {
        if (instance == null) {
            instance = new Client(host, port);
        }
        return instance;
    }

    public static void stop() throws IOException {
        client.close();
        buffer = null;
    }

    private Client(String host, int port) {
        Client.host = host;
        Client.port = port;
        try {
            client = SocketChannel.open(new InetSocketAddress(host, port));
            buffer = ByteBuffer.allocate(1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response sendRequest(Request request) throws IOException {
        var serialized = RequestSerializer.serialize(request);
        buffer = ByteBuffer.wrap(serialized.getBytes());
        client.write(buffer);
        buffer.clear();
        client.read(buffer);
        String responseData = new String(buffer.array()).trim();
        var response = ResponseParser.parse(responseData);
        System.out.printf("response-received: %s\n", response);
        buffer.flip();
        return response;
    }

}
