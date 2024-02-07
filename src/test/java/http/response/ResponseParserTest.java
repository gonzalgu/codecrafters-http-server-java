package http.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseParserTest {
    @Test
    void parseOk(){
        var data = "HTTP/1.1 200 OK";
        var parsed = ResponseParser.parse(data);
        System.out.println(parsed);
    }

    @Test
    void parse404NotFound(){
        var data = "HTTP/1.1 404 Not found";
        var parsed = ResponseParser.parse(data);
        System.out.println(parsed);
    }

}