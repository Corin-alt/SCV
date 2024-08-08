package fr.corentin_owen.security;

import fr.corentin_owen.config.Config;
import fr.corentin_owen.config.TypePort;
import fr.corentin_owen.messages.Message;
import fr.corentin_owen.utils.RSAUtils;
import fr.corentin_owen.utils.TCPUtils;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Map;


/**
 * Class {@link ACServer}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class ACServer {

    /**
     * Attribute(s)
     */
    public static int acPortTCP;

    private static byte[] publicKey;
    private static byte[] privateKey;
    private static Certificate certificate;

    /**
     * Main methode
     *
     * @param args args
     */
    public static void main(String[] args) {
        acPortTCP = Config.getPort(ACServer.class.getSimpleName(), TypePort.TCP);

        Map<KeyType, byte[]> keys = RSAUtils.generateKeys();
        privateKey = keys.get(KeyType.PRIVATE);
        publicKey = keys.get(KeyType.PUBLIC);

        System.err.println(ACServer.class.getSimpleName() + "\n- TCP: " + acPortTCP);
        System.out.println("\n--------------------------------------------------------------------------------\nProcessing: \n");

        ServerSocket tcpServerSocket = TCPUtils.createServerSocket(acPortTCP);
        Socket clientSocket = TCPUtils.createClientSocket(tcpServerSocket);

        Message message;
        Message response;
        JSONObject jo;
        while (true) {
            String str = TCPUtils.receiveTCPMessage(clientSocket);
            message = Message.fromJson(new JSONObject(str));

            switch (message.getId()) {

                case "requestCertificate":

                    jo = new JSONObject(message.getContent());
                    System.out.println("\nRequest for certificate by " + jo.getString("name") + "\n");

                    String machine = jo.getString("name");
                    String IP = jo.getString("ip");
                    String pk = jo.getString("publicKey");
                    String ACName = ACServer.class.getSimpleName();
                    String ACIp = tcpServerSocket.getInetAddress().getHostAddress();

                    Certificate certificate = new Certificate(machine, IP, Base64.getDecoder().decode(pk), ACName, ACIp);
                    certificate.sign(privateKey);

                    message = new Message("responseCertificate", certificate.toJson().toString());
                    TCPUtils.sendTCPMessage(clientSocket, message.toJson().toString());

                    System.out.println("\n-> Certification created, signed and sent.");

                    break;

                case "verificationCertificate":

                    jo = new JSONObject(message.getContent());
                    Certificate c = Certificate.fromJson(jo);

                    if(c.checkSignature(Base64.getDecoder().decode(publicKey))){
                        message = new Message("responseVerificationCertificate", true);
                    } else {
                        message = new Message("responseVerificationCertificate", false);
                    }

                    TCPUtils.sendTCPMessage(clientSocket, message.toJson().toString());

                    break;


            }
        }
    }


}
