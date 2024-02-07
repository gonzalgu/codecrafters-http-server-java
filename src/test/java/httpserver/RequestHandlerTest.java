package httpserver;

import http.request.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class RequestHandlerTest {
    @Test
    void requestWithEchoShouldReturnPathInBody() {
        Request request = new Request("GET", "/echo/charles/papa/tango", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleRequest(request);
        Assertions.assertTrue(response.getBody().contains("charles/papa/tango"));
    }

    @Test
    void requestWithRootPathShouldReturnOk() {
        Request request = new Request("GET", "/", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleRequest(request);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    void requestWithUserAgentPathShouldReturnUserAgentInBody() {
        Request request = new Request("GET", "/user-agent", "HTTP/1.1",
                Map.of("User-Agent", "test-agent-123"),
                ""
        );
        var response = RequestHandler.handleRequest(request);
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("test-agent-123"));
    }

    @Test
    void otherRequestsShouldReturn404NotFound(){
        Request request = new Request("GET", "/bazinga", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleRequest(request);
        Assertions.assertEquals(404, response.getStatusCode());
    }

}