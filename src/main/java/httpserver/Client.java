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
    private String host;
    private int port;

    private SocketChannel client;
    private ByteBuffer buffer;


    public void stop() throws IOException {
        this.client.close();
        this.buffer = null;
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.client = SocketChannel.open(new InetSocketAddress(host, port));
            this.buffer = ByteBuffer.allocate(1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response sendRequest(Request request) throws IOException {
        var serialized = RequestSerializer.serialize(request);
        buffer = ByteBuffer.wrap(serialized.getBytes());
        client.write(buffer);
        buffer.clear();
        int bytesRead = client.read(buffer);
        String responseData = new String(buffer.array()).trim();
        System.out.printf("client: bytes-read: %d\n", bytesRead);
        System.out.printf("client: response-received: %s\n", responseData);
        var response = ResponseParser.parse(responseData);
        buffer.flip();
        return response;
    }

}
