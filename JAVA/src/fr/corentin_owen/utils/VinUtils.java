package fr.corentin_owen.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Class {@link VinUtils} which is a utility to generate a VIN code
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class VinUtils {
    /**
     * Attribute(s)
     */
    private static final List<String> AUTHORIZED_VALUES = Arrays.asList(
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "A", "B", "C",
            "D", "E", "F", "G", "H", "J",
            "K", "L", "M", "N", "P", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    );

    /**
     * Method to generate a VIN code
     *
     * @return the VIN code
     */
    public static String generateCodeVin() {
        StringBuilder vin = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 19; i++) {
            if (i == 3 || i == 10) {
                vin.append("-");
            } else {
                vin.append(AUTHORIZED_VALUES.get(rand.nextInt(AUTHORIZED_VALUES.size())));
            }
        }
        return vin.toString();
    }
}
