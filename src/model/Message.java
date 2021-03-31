package model;

import client.Client;

public class Message {
    public final MessageType type;
    public final int senderId;

    public Message(MessageType type) {
        this(type, Client.getInstance().getClientId());
    }

    public Message(MessageType type, int senderId) {
        this.type = type;
        this.senderId = senderId;
    }

    public String encode() {
        return "";
    }
}
