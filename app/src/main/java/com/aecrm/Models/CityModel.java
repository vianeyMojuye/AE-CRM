package com.aecrm.Models;

public class CityModel {

    String CityId;
    String Pincode;
    String City;
    String State;
    String Status;

    public CityModel(String CityId,  String Pincode,String City ,String State,String Status)
    {
        this.CityId= CityId;
        this.Pincode=Pincode;
        this.City = City;
        this.State=State;
        this.Status=Status;
    }

    public String getState() {
        return State;
    }

    public String getStatus() {
        return Status;
    }

    public String getPincode() {
        return Pincode;
    }

    public String getCity() {
        return City;
    }

    public String getCityId() {
        return CityId;
    }

    public void setState(String state) {
        State = state;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public void setCity(String city) {
        City = city;
    }
}
