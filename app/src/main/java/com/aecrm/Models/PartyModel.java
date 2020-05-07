package com.aecrm.Models;

public class PartyModel {

    String PartyId;
    String Party;
    String FirstOwner;
    String SecondOwner;
    String Contact ;
    String SContact;
    String Email;
    String Address1;
    String Address2;
    String City;
    String Latitude;
    String Longitude;
    String User;

    public PartyModel(){}

    public PartyModel(String Party,String Email)
    {
        this.Party = Party;
        this.Email = Email;
    }


    public String getPartyId() {
        return PartyId;
    }

    public String getAddress1() {
        return Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public String getCity() {
        return City;
    }

    public String getContact() {
        return Contact;
    }

    public String getEmail() {
        return Email;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getFirstOwner() {
        return FirstOwner;
    }

    public String getParty() {
        return Party;
    }

    public String getSContact() {
        return SContact;
    }

    public String getSecondOwner() {
        return SecondOwner;
    }

    public String getUser() {
        return User;
    }

    public void setSecondOwner(String secondOwner) {
        SecondOwner = secondOwner;
    }

    public void setSContact(String SContact) {
        this.SContact = SContact;
    }

    public void setParty(String party) {
        Party = party;
    }

    public void setFirstOwner(String firstOwner) {
        FirstOwner = firstOwner;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setPartyId(String partyId) {
        PartyId = partyId;
    }
}
