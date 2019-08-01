package com.sachin.testproject;
public class infoClass {
    private String regNo;
    private String distance;
    private String address;
    private String lastSeen;
    private String latitude;
    private String longitude;
    private String id;


    public String getLatitude() {
        return latitude;
    }


    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }


    public String getLongitude()
    {
        return longitude;
    }


    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }


    public String getId() {
        return id;
    }


    public  void setId(String id) {
        this.id = id;
    }


    public infoClass(){

    }
    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }



}
