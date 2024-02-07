package http.request;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestSerializerTest {
    @Test
    void testSerializeGET(){
        var request = new Request("GET", "/", "HTTP/1.1", Map.of(), "");
        var serialized = RequestSerializer.serialize(request);
        System.out.println(serialized);
    }

    static class RequestParserTest {
        @Test
        void testParseGETOnlyFirstLine(){
            String requestData = "GET / HTTP/1.1";
            var request = RequestParser.parse(requestData);
            System.out.println(request);
            assertEquals("GET", request.getMethod());
            assertEquals("/", request.getUri());
        }

        @Test
        void testParseGETWithHeaders(){
            String requestData = "GET /echo/alpha/charles/tango HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n";
            var request = RequestParser.parse(requestData);
            System.out.println(request);
            assertEquals("GET", request.getMethod());
            assertEquals("/echo/alpha/charles/tango", request.getUri());
            assertTrue(request.getHeaders().keySet().containsAll(List.of("Host", "User-Agent", "Accept")));
        }

        @Test
        void testParseGETWithBody(){
            String requestData = "GET /echo/alpha/charles/tango HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\nhere comes the body\r\nhere is another text\r\n";
            var request = RequestParser.parse(requestData);
            System.out.println(request);
            assertEquals("GET", request.getMethod());
            assertEquals("/echo/alpha/charles/tango", request.getUri());
            assertTrue(request.getHeaders().keySet().containsAll(List.of("Host", "User-Agent", "Accept")));
        }


    }
}