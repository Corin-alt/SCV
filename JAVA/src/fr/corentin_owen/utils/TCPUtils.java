package fr.corentin_owen.utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class {@link TCPUtils} which is a utility for TCP request
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class TCPUtils {

    /**
     * Method to create a tcp server socket
     *
     * @param port the port
     * @return tcp server socket
     */
    public static ServerSocket createServerSocket(int port) {
        ServerSocket socketServeur = null;
        try {
            socketServeur = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Creating the impossible socket: " + e);
            System.exit(0);
        }
        return socketServeur;
    }

    /**
     * Method to create a tcp client socket
     *
     * @param serverSocket tcp server socket
     * @return tcp client socket
     */
    public static Socket createClientSocket(ServerSocket serverSocket) {
        Socket socketClient = null;
        try {
            socketClient = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Error while waiting for a connection : " + e);
            System.exit(0);
        }
        return socketClient;
    }

    /**
     * Method to create a tcp client socket
     *
     * @param host the host of socket
     * @param port the port of socket
     * @return tcp client socket
     */
    public static Socket createClientSocket(String host, int port) {
        Socket socketClient = null;
        try {
            socketClient = new Socket(host, port);
        } catch (IOException e) {
            System.err.println("Error while waiting for a connection : " + e);
            System.exit(0);
        }
        return socketClient;
    }

    /**
     * Method to receive a TCP message
     *
     * @param socket the socket
     * @return the message received
     */
    public static String receiveTCPMessage(Socket socket) {
        String message = "";
        BufferedReader input;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = input.readLine();
        } catch (IOException e) {
            System.err.println("Impossible to associate flows: " + e);
            System.exit(0);
        }
        return message;
    }

    /**
     * Method to send a TCP message
     *
     * @param socket the socket
     * @param msg    the message to send
     */
    public static void sendTCPMessage(Socket socket, String msg) {
        PrintWriter output;
        try {
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            output.println(msg);
            output.flush();
        } catch (IOException e) {
            System.err.println("Impossible to associate flows: " + e);
            System.exit(0);
        }
    }
}
