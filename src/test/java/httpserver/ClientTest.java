package httpserver;

import http.request.Request;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ClientTest {
    @Test
    void testParallelClientRequests() throws IOException, InterruptedException {
        String host = "localhost";
        int port = 4221;
        Server server = new Server(host, port);
        server.setDirectory("src/test/java/files");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(500);
        Client client1 = new Client(host, port);
        Client client2 = new Client(host, port);
        Client client3 = new Client(host, port);

        sendRequestToClient(
                new Request("GET", "/", "HTTP/1.1", Map.of(), ""),
                client1
        );
        sendRequestToClient(
                new Request("GET", "/echo/charles/hi", "HTTP/1.1", Map.of(), ""),
                client2
        );
        sendRequestToClient(
                new Request("GET", "/files/testFile.txt", "HTTP/1.1", Map.of(), ""),
                client3
        );
        client1.stop();
        client2.stop();
        client3.stop();

        executorService.shutdownNow();
    }

    private void sendRequestToClient(Request request, Client client) throws IOException {
        var response = client.sendRequest(request);
        System.out.println(response);
    }

    @Test
    void sentPostWithFile() throws InterruptedException, IOException {
        String host = "localhost";
        int port = 4221;
        Server server = new Server(host, port);
        server.setDirectory("src/test/java/files");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(500);

        //how to add a file to a request?
        Client client1 = new Client(host, port);
        var filePath = Path.of("src/test/java/fileSource/fileForPosting.txt");
        var response = client1.postFile(filePath);
        System.out.println(response);

        //then issue a get for this file, it should return the file and ok
        sendRequestToClient(
                new Request("GET", "/files/fileForPosting.txt", "HTTP/1.1", Map.of(), ""),
                client1
        );
        client1.stop();
        executorService.shutdownNow();
    }

}