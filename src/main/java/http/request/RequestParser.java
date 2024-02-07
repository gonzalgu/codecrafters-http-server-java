package http.request;

import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public static Request parse(String requestData) {
        String[] lines = requestData.split("\r\n");
        String[] requestLine = lines[0].split(" ");
        String method = requestLine[0];
        String uri = requestLine[1];
        String httpVersion = requestLine[2];
        Map<String, String> headers = new HashMap<>();
        int i = 1;
        for (; i < lines.length; ++i) {
            String line = lines[i];
            if (line.isEmpty()) {
                break;
            }
            String[] header = line.split(": ");
            if (header.length == 2) {
                headers.put(header[0], header[1]);
            }
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (i += 1; i < lines.length; ++i) {
            bodyBuilder.append(lines[i]);
            if (i < lines.length - 1) {
                bodyBuilder.append("\r\n");
            }
        }
        String body = bodyBuilder.toString();
        return new Request(method, uri, httpVersion, headers, body);
    }
}
