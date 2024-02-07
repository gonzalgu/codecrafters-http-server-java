import http.response.ReponseSerializer;
import http.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class ReponseSerializerTest {
    @Test
    void testSerialize200OK(){
        var response = new Response("HTTP/1.1", 200, "OK", new HashMap<>(), "");
        var serialized = ReponseSerializer.serialize(response);
        System.out.println(serialized);
    }

    @Test
    void testSerializeOKWithHeaders(){
        var response = new Response(
                "HTTP/1.1",
                200, "OK",
                Map.of(
                        "Connection", "Keep-Alive",
                        "Content-Type", "text/plain",
                        "Content-Length", "123"
                        ),
                "");
        var serialized = ReponseSerializer.serialize(response);
        System.out.println(serialized);
    }

    @Test
    void testSerializeOkWithBody(){
        var response = new Response(
                "HTTP/1.1",
                200, "OK",
                Map.of(
                        "Connection", "Keep-Alive",
                        "Content-Type", "text/plain",
                        "Content-Length", "123"
                ),
                "My Body is my Temple. This is my body, there are many like this, but this one is mine.");
        var serialized = ReponseSerializer.serialize(response);
        System.out.println(serialized);
    }

}