package fr.corentin_owen.communications.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.corentin_owen.car.Car;
import fr.corentin_owen.delivery.Deliverer;
import fr.corentin_owen.messages.Message;
import fr.corentin_owen.messages.ResponseMessage;
import fr.corentin_owen.security.Certificate;
import fr.corentin_owen.utils.AESUtils;
import fr.corentin_owen.utils.FilesUtils;
import fr.corentin_owen.utils.HTTPUtils;
import fr.corentin_owen.utils.TCPUtils;
import org.json.JSONObject;

import java.net.Socket;

/**
 * Class {@link PeugeotConstructorHandler}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class PeugeotConstructorHandler implements HttpHandler {

    /**
     * Attribute(s)
     */
    private final char scenario;
    private Socket socket;

    private static String decryptKey;
    private static int numOrder = 0;

    private static byte[] publicKey;
    private static byte[] privateKey;
    private static Certificate certificate;


    /**
     * Constructor by default
     */
    public PeugeotConstructorHandler(char scenario, Socket socket) {
        this.scenario = scenario;
        this.socket = socket;
    }


    /**
     * Method which listen HTTP context "/constructor"
     *
     * @param t the exchange
     */
    @Override
    public void handle(HttpExchange t) {
        String response;
        Message message;
        Message messageResponse;

        String query = HTTPUtils.receiveHTTPMessage(t);
        message = Message.fromJson(new JSONObject(query));

        switch (message.getId()) {
            case "order":
                ResponseMessage jsonResponse;

                JSONObject order = new JSONObject(message.getContent());
                numOrder = order.getInt("num_order");
                System.out.println("\nOrder (" + numOrder + "): \n");
                System.out.println(" -> Client: " + order.getString("client") + "\n");

                JSONObject jsonCar = order.getJSONObject("car");
                System.out.println(" -> Order: " + order);

                System.out.println("\n--------------------------------------------------------------------------------\n");


                //request factory to check if model is in stock
                System.out.println("\nChecking the availability of the model\n");
                JSONObject modelCar = jsonCar.getJSONObject("model");
                message = new Message("verificationModel", modelCar.toString());
                TCPUtils.sendTCPMessage(socket, message.toJson().toString());


                response = TCPUtils.receiveTCPMessage(socket);
                messageResponse = Message.fromJson(new JSONObject(response));


                boolean stock = messageResponse.getBool();
                if (stock) {
                    System.out.println(" -> The model is already in stock.");
                    jsonResponse = new ResponseMessage("orderPassed", false, "Order received.");
                    HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                } else {
                    if (scenario != 'A') {
                        System.out.println(" -> The model is not in stock. The construction has to be started.");
                        message = new Message("constructCar", jsonCar.toString());
                        TCPUtils.sendTCPMessage(socket, message.toJson().toString());

                        System.out.println("\n--------------------------------------------------------------------------------\n");
                        System.out.println("\nLaunch construction...\n");

                    } else {
                        System.out.println(" -> The model is not in stock");
                    }
                }

                response = TCPUtils.receiveTCPMessage(socket);
                messageResponse = Message.fromJson(new JSONObject(response));

                if (scenario == 'B') {
                    boolean state = messageResponse.getBool();
                    if (state) {
                        System.out.println("\n -> The car was well built. It has as its vin code: " + messageResponse.getContent());
                    } else {
                        System.out.println(" -> No more parking available. Construction has been cancelled.\n");
                    }
                    jsonResponse = new ResponseMessage("orderPassed", false, "Order received.");
                    HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());

                } else if (scenario == 'C') {
                    decryptKey = messageResponse.getContent();
                    System.out.println(" -> The car has been delivered.");
                    System.out.println(" -> KEY: " + decryptKey);

                    jsonResponse = new ResponseMessage("orderPassedAndDelivery", false, "Order received.");
                    HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                }
                break;


            case "informationDelivery":
                if (scenario == 'C') {
                    JSONObject content = new JSONObject();
                    content.put("order", numOrder);
                    content.put("key", decryptKey);
                    jsonResponse = new ResponseMessage("informationDelivery", false, content.toString());
                    HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                } else {
                    jsonResponse = new ResponseMessage("informationDelivery", true, "bad_scenario");
                    HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                }
                break;

            case "delivery":
                if (scenario == 'C') {

                    JSONObject jsonObject = new JSONObject(message.getContent());
                    String numOrder = jsonObject.getString("num_order");
                    String key = jsonObject.getString("key");

                    if (!key.equals(decryptKey)) {
                        jsonResponse = new ResponseMessage("delivery", true, "bad_key");
                        HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                    }

                    JSONObject delivererJson = FilesUtils.deserializeJSONFile("Order/Deliverer/deliverer_" + numOrder + ".json");

                    if (delivererJson != null) {
                        Deliverer deliverer = Deliverer.fromJSON(delivererJson);

                        String decrypt = AESUtils.decrypt(key, deliverer.getEncryptCar());
                        Car car = Car.fromJson(new JSONObject(decrypt));

                        jsonResponse = new ResponseMessage("delivery", false, car.toJson().toString());
                        HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                    } else {
                        jsonResponse = new ResponseMessage("delivery", true, "bad_order_number");
                        HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                    }
                } else {
                    jsonResponse = new ResponseMessage("delivery", true, "bad_scenario");
                    HTTPUtils.sendHTTPMessage(t, jsonResponse.toJson().toString());
                }
                break;
        }
    }
}
