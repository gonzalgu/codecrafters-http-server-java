package httpserver;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        Server server = new Server("localhost", 4221);
        server.start();
    }
}
