package com.example.transparency.Database;

import java.io.Serializable;

public class Politicians implements Serializable {
    private String firstName;

    private String middleName;
    private String lastName;

    private String position;

    private String birthdate;
    private String address;

    private String contactNum;

    private String email;


    private String password;
    private String profile;

    private String priv;

    public Politicians()
    {

    }

    public Politicians(String firstName, String middleName, String lastName, String position, String birthdate, String address, String contactNum, String email, String password, String profile, String priv) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.position = position;
        this.birthdate = birthdate;
        this.address = address;
        this.contactNum = contactNum;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.priv = priv;
    }

    public String getPriv() {
        return priv;
    }

    public void setPriv(String priv) {
        this.priv = priv;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
