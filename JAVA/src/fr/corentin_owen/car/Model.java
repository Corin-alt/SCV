package fr.corentin_owen.car;

import org.json.JSONObject;

/**
 * Class {@link Model}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Model {

    /**
     * Attribute(s)
     */
    private final String brand;
    private final String modelName;

    /**
     * Constructor by initialisation
     *
     * @param brand     the brand of model
     * @param modelName the name of model
     */
    public Model(String brand, String modelName) {
        this.brand = brand;
        this.modelName = modelName;
    }

    /**
     * Get brand of model
     *
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Get the model name of model
     *
     * @return the model name
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Transform the Spot to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("brand", this.brand);
        jsonObject.put("model", this.modelName);
        return jsonObject;
    }

    /**
     * Create a Model from json
     *
     * @param jsonObject the json
     * @return the new Model
     */
    public static Model fromJson(JSONObject jsonObject) {
        return new Model(jsonObject.getString("brand"), jsonObject.getString("model"));
    }

    /**
     * Method to check if two models objects is equals
     *
     * @param obj model
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Model)) {
            return false;
        }
        Model model = (Model) obj;
        return this.brand.equals(model.getBrand()) && this.modelName.equals(model.getModelName());
    }

    /**
     * Transform the Model object in string
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "\nModel: \n-brand: " + getBrand() + "\n-model: " + getModelName();
    }
}
