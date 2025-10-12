package com.example.locationsearch.model;

import java.util.List;


/**
 * Model class that represents a geographic location with basic information
 * */
public class Location {
    private String city;
    private List<String> zipCodes;
    private String state;
    private String country;

    public Location() {
    }

    public Location(String city, List<String> zipCodes, String state, String country) {
        this.city = city;
        this.zipCodes = zipCodes;
        this.state = state;
        this.country = country;
    }

    public Location(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getZipCodes() {
        return zipCodes;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", zipCodes=" + zipCodes +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
