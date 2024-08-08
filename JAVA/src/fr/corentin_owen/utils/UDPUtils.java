package fr.corentin_owen.utils;

import java.io.IOException;
import java.net.*;

/**
 * Class {@link UDPUtils} which is a utility for UDP request
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class UDPUtils {

    /**
     * Method to create a datagram socket
     *
     * @param port the port of datagram socket
     * @return datagram socket
     */
    public static DatagramSocket createSocket(Integer port) {
        DatagramSocket datagramSocket = null;
        try {
            if (port == null) {
                datagramSocket = new DatagramSocket();
            } else {
                datagramSocket = new DatagramSocket(port);
            }
        } catch (SocketException e) {
            System.err.println("Error when creating the socket : " + e);
            System.exit(0);
        }
        return datagramSocket;
    }

    /**
     * Method to receive a UDP message
     *
     * @param socket the socket
     * @return the message received
     */
    public static String receiveUDPMessage(DatagramSocket socket) {
        byte[] tampon = new byte[1024];
        DatagramPacket msg = new DatagramPacket(tampon, tampon.length);
        try {
            socket.receive(msg);
            return new String(msg.getData(), 0, msg.getLength());
        } catch (IOException e) {
            System.err.println("Error when receiving the message: " + e);
            System.exit(0);
        }
        return null;
    }

    /**
     * Methode to send a UDP message
     *
     * @param socket  the socket
     * @param message the message
     * @param port    the port
     */
    public static void sendUDPMessage(DatagramSocket socket, String message, int port) {
        DatagramPacket msg = null;
        try {
            InetAddress adresse = InetAddress.getByName(null);
            byte[] tampon2 = message.getBytes();
            msg = new DatagramPacket(tampon2, tampon2.length, adresse, port);
        } catch (UnknownHostException e) {
            System.err.println("Error while creating the message: " + e);
            System.exit(0);
        }
        try {
            socket.send(msg);
        } catch (IOException e) {
            System.err.println("Error while sending the message: " + e);
            System.exit(0);
        }
    }

}
