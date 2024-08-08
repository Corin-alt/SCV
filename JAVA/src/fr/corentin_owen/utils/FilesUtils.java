package fr.corentin_owen.utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;


/**
 * Class {@link FilesUtils} which is a utility for files
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class FilesUtils {
    /**
     * Method to serialize a file
     *
     * @param directoryPath the path of file
     * @param fileName      the name of file
     * @param content       the content of file
     * @return success or no of serialization
     */
    public static boolean serializeJSONFile(String directoryPath, String fileName, JSONObject content) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        FileWriter writer;
        try {
            writer = new FileWriter(new File(directory, fileName + ".json"));
            writer.write(content.toString(2));
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to serialize a file
     *
     * @param path the path of file
     * @return the file deserialized
     */
    public static JSONObject deserializeJSONFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("The specified file does not exist: " + path);
        }
        if (!file.getName().endsWith(".json")) {
            throw new IllegalArgumentException("The specified file is not a JSON file: " + path);
        }
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            return new JSONObject(new String(content));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
