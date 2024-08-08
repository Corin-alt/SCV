package fr.corentin_owen.communications.http;

import com.sun.net.httpserver.HttpServer;
import fr.corentin_owen.communications.http.handlers.PeugeotConstructorHandler;
import fr.corentin_owen.config.Config;
import fr.corentin_owen.config.TypePort;
import fr.corentin_owen.utils.FilesUtils;
import fr.corentin_owen.utils.SocketUtils;
import fr.corentin_owen.utils.TCPUtils;
import org.json.JSONObject;

import java.net.Socket;

/**
 * Class {@link PeugeotDealerShip}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class PeugeotDealerShip {
    /**
     * Attribute(e)
     */
    private static char scenario;
    private static int peugeotDealerShipPortTCP;

    /**
     * Main method
     *
     * @param args args
     */
    public static void main(String[] args) {
        scenario = Config.getScenario();
        System.out.println("-- Scenario: " + scenario + " --\n");

        peugeotDealerShipPortTCP = Config.getPort(PeugeotDealerShip.class.getSimpleName(), TypePort.TCP);

        if(peugeotDealerShipPortTCP == -1){
            System.out.println("Ports have not been or are incorrectly configured. In the \"config/ports.json\" file, the ports must not be equal to -1.");
            System.exit(1);
        }

        HttpServer serveur = SocketUtils.getAvailableHTPPServer();

        System.out.println("PeugotDealerShip \n- HTTP port: " + serveur.getAddress().getPort() + "\n- TCP port: " + peugeotDealerShipPortTCP);

        System.out.println("\n--------------------------------------------------------------------------------\nProcessing: \n");


        setPortForPHPServer(serveur.getAddress().getPort());

        Socket socket = TCPUtils.createClientSocket("localhost", peugeotDealerShipPortTCP);

        serveur.createContext("/constructor", new PeugeotConstructorHandler(scenario, socket));
        serveur.setExecutor(null);
        serveur.start();
    }

    /**
     * Set the port of constructor in the php file
     *
     * @param port the port
     */
    public static void setPortForPHPServer(int port) {
        String name = PeugeotDealerShip.class.getSimpleName().replace("DealerShip", "");
        JSONObject jsonObject = FilesUtils.deserializeJSONFile("PHP/dealer/catalog/" + name + ".json");
        if (jsonObject != null) {
            jsonObject.put("port", String.valueOf(port));
            FilesUtils.serializeJSONFile("PHP/dealer/catalog/", name, jsonObject);
        }
    }
}

