package http.request;

import http.response.Response;

public class RequestSerializer {
    public static String serialize(Request request){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request.getMethod());
        stringBuilder.append(" ");
        stringBuilder.append(request.getUri());
        stringBuilder.append(" ");
        stringBuilder.append(request.getHttpVersion());
        stringBuilder.append("\r\n");
        request.getHeaders().forEach((h,v)->{
            stringBuilder.append(h + ": ");
            stringBuilder.append(v);
            stringBuilder.append("\r\n");
        });
        if(!request.body.isEmpty()){
            stringBuilder.append("\r\n");
            stringBuilder.append(request.body);
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }
}
