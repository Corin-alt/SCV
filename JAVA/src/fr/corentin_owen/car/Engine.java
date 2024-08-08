package fr.corentin_owen.car;

import org.json.JSONObject;

/**
 * Class {@link Engine}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Engine {

    /**
     * Attribute(s)
     */
    private final Carburation carburation;
    private final int power;

    /**
     * Constructor by initialisation
     *
     * @param carburation the carburation of engine
     * @param power       the power of engine
     */
    public Engine(Carburation carburation, int power) {
        this.carburation = carburation;
        this.power = power;
    }

    /**
     * Get type of carburation of engine
     *
     * @return the carburation
     */
    public Carburation getCarburation() {
        return carburation;
    }

    /**
     * Get power of engine
     *
     * @return the power
     */
    public int getPower() {
        return power;
    }


    /**
     * Transform the Engine to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("carburation", this.carburation.toString());
        jsonObject.put("power", this.power);
        return jsonObject;
    }

    /**
     * Create an Engine from json
     *
     * @param jsonObject the json
     * @return the new Engine
     */
    public static Engine fromJson(JSONObject jsonObject) {
        return new Engine(Carburation.valueOf(jsonObject.getString("carburation").toUpperCase()), jsonObject.getInt("power"));
    }

    /**
     * Method to check if two engine objects is equals
     *
     * @param obj engine
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Engine)) {
            return false;
        }
        Engine engine = (Engine) obj;
        return this.carburation == engine.getCarburation() && this.power == engine.getPower();
    }

    /**
     * Transform the Engine object in string
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "\nEngine : \n-carburation: " + getCarburation().toString() + "\n-power: " + getPower();
    }
}
