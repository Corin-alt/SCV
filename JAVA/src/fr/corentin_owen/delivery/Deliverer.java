package fr.corentin_owen.delivery;

import fr.corentin_owen.car.Car;
import fr.corentin_owen.utils.AESUtils;
import org.json.JSONObject;

/**
 * Class {@link Deliverer}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Deliverer {

    /**
     * Attribute(e)
     */
    private final String encryptCar;

    /**
     * Constructor by initialisation
     * @param encryptKey the key used to encrypt
     * @param car the car to encrypt
     */
    public Deliverer(String encryptKey, Car car) {
        this.encryptCar = AESUtils.encrypt(encryptKey, car.toJson().toString());
    }

    /**
     * Constructor by initialisation
     * @param encryptCar the encrypted car
     */
    public Deliverer(String encryptCar) {
        this.encryptCar = encryptCar;
    }

    /**
     * Transform the deliverer to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("encryptCar", this.encryptCar);
        return jsonObject;
    }

    /**
     * Create a Deliverer from json
     *
     * @param jsonObject the json
     * @return the deliverer
     */
    public static Deliverer fromJSON(JSONObject jsonObject){
        return new Deliverer(jsonObject.getString("encryptCar"));
    }

    /**
     * Get the encrypted car
     * @return the encrypted car
     */
    public String getEncryptCar() {
        return encryptCar;
    }
}
