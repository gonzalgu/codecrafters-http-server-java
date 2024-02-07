package http.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseParser {
    public static Response parse(String responseData) {
        String[] lines = responseData.split("\r\n");

        String[] statusLine = lines[0].split(" ", 3);
        String protocolVersion = statusLine[0];
        String statusCode = statusLine[1];
        String statusText = statusLine[2];

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
        return new Response(
                protocolVersion,
                Integer.parseInt(statusCode),
                statusText,
                headers,
                body
        );
    }

}
