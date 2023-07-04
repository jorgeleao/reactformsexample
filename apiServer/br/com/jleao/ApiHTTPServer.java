
//Compile: javac -cp ./ br/com/jleao/ApiHTTPServer.java
//Run:     java -cp ./ br.com.jleao.ApiHTTPServer

package br.com.jleao;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

public class ApiHTTPServer {
    public static int port = 8080;

    public static Semaphore mutex = new Semaphore(1,true);
    
    public static String valueJSON = "{'value':60}";
    
    public static void main(String[] args) throws IOException {
//====================================================================        
        
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/value", (exchange -> {
        //-------------------------------------------------------------------            
        //  curl.exe -X GET   http://localhost:8080/value

            if ("GET".equals(exchange.getRequestMethod())) {
                String responseText = "";
                try{
                    mutex.acquire();
                    responseText = valueJSON;
                    mutex.release();
                }catch(Exception e){
                    e.printStackTrace();
                }

                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");   // The server tells it allows localhost:3000 !!
                exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                exchange.getResponseHeaders().add("Accept","application/json, text/plain, */*");   // The server tells it allows localhost:3000 !!
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
System.out.println("=== GET  responseText: "+responseText);                
                OutputStream output = exchange.getResponseBody();
                output.write(responseText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        
//====================================================================

        server.createContext("/postvalue", (exchange -> {
        //  curl.exe -X POST  -d "{'value': 300}" http://localhost:8080/postvalue
            if ("POST".equals(exchange.getRequestMethod())) {
                
                InputStream input = exchange.getRequestBody();
                byte[] inputBytes = new byte[200];
                int n = input.read(inputBytes);
                String localvalueJSON = (new String(inputBytes,StandardCharsets.UTF_8)).substring(0,n);
                try{
                    mutex.acquire();
                    valueJSON = localvalueJSON;
                    mutex.release();
                }catch(Exception e){
                    e.printStackTrace();
                }
                String responseText = localvalueJSON;

                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");   // The server tells it allows localhost:3000 !!
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
                exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                exchange.getResponseHeaders().set("contentType", "application/json; charset=UTF-8, text/plain, */*");

                exchange.sendResponseHeaders(200, responseText.getBytes().length);
System.out.println("=== POST responseText: "+responseText);                
                OutputStream output = exchange.getResponseBody();
                output.write(responseText.getBytes());
                output.flush();
    
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        
//====================================================================        
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("==== Server running at port "+port);
    }
}
