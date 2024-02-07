import http.request.RequestParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RequestParserTest {
    @Test
    void testParseGETOnlyFirstLine(){
        String requestData = "GET / HTTP/1.1";
        var request = RequestParser.parse(requestData);
        System.out.println(request);
        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/", request.getUri());
    }

    @Test
    void testParseGETWithHeaders(){
        String requestData = "GET /echo/alpha/charles/tango HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n";
        var request = RequestParser.parse(requestData);
        System.out.println(request);
        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/echo/alpha/charles/tango", request.getUri());
        Assertions.assertTrue(request.getHeaders().keySet().containsAll(List.of("Host", "User-Agent", "Accept")));
    }

    @Test
    void testParseGETWithBody(){
        String requestData = "GET /echo/alpha/charles/tango HTTP/1.1\r\nHost: localhost:4221\r\nUser-Agent: curl/7.81.0\r\nAccept: */*\r\n\r\nhere comes the body\r\nhere is another text\r\n";
        var request = RequestParser.parse(requestData);
        System.out.println(request);
        Assertions.assertEquals("GET", request.getMethod());
        Assertions.assertEquals("/echo/alpha/charles/tango", request.getUri());
        Assertions.assertTrue(request.getHeaders().keySet().containsAll(List.of("Host", "User-Agent", "Accept")));
    }


}