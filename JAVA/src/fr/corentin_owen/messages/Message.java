package fr.corentin_owen.messages;

import org.json.JSONObject;

/**
 * Class {@link Message} which are the exchange
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Message {

    /**
     * Attribute(s)
     */
    private final String id;
    private final String content;
    private final boolean bool;

    /**
     * Constructor by initialisation
     *
     * @param id      id of message
     * @param content content of message
     * @param bool    bool of message
     */
    public Message(String id, String content, boolean bool) {
        this.id = id;
        this.content = content;
        this.bool = bool;
    }

    /**
     * Constructor by initialisation
     *
     * @param id      id of message
     * @param content content of message
     */
    public Message(String id, String content) {
        this(id, content, false);
    }

    /**
     * Constructor by initialisation
     *
     * @param id   id of message
     * @param bool bool of message
     */
    public Message(String id, boolean bool) {
        this(id, "", bool);
    }


    /**
     * Transform the message to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJson() {
        return new JSONObject()
                .put("id", this.id)
                .put("bool", this.bool)
                .put("content", this.content);
    }

    /**
     * Create a Message from json
     *
     * @param jsonObject the json
     * @return the message
     */
    public static Message fromJson(JSONObject jsonObject) {
        return new Message(jsonObject.getString("id"), jsonObject.getString("content"), jsonObject.getBoolean("bool"));
    }

    /**
     * Get the id of message
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    public boolean getBool() {
        return bool;
    }

    /**
     * Get the content of message
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
