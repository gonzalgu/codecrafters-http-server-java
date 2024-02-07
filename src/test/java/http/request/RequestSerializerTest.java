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
}