package httpserver;

import http.request.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

class RequestHandlerTest {
    @Test
    void requestWithEchoShouldReturnPathInBody() throws IOException {
        Request request = new Request("GET", "/echo/charles/papa/tango", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleRequest(request, null);
        Assertions.assertTrue(response.getBody().contains("charles/papa/tango"));
    }

    @Test
    void requestWithRootPathShouldReturnOk() throws IOException {
        Request request = new Request("GET", "/", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleRequest(request, null);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    void requestWithUserAgentPathShouldReturnUserAgentInBody() throws IOException {
        Request request = new Request("GET", "/user-agent", "HTTP/1.1",
                Map.of("User-Agent", "test-agent-123"),
                ""
        );
        var response = RequestHandler.handleRequest(request, null);
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("test-agent-123"));
    }

    @Test
    void otherRequestsShouldReturn404NotFound() throws IOException {
        Request request = new Request("GET", "/bazinga", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleRequest(request, null);
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    void requestFile() throws IOException {
        String directory = "src/test/java/files";
        Request request = new Request("GET", "/files/testFile.txt", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleRequest(request, directory);
        Assertions.assertEquals(200, response.getStatusCode());
        System.out.println(response);
    }
}