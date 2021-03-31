package model;

import client.Client;
import exceptions.IllegalMessageException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Message {
    private static final String TYPE_LABEL = "type",
            SENDER_ID_LABEL = "senderId";
    public final MessageType type;
    public final int senderId;

    public Message(MessageType type) {
        this(type, Client.getInstance().getClientId());
    }

    public Message(MessageType type, int senderId) {
        this.type = type;
        this.senderId = senderId;
    }

    public String serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TYPE_LABEL, type.toString());
        jsonObject.put(SENDER_ID_LABEL, senderId);
        return jsonObject.toJSONString();
    }

    public static Message deserialize(String s) throws ParseException, IllegalMessageException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(s);
        if (!jsonObject.containsKey(TYPE_LABEL) || ! jsonObject.containsKey(SENDER_ID_LABEL))
            throw new IllegalMessageException("Either type or senderId missing");
        Message msg = new Message(MessageType.valueOf((String) jsonObject.get(TYPE_LABEL)), ((Long) jsonObject.get(SENDER_ID_LABEL)).intValue());
        return msg;
    }
}
