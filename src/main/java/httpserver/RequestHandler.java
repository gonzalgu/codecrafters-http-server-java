package httpserver;

import http.request.Request;
import http.response.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class RequestHandler {

    public static Response handleRequest(Request request, String directory) throws IOException {
        return switch (request.getMethod()) {
            case "GET" -> handleGet(request, directory);
            case "POST" -> handlePost(request, directory);
            default -> null;
        };
    }


    public static Response handleGet(Request request, String directory) throws IOException {
        var uri = request.getUri();
        Response response;
        if (uri.equals("/") || uri.startsWith("/echo/")) {
            var extractedString = extractStringFromPath(uri);
            response = oKWithContent(extractedString);
        } else if (uri.startsWith("/user-agent")) {
            var userAgent = request.getHeaders().get("User-Agent");
            response = oKWithContent(userAgent);
        } else if(uri.startsWith("/files/")){
            //process directory
            var fileName = extractFileName(uri);
            var pathFile = Path.of(directory, fileName);
            if(Files.exists(pathFile) && Files.isRegularFile(pathFile)){
                response = okWithFileContents(pathFile);
            }else{
                response = NotFound();
            }
        } else {
            response = NotFound();
        }
        return response;
    }

    public static Response handlePost(Request request, String directory) throws IOException {
        var uri = request.getUri();
        if(uri.startsWith("/files/")){
            var filePath = Path.of(directory, Path.of(uri).getFileName().toString());
            if(!Files.exists(filePath)){
                Files.createFile(filePath);
            }
            //var contentLength = Integer.parseInt(request.getHeaders().get("Content-Length"));
            try (var fileOutputStream = new FileOutputStream(filePath.toFile())) {
                var bodyBytes = request.getBody().getBytes();
                for(byte b : bodyBytes){
                    fileOutputStream.write(b);
                }
                fileOutputStream.flush();
            }
            return new Response(
                    "HTTP/1.1",
                    201,
                    "Created",
                    Map.of(),
                    ""
                    );
        }else{
            //return error
            return NotFound();
        }
    }

    private static Response okWithFileContents(Path pathFile) throws IOException {
        var size = Files.size(pathFile);
        var response = new Response(
            "HTTP/1.1",
                200,
                "OK",
                Map.of(
                    "Content-Type", "application/octet-stream",
                        "Content-Length", String.valueOf(size)
                ),
                ""
        );
        response.setFilePath(pathFile);
        return response;
    }

    private static String extractStringFromPath(String path) {
        if (path.startsWith("/echo/")) {
            return path.substring(6);
        } else {
            return "";
        }
    }

    private static String extractFileName(String path){
        var filename = Path.of(path).getFileName();
        return filename.toString();
    }

    private static Response oKWithContent(String content) {
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
