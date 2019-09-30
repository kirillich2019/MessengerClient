package com.wg.messengerclient.models.server_answers.internalEntities;

public class FriendInfo {
    private int id;

    private String login;

    private String avaUrl;

    private String name, surname;

    public FriendInfo(int id, String login, String avaUrl, String name, String surname) {
        this.id = id;
        this.login = login;
        this.avaUrl = avaUrl;
        this.name = name;
        this.surname = surname;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
