package fr.corentin_owen.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;

/**
 * Class {@link HTTPUtils} which is a utility for HTTP request
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class HTTPUtils {

    /**
     * Method to receive an HTTP request
     *
     * @param t the http exchange
     * @return the result of request
     */
    public static String receiveHTTPMessage(HttpExchange t) {
        URI requestedUri = t.getRequestURI();
        String query = requestedUri.getRawQuery();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(t.getRequestBody(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error while retrieving the stream: " + e);
            System.exit(0);
        }
        try {
            query = br.readLine();
            return query;
        } catch (IOException e) {
            System.err.println("Error when reading a line: " + e);
            System.exit(0);
        }
        return null;
    }

    /**
     * Method to send an HTTP request
     *
     * @param t       the http exchange
     * @param message the message to send with a http request
     */
    public static void sendHTTPMessage(HttpExchange t, String message) {
        try {
            Headers h = t.getResponseHeaders();
            h.set("Content-Type", "text/html; charset=utf-8");
            t.sendResponseHeaders(200, message.getBytes().length);
        } catch (IOException e) {
            System.err.println("Error when sending header: " + e);
            System.exit(0);
        }
        try {
            OutputStream os = t.getResponseBody();
            os.write(message.getBytes());
            os.close();
        } catch (IOException e) {
            System.err.println("Error when sending the body: " + e);
        }
    }
}
