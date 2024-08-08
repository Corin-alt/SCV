package fr.corentin_owen.config;

import fr.corentin_owen.utils.FilesUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class {@link Config}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Config {

    /**
     * Attribute(s)
     */
    private static final JSONObject jsonConfigFile = FilesUtils.deserializeJSONFile("config/config.json");

    /**
     * Get the scenario
     *
     * @return the scenario
     */
    public static char getScenario() {
        char scenario = 'A';
        if (jsonConfigFile != null) {
            char get = jsonConfigFile.getString("scenario").charAt(0);
            if (get == 'B' || get == 'C') {
                scenario = get;
            }
        }
        return scenario;
    }

    /**
     * Get the configured port
     *
     * @param name   name of the port
     * @param socket type of the port
     * @return the port
     */
    public static int getPort(String name, TypePort socket) {
        int port = -1;
        String key = name + socket.toString();
        if (jsonConfigFile != null) {
            JSONObject ports = jsonConfigFile.getJSONObject("ports");
            if (ports != null) {
                try {
                    port = ports.getInt(key);
                } catch (JSONException e) {
                    ports.put(key, -1);
                    FilesUtils.serializeJSONFile("config/", "config", jsonConfigFile);
                }
            }
        }
        return port;
    }
}
