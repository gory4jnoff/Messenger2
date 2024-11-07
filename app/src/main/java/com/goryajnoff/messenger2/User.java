package com.goryajnoff.messenger2;

import java.io.Serializable;

public class User implements Serializable {

    private String id;

    public User() {
    }

    public User(String id, boolean online, String name, String lastName, int age) {
        this.id = id;
        this.online = online;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    private boolean online;
    private String name;
    private String lastName;
    private int age;

    public String getId() {
        return id;
    }

    public boolean isOnline() {
        return online;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }
}
