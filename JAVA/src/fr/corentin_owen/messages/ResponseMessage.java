package fr.corentin_owen.messages;

import fr.corentin_owen.delivery.Deliverer;
import org.json.JSONObject;

/**
 * Class {@link Deliverer}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class ResponseMessage {

    /**
     * Attribute(s)
     */
    private final String idFunction;
    private final boolean error;
    private final String content;
    private int code;

    /**
     * Constructor by initialisation
     *
     * @param idFunction the id of function
     * @param error      error or not
     * @param content    the content of response
     */
    public ResponseMessage(String idFunction, boolean error, String content) {
        this.idFunction = idFunction;
        this.content = content;
        this.error = error;
        setCode(this.error);
    }

    /**
     * Set the code of response for http
     *
     * @param state the state
     */
    public void setCode(boolean state) {
        if (!state) {
            this.code = 200;
        } else {
            this.code = 400;
        }
    }

    /**
     * Transform the response message to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJson() {
        return new JSONObject()
                .put("idFunction", this.idFunction)
                .put("content", this.content)
                .put("code", this.code);
    }

    /**
     * Get the function of response
     *
     * @return the function of response
     */
    public String getIdFunction() {
        return idFunction;
    }

    /**
     * Get the error of response
     *
     * @return the error of response
     */
    public boolean isError() {
        return error;
    }

    /**
     * Get the content of response
     *
     * @return the content of response
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the code of response
     *
     * @return the code of response
     */
    public int getCode() {
        return code;
    }
}
