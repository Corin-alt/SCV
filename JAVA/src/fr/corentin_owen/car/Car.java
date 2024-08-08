package fr.corentin_owen.car;

import fr.corentin_owen.utils.VinUtils;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class {@link Car}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Car implements Comparable {

    /**
     * Attribute(s)
     */
    private String vin;
    private final Model model;
    private final Color color;
    private final Engine engine;
    private final String options;

    /**
     * Constructor by initialisation
     *
     * @param vin     the vin code of car
     * @param model   the model code of car
     * @param color   the color code of car
     * @param engine  the engine code of car
     * @param options the options code of car
     */
    public Car(String vin, Model model, Color color, Engine engine, String options) {
        this.vin = vin;
        this.model = model;
        this.color = color;
        this.engine = engine;
        this.options = options;
    }

    /**
     * Get vin code of the car
     *
     * @return the vin code
     */
    public String getVin() {
        return vin;
    }

    /**
     * Set the VIN code
     *
     * @param vin the vin code to set
     */
    public void setVin(String vin) {
        this.vin = vin;
    }

    /**
     * Get model of the car
     *
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Get color of the car
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get engine of the car
     *
     * @return the engine
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Get options of the car
     *
     * @return the options
     */
    public String getOptions() {
        return options;
    }


    /**
     * Transform the Car to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vin", this.vin);
        jsonObject.put("model", this.model.toJson());
        jsonObject.put("color", this.color.toString());
        jsonObject.put("engine", this.engine.toJson());
        jsonObject.put("options", this.options);

        return jsonObject;
    }

    /**
     * Create a Car from json
     *
     * @param jsonObject the json
     * @return the new Car
     */
    public static Car fromJson(JSONObject jsonObject) {
        String vin = jsonObject.getString("vin");
        Model model = Model.fromJson(jsonObject.getJSONObject("model"));
        Color color = Color.valueOf(jsonObject.getString("color").toUpperCase());
        Engine engine = Engine.fromJson(jsonObject.getJSONObject("engine"));
        String options = jsonObject.getString("options");
        return new Car(vin, model, color, engine, options);
    }

    /**
     * Create a random car
     *
     * @return the random car which created
     */
    public static Car createRandomCar() {
        Random rand = new Random();
        List<String> brands = Arrays.asList("Tesla", "Peugeot");
        List<String> modelTesla = Arrays.asList("S", "3", "X", "Y");
        List<String> modelPeugeot = Arrays.asList("208", "308", "3008", "5008");
        String brand = brands.get(rand.nextInt(brands.size()));
        String modelStr = "";
        switch (brand) {
            case "Tesla":
                modelStr = modelTesla.get(rand.nextInt(modelTesla.size()));
                break;
            case "Peugeot":
                modelStr = modelPeugeot.get(rand.nextInt(modelPeugeot.size()));
                break;
        }
        Model model = new Model(brand, modelStr);
        Carburation carburation = Carburation.values()[rand.nextInt(Carburation.values().length)];
        Color color = Color.values()[rand.nextInt(Color.values().length)];
        Engine engine = new Engine(carburation, rand.nextInt(800));
        StringBuilder options = new StringBuilder();
        List<String> availableOptions = Arrays.asList("GPS", "Radio", "Heated seats", "Electric glass");
        int nbOptions = rand.nextInt(availableOptions.size());
        if (nbOptions != 0) {
            for (int i = 0; i < nbOptions; i++) {
                if (i != nbOptions - 1) {
                    options.append(availableOptions.get(i)).append(", ");
                } else {
                    options.append(availableOptions.get(i));
                }
            }
        }

        return new Car(VinUtils.generateCodeVin(), model, color, engine, options.toString());
    }

    /**
     * Check if two cars has the same model
     *
     * @param model the model car
     * @return true or false
     */
    public boolean hasSameModel(Model model) {
        return this.getModel().equals(model);
    }

    /**
     * Check if two cars has the same model
     *
     * @param car the car
     * @return true or false
     */
    public boolean hasSameModel(Car car) {
        return hasSameModel(car.getModel());
    }

    /**
     * Compare two car with their VIN code
     *
     * @param o object to compare
     * @return true or false
     */
    @Override
    public int compareTo(Object o) {
        return this.vin.compareTo(((Car) o).getVin());
    }

    /**
     * Transform the Car object in string
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Car: \n-vin: " + getVin()
                + "\n-color: " + getColor()
                + "\n" + getModel().toString()
                + "\n" + getEngine().toString() + "\n-options: " + getOptions();
    }
}
