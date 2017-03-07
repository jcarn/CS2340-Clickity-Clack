package com.watro.clickityclack.watro.Model;

public class BasicUser extends SuperUser {

    protected String homeAddress;
    protected String firstName;
    protected String lastName;
    protected String userType;

    public BasicUser(String firstName, String lastName, String email, String id, String homeAddress, String userType) {
        super(email, id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String address) {
        this.homeAddress = address;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}