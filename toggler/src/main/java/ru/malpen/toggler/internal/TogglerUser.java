package ru.malpen.toggler.internal;

public class TogglerUser {
    private String id;
    private String userName;

    public String getId() {
        return id;
    }

    public TogglerUser setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public TogglerUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
