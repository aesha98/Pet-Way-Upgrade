package com.example.petway.Model;

public class Vet {

    private String name;
    private String address;
    private String city;
    private String phoneNumber;
    private String placeID;
    private String ImageUrl;
    private String distance;
    private double latitude;
    private double longitude;

    public Vet() {
    }

    public Vet(String name, String address, String city, String phoneNumber, String placeID, String imageUrl, String distance, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.placeID = placeID;
        ImageUrl = imageUrl;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Vet(String name, String address, String phoneNumber) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
