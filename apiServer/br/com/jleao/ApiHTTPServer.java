/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

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
    
    public static String valueJSON = "{\"value\":60}";
    
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
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                
                OutputStream output = exchange.getResponseBody();
                output.write(responseText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
//        }));
        //-------------------------------------------------------------------            
        //  curl.exe -X POST  -d "{'value': 300}" http://localhost:8080/value
            if ("POST".equals(exchange.getRequestMethod())) {
                
                InputStream input = exchange.getRequestBody();
                byte[] inputBytes = new byte[200];
                int n = input.read(inputBytes);
System.out.println("=== n="+n);                
                String localvalueJSON = (new String(inputBytes,StandardCharsets.UTF_8)).substring(0,n);
                try{
                    mutex.acquire();
                    valueJSON = localvalueJSON;
                    mutex.release();
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                String responseText = localvalueJSON;
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(responseText.getBytes());
                output.flush();
    
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        //-------------------------------------------------------------------            

        
//====================================================================
/*
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
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(responseText.getBytes());
                output.flush();
    
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
*/        
//====================================================================        
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("==== Server running at port "+port);
    }
}
