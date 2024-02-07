package http.request;

import java.util.Map;

public class Request {
    String method;
    String uri;
    String httpVersion;
    Map<String,String> headers;
    String body;

    public Request(String method, String uri,String httpVersion, Map<String, String> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion(){ return httpVersion; }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "http.request.Request{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
