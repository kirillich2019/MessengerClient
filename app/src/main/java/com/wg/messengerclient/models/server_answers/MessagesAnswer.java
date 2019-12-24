package com.wg.messengerclient.models.server_answers;

import com.google.gson.annotations.SerializedName;

public class MessagesAnswer extends AnswerWithError {
    @SerializedName("id")
    private int id;

    @SerializedName("randomUUID")
    private String randomUUID;

    @SerializedName("text")
    private String text;

    @SerializedName("sender")
    private int sender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRandomUUID() {
        return randomUUID;
    }

    public void setRandomUUID(String randomUUID) {
        this.randomUUID = randomUUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}


