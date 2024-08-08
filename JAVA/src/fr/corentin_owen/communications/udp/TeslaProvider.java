package fr.corentin_owen.communications.udp;

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
 * Class {@link TeslaProvider}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class TeslaProvider {

    /**
     * Attribute(s)
     */
    private static int numberRequests = 0;

    private static int teslaProviderPortUDP;
    private static int teslaFactoryPortUDP;

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

        teslaProviderPortUDP = Config.getPort(TeslaProvider.class.getSimpleName(), TypePort.UDP);
        teslaFactoryPortUDP = Config.getPort(TeslaFactory.class.getSimpleName(), TypePort.UDP);

        if(teslaProviderPortUDP == -1 || teslaFactoryPortUDP == -1){
            System.out.println("Ports have not been or are incorrectly configured. In the \"config/ports.json\" file, the ports must not be equal to -1.");
            System.exit(1);
        }


        System.out.println(TeslaProvider.class.getSimpleName() + "\n- Provider UDP: " + teslaProviderPortUDP + "\n- Factory UDP: " + teslaFactoryPortUDP);

        System.out.println("\n--------------------------------------------------------------------------------\nProcessing: \n");

        /**
         * Creation of sockets
         */
        DatagramSocket socket = UDPUtils.createSocket(null);
        DatagramSocket socket_receive = UDPUtils.createSocket(teslaProviderPortUDP);


        //send provider port to factory
        UDPUtils.sendUDPMessage(socket, String.valueOf(teslaProviderPortUDP), teslaFactoryPortUDP);


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
                UDPUtils.sendUDPMessage(socket, response.toJson().toString(), teslaFactoryPortUDP);
            }
        }
    }
}
