package fr.corentin_owen.parking;


import fr.corentin_owen.car.Car;
import org.json.JSONObject;

/**
 * Class {@link Spot}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Spot {

    /**
     * Attribute(s)
     */
    private final int parkingId;
    private Car car;
    private final char letter;
    private final int number;

    /**
     * Constructor by initialisation
     *
     * @param parkingId the if of spot
     * @param car       the car stored this spot
     * @param letter    the letter of spot
     * @param number    the number of spot
     */
    public Spot(int parkingId, Car car, char letter, int number) {
        this.parkingId = parkingId;
        this.car = car;
        this.letter = letter;
        this.number = number;
    }

    /**
     * Get the spot in string
     * @return the spot
     */
    public String getStringSpot() {
        return getLetter() + "" + getNumber();
    }

    /**
     * Get the id of parking id
     * @return the parking id
     */
    public int getParkingId() {
        return parkingId;
    }

    /**
     * Get the stored car
     * @return the stored car
     */
    public Car getCar() {
        return car;
    }

    /**
     * Get the spot letter
     * @return the spot letter
     */
    public char getLetter() {
        return letter;
    }

    /**
     * Get the spot number
     * @return the spot number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Set the new car in this spot
     * @param car the new car
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * Transform the Spot to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJSON() {
        return new JSONObject()
                .put("parkingId", this.parkingId)
                .put("car", this.car.toJson())
                .put("letter", this.letter)
                .put("number", this.number);
    }

    /**
     * Create a Spot from json
     *
     * @param json the json
     * @return the new Spot
     */
    public static Spot fromJSON(JSONObject json) {
        return new Spot(
                json.getInt("parkingId"),
                Car.fromJson(json.getJSONObject("car")),
                json.getString("letter").charAt(0),
                json.getInt("number")
        );
    }

    /**
     * Transform the spot object in string
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Spot [\n: "
                + "\n-car: " + getCar().toString()
                + "\n-spot: " + getLetter() + "" + getNumber()
                + "\n]";
    }
}
