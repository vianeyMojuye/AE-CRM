package com.aecrm.Models;


public class UserModel {
    String Name;
    String Contact;
    String Email;
    String Role;
    String Picture;


   public UserModel(String Name, String Contact, String Email, String Role, String Picture)
    {
        this.Name =Name;
        this.Contact = Contact;
        this.Email = Email;
        this.Role = Role;
        this.Picture = Picture;

    }

    public UserModel(){}

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setContact(String Contact) {
        this.Contact = Contact;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public void setPicture(String Picture) {
        this.Picture = Picture;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getContact() {
        return Contact;
    }

    public String getPicture() {
        return Picture;
    }

    public String getRole() {
        return Role;
    }
}
