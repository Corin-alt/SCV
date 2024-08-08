package fr.corentin_owen.parking;

import fr.corentin_owen.car.Car;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@link Parking}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Parking {

    /**
     * Attribute(s)
     */
    public char currentSpotLetter = 'a';
    public int currentSpotNumber = 0;

    private int id;
    private final int size;
    private List<Spot> spots;

    /**
     * Constructor by initialisation #1
     *
     * @param id   parking id
     * @param size size of parking (number of spot)
     */
    public Parking(int id, int size) {
        if (size <= 0 || size > 26) {
            throw new IllegalArgumentException("The size cannot be superior or equal to 0 OR inferior to 26!");
        }
        this.id = id;
        this.size = size;
        this.spots = new ArrayList<>();
    }

    /**
     * Constructor by initialisation #2
     *
     * @param id    parking id
     * @param size  size of parking (number of spot)
     * @param spots list of spot
     */
    public Parking(int id, int size, List<Spot> spots) {
        if (size <= 0 || size > 26) {
            throw new IllegalArgumentException("The size cannot be superior or equal to 0 AND inferior to 26!");
        }
        this.id = id;
        this.size = size;
        this.spots = spots;
    }

    /**
     * Get the parking id
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the size of parking
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the list of spots
     *
     * @return the list of spots
     */
    public List<Spot> getSpots() {
        return spots;
    }

    /**
     * Check is this parking is full
     *
     * @return true or false
     */
    public boolean isFull() {
        return this.spots.size() == this.size * this.size;
    }

    /**
     * Add car in this parking
     *
     * @param car the car
     * @return the spot where the car is stored
     */
    public Spot addCar(Car car) {
        Spot spot = new Spot(this.id, car, this.currentSpotLetter, this.currentSpotNumber);
        this.spots.add(spot);
        if (this.currentSpotNumber != this.size - 1) {
            this.currentSpotNumber++;
        } else {
            this.currentSpotLetter = (char) (this.currentSpotLetter + 1);
            this.currentSpotNumber = 0;
        }
        return spot;
    }

    /**
     * Transform the Parking to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJSON() {
        return new JSONObject()
                .put("id", this.id)
                .put("size", this.size)
                .put("spots", this.spots);
    }

    /**
     * Create a Parking from json
     *
     * @param json the json
     * @return the new Parking
     */
    public static Parking fromJSON(JSONObject json) {
        List<Spot> places = new ArrayList<>();

        for (int i = 0; i < json.getJSONArray("spots").length(); i++) {
            places.add(Spot.fromJSON(json.getJSONArray("spots").getJSONObject(i)));
        }

        return new Parking(json.getInt("id"), json.getInt("size"), places);
    }

    /**
     * Transform the parking object in string
     *
     * @return the string
     */
    @Override
    public String toString() {
        String str = "Parking " + getId()
                + " size = " + getSize() + "\n"
                + ":  \n";
        for (Spot pdp : getSpots()) {
            str += pdp.toString();
        }
        return str;
    }
}
