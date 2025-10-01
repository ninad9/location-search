package com.example.locationsearch.model;

public class Location {
    private String zip;
    private String city;

    public Location(String zip, String city) {
        this.zip = zip;
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }
}
