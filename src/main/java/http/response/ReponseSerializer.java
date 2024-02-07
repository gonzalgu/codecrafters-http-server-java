package http.response;

public class ReponseSerializer {
    public static String serialize(Response response){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(response.getProtocolVersion());
        stringBuilder.append(" ");
        stringBuilder.append(response.statusCode);
        stringBuilder.append(" ");
        stringBuilder.append(response.statusText);
        stringBuilder.append("\r\n");
        response.getHeaders().forEach((h,v)->{
            stringBuilder.append(h + ": ");
            stringBuilder.append(v);
            stringBuilder.append("\r\n");
        });
        if(!response.body.isEmpty()){
            stringBuilder.append("\r\n");
            stringBuilder.append(response.body);
            stringBuilder.append("\r\n");
        }
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }
}
