package com.wg.messengerclient.models.server_answers.internalEntities;

public class FriendRequest {
    private int id;

    private String login;

    private String avaUrl;

    public FriendRequest(int id, String login, String avaUrl) {
        this.id = id;
        this.login = login;
        this.avaUrl = avaUrl;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getAvaUrl() {
        return avaUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAvaUrl(String avaUrl) {
        this.avaUrl = avaUrl;
    }
}
