package fr.corentin_owen.communications.udp;

import fr.corentin_owen.communications.PeugeotFactory;
import fr.corentin_owen.communications.TeslaFactory;
import fr.corentin_owen.config.Config;
import fr.corentin_owen.config.TypePort;
import fr.corentin_owen.messages.Message;
import fr.corentin_owen.security.Certificate;
import fr.corentin_owen.security.KeyType;
import fr.corentin_owen.utils.RSAUtils;
import fr.corentin_owen.utils.UDPUtils;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.util.Map;

/**
 * Class {@link PeugeotProvider}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class PeugeotProvider {

    /**
     * Attribute(s)
     */
    private static int numberRequests = 0;

    private static int peugeotProviderPortUDP;
    private static int peugeotFactoryPortUDP;

    private static char scenario;

    private static byte[] publicKey;
    private static byte[] privateKey;
    private static Certificate certificate;

    /**
     * Main method
     *
     * @param args args
     */
    public static void main(String[] args) {
        //-----------------------------Set scenario--------------------------------

        scenario = Config.getScenario();
        System.out.println("-- Scenario: " + scenario + " --\n");


        //-----------------------------Set port--------------------------------

        peugeotProviderPortUDP = Config.getPort(PeugeotProvider.class.getSimpleName(), TypePort.UDP);
        peugeotFactoryPortUDP = Config.getPort(PeugeotFactory.class.getSimpleName(), TypePort.UDP);

        if(peugeotProviderPortUDP == -1 || peugeotFactoryPortUDP == -1){
            System.out.println("Ports have not been or are incorrectly configured. In the \"config/ports.json\" file, the ports must not be equal to -1.");
            System.exit(1);
        }


        System.out.println(PeugeotProvider.class.getSimpleName() + "\n- Provider UDP: " + peugeotProviderPortUDP + "\n- Factory UDP: " + peugeotFactoryPortUDP);

        System.out.println("\n--------------------------------------------------------------------------------\nProcessing: \n");

        /**
         * Creation of sockets
         */
        DatagramSocket socket = UDPUtils.createSocket(null);
        DatagramSocket socket_receive = UDPUtils.createSocket(peugeotProviderPortUDP);


        //send provider port to factory
        UDPUtils.sendUDPMessage(socket, String.valueOf(peugeotProviderPortUDP), peugeotFactoryPortUDP);


        //-----------------------------Processing--------------------------------

        Message message;
        Message response = null;
        String answer;
        while(true){
            String request = UDPUtils.receiveUDPMessage(socket_receive);
            message = Message.fromJson(new JSONObject(request));

            switch (message.getId()){
                case  "stockRequest":
                    if (1 / (1 + Math.pow(0.8, numberRequests)) > Math.random() || numberRequests > 2000) {
                        answer = "Stock unavailable for: " + message.getContent();
                        response = new Message("responseStockRequest", answer, false);
                        System.out.println("\n- Request " + numberRequests +  "\n -> " + answer + "\n");
                    } else {
                        answer = "Stock available for: " +  message.getContent();
                        response = new Message("responseStockRequest", answer, true);
                        System.out.println("\n- Request " + numberRequests +  "\n -> " + answer + "\n");
                        numberRequests = 0;
                    }
                    numberRequests++;
                    break;
            }

            if(response != null){
                UDPUtils.sendUDPMessage(socket, response.toJson().toString(), peugeotFactoryPortUDP);
            }
        }
    }
}
