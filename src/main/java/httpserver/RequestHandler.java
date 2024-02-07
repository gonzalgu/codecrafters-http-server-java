package httpserver;

import http.request.Request;
import http.response.Response;

import java.util.Map;

public class RequestHandler {
    public static Response handleRequest(Request request) {
        var uri = request.getUri();
        Response response;
        if (uri.equals("/") || uri.startsWith("/echo/")) {
            var extractedString = extractStringFromPath(uri);
            response = OKWithContent(extractedString);
        } else if (uri.startsWith("/user-agent")) {
            var userAgent = request.getHeaders().get("User-Agent");
            response = OKWithContent(userAgent);
        } else {
            response = NotFound();
        }
        return response;
    }

    private static String extractStringFromPath(String path) {
        if (path.startsWith("/echo/")) {
            return path.substring(6);
        } else {
            return "";
        }
    }

    private static Response OKWithContent(String content) {
        var contentLength = content.length();
        return new Response("HTTP/1.1", 200, "OK",
                Map.of("Content-Type", "text/plain",
                        "Content-Length", String.valueOf(contentLength)
                ),
                content
        );
    }

    public static Response NotFound() {
        return new Response("HTTP/1.1", 404, "Not Found", Map.of(), "");
    }
}
