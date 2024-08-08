package fr.corentin_owen.utils;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * Class {@link HTTPUtils} which is a utility for socket
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class SocketUtils {

    /**
     * Attribute(s)
     */
    private static int BEGIN_HTTP_PORT = 8000;

    /**
     * Methode to get available HTPP server
     *
     * @return http server
     */
    public static HttpServer getAvailableHTPPServer() {
        int finalPort = BEGIN_HTTP_PORT;
        BEGIN_HTTP_PORT++;
        HttpServer serveur = null;
        while (serveur == null) {
            try {
                serveur = HttpServer.create(new InetSocketAddress(finalPort), 0);
            } catch (IOException e) {
                finalPort++;
            }
        }
        if (finalPort + 1 != BEGIN_HTTP_PORT) {
            BEGIN_HTTP_PORT = finalPort;
        }
        return serveur;
    }
}
