package com.wit.weather;

/**
 * Created by aribeiro on 17/8/14.
 */
public class City {

    private String name;
    private boolean subscribed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
