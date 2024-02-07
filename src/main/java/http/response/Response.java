package http.response;

import java.nio.file.Path;
import java.util.Map;

public class Response {
    //status line
    String protocolVersion;
    int statusCode;
    String statusText;
    //Headers
    Map<String, String> headers;
    String body;

    Path filePath;

    public Response(String protocolVersion, int statusCode, String statusText, Map<String, String> headers, String body) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.headers = headers;
        this.body = body;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }


    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "http.response.Response{" +
                "protocolVersion='" + protocolVersion + '\'' +
                ", statusCode=" + statusCode +
                ", statusText='" + statusText + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
