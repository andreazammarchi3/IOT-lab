import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args)  throws Exception {
        System.out.println("Hello world!");
        HttpServer server = HttpServer.create(new InetSocketAddress(1201), 0);
        server.createContext("/SmartGarden", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            // extracting barcode from URL
            String myBarcode = exchange.getRequestURI().toString();
            System.out.println("My barcode: " + myBarcode);
            String response = "{myData: \"myText\":\"Like\",\"otherText\":\"Subscribe\"}";
            exchange.sendResponseHeaders(200, response.length());
            os.write(response.getBytes());
            os.close();
        }
    }
}