package httpserver;

import http.request.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

class RequestHandlerTest {
    @Test
    void requestWithEchoShouldReturnPathInBody() throws IOException {
        Request request = new Request("GET", "/echo/charles/papa/tango", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleGet(request, null);
        Assertions.assertTrue(response.getBody().contains("charles/papa/tango"));
    }

    @Test
    void requestWithRootPathShouldReturnOk() throws IOException {
        Request request = new Request("GET", "/", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleGet(request, null);
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    void requestWithUserAgentPathShouldReturnUserAgentInBody() throws IOException {
        Request request = new Request("GET", "/user-agent", "HTTP/1.1",
                Map.of("User-Agent", "test-agent-123"),
                ""
        );
        var response = RequestHandler.handleGet(request, null);
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("test-agent-123"));
    }

    @Test
    void otherRequestsShouldReturn404NotFound() throws IOException {
        Request request = new Request("GET", "/bazinga", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleGet(request, null);
        Assertions.assertEquals(404, response.getStatusCode());
    }

    @Test
    void requestFile() throws IOException {
        String directory = "src/test/java/files";
        Request request = new Request("GET", "/files/testFile.txt", "HTTP/1.1", Map.of(), "");
        var response = RequestHandler.handleGet(request, directory);
        Assertions.assertEquals(200, response.getStatusCode());
        System.out.println(response);
    }

    @Test
    void handlePostOK() throws IOException {

        String directory = "src/test/java/files";


        String data = """
                La candente mañana de febrero en que Beatriz Viterbo murió, después de una imperiosa agonía que no se
                rebajó un solo instante ni al sentimentalismo ni al miedo, noté que las carteleras de fierro de la
                Plaza Constitución habían renovado no sé qué aviso de cigarrillos rubios; el hecho me dolió,
                pues comprendí que el incesante y vasto universo ya se apartaba de ella y que ese cambio era el 
                primero de una serie infinita. Cambiará el universo pero yo no, pensé con melancólica vanidad; 
                alguna vez, lo sé, mi vana devoción la había exasperado; muerta, yo podía consagrarme a su memoria, 
                sin esperanza, pero también sin humillación. Consideré que el 30 de abril era su cumpleaños; visitar 
                ese día la casa la calle Garay para saludar a su padre y a Carlos Argentino Daneri, su primo hermano, 
                era un acto cortés, irreprochable, tal vez ineludible. De nuevo aguardaría en el crepúsculo de la 
                abarrotada salita, de nuevo estudiaría las circunstancias de sus muchos retratos, Beatriz Viterbo, de 
                perfil, en colores; Beatriz, con antifaz, en los carnavales de 1921; la primera comunión de Beatriz; 
                Beatriz, el día de su boda con Roberto Alessandri; Beatriz, poco después del divorcio, en un almuerzo 
                del Club Hípico; Beatriz, en Quilmes, con Delia San Marco Porcel y Carlos Argentino; Beatriz, con el 
                pekinés que le regaló Villegas Haedo; Beatriz, de frente y de tres cuartos, sonriendo; la mano en el 
                mentón… No estaría obligado, como otras veces, a justificar mi presencia con módicas ofrendas de 
                libros: libros cuyas páginas, finalmente, aprendí a cortar, para no comprobar, meses después, 
                que estaban intactos.
                """;
        var contentLength = String.valueOf(data.getBytes().length);
        Request request = new Request("POST", "/files/testPostOK.txt", "HTTP/1.1",
        Map.of("Content-Length", contentLength,
                "Content-Type", "application/octet-stream"
                ),
                data
        );
        var response = RequestHandler.handleRequest(request, directory);
        System.out.print(response);
        Assertions.assertEquals(201, response.getStatusCode());
    }

    @Test
    void handlePostNotFound() throws IOException {
        String directory = "src/test/java/files";
        String data = """
                La candente mañana de febrero en que Beatriz Viterbo murió, después de una imperiosa agonía que no se
                rebajó un solo instante ni al sentimentalismo ni al miedo, noté que las carteleras de fierro de la
                Plaza Constitución habían renovado no sé qué aviso de cigarrillos rubios; el hecho me dolió,
                pues comprendí que el incesante y vasto universo ya se apartaba de ella y que ese cambio era el 
                primero de una serie infinita. Cambiará el universo pero yo no, pensé con melancólica vanidad; 
                alguna vez, lo sé, mi vana devoción la había exasperado; muerta, yo podía consagrarme a su memoria, 
                sin esperanza, pero también sin humillación. Consideré que el 30 de abril era su cumpleaños; visitar 
                ese día la casa la calle Garay para saludar a su padre y a Carlos Argentino Daneri, su primo hermano, 
                era un acto cortés, irreprochable, tal vez ineludible. De nuevo aguardaría en el crepúsculo de la 
                abarrotada salita, de nuevo estudiaría las circunstancias de sus muchos retratos, Beatriz Viterbo, de 
                perfil, en colores; Beatriz, con antifaz, en los carnavales de 1921; la primera comunión de Beatriz; 
                Beatriz, el día de su boda con Roberto Alessandri; Beatriz, poco después del divorcio, en un almuerzo 
                del Club Hípico; Beatriz, en Quilmes, con Delia San Marco Porcel y Carlos Argentino; Beatriz, con el 
                pekinés que le regaló Villegas Haedo; Beatriz, de frente y de tres cuartos, sonriendo; la mano en el 
                mentón… No estaría obligado, como otras veces, a justificar mi presencia con módicas ofrendas de 
                libros: libros cuyas páginas, finalmente, aprendí a cortar, para no comprobar, meses después, 
                que estaban intactos.
                """;
        var contentLength = String.valueOf(data.getBytes().length);
        Request request = new Request("POST", "/notSuchDirectory/testPostOK.txt", "HTTP/1.1",
                Map.of("Content-Length", contentLength,
                        "Content-Type", "application/octet-stream"
                ),
                data
        );
        var response = RequestHandler.handleRequest(request, directory);
        System.out.print(response);
        Assertions.assertEquals(404, response.getStatusCode());
    }
}