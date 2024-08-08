package fr.corentin_owen.communications;

import fr.corentin_owen.car.Car;
import fr.corentin_owen.car.Model;
import fr.corentin_owen.config.Config;
import fr.corentin_owen.config.TypePort;
import fr.corentin_owen.delivery.Deliverer;
import fr.corentin_owen.messages.Message;
import fr.corentin_owen.parking.Parking;
import fr.corentin_owen.parking.Spot;
import fr.corentin_owen.security.Certificate;
import fr.corentin_owen.security.KeyType;
import fr.corentin_owen.utils.*;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Class {@link PeugeotFactory}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class PeugeotFactory extends Surveillant {

    /**
     * Attribute(s)
     */
    private static final String name = "PeugeotFactory";

    private static final List<Parking> parkings = new ArrayList<>();

    public static int peugeotFactoryPortTCP;
    public static int peugeotFactoryPortUDP;

    private static int peugeotProviderPortUDP;

    private static char scenario;

    private static byte[] publicKey;
    private static byte[] privateKey;
    private static Certificate certificate;


    /**
     * Constructor by default
     */
    public PeugeotFactory() {
        super();
        this.menu.add("View cars in stock.");
        this.menu.add("Add car in stock.");
        this.menu.add("Delete car with VIN code.");
    }

    /**
     * Initialisation of factory at rin of main
     */
    public static void initFactory() {
        Parking p1 = new Parking(1, 1);
        Parking p2 = new Parking(2, 3);
        addParking(p1);
        addParking(p2);
        if (scenario == 'A') {
            System.out.println("A random car has been added to the factory to test scenario A. Enter 1 to know the model.");
            addRandomTeslaCar();
        }
    }

    /**
     * Main methode
     *
     * @param args args
     */
    public static void main(String[] args) {
        //-----------------------------Set scenario--------------------------------

        scenario = Config.getScenario();
        System.out.println("-- Scenario: " + scenario + " --\n");

        //-----------------------------Set port--------------------------------
        peugeotFactoryPortTCP = Config.getPort(PeugeotFactory.class.getSimpleName(), TypePort.TCP);
        peugeotFactoryPortUDP = Config.getPort(PeugeotFactory.class.getSimpleName(), TypePort.UDP);

        if (peugeotFactoryPortTCP == -1 || peugeotFactoryPortUDP == -1) {
            System.out.println("Ports have not been or are incorrectly configured. In the \"config/ports.json\" file, the ports must not be equal to -1.");
            System.exit(1);
        }


        ServerSocket tcpServerSocket = TCPUtils.createServerSocket(peugeotFactoryPortTCP);
        DatagramSocket udpSocket = UDPUtils.createSocket(peugeotFactoryPortUDP);

        //-----------------------------TCP Sockets--------------------------------
        Socket clientSocket = TCPUtils.createClientSocket(tcpServerSocket);


        //-----------------------------UDP Sockets--------------------------------
        String receivePort = UDPUtils.receiveUDPMessage(udpSocket);
        peugeotProviderPortUDP = Integer.parseInt(receivePort);


        System.err.println(PeugeotFactory.class.getSimpleName() + "\n- TCP: " + peugeotFactoryPortTCP + "\n- UDP: " + peugeotFactoryPortUDP + "\n- Provider UDP: " + peugeotProviderPortUDP);

        System.out.println("\n--------------------------------------------------------------------------------\nProcessing: \n");
        //-----------------------------Initialisation Tesla factory--------------------------------
        var thread = new Thread(new PeugeotFactory());
        thread.start();


        initFactory();

        //-----------------------------Processing--------------------------------
        Message message;
        Message response;
        while (true) {
            String str = TCPUtils.receiveTCPMessage(clientSocket);
            message = Message.fromJson(new JSONObject(str));

            switch (message.getId()) {
                //check if model is in stock
                case "verificationModel":
                    System.out.println("\n--------------------------------------------------------------------------------\n");
                    System.out.println("\nCheck if the model is in stock...\n");
                    Model model = Model.fromJson(new JSONObject(message.getContent()));
                    boolean stock = existsModel(model);
                    response = new Message("responseVerificationModel", stock);
                    TCPUtils.sendTCPMessage(clientSocket, response.toJson().toString());
                    System.out.println("\n -> Verification finish.");
                    break;

                case "constructCar":
                    //construct car instantly and no delivery
                    if (scenario == 'B') {
                        System.out.println("\n--------------------------------------------------------------------------------\n");
                        System.out.println("\nConstruction in progress...\n");
                        Car car = Car.fromJson(new JSONObject(message.getContent()));

                        String vin = VinUtils.generateCodeVin();
                        car.setVin(vin);
                        if (availableParking()) {
                            Parking p = getSuitableParking();
                            p.addCar(car);
                            response = new Message("responseConstruction", vin, true);
                        } else {
                            response = new Message("responseConstruction", false);
                        }
                        TCPUtils.sendTCPMessage(clientSocket, response.toJson().toString());

                        System.out.println("\n -> Construction finish\n");
                    }

                    //construct car when provider has stock and delivery it
                    else {
                        System.out.println("\n--------------------------------------------------------------------------------\n");
                        System.out.println("\nVerification stock...\n");

                        Car car = Car.fromJson(new JSONObject(message.getContent()));
                        String strPieces = "engine(" + car.getEngine().getCarburation() + "), color(" + car.getColor() + ")";
                        if (!car.getOptions().equals("")) {
                            strPieces += "," + car.getOptions();
                        }

                        String[] pieces = strPieces.split(",");

                        for (String piece : pieces) {
                            message = new Message("stockRequest", piece);
                            boolean stockProvider = false;
                            int stopInfiniteLoop = 0;
                            while (!stockProvider || stopInfiniteLoop > 2000) {
                                UDPUtils.sendUDPMessage(udpSocket, message.toJson().toString(), peugeotProviderPortUDP);
                                String srtResponse = UDPUtils.receiveUDPMessage(udpSocket);
                                response = Message.fromJson(new JSONObject(srtResponse));
                                stopInfiniteLoop++;
                                if (response.getBool()) {
                                    stockProvider = true;
                                }
                            }
                        }

                        System.out.println(" -> The provider has sufficient stock.");
                        System.out.println("\n--------------------------------------------------------------------------------\n");
                        System.out.println("\nConstruction in progress...\n");

                        String vin = VinUtils.generateCodeVin();
                        car.setVin(vin);
                        System.out.println("Car: " + car.toJson().toString());

                        System.out.println("\n -> Construction finish\n");

                        System.out.println("\n--------------------------------------------------------------------------------\n");
                        System.out.println("\nSetting up a deliverer...\n");
                        String key = AESUtils.generateRandomKey();

                        Deliverer deliverer = new Deliverer(key, car);


                        JSONObject jsonObject = FilesUtils.deserializeJSONFile("PHP/dealer/orders/informations_order.json");
                        if(jsonObject != null){
                            String nb = String.valueOf(jsonObject.getInt("nb_order") - 1);
                            FilesUtils.serializeJSONFile("Order/Car/", "car_" + nb, car.toJson());
                            FilesUtils.serializeJSONFile("Order/Deliverer/", "deliverer_" + nb, deliverer.toJSON());
                        }

                        response = new Message("responseConstruction", key);
                        TCPUtils.sendTCPMessage(clientSocket, response.toJson().toString());
                        System.out.println(" -> The car was entrusted to a delivery person.");
                    }
                    break;

            }
        }
    }


    /**
     * Add a random car of Tesla brand
     */
    public static void addRandomTeslaCar() {
        Car car = null;
        while (car == null || !car.getModel().getBrand().equals("Peugeot")) {
            car = Car.createRandomCar();
        }
        getSuitableParking().addCar(car);
    }

    /**
     * Add a parking to factory
     *
     * @param parking the parking
     */
    public static void addParking(Parking parking) {
        parkings.add(parking);
    }


    /**
     * Remove a parking to factory
     *
     * @param idParking the id parking
     */
    public static void removeParking(int idParking) {
        parkings.remove(getParking(idParking));
    }

    /**
     * Get a parking with is id
     *
     * @param idParking parking id
     * @return the parking
     */
    public static Parking getParking(int idParking) {
        return parkings.stream().filter(p -> p.getId() == idParking)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No parking found with the id: " + idParking));
    }

    /**
     * Get the Json of car with is vin code
     *
     * @param codeVin the vin code iof car
     * @return the string of json car
     */
    public static String getJSONCarByCodeVin(String codeVin) {
        Car car = null;
        for (Parking parking : parkings) {
            for (Spot pdp : parking.getSpots()) {
                if (pdp.getCar().getVin().equals(codeVin)) {
                    car = pdp.getCar();
                    break;
                }
            }
        }
        if (car == null) {
            System.out.println("No cars found in the " + name + " factory with the vin code: " + codeVin);
            return null;
        } else {
            return car.toJson().toString();
        }
    }

    /**
     * Get car by is vin code
     *
     * @param vin vin code of car
     * @return the car
     */
    public static Car getCarByCodeVin(String vin) {
        return Car.fromJson(new JSONObject(Objects.requireNonNull(getJSONCarByCodeVin(vin))));
    }

    /**
     * Check if a car already exists in factory
     *
     * @param car the ca
     * @return true or false
     */
    public static boolean alreadyContainsCar(Car car) {
        return parkings.stream()
                .flatMap(parking -> parking.getSpots().stream())
                .anyMatch(spot -> spot.getCar().equals(car));
    }

    /**
     * Check if a model already exists in factory
     *
     * @param model the model
     * @return true or false
     */
    public static boolean existsModel(Model model) {
        return parkings.stream()
                .flatMap(parking -> parking.getSpots().stream())
                .anyMatch(spot -> spot.getCar().hasSameModel(model));
    }

    /**
     * Check if there is still parking available
     *
     * @return true or false
     */
    public static boolean availableParking() {
        return parkings.stream().anyMatch(parking -> !parking.isFull());
    }

    /**
     * Get a available parking (not full)
     *
     * @return a available parking
     */
    public static Parking getSuitableParking() {
        return parkings.stream().filter(parking -> !parking.isFull()).findFirst().get();
    }

    /**
     * Apply the choice of the Surveillant to interact with Factory
     *
     * @param choice user choice
     */
    @Override
    public void appliqueChoix(int choice) {
        switch (choice) {
            case 1:
                boolean empty = true;
                for (Parking parking : parkings) {
                    for (Spot spot : parking.getSpots()) {
                        if (spot.getCar() != null) {
                            empty = false;
                            System.out.println("-------------Parking #" + parking.getId() + " | Spot: " + spot.getStringSpot() + "-------------\n");
                            System.out.println(spot.getCar() + "\n");
                            System.out.println("-------------------------------------------------------");
                        }
                    }
                }
                if (empty) {
                    System.out.println("The parkings of this factory are empty.");
                }
                break;
            case 2:
                addRandomTeslaCar();
                System.out.println("You have added a car in stock");
                break;

            case 3:
                Scanner cl = new Scanner(System.in);
                System.out.print("Code VIN: ");
                String code = cl.nextLine();
                Optional<Spot> spotToRemoveCar = parkings.stream()
                        .flatMap(parking -> parking.getSpots().stream())
                        .filter(spot -> spot.getCar().getVin().equals(code))
                        .findFirst();
                if (spotToRemoveCar.isPresent()) {
                    spotToRemoveCar.get().setCar(null);
                    System.out.println("Car with vin code " + code + " has been deleted.");
                } else {
                    System.out.println("No cars found in the " + name + " factory with the vin code: " + code);
                }
                break;
        }
    }
}
